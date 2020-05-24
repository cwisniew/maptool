/*
 * This software Copyright by the RPTools.net development team, and
 * licensed under the Affero GPL Version 3 or, at your option, any later
 * version.
 *
 * MapTool Source Code is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * You should have received a copy of the GNU Affero General Public
 * License * along with this source Code.  If not, please visit
 * <http://www.gnu.org/licenses/> and specifically the Affero license
 * text at <http://www.gnu.org/licenses/agpl.html>.
 */
package net.rptools.maptool.model;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import net.rptools.lib.MD5Key;
import net.rptools.maptool.client.AppPreferences;
import net.rptools.maptool.client.AppUtil;
import net.rptools.maptool.client.MapTool;
import net.rptools.maptool.client.tool.drawing.UndoPerZone;
import net.rptools.maptool.client.ui.MapToolFrame;
import net.rptools.maptool.client.ui.zone.PlayerView;
import net.rptools.maptool.client.ui.zone.ZoneRenderer;
import net.rptools.maptool.client.ui.zone.ZoneView;
import net.rptools.maptool.events.MapToolEventBus;
import net.rptools.maptool.events.initiative.InitiativeListReplacedEvent;
import net.rptools.maptool.events.zone.GridChangedEvent;
import net.rptools.maptool.language.I18N;
import net.rptools.maptool.model.InitiativeList.TokenInitiative;
import net.rptools.maptool.model.Token.TerrainModifierOperation;
import net.rptools.maptool.model.drawing.Drawable;
import net.rptools.maptool.model.drawing.DrawableColorPaint;
import net.rptools.maptool.model.drawing.DrawablePaint;
import net.rptools.maptool.model.drawing.DrawableTexturePaint;
import net.rptools.maptool.model.drawing.DrawablesGroup;
import net.rptools.maptool.model.drawing.DrawnElement;
import net.rptools.maptool.model.drawing.Pen;
import net.rptools.maptool.util.StringUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This object represents the maps that will appear for placement of {@link Token}s.
 *
 * <p>Note: When adding new fields to this class, make sure to add functionality to the constructor,
 * {@link #imported()}, {@link #optimize()}, and {@link #readResolve()} to ensure they are properly
 * initialized for maximum compatibility.
 */
public class Zone extends BaseModel {

  private static final Logger log = LogManager.getLogger(Zone.class);

  /** The vision type (OFF, DAY, NIGHT). */
  public enum VisionType {
    OFF,
    DAY,
    NIGHT
  }

  /** Event types for the listeners. */
  public enum Event {
    TOKEN_ADDED,
    TOKEN_REMOVED,
    TOKEN_CHANGED,
    //GRID_CHANGED,
    DRAWABLE_ADDED,
    DRAWABLE_REMOVED,
    FOG_CHANGED,
    LABEL_ADDED,
    LABEL_REMOVED,
    LABEL_CHANGED,
    TOPOLOGY_CHANGED,
    //INITIATIVE_LIST_CHANGED,
    BOARD_CHANGED,
    TOKEN_EDITED, // the token was edited
    TOKEN_MACRO_CHANGED, // a token macro changed
    TOKEN_PANEL_CHANGED // the panel appearance changed
  }

  /** The type of layer (TOKEN, GM, OBJECT or BACKGROUND). */
  public enum Layer {
    TOKEN("Token"),
    GM("Hidden"),
    OBJECT("Object"),
    BACKGROUND("Background");

    private String displayName;

    private Layer(String displayName) {
      this.displayName = displayName;
    }

    @Override
    public String toString() {
      return displayName;
    }

    /** A simple interface to allow layers to be turned on/off */
    private boolean drawEnabled = true;

    /** @return drawEnabled */
    public boolean isEnabled() {
      return drawEnabled;
    }

    public void setEnabled(boolean enabled) {
      drawEnabled = enabled;
    }
  }

  /** The selection type (PC, NPC, ALL, GM). */
  public enum TokenSelection {
    PC,
    NPC,
    ALL,
    GM
  }

  /** Control how A* Pathfinding distances is rounded off due to terrain costs */
  public enum AStarRoundingOptions {
    NONE,
    CELL_UNIT,
    INTEGER
  }

  // Control what topology layer(s) to add/get drawing to/from
  public enum TopologyMode {
    VBL,
    MBL,
    COMBINED
  }

  public static final int DEFAULT_TOKEN_VISION_DISTANCE = 250; // In units
  public static final int DEFAULT_PIXELS_CELL = 50;
  public static final int DEFAULT_UNITS_PER_CELL = 5;

  public static final DrawablePaint DEFAULT_FOG = new DrawableColorPaint(Color.black);

  // The zones should be ordered. We could have the server assign each zone
  // an incrementing number as new zones are created, but that would take a lot
  // more elegance than we really need. Instead, let's just keep track of the
  // time when it was created. This should give us sufficient granularity, because
  // seriously -- what's the likelihood of two GMs separately creating a new zone at exactly
  // the same millisecond since the epoch?
  private long creationTime = System.currentTimeMillis();

  private GUID id = new GUID(); // Ideally would be 'final', but that complicates imported()

  private Grid grid;
  private int gridColor = Color.black.getRGB();
  private float imageScaleX = 1;
  private float imageScaleY = 1;

  private int tokenVisionDistance = DEFAULT_TOKEN_VISION_DISTANCE;

  private double unitsPerCell = DEFAULT_UNITS_PER_CELL;
  private AStarRoundingOptions aStarRounding = AStarRoundingOptions.NONE;
  private TopologyMode topologyMode = null; // get default from AppPreferences

  private List<DrawnElement> drawables = new LinkedList<DrawnElement>();
  private List<DrawnElement> gmDrawables = new LinkedList<DrawnElement>();
  private List<DrawnElement> objectDrawables = new LinkedList<DrawnElement>();
  private List<DrawnElement> backgroundDrawables = new LinkedList<DrawnElement>();

  private final Map<GUID, Label> labels = new LinkedHashMap<GUID, Label>();
  /** Map each token GUID to the corresponding token. */
  private final Map<GUID, Token> tokenMap = new HashMap<GUID, Token>();
  /** Map each token GUID to its exposed area metadata */
  private Map<GUID, ExposedAreaMetaData> exposedAreaMeta = new HashMap<GUID, ExposedAreaMetaData>();

  /** Token list ordered by Z. */
  private final List<Token> tokenOrderedList = new LinkedList<Token>();

  private InitiativeList initiativeList = new InitiativeList(this);

  /** The global exposed area. */
  private Area exposedArea = new Area();

  private boolean hasFog;
  private DrawablePaint fogPaint;
  private transient UndoPerZone undo;

  /** The VBL topology of the zone. Does not include token VBL. */
  private Area topology = new Area();

  // New topology to hold Movement Blocking Only
  private Area topologyTerrain = new Area();

  // The 'board' layer, at the very bottom of the layer stack.
  // Itself has two sub-layers:
  // The top one is an optional texture, typically a pre-drawn map.
  // The bottom one is either an infinitely tiling texture or a color.
  private DrawablePaint backgroundPaint;
  private MD5Key mapAsset;
  private Point boardPosition = new Point(0, 0);
  private boolean drawBoard = true;
  private boolean boardChanged = false;

  // Lee: adding extra property to determine FoW exposure method
  // Jamz: Changed to transient to allow backwards compatibility of campaign
  // files & changed default to on
  private transient boolean exposeFogAtWaypoints = false;

  private String name;
  private boolean isVisible;

  /** The VisionType of the zone. OFF, DAY or NIGHT. */
  private VisionType visionType = VisionType.OFF;

  private TokenSelection tokenSelection = TokenSelection.ALL;

  // These are transitionary properties, very soon the width and height won't matter
  private int height;
  private int width;

  private transient HashMap<String, Integer> tokenNumberCache;

  /**
   * Note: When adding new fields to this class, make sure to update all constructors, {@link
   * #imported()}, {@link #readResolve()}, and potentially {@link #optimize()}.
   */
  public Zone() {
    // TODO: Was this needed?
    // setGrid(new SquareGrid());
    undo = new UndoPerZone(this); // registers as ModelChangeListener for drawables...
    addModelChangeListener(undo);
  }

  public void setBackgroundPaint(DrawablePaint paint) {
    backgroundPaint = paint;
  }

  public void setBackgroundAsset(MD5Key id) {}

  public MD5Key getBackgroundAsset() {
    return ((DrawableTexturePaint) getBackgroundPaint()).getAssetId();
  }

  public void setMapAsset(MD5Key id) {
    mapAsset = id;
    boardChanged = true;
  }

  public void setTokenVisionDistance(int units) {
    tokenVisionDistance = units;
  }

  public int getTokenVisionDistance() {
    return tokenVisionDistance;
  }

  public VisionType getVisionType() {
    return visionType;
  }

  public void setVisionType(VisionType visionType) {
    this.visionType = visionType;
  }

  public TokenSelection getTokenSelection() {
    return tokenSelection;
  }

  public void setTokenSelection(TokenSelection tokenSelection) {
    this.tokenSelection = tokenSelection;
  }

  /** @return the distance in map pixels at a 1:1 zoom */
  public int getTokenVisionInPixels() {
    if (tokenVisionDistance == 0) {
      // TODO: This is here to provide transition between pre 1.3b19 an 1.3b19. Remove later
      tokenVisionDistance = DEFAULT_TOKEN_VISION_DISTANCE;
    }
    return Double.valueOf(tokenVisionDistance * grid.getSize() / getUnitsPerCell()).intValue();
  }

  public void setFogPaint(DrawablePaint paint) {
    fogPaint = paint;
  }

  @Override
  public String toString() {
    return name;
  }

  /** @return name of the zone */
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public MD5Key getMapAssetId() {
    return mapAsset;
  }

  public DrawablePaint getBackgroundPaint() {
    return backgroundPaint;
  }

  public DrawablePaint getFogPaint() {
    return fogPaint != null ? fogPaint : DEFAULT_FOG;
  }

  /**
   * Note: When adding new fields to this class, make sure to update all constructors, {@link
   * #imported()}, {@link #readResolve()}, and potentially {@link #optimize()}.
   *
   * <p>JFJ 2010-10-27 Don't forget that since there are new zones AND new tokens created here from
   * the old one being passed in, if you have any data that needs to transfer over, you will need to
   * manually copy it as is done below for various items.
   */

  /**
   * Create a new zone with old zone's properties and with new token ids.
   *
   * @param zone The zone to copy.
   */
  public Zone(Zone zone) {
    this(zone, false);
  }

  /**
   * Create a new zone with old zone's properties.
   *
   * @param zone The zone to copy from.
   * @param keepIds Should the token ids stay the same.
   */
  public Zone(Zone zone, boolean keepIds) {
    backgroundPaint = zone.backgroundPaint;
    mapAsset = zone.mapAsset;
    fogPaint = zone.fogPaint;
    visionType = zone.visionType;

    undo = new UndoPerZone(this); // Undo/redo manager isn't copied
    addModelChangeListener(undo);
    setName(zone.getName());

    try {
      grid = (Grid) zone.grid.clone();
      grid.setZone(this);
    } catch (CloneNotSupportedException cnse) {
      MapTool.showError("Trying to copy the zone's grid; no grid assigned", cnse);
    }
    unitsPerCell = zone.unitsPerCell;
    tokenVisionDistance = zone.tokenVisionDistance;
    imageScaleX = zone.imageScaleX;
    imageScaleY = zone.imageScaleY;

    // In the following blocks we allocate a new linked list then fill it with null values
    // because the Collections.copy() method requires the destination list to already be
    // of a large enough size. I couldn't find any method that would copy individual
    // elements as it populated the new linked lists except those from the Apache Commons
    // library that use a Transformer and that seemed like a lot more work. :-/
    if (zone.drawables != null && !zone.drawables.isEmpty()) {
      drawables = new LinkedList<DrawnElement>();
      drawables.addAll(Collections.nCopies(zone.drawables.size(), (DrawnElement) null));
      Collections.copy(drawables, zone.drawables);
    }
    if (zone.objectDrawables != null && !zone.objectDrawables.isEmpty()) {
      objectDrawables = new LinkedList<DrawnElement>();
      objectDrawables.addAll(Collections.nCopies(zone.objectDrawables.size(), (DrawnElement) null));
      Collections.copy(objectDrawables, zone.objectDrawables);
    }
    if (zone.backgroundDrawables != null && !zone.backgroundDrawables.isEmpty()) {
      backgroundDrawables = new LinkedList<DrawnElement>();
      backgroundDrawables.addAll(
          Collections.nCopies(zone.backgroundDrawables.size(), (DrawnElement) null));
      Collections.copy(backgroundDrawables, zone.backgroundDrawables);
    }
    if (zone.gmDrawables != null && !zone.gmDrawables.isEmpty()) {
      gmDrawables = new LinkedList<DrawnElement>();
      gmDrawables.addAll(Collections.nCopies(zone.gmDrawables.size(), (DrawnElement) null));
      Collections.copy(gmDrawables, zone.gmDrawables);
    }
    if (zone.labels != null && !zone.labels.isEmpty()) {
      for (GUID guid : zone.labels.keySet()) {
        this.putLabel(new Label(zone.labels.get(guid)));
      }
    }
    exposedAreaMeta = new HashMap<GUID, ExposedAreaMetaData>(zone.exposedAreaMeta.size() * 4 / 3);

    // Copy the tokens, save a map between old and new for the initiative list.
    if (zone.initiativeList == null) {
      zone.initiativeList = new InitiativeList(zone);
    }
    Object[][] saveInitiative = new Object[zone.initiativeList.getSize()][2];
    initiativeList.setZone(null);

    if (zone.tokenMap != null && !zone.tokenMap.isEmpty()) {
      for (GUID oldGUID : zone.tokenMap.keySet()) {
        Token old = zone.tokenMap.get(oldGUID);
        Token token = new Token(old, keepIds); // keep old ids at server start
        if (old.getExposedAreaGUID() != null) {
          GUID guid = new GUID();
          token.setExposedAreaGUID(guid);
          // Update the TEA on the new map, since we have the Token object available...
          ExposedAreaMetaData eamd = zone.getExposedAreaMetaData(old.getExposedAreaGUID());
          if (eamd != null) {
            exposeArea(eamd.getExposedAreaHistory(), token);
          }
        }
        putToken(token);
        List<Integer> list = zone.initiativeList.indexOf(old);
        for (Integer index : list) {
          saveInitiative[index][0] = token;
          saveInitiative[index][1] = zone.initiativeList.getTokenInitiative(index);
        }
      }
    }
    // Set the initiative list using the newly create tokens.
    if (saveInitiative.length > 0) {
      for (int i = 0; i < saveInitiative.length; i++) {
        Token token = (Token) saveInitiative[i][0];
        initiativeList.insertToken(i, token);
        TokenInitiative ti = initiativeList.getTokenInitiative(i);
        TokenInitiative oldti = (TokenInitiative) saveInitiative[i][1];
        ti.setHolding(oldti.isHolding());
        ti.setState(oldti.getState());
      }
    }
    initiativeList.setZone(this);
    initiativeList.setCurrent(zone.initiativeList.getCurrent());
    initiativeList.setRound(zone.initiativeList.getRound());
    initiativeList.setHideNPC(zone.initiativeList.isHideNPC());

    boardPosition = (Point) zone.boardPosition.clone();
    exposedArea = (Area) zone.exposedArea.clone();
    topology = (Area) zone.topology.clone();
    topologyTerrain = (Area) zone.topologyTerrain.clone();
    aStarRounding = zone.aStarRounding;
    topologyMode = zone.topologyMode;
    isVisible = zone.isVisible;
    hasFog = zone.hasFog;
  }

  public GUID getId() {
    return id;
  }

  /**
   * Should be invoked only when a Zone has been imported from an external source and needs to be
   * cleaned up before being used. Currently this cleanup consists of allocating a new GUID, setting
   * the creation time to `now', and resetting the initiative list (setting the related zone and
   * clearing the model).
   */
  public void imported() {
    id = new GUID();
    creationTime = System.currentTimeMillis();
    initiativeList.setZone(this);
    initiativeList.clearModel();
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public boolean isVisible() {
    return isVisible;
  }

  public void setVisible(boolean isVisible) {
    this.isVisible = isVisible;
  }

  public void setGrid(Grid grid) {
    this.grid = grid;
    grid.setZone(this);
    // tokenVisionDistance = DEFAULT_TOKEN_VISION_DISTANCE * grid.getSize() / unitsPerCell;
    new MapToolEventBus().getMainEventBus().post(new GridChangedEvent(grid, this));
  }

  public Grid getGrid() {
    return grid;
  }

  public int getGridColor() {
    return gridColor;
  }

  public void setGridColor(int color) {
    gridColor = color;
  }

  /**
   * Board pseudo-object. Not making full object since this will change when new layer model is
   * created
   *
   * @return has the board changed?
   */
  public boolean isBoardChanged() {
    return boardChanged;
  }

  public void setBoardChanged(boolean set) {
    boardChanged = set;
  }

  public void setBoard(Point position) {
    boardPosition.x = position.x;
    boardPosition.y = position.y;
    setBoardChanged(true);
    fireModelChangeEvent(new ModelChangeEvent(mapAsset, Event.BOARD_CHANGED));
  }

  public void setBoard(int newX, int newY) {
    boardPosition.x = newX;
    boardPosition.y = newY;
    setBoardChanged(true);
    fireModelChangeEvent(new ModelChangeEvent(mapAsset, Event.BOARD_CHANGED));
  }

  public void setBoard(Point position, MD5Key asset) {
    this.setMapAsset(asset);
    this.setBoard(position);
  }

  public int getBoardX() {
    return boardPosition.x;
  }

  public int getBoardY() {
    return boardPosition.y;
  }

  public boolean drawBoard() {
    return drawBoard;
  }

  public void setDrawBoard(boolean draw) {
    drawBoard = draw;
  }

  //
  // Misc Scale methods
  //

  public float getImageScaleX() {
    return imageScaleX;
  }

  public void setImageScaleX(float imageScaleX) {
    this.imageScaleX = imageScaleX;
  }

  public float getImageScaleY() {
    return imageScaleY;
  }

  public void setImageScaleY(float imageScaleY) {
    this.imageScaleY = imageScaleY;
  }

  //
  // Fog
  //
  public boolean hasFog() {
    return hasFog;
  }

  public void setHasFog(boolean flag) {
    hasFog = flag;
    fireModelChangeEvent(new ModelChangeEvent(this, Event.FOG_CHANGED));
  }

  /**
   * Determines whether the given ZonePoint is visible when using the specified PlayerView. This
   * currently includes checking the following criteria:
   *
   * <ol>
   *   <li>If fog is turned off, return true.
   *   <li>If the view is a GM view, return true.
   *   <li>If Vision is <b>Day</b> or <b>Night</b> and we're not using IndividualFOW, return
   *       intersection of point with exposedArea.
   *   <li>If Vision is off or we ARE using IndividualFOW, combine exposed areas of all owned tokens
   *       (with HasSight==true) and return intersection of point with the combined area.
   * </ol>
   *
   * @param point the ZonePoint to test.
   * @param view the PlayerView.
   * @return is the point visible?
   */
  public boolean isPointVisible(ZonePoint point, PlayerView view) {
    if (!hasFog() || view.isGMView()) {
      return true;
    }
    if (MapTool.getServerPolicy().isUseIndividualFOW() && getVisionType() != VisionType.OFF) {
      Area combined = new Area(exposedArea);
      List<Token> toks = view.getTokens(); // only owned and HasSight tokens are returned
      if (toks != null && !toks.isEmpty()) {
        for (Token tok : toks) {
          ExposedAreaMetaData meta = exposedAreaMeta.get(tok.getExposedAreaGUID());
          if (meta != null) {
            combined.add(meta.getExposedAreaHistory());
          }
        }
      }
      return combined.contains(point.x, point.y);
    } else {
      return exposedArea.contains(point.x, point.y);
    }
  }

  public boolean isEmpty() {
    // @formatter:off
    return (drawables == null || drawables.isEmpty())
        && (gmDrawables == null || gmDrawables.isEmpty())
        && (objectDrawables == null || objectDrawables.isEmpty())
        && (backgroundDrawables == null || backgroundDrawables.isEmpty())
        && (tokenOrderedList == null || tokenOrderedList.isEmpty())
        && (labels == null || labels.isEmpty());
    // @formatter:on
  }

  /**
   * Determines if the passed non-<code>null</code> parameter represents a visible token. The
   * current criteria works like this:
   *
   * <ol>
   *   <li>If the <i>Visible to Players</i> flag is off, return <code>false</code>.
   *   <li>If the fog-of-war for the map is off, return <code>true</code>.
   *   <li>If the player does not own the token and it's <i>Visible to Owner Only</i>, return <code>
   *       false</code>.
   *   <li>If the token's bounds intersect the exposed area for this map, return <code>true</code>.
   * </ol>
   *
   * @param token the token to test.
   * @return is the token visible?
   */
  public boolean isTokenVisible(Token token) {
    if (token == null) {
      return false;
    }

    // Base case, nothing is visible
    if (!token.isVisible()) {
      return false;
    }

    // Base case, everything is visible
    if (!hasFog()) {
      return true;
    }

    // Token is visible to owner only and client isn't that player
    if (token.isVisibleOnlyToOwner() && !AppUtil.playerOwns(token)) {
      return false;
    }

    // Token is visible, and there is fog
    Rectangle tokenSize = token.getBounds(this);
    Area combined = new Area(exposedArea);
    PlayerView view = MapTool.getFrame().getZoneRenderer(this).getPlayerView();
    if (MapTool.getServerPolicy().isUseIndividualFOW() && getVisionType() != VisionType.OFF) {
      List<Token> toks = view.getTokens();

      // Jamz: Lets change the logic a bit looking for ownerships
      if (toks != null && !toks.isEmpty()) {
        for (Token tok : toks) {
          if (!AppUtil.playerOwns(tok)) {
            continue;
          }
          if (exposedAreaMeta.containsKey(tok.getExposedAreaGUID())) {
            combined.add(exposedAreaMeta.get(tok.getExposedAreaGUID()).getExposedAreaHistory());
          }
        }
      }
    }
    return combined.intersects(tokenSize);
  }

  public boolean isTokenFootprintVisible(Token token) {
    if (token == null) {
      return false;
    }
    // Base case, nothing is visible
    if (!token.isVisible()) {
      return false;
    }
    // Base case, everything is visible
    if (!hasFog()) {
      return true;
    }
    if (token.isVisibleOnlyToOwner() && !AppUtil.playerOwns(token)) {
      return false;
    }
    // Token is visible, and there is fog
    Rectangle tokenSize = token.getBounds(this);
    Area tokenFootprint = getGrid().getTokenCellArea(tokenSize);
    Area combined = new Area(exposedArea);
    PlayerView view = MapTool.getFrame().getZoneRenderer(this).getPlayerView();
    if (MapTool.getServerPolicy().isUseIndividualFOW() && getVisionType() != VisionType.OFF) {
      List<Token> toks = view.getTokens();
      if (toks != null && !toks.isEmpty()) {
        // Should this use FindTokenFunctions.OwnedFilter and zone.getTokenList()?
        for (Token tok : toks) {
          if (!AppUtil.playerOwns(tok)) {
            continue;
          }
          if (exposedAreaMeta.containsKey(tok.getExposedAreaGUID())) {
            combined.add(exposedAreaMeta.get(tok.getExposedAreaGUID()).getExposedAreaHistory());
          }
        }
      }
    }
    combined.intersect(tokenFootprint);
    return !combined.isEmpty();
    // return combined.intersects(tokenSize);
  }

  public void clearTopology() {
    topology = new Area();
    fireModelChangeEvent(new ModelChangeEvent(this, Event.TOPOLOGY_CHANGED));
  }

  /**
   * Add the area to the topology, and fire the event TOPOLOGY_CHANGED
   *
   * @param area the area
   * @param topologyMode the mode of the topology
   */
  public void addTopology(Area area, TopologyMode topologyMode) {
    switch (topologyMode) {
      case VBL:
        getTopology().add(area);
        break;
      case MBL:
        getTopologyTerrain().add(area);
        break;
      case COMBINED:
        getTopology().add(area);
        getTopologyTerrain().add(area);
        break;
    }

    fireModelChangeEvent(new ModelChangeEvent(this, Event.TOPOLOGY_CHANGED));
  }

  public void addTopology(Area area) {
    addTopology(area, getTopologyMode());
  }

  /**
   * Subtract the area from the topology, and fire the event TOPOLOGY_CHANGED
   *
   * @param area the area
   * @param topologyMode the mode of the topology
   */
  public void removeTopology(Area area, TopologyMode topologyMode) {
    switch (topologyMode) {
      case VBL:
        getTopology().subtract(area);
        break;
      case MBL:
        getTopologyTerrain().subtract(area);
        break;
      case COMBINED:
        getTopology().subtract(area);
        getTopologyTerrain().subtract(area);
        break;
    }

    fireModelChangeEvent(new ModelChangeEvent(this, Event.TOPOLOGY_CHANGED));
  }

  public void removeTopology(Area area) {
    removeTopology(area, getTopologyMode());
  }

  /** Fire the event TOPOLOGY_CHANGED. */
  public void tokenTopologyChanged() {
    fireModelChangeEvent(new ModelChangeEvent(this, Event.TOPOLOGY_CHANGED));
  }

  /** @return the topology of the zone */
  public Area getTopology() {
    return topology;
  }

  /** @return the terrain topology of the zone */
  public Area getTopologyTerrain() {
    return topologyTerrain;
  }

  /**
   * Fire the event TOKEN_CHANGED
   *
   * @param token the token that changed
   */
  public void tokenChanged(Token token) {
    fireModelChangeEvent(new ModelChangeEvent(this, Event.TOKEN_CHANGED, token));
  }

  /**
   * Fire the event TOKEN_MACRO_CHANGED.
   *
   * @param token the token that had its macro changed
   */
  public void tokenMacroChanged(Token token) {
    fireModelChangeEvent(new ModelChangeEvent(this, Event.TOKEN_MACRO_CHANGED, token));
  }

  /**
   * Fire the event TOKEN_PANEL_CHANGED.
   *
   * @param token the token that had its panel appearance changed
   */
  public void tokenPanelChanged(Token token) {
    fireModelChangeEvent(new ModelChangeEvent(this, Event.TOKEN_PANEL_CHANGED, token));
  }

  /**
   * Clears the global exposed area. Can also clear the exposed area for ALL tokens, including NPC's
   *
   * @param globalOnly should the exposed area of all tokens be also cleared?
   */
  public void clearExposedArea(boolean globalOnly) {
    exposedArea = new Area();
    if (!globalOnly) {
      exposedAreaMeta.clear();
    }
    fireModelChangeEvent(new ModelChangeEvent(this, Event.FOG_CHANGED));
  }

  /**
   * Clear only exposed area for tokenSet, eg only PC's. Updates the server and other clients.
   *
   * @param tokenSet the set of token GUID to reset
   */
  public void clearExposedArea(Set<GUID> tokenSet) {
    // Jamz: Clear FoW for set tokens only, for use by
    // ExposeVisibleAreaOnlyAction Menu action and exposePCOnlyArea() macro

    for (GUID tea : tokenSet) {
      ExposedAreaMetaData meta = getExposedAreaMetaData(tea);
      if (meta != null) {
        meta.clearExposedAreaHistory();
        Token token = getToken(tea);
        ZoneRenderer zr = MapTool.getFrame().getZoneRenderer(this.getId());
        zr.flush(token);
        setExposedAreaMetaData(token.getExposedAreaGUID(), meta);
        MapTool.serverCommand().updateExposedAreaMeta(getId(), token.getExposedAreaGUID(), meta);
      }
    }

    fireModelChangeEvent(new ModelChangeEvent(this, Event.FOG_CHANGED));
  }

  /**
   * Expose a FoW area. Add the exposed area to the metadata of the token. If tok is set to null or
   * the settings disable individual FoW, instead add it to the general exposedArea.
   *
   * @param area the area to expose
   * @param tok the token to expose for, or null
   */
  public void exposeArea(Area area, Token tok) {
    if (area == null || area.isEmpty()) {
      return;
    }
    if (tok != null) {
      if (MapTool.isPersonalServer()
          || (MapTool.getServerPolicy().isUseIndividualFOW() && AppUtil.playerOwns(tok))) {
        GUID tea = tok.getExposedAreaGUID();
        ExposedAreaMetaData meta = exposedAreaMeta.get(tea);
        if (meta == null) {
          meta = new ExposedAreaMetaData();
          exposedAreaMeta.put(tea, meta);
        }
        meta.addToExposedAreaHistory(area);
        ZoneRenderer zr = MapTool.getFrame().getZoneRenderer(this.getId());
        if (zr != null) // Could be null if the AutoSaveManager is saving the campaign by copying
        // Zones, but not
        // ZoneRenderers
        {
          zr.getZoneView().flush();
        }
        putToken(tok);
        fireModelChangeEvent(new ModelChangeEvent(this, Event.FOG_CHANGED));
        return; // FJE Added so that TEA isn't added to the GEA, below.
      }
    }
    exposedArea.add(area);
    fireModelChangeEvent(new ModelChangeEvent(this, Event.FOG_CHANGED));
  }

  /**
   * Retrieves the selected tokens and adds the passed in area to their exposed area. (Why are we
   * passing in a {@code Set<GUID>} when {@code Set<Token>} would be much more efficient?)
   *
   * @param area the area to expose
   * @param selectedToks the set GUID of selected tokens
   */
  public void exposeArea(Area area, Set<GUID> selectedToks) {
    if (area == null || area.isEmpty()) {
      return;
    }
    if (getVisionType() == VisionType.OFF) {
      // Why is this done here and then again below???
      // And just because Vision==Off doesn't mean we aren't doing IF...
      // Jamz: if this exposedArea isn't done then it breaks getExposedTokens when vision is off...
      exposedArea.add(area);
    }
    if (selectedToks != null
        && !selectedToks.isEmpty()
        && (MapTool.getServerPolicy().isUseIndividualFOW() || MapTool.isPersonalServer())) {
      boolean isAllowed =
          MapTool.getPlayer().isGM() || !MapTool.getServerPolicy().useStrictTokenManagement();
      String playerId = MapTool.getPlayer().getName();
      MapToolFrame frame = MapTool.getFrame();
      // FIXME 'zr' was null -- how can this happen? Fix is to use getId() instead of 'this'
      ZoneRenderer zr = frame.getZoneRenderer(getId());
      ZoneView zoneView = zr.getZoneView();
      ExposedAreaMetaData meta = null;

      for (GUID guid : selectedToks) {
        Token tok = getToken(guid);
        if (tok == null) {
          continue;
        }
        if ((isAllowed || tok.isOwner(playerId)) && tok.getHasSight()) {
          GUID tea = tok.getExposedAreaGUID();
          meta = exposedAreaMeta.get(tea);
          if (meta == null) {
            meta = new ExposedAreaMetaData();
            exposedAreaMeta.put(tea, meta);
          }
          meta.addToExposedAreaHistory(area);
        }
      }
    } else {
      // Not using IF so add the EA to the GEA instead of a TEA.
      exposedArea.add(area);
    }
    fireModelChangeEvent(new ModelChangeEvent(this, Event.FOG_CHANGED));
  }

  /**
   * Modifies the global exposed area (GEA) or token exposed by resetting it and then setting it to
   * the contents of the passed in Area and firing a ModelChangeEvent.
   *
   * @param area the area to expose
   * @param selectedToks the selected tokens
   */
  public void setFogArea(Area area, Set<GUID> selectedToks) {
    if (area == null) {
      return;
    }
    if (selectedToks != null && !selectedToks.isEmpty()) {
      List<Token> allToks = new ArrayList<Token>();

      for (GUID guid : selectedToks) {
        allToks.add(getToken(guid));
      }
      for (Token tok : allToks) {
        if (!tok.getHasSight()) {
          continue;
        }
        ExposedAreaMetaData meta = exposedAreaMeta.get(tok.getExposedAreaGUID());
        if (meta == null) {
          meta = new ExposedAreaMetaData();
        }
        meta.clearExposedAreaHistory();
        meta.addToExposedAreaHistory(area);
        exposedAreaMeta.put(tok.getExposedAreaGUID(), meta);
        MapTool.getFrame().getZoneRenderer(this.getId()).getZoneView().flush(tok);
        putToken(tok);
      }
    } else {
      exposedArea.reset();
      exposedArea.add(area);
    }
    fireModelChangeEvent(new ModelChangeEvent(this, Event.FOG_CHANGED));
  }

  public void hideArea(Area area, Set<GUID> selectedToks) {
    if (area == null) {
      return;
    }
    if (getVisionType() == VisionType.OFF) {
      exposedArea.subtract(area);
    }
    if (selectedToks != null
        && !selectedToks.isEmpty()
        && (MapTool.getServerPolicy().isUseIndividualFOW() || MapTool.isPersonalServer())) {
      List<Token> allToks = new ArrayList<Token>();

      for (GUID guid : selectedToks) {
        allToks.add(getToken(guid));
      }
      for (Token tok : allToks) {
        if (!AppUtil.playerOwns(tok)) {
          continue;
        }
        if (!tok.getHasSight()) {
          continue;
        }
        ExposedAreaMetaData meta = exposedAreaMeta.get(tok.getExposedAreaGUID());
        if (meta == null) {
          meta = new ExposedAreaMetaData();
        }
        meta.removeExposedAreaHistory(area);
        exposedAreaMeta.put(tok.getExposedAreaGUID(), meta);
        MapTool.getFrame().getZoneRenderer(this.getId()).getZoneView().flush(tok);
        putToken(tok);
      }
    } else {
      exposedArea.subtract(area);
    }
    fireModelChangeEvent(new ModelChangeEvent(this, Event.FOG_CHANGED));
  }

  public long getCreationTime() {
    return creationTime;
  }

  public ZonePoint getNearestVertex(ZonePoint point) {
    return grid.getNearestVertex(point);
  }

  /**
   * Returns the Area of the exposed fog for the current tokens (as determined by view.getTokens()).
   * This means if no tokens are current, the return value is the zone's global exposed fog area. If
   * tokens are returned by getTokens(), their exposed areas are added to the zone's global area and
   * the result is returned.
   *
   * @param view holds whether or not tokens are selected
   * @return the exposed area
   */
  public Area getExposedArea(PlayerView view) {
    Area combined = new Area(exposedArea);

    List<Token> toks = view.getTokens();
    // Don't need to worry about StrictTokenOwnership since the PlayerView only contains tokens we
    // own by calling
    // AppUtil.playerOwns()
    if (toks == null || toks.isEmpty()) {
      return combined;
    }
    for (Token tok : toks) {
      // Don't need this IF statement; see
      // net.rptools.maptool.client.ui.zone.ZoneRenderer.getPlayerView(Role)
      // if (!tok.getHasSight() || !AppUtil.playerOwns(tok)) {
      // continue;
      // }
      ExposedAreaMetaData meta = exposedAreaMeta.get(tok.getExposedAreaGUID());
      if (meta != null) {
        combined.add(meta.getExposedAreaHistory());
      }
    }
    return combined;
  }

  /**
   * This is the Global Exposed Area (GEA) discussed so much on the dev-team mailing list. :)
   *
   * @return Area object representing exposed fog area visible to all tokens
   */
  public Area getExposedArea() {
    return exposedArea;
  }

  public double getUnitsPerCell() {
    return Math.max(unitsPerCell, 0);
  }

  public void setUnitsPerCell(double unitsPerCell) {
    this.unitsPerCell = unitsPerCell;
  }

  public AStarRoundingOptions getAStarRounding() {
    if (aStarRounding == null) {
      aStarRounding = AStarRoundingOptions.NONE;
    }

    return aStarRounding;
  }

  public void setAStarRounding(AStarRoundingOptions aStarRounding) {
    this.aStarRounding = aStarRounding;
  }

  public TopologyMode getTopologyMode() {
    if (topologyMode == null) {
      topologyMode = AppPreferences.getTopologyDrawingMode();
    }

    return topologyMode;
  }

  public void setTopologyMode(TopologyMode topologyMode) {
    this.topologyMode = topologyMode;
  }

  public int getLargestZOrder() {
    return tokenOrderedList.size() > 0
        ? tokenOrderedList.get(tokenOrderedList.size() - 1).getZOrder()
        : 0;
  }

  public int getSmallestZOrder() {
    return tokenOrderedList.size() > 0 ? tokenOrderedList.get(0).getZOrder() : 0;
  }

  /** Sort the tokens by their ZOrder */
  public void sortZOrder() {
    Collections.sort(tokenOrderedList, TOKEN_Z_ORDER_COMPARATOR);
  }

  ///////////////////////////////////////////////////////////////////////////
  // labels
  ///////////////////////////////////////////////////////////////////////////
  public void putLabel(Label label) {
    boolean newLabel = labels.containsKey(label.getId());
    labels.put(label.getId(), label);

    if (newLabel) {
      fireModelChangeEvent(new ModelChangeEvent(this, Event.LABEL_ADDED, label));
    } else {
      fireModelChangeEvent(new ModelChangeEvent(this, Event.LABEL_CHANGED, label));
    }
  }

  public List<Label> getLabels() {
    return new ArrayList<Label>(this.labels.values());
  }

  public void removeLabel(GUID labelId) {
    Label label = labels.remove(labelId);
    if (label != null) {
      fireModelChangeEvent(new ModelChangeEvent(this, Event.LABEL_REMOVED, label));
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // drawables
  ///////////////////////////////////////////////////////////////////////////

  public void addDrawable(DrawnElement drawnElement) {
    switch (drawnElement.getDrawable().getLayer()) {
      case OBJECT:
        objectDrawables.add(drawnElement);
        break;
      case BACKGROUND:
        backgroundDrawables.add(drawnElement);
        break;
      case GM:
        gmDrawables.add(drawnElement);
        break;
      default:
        drawables.add(drawnElement);
    }
    fireModelChangeEvent(new ModelChangeEvent(this, Event.DRAWABLE_ADDED, drawnElement));
  }

  public void updateDrawable(DrawnElement drawnElement, Pen pen) {
    if (drawnElement.getDrawable().getLayer() == Layer.OBJECT) {
      updatePen(objectDrawables, drawnElement, pen);
    } else if (drawnElement.getDrawable().getLayer() == Layer.BACKGROUND) {
      updatePen(backgroundDrawables, drawnElement, pen);
    } else if (drawnElement.getDrawable().getLayer() == Layer.GM) {
      updatePen(gmDrawables, drawnElement, pen);
    } else {
      updatePen(drawables, drawnElement, pen);
    }
    fireModelChangeEvent(new ModelChangeEvent(this, Event.DRAWABLE_ADDED, drawnElement));
  }

  private void updatePen(List<DrawnElement> elementList, DrawnElement drawnElement, Pen pen) {
    for (DrawnElement de : elementList) {
      if (de.getDrawable().getId().equals(drawnElement.getDrawable().getId())) {
        de.setPen(new Pen(pen));
        break;
      }
    }
  }

  public void addDrawableRear(DrawnElement drawnElement) {
    // Since the list is drawn in order
    // items that are drawn first are at the "back"
    switch (drawnElement.getDrawable().getLayer()) {
      case OBJECT:
        ((LinkedList<DrawnElement>) objectDrawables).addFirst(drawnElement);
        break;
      case BACKGROUND:
        ((LinkedList<DrawnElement>) backgroundDrawables).addFirst(drawnElement);
        break;
      case GM:
        ((LinkedList<DrawnElement>) gmDrawables).addFirst(drawnElement);
        break;
      default:
        ((LinkedList<DrawnElement>) drawables).addFirst(drawnElement);
    }
    fireModelChangeEvent(new ModelChangeEvent(this, Event.DRAWABLE_ADDED, drawnElement));
  }

  public List<DrawnElement> getDrawnElements() {
    return getDrawnElements(Zone.Layer.TOKEN);
  }

  public List<DrawnElement> getObjectDrawnElements() {
    return getDrawnElements(Zone.Layer.OBJECT);
  }

  public List<DrawnElement> getGMDrawnElements() {
    return getDrawnElements(Zone.Layer.GM);
  }

  public List<DrawnElement> getBackgroundDrawnElements() {
    return getDrawnElements(Zone.Layer.BACKGROUND);
  }

  public List<DrawnElement> getDrawnElements(Zone.Layer layer) {
    switch (layer) {
      case OBJECT:
        return objectDrawables;
      case GM:
        return gmDrawables;
      case BACKGROUND:
        return backgroundDrawables;
      default:
        return drawables;
    }
  }

  public void removeDrawable(GUID drawableId) {
    // Since we don't know anything about the drawable, look through all the layers
    // Do we need to remove it from the Undo manager as well? Probably. Perhaps some
    // UndoPerZone method that searches and deletes the drawable ID?
    removeDrawable(drawables, drawableId);
    removeDrawable(backgroundDrawables, drawableId);
    removeDrawable(objectDrawables, drawableId);
    removeDrawable(gmDrawables, drawableId);
  }

  private void removeDrawable(List<DrawnElement> drawableList, GUID drawableId) {
    ListIterator<DrawnElement> i = drawableList.listIterator();
    while (i.hasNext()) {
      DrawnElement drawable = i.next();
      if (drawable.getDrawable().getId().equals(drawableId)) {
        i.remove();
        fireModelChangeEvent(new ModelChangeEvent(this, Event.DRAWABLE_REMOVED, drawable));
        return;
      }
      if (drawable.getDrawable() instanceof DrawablesGroup) {
        DrawablesGroup dg = (DrawablesGroup) drawable.getDrawable();
        removeDrawable(dg.getDrawableList(), drawableId);
      }
    }
  }

  public void clearDrawables(List<DrawnElement> drawableList) {
    ListIterator<DrawnElement> i = drawableList.listIterator();
    while (i.hasNext()) {
      DrawnElement drawable = i.next();
      fireModelChangeEvent(new ModelChangeEvent(this, Event.DRAWABLE_REMOVED, drawable));
    }
    drawableList.clear();
    undo.clear(); // clears the *entire* undo queue, but finer grained control isn't available
  }

  public void addDrawable(Pen pen, Drawable drawable) {
    undo.addDrawable(pen, drawable);
  }

  public boolean canUndo() {
    return undo.canUndo();
  }

  public void undoDrawable() {
    undo.undo();
  }

  public boolean canRedo() {
    return undo.canRedo();
  }

  public void redoDrawable() {
    undo.redo();
  }

  ///////////////////////////////////////////////////////////////////////////
  // tokens
  ///////////////////////////////////////////////////////////////////////////

  /**
   * Adds the specified Token to this zone, accounting for updating the ordered list of tokens as
   * well as firing the appropriate <code>ModelChangeEvent</code> (either <code>Event.TOKEN_ADDED
   * </code> or <code>Event.TOKEN_CHANGED</code>).
   *
   * @param token the Token to be added to this zone
   */
  public void putToken(Token token) {
    boolean newToken = !tokenMap.containsKey(token.getId());

    tokenMap.put(token.getId(), token);

    // LATER: optimize this
    tokenOrderedList.remove(token);
    tokenOrderedList.add(token);
    tokenOrderedList.sort(TOKEN_Z_ORDER_COMPARATOR);

    if (newToken) {
      fireModelChangeEvent(new ModelChangeEvent(this, Event.TOKEN_ADDED, token));
    } else {
      fireModelChangeEvent(new ModelChangeEvent(this, Event.TOKEN_CHANGED, token));
    }
  }

  /**
   * Put the token, and also fires the token edited event.
   *
   * @param token the token that was edited
   */
  public void editToken(Token token) {
    putToken(token);
    fireModelChangeEvent(new ModelChangeEvent(this, Event.TOKEN_EDITED, token));
  }
  /**
   * Same as {@link #putToken(Token)} but optimizes map updates by accepting a list of Tokens. Note
   * that this method fires a single <code>ModelChangeEvent</code> using <code> Event.TOKEN_ADDED
   * </code> and passes the list of added tokens as a parameter. Ditto for <code>Event.TOKEN_CHANGED
   * </code>.
   *
   * <p>Not currently invoked by other code, but event handling changes for multiple tokens has been
   * made. Marked as deprecated to prevent use until the rest of the integration is completed.
   *
   * @param tokens List of Tokens to be added to this zone
   */
  @Deprecated
  public void putTokens(List<Token> tokens) {
    // System.out.println("putToken() called with list of " + tokens.size() + " tokens.");

    Collection<Token> values = tokenMap.values();

    List<Token> addedTokens = new LinkedList<Token>(tokens);
    addedTokens.removeAll(values);

    List<Token> changedTokens = new LinkedList<Token>(tokens);
    changedTokens.retainAll(values);

    for (Token t : tokens) {
      tokenMap.put(t.getId(), t);
    }
    tokenOrderedList.removeAll(tokens);
    tokenOrderedList.addAll(tokens);
    tokenOrderedList.sort(TOKEN_Z_ORDER_COMPARATOR);

    if (!addedTokens.isEmpty()) {
      fireModelChangeEvent(new ModelChangeEvent(this, Event.TOKEN_ADDED, addedTokens));
    }
    if (!changedTokens.isEmpty()) {
      fireModelChangeEvent(new ModelChangeEvent(this, Event.TOKEN_CHANGED, changedTokens));
    }
  }

  /**
   * Removes a token, and fires Event.TOKEN_REMOVED.
   *
   * @param id the id of the token
   */
  public void removeToken(GUID id) {
    Token token = tokenMap.remove(id);
    if (token != null) {
      tokenOrderedList.remove(token);
      fireModelChangeEvent(new ModelChangeEvent(this, Event.TOKEN_REMOVED, token));
    }
  }

  /**
   * Removes multiple token, and fires Event.TOKEN_REMOVED once.
   *
   * @param ids the list of ids of the tokens
   */
  public void removeTokens(List<GUID> ids) {
    List<Token> removedTokens = new ArrayList<>();
    if (ids != null) {
      for (GUID id : ids) {
        Token token = tokenMap.remove(id);
        if (token != null) {
          tokenOrderedList.remove(token);
          removedTokens.add(token);
        }
      }
      if (!removedTokens.isEmpty()) {
        fireModelChangeEvent(new ModelChangeEvent(this, Event.TOKEN_REMOVED, removedTokens));
      }
    }
  }

  public Token getToken(GUID id) {
    return tokenMap.get(id);
  }

  /**
   * @param name the name of the token.
   * @return the first token with a given name. The name is matched case-insensitively.
   */
  public Token getTokenByName(String name) {
    for (Token token : getAllTokens()) {
      if (name.equalsIgnoreCase(token.getName())) {
        return token;
      }
    }
    return null;
  }

  /**
   * Looks for the given identifier as a token name, token GM name, or GUID, in that order.
   *
   * @param identifier the String identifier of the token
   * @return token that matches the identifier or <code>null</code>
   */
  public Token resolveToken(String identifier) {

    // try fast lookup first for an identifier that might be a GUID len=16
    if (!GUID.isNotGUID(identifier)) {
      try {
        return getToken(GUID.valueOf(identifier));
      } catch (Exception e) {
        // wasn't a GUID afterall, OK to ignore
      }
    }

    // look at (GM)name
    Token token = getTokenByName(identifier);
    if (token == null) {
      token = getTokenByGMName(identifier);
    }
    return token;
  }

  /**
   * @param name the GM name of the token.
   * @return the first token with a given GM name. The name is matched case-insensitively.
   */
  public Token getTokenByGMName(String name) {
    for (Token token : getAllTokens()) {
      if (StringUtil.isEmpty(token.getGMName())) {
        continue;
      }
      if (token.getGMName().equalsIgnoreCase(name)) {
        return token;
      }
    }
    return null;
  }

  public List<DrawnElement> getAllDrawnElements() {
    List<DrawnElement> list = new ArrayList<DrawnElement>();

    list.addAll(getDrawnElements());
    list.addAll(getObjectDrawnElements());
    list.addAll(getBackgroundDrawnElements());
    list.addAll(getGMDrawnElements());

    return list;
  }

  public DrawnElement getDrawnElement(GUID id) {
    DrawnElement result = findDrawnElement(getAllDrawnElements(), id);
    return result;
  }

  private DrawnElement findDrawnElement(List<DrawnElement> list, GUID id) {
    for (DrawnElement de : list) {
      if (de.getDrawable().getId() == id) {
        return de;
      }
      if (de.getDrawable() instanceof DrawablesGroup) {
        DrawnElement result =
            findDrawnElement(((DrawablesGroup) de.getDrawable()).getDrawableList(), id);
        if (result != null) {
          return result;
        }
      }
    }
    return null;
  }

  public int getTokenCount() {
    return tokenOrderedList.size();
  }

  public List<Token> getAllTokens() {
    return Collections.unmodifiableList(new ArrayList<Token>(tokenOrderedList));
  }

  public Set<MD5Key> getAllAssetIds() {
    Set<MD5Key> idSet = new HashSet<MD5Key>();

    // Zone
    if (getBackgroundPaint() instanceof DrawableTexturePaint) {
      idSet.add(((DrawableTexturePaint) getBackgroundPaint()).getAssetId());
    }
    idSet.add(getMapAssetId());
    if (getFogPaint() instanceof DrawableTexturePaint) {
      idSet.add(((DrawableTexturePaint) getFogPaint()).getAssetId());
    }

    // Tokens
    for (Token token : getAllTokens()) {
      idSet.addAll(token.getAllImageAssets());
    }

    // Painted textures
    for (DrawnElement drawn : getAllDrawnElements()) {
      DrawablePaint paint = drawn.getPen().getPaint();
      if (paint instanceof DrawableTexturePaint) {
        idSet.add(((DrawableTexturePaint) paint).getAssetId());
      }
      paint = drawn.getPen().getBackgroundPaint();
      if (paint instanceof DrawableTexturePaint) {
        idSet.add(((DrawableTexturePaint) paint).getAssetId());
      }
    }
    // It's easier to just remove null at the end than to do a is-null check on each asset
    idSet.remove(null);

    return idSet;
  }

  public List<Token> getTokensFiltered(Filter filter) {
    ArrayList<Token> copy = new ArrayList<Token>(getTokenCount());

    for (Token token : tokenOrderedList) {
      if (filter.matchToken(token)) {
        copy.add(token);
      }
    }
    return Collections.unmodifiableList(copy);
  }

  public List<Token> removeTokens(List<Token> tokensToKeep, List<Token> tokensToRemove) {
    ArrayList<Token> originalList = new ArrayList<Token>(tokensToKeep);
    originalList.removeAll(tokensToRemove);

    return Collections.unmodifiableList(originalList);
  }

  /** @return list of non-stamp tokens, both pc and npc */
  public List<Token> getTokens() {
    return getTokens(true);
  }

  public List<Token> getTokens(boolean getAlwaysVisible) {
    return getTokensFiltered(
        new Filter() {
          @Override
          public boolean matchToken(Token t) {
            if (getAlwaysVisible) {
              return !t.isStamp();
            } else {
              return !t.isStamp() && !t.isAlwaysVisible();
            }
          }
        });
  }

  public List<Token> getGMStamps() {
    return getGMStamps(true);
  }

  public List<Token> getGMStamps(boolean getAlwaysVisible) {
    return getTokensFiltered(
        new Filter() {
          @Override
          public boolean matchToken(Token t) {
            if (getAlwaysVisible) {
              return t.isGMStamp();
            } else {
              return t.isGMStamp() && !t.isAlwaysVisible();
            }
          }
        });
  }

  public List<Token> getStampTokens() {
    return getStampTokens(true);
  }

  public List<Token> getStampTokens(boolean getAlwaysVisible) {
    return getTokensFiltered(
        new Filter() {
          @Override
          public boolean matchToken(Token t) {
            if (getAlwaysVisible) {
              return t.isObjectStamp();
            } else {
              return t.isObjectStamp() && !t.isAlwaysVisible();
            }
          }
        });
  }

  public List<Token> getBackgroundStamps() {
    return getBackgroundStamps(true);
  }

  public List<Token> getBackgroundStamps(boolean getAlwaysVisible) {
    return getTokensFiltered(
        new Filter() {
          @Override
          public boolean matchToken(Token t) {
            if (getAlwaysVisible) {
              return t.isBackgroundStamp();
            } else {
              return t.isBackgroundStamp() && !t.isAlwaysVisible();
            }
          }
        });
  }

  public List<Token> getPlayerTokens() {
    return getTokensFiltered(
        new Filter() {
          @Override
          public boolean matchToken(Token t) {
            return t.getType() == Token.Type.PC;
          }
        });
  }

  public List<Token> getFigureTokens() {
    return getTokensFiltered(
        new Filter() {
          @Override
          public boolean matchToken(Token t) {
            return t.getShape() == Token.TokenShape.FIGURE;
          }
        });
  }

  public List<Token> getTokensAlwaysVisible() {
    return getTokensFiltered(
        new Filter() {
          @Override
          public boolean matchToken(Token t) {
            return t.isAlwaysVisible();
          }
        });
  }

  public List<Token> getTokensWithVBL() {
    return getTokensFiltered(
        new Filter() {
          @Override
          public boolean matchToken(Token t) {
            return t.hasVBL();
          }
        });
  }

  public List<Token> getTokensWithTerrainModifiers() {
    return getTokensFiltered(
        new Filter() {
          @Override
          public boolean matchToken(Token t) {
            return !t.getTerrainModifierOperation().equals(TerrainModifierOperation.NONE);
          }
        });
  }

  /**
   * This method is called when no tokens are selected and it determines which tokens FoW to show.
   * New buttons were added to select what type of tokens, by ownership, should be shown and driven
   * by the TokenSelection enum.
   *
   * @param p the player
   * @return the list of tokens
   * @author updated by Jamz
   * @since updated 1.4.1.0
   */
  public List<Token> getOwnedTokensWithSight(Player p) {
    return getTokensFiltered(
        new Filter() {
          public boolean matchToken(Token t) {
            // System.out.println("isOwnedByAll(): " + t.getName() + ":" + t.isOwnedByAll());
            // System.out.println("AppUtil.playerOwns(t): " + t.getName() + ":" +
            // AppUtil.playerOwns(t));
            // return t.getType() == Token.Type.PC && t.getHasSight() && AppUtil.playerOwns(t);

            if (tokenSelection == null) {
              tokenSelection = TokenSelection.ALL;
            }
            // System.out.println("TokenSelection: " + tokenSelection);
            switch (tokenSelection) {
              case ALL: // Show FoW for ANY Token I own
                return t.getHasSight() && AppUtil.playerOwns(t);
              case NPC: // Show FoW for only NPC Tokens I own
                return t.getHasSight()
                    && t.getType() == Token.Type.NPC
                    && (t.isOwnedByAll() || AppUtil.playerOwns(t));
              case PC: // Show FoW for only PC Tokens I own
                return t.getHasSight()
                    && t.getType() == Token.Type.PC
                    && (t.isOwnedByAll() || AppUtil.playerOwns(t));
              case GM: // Show FoW for ANY Token the GM "username" explicitly owns OR has no
                // ownership
                return t.getHasSight() && AppUtil.gmOwns(t);
              default: // Show FoW for ANY Token I own
                return t.getHasSight() && AppUtil.playerOwns(t);
            }
          }
        });
  }

  /** @return list of PCs tokens with sight. For FogUtil.exposePCArea to skip sight test. */
  public List<Token> getPlayerTokensWithSight() {
    return getTokensFiltered(
        new Filter() {
          public boolean matchToken(Token t) {
            return t.getType() == Token.Type.PC && t.getHasSight();
          }
        });
  }

  /**
   * Jamz: Get a list of all tokens with sight that are either PC tokens or NPC Tokens "Owned by
   * All", or "Owned" by the current player; in theory, NPC tokens the Player control.
   *
   * @return the list of tokens with sight.
   */
  public List<Token> getTokensOwnedByAllWithSight() {
    return getTokensFiltered(
        new Filter() {
          // String playerId = MapTool.getPlayer().getName();
          public boolean matchToken(Token t) {
            return (t.getHasSight()
                && (t.getType() == Token.Type.PC && (t.isOwnedByAll() || AppUtil.playerOwns(t))));
          }
        });
  }

  // Jamz: Get a list of all tokens with sight that are either PC tokens or NPC Tokens "Owned by
  // All",
  // or "Owned" by the current player; in theory, NPC tokens the Player control.
  public List<Token> getTokensOwnedByAllWithSight(Player p) {
    return getTokensFiltered(
        new Filter() {
          String playerId = MapTool.getPlayer().getName();

          public boolean matchToken(Token t) {
            return (t.getHasSight()
                && (t.getType() == Token.Type.PC || t.isOwnedByAll() || t.isOwner(playerId)));
          }
        });
  }

  public List<Token> getPlayerOwnedTokensWithSight(Player p) {
    return getTokensFiltered(
        new Filter() {
          @Override
          public boolean matchToken(Token t) {
            return t.getType() == Token.Type.PC && t.getHasSight() && AppUtil.playerOwns(t);
          }
        });
  }

  public int findFreeNumber(String tokenBaseName, boolean checkDm) {
    if (tokenNumberCache == null) {
      tokenNumberCache = new HashMap<String, Integer>();
    }
    Integer _lastUsed = tokenNumberCache.get(tokenBaseName);

    int lastUsed;

    if (_lastUsed == null) {
      lastUsed = 0;
    } else {
      lastUsed = _lastUsed;
    }
    boolean repeat;
    do {
      lastUsed++;
      repeat = false;
      if (checkDm) {
        Token token = getTokenByGMName(Integer.toString(lastUsed));
        if (token != null) {
          repeat = true;
        }
      }
      if (!repeat && tokenBaseName != null) {
        String name = tokenBaseName + " " + lastUsed;
        Token token = getTokenByName(name);
        if (token != null) {
          repeat = true;
        }
      }
    } while (repeat);
    tokenNumberCache.put(tokenBaseName, lastUsed);
    return lastUsed;
  }

  /** Interface for matchToken. */
  public static interface Filter {

    public boolean matchToken(Token t);
  }

  /** The TokenZOrderComparator used to order token lists. */
  public static final Comparator<Token> TOKEN_Z_ORDER_COMPARATOR = new TokenZOrderComparator();

  /** The class used to compare the Zorder of two tokens. */
  public static class TokenZOrderComparator implements Comparator<Token> {

    @Override
    public int compare(Token o1, Token o2) {
      int lval = o1.getZOrder();
      int rval = o2.getZOrder();

      if (lval == rval) {
        return o1.getId().compareTo(o2.getId());
      } else {
        return lval - rval;
      }
    }
  }

  /**
   * Replaces the static TOKEN_Z_ORDER_COMPARATOR comparator with instantiated version so that grid
   * is available and can access token footprint. Only used when Rendering just Figure Tokens.
   *
   * @return the token comparator.
   */
  public Comparator<Token> getFigureZOrderComparator() {
    return new Comparator<Token>() {
      @Override
      public int compare(Token o1, Token o2) {
        /*
         * It is an assumption of this comparator that all tokens are being sorted using isometric
         * logic.
         *
         * <p>Get the footprint (dependent on Grid) and find the centre of the footprint. If the
         * same, place tokens above objects. Otherwise use height to place the smallest in front. If
         * still equal use normal z order.
         */
        int v1 = getFigureZOrder(o1);
        int v2 = getFigureZOrder(o2);
        if ((v1 - v2) != 0) {
          return v1 - v2;
        }
        if (!o1.isToken() && o2.isToken()) {
          return -1;
        }
        if (!o2.isToken() && o1.isToken()) {
          return +1;
        }
        if (o1.getHeight() != o2.getHeight()) {
          // Larger tokens at the same position, go behind
          return o2.getHeight() - o1.getHeight();
        }
        int lval = o1.getZOrder();
        int rval = o2.getZOrder();

        if (lval == rval) {
          return o1.getId().compareTo(o2.getId());
        } else {
          return lval - rval;
        }
      }
    };
  }

  /**
   * If set size return the footprint, otherwise return bounding box. Figure tokens are designed for
   * set sizes so we need to approximate free size tokens
   *
   * @param t the token to get center of.
   * @return the center of the footprint / bounding box.
   */
  private int getFigureZOrder(Token t) {

    Rectangle b1 =
        t.isSnapToScale() ? t.getFootprint(getGrid()).getBounds(getGrid()) : t.getBounds(getZone());
    /*
     * This is an awful approximation of centre of token footprint. The bounding box (b1 & b2) are
     * usually centred on token x & y So token y + bounding y give you the bottom of the box Then
     * subtract portion of height to get the centre point of the base.
     */
    int bottom = (t.isSnapToScale() ? t.getY() + b1.y : b1.y) + b1.height;
    int centre = t.isSnapToScale() ? b1.height / 2 : b1.width / 4;
    return bottom - centre;
  }

  /** @return this */
  private Zone getZone() {
    return this;
  }

  /** @return Getter for initiativeList */
  public InitiativeList getInitiativeList() {
    return initiativeList;
  }

  /** @param initiativeList Setter for the initiativeList */
  public void setInitiativeList(InitiativeList initiativeList) {
    this.initiativeList = initiativeList;
    new MapToolEventBus().getMainEventBus().post(new InitiativeListReplacedEvent(this));
  }

  public void optimize() {
    log.debug("Optimizing Map " + getName());
    MapTool.getFrame().setStatusMessage(I18N.getText("Zone.status.optimizing", getName()));
    collapseDrawables();
  }

  /**
   * Clear out any drawables that are hidden/erased. This is an optimization step that should only
   * happen when you can't undo your changes and re-expose a drawable, typically at load.
   */
  private void collapseDrawables() {
    collapseDrawableLayer(drawables);
    collapseDrawableLayer(gmDrawables);
    collapseDrawableLayer(objectDrawables);
    collapseDrawableLayer(backgroundDrawables);
  }

  private void collapseDrawableLayer(List<DrawnElement> layer) {
    if (layer.isEmpty()) {
      return;
    }
    Area area = new Area();
    List<DrawnElement> list = new ArrayList<DrawnElement>(layer);
    Collections.reverse(list);
    int eraserCount = 0;
    for (ListIterator<DrawnElement> drawnIter = list.listIterator(); drawnIter.hasNext(); ) {
      DrawnElement drawn = drawnIter.next();
      // Are we covered ourselves ?
      Area drawnArea = drawn.getDrawable().getArea();
      if (drawnArea == null) {
        continue;
      }
      // The following is over-zealous optimization. Lines (1-dimensional) should be kept.
      // (Does drawable cover area? If not, get rid of it.)
      // if (drawnArea.isEmpty()) {
      // drawnIter.remove();
      // continue;
      // }
      // if (GraphicsUtil.contains(area, drawnArea)) { // Too expensive
      if (area.contains(drawnArea.getBounds())) { // Not as accurate, but faster
        drawnIter.remove();
        continue;
      }
      // Are we possibly covering something up?
      if (drawn.getPen().isEraser() && (drawn.getPen().getBackgroundMode() == Pen.MODE_SOLID)) {
        area.add(drawnArea);
        eraserCount++;
        continue;
      }
    }
    // Now use the new list
    layer.clear();
    // If the number of elements is greater than the number of erasables, keep them all.
    if (list.size() > eraserCount) {
      layer.addAll(list);
      Collections.reverse(layer);
    }
  }

  ////
  // Backward compatibility
  @Override
  protected Object readResolve() {
    super.readResolve();

    // 1.3b76 -> 1.3b77
    // adding the exposed area for Individual FOW
    if (exposedAreaMeta == null) {
      exposedAreaMeta = new HashMap<GUID, ExposedAreaMetaData>();
    }
    // 1.3b70 -> 1.3b71
    // These two variables were added
    if (drawBoard == false) {
      // this should check the file version, not the value
      drawBoard = true;
    }
    if (boardPosition == null) {
      boardPosition = new Point(0, 0);
    }
    Zone.Layer.TOKEN.setEnabled(true);
    Zone.Layer.GM.setEnabled(true);
    Zone.Layer.OBJECT.setEnabled(true);
    Zone.Layer.BACKGROUND.setEnabled(true);

    // 1.3b47 -> 1.3b48
    if (visionType == null) {
      if (getTokensFiltered(
                  new Filter() {
                    @Override
                    public boolean matchToken(Token token) {
                      return token.hasLightSources();
                    }
                  })
              .size()
          > 0) {
        visionType = VisionType.NIGHT;
      } else if (topology != null && !topology.isEmpty()) {
        visionType = VisionType.DAY;

      } else {
        visionType = VisionType.OFF;
      }
    }
    // Look for the bizarre z-ordering disappearing trick
    boolean foundZero = false;
    boolean fixZOrder = false;
    for (Token token : tokenOrderedList) {
      if (token.getZOrder() == 0) {
        if (foundZero) {
          fixZOrder = true;
          break;
        }
        foundZero = true;
      }
    }
    if (fixZOrder) {
      int z = 0;
      for (Token token : tokenOrderedList) {
        token.setZOrder(z++);
      }
    }
    // Transient "undo" field added in 1.3.b88
    // This will be true; it's just in case we decide to make it persistent in the future
    if (undo == null) {
      undo = new UndoPerZone(this);
    }

    // Movement Blocking Layer
    if (topologyTerrain == null) {
      topologyTerrain = new Area();
    }
    return this;
  }

  /** @return the exposedAreaMeta. */
  public Map<GUID, ExposedAreaMetaData> getExposedAreaMetaData() {
    if (exposedAreaMeta == null) {
      exposedAreaMeta = new HashMap<GUID, ExposedAreaMetaData>();
    }
    return exposedAreaMeta;
  }

  /**
   * Find the area of this map which has been exposed to the given token GUID.
   *
   * @param tokenExposedAreaGUID token whose exposed area should be returned
   * @return area of fog cleared away for/by this token
   */
  public ExposedAreaMetaData getExposedAreaMetaData(GUID tokenExposedAreaGUID) {
    ExposedAreaMetaData meta = exposedAreaMeta.get(tokenExposedAreaGUID);
    if (meta != null) {
      return meta;
    }
    meta = new ExposedAreaMetaData();
    exposedAreaMeta.put(tokenExposedAreaGUID, meta);
    return meta;
  }

  /**
   * Put the ExposedAreaMetaData in the exposedAreaMeta map for a token
   *
   * @param tokenExposedAreaGUID the GUID of the token
   * @param meta the exposed metadata
   */
  public void setExposedAreaMetaData(GUID tokenExposedAreaGUID, ExposedAreaMetaData meta) {
    if (exposedAreaMeta == null) {
      exposedAreaMeta = new HashMap<GUID, ExposedAreaMetaData>();
    }
    exposedAreaMeta.put(tokenExposedAreaGUID, meta);
    fireModelChangeEvent(new ModelChangeEvent(this, Event.FOG_CHANGED));
  }

  /**
   * Lee: gets setting to expose fog normally or only at way points
   *
   * @return boolean for exposure method
   */
  public boolean getWaypointExposureToggle() {
    return exposeFogAtWaypoints;
  }

  /**
   * Lee: Tells fog utilities to expose fog normally or only at way points
   *
   * @param toggle toggle for exposure method
   */
  public void setWaypointExposureToggle(boolean toggle) {
    exposeFogAtWaypoints = toggle;
  }
}
