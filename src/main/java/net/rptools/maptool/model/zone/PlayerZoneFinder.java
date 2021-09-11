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
package net.rptools.maptool.model.zone;

import java.util.concurrent.ExecutionException;
import net.rptools.maptool.model.Zone;
import net.rptools.maptool.model.player.Player.Role;
import net.rptools.maptool.model.player.PlayerInfo;
import net.rptools.maptool.model.player.Players;

class PlayerZoneFinder implements ZoneFinder {

  private final boolean isGM;

  PlayerZoneFinder(PlayerInfo playerInfo) {
    isGM = playerInfo.role() == Role.GM;
  }

  PlayerZoneFinder() throws ExecutionException, InterruptedException {
    this(new Players().getPlayer().get());
  }

  @Override
  public boolean filter(Zone zone) {
    return isGM || zone.isVisible();
  }

  @Override
  public ZoneInfo map(Zone zone) {
    if (isGM) {
      return new ZoneInfo(
          zone.getId(),
          zone.isVisible(),
          zone.isEmpty(),
          zone.getName(),
          zone.getPlayerAlias(),
          zone.getWidth(),
          zone.getHeight());
    } else {
      return new ZoneInfo(
          zone.getId(), zone.isVisible(), null, null, zone.getPlayerAlias(), null, null);
    }
  }
}
