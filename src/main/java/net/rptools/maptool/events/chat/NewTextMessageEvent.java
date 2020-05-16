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
package net.rptools.maptool.events.chat;

import net.rptools.maptool.model.TextMessage;

/** Event for new text messages. */
public class NewTextMessageEvent {

  /** The {@link TextMessage} that triggered this event. */
  private final TextMessage textMessage;

  /**
   * Creates a new {@code NewTextMessageEVent}.
   *
   * @param msg The {@link TextMessage} that triggered the event.
   */
  public NewTextMessageEvent(TextMessage msg) {
    textMessage = msg;
  }

  /**
   * Returns the {@link TextMessage} that triggered the event.
   *
   * @return the {@link TextMessage} that triggered the event.
   */
  public TextMessage getTextMessage() {
    return textMessage;
  }
}
