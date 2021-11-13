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

  static class AddOnInfo implements MapToolJSAPIInterface {
    AddOnInfo(String namespace, String version) {
      this.namespace = namespace;
      this.version = version;
    }

    @HostAccess.Export
    public String namespace;
    @HostAccess.Export
    public String version;

    @Override
    public String serializeToString() {
      // this be messy and dont work :(
      return "{ \"namespace\": " +  namespace +  ", \"version\": " +  version +  " }";
    }
  }

  static class AddOnInfo2 {
    AddOnInfo2(String namespace, String version) {
      this.namespace = namespace;
      this.version = version;
    }

    @HostAccess.Export
    public String namespace;
    @HostAccess.Export
    public String version;

  }

  @HostAccess.Export
  public List<AddOnInfo> getAddonVersions() {
     return new LibraryManager().getAddonLibraries().stream().map(l ->
            new AddOnInfo(l.getNamespace().join(), l.getVersion().join()))
                .collect(Collectors.toList());
  }


  @HostAccess.Export
  public List<AddOnInfo2> getAddonVersions2() {
    return new LibraryManager().getAddonLibraries().stream().map(l ->
            new AddOnInfo2(l.getNamespace().join(), l.getVersion().join()))
        .collect(Collectors.toList());
  }

  @HostAccess.Export
  public List<Map<String, String>> getAddonVersions1() {
    return new LibraryManager().getAddonLibraries().stream().map(l ->
        Map.of("namespace", l.getNamespace().join(), "version", l.getVersion().join())).toList();
  }
}
