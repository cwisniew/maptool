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
package net.rptools.maptool.webapi.general;

import static io.undertow.servlet.Servlets.defaultContainer;
import static io.undertow.servlet.Servlets.servlet;

import io.undertow.Handlers;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.PathHandler;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.api.ServletInfo;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import javax.servlet.ServletException;
import net.rptools.maptool.client.MapTool;
import net.rptools.maptool.webapi.WebAppServer;

public class MapToolServletManager {

  private static final String CONTEXT_PATH = "maptool";
  private static final String DEPLOYMENT_NAME = "maptool";

  static final String PARAM_MAPTOOL_VERSION = "maptoolVersion";
  static final String PARAM_WEB_APP_VERSION = "webAppVersion";


  private final ServletInfo[] servlets = {
    servlet("MapToolServlet", MapToolServlet.class)
        .addInitParam(PARAM_MAPTOOL_VERSION, MapTool.getVersion())
      .addInitParam(PARAM_WEB_APP_VERSION, WebAppServer.getWebAppVersion())
      .addMapping("/version")
  };

  public Collection<ServletInfo> getServlets() {
    return Arrays.asList(servlets);
  }
}
