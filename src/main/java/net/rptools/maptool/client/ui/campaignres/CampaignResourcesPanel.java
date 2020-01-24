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
package net.rptools.maptool.client.ui.campaignres;

import com.google.inject.Provider;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.inject.Inject;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

public class CampaignResourcesPanel extends JPanel {


  @Inject
  public CampaignResourcesPanel(Provider<ResourceBundlePanel> provider) {
    JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    splitPane.setContinuousLayout(true);
    ResourceBundlePanel resourceBundlePanel = provider.get();
    Dimension minTopDimension = new Dimension(100, 200);
    resourceBundlePanel.setMinimumSize(minTopDimension);
    splitPane.setTopComponent(resourceBundlePanel);
    splitPane.setBottomComponent(new ResourceListPanel());
    splitPane.setDividerLocation(200);
    setLayout(new BorderLayout());
    add(splitPane, BorderLayout.CENTER);
  }
}
