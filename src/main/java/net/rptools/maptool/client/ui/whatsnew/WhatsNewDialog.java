package net.rptools.maptool.client.ui.whatsnew;

import java.io.IOException;
import javax.swing.SwingUtilities;

import javafx.embed.swing.JFXPanel;
import net.rptools.maptool.client.MapTool;
import net.rptools.maptool.client.ui.javfx.FXMLLoaderUtil;
import net.rptools.maptool.client.ui.javfx.SwingJavaFXDialog;
import net.rptools.maptool.language.I18N;

public class WhatsNewDialog {

  private final String whatsNew;
  private static final String FXML_PATH = "/net/rptools/maptool/client/ui/fxml/WhatsNew.fxml";


  public WhatsNewDialog(String content) {
    whatsNew = content;
  }


  public void show() {
    FXMLLoaderUtil loaderUtil = new FXMLLoaderUtil();
    loaderUtil.jfxPanelFromFXML(FXML_PATH, this::showEDT);
  }


  private void showEDT(JFXPanel panel) {
    SwingJavaFXDialog dialog = new SwingJavaFXDialog(I18N.getText("action.whatsNew.title"), MapTool.getFrame(), panel);
    dialog.showDialog();
  }

}
