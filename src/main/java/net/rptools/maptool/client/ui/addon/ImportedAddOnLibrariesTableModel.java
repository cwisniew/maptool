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
package net.rptools.maptool.client.ui.addon;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.table.AbstractTableModel;
import net.rptools.maptool.language.I18N;
import net.rptools.maptool.model.library.LibraryInfo;
import net.rptools.maptool.model.library.LibraryManager;
import net.rptools.maptool.model.library.LibraryType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** Table model for displaying add-on imported for development. */
public class ImportedAddOnLibrariesTableModel extends AbstractTableModel {
  /** Logger instance. */
  private static final Logger log = LogManager.getLogger(ImportedAddOnLibrariesTableModel.class);

  /** List of add-ons. */
  private final List<LibraryInfo> addons = new ArrayList<>();

  /** Creates a new instance. */
  public ImportedAddOnLibrariesTableModel() {
    try {
      addons.addAll(new LibraryManager().getLibraries(LibraryType.ADD_ON));
    } catch (ExecutionException | InterruptedException e) {
      log.error("Error displaying add-on libraries", e);
    }
  }

  /**
   * Returns the add-on at the specified row.
   *
   * @param row the row
   * @return the add-on
   */
  public LibraryInfo getAddOn(int row) {
    return addons.get(row);
  }

  @Override
  public int getRowCount() {
    if (new LibraryManager().getAddOnLibraryDevHelper().isDevModeEnabled()) {
      return addons.size();
    } else {
      return 0;
    }
  }

  @Override
  public int getColumnCount() {
    return 3;
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    LibraryInfo addon = addons.get(rowIndex);
    return switch (columnIndex) {
      case 0 -> addon.name();
      case 1 -> addon.namespace();
      case 2 -> addon.version();
      default -> null;
    };
  }

  @Override
  public String getColumnName(int column) {
    return switch (column) {
      case 0 -> I18N.getText("library.dialog.addon.name");
      case 1 -> I18N.getText("library.dialog.addon.namespace");
      case 2 -> I18N.getText("library.dialog.addon.version");
      default -> null;
    };
  }

  @Override
  public void fireTableDataChanged() {
    try {
      addons.clear();
      addons.addAll(new LibraryManager().getLibraries(LibraryType.ADD_ON));
    } catch (ExecutionException | InterruptedException e) {
      log.error("Error displaying add-on libraries", e);
    }
    super.fireTableDataChanged();
  }
}
