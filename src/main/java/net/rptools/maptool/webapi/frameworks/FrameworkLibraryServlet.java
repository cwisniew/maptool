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
package net.rptools.maptool.webapi.frameworks;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import com.google.gson.JsonParser;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.UUID;
import net.rptools.lib.memento.Memento;
import net.rptools.lib.memento.MementoBuilderParseException;
import net.rptools.maptool.client.MapTool;
import net.rptools.maptool.client.framework.library.FrameworkLibrary;
import net.rptools.maptool.client.framework.library.FrameworkLibraryMemento;
import net.rptools.maptool.client.framework.library.FrameworkLibraryMementoBuilder;

public class FrameworkLibraryServlet extends HttpServlet {

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
      default -> {
        JsonArray jsonArray = new JsonArray();
        FrameworkLibraryMementoBuilder builder = new FrameworkLibraryMementoBuilder();
        MapTool.getCampaign().getFrameworkLibraryManager().getLibraries().stream().forEach(l ->
            jsonArray.add(builder.fromState(l.getState()).toJson())
        );
        response.add("frameworks", jsonArray);
      }
    }
    writer.write(response.toString());

    writer.close();
  }

  @Override
  protected void doPut(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    resp.setContentType("application/json");

    JsonObject response = new JsonObject();

    try {
    String reqBody = req.getReader().readLine();
      JsonObject json = JsonParser.parseString(reqBody).getAsJsonObject().getAsJsonObject("frameworkInfo");
      FrameworkLibraryMemento frameworkLibraryMemento = new FrameworkLibraryMementoBuilder().fromJson(json).build();
      FrameworkLibrary frameworkLibrary = new FrameworkLibrary(frameworkLibraryMemento);
      MapTool.getCampaign().getFrameworkLibraryManager().addLibrary(frameworkLibrary);
      response.addProperty("status", "ok");
    } catch (Exception e) {
      response.addProperty("status", "error");
      if (e instanceof MementoBuilderParseException pe) {
        response.addProperty("error", String.join(", ", pe.getErrors()));
      } else {
        response.addProperty("error", "invalid frameworkInfo");
      }
    }
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
