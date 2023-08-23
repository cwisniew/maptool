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
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.rptools.maptool.model.Zone;

public class ZoneGroupTreeNode {
  private String name;
  private UUID id;
  private final ArrayList<ZoneGroupTreeNode> groups = new ArrayList<>();

  private final ArrayList<Zone> zones = new ArrayList<>();

  private final Map<UUID, ZoneGroupTreeNode> groupMap = new HashMap<>();


  public ZoneGroupTreeNode(UUID rootZoneGroupId, String rootZoneGroupName) {
    // TODO: CDW
  }

  public void addZone(Zone zone) {
    // TODO: CDW
  }

  public void removeZone(Zone zone) {
    // TODO: CDW
  }
}
