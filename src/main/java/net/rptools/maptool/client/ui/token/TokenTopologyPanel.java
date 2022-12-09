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
package net.rptools.maptool.client.ui.token;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.TexturePaint;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.font.TextAttribute;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.PathIterator;
import java.awt.image.BufferedImage;
import java.text.NumberFormat;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import net.rptools.maptool.client.swing.SwingUtil;
import net.rptools.maptool.client.AppStyle;
import net.rptools.maptool.client.MapTool;
import net.rptools.maptool.client.ui.zone.vbl.TokenVBL.JTS_SimplifyMethodType;
import net.rptools.maptool.language.I18N;
import net.rptools.maptool.model.Token;
import net.rptools.maptool.model.Token.TokenShape;
import net.rptools.maptool.model.Zone;
import net.rptools.maptool.util.ImageManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TokenTopologyPanel extends JPanel {

  private static final Logger log = LogManager.getLogger();

  private final EditTokenDialog editTokenDialog;
  private Token token;

  private final Set<Zone.TopologyType> selectedTopologyTypes =
      EnumSet.noneOf(Zone.TopologyType.class);
  private final Map<Zone.TopologyType, Area> tokenTopologiesOriginal =
      new EnumMap<>(Zone.TopologyType.class);
  private final Map<Zone.TopologyType, Area> tokenTopologiesOptimized =
      new EnumMap<>(Zone.TopologyType.class);

  private JTS_SimplifyMethodType jtsMethod = JTS_SimplifyMethodType.getDefault();
  private Color topologyColorPick = new Color(0, 0, 0, 0);
  private boolean hideTokenImage = false;
  private boolean inverseTopology = false;
  private boolean autoGenerated = false;
  private boolean inProgress = false;
  private boolean colorPickerActive = false;
  private int colorSensitivity = 10;
  private int jtsDistanceTolerance = 2;
  // Point counts are only meaningful if autoGenerated == true
  private int tokenTopologyOriginalPointCount = 0;
  private int tokenTopologyOptimizedPointCount = 0;
  private double scale = 1;
  private int startX, startY;
  private int translateX, translateY;
  private int deltaX, deltaY;
  private BufferedImage tokenImageCached;
  private int previewX;
  private int previewY;
  private Image previewImage;

  private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
  private ScheduledFuture<?> future = executor.schedule(() -> {}, 0, TimeUnit.MILLISECONDS);

  private boolean mouseDragged = false;

  public TokenTopologyPanel(EditTokenDialog editTokenDialog) {
    this.editTokenDialog = editTokenDialog;

    addMouseWheelListener(
        e -> {
          // TODO: Zooming panel should zoom in at mouse cursor #mathishard
          int wheelMovement = e.getWheelRotation();
          if (wheelMovement == 0) {
            return;
          }
          double delta = wheelMovement > 0 ? -.25 : .25;
          scale += delta;
          scale = Math.max(1, scale);
          scale = Math.min(100, scale);

          refreshTokenImageCache();
        });

    addMouseListener(
        new MouseAdapter() {
          String old;

          @Override
          public void mousePressed(MouseEvent e) {
            if (e.getClickCount() == 2) {
              scale = 1;
              translateX = 0;
              translateY = 0;
              repaint();
            }

            startX = e.getX();
            startY = e.getY();
          }

          @Override
          public void mouseReleased(MouseEvent e) {
            translateX += deltaX;
            translateY += deltaY;
            deltaX = 0;
            deltaY = 0;

            if (isColorPickerActive()
                && SwingUtilities.isLeftMouseButton(e)
                && previewImage != null) {
              if (tokenImageCached == null) {
                refreshTokenImageCache();
              } else {
                if (!mouseDragged) {
                  Color pixelColor = new Color(tokenImageCached.getRGB(e.getX(), e.getY()), true);
                  setTopologyColorPick(pixelColor);
                  editTokenDialog.getTopologyIgnoreColorWell().setColor(pixelColor);
                  editTokenDialog.getTopologyColorPickerToggleButton().doClick();
                  editTokenDialog.updateAutoGeneratedTopology(true);
                }
              }
            }

            mouseDragged = false;
          }

          @Override
          public void mouseEntered(MouseEvent e) {
            old = MapTool.getFrame().getStatusMessage();
            MapTool.getFrame()
                .setStatusMessage(I18N.getString("EditTokenDialog.status.vbl.instructions"));
          }

          @Override
          public void mouseExited(MouseEvent e) {
            if (old != null) {
              MapTool.getFrame().setStatusMessage(old);
            }
          }
        });

    addMouseMotionListener(
        new MouseMotionAdapter() {
          @Override
          public void mouseDragged(MouseEvent e) {
            deltaX = startX - e.getX();
            deltaY = startY - e.getY();

            mouseDragged = true;
            tokenImageCached = null;
            repaint();
          }

          @Override
          public void mouseMoved(MouseEvent e) {
            if (isColorPickerActive() && previewImage != null) {
              if (tokenImageCached == null) {
                if (future.isDone()) {
                  refreshTokenImageCache();
                }
              } else {
                Color pixelColor = new Color(tokenImageCached.getRGB(e.getX(), e.getY()), true);
                setTopologyColorPick(pixelColor);
                editTokenDialog.getTopologyIgnoreColorWell().setColor(pixelColor);
              }
            }
          }
        });
  }

  private Color getTopologyColor(Color baseColor) {
    if (isHideTokenImage()) {
      return new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), 200);
    } else {
      return baseColor.brighter();
    }
  }

  @Override
  protected void paintComponent(Graphics g) {
    Dimension panelSize = getSize();
    Dimension panelUsableSize =
        new Dimension(Math.round(panelSize.width - 10), Math.round(panelSize.height - 10));

    Zone zone = MapTool.getFrame().getCurrentZoneRenderer().getZone();

    // Gather info
    BufferedImage image = ImageManager.getImage(token.getImageAssetId());
    java.awt.Rectangle tokenSize = token.getBounds(zone);
    Dimension originalImgSize = new Dimension(image.getWidth(), image.getHeight());
    Dimension imgSize = new Dimension(image.getWidth(), image.getHeight());

    // If figure we need to calculate an additional offset for the token height
    double iso_ho = 0;
    if (token.getShape() == TokenShape.FIGURE) {
      double th = token.getHeight() * (double) tokenSize.width / token.getWidth();
      iso_ho = tokenSize.height - th;
      tokenSize =
          new java.awt.Rectangle(
              tokenSize.x, tokenSize.y - (int) iso_ho, tokenSize.width, (int) th);
    }

    SwingUtil.constrainTo(imgSize, panelUsableSize.width, panelUsableSize.height);
    Point centerPoint = new Point(panelSize.width / 2, panelSize.height / 2);
    Graphics2D g2d = (Graphics2D) g;

    // Background
    ((Graphics2D) g)
        .setPaint(
            new TexturePaint(
                AppStyle.squaresTexture,
                new java.awt.Rectangle(
                    0,
                    0,
                    AppStyle.squaresTexture.getWidth() * 2,
                    AppStyle.squaresTexture.getHeight() * 2)));

    g2d.fillRect(0, 0, panelSize.width, panelSize.height);
    AppStyle.shadowBorder.paintWithin((Graphics2D) g, 0, 0, panelSize.width, panelSize.height);

    final int x =
        (int)
            (centerPoint.x
                - deltaX
                - translateX
                - (imgSize.width / 2 + token.getAnchor().x) * scale);
    final int y =
        (int)
            (centerPoint.y
                - deltaY
                - translateY
                - (imgSize.height / 2 + token.getAnchor().y) * scale);

    if (!isHideTokenImage()) {
      final int width = (int) (imgSize.width * scale);
      final int height = (int) (imgSize.height * scale);
      g2d.drawImage(image, x, y, width, height, this);

      // Store scaled version of image for color picker...
      previewX = x;
      previewY = y;
      previewImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }

    // Draw the topology
    if (!tokenTopologiesOptimized.isEmpty() && !isColorPickerActive()) {
      double sx = (imgSize.getWidth() / originalImgSize.getWidth()) * scale;
      double sy = (imgSize.getHeight() / originalImgSize.getHeight()) * scale;

      AffineTransform atArea = AffineTransform.getTranslateInstance(x, y);
      atArea.concatenate(AffineTransform.getScaleInstance(sx, sy));

      g2d.setColor(getTopologyColor(AppStyle.tokenMblColor));
      g2d.fill(
          atArea.createTransformedShape(
              tokenTopologiesOptimized.getOrDefault(Zone.TopologyType.MBL, new Area())));

      g2d.setColor(getTopologyColor(AppStyle.tokenTopologyColor));
      g2d.fill(
          atArea.createTransformedShape(
              tokenTopologiesOptimized.getOrDefault(Zone.TopologyType.WALL_VBL, new Area())));

      g2d.setColor(getTopologyColor(AppStyle.tokenHillVblColor));
      g2d.fill(
          atArea.createTransformedShape(
              tokenTopologiesOptimized.getOrDefault(Zone.TopologyType.HILL_VBL, new Area())));

      g2d.setColor(getTopologyColor(AppStyle.tokenPitVblColor));
      g2d.fill(
          atArea.createTransformedShape(
              tokenTopologiesOptimized.getOrDefault(Zone.TopologyType.PIT_VBL, new Area())));
    }

    // Draw the number of points generated
    if (autoGenerated && tokenTopologyOptimizedPointCount > 0) {
      drawOutlinedText(
          g2d,
          I18N.getText("EditTokenDialog.vbl.label.originalPointCount"),
          new Color(255, 100, 0),
          tokenTopologyOriginalPointCount,
          40);

      drawOutlinedText(
          g2d,
          I18N.getText("EditTokenDialog.vbl.label.optimizedPointCount"),
          new Color(50, 225, 0),
          tokenTopologyOptimizedPointCount,
          20);
    }

    if (isInProgress()) {
      Color color = new Color(0, 0, 0, 200);
      g.setColor(color);
      g.fillRect(0, 0, this.getWidth(), this.getHeight());

      String workingText = I18N.getText("EditTokenDialog.vbl.label.working");
      Font font = new Font("Serif", Font.BOLD, 24);

      g2d.setFont(font);
      g2d.setColor(Color.white);
      FontMetrics fontMetrics = g2d.getFontMetrics();
      int textWidth = fontMetrics.stringWidth(workingText);
      g2d.drawString(workingText, panelSize.width / 2 - textWidth / 2, panelSize.height / 2);
    }
  }

  /*
   * Note: I tried using BasicStroke but it just never looked good when rendered..
   */

  private void drawOutlinedText(Graphics2D g2d, String text, Color textColor, int count, int yPos) {
    final Dimension panelSize = getSize();
    Map<TextAttribute, Object> attributes = new HashMap<>();
    attributes.put(TextAttribute.TRACKING, 0.025);

    Font font = new Font(Font.SANS_SERIF, Font.BOLD, 18).deriveFont(attributes);

    g2d.setFont(font);
    g2d.setRenderingHint(
        RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

    FontMetrics fontMetrics = g2d.getFontMetrics();

    int textWidth = fontMetrics.stringWidth(text);
    int maxCountWidth = fontMetrics.stringWidth("10,000,000");
    float x = panelSize.width - textWidth - maxCountWidth;
    float offset = 1.0f;

    text += ": " + NumberFormat.getInstance().format(count);

    yPos = panelSize.height - yPos;

    g2d.setColor(Color.BLACK);
    g2d.drawString(text, x, yPos - offset);
    g2d.drawString(text, x, yPos + offset);
    g2d.drawString(text, x - offset, yPos);
    g2d.drawString(text, x - offset, yPos - offset);
    g2d.drawString(text, x - offset, yPos + offset);
    g2d.drawString(text, x + offset, yPos);
    g2d.drawString(text, x + offset, yPos - offset);
    g2d.drawString(text, x + offset, yPos + offset);

    g2d.setColor(textColor);
    g2d.drawString(text, x, yPos);
  }

  private void refreshTokenImageCache() {
    if (isColorPickerActive()) {
      setInProgress(true);
      tokenImageCached = null;

      // Cancel any scheduled tasks in case rapid repeated events fire...
      future.cancel(true);

      future =
          executor.schedule(
              () -> {
                tokenImageCached =
                    new BufferedImage(
                        getSize().width, getSize().height, BufferedImage.TYPE_INT_ARGB);
                tokenImageCached.createGraphics().drawImage(previewImage, previewX, previewY, null);
                setInProgress(false);
                log.info("Recached tokenImageCached");
              },
              250,
              TimeUnit.MILLISECONDS);
    }

    repaint();
  }

  public void reset(Token token) {
    future.cancel(true);
    setToken(token);

    // jtsMethod = JTS_SimplifyMethodType.getDefault();
    topologyColorPick = new Color(0, 0, 0, 0);
    hideTokenImage = false;
    inverseTopology = false;
    autoGenerated = false;
    inProgress = false;
    colorPickerActive = false;
    mouseDragged = false;
    colorSensitivity = 10;
    jtsDistanceTolerance = 2;
    tokenTopologyOriginalPointCount = 0;
    tokenTopologyOptimizedPointCount = 0;
    scale = 1;
    startX = 0;
    startY = 0;
    translateX = 0;
    translateY = 0;
    deltaX = 0;
    deltaY = 0;
    previewX = 0;
    previewY = 0;
    tokenImageCached = null;
    previewImage = null;

    editTokenDialog.getTopologyIgnoreColorWell().setColor(topologyColorPick);
    editTokenDialog.repaint();
  }

  public boolean isColorPickerActive() {
    return colorPickerActive;
  }

  public void setColorPickerActive(boolean colorPickerActive) {
    log.info("color picker is now {}", colorPickerActive);
    this.colorPickerActive = colorPickerActive;

    if (colorPickerActive) {
      setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
      refreshTokenImageCache();
    } else {
      setCursor(Cursor.getDefaultCursor());
    }
  }

  public boolean isInProgress() {
    return inProgress;
  }

  public void setInProgress(boolean inProgress) {
    this.inProgress = inProgress;
    repaint();
  }

  public double getScale() {
    return scale;
  }

  public void setScale(double scale) {
    this.scale = scale;
  }

  public int getAnchorX() {
    return token.getAnchor().x;
  }

  public int getAnchorY() {
    return token.getAnchor().y;
  }

  public Token getToken() {
    return token;
  }

  public void setToken(Token token) {
    this.token = new Token(token);
    autoGenerated = false;
    tokenTopologyOptimizedPointCount = 0;
    tokenTopologyOriginalPointCount = 0;

    selectedTopologyTypes.clear();
    tokenTopologiesOriginal.clear();
    tokenTopologiesOptimized.clear();
    for (final var type : Zone.TopologyType.values()) {
      final var topology = token.getTopology(type);
      if (topology != null) {
        selectedTopologyTypes.add(type);
        tokenTopologiesOriginal.put(type, topology);
        tokenTopologiesOptimized.put(type, topology);
      }
    }

    if (token.getColorSensitivity() >= 0) {
      colorSensitivity = token.getColorSensitivity();
    }
  }

  public Set<Zone.TopologyType> getSelectedTopologyTypes() {
    return EnumSet.copyOf(selectedTopologyTypes);
  }

  public boolean isTopologyTypeSelected(Zone.TopologyType type) {
    return selectedTopologyTypes.contains(type);
  }

  public void setTopologyTypeSelected(Zone.TopologyType type, boolean selected) {
    if (selected) {
      selectedTopologyTypes.add(type);
    } else {
      selectedTopologyTypes.remove(type);
    }
  }

  public void putCustomTopology(Zone.TopologyType type, Area topology) {
    tokenTopologiesOptimized.put(type, new Area(topology));
    autoGenerated = false;
    tokenTopologyOptimizedPointCount = 0;
  }

  public @Nullable Area getTopology(Zone.TopologyType type) {
    if (tokenTopologiesOptimized.containsKey(type)) {
      return new Area(tokenTopologiesOptimized.get(type));
    }

    return null;
  }

  // region Only for generating or clearing auto-generated topology.

  public boolean hasAnyOriginalTopologySet() {
    return !tokenTopologiesOriginal.isEmpty();
  }

  public boolean hasAnyOptimizedTopologySet() {
    return !tokenTopologiesOptimized.isEmpty();
  }

  public void setGeneratedTopologies(Area original, Area optimized) {
    autoGenerated = true;

    tokenTopologiesOriginal.clear();
    tokenTopologyOriginalPointCount = 0;
    if (original != null) {
      for (PathIterator pi = original.getPathIterator(null); !pi.isDone(); pi.next()) {
        ++tokenTopologyOriginalPointCount;
      }

      for (final var type : selectedTopologyTypes) {
        tokenTopologiesOriginal.put(type, new Area(original));
      }
    }

    tokenTopologiesOptimized.clear();
    tokenTopologyOptimizedPointCount = 0;
    if (optimized != null) {
      for (PathIterator pi = optimized.getPathIterator(null); !pi.isDone(); pi.next()) {
        tokenTopologyOptimizedPointCount++;
      }

      for (final var type : selectedTopologyTypes) {
        tokenTopologiesOptimized.put(type, new Area(optimized));
      }
    }
  }

  public void clearGeneratedTopologies() {
    autoGenerated = false;
    tokenTopologiesOptimized.clear();
  }
  // endregion

  public boolean isHideTokenImage() {
    return hideTokenImage;
  }

  public void setHideTokenImage(boolean hideTokenImage) {
    this.hideTokenImage = hideTokenImage;
  }

  public boolean isInverseTopology() {
    return inverseTopology;
  }

  public void setInverseTopology(boolean inverseTopology) {
    this.inverseTopology = inverseTopology;
  }

  public boolean getAutoGenerated() {
    return autoGenerated;
  }

  public void setAutoGenerated(boolean autoGenerated) {
    this.autoGenerated = autoGenerated;
  }

  public int getColorSensitivity() {
    return colorSensitivity;
  }

  public void setColorSensitivity(int value) {
    colorSensitivity = value;
  }

  public Color getTopologyColorPick() {
    return topologyColorPick;
  }

  public void setTopologyColorPick(Color color) {
    topologyColorPick = color;
  }

  public int getJtsDistanceTolerance() {
    return jtsDistanceTolerance;
  }

  public void setJtsDistanceTolerance(int value) {
    jtsDistanceTolerance = value;
  }

  public JTS_SimplifyMethodType getJtsMethod() {
    return jtsMethod;
  }

  public void setJtsMethod(JTS_SimplifyMethodType method) {
    jtsMethod = method;
  }
}
