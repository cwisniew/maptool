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

import java.util.ArrayList;
import java.util.Comparator;
import javax.swing.table.AbstractTableModel;
import net.rptools.maptool.language.I18N;
import net.rptools.maptool.model.Label;
import net.rptools.maptool.model.label.LabelManager;

public class LabelPresetsTableModel extends AbstractTableModel {

  private final LabelManager labelManager;

  private final ArrayList<Label> presets = new ArrayList<>();

  public LabelPresetsTableModel(LabelManager labelManager) {
    this.labelManager = labelManager;
    presets.addAll(
        this.labelManager.getPresets().getPresets().stream()
            .sorted(Comparator.comparing(Label::getLabel))
            .toList());
  }

  @Override
  public void fireTableDataChanged() {
    presets.clear();
    presets.addAll(
        labelManager.getPresets().getPresets().stream()
            .sorted(Comparator.comparing(Label::getLabel))
            .toList());
    super.fireTableDataChanged();
  }

  @Override
  public int getRowCount() {
    return presets.size();
  }

  @Override
  public int getColumnCount() {
    return 3;
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    return switch (columnIndex) {
      case 0 -> presets.get(rowIndex).getLabel();
      case 1 -> true;
      case 2 -> presets.get(rowIndex);
        // should never happen
      default -> throw new IllegalArgumentException("Invalid column index: " + columnIndex);
    };
  }

  @Override
  public String getColumnName(int column) {
    return switch (column) {
      case 0 -> I18N.getText("Label.preset");
      case 1 -> I18N.getText("Label.gmOnly");
      case 2 -> I18N.getText("Label.preview");
      default -> "";
    };
  }

  @Override
  public boolean isCellEditable(int rowIndex, int columnIndex) {
    return false;
  }

  @Override
  public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    // Do nothing
  }

  @Override
  public Class<?> getColumnClass(int columnIndex) {
    return switch (columnIndex) {
      case 0 -> String.class;
      case 1 -> Boolean.class;
      case 2 -> Label.class;
      default -> throw new IllegalArgumentException("Invalid column index: " + columnIndex);
    };
  }
}
