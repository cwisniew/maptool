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
package net.rptools.maptool.client.tool;

import static org.kordamp.ikonli.material2.Material2AL.ARROW_DROP_DOWN;
import static org.kordamp.ikonli.material2.Material2AL.ARROW_RIGHT;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import net.rptools.maptool.client.MapTool;
import net.rptools.maptool.client.ui.zone.CollapsableControlPanel;
import net.rptools.maptool.language.I18N;
import net.rptools.maptool.model.Zone;
import net.rptools.maptool.model.Zone.Layer;
import org.kordamp.ikonli.swing.FontIcon;

public class LayerSelectionDialog extends CollapsableControlPanel {

  // TODO: CDW private final FormPanel panel;
  private JList<Zone.Layer> list;
  private LayerSelectionListener listener;
  private final Zone.Layer[] layerList;

  private final JPanel contentPanel = new JPanel();

  //private final JLabel layerLabel = new JLabel(I18N.getText("Label.layer"));

  private boolean minified = false;

  private static final LayerSelectionDialog instance = new LayerSelectionDialog(
        new Zone.Layer[] {
    Zone.Layer.TOKEN, Zone.Layer.GM, Zone.Layer.OBJECT, Zone.Layer.BACKGROUND
  }, null);
  private Layer currentLayer;

  public static LayerSelectionDialog getInstance() {
    return instance;
  }

  private LayerSelectionDialog(Zone.Layer[] layerList, LayerSelectionListener listener) {
    // TODO: CDW panel = new
    // FormPanelI18N("net/rptools/maptool/client/ui/forms/layerSelectionDialog.xml");
    this.listener = listener;
    this.layerList = layerList;

    getLayerList();
    setLayout(new BorderLayout());

    // TODO: CDW setLayout(new GridLayout(1, 1));
    // TODO: CDW add(panel);
    contentPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    contentPanel.setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = 1;
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    contentPanel.add(layerLabel, gbc);
    layerLabel.setIcon(FontIcon.of(ARROW_DROP_DOWN, 16, UIManager.getColor("Label.foreground")));
    gbc.fill = GridBagConstraints.BOTH;
    gbc.gridy++;
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    contentPanel.add(list, gbc);
    list.setBorder(BorderFactory.createLineBorder(UIManager.getColor("Component.borderColor")));
    list.setMaximumSize(new Dimension(Integer.MAX_VALUE, list.getPreferredSize().height));
    add(contentPanel);

    minifiedContentPanel.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
    minifiedContentPanel.setLayout(new BorderLayout());
    minifiedContentPanel.add(minifiedLayerLabel, BorderLayout.CENTER);

    layerLabel.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        toggleMinified();
      }
    });

    minifiedLayerLabel.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        toggleMinified();
      }
    });
  }

  private void toggleMinified() {
    minified = !minified;
    if (minified) {
      removeAll();
      minifiedLayerLabel.setText(currentLayer.toString());
      minifiedLayerLabel.setIcon(FontIcon.of(ARROW_RIGHT, 16, UIManager.getColor("Label.foreground")));
      add(minifiedContentPanel);
    } else {
      removeAll();
      add(contentPanel);
    }
    revalidate();
    MapTool.getFrame().refreshControlPanel();
  }

  public void setLayerSelectionListener(LayerSelectionListener listener) {
    this.listener = listener;
  }

  public void fireViewSelectionChange() {

    int index = list.getSelectedIndex();
    if (index >= 0 && listener != null) {
      currentLayer = list.getModel().getElementAt(index);
      listener.layerSelected(currentLayer);
    }
  }

  public void updateViewList() {
    var activeLayer = MapTool.getFrame().getCurrentZoneRenderer().getActiveLayer();
    getLayerList().setSelectedValue(activeLayer, true);
    currentLayer = activeLayer;
  }

  private JList<Zone.Layer> getLayerList() {

    if (list == null) {
      // TODO: CDW list = panel.getList("layerList");
      list = new JList<Zone.Layer>();

      DefaultListModel<Zone.Layer> model = new DefaultListModel<>();
      for (Zone.Layer layer : layerList) {
        model.addElement(layer);
      }

      list.setModel(model);
      list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      list.addListSelectionListener(
          e -> {
            if (e.getValueIsAdjusting()) {
              return;
            }

            fireViewSelectionChange();
          });
      list.setSelectedIndex(0);
    }

    return list;
  }

  public void setSelectedLayer(Zone.Layer layer) {
    list.setSelectedValue(layer, true);
  }

  public interface LayerSelectionListener {
    public void layerSelected(Zone.Layer layer);
  }
}
