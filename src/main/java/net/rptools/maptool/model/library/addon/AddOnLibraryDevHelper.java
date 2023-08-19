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
package net.rptools.maptool.model.library.addon;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import net.rptools.maptool.client.AppUtil;
import net.rptools.maptool.model.library.LibraryInfo;

public class AddOnLibraryDevHelper {

  private record PackageInfo(String mtLibFileName, boolean addVersionToFile, Path path) {}

  private final Set<LibraryInfo> addOns = new TreeSet<>(Comparator.comparing(LibraryInfo::name));
  private final Map<String, PackageInfo> addOnPackages = new HashMap<>();

  private boolean devModeEnabled = false;

  private Path lastPath = AppUtil.getAppHome().toPath();

  public Set<LibraryInfo> getAddOns() {
    return addOns;
  }

  public void createAddOnSkeleton(Path path) {
    lastPath = path;
    // TODO: CDW
  }


  public LibraryInfo packageAddOn(Path path, String mtLibFileName, boolean addVersionToFile) {
    // TODO: CDW
    return null;
  }


  public LibraryInfo importPackagedAddOn(LibraryInfo libraryInfo) {
    var det = addOnPackages.get(libraryInfo.namespace());
    // TODO: CDW
    return null;
  }

  public LibraryInfo reImportPackagedAddOn(LibraryInfo libraryInfo) {
    var info = addOnPackages.get(libraryInfo.namespace());
    return packageAddOn(info);
  }

  private LibraryInfo packageAddOn(PackageInfo info) {
    return null;
    // TODO: CDW
  }

  public void setDevModeEnabled(boolean enabled) {
    devModeEnabled = enabled;
  }

  public boolean isDevModeEnabled() {
    return devModeEnabled;
  }

  public Path getLastPath() {
    return lastPath;
  }
}
