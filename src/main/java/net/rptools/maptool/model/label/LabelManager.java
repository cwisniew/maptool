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
package net.rptools.maptool.model.label;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import net.rptools.maptool.client.MapTool;
import net.rptools.maptool.model.GUID;
import net.rptools.maptool.model.Label;
import net.rptools.maptool.model.Zone;
import net.rptools.maptool.model.label.presets.LabelPresets;

/** Manages labels. */
public class LabelManager {

  /** The {@link net.rptools.maptool.model.Label} presets. */
  private static final LabelPresets presets = LabelPresets.createTrackedLabelPresets();

  /**
   * Gets the {@link net.rptools.maptool.model.Label} presets.
   *
   * @return the label presets.
   */
  public LabelPresets getPresets() {
    return presets;
  }

  /**
   * Returns all labels.
   *
   * @return all labels.
   */
  public Set<Label> getAllLabels() {
    return Set.copyOf(getLabelZoneMap().keySet());
  }

  /**
   * Returns the labels with the given preset id.
   *
   * @param id the id of the preset.
   * @return the labels with the given preset id.
   */
  public Set<Label> getLabelsWithPresetId(GUID id) {
    return Set.copyOf(getLabelZoneMapWithPresetId(id).keySet());
  }

  /**
   * Returns a map of label ids to zone ids.
   *
   * @return a map of label ids to zone ids.
   */
  private Map<Label, Zone> getLabelZoneMap() {
    var labelZoneMap = new HashMap<Label, Zone>();
    MapTool.getCampaign()
        .getZones()
        .forEach(
            zone -> {
              zone.getLabels()
                  .forEach(
                      label -> {
                        labelZoneMap.put(label, zone);
                      });
            });
    return labelZoneMap;
  }

  /**
   * Returns a map of label ids to zone ids with the given preset id.
   *
   * @param id the id of the preset.
   * @return a map of label ids to zone ids with the given preset id.
   */
  private Map<Label, Zone> getLabelZoneMapWithPresetId(GUID id) {
    return getLabelZoneMap().entrySet().stream()
        .filter(entry -> entry.getKey().getPresetId().equals(id))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  /**
   * Updates the zones with the given preset id.
   *
   * @param id the id of the preset.
   */
  public void updateZonesLabelsWithPresetId(GUID id) {
    getLabelZoneMapWithPresetId(id)
        .forEach(
            (label, zone) -> {
              zone.putLabel(label);
            });
  }

  /**
   * Returns the label with the given id.
   * @param id the id of the label.
   * @return the label with the given id.
   */
  public Label getLabelWithId(GUID id) {
    return getLabelZoneMap().keySet().stream()
        .filter(label -> label.getId().equals(id))
        .findFirst()
        .orElse(null);
  }

  /**
   * Returns the labels with the given ids.
   * @param id the ids of the labels.
   * @return the labels with the given ids.
   */
  public Set<Label> getLabelsWithIds(Collection<GUID> id) {
    return getLabelZoneMap().keySet().stream()
        .filter(label -> id.contains(label.getId()))
        .collect(Collectors.toSet());
  }
}
