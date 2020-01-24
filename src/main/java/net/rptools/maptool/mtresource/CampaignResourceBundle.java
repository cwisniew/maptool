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
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import org.jetbrains.annotations.NotNull;

public class CampaignResourceBundle implements MTResourceBundle {

  private String name;
  private String qualifiedName;
  private Map<String, MTResource> resources = new TreeMap<>();
  private String version;

  private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

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
  public Optional<MTResource> getResource(String path) {
    return Optional.ofNullable(resources.get(path));
  }

  @Override
  public void putResource(String path, MTResource res) {
    resources.put(path, res);
    propertyChangeSupport.firePropertyChange("putResource", null, res);
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
}
