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

/** Dialog for managing label presets. */
public class LabelPresetsDialog extends JDialog {

  /** The content pane. */
  private JPanel contentPane;

  /** The close button. */
  private JButton closeButton;

  /** The edit button. */
  private JButton editButton;

  /** The delete button. */
  private JButton deleteButton;

  /** The label presets table. */
  private JTable labelPresetsTable;

  /** The add button. */
  private JButton addButton;

  /** The rename button. */
  private JButton renameButton;

  /** The duplicate button. */
  private JButton duplicateButton;

  /** The label manager. */
  private final LabelManager labelManager;

  /**
   * Instantiates a new label presets dialog.
   *
   * @param labelManager the label manager to use for managing presets.
   */
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

    editButton.setEnabled(false);
    deleteButton.setEnabled(false);
    renameButton.setEnabled(false);
    duplicateButton.setEnabled(false);
    addButton.addActionListener(e -> addPreset());
    renameButton.addActionListener(e -> renamePreset());
    duplicateButton.addActionListener(e -> duplicatePreset());
    editButton.addActionListener(e -> editPreset());
    deleteButton.addActionListener(e -> deletePreset());
  }

  /** Rename preset. */
  private void renamePreset() {
    var preset = (Label) labelPresetsTable.getValueAt(labelPresetsTable.getSelectedRow(), 2);
    var presetName = getNewPresetName("Label.presetsDialog.renamePreset", preset.getLabel());
    if (presetName != null) {
      labelManager.getPresets().renamePreset(preset.getId(), presetName);
      ((LabelPresetsTableModel) labelPresetsTable.getModel()).fireTableDataChanged();
    }
  }

  /** Duplicate preset. */
  private void duplicatePreset() {
    // TODO: CDW implement duplicate functionality
  }

  /** Edit preset. */
  private void editPreset() {
    var preset = (Label) labelPresetsTable.getValueAt(labelPresetsTable.getSelectedRow(), 2);
    var dialog = new EditLabelDialog(preset, labelManager, true);
    dialog.setVisible(true);
    if (dialog.isAccepted()) {
      ((LabelPresetsTableModel) labelPresetsTable.getModel()).fireTableDataChanged();
    }
  }

  /** Delete preset. */
  private void deletePreset() {
    // TODO: CDW implement delete functionality
  }

  /** Adds a new preset. */
  private void addPreset() {
    var preset = new Label();
    preset.setIsPreset(true);

    var presetName = getNewPresetName("Label.presetsDialog.addPreset", "");

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

  /**
   * Prompts the user for a new preset name.
   *
   * @param title i18n key for the title of the dialog box.
   * @param promptValue the initial value for the prompt.
   * @return the new preset name. This will be {@code null} if the user cancels the dialog.
   */
  private String getNewPresetName(String title, String promptValue) {
    boolean isDone = false;
    String presetName = null;
    while (!isDone) {
      presetName = JOptionPane.showInputDialog(I18N.getText(title), promptValue);
      if (presetName == null) {
        isDone = true; // Cancel button pressed
      } else if (presetName.isBlank()) {
        JOptionPane.showMessageDialog(this, I18N.getText("Label.presetsDialog.presetNameEmpty"));
      } else {
        if (labelManager.getPresets().getPresetNames().contains(presetName)) {
          JOptionPane.showMessageDialog(
              this, I18N.getText("Label.presetsDialog.presetExists", presetName));
          promptValue = presetName;
        } else {
          isDone = true;
        }
      }
    }
    return presetName;
  }

  /** Close the dialog. */
  private void close() {
    setVisible(false);
  }

  /** Create UI components. */
  private void createUIComponents() {
    labelPresetsTable = new JTable(new LabelPresetsTableModel(labelManager));
    labelPresetsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    labelPresetsTable.getColumnModel().getColumn(2).setCellRenderer(new LabelCellRenderer());
    labelPresetsTable
        .getSelectionModel()
        .addListSelectionListener(
            e -> {
              var selectedRow = labelPresetsTable.getSelectedRow();
              if (selectedRow == -1) {
                editButton.setEnabled(false);
                deleteButton.setEnabled(false);
                renameButton.setEnabled(false);
                duplicateButton.setEnabled(false);
              } else {
                editButton.setEnabled(true);
                deleteButton.setEnabled(true);
                renameButton.setEnabled(true);
                duplicateButton.setEnabled(true);
              }
            });
  }
}
