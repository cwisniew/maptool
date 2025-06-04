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
package net.rptools.maptool.client;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import net.rptools.maptool.client.swing.ImageBorder;
import net.rptools.maptool.client.ui.theme.Borders;
import net.rptools.maptool.client.ui.theme.RessourceManager;

/**
 * @author trevor
 */
public class AppStyle {

  public static ImageBorder border;
  public static ImageBorder selectedBorder;
  public static ImageBorder selectedStampBorder;
  public static ImageBorder selectedUnownedBorder;
  public static ImageBorder miniMapBorder;
  public static ImageBorder shadowBorder;
  public static ImageBorder commonMacroBorder;
  public static Font labelFont;
  public static Color selectionBoxOutline;
  public static Color selectionBoxFill;
  public static Color resizeBoxOutline = Color.red;
  public static Color resizeBoxFill = Color.yellow;
  public static Color wallTopologyColor = new Color(255, 182, 0, 255);
  public static Color wallTopologyOutlineColor = Color.black;
  public static Color highlightedWallTopologyColor = new Color(255, 136, 0, 255);
  public static Color selectedWallOutlineColor = new Color(255, 255, 255, 255);
  public static Color topologyColor = new Color(0, 0, 255, 128);
  public static Color topologyAddColor = new Color(255, 0, 0, 128);
  public static Color topologyRemoveColor = new Color(255, 255, 255, 128);
  public static Color hillVblColor = new Color(0, 255, 255, 128);
  public static Color pitVblColor = new Color(104, 255, 0, 128);
  public static Color coverVblColor = new Color(245, 0, 0, 128);
  public static Color topologyTerrainColor = new Color(255, 0, 255, 128);
  public static Color tokenTopologyColor = new Color(255, 255, 0, 128);
  public static Color tokenHillVblColor = new Color(255, 136, 0, 128);
  public static Color tokenPitVblColor = new Color(255, 0, 0, 128);
  public static Color tokenCoverVblColor = new Color(245, 0, 0, 128);
  public static Color tokenMblColor = new Color(255, 128, 255, 128);

  static {
    if (!GraphicsEnvironment.isHeadless()) {
      border = RessourceManager.getBorder(Borders.GRAY2);
      selectedBorder = RessourceManager.getBorder(Borders.RED);
      selectedStampBorder = RessourceManager.getBorder(Borders.BLUE);
      selectedUnownedBorder = RessourceManager.getBorder(Borders.GREEN);
      miniMapBorder = RessourceManager.getBorder(Borders.GRAY);
      shadowBorder = RessourceManager.getBorder(Borders.SHADOW);
      commonMacroBorder = RessourceManager.getBorder(Borders.HIGHLIGHT);
      labelFont = Font.decode("serif-NORMAL-12");
      selectionBoxOutline = Color.black;
      selectionBoxFill = Color.blue;
    } else {
      // Provide sensible defaults for headless mode if any code tries to access these
      // Though ideally, code using these would also check for headless mode
      labelFont = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
      selectionBoxOutline = Color.black; // Or some other non-null default
      selectionBoxFill = Color.blue;    // Or some other non-null default
      // Borders will remain null, which is acceptable if drawing code handles null.
    }
  }
}
