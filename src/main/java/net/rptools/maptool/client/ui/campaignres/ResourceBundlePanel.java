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

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.function.Consumer;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import net.rptools.maptool.language.I18N;
import net.rptools.maptool.mtresource.MTResourceBundle;
import net.rptools.maptool.mtresource.MTResourceLibrary;
import net.rptools.maptool.mtresource.ResourceBundleTableModel;

public class ResourceBundlePanel extends JPanel {

  private final MTResourceLibrary resourceLibrary;
  private final JTable resourceBundlesTable;
  private final ResourceBundleTableModel tableModel;

  private MTResourceBundle selectedResourceBundle;

  public ResourceBundlePanel(MTResourceLibrary lib, Consumer<MTResourceBundle> bundleSelected) {
    resourceLibrary = lib;

    tableModel = new ResourceBundleTableModel(resourceLibrary);
    resourceBundlesTable = new JTable(tableModel);
    // TODO CDW: resourceBundlesTable.getSelectionModel().addListSelectionListener(l -> { if
    // (!l.getValueIsAdjusting()) System.out.println(resourceBundlesTable.getSelectedRow()); });
    resourceBundlesTable
        .getSelectionModel()
        .addListSelectionListener(
            l -> {
              if (!l.getValueIsAdjusting()) {
                int index = resourceBundlesTable.getSelectedRow();
                if (index >= 0) {
                  selectedResourceBundle = tableModel.getResourceBundle(index);
                  bundleSelected.accept(selectedResourceBundle);
                }
              }
            });

    resourceBundlesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    JScrollPane scrollPane = new JScrollPane(resourceBundlesTable);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    resourceBundlesTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    setLayout(new BorderLayout());
    add(scrollPane, BorderLayout.CENTER);

    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
    JButton newButton = new JButton(I18N.getText("panel.CampaignResources.bundle.button.new"));
    newButton.addActionListener(
        l -> {
          ResourceBundleDialog dialog = new ResourceBundleDialog();
          dialog.showDialog();
        });
    buttonPanel.add(newButton);

    JButton editButton = new JButton(I18N.getText("panel.CampaignResources.bundle.button.edit"));
    editButton.addActionListener(
        l -> {
          ResourceBundleDialog dialog = new ResourceBundleDialog(selectedResourceBundle);
          dialog.showDialog();
        }
    );
    buttonPanel.add(editButton);

    JButton deleteButton =
        new JButton(I18N.getText("panel.CampaignResources.bundle.button.delete"));
    buttonPanel.add(deleteButton);

    add(buttonPanel, BorderLayout.PAGE_END);
  }
}
