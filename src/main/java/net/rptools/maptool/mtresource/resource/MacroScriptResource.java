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

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class MacroScriptResource extends TextResource {

  MacroScriptResource(String resourceName, String resourcePath, String filename, String text) {
    super(resourceName, resourcePath, filename, text, MTResourceType.MACRO_SCRIPT);
  }

  MacroScriptResource(String resourceName, String resourcePath, String filename, InputStream in)
      throws IOException {
    super(resourceName, resourcePath, filename, in, MTResourceType.MACRO_SCRIPT);
  }

  MacroScriptResource(
      UUID id, String resourceName, String resourcePath, String filename, String text) {
    super(id, resourceName, resourcePath, filename, text, MTResourceType.MACRO_SCRIPT);
  }

  MacroScriptResource(
      UUID id, String resourceName, String resourcePath, String filename, InputStream in)
      throws IOException {
    super(id, resourceName, resourcePath, filename, in, MTResourceType.MACRO_SCRIPT);
  }
}
