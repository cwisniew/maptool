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

import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import net.rptools.maptool.client.swing.label.FlatImageLabelFactory;
import net.rptools.maptool.model.Label;

public class LabelCellRenderer extends DefaultTableCellRenderer {

  @Override
  public Component getTableCellRendererComponent(
      JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    var preview = new JLabel();
    if (value instanceof Label label) {
      var flatLabel = new FlatImageLabelFactory().getMapImageLabel(label);

      var text = label.getPreviewText();
      if (text == null || text.isEmpty()) {
        text = "";
      }
      var img = flatLabel.renderImage(text);
      preview.setIcon(new ImageIcon(img));
      if (isSelected) {
        preview.setBackground(table.getSelectionBackground());
        preview.setOpaque(true);
      }
    }
    table.setRowHeight(row, preview.getPreferredSize().height + 4);
    return preview;
  }
}
