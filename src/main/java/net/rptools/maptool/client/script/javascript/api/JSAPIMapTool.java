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
package net.rptools.maptool.client.script.javascript.api;

import net.rptools.maptool.client.MapTool;
import org.graalvm.polyglot.HostAccess;

/**
 * Class used to represent the MapTool JavaScript Object.
 */
@MapToolJSAPIDefinition(javaScriptVariableName = "MapTool")
public class JSAPIMapTool implements MapToolJSAPIInterface {
  @Override
  public String serializeToString() {
    return "MapTool";
  }

  /**
   * Returns the current MapTool version.
   *
   * @return the current MapTool version
   */
  @HostAccess.Export
  public String version() {
    return MapTool.getVersion();
  }

  /**
   * Returns if the current MapTool version is a development version or not.
   *
   * @return if the current MapTool version is a development version or not.
   */
  @HostAccess.Export
  public boolean isDevelopment() {
    return MapTool.isDevelopment();
  }

}
