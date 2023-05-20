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
package net.rptools.clientserver.simple.client;

import net.rptools.clientserver.ActivityListener;
import net.rptools.clientserver.simple.Connection;
import net.rptools.clientserver.simple.DisconnectHandler;

public interface ClientConnection extends Connection {
  void sendMessage(byte[] message);

  void sendMessage(Object channel, byte[] message);

  boolean isAlive();

  String getId();

  void addDisconnectHandler(DisconnectHandler handler);

  void removeDisconnectHandler(DisconnectHandler handler);

  void addActivityListener(ActivityListener listener);

  void removeActivityListener(ActivityListener listener);

  String getError();
}
