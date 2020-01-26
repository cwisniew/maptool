package net.rptools.maptool.mtresource.tree;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
import net.rptools.maptool.mtresource.MTResource;

public class ResourceTreeNode {

  private final MTResource resource;
  private final Set<ResourceTreeNode> children;
  private final String directoryName;

  public ResourceTreeNode(MTResource res) {
    resource = res;
    children = Set.of();
    directoryName = "";
  }

  public ResourceTreeNode(String dirName) {
    resource = null;
    children = new TreeSet<>(new ResourceTreeComparator());
    directoryName = dirName;
  }

  public MTResource getResource() {
    return resource;
  }

  public Collection<ResourceTreeNode>getChildren() {
    return Collections.unmodifiableCollection(children);
  }

  public boolean isDirectory() {
    return directoryName.length() > 0;
  }

  public String getDirectoryName() {
    return directoryName;
  }

  public boolean hasChildren() {
    return children.size() > 0;
  }

  public int getNumberOfChildren() {
    return children.size();
  }

  public void addChild(ResourceTreeNode node) {
    children.add(node);
  }

  public void addChild(MTResource res) {

    addChild(new ResourceTreeNode(res));
  }


}
