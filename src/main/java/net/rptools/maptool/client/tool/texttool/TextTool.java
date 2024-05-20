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
package net.rptools.maptool.client.tool.texttool;

import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import net.rptools.maptool.client.AppStyle;
import net.rptools.maptool.client.MapTool;
import net.rptools.maptool.client.ScreenPoint;
import net.rptools.maptool.client.tool.DefaultTool;
import net.rptools.maptool.client.ui.zone.ZoneOverlay;
import net.rptools.maptool.client.ui.zone.renderer.ZoneRenderer;
import net.rptools.maptool.model.Label;
import net.rptools.maptool.model.ZonePoint;
import net.rptools.maptool.model.label.LabelManager;

/**
 * The TextTool class represents a tool that allows users to add and edit labels in a graphical
 * editor. It extends the DefaultTool class and implements the ZoneOverlay interface.
 */
public class TextTool extends DefaultTool implements ZoneOverlay {
  /** The serial version UID. */
  private static final long serialVersionUID = -8944323545051996907L;

  /**
   * Represents the currently selected Label object.
   *
   * @see Label
   */
  private Label selectedLabel;

  /** The horizontal offset for dragging the element. */
  private int dragOffsetX;

  /** The vertical offset of the drag operation. */
  private int dragOffsetY;

  /** Is the Label being dragged. */
  private boolean isDragging;

  private boolean selectedNewLabel;

  @Override
  protected void attachTo(ZoneRenderer renderer) {
    renderer.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
    super.attachTo(renderer);
    selectedLabel = null;
  }

  @Override
  protected void detachFrom(ZoneRenderer renderer) {
    renderer.setCursor(Cursor.getDefaultCursor());
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

  /**
   * Paints the overlay for the given ZoneRenderer using the provided Graphics2D object.
   *
   * @param renderer the ZoneRenderer object used to render the zone
   * @param g the Graphics2D object used for rendering
   */
  public void paintOverlay(ZoneRenderer renderer, Graphics2D g) {
    if (selectedLabel != null && renderer.getLabelBounds(selectedLabel) != null) {
      AppStyle.selectedBorder.paintWithin(g, renderer.getLabelBounds(selectedLabel));
    }
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
        Label label = renderer.getLabelAt(e.getX(), e.getY());
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
        }
        EditLabelDialog dialog = new EditLabelDialog(label, new LabelManager(), false);
        dialog.setVisible(true);

        if (!dialog.isAccepted()) {
          return;
        }
        renderer.getZone().putLabel(label);
        renderer.repaint(); // We may have changed label presets.
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
}
