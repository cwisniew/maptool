package net.rptools.maptool.client.ui.javfx;

import java.io.IOException;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.util.Callback;
import javax.swing.SwingUtilities;
import net.rptools.maptool.client.MapTool;
import net.rptools.maptool.language.I18N;

public class FXMLLoaderUtil {

  private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
      .getBundle("net.rptools.maptool.language.i18n");

  public Parent parentFromFXML(String fxmlPath) throws IOException {
    JFXPanel panel = new JFXPanel();
    javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource(fxmlPath),
        RESOURCE_BUNDLE);
    Parent parentControl = loader.load();
    Scene scene = new Scene(parentControl);
    panel.setScene(scene);

    return parentControl;
  }

  public void jfxPanelFromFXML(String fxmlPath, Consumer<JFXPanel> callback) {
    Platform.runLater(() -> {
      JFXPanel panel = new JFXPanel();
      try {
        Parent parent = parentFromFXML(fxmlPath);
        panel.setScene(parent.getScene());
        SwingUtilities.invokeLater(() -> callback.accept(panel));
      } catch (IOException e) {
        MapTool.showError(I18N.getText("javafx.error.errorLoadingFXML", fxmlPath), e);
      }
    });
  }

}

