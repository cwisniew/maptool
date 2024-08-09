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

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import net.rptools.maptool.client.MapTool;
import net.rptools.maptool.language.I18N;
import net.rptools.maptool.model.Label;
import net.rptools.maptool.model.label.LabelManager;

public class LabelPresetsDialog extends JDialog {

  private JPanel contentPane;
  private JButton closeButton;
  private JButton editButton; // TODO: CDW implement edit functionality
  private JButton deleteButton; // TODO: CDW implement delete functionality
  private JTable labelPresetsTable;
  private JButton addButton;

  private final LabelManager labelManager;

  public LabelPresetsDialog(LabelManager labelManager) {
    super(MapTool.getFrame(), I18N.getText("Label.presetsDialog.title"), true);
    this.labelManager = labelManager;

    setContentPane(contentPane);
    setModal(true);
    getRootPane().setDefaultButton(closeButton);

    closeButton.addActionListener(e -> close());
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    pack();
    setLocationRelativeTo(MapTool.getFrame());

    addButton.addActionListener(e -> addPreset());
  }

  private void addPreset() {
    var preset = new Label();
    preset.setIsPreset(true);

    var presetName =
        JOptionPane.showInputDialog(this, I18N.getText("Label.presetsDialog.addPreset"));
    if (presetName == null || !presetName.isBlank()) {
      if (labelManager.getPresets().getPresetNames().contains(presetName)) {
        JOptionPane.showMessageDialog(
            this, I18N.getText("Label.presetsDialog.presetExists", presetName));
        return;
      }
      preset.setLabel(presetName);

      var dialog = new EditLabelDialog(preset, labelManager, true);
      dialog.setVisible(true);
      if (dialog.isAccepted()) {
        // As the dialog sets the label with the entered text we need to swap the label and
        // preview text
        String previewText = preset.getLabel();
        preset.setLabel(presetName);
        preset.setPreviewText(previewText);
        labelManager.getPresets().addPreset(presetName, preset);
        ((LabelPresetsTableModel) labelPresetsTable.getModel()).fireTableDataChanged();
      }
    }
  }

  private void close() {
    setVisible(false);
  }

  private void createUIComponents() {
    labelPresetsTable = new JTable(new LabelPresetsTableModel(labelManager));
    labelPresetsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    labelPresetsTable.getColumnModel().getColumn(2).setCellRenderer(new LabelCellRenderer());
  }
}
