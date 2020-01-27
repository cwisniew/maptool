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
package net.rptools.maptool.mtresource;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import net.rptools.maptool.language.I18N;

public class ResourceBundleTableModel extends AbstractTableModel implements PropertyChangeListener {

  private static final int NUMBER_COLUMNS = 3;

  private final MTResourceLibrary resourceLibrary;

  private final List<MTResourceBundle> resourceBundles = new ArrayList<>();

  public ResourceBundleTableModel(MTResourceLibrary lib) {
    resourceLibrary = lib;
    resourceBundles.addAll(resourceLibrary.getResourceBundles());

    resourceLibrary.addPropertyChangeListener(pcl -> resourceBundleChange(pcl));
  }

  @Override
  public int getRowCount() {
    return resourceLibrary.getResourceBundles().size();
  }

  @Override
  public int getColumnCount() {
    return NUMBER_COLUMNS;
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    switch (columnIndex) {
      case 0:
        return getResourceBundle(rowIndex).getName();
      case 1:
        return getResourceBundle(rowIndex).getQualifiedName();
      case 2:
        return getResourceBundle(rowIndex).getShortDescription();
    }
    throw new IllegalArgumentException("Column out of range.");
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {}

  @Override
  public String getColumnName(int column) {
    switch (column) {
      case 0:
        return I18N.getText("panel.CampaignResources.bundle.table.name");
      case 1:
        return I18N.getText("panel.CampaignResources.bundle.table.qname");
      case 2:
        return I18N.getText("panel.CampaignResources.bundle.table.version");
    }

    return null;
  }

  public MTResourceBundle getResourceBundle(int index) {
    return resourceBundles.get(index);
  }

  private void resourceBundleChange(PropertyChangeEvent pcl) {
    resourceBundles.clear();
    resourceBundles.addAll(resourceLibrary.getResourceBundles());
    fireTableDataChanged();
  }
}
