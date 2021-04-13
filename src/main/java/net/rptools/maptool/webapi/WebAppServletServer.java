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

import static io.undertow.servlet.Servlets.defaultContainer;

import io.undertow.Handlers;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.PathHandler;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import javax.servlet.ServletException;
import net.rptools.maptool.webapi.frameworks.FrameworkLibraryServletServer;
import net.rptools.maptool.webapi.general.MapToolServletManager;

public class WebAppServletServer {

  private static final String CONTEXT_PATH = "maptool";
  private static final String DEPLOYMENT_NAME = "maptool";

  private final PathHandler pathHandler;

  public WebAppServletServer() throws ServletException {
    DeploymentInfo servletBuilder =
        Servlets.deployment()
            .setClassLoader(WebAppServletServer.class.getClassLoader())
            .setContextPath(CONTEXT_PATH)
            .setDeploymentName(DEPLOYMENT_NAME);

    new MapToolServletManager().getServlets().forEach(servletBuilder::addServlet);
    new FrameworkLibraryServletServer().getServlets().forEach(servletBuilder::addServlet);

    DeploymentManager deploymentManager = defaultContainer().addDeployment(servletBuilder);
    deploymentManager.deploy();

    HttpHandler servletHandler = deploymentManager.start();
    pathHandler =
        Handlers.path(Handlers.redirect(DEPLOYMENT_NAME))
            .addPrefixPath(DEPLOYMENT_NAME, servletHandler);
  }

  public PathHandler getPathHandler() {
    return pathHandler;
  }
}
