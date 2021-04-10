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
package net.rptools.maptool.webapi.macrolibrary;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MacroLibraryServlet extends HttpServlet {

  public static final String MESSAGE = "message";

  private String message;

  @Override
  protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
      throws ServletException, IOException {
    resp.setContentType("application/json");

    PrintWriter writer = resp.getWriter();
    String path = Objects.requireNonNullElse(req.getPathInfo(), "/");
    JsonObject response = new JsonObject();

    switch (path) {
      case "/newFrameworkId" ->
        response.addProperty("id", UUID.randomUUID().toString());
      default ->
        response.add("frameworks", new JsonArray());
    }
    writer.write(response.toString());

    writer.close();
  }

  @Override
  protected void doPost(final HttpServletRequest req, final HttpServletResponse resp)
      throws ServletException, IOException {
    doGet(req, resp);
  }
}
