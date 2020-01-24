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

import com.google.inject.Inject;
import com.google.inject.Provider;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import net.rptools.maptool.mtresource.MTResourceLibrary;
import net.rptools.maptool.mtresource.ResourceBundleTableModel;

public class ResourceBundlePanel extends JPanel {

  private final MTResourceLibrary resourceLibrary;
  private final JTable resourceBundlesTable;

  public ResourceBundlePanel(MTResourceLibrary lib) {
    resourceLibrary = lib;

    ResourceBundleTableModel tableModel = new ResourceBundleTableModel(resourceLibrary);
    resourceBundlesTable = new JTable(tableModel);

    resourceBundlesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    JScrollPane scrollPane = new JScrollPane(resourceBundlesTable);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    resourceBundlesTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    setLayout(new BorderLayout());
    add(scrollPane, BorderLayout.CENTER);

    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
    buttonPanel.add(new JButton("New"));
    buttonPanel.add(new JButton("Edit"));
    buttonPanel.add(new JButton("Delete"));

    add(buttonPanel, BorderLayout.PAGE_END);
  }
}
