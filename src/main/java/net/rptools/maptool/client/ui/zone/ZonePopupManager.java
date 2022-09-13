package net.rptools.maptool.client.ui.zone;

import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class ZonePopupManager {

  private final JPanel popupPanel;

  private final Map<JComponent, String> popupComponents = new HashMap<>();


  public ZonePopupManager(JPanel panel) {
    popupPanel = panel;
    popupPanel.setOpaque(false);
    popupPanel.setLayout(null);
  }

  public void addComponent(JComponent component, String tag, int x, int y) {
    popupComponents.put(component, tag);
    var insets = popupPanel.getInsets();
    var preferredSize = component.getPreferredSize();
    component.setBounds(x + insets.left, y + insets.top, preferredSize.width, preferredSize.height);
    popupPanel.add(component);
    popupPanel.revalidate();
    popupPanel.repaint();
  }

  public void removeComponent(JComponent component) {
    if (popupComponents.containsKey(component)) {
      popupComponents.remove(component);
      popupPanel.remove(component);
      popupPanel.repaint();
    }
  }

  public void removeAllComponents(String tag) {
    var toRemove =
        popupComponents.entrySet().stream().filter(e -> e.getValue().equals(tag))
            .map(Map.Entry::getKey).toList();
    if (!toRemove.isEmpty()) {
      toRemove.forEach(popupPanel::remove);
      toRemove.forEach(popupComponents.keySet()::remove);
      popupPanel.repaint();
    }
  }

  public void clear() {
    if (!popupComponents.isEmpty()) {
      popupComponents.keySet().forEach(popupPanel::remove);
      popupComponents.clear();
      popupPanel.repaint();
    }
  }


}
