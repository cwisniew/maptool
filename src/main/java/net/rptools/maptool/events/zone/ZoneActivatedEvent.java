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
package net.rptools.maptool.events.zone;

import net.rptools.maptool.model.Zone;

/** Event raised when a new {@link Zone} is added. */
public class ZoneActivatedEvent extends ZoneEvent {

  private final Zone oldZone;

  /**
   * Creates a new {@code ZoneAddedEvent}.
   *
   * @param activatedZone The {@link Zone} that was changed to.
   * @param deactivatedZone The {@link Zone} that was previously active.
   */
  public ZoneActivatedEvent(Zone activatedZone, Zone deactivatedZone) {
    super(activatedZone);
    oldZone = deactivatedZone;
  }

  /**
   * Returns the {@link Zone} that was previously active.
   *
   * @return the {@link Zone} that was previously active.
   */
  public Zone getOldZone() {
    return oldZone;
  }
}
