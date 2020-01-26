package net.rptools.maptool.mtresource.tree;

import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

public class ResourceTreeConverter {


  public TreeNode convert(ResourceTreeNode node) {
    DefaultMutableTreeNode mtn = new DefaultMutableTreeNode();
    // TODO: CDW mtn.setUserObject(new TreeNodeAdaptor(node));
    mtn.setUserObject(node);

    for (ResourceTreeNode child : node.getChildren()) {
      mtn.add((DefaultMutableTreeNode) convert(child));
    }

    return mtn;
  }

}
