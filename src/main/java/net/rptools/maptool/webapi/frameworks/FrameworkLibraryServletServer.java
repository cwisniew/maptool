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

import static io.undertow.servlet.Servlets.servlet;

import io.undertow.servlet.api.ServletInfo;
import java.util.Arrays;
import java.util.Collection;

public class FrameworkLibraryServletServer {

  private final ServletInfo[] servlets = {
      servlet("MacroLibraryServlet", FrameworkLibraryServlet.class)
          .addMapping("/frameworks/*")
  };

  public Collection<ServletInfo> getServlets() {
    return Arrays.asList(servlets);
  }

}
