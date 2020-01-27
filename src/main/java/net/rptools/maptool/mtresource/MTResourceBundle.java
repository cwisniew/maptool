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
import java.util.Optional;
import java.util.UUID;
import javax.swing.tree.TreeNode;
import net.rptools.maptool.mtresource.resource.MTResource;

public interface MTResourceBundle extends Comparable<MTResourceBundle> {

  UUID getId();

  String getName();

  void setName(String bundleName);

  String getQualifiedName();

  void setQualifiedName(String qname);

  Optional<MTResource> getResource(String fullpath);

  Optional<MTResource> getResource(String path, String name);

  void putResource(String path, MTResource res);

  int getNumberOfResources();

  void addPropertyChangeListener(PropertyChangeListener pcl);

  void removePropertyChangeListener(PropertyChangeListener pcl);

  String getShortDescription();

  void setShortDescription(String desc);

  String getLongDescription();

  void setLongDescription(String desc);

  TreeNode getResourceTree();
}
