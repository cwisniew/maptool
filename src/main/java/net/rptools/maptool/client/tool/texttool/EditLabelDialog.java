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

import java.util.Arrays;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import net.rptools.maptool.client.MapTool;
import net.rptools.maptool.client.swing.AbeillePanel;
import net.rptools.maptool.client.swing.ColorWell;
import net.rptools.maptool.client.swing.SwingUtil;
import net.rptools.maptool.client.swing.label.FlatImageLabelFactory;
import net.rptools.maptool.language.I18N;
import net.rptools.maptool.model.Label;
import net.rptools.maptool.model.label.LabelManager;
import net.rptools.maptool.model.label.LabelShape;

/**
 * The EditLabelDialog class is a dialog box that allows the user to edit a label. It extends the
 * JDialog class and provides functionality for displaying and interacting with the label editing
 * panel.
 */
public class EditLabelDialog extends JDialog {

  /** The serial version UID. */
  private static final long serialVersionUID = 7621373725343873527L;

  /** Indicates whether the changes have been accepted. */
  private boolean accepted;

  private final boolean editingPresets;

  private String noPresetName = I18N.getText("Label.noPreset");

  /**
   * Constructs a new EditLabelDialog.
   *
   * @param label the label to be edited
   * @param labelManager the label manager
   * @param isPresets whether the label is a preset
   */
  public EditLabelDialog(Label label, LabelManager labelManager, boolean isPresets) {
    super(
        MapTool.getFrame(),
        I18N.getText(isPresets ? "tool.label.dialogtitlePresets" : "tool.label.dialogtitle"),
        true);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    editingPresets = isPresets;

    EditLabelPanel panel = new EditLabelPanel(this, labelManager);
    panel.bind(label);

    add(panel);
    getRootPane().setDefaultButton(panel.getOKButton());
    pack();
  }

  /**
   * Checks if the changes made in the dialog have been accepted.
   *
   * @return true if the changes have been accepted, false otherwise
   */
  public boolean isAccepted() {
    return accepted;
  }

  @Override
  public void setVisible(boolean b) {
    if (b) {
      SwingUtil.centerOver(this, getOwner());
    }
    super.setVisible(b);
  }

  /**
   * EditLabelPanel is a GUI panel used for editing label properties. It extends the AbeillePanel
   * class and provides functionality for binding a Label model, committing changes to the model,
   * and initializing the user interface components.
   */
  public class EditLabelPanel extends AbeillePanel<Label> {

    private static final long serialVersionUID = 3307411310513003924L;

    private final EditLabelDialog dialog;

    private final LabelManager labelManager;

    private boolean updatingPresetComboBox = false;

    /** The values being edited in the dialog. */
    private Label workingLabel = new Label();

    public EditLabelPanel(EditLabelDialog dialog, LabelManager labelManager) {
      super(new EditLabelDialogView().getRootComponent());

      this.dialog = dialog;
      this.labelManager = labelManager;
      panelInit();

      getLabelTextField().setSelectionStart(0);
      getLabelTextField().setSelectionEnd(getLabelTextField().getText().length());
      getLabelTextField().setCaretPosition(getLabelTextField().getText().length());
    }

    @Override
    public void bind(Label model) {
      workingLabel.copyValuesFrom(model);
      copyLabelToValues(workingLabel);
      super.bind(model);
      handlePresets();
    }

    @Override
    public boolean commit() {
      copyValuesToLabel(workingLabel);
      var model = getModel();
      // We want to keep the x and y values from the original label.
      int x = model.getX();
      int y = model.getY();
      getModel().copyValuesFrom(workingLabel);
      getModel().setX(x);
      getModel().setY(y);
      return super.commit();
    }

    private void copyLabelToValues(Label label) {
      copyLabelToValues(label, label.getLabel());
    }

    private void copyLabelToValues(Label label, String text) {
      getForegroundColorWell().setColor(label.getForegroundColor());
      getBackgroundColorWell().setColor(label.getBackgroundColor());
      getFontSizeSpinner().setValue(label.getFontSize());
      getBorderColorWell().setColor(label.getBorderColor());
      getBorderWidthSpinner().setValue(label.getBorderWidth());
      getBorderArcSpinner().setValue(label.getBorderArc());
      getLabelTextField().setText(text);
      var presetName = labelManager.getPresets().getPresetName(label.getPreset());
      if (presetName == null) {
        presetName = "";
      }
      getLabelPresetsComboBox().setSelectedItem(presetName);
      getShowBorderCheckBox().setSelected(label.isShowBorder());
      getShowBackgroundCheckBox().setSelected(label.isShowBackground());
      getLabelShapeComboBox().setSelectedItem(label.getShape());
      getHorizontalPaddingSpinner().setValue(label.getHorizontalPadding());
      getVerticalPaddingSpinner().setValue(label.getVerticalPadding());
      getGMOnlyCheckBox().setSelected(label.isGmOnly());
      adjustControls();
    }

    private void copyValuesToLabel(Label label) {
      label.setForegroundColor(getForegroundColorWell().getColor());
      label.setBackgroundColor(getBackgroundColorWell().getColor());
      label.setFontSize((Integer) getFontSizeSpinner().getValue());
      label.setBorderColor(getBorderColorWell().getColor());
      label.setBorderWidth((Integer) getBorderWidthSpinner().getValue());
      label.setBorderArc((Integer) getBorderArcSpinner().getValue());
      label.setShowBorder(getShowBorderCheckBox().isSelected());
      label.setShowBackground(getShowBackgroundCheckBox().isSelected());
      if (getLabelShapeComboBox().getSelectedItem() == null) {
        label.setShape(LabelShape.RECTANGLE);
      } else {
        label.setShape((LabelShape) getLabelShapeComboBox().getSelectedItem());
      }
      label.setHorizontalPadding((Integer) getHorizontalPaddingSpinner().getValue());
      label.setVerticalPadding((Integer) getVerticalPaddingSpinner().getValue());
      label.setLabel(getLabelTextField().getText());
      label.setGmOnly(getGMOnlyCheckBox().isSelected());
    }

    /**
     * Retrieves the foreground color well component.
     *
     * @return The foreground color well component.
     */
    public ColorWell getForegroundColorWell() {
      return (ColorWell) getComponent("foregroundColor");
    }

    /**
     * Retrieves the background color well component.
     *
     * @return The background color well component.
     */
    public ColorWell getBackgroundColorWell() {
      return (ColorWell) getComponent("backgroundColor");
    }

    /**
     * Retrieves the border color well component.
     *
     * @return The border color well component.
     */
    public ColorWell getBorderColorWell() {
      return (ColorWell) getComponent("borderColor");
    }

    /**
     * Retrieves the border width spinner component from EditLabelPanel.
     *
     * @return The border width spinner component.
     */
    public JSpinner getBorderWidthSpinner() {
      return (JSpinner) getComponent("borderWidth");
    }

    /**
     * Retrieves the border arc spinner component from EditLabelPanel.
     *
     * @return The border arc spinner component.
     */
    public JSpinner getBorderArcSpinner() {
      return (JSpinner) getComponent("borderArc");
    }

    /**
     * Retrieves the font size spinner component from EditLabelPanel.
     *
     * @return The font size spinner component.
     */
    public JSpinner getFontSizeSpinner() {
      return (JSpinner) getComponent("fontSize");
    }

    /**
     * Retrieves the label text field component from EditLabelPanel.
     *
     * @return The label text field component.
     */
    public JTextField getLabelTextField() {
      return (JTextField) getComponent("@label");
    }

    /**
     * Retrieves the OK button component from EditLabelPanel.
     *
     * @return The OK button component.
     */
    public JButton getOKButton() {
      return (JButton) getComponent("okButton");
    }

    public JButton getManagePresetsButton() {
      return (JButton) getComponent("managePresetsButton");
    }

    public JComboBox<String> getLabelPresetsComboBox() {
      return (JComboBox<String>) getComponent("presetsComboBox");
    }

    public JCheckBox getShowBorderCheckBox() {
      return (JCheckBox) getComponent("@showBorder");
    }

    public JCheckBox getShowBackgroundCheckBox() {
      return (JCheckBox) getComponent("@showBackground");
    }

    public JLabel getLabelPreview() {
      return (JLabel) getComponent("labelPreview");
    }

    public JComboBox<LabelShape> getLabelShapeComboBox() {
      return (JComboBox<LabelShape>) getComponent("labelShape");
    }

    public JSpinner getHorizontalPaddingSpinner() {
      return (JSpinner) getComponent("horizontalPadding");
    }

    public JSpinner getVerticalPaddingSpinner() {
      return (JSpinner) getComponent("verticalPadding");
    }

    public JCheckBox getGMOnlyCheckBox() {
      return (JCheckBox) getComponent("gmOnlyCheckBox");
    }

    public JLabel getPresetLabel() {
      return (JLabel) getComponent("presetLabel");
    }

    public JPanel getPropertiesPanel() {
      return (JPanel) getComponent("propertiesPanel");
    }

    /**
     * Initializes the OK button by adding an ActionListener that handles the button click event.
     * Upon clicking the OK button, the dialog's 'accepted' flag is set to true, the commit() method
     * is called, and the dialog is closed.
     */
    public void initOKButton() {
      getOKButton()
          .addActionListener(
              e -> {
                dialog.accepted = true;
                commit();
                close();
              });
    }

    public void initPresetsComboBox() {
      getLabelPresetsComboBox()
          .addActionListener(
              e -> {
                if (updatingPresetComboBox) {
                  return;
                }
                var presetName = (String) getLabelPresetsComboBox().getSelectedItem();
                if (presetName != null
                    && !presetName.isEmpty()
                    && !presetName.equals(noPresetName)) {
                  var label = labelManager.getPresets().getPreset(presetName);
                  if (label != null) {
                    copyLabelToValues(label, getLabelTextField().getText());
                    workingLabel.copyValuesFrom(label);
                    workingLabel.setPresetId(label.getId());
                  } else {
                    workingLabel.setPresetId(null);
                  }
                  handlePresets();
                } else {
                  workingLabel.setPresetId(null);
                }
                adjustControls();
                dialog.pack();
              });
    }

    public void initShowBackgroundCheckBox() {
      getShowBackgroundCheckBox()
          .addItemListener(
              l -> {
                adjustControls();
                generatePreview();
              });
    }

    public void initManagePresetsButton() {
      getManagePresetsButton()
          .addActionListener(
              e -> {
                new LabelPresetsDialog(labelManager).setVisible(true);
                handlePresets();
              });
    }

    public void initPreview() {
      getLabelTextField()
          .getDocument()
          .addDocumentListener(
              new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                  generatePreview();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                  generatePreview();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                  generatePreview();
                }
              });
      getFontSizeSpinner()
          .addChangeListener(
              e -> {
                adjustControls();
                generatePreview();
              });
      getForegroundColorWell()
          .addActionListener(
              e -> {
                adjustControls();
                generatePreview();
              });
      getBackgroundColorWell()
          .addActionListener(
              e -> {
                adjustControls();
                generatePreview();
              });
      getBorderColorWell()
          .addActionListener(
              e -> {
                adjustControls();
                generatePreview();
              });
      getBorderWidthSpinner()
          .addChangeListener(
              e -> {
                adjustControls();
                generatePreview();
              });
      getBorderArcSpinner()
          .addChangeListener(
              e -> {
                adjustControls();
                generatePreview();
              });
      getShowBorderCheckBox()
          .addChangeListener(
              e -> {
                adjustControls();
                generatePreview();
              });
      getShowBackgroundCheckBox()
          .addChangeListener(
              e -> {
                adjustControls();
                generatePreview();
              });
      getLabelShapeComboBox()
          .addActionListener(
              e -> {
                adjustControls();
                generatePreview();
              });
      getHorizontalPaddingSpinner()
          .addChangeListener(
              e -> {
                adjustControls();
                generatePreview();
              });
      getVerticalPaddingSpinner()
          .addChangeListener(
              e -> {
                adjustControls();
                generatePreview();
              });
      getGMOnlyCheckBox()
          .addChangeListener(
              e -> {
                adjustControls();
              });

      generatePreview();
    }

    public void initShowBorderCheckBox() {
      getShowBorderCheckBox()
          .addItemListener(
              l -> {
                adjustControls();
              });
    }

    public void initLabelShape() {
      Arrays.stream(LabelShape.values()).forEach(getLabelShapeComboBox()::addItem);
    }

    private void handlePresets() {
      updatingPresetComboBox = true;
      var combo = getLabelPresetsComboBox();
      combo.removeAllItems();
      var presets = labelManager.getPresets().getPresetNames().stream().sorted().toArray();
      combo.addItem(noPresetName);
      for (var preset : presets) {
        combo.addItem((String) preset);
      }
      if (workingLabel.getPreset() != null) {
        var presetName = labelManager.getPresets().getPresetName(workingLabel.getPreset());
        if (presetName != null) {
          combo.setSelectedItem(presetName);
          adjustControls();
        }
      }
      updatingPresetComboBox = false;
    }

    private void generatePreview() {
      var label = new Label(getLabelTextField().getText(), 0, 0);
      copyValuesToLabel(label);
      var flatLabel = new FlatImageLabelFactory().getMapImageLabel(label);

      var text = label.getLabel();
      if (text == null || text.isEmpty()) {
        text = "";
      }
      var img = flatLabel.renderImage(text);
      getLabelPreview().setIcon(new ImageIcon(img));
      dialog.pack(); // Contents of dialog may have changed size.
    }

    public void adjustControls() {

      getPresetLabel().setVisible(!editingPresets);
      getLabelPresetsComboBox().setVisible(!editingPresets);
      getManagePresetsButton().setVisible(!editingPresets);

      getPropertiesPanel().setVisible(workingLabel.getPreset() == null);

      getBorderColorWell()
          .setVisible(workingLabel.isShowBorder()); // disabling a ColorWell does nothing.
      getBorderWidthSpinner().setEnabled(workingLabel.isShowBorder());
      getBorderArcSpinner().setEnabled(workingLabel.isShowBorder());
      var flatLabel = new FlatImageLabelFactory().getMapImageLabel(workingLabel);
      if (flatLabel.supportsArcCorner()) {
        getBorderArcSpinner().setEnabled(workingLabel.isShowBorder());
      } else {
        getBorderArcSpinner().setEnabled(false);
      }
    }

    /**
     * Initializes the cancel button by adding an ActionListener that handles the button click
     * event. Upon clicking the cancel button, the dialog is closed.
     */
    public void initCancelButton() {
      ((JButton) getComponent("cancelButton")).addActionListener(e -> close());
    }

    /** Closes the dialog. */
    private void close() {
      dialog.setVisible(false);
    }
  }
}
