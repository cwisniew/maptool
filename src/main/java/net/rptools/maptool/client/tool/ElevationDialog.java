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



import static org.kordamp.ikonli.material2.Material2AL.ADD_CIRCLE;
import static org.kordamp.ikonli.material2.Material2AL.ARROW_CIRCLE_DOWN;
import static org.kordamp.ikonli.material2.Material2AL.ARROW_DROP_DOWN;
import static org.kordamp.ikonli.material2.Material2AL.ARROW_RIGHT;
import static org.kordamp.ikonli.material2.Material2AL.LINK;
import static org.kordamp.ikonli.material2.Material2MZ.REMOVE_CIRCLE;
import static org.kordamp.ikonli.material2.Material2MZ.SETTINGS;
import static org.kordamp.ikonli.material2.Material2OutlinedAL.ELEVATOR;
import static org.kordamp.ikonli.material2.Material2OutlinedMZ.VISIBILITY;
import static org.kordamp.ikonli.material2.Material2OutlinedMZ.VISIBILITY_OFF;
import static org.kordamp.ikonli.material2.Material2RoundAL.ARROW_CIRCLE_UP;
import static org.kordamp.ikonli.material2.Material2RoundAL.EDIT;

import com.google.common.eventbus.Subscribe;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import net.rptools.maptool.client.MapTool;
import net.rptools.maptool.language.I18N;
import net.rptools.maptool.model.zone.ElevationLevel;
import net.rptools.maptool.model.zone.ZoneLoadedEvent;
import org.kordamp.ikonli.swing.FontIcon;

public class ElevationDialog extends JPanel {

  private final JList<ElevationLevel> elevationLevelJList = new JList<>();

  private final JLabel elevationLabel = new JLabel(I18N.getText("label.elevation"));


  private final JPanel contentPanel = new JPanel();

  private final JPanel minifiedContentPanel = new JPanel();

  private final JLabel minifiedLabel = new JLabel();

  private static final int BUTTON_ICON_SIZE = 24;
  private static final FontIcon addElevationIcon = FontIcon.of(ADD_CIRCLE , BUTTON_ICON_SIZE,
      UIManager.getColor("Label.foreground"));

  private static final FontIcon removeElevationIcon = FontIcon.of(REMOVE_CIRCLE, BUTTON_ICON_SIZE,
      UIManager.getColor("Label.foreground"));

  private static final FontIcon editElevationIcon = FontIcon.of(EDIT, BUTTON_ICON_SIZE,
      UIManager.getColor("Label.foreground"));

  private static final FontIcon linkElevationIcon = FontIcon.of(LINK, BUTTON_ICON_SIZE, UIManager.getColor(
      "Label.foreground"));

  private static final FontIcon tokenUp = FontIcon.of(ARROW_CIRCLE_UP, BUTTON_ICON_SIZE, UIManager.getColor(
      "Label.foreground"));

  private static final FontIcon tokenDown = FontIcon.of(ARROW_CIRCLE_DOWN, BUTTON_ICON_SIZE, UIManager.getColor(
      "Label.foreground"));

  private static final FontIcon setting = FontIcon.of(SETTINGS, BUTTON_ICON_SIZE, UIManager.getColor(
      "Label.foreground"));

  private static final FontIcon tokenElevation = FontIcon.of(ELEVATOR, BUTTON_ICON_SIZE, UIManager.getColor(
      "Label.foreground"));

  private static final FontIcon groupVisibleOn = FontIcon.of(VISIBILITY, BUTTON_ICON_SIZE,
      UIManager.getColor(
      "Label.foreground"));

  private static final FontIcon groupVisibleOff = FontIcon.of(VISIBILITY_OFF, BUTTON_ICON_SIZE,
      UIManager.getColor(
          "Label.foreground"));
  private boolean minified = false;


  public ElevationDialog() {
    super();
    setLayout(new BorderLayout());
    contentPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    elevationLevelJList.setBorder(
        BorderFactory.createLineBorder(UIManager.getColor("Component.borderColor")));
    contentPanel.setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = 1;
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    contentPanel.add(elevationLabel, gbc);
    elevationLabel.setIcon(FontIcon.of(ARROW_DROP_DOWN, 16, UIManager.getColor("Label.foreground")));
    gbc.gridy++;
    contentPanel.add(elevationLevelJList, gbc);
    gbc.gridy++;
    //var addButton = new JButton(I18N.getText("elevation.add"));
    gbc.gridwidth = 1;
    gbc.weightx = 0.5;
    var addButton = new JButton(addElevationIcon);
    contentPanel.add(addButton, gbc);
    gbc.gridx++;
    var removeButton = new JButton(removeElevationIcon);
    contentPanel.add(removeButton, gbc);
    gbc.gridx++;
    var editButton = new JButton(editElevationIcon);
    contentPanel.add(editButton,gbc);
    gbc.gridx = 0;
    gbc.gridy++;
    var linkButton = new JButton(linkElevationIcon);
    contentPanel.add(linkButton, gbc);
    gbc.gridx++;
    var tokenUpButton = new JButton(tokenUp);
    contentPanel.add(tokenUpButton, gbc);
    gbc.gridx++;
    var tokenDownButton = new JButton(tokenDown);
    contentPanel.add(tokenDownButton, gbc);
    gbc.gridx = 0;
    gbc.gridy++;
    var settingButton = new JButton(setting);
    contentPanel.add(settingButton, gbc);
    gbc.gridx++;
    var tokenElevationButton = new JButton(tokenElevation);
    contentPanel.add(tokenElevationButton, gbc);
    add(contentPanel);


    minifiedContentPanel.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
    minifiedContentPanel.setLayout(new BorderLayout());
    minifiedContentPanel.add(minifiedLabel, BorderLayout.CENTER);
  }

  private void toggleMinified() {
    minified = !minified;
    if (minified) {
      removeAll();
      //TODO: CDW: minifiedLabel.setText(currentLayer.toString());
      minifiedLabel.setIcon(FontIcon.of(ARROW_RIGHT, 16, UIManager.getColor("Label.foreground")));
      add(minifiedContentPanel);
    } else {
      removeAll();
      add(contentPanel);
    }
    revalidate();
    MapTool.getFrame().refreshControlPanel();
  }


  @Subscribe
  public void handleZoneLoaded(ZoneLoadedEvent event) {
    var zone = MapTool.getCampaign().getZone(event.zoneId());
    ElevationLevel[] levels;
    if (zone == null) {
      levels = new ElevationLevel[0];
    } else {
      levels = zone.getElevation().getLevels().toArray(new ElevationLevel[0]);
    }
    SwingUtilities.invokeLater(
        () -> {
          elevationLevelJList.setListData(levels);
          contentPanel.setVisible(zone.getElevation().isEnabled());
        });
  }
}
