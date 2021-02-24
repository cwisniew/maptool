package net.rptools.maptool.client.ui.whatsnew;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.web.WebView;

public class WhatsNewController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="whatsNewWebView"
    private WebView whatsNewWebView; // Value injected by FXMLLoader

    @FXML // fx:id="okButton"
    private Button okButton; // Value injected by FXMLLoader

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert whatsNewWebView != null : "fx:id=\"whatsNewWebView\" was not injected: check your FXML file 'Untitled'.";
        assert okButton != null : "fx:id=\"okButton\" was not injected: check your FXML file 'Untitled'.";

    }
}
