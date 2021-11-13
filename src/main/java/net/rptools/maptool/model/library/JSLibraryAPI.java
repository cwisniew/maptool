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
package net.rptools.maptool.model.library;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import net.rptools.maptool.client.script.javascript.api.MapToolJSAPIInterface;
import org.graalvm.polyglot.HostAccess;

public class JSLibraryAPI {

  public static class JSAPIAddOn implements MapToolJSAPIInterface {
    @HostAccess.Export
    public String namespace;
    @HostAccess.Export
    public String version;
    @Override
    public String serializeToString() {
      return String.format("AddOn(namespace=\"%s\", version=\"%s\")", namespace, version);
    }

    public JSAPIAddOn(String name, String version) {
      this.namespace = name;
      this.version = version;
    }


  }

  @HostAccess.Export
  public List<JSAPIAddOn> getAddonVersions() {
     return new LibraryManager().getAddonLibraries().stream().map(l ->
            new JSAPIAddOn(l.getNamespace().join(), l.getVersion().join()))
                .collect(Collectors.toList());
  }
}
