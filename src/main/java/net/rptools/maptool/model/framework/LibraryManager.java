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
package net.rptools.maptool.model.framework;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import net.rptools.maptool.model.framework.dropinlibrary.AddOnLibrary;
import net.rptools.maptool.model.framework.proto.AddOnLibraryListDto;

public interface LibraryManager {

  boolean usesReservedPrefix(String name);

  boolean usesReservedName(String name);

  String getReservedPrefix(String name);

  CompletableFuture<Optional<Library>> getLibrary(URL path);

  CompletableFuture<Boolean> libraryExists(URL path);

  boolean addOnLibraryExists(String namespace);

  boolean registerAddOnLibrary(AddOnLibrary addOn);

  void deregisterAddOnLibrary(String namespace);

  boolean reregisterAddOnLibrary(AddOnLibrary addOnLibrary);

  List<LibraryInfo> getLibraries(LibraryType libraryType)
      throws ExecutionException, InterruptedException;

  Optional<LibraryInfo> getLibraryInfo(String namespace)
      throws ExecutionException, InterruptedException;

  Optional<Library> getLibrary(String namespace) throws ExecutionException, InterruptedException;

  CompletableFuture<AddOnLibraryListDto> addOnLibrariesToDto();

  void removeAddOnLibraries();
}
