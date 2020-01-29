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

public class ResourceBundleTableModel extends AbstractTableModel {

  private static final int NUMBER_COLUMNS = 3;
  private static final String[] COLUMN_HEADINGS = {
    I18N.getText("panel.CampaignResources.bundle.table.name"),
    I18N.getText("panel.CampaignResources.bundle.table.qname"),
    I18N.getText("panel.CampaignResources.bundle.table.shortDesc")
  };

  private final PropertyChangeListener addOrDelResourceBundle = this::resourceBundleAddOrRemove;
  private final PropertyChangeListener changeResourceBundle = this::resourceBundleChanged;

  private final MTResourceLibrary resourceLibrary;

  private final List<MTResourceBundle> resourceBundles = new ArrayList<>();

  public ResourceBundleTableModel(MTResourceLibrary lib) {
    resourceLibrary = lib;

    resourceLibrary.addPropertyChangeListener(addOrDelResourceBundle);

    for (MTResourceBundle bundle : resourceLibrary.getResourceBundles()) {
      addResourceBundle(bundle);
    }
  }

  private void addResourceBundle(MTResourceBundle bundle) {
    resourceBundles.add(bundle);
    bundle.addPropertyChangeListener(changeResourceBundle);
  }

  private void removeResourceBundle(MTResourceBundle bundle) {
    resourceBundles.remove(bundle);
    bundle.removePropertyChangeListener(changeResourceBundle);
  }

  @Override
  public int getRowCount() {
    return resourceBundles.size();
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
  public String getColumnName(int column) {
    return COLUMN_HEADINGS[column];
  }

  public MTResourceBundle getResourceBundle(int index) {
    return resourceBundles.get(index);
  }

  private void resourceBundleChanged(PropertyChangeEvent pcl) {
    int ind = resourceBundles.indexOf(pcl.getSource());
    if (ind >= 0) {
      fireTableRowsUpdated(ind, ind);
    }
  }

  private void resourceBundleAddOrRemove(PropertyChangeEvent pcl) {
    if (pcl.getPropertyName().equals(MTResourceLibrary.NEW_RESOURCE_BUNDLE)) {
      addResourceBundle((MTResourceBundle) pcl.getNewValue());
      fireTableRowsInserted(resourceBundles.size(), resourceBundles.size());
    } else if (pcl.getPropertyName().equals(MTResourceLibrary.REMOVE_RESOURCE_BUNDLE)) {
      removeResourceBundle((MTResourceBundle) pcl.getOldValue());
      fireTableDataChanged();
    }
  }
}
