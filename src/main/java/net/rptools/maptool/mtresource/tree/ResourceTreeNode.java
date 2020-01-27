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
package net.rptools.maptool.mtresource.tree;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
import net.rptools.maptool.mtresource.resource.MTResource;

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

  public Collection<ResourceTreeNode> getChildren() {
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
