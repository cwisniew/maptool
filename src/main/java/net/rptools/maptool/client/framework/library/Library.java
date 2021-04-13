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

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.rptools.lib.MD5Key;

public class Library {

  private final UUID id;
  private final String name;
  private final String version;
  private final String gitHubUrl;

  private final Map<String, String> definedFunctions = new ConcurrentHashMap<>();

  private final Map<String, DataValue> dataValues = new ConcurrentHashMap<>();
  private final Map<String, LibraryFunction> libraryFunctions = new ConcurrentHashMap<>();

  private final Map<String, MD5Key> assetMap = new ConcurrentHashMap<>();

  private final boolean compatibilityMode;

  /**
   * Creates a new {@code Library} object.
   *
   * @param id the id of the {@code Library}.
   * @param name the name of the {@code Library}.
   * @param version the version of the {@code Library}.
   */
  public Library(UUID id, String name, String version, String gitHubUrl, boolean compatible) {
    this.id = id;
    this.name = name;
    this.version = version;
    this.gitHubUrl = gitHubUrl;
    this.compatibilityMode = compatible;
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

  /**
   * Returns the function at the specified path.
   *
   * @param path the path of the function.
   * @return the function at the specified path/
   */
  public Optional<LibraryFunction> getFunction(String path) {
    return Optional.ofNullable(libraryFunctions.get(path));
  }

  public DataValue getData(String path) {
    return dataValues.getOrDefault(path, DataValue.UNDEFINED);
  }

  public void setData(String path, DataValue value) {
    if (isStaticData(path)) {
      throw new IllegalArgumentException("Static data can not be modified."); // TODO: CDW
    }
    dataValues.put(path, value);
  }

  public Collection<String> getAssetPaths() {
    return Collections.unmodifiableCollection(assetMap.keySet());
  }

  public Collection<MD5Key> getAssetKeys() {
    return Collections.unmodifiableCollection(assetMap.values());
  }

  public void addLibraryFunction(String path, LibraryFunction function) {
    libraryFunctions.put(path, function);
  }

  public void addAsset(String path, MD5Key key) {
    assetMap.put(path, key);
  }

  public boolean isStaticData(String path) {
    return path.startsWith("static.");
  }
}
