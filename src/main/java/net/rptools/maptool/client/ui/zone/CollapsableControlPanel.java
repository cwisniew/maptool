package net.rptools.maptool.client.ui.zone;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class CollapsableControlPanel extends JPanel {
  private final JLabel headingLabel = new JLabel();

  private String headingLabelText = "";

  private String collapedheadingLabelText = "";

  private boolean collapsed = false;

  public CollapsableControlPanel() {
    headingLabel.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        toggleCollapsed();
      }
    });
  }
  public boolean isCollapsed() {
    return collapsed;
  }

  public void setCollapsed(boolean collapsed) {
    this.collapsed = collapsed;
  }

  public void toggleCollapsed() {
    setCollapsed(!isCollapsed());
  }

  protected void setHeading(String heading) {
    headingLabelText = heading;
    if (!isCollapsed()) {
      headingLabel.setText(headingLabelText);
    }
  }

  protected void setCollapsedHeading(String heading) {
    collapedheadingLabelText = heading;
    if (isCollapsed()) {
      headingLabel.setText(heading);
    }
  }

  protected void setContent(JPanel content) {
    add(content);
  }


}
