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

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public class CampaignResourceBundle implements MTResourceBundle {

  private String name;
  private String qualifiedName;
  private Map<String, MTResource> resources = new TreeMap<>();
  private String version;

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void setName(String bundleName) {
    name = bundleName;
  }

  @Override
  public String getVersion() {
    return version;
  }

  @Override
  public void setVersion(String versionString) {
    version = versionString;
  }

  @Override
  public String getQualifiedName() {
    return qualifiedName;
  }

  @Override
  public void setQualifiedName(String qname) {
    qualifiedName = qname;
  }

  @Override
  public Optional<MTResource> getResource(String path) {
    return Optional.ofNullable(resources.get(path));
  }

  @Override
  public void putResource(String path, MTResource res) {
    resources.put(path, res);
  }
}
