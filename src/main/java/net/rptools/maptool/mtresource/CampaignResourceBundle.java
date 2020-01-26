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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import javax.swing.tree.TreeNode;
import net.rptools.maptool.mtresource.tree.ResourceTreeConverter;
import net.rptools.maptool.mtresource.tree.ResourceTreeNode;
import org.jetbrains.annotations.NotNull;

public class CampaignResourceBundle implements MTResourceBundle {

  private String name;
  private String qualifiedName;
  private String version;

  private final ResourceTreeNode resourceTree = new ResourceTreeNode("/");

  private final Map<UUID, MTResource> resourceIdMap = new HashMap<>();


  private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

  @Override
  public UUID getId() {
    return null;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void setName(String bundleName) {
    propertyChangeSupport.firePropertyChange("name", name, bundleName);
    name = bundleName;
  }

  @Override
  public String getVersion() {
    return version;
  }

  @Override
  public void setVersion(String versionString) {
    propertyChangeSupport.firePropertyChange("version", version, versionString);
    version = versionString;
  }

  @Override
  public String getQualifiedName() {
    return qualifiedName;
  }

  @Override
  public void setQualifiedName(String qname) {
    propertyChangeSupport.firePropertyChange("version", qualifiedName, qname);
    qualifiedName = qname;
  }

  @Override
  public Optional<MTResource> getResource(String fullPath) {
    List<String> pathList = pathAsList(fullPath);
    String name = pathList.get(pathList.size() - 1);
    pathList = pathList.subList(0, pathList.size() - 1);
    return getResource(pathList, name);
  }

  @Override
  public Optional<MTResource> getResource(String path, String name) {
    return getResource(pathAsList(path), name);
  }

  private Optional<MTResource> getResource(List<String> path, @NotNull String name) {
    ResourceTreeNode node = getPath(resourceTree, path, false);

    if (node != null) {
      for (ResourceTreeNode child : node.getChildren()) {
        if (name.equals(child.getResource().getName())) {
          return Optional.of(child.getResource());
        }
      }
    }

    return Optional.empty();
  }

  @Override
  public void putResource(String path, MTResource res) {

    ResourceTreeNode parent = getPath(resourceTree, pathAsList(path), true);

    parent.addChild(res);

    resourceIdMap.put(res.getId(), res);

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
  public TreeNode getResourceTree() {
    ResourceTreeConverter converter = new ResourceTreeConverter();
    return converter.convert(resourceTree);
  }

  @Override
  public int compareTo(@NotNull MTResourceBundle o) {
    int compare = name.compareTo(o.getName());
    if (compare != 0) {
      return compare;
    }
    compare = qualifiedName.compareTo(o.getQualifiedName());
    if (compare != 0) {
      return compare;
    }

    return compare = version.compareTo(o.getVersion());
  }

  private ResourceTreeNode getPath(ResourceTreeNode parent, List<String> path, boolean create) {

    if (path.isEmpty()) {
      return parent;
    }

    String name = path.get(0);
    List<String> remainingPath = path.subList(1, path.size());

    for (ResourceTreeNode child : parent.getChildren()) {
      if (child.isDirectory()  && child.getDirectoryName().equals(name)) {
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


  private List<String> pathAsList(String path) {
    String trimmedPath = path.replaceAll("^/", "").replaceAll("/$", "").trim();
    if (trimmedPath.length() == 0) {
      return List.of();
    } else {
      return Arrays.asList(trimmedPath.split("/"));
    }
  }

  @Override
  public String toString() {
    return "CampaignResourceBundle{" +
        "name='" + name + '\'' +
        ", qualifiedName='" + qualifiedName + '\'' +
        ", version='" + version + '\'' +
        ", resourceTree=" + resourceTree +
        ", resourceIdMap=" + resourceIdMap +
        ", propertyChangeSupport=" + propertyChangeSupport +
        '}';
  }
}
