package net.rptools.maptool.client.ui;

import javax.swing.SwingUtilities;
import net.rptools.maptool.client.AppConstants;

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
    // TODO CDW: Here
  }

}
