package net.rptools.maptool.client.ui.macrolibraries;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.web.WebView;

public class MacroLibrariesController {

  @FXML // ResourceBundle that was given to the FXMLLoader
  private ResourceBundle resources;

  @FXML // URL location of the FXML file that was given to the FXMLLoader
  private URL location;

  @FXML // fx:id="installedLibrariesList"
  private ListView<?> installedLibrariesList; // Value injected by FXMLLoader

  @FXML // fx:id="availableLibrariesList"
  private ListView<?> availableLibrariesList; // Value injected by FXMLLoader

  @FXML // fx:id="librariesWebView"
  private WebView librariesWebView; // Value injected by FXMLLoader

  @FXML // fx:id="updateAllButton"
  private Button updateAllButton; // Value injected by FXMLLoader

  @FXML // fx:id="addFromURLButton1"
  private Button addFromURLButton1; // Value injected by FXMLLoader

  @FXML // fx:id="addLocalButton"
  private Button addLocalButton; // Value injected by FXMLLoader

  @FXML // fx:id="closeButton"
  private Button closeButton; // Value injected by FXMLLoader

  @FXML // This method is called by the FXMLLoader when initialization is complete
  void initialize() {
    assert installedLibrariesList != null : "fx:id=\"installedLibrariesList\" was not injected: check your FXML file 'MacroLibraries.fxml'.";
    assert availableLibrariesList != null : "fx:id=\"availableLibrariesList\" was not injected: check your FXML file 'MacroLibraries.fxml'.";
    assert librariesWebView != null : "fx:id=\"librariesWebView\" was not injected: check your FXML file 'MacroLibraries.fxml'.";
    assert updateAllButton != null : "fx:id=\"updateAllButton\" was not injected: check your FXML file 'MacroLibraries.fxml'.";
    assert addFromURLButton1 != null : "fx:id=\"addFromURLButton1\" was not injected: check your FXML file 'MacroLibraries.fxml'.";
    assert addLocalButton != null : "fx:id=\"addLocalButton\" was not injected: check your FXML file 'MacroLibraries.fxml'.";
    assert closeButton != null : "fx:id=\"closeButton\" was not injected: check your FXML file 'MacroLibraries.fxml'.";

  }
}
