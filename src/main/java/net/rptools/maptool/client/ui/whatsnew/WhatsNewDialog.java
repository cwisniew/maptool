package net.rptools.maptool.client.ui;

import javax.swing.SwingUtilities;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import net.rptools.maptool.client.AppConstants;
import net.rptools.maptool.client.MapTool;
import net.rptools.maptool.client.ui.javfx.SwingJavaFXDialog;
import net.rptools.maptool.language.I18N;

public class WhatsNewDialog {

  private final String whatsNew;


  public WhatsNewDialog(String content) {
    whatsNew = content;
  }


  public void show() {
    if (SwingUtilities.isEventDispatchThread()) {
      showEDT();
    } else {
      SwingUtilities.invokeLater(this::showEDT);
    }
  }


  private void showEDT() {
      JFXPanel panel = new JFXPanel();
      Platform.runLater(() -> {
        Group root = new Group();
        Scene scene = new Scene(root);
        Text text = new Text("This is a test");
        root.getChildren().add(text);

        panel.setScene(scene);

      });
    SwingJavaFXDialog dialog = new SwingJavaFXDialog(I18N.getText("action.whatsNew.title"), MapTool.getFrame(), panel);
    dialog.showDialog();
  }

}
