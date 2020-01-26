package net.rptools.maptool.client.ui.campaignres;

import de.muntjak.tinylookandfeel.Theme;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import net.rptools.maptool.mtresource.tree.ResourceTreeNode;

public class ResourceTreeCellRenderer implements TreeCellRenderer {
  private final JLabel resourceName = new JLabel();
  private final JLabel resourceType = new JLabel();
  private final JPanel renderer = new JPanel();

  private static final DefaultTreeCellRenderer defaultRenderer = new DefaultTreeCellRenderer();

  public ResourceTreeCellRenderer() {
    renderer.add(resourceName);
    renderer.add(resourceType);

    resourceName.setFont(Theme.treeFont.getFont());
    resourceName.setForeground(Theme.treeTextColor.getColor());
    resourceName.setBackground(Theme.treeTextBgColor.getColor());

    resourceType.setFont(Theme.treeFont.getFont());
    resourceType.setForeground(Theme.treeTextColor.getColor());
    resourceType.setBackground(Theme.treeTextBgColor.getColor());

    renderer.setBackground(Theme.treeBgColor.getColor());

  }

  @Override
  public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected,
      boolean expanded, boolean leaf, int row, boolean hasFocus) {

    Component returnVal = null;

    if (value != null && value instanceof DefaultMutableTreeNode) {
      DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
      Object userObject = node.getUserObject();
      if (userObject != null && userObject instanceof ResourceTreeNode) {
        ResourceTreeNode rtn = (ResourceTreeNode) node.getUserObject();
        if (rtn.isDirectory()) {
          resourceName.setText(rtn.getDirectoryName());
          resourceType.setText("");
        } else {
          resourceName.setText(rtn.getResource().getName());
          resourceType.setText(rtn.getResource().getResourceType().name());
        }
        return renderer;
      }
    }

    if (returnVal == null) {
      returnVal = defaultRenderer.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
    }

    return returnVal;
  }
}
