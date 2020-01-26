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

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.UUID;
import org.apache.commons.io.IOUtils;

public class TextResource extends MTAbstractResource {

  private String text;

  TextResource(String resourceName, String resourcePath, String filename, String txt) {
    super(resourceName, resourcePath, filename, MTResourceType.TEXT);
    text = txt;
  }

  TextResource(String resourceName, String resourcePath, String filename, InputStream in)
      throws IOException {
    super(resourceName, resourcePath, filename, MTResourceType.TEXT);
    StringWriter writer = new StringWriter();
    IOUtils.copy(in, writer, Charset.defaultCharset());
  }


  TextResource(UUID id, String resourceName, String resourcePath, String filename, String txt) {
    super(id, resourceName, resourcePath, filename, MTResourceType.TEXT);
    text = txt;
  }

  TextResource(UUID id, String resourceName, String resourcePath, String filename, InputStream in)
      throws IOException {
    super(id, resourceName, resourcePath, filename, MTResourceType.TEXT);
    StringWriter writer = new StringWriter();
    IOUtils.copy(in, writer, Charset.defaultCharset());
  }

  @Override
  public String getText() {
    return text;
  }
}
