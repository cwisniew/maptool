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
package net.rptools.maptool.client.framework.library;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import net.rptools.lib.memento.Originator;
import net.rptools.maptool.client.framework.library.libtoken.LibTokenEmulation;

public class FrameworkLibrary implements Originator<FrameworkLibraryMemento> {

  private final UUID id;
  private final String name;
  private final String namespace;
  private final String version;
  private final String gitHubUrl;

  private final Map<String, String> definedFunctions = new ConcurrentHashMap<>();

  private final Map<String, DataValue> dataValues = new ConcurrentHashMap<>();

  private final boolean compatibilityMode;

  private transient Set<LibTokenEmulation> libTokenEmulation = new HashSet<>();

  /**
   * Creates a new {@code Library} object.
   *
   * @param id the id of the {@code Library}.
   * @param name the name of the {@code Library}.
   * @param version the version of the {@code Library}.
   */
  public FrameworkLibrary(UUID id, String name, String namespace, String version, String gitHubUrl, boolean compatible, Set<LibTokenEmulation> libTokenEmulationSet) {
    this.id = id;
    this.name = name;
    this.namespace = namespace;
    this.version = version;
    this.gitHubUrl = gitHubUrl;
    this.compatibilityMode = compatible;
    this.libTokenEmulation.addAll(libTokenEmulationSet);
  }

  public FrameworkLibrary(FrameworkLibraryMemento state) {
    this(state.id(), state.name(), state.namespace(), state.version(), state.gitHubUrl(), state.compatibilityMode(), state.libTokenEmulation());

    definedFunctions.putAll(state.definedFunctions());
    dataValues.putAll(state.dataValues());
  }

  public UUID getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getVersion() {
    return version;
  }

  public String getGitHubUrl() {
    return gitHubUrl;
  }

  public boolean getCompatibilityMode() {
    return compatibilityMode;
  }

  /**
   * Defines a macro function that can be called from MTScript.
   *
   * @param name The name of the function.
   * @param path The path of the text file in the library.
   */
  public void defineFunction(String name, String path) {
    definedFunctions.put(name, path);
  }

  public DataValue getData(String path) {
    return dataValues.getOrDefault(path, DataValue.UNDEFINED);
  }

  /**
   * Returns the namespace of the library.
   * @return the namespace of the library.
   */
  public String getNamespace() {
    return namespace;
  }

  public void setData(String path, DataValue value) {
    if (isStaticData(path)) {
      throw new IllegalArgumentException("Static data can not be modified."); // TODO: CDW
    }
    dataValues.put(path, value);
  }

  public boolean isStaticData(String path) {
    return path.startsWith("static.");
  }

  @Override
  public FrameworkLibraryMemento getState() {
    FrameworkLibraryMementoBuilder builder = new FrameworkLibraryMementoBuilder();
    builder.setId(id).setName(name).setNamespace(namespace).setVersion(version)
        .setCompatibilityMode(compatibilityMode).setGitHubUrl(gitHubUrl)
        .setDataValues(dataValues).setDefinedFunctions(definedFunctions)
        .setLibTokenEmulation(libTokenEmulation);

    return builder.build();
  }

}
