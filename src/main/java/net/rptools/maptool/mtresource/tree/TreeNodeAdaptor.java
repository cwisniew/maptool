package net.rptools.maptool.mtresource.tree;


public class TreeNodeAdaptor {

  private final ResourceTreeNode node;

  TreeNodeAdaptor(ResourceTreeNode n) {
    node = n;
  }



  @Override
  public String toString() {
    if (node.isDirectory()) {
      return node.getDirectoryName();
    } else {
      return node.getResource().getName();
    }
  }

}
