package net.rptools.maptool.client.tool;

import com.google.common.eventbus.Subscribe;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import net.rptools.maptool.model.zone.ElevationLevel;

public class ElevationDialog extends JPanel {

  private final JList<ElevationLevel> elevationLevelJList = new JList<>();

  public ElevationDialog() {
    super();
  }



}
