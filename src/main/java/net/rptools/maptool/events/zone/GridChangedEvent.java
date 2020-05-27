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

import net.rptools.maptool.model.Grid;
import net.rptools.maptool.model.Zone;

/** Event fired when the {@link Grid} is Changed for a {@link Zone}. */
public class GridChangedEvent extends ZoneEvent {

  private final Grid grid;

  /**
   * Creates a new {@code ZoneEvent}.
   *
   * @param grid The {@link Grid} that changed.
   * @param zone The {@link Zone} that the event occurred for.
   */
  public GridChangedEvent(Grid grid, Zone zone) {
    super(zone);
    this.grid = grid;
  }

  /**
   * Returns the {@link Grid} that changed.
   *
   * @return the {@link Grid} that changed.
   */
  public Grid getGrid() {
    return grid;
  }
}
