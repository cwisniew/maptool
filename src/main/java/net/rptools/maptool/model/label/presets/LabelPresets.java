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

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import net.rptools.maptool.model.GUID;
import net.rptools.maptool.model.Label;
import net.rptools.maptool.server.proto.LabelPresetDto;
import net.rptools.maptool.server.proto.LabelPresetsDto;

public class LabelPresets {

  /** Maps the name of the preset to the preset. */
  private ConcurrentHashMap<String, Label> presetsNameMap = new ConcurrentHashMap<>();

  /** Maps the id of the preset to the preset. */
  private ConcurrentHashMap<GUID, Label> presetsIdMap = new ConcurrentHashMap<>();

  public Set<String> getPresetNames() {
    return new HashSet<>(presetsNameMap.keySet());
  }

  public Label getPreset(String name) {
    return presetsNameMap.get(name);
  }

  public Label getPreset(GUID id) {
    return presetsIdMap.get(id);
  }

  public String getPresetName(Label label) {
    if (label == null) {
      return null;
    }
    for (var entry : presetsNameMap.entrySet()) {
      if (entry.getValue().equals(label)) {
        return entry.getKey();
      }
    }
    return null;
  }

  public void addPreset(String name, Label label) {
    // TODO: Check that the name doesn't already exist.

    label.setPresetId(label.getPresetId()); // The presets for a presets is it's self.
    presetsNameMap.put(name, label);
    presetsIdMap.put(label.getId(), label);
  }

  public Collection<Label> getPresets() {
    return Collections.list(presetsNameMap.elements());
  }

  public LabelPresetsDto toDto() {
    var presetsDto = LabelPresetsDto.newBuilder();
    for (var entry : presetsNameMap.entrySet()) {
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
    presetsNameMap.putAll(newPresets.presetsNameMap);
  }

  public void rename(String oldName, String newName) {
    // TODO: Check that name does not already exist.
    var value = presetsNameMap.remove(oldName);
    if (value != null) {
      presetsNameMap.put(newName, value);
    }
  }

  public int count() {
    return presetsNameMap.size();
  }

  public void clear() {
    presetsNameMap.clear();
  }
}
