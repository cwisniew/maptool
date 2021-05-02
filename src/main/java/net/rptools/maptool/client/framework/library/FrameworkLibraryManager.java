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

import java.util.*;

import net.rptools.maptool.client.framework.library.libtoken.LibTokenEmulation;

public class FrameworkLibraryManager {
  private final Map<UUID, FrameworkLibrary> libraries = new HashMap<>();
  private final Map<UUID, Set<LibTokenEmulation>> libTokenEmulations = new HashMap<>();

  public void addLibrary(FrameworkLibrary frameworkLibrary) {
    UUID id = frameworkLibrary.getId();
    libraries.remove(id);
    libTokenEmulations.remove(id);

    libraries.put(id, frameworkLibrary);
  }

  public void addLibTokenEmulation(FrameworkLibrary frameworkLibrary, LibTokenEmulation libTokenEmulation) {
    UUID id = frameworkLibrary.getId();
    libTokenEmulations.computeIfAbsent(id, k ->  new HashSet<>());
    libTokenEmulations.get(id).add(libTokenEmulation);
  }

  public void addLibTokenEmulation(FrameworkLibrary frameworkLibrary, Set<LibTokenEmulation> libTokenEmulationSet) {
    libTokenEmulationSet.forEach(lt -> addLibTokenEmulation(frameworkLibrary, lt));
  }

  public Set<FrameworkLibrary> getLibraries() {
    return Set.copyOf(libraries.values());
  }
}
