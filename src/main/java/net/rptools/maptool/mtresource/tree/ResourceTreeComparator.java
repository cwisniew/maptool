package net.rptools.maptool.mtresource.tree;

import java.util.Comparator;

public class ResourceTreeComparator implements Comparator<ResourceTreeNode> {

  @Override
  public int compare(ResourceTreeNode o1, ResourceTreeNode o2) {
    if (o1.isDirectory() && o2.isDirectory()) {
      return o1.getDirectoryName().compareTo(o2.getDirectoryName());
    } else if (o1.isDirectory()) {
      return -1; // Directory comes before non directory
    } else if (o2.isDirectory()) {
      return 1; // Directory comes before non directory
    } else {
      return o1.getResource().getName().compareTo(o2.getResource().getName());
    }
  }
}
