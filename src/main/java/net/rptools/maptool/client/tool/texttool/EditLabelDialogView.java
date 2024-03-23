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

import javax.swing.*;
import net.rptools.maptool.client.AppPreferences;
import net.rptools.maptool.client.swing.ColorWell;
import net.rptools.maptool.model.label.presets.LabelPresets;

/**
 * The EditLabelDialogView class represents a dialog view for editing a label. It contains
 * components for specifying label properties such as border color, border width, and border arc.
 * The dialog view can be accessed by calling the getRootComponent() method, which returns the root
 * component of the view.
 */
public class EditLabelDialogView {
  /** The main panel for the dialog. */
  private JPanel mainPanel;

  /** Checkbox to determine whether to show the border. */
  private JCheckBox showBorderCheckBox;

  /** The border colour picker. */
  private ColorWell borderColor;

  /** Spinner for specifying the border width. */
  private JSpinner borderWidthSpinner;

  /** Spinner for specifying the border arc. */
  private JSpinner borderArcSpinner;

  private JComboBox<LabelPresets> labelShape;
  private JSpinner fontSizeSpinner;
  private JSpinner horizontalPaddingSpinner;
  private JSpinner verticalPaddingSpinner;

  /**
   * The EditLabelDialogView class represents a dialog view for editing a label. It contains
   * components for specifying label properties such as border color, border width, and border arc.
   * The dialog view can be accessed by calling the getRootComponent() method, which returns the
   * root component of the view.
   */
  public EditLabelDialogView() {}

  /**
   * Retrieves the root component of the EditLabelDialogView class.
   *
   * @return The root component of the EditLabelDialogView class.
   */
  public JComponent getRootComponent() {
    return mainPanel;
  }

  private void createUIComponents() {
    labelShape = new JComboBox<>();
    fontSizeSpinner =
        new JSpinner(new SpinnerNumberModel(AppPreferences.getMapLabelFontSize(), 8, 256, 1));
    horizontalPaddingSpinner = new JSpinner(new SpinnerNumberModel(4, 0, 256, 1));
    verticalPaddingSpinner = new JSpinner(new SpinnerNumberModel(4, 0, 256, 1));
    borderArcSpinner =
        new JSpinner(new SpinnerNumberModel(AppPreferences.getMapLabelBorderArc(), 0, 256, 1));
    borderWidthSpinner =
        new JSpinner(new SpinnerNumberModel(AppPreferences.getMapLabelBorderWidth(), 0, 256, 1));
  }
}
