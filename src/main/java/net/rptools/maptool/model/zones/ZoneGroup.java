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
package net.rptools.maptool.model.zones;

import java.util.ArrayList;
import java.util.UUID;
import javax.annotation.Nonnull;
import net.rptools.maptool.model.Zone;
import net.rptools.maptool.server.proto.ZoneGroupDto;

/** The class that represents a Zone Group in MapTool. */
public class ZoneGroup {

  /** The unique identifier for this Zone Group. */
  private final UUID id;

  /** The name of this Zone Group. */
  private String name;

  /** The parent of this Zone Group. */
  private final ArrayList<ZoneGroup> children = new ArrayList<>();

  /**
   * Creates a new Zone Group with the given name.
   *
   * @param name The name of the new Zone Group.
   */
  public ZoneGroup(String name) {
    this(name, UUID.randomUUID());
  }

  /**
   * Creates a new Zone Group with the given name and id.
   *
   * @param name The name of the new Zone Group.
   * @param id The id of the new Zone Group.
   */
  public ZoneGroup(String name, UUID id) {
    this.name = name;
    this.id = id;
  }

  public ZoneGroup(ZoneGroup zoneGroup) {
    this.name = zoneGroup.name;
    this.id = zoneGroup.id;
    children.forEach(child -> children.add(new ZoneGroup(child)));
  }

  public UUID getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(@Nonnull String name) {
    this.name = name;
  }

  public ArrayList<ZoneGroup> getChildren() {
    return children;
  }

  public void addChild(@Nonnull ZoneGroup child) {
    children.add(child);
  }

  public ZoneGroupDto toDto() {
    ZoneGroupDto.Builder builder = ZoneGroupDto.newBuilder();
    builder.setId(id.toString());
    builder.setName(name);
    children.forEach(child -> builder.addChildren(child.toDto()));
    return builder.build();
  }

  public static ZoneGroup fromDto(ZoneGroupDto dto) {
    ZoneGroup zoneGroup = new ZoneGroup(dto.getName(), UUID.fromString(dto.getId()));
    dto.getChildrenList().forEach(child -> zoneGroup.addChild(fromDto(child)));
    return zoneGroup;
  }

  public void addZone(Zone zone) {
  }
}
