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
package net.rptools.maptool.client.ui.macrolibraries;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import net.rptools.maptool.client.MapTool;
import net.rptools.maptool.client.ui.javfx.FXMLLoaderUtil;
import net.rptools.maptool.client.ui.javfx.SwingJavaFXDialog;
import net.rptools.maptool.language.I18N;

public class MacroLibrariesDialog {
  private static final String FXML_PATH = "/net/rptools/maptool/client/ui/fxml/MacroLibraries.fxml";

  public void show() {
    FXMLLoaderUtil loaderUtil = new FXMLLoaderUtil();
    loaderUtil.jfxPanelFromFXML(FXML_PATH, this::showEDT);
  }

  private void showEDT(JFXPanel panel, Object controller) {
    SwingJavaFXDialog dialog =
        new SwingJavaFXDialog(
            I18N.getText("macro.libraries.dialogTitle"), MapTool.getFrame(), panel);
    Platform.runLater(
        () -> {
          // TODO: CDW: init goes here
        });
    dialog.showDialog();
  }
}
