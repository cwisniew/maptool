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

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import net.rptools.maptool.model.Label;
import net.rptools.maptool.server.proto.LabelPresetDto;
import net.rptools.maptool.server.proto.LabelPresetsDto;

public class LabelPresets {

  /** The presets for labels, these are stored as {@link Label} objects. */
  private ConcurrentHashMap<String, Label> presets = new ConcurrentHashMap<>();

  public Set<String> getPresetNames() {
    return new HashSet<>(presets.keySet());
  }

  public Label getPreset(String name) {
    return presets.get(name);
  }

  public String getPresetName(Label label) {
    if (label == null) {
      return null;
    }
    for (var entry : presets.entrySet()) {
      if (entry.getValue().equals(label)) {
        return entry.getKey();
      }
    }
    return null;
  }

  public void addPreset(String name, Label label) {
    // TODO: Check that the name doesn't already exist.

    label.setPresetsId(label.getPresetsId()); // The presets for a presets is it's self.
    presets.put(name, label);
  }

  public LabelPresetsDto toDto() {
    var presetsDto = LabelPresetsDto.newBuilder();
    for (var entry : presets.entrySet()) {
      var dto = LabelPresetDto.newBuilder();
      var preset = entry.getValue();
      dto.setId(preset.getId().toString()).setName(entry.getKey()).setPreset(preset.toDto());
      presetsDto.addPresets(dto);
    }
    return presetsDto.build();
  }

  public static LabelPresets fromDto(LabelPresetsDto dto) {
    var presets = new LabelPresets();
    for (var preset : dto.getPresetsList()) {
      presets.addPreset(preset.getName(), Label.fromDto(preset.getPreset()));
    }
    return presets;
  }

  public void setPresets(LabelPresets newPresets) {
    clear();
    presets.putAll(newPresets.presets);
  }

  public void rename(String oldName, String newName) {
    // TODO: Check that name does not already exist.
    var value = presets.remove(oldName);
    if (value != null) {
      presets.put(newName, value);
    }
  }

  public void clear() {
    presets.clear();
  }
}
