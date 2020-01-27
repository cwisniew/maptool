/*
 * This software Copyright by the RPTools.net development team, and
 * licensed under the Affero GPL Version 3 or, at your option, any later
 * version.
 *
 * MapTool Source Code is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * You should have received a copy of the GNU Affero General Public
 * License * along with this source Code.  If not, please visit
 * <http://www.gnu.org/licenses/> and specifically the Affero license
 * text at <http://www.gnu.org/licenses/agpl.html>.
 */
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
    renderer.add(resourceType);
    renderer.add(resourceName);

    resourceName.setFont(Theme.treeFont.getFont());
    resourceName.setForeground(Theme.treeTextColor.getColor());
    resourceName.setBackground(Theme.treeTextBgColor.getColor());

    resourceType.setFont(Theme.treeFont.getFont());
    resourceType.setForeground(Theme.treeTextColor.getColor());
    resourceType.setBackground(Theme.treeTextBgColor.getColor());

    renderer.setBackground(Theme.treeBgColor.getColor());
  }

  @Override
  public Component getTreeCellRendererComponent(
      JTree tree,
      Object value,
      boolean selected,
      boolean expanded,
      boolean leaf,
      int row,
      boolean hasFocus) {

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
      returnVal =
          defaultRenderer.getTreeCellRendererComponent(
              tree, value, selected, expanded, leaf, row, hasFocus);
    }

    return returnVal;
  }
}
