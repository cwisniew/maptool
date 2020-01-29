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
package net.rptools.maptool.client.ui.campaignres;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import net.rptools.maptool.client.MapTool;
import net.rptools.maptool.client.swing.AbeillePanel;
import net.rptools.maptool.client.swing.GenericDialog;
import net.rptools.maptool.language.I18N;
import net.rptools.maptool.mtresource.MTResourceBundle;

public class ResourceBundleDialog extends AbeillePanel<ResourceBundleDetails> {

  private GenericDialog dialog;
  private ResourceBundleDetails resourceBundleDetails = new ResourceBundleDetails();
  private MTResourceBundle editingBundle;

  public ResourceBundleDialog() {
    super("net/rptools/maptool/client/ui/forms/resourceBundleDialog.xml");
    panelInit();
  }

  public ResourceBundleDialog(MTResourceBundle editing) {
    super("net/rptools/maptool/client/ui/forms/resourceBundleDialog.xml");
    editingBundle = editing;
    panelInit();
    JTextField qualifiedName = (JTextField) getComponent("@qualifiedName");
    qualifiedName.setEditable(false);
  }

  public void showDialog() {
    dialog =
        new GenericDialog(I18N.getText("resourceBundle.dialog.title"), MapTool.getFrame(), this);
    if (editingBundle != null) {
      resourceBundleDetails.setBundleName(editingBundle.getName());
      resourceBundleDetails.setQualifiedName(editingBundle.getQualifiedName());
      resourceBundleDetails.setShortDescription(editingBundle.getShortDescription());
      resourceBundleDetails.setLongDescription(editingBundle.getLongDescription());
    }
    bind(resourceBundleDetails);
    JButton cancelButton = (JButton) getComponent("cancelButton");
    cancelButton.addActionListener(l -> dialog.closeDialog());

    JButton okButton = (JButton) getComponent("okButton");
    okButton.addActionListener(l -> handleOkButton());

    JTextField qualilfiedTextField = (JTextField) getComponent("@qualifiedName");
    qualilfiedTextField.setToolTipText(I18N.getText("resourceBundle.dialog.qualifiedName.tooltip"));
    dialog.showDialog();
  }

  private void handleOkButton() {
    commit();
    if (checkRequiredFields()) {
      if (isQualifiedNameValid()) {
        if (editingBundle == null) {
          if (MapTool.getCampaignResourceLibrary()
              .hasResourceBundle(resourceBundleDetails.getQualifiedName())) {
            MapTool.showWarning(I18N.getText("resourceBundle.warning.existingBundle"));
            return;
          }
          MapTool.getCampaignResourceLibrary()
              .putResourceBundle(
                  resourceBundleDetails.getBundleName(),
                  resourceBundleDetails.getQualifiedName(),
                  resourceBundleDetails.getShortDescription(),
                  resourceBundleDetails.getLongDescription());
          dialog.closeDialog();
        } else {
          editingBundle.setName(resourceBundleDetails.getBundleName());
          editingBundle.setShortDescription(resourceBundleDetails.getShortDescription());
          editingBundle.setLongDescription(resourceBundleDetails.getLongDescription());
          dialog.closeDialog();
        }
      } else {
        JLabel label = (JLabel) getComponent("qualifiedNameError");
        label.setText("resourceBundle.dialog.invalidQualifiedName");
      }
    }
  }

  private boolean checkRequiredFields() {
    boolean valid = true;

    JLabel qnameError = (JLabel) getComponent("qualifiedNameError");
    String qname = resourceBundleDetails.getQualifiedName();
    if (qname == null || qname.trim().isEmpty()) {
      qnameError.setText(I18N.getText("resourceBundle.dialog.emptyQualifiedName"));
      resourceBundleDetails.setQualifiedNameError(
          I18N.getText("resourceBundle.dialog.emptyQualifiedName"));
      valid = false;
    } else {
      qnameError.setText("");
      resourceBundleDetails.setQualifiedNameError("");
    }

    JLabel nameError = (JLabel) getComponent("bundleNameError");
    String name = resourceBundleDetails.getBundleName();
    if (name == null || name.trim().isEmpty()) {
      nameError.setText(I18N.getText("resourceBundle.dialog.emptyName"));
      resourceBundleDetails.setBundleNameError(I18N.getText("resourceBundle.dialog.emptyName"));
      valid = false;
    } else {
      nameError.setText("");
      resourceBundleDetails.setBundleNameError("");
    }

    JLabel shortDescError = (JLabel) getComponent("shortDescriptionError");
    String shortDesc = resourceBundleDetails.getShortDescription();
    if (shortDesc == null || shortDesc.trim().isEmpty()) {
      shortDescError.setText(I18N.getText("resourceBundle.dialog.emptyShortDescription"));
      resourceBundleDetails.setShortDescriptionError(
          I18N.getText("resourceBundle.dialog.emptyShortDescription"));
      valid = false;
    } else {
      shortDescError.setText("");
      resourceBundleDetails.setShortDescriptionError("");
    }

    return valid;
  }

  private boolean isQualifiedNameValid() {
    String qname = resourceBundleDetails.getQualifiedName();
    if (qname.indexOf(".") < 0) {
      return false;
    }

    Matcher noInvalidChars = Pattern.compile("[A-z0-9][A-z0-9_\\-\\.]+[A-z0-9]").matcher(qname);
    if (!noInvalidChars.matches()) {
      return false;
    }

    Matcher noDoubleDot = Pattern.compile(".*\\.\\..*").matcher(qname);
    if (noDoubleDot.matches()) {
      return false;
    }

    return true;
  }
}
