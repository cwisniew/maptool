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
package net.rptools.maptool.events;

import net.rptools.maptool.model.Zone;

/** Abstract base class for all Zone based events. */
abstract class ZoneEvent {

  /** Zone that the event {@link Zone} occured for. */
  private final Zone zone;

  /**
   * Creates a new {@code ZoneEvent}.
   *
   * @param zone The {@link Zone} that the event occured for.
   */
  ZoneEvent(Zone zone) {
    this.zone = zone;
  }

  /**
   * Returns the {@link Zone} that the event occured for.
   *
   * @return
   */
  public Zone getZone() {
    return zone;
  }
}
