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

public class LabelPresets {

  /** The presets for labels, these are stored as {@link Label} objects. */
  private ConcurrentHashMap<String, Label> presets = new ConcurrentHashMap<>();

  public Set<String> getPresetNames() {
    return new HashSet<>(presets.keySet());
  }

  public Label getLabel(String name) {
    return presets.get(name);
  }

  public void addPreset(String name, Label label) {
    presets.put(name, label);
  }
}
