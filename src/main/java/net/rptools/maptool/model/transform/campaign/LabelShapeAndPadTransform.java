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
package net.rptools.maptool.model.transform.campaign;

import java.awt.Color;
import java.util.regex.Pattern;
import net.rptools.lib.ModelVersionTransformation;

/**
 * A class that implements the ModelVersionTransformation interface to transform XML strings by
 * replacing the end label tag with a new label tag that includes padding and shape type.
 */
public class LabelShapeAndPadTransform implements ModelVersionTransformation {

  /** The end label tag that we want to replace. */
  private static final String endLabelTag = "</net.rptools.maptool.model.Label>";

  /** The default horizontal padding for labels. */
  private static final int DEFAULT_HORIZONTAL_PADDING = 4;

  /** The default vertical padding for labels. */
  private static final int DEFAULT_VERTICAL_PADDING = 4;

  /**
   * The foreground color that we want to use for the label where the legacy foreground color is 0.
   */
  private static final Color legacyForegroundColor = new Color(0.0f, 0.0f, 0.0f, 1.0f);

  private static final String foreground0Replacement =
      "<foregroundColor>" + legacyForegroundColor.getRGB() + "</foregroundColor>\n" + endLabelTag;

  /** The background color that we want to use for the new label tag. */
  private static final Color legacyBackgroundColor = new Color(0.82f, 0.82f, 0.82f, 1.0f);

  /** The replacement string that we want to use for the new label tag. */
  private static final String replacement =
      "<horizontalPadding>"
          + DEFAULT_HORIZONTAL_PADDING
          + "</horizontalPadding>\n"
          + "<verticalPadding>"
          + DEFAULT_VERTICAL_PADDING
          + "</verticalPadding>\n"
          + "<shape>RECTANGLE</shape>"
          + endLabelTag;

  /** The pattern that we want to use to match the end label tag. */
  private static final Pattern pattern = Pattern.compile(endLabelTag, Pattern.DOTALL);

  @Override
  public String transform(String xml) {
    return pattern.matcher(xml).replaceAll(replacement);
  }
}
