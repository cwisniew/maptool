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
package net.rptools.maptool.client.framework.library.libtoken;

import net.rptools.lib.memento.Originator;
import net.rptools.maptool.client.framework.library.FrameworkLibraryFunction;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public class LibTokenEmulation implements Originator<LibTokenEmulationMemento>  {

  private static final String UNDEFINED_PROPERTY_PATH = "data/undefined-properties/";


  private final String libTokenName;
  private final String libTokenVersion;
  private final Map<String, String> macroFunctionMap;
  private final Map<String, String> macroPropertyMap;

  public LibTokenEmulation(
      String name,
      String version,
      Map<String, String> functionMap,
      Map<String, String> propertyMap) {
    libTokenName = name;
    libTokenVersion = version;
    macroFunctionMap = Collections.unmodifiableSortedMap(new TreeMap<>(functionMap));
    macroPropertyMap = Collections.unmodifiableSortedMap(new TreeMap<>(propertyMap));
  }


  public LibTokenEmulation(LibTokenEmulationMemento state) {
    this(state.name(), state.version(), state.definedFunctions(), state.properties());
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

  @Override
  public LibTokenEmulationMemento getState() {
    return new LibTokenEmulationMementoBuilder()
            .setName(libTokenName)
            .setVersion(libTokenVersion)
            .setDefinedFunctions(macroFunctionMap)
            .setProperties(macroPropertyMap)
            .build();
  }
}
