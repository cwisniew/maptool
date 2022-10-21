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
package net.rptools.maptool.model.zone;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import net.rptools.maptool.language.I18N;
import net.rptools.maptool.server.proto.ElevationDto;

public class Elevation {
  private static final ElevationLevel DEFAULT_LEVEL = new ElevationLevel(
      0, I18N.getText("elevation.default.name")
  );


  private boolean enabled;

  private final Set<ElevationLevel> levels =
      new TreeSet<>((el1, el2) -> Comparator.comparingInt(ElevationLevel::level).compare(el1, el2));

  private ElevationLevel currentLevel;

  public static Elevation fromDto(ElevationDto dto) {
    Elevation elevation = new Elevation();
    elevation.setEnabled(dto.getEnabled());
    dto.getLevelList().forEach(level -> elevation.levels.add(ElevationLevel.fromDto(level)));
    elevation.currentLevel = ElevationLevel.fromDto(dto.getCurrentLevel());
    return elevation;
  }

  public Elevation() {
    enabled = false;
    levels.add(DEFAULT_LEVEL);
    currentLevel = DEFAULT_LEVEL;
  }

  public Elevation(Elevation from) {
    enabled = from.enabled;
    levels.addAll(from.levels);
    currentLevel = from.currentLevel;
  }

  public ElevationDto toDto() {
    ElevationDto.Builder builder = ElevationDto.newBuilder().setEnabled(enabled);
    levels.forEach(level -> builder.addLevel(level.toDto()));
    builder.setCurrentLevel(currentLevel.toDto());
    return builder.build();
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public void addLevel(ElevationLevel level) {
    levels.add(level);
  }

  public void removeLevel(ElevationLevel level) {
    levels.remove(level);
    if (levels.isEmpty()) {
      levels.add(DEFAULT_LEVEL);
    }
    if (level.equals(getCurrentLevel())) {
      if (levels.contains(DEFAULT_LEVEL)) {
        setCurrentLevel(DEFAULT_LEVEL);
      } else {
        setCurrentLevel(levels.iterator().next());
      }
    }
  }

  public ElevationLevel getCurrentLevel() {
    return currentLevel;
  }

  public void setCurrentLevel(ElevationLevel level) {
    currentLevel = level;
    levels.add(currentLevel);
  }

  public List<ElevationLevel> getLevels() {
    return List.copyOf(levels);
  }

  public boolean isEnabled() {
    return enabled;
  }
}
