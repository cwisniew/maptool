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
package net.rptools.maptool.client.tool;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import net.rptools.lib.image.ImageUtil;
import net.rptools.maptool.client.MapTool;
import net.rptools.maptool.client.ScreenPoint;
import net.rptools.maptool.client.swing.HTMLPanelRenderer;
import net.rptools.maptool.client.ui.zone.ZoneOverlay;
import net.rptools.maptool.client.ui.zone.ZoneRenderer;
import net.rptools.maptool.model.Label;
import net.rptools.maptool.model.ZonePoint;
import net.rptools.maptool.model.map.MapMarker;

/** */
public class MapMarkerTool extends DefaultTool implements ZoneOverlay {
  private static final long serialVersionUID = -8944323545051996907L;

  private Label selectedLabel;

  private int dragOffsetX;
  private int dragOffsetY;
  private boolean isDragging;
  private boolean selectedNewLabel;

  public MapMarkerTool() {
    try {
      setIcon(
          new ImageIcon(ImageUtil.getImage("net/rptools/maptool/client/image/tool/info-blue.png")));
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }

  @Override
  protected void attachTo(ZoneRenderer renderer) {
    // TODO: CDW renderer.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
    super.attachTo(renderer);
    selectedLabel = null;
  }

  @Override
  protected void detachFrom(ZoneRenderer renderer) {
    // TODO: CDW renderer.setCursor(Cursor.getDefaultCursor());
    super.detachFrom(renderer);
  }

  @Override
  public String getTooltip() {
    return "tool.label.tooltip";
  }

  @Override
  public String getInstructions() {
    return "tool.label.instructions";
  }

  public void paintOverlay(ZoneRenderer renderer, Graphics2D g) {
    // TODO: CDW
  }

  @Override
  protected void installKeystrokes(Map<KeyStroke, Action> actionMap) {
    super.installKeystrokes(actionMap);

    actionMap.put(
        KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0),
        new AbstractAction() {
          public void actionPerformed(ActionEvent e) {
            if (selectedLabel != null) {
              renderer.getZone().removeLabel(selectedLabel.getId());
              MapTool.serverCommand()
                  .removeLabel(renderer.getZone().getId(), selectedLabel.getId());
              selectedLabel = null;
              repaint();
            }
          }
        });
  }

  ////
  // MOUSE
  @Override
  public void mousePressed(MouseEvent e) {
    Label label = renderer.getLabelAt(e.getX(), e.getY());
    if (label != selectedLabel) {
      selectedNewLabel = true;
      renderer.repaint();
    } else {
      selectedNewLabel = false;
    }
    if (label != null) {
      ScreenPoint sp = ScreenPoint.fromZonePoint(renderer, label.getX(), label.getY());
      dragOffsetX = (int) (e.getX() - sp.x);
      dragOffsetY = (int) (e.getY() - sp.y);
    }
    super.mousePressed(e);
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    if (SwingUtilities.isLeftMouseButton(e)) {
      if (!isDragging) {
        /* TODO: CDW Label label = renderer.getLabelAt(e.getX(), e.getY());
        if (label == null) {
          if (selectedLabel == null) {
            ZonePoint zp = new ScreenPoint(e.getX(), e.getY()).convertToZone(renderer);
            label = new Label("", zp.x, zp.y);
            selectedLabel = label;
          } else {
            selectedLabel = null;
            renderer.repaint();
            return;
          }
        } else {
          if (selectedNewLabel) {
            selectedLabel = label;
            renderer.repaint();
            return;
          }
        }*/
        ZonePoint zp = new ScreenPoint(e.getX(), e.getY()).convertToZone(renderer);
        var marker = new MapMarker("test", "AT11", zp.x, zp.y);
        renderer.getZone().putMarker(marker);
        /* TODO: CDW EditLabelDialog dialog = new EditLabelDialog(label);
        dialog.setVisible(true);

        if (!dialog.isAccepted()) {
          return;
        }*/
      }
      if (selectedLabel != null) {
        MapTool.serverCommand().putLabel(renderer.getZone().getId(), selectedLabel);
        renderer.repaint();
      }
    }
    isDragging = false;
    super.mouseReleased(e);
  }

  @Override
  public void mouseDragged(MouseEvent e) {
    super.mouseDragged(e);
    if (!isDragging) {
      // Setup
      Label label = renderer.getLabelAt(e.getX(), e.getY());
      if (selectedLabel == null || selectedLabel != label) {
        selectedLabel = label;
      }
      if (selectedLabel == null || SwingUtilities.isRightMouseButton(e)) {
        return;
      }
    }
    isDragging = true;

    ZonePoint zp =
        new ScreenPoint(e.getX() - dragOffsetX, e.getY() - dragOffsetY).convertToZone(renderer);

    selectedLabel.setX(zp.x);
    selectedLabel.setY(zp.y);
    renderer.repaint();
  }

  @Override
  public boolean isAvailable() {
    return MapTool.getPlayer().isGM();
  }


  public static void mouseOverPopUp(MapMarker mapMarker, Graphics2D g, Rectangle2D bounds,
      ZoneRenderer renderer) {
    GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
    GraphicsDevice device = env.getDefaultScreenDevice();
    GraphicsConfiguration config = device.getDefaultConfiguration();
    var image = config.createCompatibleImage(renderer.getWidth(), renderer.getHeight(),
        Transparency.TRANSLUCENT);
    var imgG2d = image.createGraphics();
    imgG2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    Color bgColor = UIManager.getColor("Label.background");
    Color fgColor = UIManager.getColor("Label.foreground");
    Color borderColor = UIManager.getColor("InternalFrame.borderHighlight");

    int borderSize = 2;
    String header = mapMarker.getMapKey() + " : " + mapMarker.getName();
    Font standardFont = imgG2d.getFont();
    Font headerFont = standardFont.deriveFont(Font.BOLD, standardFont.getSize() + 4);
    imgG2d.setFont(headerFont);
    FontMetrics fm = imgG2d.getFontMetrics();
    int headerWidth = fm.stringWidth(header);
    int padding = fm.stringWidth("M");
    int headerHeight = fm.getHeight();
    int width = Math.max(headerWidth + padding * 2 + borderSize * 2, 200);
    int height = 300;
    int lineSpacing = 5;
    int y = borderSize + padding;

    imgG2d.setBackground(bgColor);
    imgG2d.clearRect(0, 0, image.getWidth(), image.getHeight());

    imgG2d.setColor(fgColor);
    imgG2d.drawString(header, padding + borderSize, y);
    y += lineSpacing;
    imgG2d.setColor(fgColor);
    imgG2d.drawLine(borderSize + padding, y, width - borderSize - padding, y);
    imgG2d.setFont(standardFont);
    fm = imgG2d.getFontMetrics();
    int lineHeight = fm.getHeight();
    y += padding;


    HTMLPanelRenderer htmlRenderer = new HTMLPanelRenderer();
    htmlRenderer.setBackground(new Color(0, 0, 0, 200));
    htmlRenderer.setForeground(Color.black);
    htmlRenderer.setOpaque(false);
    htmlRenderer.setText(mapMarker.getDescription(), width - borderSize * 2 - padding * 2, 500);
    htmlRenderer.render(imgG2d, borderSize + padding, y);
    y += htmlRenderer.getMinimumSize().height + padding ;


    imgG2d.setColor(borderColor);
    var sourceBounds = new Rectangle2D.Double(0, 0, width, y + borderSize);
    var destBounds = new Rectangle2D.Double(bounds.getMaxX() + 5, bounds.getY(), width,
        y + borderSize);
    imgG2d.setStroke(new BasicStroke(borderSize));
    imgG2d.draw(sourceBounds);


    g.drawImage(image, (int) destBounds.getX(), (int) destBounds.getY(), (int) destBounds.getMaxX(),
        (int) destBounds.getMaxY(), (int) sourceBounds.getX(), (int) sourceBounds.getY(),
        (int) sourceBounds.getMaxX(), (int) sourceBounds.getMaxY(), null);

    imgG2d.dispose();
  }

}
