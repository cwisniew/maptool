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

import java.util.Collections;
import net.rptools.maptool.client.framework.LibTokenEmulation;

import java.util.HashSet;
import java.util.Set;

public class FrameworkLibraryManager {
  private final Set<FrameworkLibrary> libraries = new HashSet<>();
  private final Set<LibTokenEmulation> libTokenEmulations = new HashSet<>();


  public void addLibrary(FrameworkLibrary frameworkLibrary) {
    libraries.add(frameworkLibrary);
  }

  public Set<FrameworkLibrary> getLibraries() {
    return Collections.unmodifiableSet(libraries);
  }
}
