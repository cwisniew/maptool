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

class MTAbstractResource implements MTResource {

  private String name;
  private String path;
  private MTResourceType resourceType;
  private String filename;

  MTAbstractResource(String resourceName, String resourcePath, String fname,  MTResourceType type) {
    name = resourceName;
    path = resourcePath;
    filename = fname;
    resourceType = type;
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
}
