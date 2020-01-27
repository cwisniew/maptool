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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
import net.rptools.maptool.mtresource.resource.MTResource;
import net.rptools.maptool.mtresource.resource.MTResourceFactory;

public class CampaignResourceLibrary implements MTResourceLibrary {
  private final Set<MTResourceBundle> resourceBundles = new TreeSet<>();
  private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

  // TODO: CDW remove
  public CampaignResourceLibrary() {
    // Add some bogus data for testing while developing
    MTResourceFactory resourceFactory = new MTResourceFactory();
    CampaignResourceBundle bundle =
        new CampaignResourceBundle("Test 1", "maptool.test1", "A test", "This is a test");
    bundle.setName("Test 1");
    bundle.setQualifiedName("maptool.test1");
    MTResource res = resourceFactory.resourceFor("res1", "/test", "res1.js", "var test = 1");
    bundle.putResource("/test", res);
    res = resourceFactory.resourceFor("res2", "/test/val1", "res2.html", "var test = 2");
    bundle.putResource("/test/val1", res);
    resourceBundles.add(bundle);

    bundle =
        new CampaignResourceBundle("Test 2", "maptool.test2", "Test No 2", "This is another test");
    resourceBundles.add(bundle);
    res = resourceFactory.resourceFor("res1_1", "/test/", "res1_1.js", "var test = 3");
    bundle.putResource("/test/", res);
    res = resourceFactory.resourceFor("res2_2", "/test2/", "res2_1.css", "var test = 2");
    bundle.putResource("/test2/", res);

    bundle =
        new CampaignResourceBundle(
            "Test 3", "maptool.test3", "Test No 3", "This is yet another test");
    res = resourceFactory.resourceFor("res3_1", "/", "res1_3.js", "var test = 3");
    bundle.putResource("/", res);
    res = resourceFactory.resourceFor("res3_2", "/test4", "res2_3.txt", "var test = 3");
    bundle.putResource("/test4", res);
    res = resourceFactory.resourceFor("res3_3", "/test4", "res3_3", "var test = 3");
    bundle.putResource("/test4", res);
    res = resourceFactory.resourceFor("res3_4", "/test4", "res3_4.mtms", "var test = 3");
    bundle.putResource("/test4", res);
    res = resourceFactory.resourceFor("res3_5", "/test4", "res3_5.csv", "var test = 3");
    bundle.putResource("/test4", res);

    resourceBundles.add(bundle);
  }

  private void addResourceBundle(MTResourceBundle resourceBundle) {
    resourceBundles.add(resourceBundle);
    propertyChangeSupport.firePropertyChange("addResourceBundle", null, resourceBundle);
  }

  @Override
  public void addResourceBundle(
      String name, String qualifiedName, String shortDescription, String longDescription) {
    addResourceBundle(
        new CampaignResourceBundle(name, qualifiedName, shortDescription, longDescription));
  }

  @Override
  public Collection<MTResourceBundle> getResourceBundles() {
    return Collections.unmodifiableCollection(resourceBundles);
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
