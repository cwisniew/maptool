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

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentHashMap.KeySetView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.web.WebView;
import javax.swing.SwingUtilities;
import net.rptools.maptool.client.ui.javfx.SwingJavaFXDialogController;
import net.rptools.maptool.client.ui.javfx.SwingJavaFXDialogEventHandler;

/**
 * Concurrent hash map for listeners
 */
public class MacroLibrariesController implements SwingJavaFXDialogController {

  private final KeySetView<SwingJavaFXDialogEventHandler, Boolean> eventHandlers = ConcurrentHashMap
      .newKeySet();
  @FXML // ResourceBundle that was given to the FXMLLoader
  private ResourceBundle resources;

  @FXML // URL location of the FXML file that was given to the FXMLLoader
  private URL location;

  @FXML // fx:id="updateAllButton"
  private Button updateAllButton; // Value injected by FXMLLoader

  @FXML // fx:id="addFromURLButton1"
  private Button addFromURLButton1; // Value injected by FXMLLoader

  @FXML // fx:id="addLocalButton"
  private Button addLocalButton; // Value injected by FXMLLoader

  @FXML // fx:id="closeButton"
  private Button closeButton; // Value injected by FXMLLoader

  @FXML // fx:id="installedLibrariesList"
  private ListView<?> installedLibrariesList; // Value injected by FXMLLoader

  @FXML // fx:id="librariesWebView"
  private WebView librariesWebView; // Value injected by FXMLLoader

  @FXML
    // This method is called by the FXMLLoader when initialization is complete
  void initialize() {
    assert updateAllButton
        != null : "fx:id=\"updateAllButton\" was not injected: check your FXML file 'MacroLibraries.fxml'.";
    assert addFromURLButton1
        != null : "fx:id=\"addFromURLButton1\" was not injected: check your FXML file 'MacroLibraries.fxml'.";
    assert addLocalButton
        != null : "fx:id=\"addLocalButton\" was not injected: check your FXML file 'MacroLibraries.fxml'.";
    assert closeButton
        != null : "fx:id=\"closeButton\" was not injected: check your FXML file 'MacroLibraries.fxml'.";
    assert installedLibrariesList
        != null : "fx:id=\"installedLibrariesList\" was not injected: check your FXML file 'MacroLibraries.fxml'.";
    assert librariesWebView
        != null : "fx:id=\"librariesWebView\" was not injected: check your FXML file 'MacroLibraries.fxml'.";

  }


  @FXML
  void closeButtonAction(ActionEvent event) {
    eventHandlers.forEach(h -> h.close(this));
  }

  @FXML
  void addLocalAction(ActionEvent event) {
    System.out.println("addLocal");
  }

  @FXML
  void addFromURLAction(ActionEvent event) {
    System.out.println("addURL");
  }

  @FXML
  void createAction(ActionEvent event) {
    SwingUtilities.invokeLater(() -> new MacroLibraryEditDialog().show());
    System.out.println("create");
  }

  @Override
  public void registerEventHandler(SwingJavaFXDialogEventHandler handler) {
    eventHandlers.add(handler);
  }

  @Override
  public void deregisterEventHandler(SwingJavaFXDialogEventHandler handler) {
    eventHandlers.remove(handler);
  }

  @Override
  public void init() {
    // TODO: CDW
  }
}
