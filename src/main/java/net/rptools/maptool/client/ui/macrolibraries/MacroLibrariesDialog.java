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
    SwingJavaFXDialog dialog = new SwingJavaFXDialog(I18N.getText("macro.libraries.dialogTitle"),
        MapTool.getFrame(), panel);
    Platform.runLater(() ->{
      // TODO: CDW: init goes here
    });
    dialog.showDialog();
  }

}
