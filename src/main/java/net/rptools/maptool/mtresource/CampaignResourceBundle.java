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
package net.rptools.maptool.mtresource;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.tree.TreeNode;
import net.rptools.maptool.mtresource.resource.MTResource;
import net.rptools.maptool.mtresource.tree.ResourceTreeConverter;
import net.rptools.maptool.mtresource.tree.ResourceTreeNode;

public class CampaignResourceBundle implements MTResourceBundle {

  private final Object lock = new Object();

  private final UUID id;
  private String name;
  private String qualifiedName;
  private String shortDescription;
  private String longDescription;

  private final ResourceTreeNode resourceTree = new ResourceTreeNode("/");

  private final Map<UUID, MTResource> resourceIdMap = new ConcurrentHashMap<>();

  private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

  public CampaignResourceBundle(
      String bundleName, String qname, String shortDesc, String longDesc) {
    name = bundleName;
    qualifiedName = qname;
    shortDescription = shortDesc;
    longDescription = longDesc;
    id = UUID.randomUUID();
  }

  @Override
  public UUID getId() {
    return id;
  }

  @Override
  public String getName() {
    synchronized (lock) {
      return name;
    }
  }

  @Override
  public void setName(String bundleName) {
    String oldName;
    synchronized (lock) {
      oldName = name;
      name = bundleName;
    }
    propertyChangeSupport.firePropertyChange("name", oldName, bundleName);
  }

  @Override
  public String getQualifiedName() {
    synchronized (lock) {
      return qualifiedName;
    }
  }

  @Override
  public void setQualifiedName(String qname) {
    String oldQName;
    synchronized (lock) {
      oldQName = qualifiedName;
      qualifiedName = qname;
    }
    propertyChangeSupport.firePropertyChange("version", oldQName, qname);
  }

  @Override
  public Optional<MTResource> getResource(String fullPath) {
    List<String> pathList = pathAsList(fullPath);
    String filename = pathList.get(pathList.size() - 1);
    pathList = pathList.subList(0, pathList.size() - 1);
    return getResource(pathList, filename);
  }

  @Override
  public Optional<MTResource> getResource(String path, String filename) {
    return getResource(pathAsList(path), filename);
  }

  private Optional<MTResource> getResource(List<String> path, String filename) {
    synchronized (lock) {
      ResourceTreeNode node = getPath(resourceTree, path, false);

      if (node != null) {
        for (ResourceTreeNode child : node.getChildren()) {
          if (filename.equals(child.getResource().getName())) {
            return Optional.of(child.getResource());
          }
        }
      }
    }

    return Optional.empty();
  }

  @Override
  public void putResource(String path, MTResource res) {

    synchronized (lock) {
      ResourceTreeNode parent = getPath(resourceTree, pathAsList(path), true);

      parent.addChild(res);

      resourceIdMap.put(res.getId(), res);
    }
  }

  @Override
  public int getNumberOfResources() {
    return resourceIdMap.size();
  }

  @Override
  public void addPropertyChangeListener(PropertyChangeListener pcl) {
    propertyChangeSupport.addPropertyChangeListener(pcl);
  }

  @Override
  public void removePropertyChangeListener(PropertyChangeListener pcl) {
    propertyChangeSupport.removePropertyChangeListener(pcl);
  }

  @Override
  public String getShortDescription() {
    synchronized (lock) {
      return shortDescription;
    }
  }

  @Override
  public void setShortDescription(String desc) {
    String oldDesc;
    synchronized (lock) {
      oldDesc = shortDescription;
      shortDescription = desc;
    }
    propertyChangeSupport.firePropertyChange("shortDescription", oldDesc, desc);
  }

  @Override
  public String getLongDescription() {
    synchronized (lock) {
      return longDescription;
    }
  }

  @Override
  public void setLongDescription(String desc) {
    String oldDesc;
    synchronized (lock) {
      oldDesc = longDescription;
      longDescription = desc;
    }
    propertyChangeSupport.firePropertyChange("longDescription", oldDesc, desc);
  }

  @Override
  public TreeNode getResourceTree() {
    synchronized (lock) {
      ResourceTreeConverter converter = new ResourceTreeConverter();
      return converter.convert(resourceTree);
    }
  }

  @Override
  public int compareTo(MTResourceBundle o) {
    synchronized (lock) {
      int compare = name.compareTo(o.getName());
      if (compare != 0) {
        return compare;
      }
      return qualifiedName.compareTo(o.getQualifiedName());
    }
  }

  private ResourceTreeNode getPath(ResourceTreeNode parent, List<String> path, boolean create) {

    if (path.isEmpty()) {
      return parent;
    }

    String name = path.get(0);
    List<String> remainingPath = path.subList(1, path.size());

    synchronized (lock) {
      for (ResourceTreeNode child : parent.getChildren()) {
        if (child.isDirectory() && child.getDirectoryName().equals(name)) {
          return getPath(child, remainingPath, create);
        }
      }

      // If it gets here there is no matching path.
      if (create) {
        ResourceTreeNode newNode = new ResourceTreeNode(name);
        parent.addChild(newNode);
        return getPath(newNode, remainingPath, create);
      } else {
        return null;
      }
    }
  }

  private List<String> pathAsList(String path) {
    String trimmedPath = path.replaceAll("^/", "").replaceAll("/$", "").trim();
    if (trimmedPath.length() == 0) {
      return List.of();
    } else {
      return Arrays.asList(trimmedPath.split("/"));
    }
  }
}
