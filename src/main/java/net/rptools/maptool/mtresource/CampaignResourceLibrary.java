package net.rptools.maptool.mtresource;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.table.TableModel;

public class CampaignResourceLibrary implements MTResourceLibrary {
  private final Set<MTResourceBundle> resourceBundles = new TreeSet<>();
  private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

  @Override
  public void addResourceBundle(MTResourceBundle resourceBundle) {
    resourceBundles.add(resourceBundle);
    propertyChangeSupport.firePropertyChange("addResourceBundle", null, resourceBundle);
  }

  @Override
  public Collection<MTResourceBundle> getResourceBundles() {
    return Collections.unmodifiableCollection(resourceBundles);
  }

  @Override
  public TableModel getTableModel() {
    return null;
  }

  @Override
  public int getResourceBundleCount() {
    return resourceBundles.size();
  }

  @Override
  public void addPropertyChangeListener(PropertyChangeListener pcl) {
    propertyChangeSupport.addPropertyChangeListener(pcl);
  }

  @Override
  public void removePropertyChangeListener(PropertyChangeListener pcl) {
    propertyChangeSupport.removePropertyChangeListener(pcl);
  }

}
