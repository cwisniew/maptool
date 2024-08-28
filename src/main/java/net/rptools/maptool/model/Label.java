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

import java.awt.Color;
import net.rptools.maptool.model.label.LabelManager;
import net.rptools.maptool.model.label.LabelShape;
import net.rptools.maptool.server.proto.LabelDto;

/**
 * The Label class represents a label in a graphical user interface. Labels are used to display text
 * on the screen.
 *
 * <p>A label can have various properties such as the text content, position, visibility of the
 * background and border, and the colors and font size.
 *
 * <p>This class provides methods to set and retrieve these properties of a label object.
 * Additionally, it provides constructors to create label objects with different properties.
 */
public class Label {

  /** The default font size for labels. */
  public static final int DEFAULT_LABEL_FONT_SIZE = 12;

  /** The default background color for labels. */
  public static final Color DEFAULT_LABEL_BACKGROUND_COLOR = new Color(0.82f, 0.82f, 0.82f, 1.0f);

  /** The default foreground color for labels. */
  public static final Color DEFAULT_LABEL_FOREGROUND_COLOR = Color.BLACK;

  /** The default border color for labels. */
  public static final Color DEFAULT_LABEL_BORDER_COLOR = DEFAULT_LABEL_FOREGROUND_COLOR;

  /** The default border width for labels. */
  public static final int DEFAULT_LABEL_BORDER_WIDTH = 2;

  /** The default border arc for labels. */
  public static final int DEFAULT_LABEL_BORDER_ARC = 10;

  /**
   * The unique identifier for a Label object.
   *
   * <p>The {@link GUID} object is used to generate a globally unique identifier for each instance
   * of the Label class. The ID is assigned when a new Label object is created and cannot be
   * modified afterwards.
   *
   * <p>The ID is used to uniquely identify each Label object.
   *
   * @see GUID
   */
  private final GUID id;

  /** The text of the label. The text of the label is the text that is displayed on the map. */
  private String label;

  /** The x coordinate of the label is the x coordinate of the location of the label on the map. */
  private int x;

  /** The y coordinate of the label is the y coordinate of the location of the label on the map. */
  private int y;

  /** Whether the background of the label is shown. */
  private boolean showBackground;

  /** Whether the border of the label is shown. */
  private boolean showBorder;

  /** The border arc of the label. */
  private int borderArc = DEFAULT_LABEL_BORDER_ARC;

  /** The border color of the label. */
  private int borderColor = DEFAULT_LABEL_BORDER_COLOR.getRGB();

  /** The border width of the label. */
  private int borderWidth = DEFAULT_LABEL_BORDER_WIDTH;

  /** The foreground color of the label. */
  private int foregroundColor = DEFAULT_LABEL_FOREGROUND_COLOR.getRGB();

  /** The background color of the label. */
  private int backgroundColor = DEFAULT_LABEL_BACKGROUND_COLOR.getRGB();

  /** The font size of the label. */
  private int fontSize = DEFAULT_LABEL_FONT_SIZE;

  /** The shape of the label. */
  private LabelShape shape = LabelShape.RECTANGLE;

  /** The default padding of the label. */
  private static final int DEFAULT_PADDING = 4;

  /** The horizontal padding of the label. */
  private int horizontalPadding = DEFAULT_PADDING;

  /** The vertical padding of the label. */
  private int verticalPadding = DEFAULT_PADDING;

  /** Whether the label is only visible to the GM. */
  private boolean gmOnly = false;

  /** The preview text of the label. */
  private String previewText;

  /**
   * The preset label ID to copy properties from.
   *
   * @see LabelManager
   * @see LabelManager#getPresets()
   * @see GUID
   */
  private GUID presetId;

  /** Indicates whether the label is a preset. */
  private boolean preset = false;

  /** The preset label object to copy properties from. */
  private volatile Label presetLabel;

  /**
   * Creates a new instance of the {@link Label} class.
   *
   * @param id the global unique identifier of the label
   * @param label the text content of the label
   * @param x the x-coordinate of the label's position
   * @param y the y-coordinate of the label's position
   * @param showBackground indicates whether the label should show the background
   * @param foregroundColor the color of the label's foreground
   * @param backgroundColor the color of the label's background
   * @param showBorder indicates whether the label should show the border
   * @param borderArc the arc of the label's border
   * @param fontSize the font size of the label
   * @param horizontalPadding the horizontal padding of the label
   * @param verticalPadding the vertical padding of the label
   * @param shape the shape of the label
   * @param gmOnly indicates whether the label is only visible to the GM
   * @param presetId the preset id to copy values from
   * @param previewText the preview text of the label
   */
  private Label(
      GUID id,
      String label,
      int x,
      int y,
      boolean showBackground,
      int foregroundColor,
      int backgroundColor,
      int borderColor,
      boolean showBorder,
      int borderWidth,
      int borderArc,
      int fontSize,
      int horizontalPadding,
      int verticalPadding,
      LabelShape shape,
      boolean gmOnly,
      GUID presetId,
      boolean preset,
      String previewText) {
    this.id = id;
    this.label = label;
    this.x = x;
    this.y = y;
    this.showBackground = showBackground;
    this.foregroundColor = foregroundColor;
    this.backgroundColor = backgroundColor;
    this.borderColor = borderColor;
    this.showBorder = showBorder;
    this.borderWidth = borderWidth;
    this.borderArc = borderArc;
    this.fontSize = fontSize;
    this.horizontalPadding = horizontalPadding;
    this.verticalPadding = verticalPadding;
    this.shape = shape;
    this.gmOnly = gmOnly;
    this.presetId = presetId;
    this.preset = preset;
    if (previewText == null || previewText.isBlank() || previewText.equals(label)) {
      this.previewText = label;
    } else {
      this.previewText = previewText;
    }
  }

  /**
   * Creates a new instance of the Label class with the specified properties.
   *
   * @param id the global unique identifier of the label
   * @param label the text content of the label
   * @param x the x-coordinate of the label's position
   * @param y the y-coordinate of the label's position
   * @param presetId the preset id to copy properties from
   * @param isPreset indicates whether the label is a preset
   */
  public Label(GUID id, String label, int x, int y, GUID presetId, boolean isPreset) {
    this.id = id;
    this.label = label;
    this.x = x;
    this.y = y;
    this.presetId = isPreset ? null : presetId;
    this.preset = isPreset;
    setFromPreset();
  }

  /** Sets the properties of the label object based on the preset label object. */
  private void setFromPreset() {
    if (preset) {
      return; // If this is a preset there is nothing to do
    }

    if (presetId == null) {
      presetLabel = null;
    } else {
      presetLabel = new LabelManager().getPresets().getPreset(presetId);
    }

    if (presetLabel != null) {
      showBackground = presetLabel.isShowBackground();
      foregroundColor = presetLabel.getForegroundColorValue();
      backgroundColor = presetLabel.getBackgroundColorValue();
      showBorder = presetLabel.isShowBorder();
      borderWidth = presetLabel.getBorderWidth();
      borderArc = presetLabel.getBorderArc();
      fontSize = presetLabel.getFontSize();
      horizontalPadding = presetLabel.getHorizontalPadding();
      verticalPadding = presetLabel.getVerticalPadding();
      shape = presetLabel.getShape();
      gmOnly = presetLabel.isGmOnly();
    }
  }

  /** Creates a new instance of the Label class with an empty string as the label text. */
  public Label() {
    this("");
  }

  /**
   * Creates a new instance of the Label class with the specified label text.
   *
   * @param label the text content of the label
   */
  public Label(String label) {
    this(label, 0, 0);
  }

  /**
   * Creates a new instance of the Label class with the specified label text, x-coordinate, and
   * y-coordinate.
   *
   * @param label the text content of the label
   * @param x the x-coordinate of the label's position
   * @param y the y-coordinate of the label's position
   */
  public Label(String label, int x, int y) {
    id = new GUID();
    this.label = label;
    this.x = x;
    this.y = y;
    showBackground = true;
    showBorder = true;
  }

  /**
   * Creates a new instance of the Label class by copying the properties from another Label object.
   *
   * @param label The Label object to copy from.
   */
  public Label(Label label) {
    this.id = new GUID();
    copyValuesFrom(label);
  }

  /**
   * Retrieves the label text of the Label object.
   *
   * @return the label text
   */
  public String getLabel() {
    return label;
  }

  /**
   * Sets the label text of the Label object.
   *
   * @param label the text content of the label
   */
  public void setLabel(String label) {
    this.label = label;
  }

  /**
   * Retrieves the global unique identifier (GUID) of the Label object.
   *
   * @return the GUID of the Label object
   */
  public GUID getId() {
    return id;
  }

  /**
   * Retrieves the x-coordinate of the Label object's position.
   *
   * @return the x-coordinate
   */
  public int getX() {
    return x;
  }

  /**
   * Sets the x-coordinate of the Label object's position.
   *
   * @param x the x-coordinate to set
   */
  public void setX(int x) {
    this.x = x;
  }

  /**
   * Retrieves the y-coordinate of the Label object's position.
   *
   * @return the y-coordinate
   */
  public int getY() {
    return y;
  }

  /**
   * Sets the y-coordinate of the Label object's position.
   *
   * @param y the y-coordinate to set
   */
  public void setY(int y) {
    this.y = y;
  }

  /**
   * Checks whether the background of the label should be shown.
   *
   * @return true if the background should be shown, false otherwise
   */
  public boolean isShowBackground() {
    if (!preset && presetLabel != null) {
      return presetLabel.isShowBackground();
    } else {
      return showBackground;
    }
  }

  /**
   * Sets whether the background of the label should be shown.
   *
   * @param showBackground indicates whether the background should be shown
   */
  public void setShowBackground(boolean showBackground) {
    if (preset || presetLabel == null) {
      this.showBackground = showBackground;
    } // else ignore
  }

  /**
   * Retrieves the foreground color of the Label object.
   *
   * @return the foreground color
   */
  public Color getForegroundColor() {
    if (!preset && presetLabel != null) {
      return presetLabel.getForegroundColor();
    } else {
      return new Color(foregroundColor, true);
    }
  }

  /**
   * Retrieves the background color of the Label object.
   *
   * @return the background color
   */
  public Color getBackgroundColor() {
    if (!preset && presetLabel != null) {
      return presetLabel.getBackgroundColor();
    } else {
      return new Color(backgroundColor, true);
    }
  }

  /**
   * Retrieves the font size of the Label object.
   *
   * @return the font size
   */
  public int getFontSize() {
    if (!preset && presetLabel != null) {
      return presetLabel.getFontSize();
    } else {
      return fontSize;
    }
  }

  /**
   * Retrieves the foreground color value of the Label object.
   *
   * @return the foreground color value
   */
  public int getForegroundColorValue() {
    if (!preset && presetLabel != null) {
      return presetLabel.getForegroundColorValue();
    } else {
      return foregroundColor;
    }
  }

  /**
   * Retrieves the background color value of the Label object.
   *
   * @return the background color value
   */
  public int getBackgroundColorValue() {
    if (!preset && presetLabel != null) {
      return presetLabel.getBackgroundColorValue();
    } else {
      return backgroundColor;
    }
  }

  /**
   * Sets the foreground color of the Label object.
   *
   * @param foregroundColor the foreground color to set
   */
  public void setForegroundColor(Color foregroundColor) {
    if (preset || presetLabel == null) {
      this.foregroundColor = foregroundColor.getRGB();
    } // else ignore
  }

  /**
   * Sets the background color of the Label object.
   *
   * @param backgroundColor the color to set as the background color
   */
  public void setBackgroundColor(Color backgroundColor) {
    if (preset || presetLabel == null) {
      this.backgroundColor = backgroundColor.getRGB();
    } // else ignore
  }

  /**
   * Sets the font size of the Label object.
   *
   * @param fontSize the font size to set
   */
  public void setFontSize(int fontSize) {
    if (preset || presetLabel == null) {
      this.fontSize = fontSize;
    } // else ignore
  }

  /**
   * Retrieves the border color of the Label object.
   *
   * @return the border color
   */
  public boolean isShowBorder() {
    if (!preset && presetLabel != null) {
      return presetLabel.isShowBorder();
    } else {
      return showBorder;
    }
  }

  /**
   * Sets whether the border of the Label object should be shown.
   *
   * @param showBorder indicates whether the border should be shown
   */
  public void setShowBorder(boolean showBorder) {
    if (preset || presetLabel == null) {
      this.showBorder = showBorder;
    } // else ignore
  }

  /**
   * Retrieves the border color of the Label object.
   *
   * @return the border color
   */
  public Color getBorderColor() {
    if (!preset && presetLabel != null) {
      return presetLabel.getBorderColor();
    } else {
      return new Color(borderColor, true);
    }
  }

  /**
   * Sets the border color of the Label object.
   *
   * @param borderColor the color to set as the border color
   */
  public void setBorderColor(Color borderColor) {
    if (preset || presetLabel == null) {
      this.borderColor = borderColor.getRGB();
    } // else ignore
  }

  /**
   * Retrieves the width of the border of the Label object.
   *
   * @return the width of the border
   */
  public int getBorderWidth() {
    if (!preset && presetLabel != null) {
      return presetLabel.getBorderWidth();
    } else {
      return borderWidth;
    }
  }

  /**
   * Sets the width of the border for the Label object.
   *
   * @param borderWidth the width of the border to set
   */
  public void setBorderWidth(int borderWidth) {
    if (preset || presetLabel == null) {
      this.borderWidth = borderWidth;
    } // else ignore
  }

  /**
   * Retrieves the arc of the border of the Label object.
   *
   * @return the arc of the border
   */
  public int getBorderArc() {
    if (!preset && presetLabel != null) {
      return presetLabel.getBorderArc();
    } else {
      return borderArc;
    }
  }

  /**
   * Sets the arc of the border for the Label object.
   *
   * @param borderArc the arc of the border to set
   */
  public void setBorderArc(int borderArc) {
    if (preset || presetLabel == null) {
      this.borderArc = borderArc;
    } // else ignore
  }

  /**
   * Retrieves the preset ID of the Label object.
   *
   * @return the preset ID of the Label object
   */
  public GUID getPresetId() {
    return presetId;
  }

  /**
   * Sets the preset ID of the Label object.
   *
   * @param presetId the preset ID to set.
   */
  public void setPresetId(GUID presetId) {
    if (!preset) { // If this is a preset there is nothing to do
      this.presetId = presetId;
      presetWasUpdated();
    }
  }

  /**
   * Retrieves the preset label object of the Label object.
   *
   * @return the preset label object of the Label object
   */
  public Label getPreset() {
    return presetLabel;
  }

  /**
   * Retrieves the shape of the Label object.
   *
   * @return the shape of the Label object
   */
  public LabelShape getShape() {
    return shape;
  }

  /**
   * Sets the shape of the Label object.
   *
   * @param shape the shape to set
   */
  public void setShape(LabelShape shape) {
    this.shape = shape;
  }

  /**
   * Retrieves the horizontal padding of the Label object.
   *
   * @return the horizontal padding of the Label object
   */
  public int getHorizontalPadding() {
    return horizontalPadding;
  }

  /**
   * Sets the horizontal padding of the Label object.
   *
   * @param horizontalPadding the horizontal padding to set
   */
  public void setHorizontalPadding(int horizontalPadding) {
    this.horizontalPadding = horizontalPadding;
  }

  /**
   * Retrieves the vertical padding of the Label object.
   *
   * @return the vertical padding of the Label object
   */
  public int getVerticalPadding() {
    return verticalPadding;
  }

  /**
   * Sets the vertical padding of the Label object.
   *
   * @param verticalPadding the vertical padding to set
   */
  public void setVerticalPadding(int verticalPadding) {
    this.verticalPadding = verticalPadding;
  }

  /**
   * Copies the properties of the provided Label object to the current Label object. This will not
   * copy the value of is a preset flag.
   *
   * @param from The label to copy the properties of.
   */
  public void copyValuesFrom(Label from) {
    this.label = from.label;
    this.x = from.x;
    this.y = from.y;
    this.showBackground = from.showBackground;
    this.foregroundColor = from.foregroundColor;
    this.backgroundColor = from.backgroundColor;
    this.borderColor = from.borderColor;
    this.showBorder = from.showBorder;
    this.borderWidth = from.borderWidth;
    this.borderArc = from.borderArc;
    this.fontSize = from.fontSize;
    this.horizontalPadding = from.horizontalPadding;
    this.verticalPadding = from.verticalPadding;
    this.shape = from.shape;
    this.presetId = from.presetId;
    this.presetLabel = from.presetLabel;
    this.gmOnly = from.gmOnly;
  }

  /** Checks whether the label is a preset. */
  public boolean isPresets() {
    return preset;
  }

  /**
   * Sets whether the label is only visible to the GM.
   *
   * @return true if the label is only visible to the GM, false otherwise
   */
  public boolean isGmOnly() {
    return gmOnly;
  }

  /**
   * Sets whether the label is only visible to the GM.
   *
   * @param gmOnly indicates whether the label is only visible to the GM
   */
  public void setGmOnly(boolean gmOnly) {
    this.gmOnly = gmOnly;
  }

  /**
   * Sets the preview text of the label.
   *
   * @param previewText the preview text to set.
   */
  public void setPreviewText(String previewText) {
    this.previewText = previewText;
  }

  /**
   * Retrieves the preview text of the label.
   *
   * @return the preview text of the label.
   */
  public String getPreviewText() {
    if (previewText == null || previewText.isBlank()) {
      return label;
    } else {
      return previewText;
    }
  }

  /**
   * Creates a new instance of the {@link Label} class based on the provided LabelDto object.
   *
   * @param dto the LabelDto object used to create the Label
   * @return a new instance of the Label class
   */
  public static Label fromDto(LabelDto dto) {
    return new Label(
        GUID.valueOf(dto.getId()),
        dto.getLabel(),
        dto.getX(),
        dto.getY(),
        dto.getShowBackground(),
        dto.getForegroundColor(),
        dto.getBackgroundColor(),
        dto.getBorderColor(),
        dto.getShowBorder(),
        dto.getBorderWidth(),
        dto.getBorderArc(),
        dto.getFontSize(),
        dto.getHorizontalPadding(),
        dto.getVerticalPadding(),
        LabelShape.valueOf(dto.getShape()),
        dto.getGmOnly(),
        dto.getPresetsId().isEmpty() ? null : GUID.valueOf(dto.getPresetsId()),
        dto.getPreset(),
        dto.getPreviewText());
  }

  /**
   * Converts the {@link Label} object to a {@link LabelDto} object.
   *
   * @return a new instance of the {@link LabelDto} class with the properties of the {@link Label}
   *     object.
   */
  public LabelDto toDto() {
    var dto =
        LabelDto.newBuilder()
            .setId(id.toString())
            .setLabel(label)
            .setX(x)
            .setY(y)
            .setShowBackground(showBackground)
            .setForegroundColor(foregroundColor)
            .setBackgroundColor(backgroundColor)
            .setFontSize(fontSize)
            .setBorderColor(borderColor)
            .setShowBorder(showBorder)
            .setBorderWidth(borderWidth)
            .setBorderArc(borderArc)
            .setHorizontalPadding(horizontalPadding)
            .setVerticalPadding(verticalPadding)
            .setShape(shape.name())
            .setGmOnly(gmOnly)
            .setPreset(preset);
    if (presetId != null) {
      dto.setPresetsId(presetId.toString());
    }
    return dto.build();
  }

  /** Sets the label as a preset. */
  public void setIsPreset(boolean isPreset) {
    preset = isPreset;
    if (preset) { // A preset can not have another label as its preset.
      presetId = null;
      presetLabel = null;
    }
  }

  /** Inform the label that the preset was updated. */
  public void presetWasUpdated() {
    setFromPreset();
  }
}
