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

import net.rptools.maptool.language.I18N;

public enum LabelShape {
  RECTANGLE("Label.shape.rectangle", true),
  ELLIPSE("Label.shape.ellipse", true),
  DIAMOND("Label.shape.diamond", true),
  TRIANGLE("Label.shape.triangle", true);

  private final String i18nKey;
  private final boolean applyArc;

  LabelShape(String i18nKey, boolean applyArc) {
    this.i18nKey = i18nKey;
    this.applyArc = applyArc;
  }

  public String getI18nKey() {
    return i18nKey;
  }

  public String toString() {
    return I18N.getString(i18nKey);
  }
}
