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
package net.rptools.maptool.mtresource.resource;

import java.util.Map;

public class MTResourceFactory {

  private static final Map<String, MTResourceType> extensionResourceTypeMap =
      Map.of(
          "txt", MTResourceType.TEXT,
          "js", MTResourceType.JAVA_SCRIPT,
          "html", MTResourceType.HTML,
          "css", MTResourceType.CSS,
          "mtms", MTResourceType.MACRO_SCRIPT,
          "json", MTResourceType.JSON,
          "csv", MTResourceType.CSV);

  public MTResource resourceFor(
      String resourceName, String resourcePath, String filename, String text) {
    MTResourceType resourceType = null;
    int index = filename.lastIndexOf(".");
    if (index >= 0) {
      String fileExtension = filename.substring(index + 1).toLowerCase();
      resourceType = extensionResourceTypeMap.get(fileExtension);
    }

    if (resourceType == null) {
      resourceType = MTResourceType.TEXT; // If we cant find it then its just generic text
    }

    switch (resourceType) {
      case JAVA_SCRIPT:
        return new JavaScriptResource(resourceName, resourcePath, filename, text);
      case HTML:
        return new HTMLResource(resourceName, resourcePath, filename, text);
      case CSS:
        return new CSSResource(resourceName, resourcePath, filename, text);
      case MACRO_SCRIPT:
        return new MacroScriptResource(resourceName, resourcePath, filename, text);
      case CSV:
        return new CSVResource(resourceName, resourcePath, filename, text);
      case JSON:
        return new JSONResource(resourceName, resourcePath, filename, text);
      default:
        return new GeneralTextResource(resourceName, resourcePath, filename, text);
    }
  }
}
