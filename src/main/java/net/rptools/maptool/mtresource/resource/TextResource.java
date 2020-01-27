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
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.UUID;
import org.apache.commons.io.IOUtils;

public abstract class TextResource extends MTAbstractResource {

  private String text;

  private TextResource(
      UUID id, String resourceName, String resourcePath, String filename, MTResourceType type) {
    super(id, resourceName, resourcePath, filename, type);
    if (!type.isText()) {
      throw new IllegalArgumentException(type.name() + " is not a text type");
    }
  }

  protected TextResource(
      String resourceName, String resourcePath, String filename, String txt, MTResourceType type) {
    this(UUID.randomUUID(), resourceName, resourcePath, filename, type);
    text = txt;
  }

  protected TextResource(
      String resourceName,
      String resourcePath,
      String filename,
      InputStream in,
      MTResourceType type)
      throws IOException {
    this(UUID.randomUUID(), resourceName, resourcePath, filename, type);
    StringWriter writer = new StringWriter();
    IOUtils.copy(in, writer, Charset.defaultCharset());
  }

  protected TextResource(
      UUID id,
      String resourceName,
      String resourcePath,
      String filename,
      String txt,
      MTResourceType type) {
    this(id, resourceName, resourcePath, filename, type);
    text = txt;
  }

  protected TextResource(
      UUID id,
      String resourceName,
      String resourcePath,
      String filename,
      InputStream in,
      MTResourceType type)
      throws IOException {
    this(id, resourceName, resourcePath, filename, type);
    StringWriter writer = new StringWriter();
    IOUtils.copy(in, writer, Charset.defaultCharset());
  }

  @Override
  public String getText() {
    return text;
  }
}
