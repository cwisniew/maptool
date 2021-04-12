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
package net.rptools.maptool.webapi;

import io.undertow.Undertow;
import javax.servlet.ServletException;

public class WebAppServer {

  private static final String WEB_APP_VERSION = "0.1.0";

  private final int port;
  private final Undertow server;

  public static String getWebAppVersion() {
    return WEB_APP_VERSION;
  }

  public static WebAppServer createLocalWebAppServer(int port) throws ServletException {
    return new WebAppServer(port, true);
  }

  public static WebAppServer createWebAppServer(int port) throws ServletException {
    return new WebAppServer(port, false);
  }

  private WebAppServer(int port, boolean localHostOnly) throws ServletException {
    this.port = port;
    server =
        Undertow.builder()
            .addHttpListener(port, localHostOnly ? "localhost" : "0.0.0.0")
            .setHandler(new WebAppServletServer().getPathHandler())
            .build();
  }

  public void start() {
    server.start();
  }
}
