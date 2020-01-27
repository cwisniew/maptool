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

import java.util.UUID;

class MTAbstractResource implements MTResource {

  private final UUID id;
  private final String name;
  private final String path;
  private final MTResourceType resourceType;
  private final String filename;

  MTAbstractResource(
      UUID resourceId,
      String resourceName,
      String resourcePath,
      String fname,
      MTResourceType type) {
    id = resourceId;
    name = resourceName;
    path = resourcePath;
    filename = fname;
    resourceType = type;
  }

  MTAbstractResource(String resourceName, String resourcePath, String fname, MTResourceType type) {
    this(UUID.randomUUID(), resourceName, resourcePath, fname, type);
  }

  @Override
  public UUID getId() {
    return id;
  }

  @Override
  public MTResourceType getResourceType() {
    return resourceType;
  }

  @Override
  public boolean isText() {
    return MTResourceType.TEXT.equals(getResourceType());
  }

  @Override
  public boolean isDirectory() {
    return MTResourceType.DIRECTORY.equals(getResourceType());
  }

  @Override
  public String getText() {
    throw new UnsupportedOperationException();
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getFilename() {
    return filename;
  }

  @Override
  public String getPath() {
    return path;
  }

  @Override
  public String getFullPath() {
    return getPath() + getName();
  }

  @Override
  public String toString() {
    return "{ "
        + "id = "
        + getId().toString()
        + ", "
        + "resourceType = "
        + getResourceType().toString()
        + ", "
        + "name = "
        + getName()
        + ", "
        + "path = "
        + getPath()
        + ", "
        + "filename = "
        + getFilename()
        + ", "
        + "}";
  }
}
