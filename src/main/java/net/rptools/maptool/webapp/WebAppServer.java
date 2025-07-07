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
package net.rptools.maptool.webapp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.javalin.Javalin;
import io.javalin.json.JsonMapper;
import java.lang.reflect.Type;
import javax.annotation.Nonnull;
import net.rptools.maptool.webapp.api.Info;

/**
 * The main class for the MapTool web application server. It initializes a Javalin server and sets
 * up end points for the API.
 */
public class WebAppServer {

  /** The {@linke Javalin} server instance that handles HTTP requests. */
  private Javalin server;

  /**
   * Starts the web application server on port 8080.
   *
   * <p>This method initializes the Javalin server, sets a custom JSON mapper using Gson, and
   * registers an endpoint to retrieve information about the MapTool application.
   */
  public void start() {
    Gson gson = new GsonBuilder().create();
    JsonMapper gsonMapper =
        new JsonMapper() {
          @Override
          public String toJsonString(@Nonnull Object obj, @Nonnull Type type) {
            return gson.toJson(obj, type);
          }

          @Override
          public <T> T fromJsonString(@Nonnull String json, @Nonnull Type targetType) {
            return gson.fromJson(json, targetType);
          }
        };
    server = Javalin.create(config -> config.jsonMapper(gsonMapper)).start(8080);
    server.get("/info", ctx -> ctx.json(Info.getInfo()));
  }
}
