package net.rptools.maptool.mtresource;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ResourceBundle;
import javax.swing.table.AbstractTableModel;

public class ResourceBundleTableModel extends AbstractTableModel implements PropertyChangeListener {

  private final MTResourceLibrary resourceLibrary;

  private ResourceBundle[] resourceBundles;

  private String[][] bogusTestData = {
      {"test", "maptool.test", "1.2.3"},
      {"test2", "maptool.test2", "2.2.3"},
      {"test3", "maptool.test3", "3.2.3"}
  };

  public ResourceBundleTableModel(MTResourceLibrary lib)  {
    resourceLibrary = lib;
  }

  @Override
  public int getRowCount() {
    return bogusTestData.length;
    // TODO: return resourceLibrary.getResourceBundles().size();
  }

  @Override
  public int getColumnCount() {
    return 3;
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    return bogusTestData[rowIndex][columnIndex];
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {

  }

  @Override
  public String getColumnName(int column) {
    switch (column) {
      case 0: return "Name";
      case 1: return "Qualified Name";
      case 2: return "Version";
    }

    return null;
  }
}
