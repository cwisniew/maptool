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
package net.rptools.maptool.model;

import net.rptools.maptool.language.I18N;
import net.rptools.maptool.util.I18nName;

public enum ShapeType implements I18nName {
  SQUARE("ShapeTypes.square"),
  CIRCLE("ShapeTypes.circle"),
  CONE("ShapeTypes.cone"),
  HEX("ShapeTypes.hex"),
  GRID("ShapeTypes.grid");

  private final String i18nName;

  ShapeType(String i18nName) {
    this.i18nName = i18nName;
  }

  @Override
  public String i18nName() {
    return I18N.getText(i18nName);
  }
}
