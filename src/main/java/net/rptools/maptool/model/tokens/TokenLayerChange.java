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
package net.rptools.maptool.model.tokens;

import net.rptools.maptool.model.Token;
import net.rptools.maptool.model.Token.TokenShape;
import net.rptools.maptool.model.Zone;
import net.rptools.maptool.model.Zone.Layer;

/** Class used to represent valid layer changes for tokens/objects. */
public enum TokenLayerChange {
  /** Change to token layer. */
  TOKEN("layer.changeToToken", Zone.Layer.TOKEN, null),
  /** Change to hidden layer. */
  HIDDEN("layer.changeToHidden", Zone.Layer.GM, null),
  /** Change to object layer maintaining the current token shape. */
  OBJECT_AS_TOKEN("layer.changeToObjectAsToken", Zone.Layer.OBJECT, null),
  /** Change to object layer changing the token shape to top down. */
  OBJECT_AS_OBJECT("layer.changeToObjectAsObject", Zone.Layer.OBJECT, TokenShape.TOP_DOWN),
  /** Change to background layer maintaining the current token shape. */
  BACKGROUND_AS_TOKEN("layer.changeToBackgroundAsToken", Zone.Layer.BACKGROUND, null),
  /** Change to background layer changing the token shape to top down. */
  BACKGROUND_AS_OBJECT(
      "layer.changeToBackgroundAsObject", Zone.Layer.BACKGROUND, TokenShape.TOP_DOWN);

  /** The i18n key for the name of the change. */
  private final String i18NKey;
  /** The layer to change to. */
  private final Zone.Layer layer;
  /** The shape to change to, null to retain current shape. */
  private final Token.TokenShape newShape;

  /**
   * Creates a new instance of TokenLayerChange.
   *
   * @param i18NKey The i18n key for the name of the change.
   * @param layer The layer to change to.
   * @param newShape The shape to change to, null to retain current shape.
   */
  TokenLayerChange(String i18NKey, Zone.Layer layer, Token.TokenShape newShape) {
    this.i18NKey = i18NKey;
    this.layer = layer;
    this.newShape = newShape;
  }

  /**
   * Returns the i18n key for the name of the change.
   *
   * @return The i18n key for the name of the change.
   */
  public String getI18NKey() {
    return i18NKey;
  }

  /**
   * Returns the layer to change to.
   *
   * @return The layer to change to.
   */
  public Layer getLayer() {
    return layer;
  }

  /**
   * Returns the shape to change to, null to retain current shape.
   *
   * @return The shape to change to, null to retain current shape.
   */
  public TokenShape getNewShape() {
    return newShape;
  }
}
