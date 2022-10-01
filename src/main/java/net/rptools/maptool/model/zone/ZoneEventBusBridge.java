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

import net.rptools.lib.AppEvent;
import net.rptools.lib.AppEventListener;
import net.rptools.maptool.client.MapTool.ZoneEvent;
import net.rptools.maptool.events.MapToolEventBus;
import net.rptools.maptool.model.Zone;

/**
 * This class listens to the legacy token addition events and forwards them to the main MapTool
 * event bus.
 *
 * @see MapToolEventBus#getMainEventBus()
 */
public class ZoneEventBusBridge implements AppEventListener {

  @Override
  public void handleAppEvent(AppEvent appEvent) {
    if (appEvent.getId().equals(ZoneEvent.Added) && appEvent.getNewValue() instanceof Zone zone) {
      new MapToolEventBus()
          .getMainEventBus()
          .post(new ZoneAddedEvent(zone.getId(), zone.getName()));
    } else if (appEvent.getId().equals(ZoneEvent.Removed)
        && appEvent.getOldValue() instanceof Zone zone) {
      new MapToolEventBus().getMainEventBus().post(new ZoneRemovedEvent(zone.getId()));
    }
  }

  /** The singleton instance of this class. */
  private static final ZoneEventBusBridge instance = new ZoneEventBusBridge();

  /**
   * Returns the singleton instance of this class.
   *
   * @return the singleton instance of this class.
   */
  public static ZoneEventBusBridge getInstance() {
    return instance;
  }
}
