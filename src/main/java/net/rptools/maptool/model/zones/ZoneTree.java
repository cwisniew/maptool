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

import net.rptools.maptool.model.Campaign;
import net.rptools.maptool.model.GUID;
import net.rptools.maptool.server.proto.ZoneTreeDto;

public class ZoneTree {
  private Campaign campaign;
  private GUID defaultZoneId;

  public static ZoneTree zoneTreeFor(Campaign campaign) {
    return new ZoneTree();
    // TODO: CDW
  }

  public ZoneTreeDto toDto() {
    // TODO: CDW
    return null;
  }

  public static ZoneTree fromDto(ZoneTreeDto dto) {
    // TODO: CDW
    return null;
  }

  public void setCampaign(Campaign campaign) {
    this.campaign = campaign;
  }
}
