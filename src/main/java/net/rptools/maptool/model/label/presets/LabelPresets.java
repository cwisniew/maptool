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
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import net.rptools.maptool.client.events.LabelPresetAdded;
import net.rptools.maptool.client.events.LabelPresetChanged;
import net.rptools.maptool.client.events.LabelPresetRemoved;
import net.rptools.maptool.events.MapToolEventBus;
import net.rptools.maptool.model.GUID;
import net.rptools.maptool.model.Label;
import net.rptools.maptool.server.proto.LabelPresetDto;
import net.rptools.maptool.server.proto.LabelPresetsDto;

/** The label presets. */
public class LabelPresets {

  /** Maps the name of the preset to the preset. */
  private final ConcurrentHashMap<String, Label> presetsNameMap = new ConcurrentHashMap<>();

  /** Maps the id of the preset to the preset. */
  private final ConcurrentHashMap<GUID, Label> presetsIdMap = new ConcurrentHashMap<>();

  /** Whether changes are tracked. */
  private final boolean tracked;

  /** Instantiates a new {@code LabelPresets} that notifies when changes occur. */
  public static LabelPresets createTrackedLabelPresets() {
    return new LabelPresets(true);
  }

  /** Instantiates a new {@code LabelPresets} that does not notify when changes occur. */
  public static LabelPresets createUntrackedLabelPresets() {
    return new LabelPresets(false);
  }

  /**
   * Instantiates a new {@code LabelPresets}. If {@code isTracked} is {@code true}, then changes,
   * including adding, removing, and renaming preset generate events on the event bus. If creating a
   * temporary {@code LabelPresets} that should not generate events, then {@code isTracked} should
   * be {@code false}.
   *
   * @param isTracked whether changes are tracked.
   */
  public LabelPresets(boolean isTracked) {
    tracked = isTracked;
  }

  /**
   * Returns the {@code Label} preset names.
   *
   * @return the {@code Label} preset names.
   */
  public Set<String> getPresetNames() {
    return new HashSet<>(presetsNameMap.keySet());
  }

  /**
   * Returns the {@code Label} for the given name.
   *
   * @param name the name of the preset.
   * @return the {@code Label} for the given name.
   */
  public Label getPreset(String name) {
    return presetsNameMap.get(name);
  }

  /**
   * Returns the {@code Label} for the given id.
   *
   * @param id the id of the preset.
   * @return the {@code Label} for the given id.
   */
  public Label getPreset(GUID id) {
    return presetsIdMap.get(id);
  }

  /**
   * Renames the preset with the given id.
   *
   * @param id the id of the preset.
   * @param newName the new name of the preset.
   */
  public void renamePreset(GUID id, String newName) {
    Label label = presetsIdMap.get(id);
    if (label != null) {
      presetsNameMap.remove(getPresetName(label));
      presetsNameMap.put(newName, label);
      label.setLabel(newName);
      if (tracked) {
        final var eventBus = new MapToolEventBus().getMainEventBus();
        eventBus.post(new LabelPresetChanged(List.of(label.getId())));
      }
    }
  }

  /**
   * Returns the name of the preset for the given {@code Label}.
   *
   * @param label the {@code Label}.
   * @return the name of the preset for the given {@code Label}.
   */
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

  /**
   * Adds a preset.
   *
   * @param name the name of the preset.
   * @param label the preset.
   * @throws IllegalArgumentException if the preset already exists.
   */
  public void addPreset(String name, Label label) {
    if (presetsNameMap.containsKey(name)) {
      throw new IllegalArgumentException("Label.preset.exists");
    }
    label.setPresetId(label.getPresetId()); // The preset id for a presets is it's self.
    presetsNameMap.put(name, label);
    presetsIdMap.put(label.getId(), label);
    if (tracked) {
      final var eventBus = new MapToolEventBus().getMainEventBus();
      eventBus.post(new LabelPresetAdded(List.of(label.getId())));
    }
  }

  /**
   * Returns the {@code Label} presets.
   *
   * @return the {@code Label} presets.
   */
  public Collection<Label> getPresets() {
    return Collections.list(presetsNameMap.elements());
  }

  /**
   * Returns the {@code LabelPresetsDto} for this {@code LabelPresets}.
   *
   * @return the {@code LabelPresetsDto} for this {@code LabelPresets}.
   */
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

  /**
   * Returns the {@code LabelPresets} for the given {@code LabelPresetsDto}.
   *
   * @param dto the {@code LabelPresetsDto}.
   * @return the {@code LabelPresets} for the given {@code LabelPresetsDto}.
   */
  public static LabelPresets fromDto(LabelPresetsDto dto) {
    var presets = LabelPresets.createUntrackedLabelPresets();
    for (var preset : dto.getPresetsList()) {
      presets.addPreset(preset.getName(), Label.fromDto(preset.getPreset()));
    }
    return presets;
  }

  /**
   * Sets the presets for {@code Label}s.
   *
   * @param newPresets the new presets.
   */
  public void setPresets(LabelPresets newPresets) {
    var old = List.copyOf(presetsIdMap.keySet());
    presetsNameMap.clear();
    presetsNameMap.putAll(newPresets.presetsNameMap);
    presetsIdMap.putAll(newPresets.presetsIdMap);
    if (tracked) {
      final var eventBus = new MapToolEventBus().getMainEventBus();
      if (!old.isEmpty()) {
        eventBus.post(new LabelPresetRemoved(old));
      }
      eventBus.post(new LabelPresetRemoved(List.copyOf(presetsIdMap.keySet())));
    }
  }

  /**
   * Returns the number of presets.
   *
   * @return the number of presets.
   */
  public int count() {
    return presetsNameMap.size();
  }

  /** Clears the presets. */
  public void clear() {
    clear(false);
  }

  /** Clears the presets at request of a remote client. */
  public void remoteClear() {
    clear(true);
  }

  /**
   * Clears the presets. If {@code isRemote} is {@code true}, then the request is from a remote
   * client and will not generate events.
   *
   * @param isRemote whether the request is from a remote client.
   */
  private void clear(boolean isRemote) {
    var ids = List.copyOf(presetsIdMap.keySet());
    presetsNameMap.clear();
    presetsIdMap.clear();
    if (tracked && !isRemote) {
      final var eventBus = new MapToolEventBus().getMainEventBus();
      eventBus.post(new LabelPresetAdded(ids));
    }
  }

  /**
   * Removes the preset with the given id at request of a remote client. This will not generate
   * remove events.
   *
   * @param id the id of the preset to remove.
   */
  public void remoteRemovePreset(GUID id) {
    removePreset(id, true);
  }

  /**
   * Removes the preset with the given id.
   *
   * @param id the id of the preset to remove.
   */
  public void removePreset(GUID id) {
    removePreset(id, false);
  }

  /**
   * Removes the preset with the given id. If {@code isRemote} is {@code true}, then the request is
   * from a remote client and will not generate events.
   *
   * @param id the id of the preset to remove.
   * @param isRemote whether the request is from a remote client.
   */
  private void removePreset(GUID id, boolean isRemote) {
    Label label = presetsIdMap.get(id);
    if (label != null) {
      presetsNameMap.remove(getPresetName(label));
      presetsIdMap.remove(id);
      if (tracked && !isRemote) {
        final var eventBus = new MapToolEventBus().getMainEventBus();
        eventBus.post(new LabelPresetRemoved(List.of(label.getId())));
      }
    }
  }

  /**
   * Updates the preset with the given id at request of a remote client.
   *
   * @param label the new preset.
   */
  public void remoteUpdatePreset(Label label) {
    updatePreset(label, true);
  }

  /**
   * Updates the preset with the given id.
   *
   * @param label the new preset.
   */
  public void updatePreset(Label label) {
    updatePreset(label, false);
  }

  /**
   * Updates the preset with the given id. If {@code isRemote} is {@code true}, then the request is
   * from a remote client and will not generate events.
   *
   * @param label the new preset values.
   * @param isRemote whether the request is from a remote client.
   */
  public void updatePreset(Label label, boolean isRemote) {
    Label old = presetsIdMap.get(label.getId());
    if (old != null) {
      presetsNameMap.remove(getPresetName(old));
    }
    presetsNameMap.put(label.getLabel(), label);
    presetsIdMap.put(label.getId(), label);
    if (tracked && !isRemote) {
      final var eventBus = new MapToolEventBus().getMainEventBus();
      eventBus.post(new LabelPresetChanged(List.of(label.getId())));
    }
  }
}
