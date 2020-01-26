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

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import net.rptools.maptool.mtresource.MTResourceBundle;
import net.rptools.maptool.mtresource.tree.ResourceTreeConverter;

public class ResourceTreePanel extends JPanel {

  private JTree resourcesJTree;
  private TreeNode root = new DefaultMutableTreeNode(""); // TODO: CDW:
  private TreeModel resourcesTree = new DefaultTreeModel(root);

  public ResourceTreePanel() {
    setLayout(new BorderLayout());
    resourcesJTree = new JTree(resourcesTree);
    JScrollPane scrollPane = new JScrollPane(resourcesJTree);
    add(scrollPane, BorderLayout.CENTER);
  }

  public void setResourceBundle(MTResourceBundle resourceBundle) {

    // TODO: CDW
    ResourceTreeConverter converter = new ResourceTreeConverter();
    root = resourceBundle.getResourceTree();
    resourcesTree = new DefaultTreeModel(root);
    resourcesJTree.setModel(resourcesTree);
    resourcesJTree.setShowsRootHandles(false);
    resourcesJTree.setCellRenderer(new ResourceTreeCellRenderer());

    System.out.println(resourceBundle);
  }
}
