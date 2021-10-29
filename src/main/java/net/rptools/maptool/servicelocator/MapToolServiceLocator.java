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
package net.rptools.maptool.servicelocator;

import com.google.inject.Guice;
import com.google.inject.Injector;
import net.rptools.maptool.model.framework.FrameworkModule;

/**
 * This class is used to locate the services that packages im the MapTool code base implement.
 *
 * <p>This is far from ideal, but it is a step -- hopefully -- in the right direction. The idea is
 * to move all the calls to static classes into one place so that we can eventually start the
 * arduous process of moving the code base towards dependency injection.
 *
 * <p>As it currently stands this is very difficult to do as teh Swing code is so tightly coupled
 * with everything else that trying to instantiate the main MapTool class with the injector results
 * in a long list of dialogs that wont open and other general weirdness.
 */
public class MapToolServiceLocator {

  private static final MapToolServiceLocator instance = new MapToolServiceLocator();

  private final MapToolServices mapToolServices;

  private MapToolServiceLocator() {
    Injector injector = Guice.createInjector(new FrameworkModule());
    mapToolServices = injector.getInstance(MapToolServices.class);
  }

  public static MapToolServices getMapToolServices() {
    return instance.mapToolServices;
  }
}
