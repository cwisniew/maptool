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
package net.rptools.maptool.client.framework;

import net.rptools.maptool.client.framework.library.DataValue;
import net.rptools.maptool.client.framework.library.FrameworkLibrary;
import net.rptools.maptool.client.framework.library.FrameworkLibraryFunction;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.UUID;

public class LibTokenEmulation {

  private static final String UNDEFINED_PROPERTY_PATH = "data/undefined-properties/";

  private final String libTokenName;
  private final String libTokenVersion;
  private final Map<String, String> macroFunctionMap;
  private final Map<String, String> macroPropertyMap;
  private final FrameworkLibrary frameworkLibrary;

  public LibTokenEmulation(
      String name,
      String version,
      FrameworkLibrary macroFrameworkLibrary,
      Map<String, String> functionMap,
      Map<String, String> propertyMap) {
    libTokenName = name;
    libTokenVersion = version;
    macroFunctionMap = Collections.unmodifiableSortedMap(new TreeMap<>(functionMap));
    macroPropertyMap = Collections.unmodifiableSortedMap(new TreeMap<>(propertyMap));
    frameworkLibrary = macroFrameworkLibrary;
  }

  public String getLibTokenName() {
    return libTokenName;
  }

  public String getLibTokenVersion() {
    return libTokenVersion;
  }

  /**
   * Returns the {@link FrameworkLibraryFunction} for the defined MTS function.
   *
   * @param name the name of the defined function.
   * @return the defined function.
   */
  public Optional<FrameworkLibraryFunction> getFunction(String name) {
    String path = macroFunctionMap.get(name);
    if (path == null) {
      return Optional.empty();
    }

    //return frameworkLibrary.getFunction(path);
    return null; // TODO: CDW
  }

  public DataValue getProperty(String name) {
    String path = macroPropertyMap.get(name);
    if (path == null) {
      return DataValue.UNDEFINED;
    }

    return frameworkLibrary.getData(path);
  }

  public void setProperty(String name, DataValue value) {
    String path = macroPropertyMap.get(name);
    if (path == null) {
      path = UNDEFINED_PROPERTY_PATH + UUID.randomUUID();
      macroPropertyMap.put(name, path);
    }

    frameworkLibrary.setData(path, value);
  }

  /**
   * Returns the names of the functions defined for MTScript.
   *
   * @return the names of the functions defined for MTScript.
   */
  public Collection<String> getFunctionNames() {
    return macroFunctionMap.keySet();
  }

  public Collection<String> getPropertyNames() {
    return macroPropertyMap.keySet();
  }
}
