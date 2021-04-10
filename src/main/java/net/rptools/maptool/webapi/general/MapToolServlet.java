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

import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MapToolServlet extends HttpServlet {

  private String maptoolVersion;
  private String webAppVersion;

  @Override
  public void init(final ServletConfig config) throws ServletException {
    super.init(config);
    maptoolVersion = config.getInitParameter(MapToolServletManager.PARAM_MAPTOOL_VERSION);
    webAppVersion = config.getInitParameter(MapToolServletManager.PARAM_WEB_APP_VERSION);
  }

  @Override
  protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
      throws ServletException, IOException {
    resp.setContentType("application/json");
    JsonObject response = new JsonObject();
    response.addProperty(MapToolServletManager.PARAM_MAPTOOL_VERSION, maptoolVersion);
    response.addProperty(MapToolServletManager.PARAM_WEB_APP_VERSION, webAppVersion);

    PrintWriter writer = resp.getWriter();
    writer.write(response.toString());
    writer.close();
  }

  @Override
  protected void doPost(final HttpServletRequest req, final HttpServletResponse resp)
      throws ServletException, IOException {
    doGet(req, resp);
  }
}
