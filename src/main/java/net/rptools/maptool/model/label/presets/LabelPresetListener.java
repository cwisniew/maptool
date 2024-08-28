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
package net.rptools.maptool.model.label.presets;

import com.google.common.eventbus.Subscribe;
import net.rptools.maptool.client.MapTool;
import net.rptools.maptool.client.events.LabelPresetAdded;
import net.rptools.maptool.model.label.LabelManager;

/** Listens for changes to label presets. */
public class LabelPresetListener {

  /** The label manager. */
  private final LabelManager labelManager;

  /**
   * Instantiates a new {@code PresetChangeListener}.
   *
   * @param labelManager the label manager.
   */
  public LabelPresetListener(LabelManager labelManager) {
    this.labelManager = labelManager;
  }

  /**
   * Handles the event when a label preset is added.
   *
   * @param event the event.
   */
  @Subscribe
  public void onLabelPresetAdded(LabelPresetAdded event) {
    // TODO: CDW this should be searching for / sending presets not labels
    labelManager.getLabelsWithIds(event.ids()).forEach( label ->
        MapTool.serverCommand().putLabelPreset(label));
    event.ids().forEach(labelManager::updateZonesLabelsWithPresetId);
    repaintZoneLabels();
  }

  /**
   * Handles the event when a label preset is changed.
   *
   * @param event the event.
   */
  @Subscribe
  public void onLabelPresetChanged(LabelPresetAdded event) {
    // TODO: CDW this should be searching for / sending presets not labels
    labelManager.getLabelsWithIds(event.ids()).forEach( label ->
        MapTool.serverCommand().updateLabelPreset(label));
    event.ids().forEach(labelManager::updateZonesLabelsWithPresetId);
    repaintZoneLabels();
  }

  /**
   * Handles the event when a label preset is removed.
   *
   * @param event the event.
   */
  @Subscribe
  public void onLabelPresetRemoved(LabelPresetAdded event) {
    // TODO: CDW this should be searching for / sending presets not labels
    labelManager.getLabelsWithIds(event.ids()).forEach( label ->
        MapTool.serverCommand().removeLabelPreset(label.getId()));
    event.ids().forEach(labelManager::updateZonesLabelsWithPresetId);
    repaintZoneLabels();
  }

  /** Repaints the current zone. */
  private void repaintZoneLabels() {
    MapTool.getFrame().getCurrentZoneRenderer().repaint();
  }
}
