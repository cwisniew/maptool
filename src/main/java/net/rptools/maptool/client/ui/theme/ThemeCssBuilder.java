package net.rptools.maptool.client.ui.theme;


import com.google.gson.JsonObject;
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.util.Set;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

public class ThemeCssBuilder {

  private record BorderVals(Color color, Color disabledColor, Color focusedColor,
      Color errorColor, Color focusedErrorColor, Color warningColor, Color focusedWarningColor,
      Color start, Color end, Color hoverColor,
      int width, int focusWidth, int innerFocusWidth) {
  }

  private enum ValueType {
    COLOR, FONT_FAMILY, FONT_SIZE, INSETS, INT, STRING, BORDER, DIMENSION, BOOLEAN
  }

  private record Extract(String key, String jsonProperty, ValueType type) {
  }

  private static final Set<Extract> CHECK_BOX_EXTRACT = Set.of(
      new Extract("CheckBox.foreground", "foreground", ValueType.COLOR),
      new Extract("CheckBox.background", "background", ValueType.COLOR),
      new Extract("CheckBox.disabledText", "disabledText", ValueType.COLOR),
      new Extract("CheckBox.margin", "padding", ValueType.INSETS),
      new Extract("CheckBox.iconTextGap", "iconTextGap", ValueType.INT),
      new Extract("CheckBox.font", "fontFamily", ValueType.FONT_FAMILY),
      new Extract("CheckBox.font", "fontSize", ValueType.FONT_SIZE),
      new Extract("CheckBox.border", "border", ValueType.BORDER)
  );

  private static final Set<Extract> COMBO_BOX_EXTRACT = Set.of(
      new Extract("ComboBox.foreground", "foreground", ValueType.COLOR),
      new Extract("ComboBox.disabledForeground", "disabledForeground", ValueType.COLOR),
      new Extract("ComboBox.background", "background", ValueType.COLOR),
      new Extract("ComboBox.editableBackground", "editableBackground", ValueType.COLOR),
      new Extract("ComboBox.disabledBackground", "disabledBackground", ValueType.COLOR),
      new Extract("ComboBox.focusedBackground", "focusedBackground", ValueType.COLOR),
      new Extract("ComboBox.buttonBackground", "buttonBackground", ValueType.COLOR),
      new Extract("ComboBox.buttonEditableBackground", "buttonEditableBackground", ValueType.COLOR),
      new Extract("ComboBox.buttonFocusedBackground", "buttonFocusedBackground", ValueType.COLOR),
      new Extract("ComboBox.buttonSeparatorColor", "buttonSeparatorColor", ValueType.COLOR),
      new Extract("ComboBox.buttonDisabledSeparatorColor", "buttonDisabledSeparatorColor", ValueType.COLOR),
      new Extract("ComboBox.buttonArrowColor", "buttonArrowColor", ValueType.COLOR),
      new Extract("ComboBox.buttonHoverArrowColor", "buttonHoverArrowColor", ValueType.COLOR),
      new Extract("ComboBox.buttonPressedArrowColor", "buttonPressedArrowColor", ValueType.COLOR),
      new Extract("ComboBox.buttonDisabledArrowColor", "buttonDisabledArrowColor", ValueType.COLOR),
      new Extract("ComboBox.popupBackground", "popupBackground", ValueType.COLOR),
      new Extract("ComboBox.selectionForeground", "selectionForeground", ValueType.COLOR),
      new Extract("ComboBox.selectionBackground", "selectionBackground", ValueType.COLOR),
      new Extract("ComboBox.buttonSeparatorWidth", "buttonSeparatorWidth", ValueType.INT),
      new Extract("ComboBox.padding", "margin", ValueType.INSETS),
      new Extract("ComboBox.minimumWidth", "minimumWidth", ValueType.INT),
      new Extract("ComboBox.editorColumns", "editorColumns", ValueType.INT),
      new Extract("ComboBox.maximumRowCount", "maximumRowCount", ValueType.INT),
      new Extract("ComboBox.font", "fontFamily", ValueType.FONT_FAMILY),
      new Extract("ComboBox.font", "fontSize", ValueType.FONT_SIZE),
      new Extract("ComboBox.border", "border", ValueType.BORDER),
      new Extract("ComboBox.buttonStyle", "buttonStyle", ValueType.STRING),
      new Extract("ComboBox.arrowType", "arrowType", ValueType.BORDER)
  );

  private static final Set<Extract> LABEL_EXTRACT = Set.of(
      new Extract("Label.foreground", "foreground", ValueType.COLOR),
      new Extract("Label.disabledForeground", "disabledForeground", ValueType.COLOR),
      new Extract("Label.background", "background", ValueType.COLOR),
      new Extract("Label.font", "fontFamily", ValueType.FONT_FAMILY),
      new Extract("Label.font", "fontSize", ValueType.FONT_SIZE)
  );

  private static final Set<Extract> LIST_EXTRACT = Set.of(
      new Extract("List.foreground", "foreground", ValueType.COLOR),
      new Extract("List.background", "background", ValueType.COLOR),
      new Extract("List.selectionForeground", "selectionForeground", ValueType.COLOR),
      new Extract("List.selectionBackground", "selectionBackground", ValueType.COLOR),
      new Extract("List.selectionInactiveForeground", "selectionInactiveForeground", ValueType.COLOR),
      new Extract("List.selectionInactiveBackground", "selectionInactiveBackground", ValueType.COLOR),
      new Extract("List.cellFocusColor", "cellFocusColor", ValueType.COLOR),
      new Extract("List.dropLineColor", "dropLineColor", ValueType.COLOR),
      new Extract("List.dropCellBackground", "dropCellBackground", ValueType.COLOR),
      new Extract("List.dropCellForeground", "dropCellForeground", ValueType.COLOR),
      new Extract("List.cellMargins", "cellPadding", ValueType.INSETS),
      new Extract("List.font", "fontFamily", ValueType.FONT_FAMILY),
      new Extract("List.font", "fontSize", ValueType.FONT_SIZE),
      new Extract("List.cellNoFocusBorder", "cellNoFocusBorder", ValueType.BORDER),
      new Extract("List.focusCellHighlightBorder", "focusCellHighlightBorder", ValueType.BORDER),
      new Extract("List.focusSelectedCellHighlightBorder", "focusSelectedCellHighlightBorder", ValueType.BORDER),
      new Extract("List.visibleRowCount", "visibleRowCount", ValueType.INT)
  );


  private static final Set<Extract> MENU_BAR_EXTRACT = Set.of(
      new Extract("MenuBar.foreground", "foreground", ValueType.COLOR),
      new Extract("MenuBar.background", "background", ValueType.COLOR),
      new Extract("MenuBar.selectionForeground", "selectionForeground", ValueType.COLOR),
      new Extract("MenuBar.selectionBackground", "selectionBackground", ValueType.COLOR),
      new Extract("MenuBar.hoverBackground", "hoverBackground", ValueType.COLOR),
      new Extract("MenuBar.underlineSelectionColor", "underlineSelectionColor", ValueType.BORDER),
      new Extract("MenuBar.underlineSelectionBackground", "underlineSelectionBackground", ValueType.COLOR),
      new Extract("MenuBar.borderColor", "borderColor", ValueType.COLOR),
      new Extract("MenuBar.itemMargins", "itemMargins", ValueType.INSETS),
      new Extract("MenuBar.underlineSelectionHeight", "underlineSelectionHeight", ValueType.INT),
      new Extract("MenuBar.font", "fontFamily", ValueType.FONT_FAMILY),
      new Extract("MenuBar.font", "fontSize", ValueType.FONT_SIZE),
      new Extract("MenuBar.border", "border", ValueType.BORDER)
  );

  private static final Set<Extract> MENU_ITEM_EXTRACT = Set.of(
      new Extract("MenuItem.foreground", "foreground", ValueType.COLOR),
      new Extract("MenuItem.disabledForeground", "disabledForeground", ValueType.COLOR),
      new Extract("MenuItem.background", "background", ValueType.COLOR),
      new Extract("MenuItem.selectionForeground", "selectionForeground", ValueType.COLOR),
      new Extract("MenuItem.selectionBackground", "selectionBackground", ValueType.COLOR),
      new Extract("MenuItem.acceleratorForeground", "acceleratorForeground", ValueType.COLOR),
      new Extract("MenuItem.acceleratorSelectionForeground", "acceleratorSelectionForeground", ValueType.COLOR),
      new Extract("MenuItem.checkBackground", "checkBackground", ValueType.COLOR),
      new Extract("MenuItem.underlineSelectionColor", "underlineSelectionColor", ValueType.BORDER),
      new Extract("MenuItem.underlineSelectionBackground", "underlineSelectionBackground", ValueType.COLOR),
      new Extract("MenuItem.underlineSelectionCheckBackground", "underlineSelectionCheckBackground", ValueType.COLOR),
      new Extract("MenuItem.margin", "padding", ValueType.INSETS),
      new Extract("MenuItem.minimumWidth", "minimumWidth", ValueType.INT),
      new Extract("MenuItem.minimumIconSize", "minimumIconSize", ValueType.INT),
      new Extract("MenuItem.iconTextGap", "iconTextGap", ValueType.INT),
      new Extract("MenuItem.textAcceleratorGap", "textAcceleratorGap", ValueType.INT),
      new Extract("MenuItem.textNoAcceleratorGap", "textNoAcceleratorGap", ValueType.INT),
      new Extract("MenuItem.acceleratorArrowGap", "acceleratorArrowGap", ValueType.INT),
      new Extract("MenuItem.checkMargins", "checkMargins", ValueType.INSETS),
      new Extract("MenuItem.underlineSelectionHeight", "underlineSelectionHeight", ValueType.INT),
      new Extract("MenuItem.acceleratorFont", "acceleratorFontFamily", ValueType.FONT_FAMILY),
      new Extract("MenuItem.acceleratorFont", "acceleratorFontSize", ValueType.FONT_SIZE),
      new Extract("MenuItem.acceleratorDelimiter", "acceleratorDelimiter", ValueType.STRING),
      new Extract("MenuItem.border", "border", ValueType.BORDER),
      new Extract("MenuItem.opaque", "opaque", ValueType.BOOLEAN),
      new Extract("MenuItem.font", "fontFamily", ValueType.FONT_FAMILY),
      new Extract("MenuItem.font", "fontSize", ValueType.FONT_SIZE),
      new Extract("MenuItem.selectionType", "selectionType", ValueType.STRING)
  );

  private static final Set<Extract> MENU_EXTRACT = Set.of(
      new Extract("Menu.foreground", "foreground", ValueType.COLOR),
      new Extract("Menu.disabledForeground", "disabledForeground", ValueType.COLOR),
      new Extract("Menu.background", "background", ValueType.COLOR),
      new Extract("Menu.selectionForeground", "selectionForeground", ValueType.COLOR),
      new Extract("Menu.selectionBackground", "selectionBackground", ValueType.COLOR),
      new Extract("Menu.acceleratorForeground", "acceleratorForeground", ValueType.COLOR),
      new Extract("Menu.acceleratorSelectionForeground", "acceleratorSelectionForeground", ValueType.COLOR),
      new Extract("MenuItem.checkBackground", "checkBackground", ValueType.COLOR),
      new Extract("MenuItem.underlineSelectionColor", "underlineSelectionColor", ValueType.BORDER),
      new Extract("MenuItem.underlineSelectionBackground", "underlineSelectionBackground", ValueType.COLOR),
      new Extract("MenuItem.underlineSelectionCheckBackground", "underlineSelectionCheckBackground", ValueType.COLOR),
      new Extract("Menu.margin", "padding", ValueType.INSETS),
      new Extract("MenuItem.minimumWidth", "minimumWidth", ValueType.INT),
      new Extract("MenuItem.minimumIconSize", "minimumIconSize", ValueType.INT),
      new Extract("MenuItem.iconTextGap", "iconTextGap", ValueType.INT),
      new Extract("MenuItem.textAcceleratorGap", "textAcceleratorGap", ValueType.INT),
      new Extract("MenuItem.textNoAcceleratorGap", "textNoAcceleratorGap", ValueType.INT),
      new Extract("MenuItem.acceleratorArrowGap", "acceleratorArrowGap", ValueType.INT),
      new Extract("MenuItem.checkMargins", "checkMargins", ValueType.INSETS),
      new Extract("MenuItem.underlineSelectionHeight", "underlineSelectionHeight", ValueType.INT),
      new Extract("MenuItem.acceleratorFont", "acceleratorFontFamily", ValueType.FONT_FAMILY),
      new Extract("MenuItem.acceleratorFont", "acceleratorFontSize", ValueType.FONT_SIZE),
      new Extract("MenuItem.acceleratorDelimiter", "acceleratorDelimiter", ValueType.STRING),
      new Extract("Menu.border", "border", ValueType.BORDER),
      new Extract("Menu.opaque", "opaque", ValueType.BOOLEAN),
      new Extract("MenuItem.font", "fontFamily", ValueType.FONT_FAMILY),
      new Extract("Menu.font", "fontSize", ValueType.FONT_SIZE),
      new Extract("Menu.selectionType", "selectionType", ValueType.STRING)
  );

  private static final Set<Extract> MENU_CHECK_BOX_EXTRACT = Set.of(
      new Extract("CheckBoxMenuItem.foreground", "foreground", ValueType.COLOR),
      new Extract("CheckBoxMenuItem.disabledForeground", "disabledForeground", ValueType.COLOR),
      new Extract("CheckBoxMenuItem.background", "background", ValueType.COLOR),
      new Extract("CheckBoxMenuItem.selectionForeground", "selectionForeground", ValueType.COLOR),
      new Extract("CheckBoxMenuItem.selectionBackground", "selectionBackground", ValueType.COLOR),
      new Extract("CheckBoxMenuItem.acceleratorForeground", "acceleratorForeground", ValueType.COLOR),
      new Extract("CheckBoxMenuItem.acceleratorSelectionForeground", "acceleratorSelectionForeground", ValueType.COLOR),
      new Extract("MenuItem.checkBackground", "checkBackground", ValueType.COLOR),
      new Extract("MenuItem.underlineSelectionColor", "underlineSelectionColor", ValueType.BORDER),
      new Extract("MenuItem.underlineSelectionBackground", "underlineSelectionBackground", ValueType.COLOR),
      new Extract("MenuItem.underlineSelectionCheckBackground", "underlineSelectionCheckBackground", ValueType.COLOR),
      new Extract("CheckBoxMenuItem.margin", "padding", ValueType.INSETS),
      new Extract("MenuItem.minimumWidth", "minimumWidth", ValueType.INT),
      new Extract("MenuItem.minimumIconSize", "minimumIconSize", ValueType.INT),
      new Extract("MenuItem.iconTextGap", "iconTextGap", ValueType.INT),
      new Extract("MenuItem.textAcceleratorGap", "textAcceleratorGap", ValueType.INT),
      new Extract("MenuItem.textNoAcceleratorGap", "textNoAcceleratorGap", ValueType.INT),
      new Extract("MenuItem.acceleratorArrowGap", "acceleratorArrowGap", ValueType.INT),
      new Extract("MenuItem.checkMargins", "checkMargins", ValueType.INSETS),
      new Extract("MenuItem.underlineSelectionHeight", "underlineSelectionHeight", ValueType.INT),
      new Extract("MenuItem.acceleratorFont", "acceleratorFontFamily", ValueType.FONT_FAMILY),
      new Extract("MenuItem.acceleratorFont", "acceleratorFontSize", ValueType.FONT_SIZE),
      new Extract("MenuItem.acceleratorDelimiter", "acceleratorDelimiter", ValueType.STRING),
      new Extract("CheckBoxMenuItem.border", "border", ValueType.BORDER),
      new Extract("CheckBoxMenuItem.opaque", "opaque", ValueType.BOOLEAN),
      new Extract("MenuItem.font", "fontFamily", ValueType.FONT_FAMILY),
      new Extract("CheckBoxMenuItem.font", "fontSize", ValueType.FONT_SIZE),
      new Extract("CheckBoxMenuItem.selectionType", "selectionType", ValueType.STRING)
  );


  private static final Set<Extract> MENU_RADIO_BUTTON_EXTRACT = Set.of(
      new Extract("RadioButtonMenuItem.foreground", "foreground", ValueType.COLOR),
      new Extract("RadioButtonMenuItem.disabledForeground", "disabledForeground", ValueType.COLOR),
      new Extract("RadioButtonMenuItem.background", "background", ValueType.COLOR),
      new Extract("RadioButtonMenuItem.selectionForeground", "selectionForeground", ValueType.COLOR),
      new Extract("RadioButtonMenuItem.selectionBackground", "selectionBackground", ValueType.COLOR),
      new Extract("RadioButtonMenuItem.acceleratorForeground", "acceleratorForeground", ValueType.COLOR),
      new Extract("RadioButtonMenuItem.acceleratorSelectionForeground", "acceleratorSelectionForeground", ValueType.COLOR),
      new Extract("MenuItem.checkBackground", "checkBackground", ValueType.COLOR),
      new Extract("MenuItem.underlineSelectionColor", "underlineSelectionColor", ValueType.BORDER),
      new Extract("MenuItem.underlineSelectionBackground", "underlineSelectionBackground", ValueType.COLOR),
      new Extract("MenuItem.underlineSelectionCheckBackground", "underlineSelectionCheckBackground", ValueType.COLOR),
      new Extract("RadioButtonMenuItem.margin", "padding", ValueType.INSETS),
      new Extract("MenuItem.minimumWidth", "minimumWidth", ValueType.INT),
      new Extract("MenuItem.minimumIconSize", "minimumIconSize", ValueType.INT),
      new Extract("MenuItem.iconTextGap", "iconTextGap", ValueType.INT),
      new Extract("MenuItem.textAcceleratorGap", "textAcceleratorGap", ValueType.INT),
      new Extract("MenuItem.textNoAcceleratorGap", "textNoAcceleratorGap", ValueType.INT),
      new Extract("MenuItem.acceleratorArrowGap", "acceleratorArrowGap", ValueType.INT),
      new Extract("MenuItem.checkMargins", "checkMargins", ValueType.INSETS),
      new Extract("MenuItem.underlineSelectionHeight", "underlineSelectionHeight", ValueType.INT),
      new Extract("MenuItem.acceleratorFont", "acceleratorFontFamily", ValueType.FONT_FAMILY),
      new Extract("MenuItem.acceleratorFont", "acceleratorFontSize", ValueType.FONT_SIZE),
      new Extract("MenuItem.acceleratorDelimiter", "acceleratorDelimiter", ValueType.STRING),
      new Extract("RadioButtonMenuItem.border", "border", ValueType.BORDER),
      new Extract("RadioButtonMenuItem.opaque", "opaque", ValueType.BOOLEAN),
      new Extract("MenuItem.font", "fontFamily", ValueType.FONT_FAMILY),
      new Extract("RadioButtonMenuItem.font", "fontSize", ValueType.FONT_SIZE),
      new Extract("RadioButtonMenuItem.selectionType", "selectionType", ValueType.STRING)
  );

  private static final Set<Extract> PANEL_EXTRACT = Set.of(
      new Extract("Panel.background", "background", ValueType.COLOR),
      new Extract("Panel.foreground", "foreground", ValueType.COLOR),
      new Extract("Panel.font", "fontFamily", ValueType.FONT_FAMILY),
      new Extract("Panel.font", "fontSize", ValueType.FONT_SIZE)
  );

  private static final Set<Extract> POPUP_MENU_EXTRACT = Set.of(
      new Extract("PopupMenu.background", "background", ValueType.COLOR),
      new Extract("PopupMenu.foreground", "foreground", ValueType.COLOR),
      new Extract("PopupMenu.borderColor", "borderColor", ValueType.COLOR),
      new Extract("PopupMenu.borderInsets", "itemPadding", ValueType.BORDER),
      new Extract("PopupMenu.font", "fontFamily", ValueType.FONT_FAMILY),
      new Extract("PopupMenu.font", "fontSize", ValueType.FONT_SIZE),
      new Extract("PopupMenu.border", "border", ValueType.BORDER),
      new Extract("PopupMenu.opaque", "opaque", ValueType.BOOLEAN)
  );

  private static final Set<Extract> PROGRESS_BAR_EXTRACT = Set.of(
      new Extract("ProgressBar.foreground", "foreground", ValueType.COLOR),
      new Extract("ProgressBar.background", "background", ValueType.COLOR),
      new Extract("ProgressBar.selectionForeground", "selectionForeground", ValueType.COLOR),
      new Extract("ProgressBar.selectionBackground", "selectionBackground", ValueType.COLOR),
      new Extract("ProgressBar.font", "fontFamily", ValueType.FONT_FAMILY),
      new Extract("ProgressBar.font", "fontSize", ValueType.FONT_SIZE),
      new Extract("ProgressBar.border", "padding", ValueType.BORDER),
      new Extract("ProgressBar.arc", "arc", ValueType.INT),
      new Extract("ProgressBar.horizontalSize", "horizontalSize", ValueType.INT),
      new Extract("ProgressBar.verticalSize", "verticalSize", ValueType.INT)
  );

  private static final Set<Extract> RADIO_BUTTON_EXTRACT = Set.of(
      new Extract("RadioButton.foreground", "foreground", ValueType.COLOR),
      new Extract("RadioButton.background", "background", ValueType.COLOR),
      new Extract("RadioButton.disabledText", "disabledForeground", ValueType.COLOR),
      new Extract("RadioButton.margin", "padding", ValueType.BORDER),
      new Extract("RadioButton.font", "fontFamily", ValueType.FONT_FAMILY),
      new Extract("RadioButton.font", "fontSize", ValueType.FONT_SIZE),
      new Extract("RadioButton.iconTextGap", "iconTextGap", ValueType.INT)
  );

  private static final Set<Extract> SEPARATOR_EXTRACT = Set.of(
      new Extract("Separator.foreground", "foreground", ValueType.COLOR),
      new Extract("Separator.background", "background", ValueType.COLOR),
      new Extract("Separator.height", "height", ValueType.INT),
      new Extract("Separator.stripeWidth", "stripeWidth", ValueType.INT),
      new Extract("Separator.stripeIndent", "stripeIndent", ValueType.INT)
  );

  private static final Set<Extract> SPINNER_EXTRACT = Set.of(
      new Extract("Spinner.foreground", "foreground", ValueType.COLOR),
      new Extract("Spinner.background", "background", ValueType.COLOR),
      new Extract("Spinner.disabledForeground", "disabledForeground", ValueType.COLOR),
      new Extract("Spinner.disabledBackground", "disabledBackground", ValueType.COLOR),
      new Extract("Spinner.focusedBackground", "focusedBackground", ValueType.COLOR),
      new Extract("Spinner.buttonBackground", "buttonBackground", ValueType.COLOR),
      new Extract("Spinner.buttonSeparatorColor", "buttonSeparatorColor", ValueType.COLOR),
      new Extract("Spinner.buttonDisabledSeparatorColor", "buttonDisabledSeparatorColor", ValueType.COLOR),
      new Extract("Spinner.buttonArrowColor", "buttonArrowColor", ValueType.COLOR),
      new Extract("Spinner.buttonHoverArrowColor", "buttonHoverArrowColor", ValueType.COLOR),
      new Extract("Spinner.buttonPressedArrowColor", "buttonPressedArrowColor", ValueType.COLOR),
      new Extract("Spinner.buttonDisabledArrowColor", "buttonDisabledArrowColor", ValueType.COLOR),
      new Extract("Spinner.buttonSeparatorWidth", "buttonSeparatorWidth", ValueType.INT),
      new Extract("Spinner.padding", "padding", ValueType.INSETS),
      new Extract("Component.minimumWidth", "minimumWidth", ValueType.INT),
      new Extract("Component.border", "border", ValueType.BORDER),
      new Extract("Spinner.buttonStyle", "buttonStyle", ValueType.STRING),
      new Extract("Spinner.arrowType", "arrowType", ValueType.STRING),
      new Extract("Spinner.font", "fontFamily", ValueType.FONT_FAMILY),
      new Extract("Spinner.font", "fontSize", ValueType.FONT_SIZE)
  );

  private static final Set<Extract> TABLE_EXTRACT = Set.of(
      new Extract("Table.background", "background", ValueType.COLOR),
      new Extract("Table.foreground", "foreground", ValueType.COLOR),
      new Extract("Table.selectionForeground", "selectionForeground", ValueType.COLOR),
      new Extract("Table.selectionBackground", "selectionBackground", ValueType.COLOR),
      new Extract("Table.gridColor", "gridColor", ValueType.COLOR),
      new Extract("Table.alternateRowColor", "alternateRowColor", ValueType.COLOR),
      new Extract("Table.selectionInactiveForeground", "selectionInactiveForeground", ValueType.COLOR),
      new Extract("Table.selectionInactiveBackground", "selectionInactiveBackground", ValueType.COLOR),
      new Extract("Table.cellFocusColor", "cellFocusColor", ValueType.COLOR),
      new Extract("Table.focusCellForeground", "focusCellForeground", ValueType.COLOR),
      new Extract("Table.focusCellBackground", "focusCellBackground", ValueType.COLOR),
      new Extract("Table.dropLineColor", "dropLineColor", ValueType.COLOR),
      new Extract("Table.dropLineShortColor", "dropLineShortColor", ValueType.COLOR),
      new Extract("Table.dropCellForeground", "dropCellForeground", ValueType.COLOR),
      new Extract("Table.dropCellBackground", "dropCellBackground", ValueType.COLOR),
      new Extract("Table.font", "fontFamily", ValueType.FONT_FAMILY),
      new Extract("Table.font", "fontSize", ValueType.FONT_SIZE),
      new Extract("Table.rowHeight", "rowHeight", ValueType.INT),
      new Extract("Table.cellMargins", "cellPadding", ValueType.INSETS),
      new Extract("Table.intercellSpacing", "intercellSpacing", ValueType.DIMENSION),
      new Extract("Table.showHorizontalLines", "showHorizontalLines", ValueType.BOOLEAN),
      new Extract("Table.showVerticalLines", "showVerticalLines", ValueType.BOOLEAN),
      new Extract("Table.showCellFocusIndicator", "showCellFocusIndicator", ValueType.BOOLEAN)
  );

  private static final Set<Extract> TABLE_HEADER_EXTRACT = Set.of(
      new Extract("TableHeader.background", "background", ValueType.COLOR),
      new Extract("TableHeader.foreground", "foreground", ValueType.COLOR),
      new Extract("TableHeader.separatorColor", "separatorColor", ValueType.COLOR),
      new Extract("TableHeader.bottomSeparatorColor", "bottomSeparatorColor", ValueType.COLOR),
      new Extract("TableHeader.sortIconColor", "sortIconColor", ValueType.COLOR),
      new Extract("TableHeader.font", "fontFamily", ValueType.FONT_FAMILY),
      new Extract("TableHeader.font", "fontSize", ValueType.FONT_SIZE),
      new Extract("TableHeader.cellMargins", "cellPadding", ValueType.INSETS),
      new Extract("TableHeader.height", "height", ValueType.INT),
      new Extract("TableHeader.showTrailingVerticalLine", "showSortIcon", ValueType.BOOLEAN),
      new Extract("TableHeader.arrowType", "arrowType", ValueType.STRING),
      new Extract("TableHeader.sortIconPosition", "sortIconPosition", ValueType.STRING)
  );

  private static final Set<Extract> TEXT_AREA_EXTRACT = Set.of (
      new Extract("TextArea.background", "background", ValueType.COLOR),
      new Extract("TextArea.foreground", "foreground", ValueType.COLOR),
      new Extract("TextArea.inactiveForeground", "inactiveForeground", ValueType.COLOR),
      new Extract("TextArea.disabledBackground", "disabledBackground", ValueType.COLOR),
      new Extract("TextArea.focusedBackground", "focusedBackground", ValueType.COLOR),
      new Extract("TextArea.selectionForeground", "selectionForeground", ValueType.COLOR),
      new Extract("TextArea.selectionBackground", "selectionBackground", ValueType.COLOR),
      new Extract("TextArea.caretForeground", "caretForeground", ValueType.COLOR),
      new Extract("TextArea.margin", "padding", ValueType.INSETS),
      new Extract("Component.minimumWidth", "minimumWidth", ValueType.INT),
      new Extract("TextArea.font", "fontFamily", ValueType.FONT_FAMILY),
      new Extract("TextArea.font", "fontSize", ValueType.FONT_SIZE),
      new Extract("TextArea.border", "border", ValueType.BORDER)
  );

  private static final Set<Extract> TOOLTIP_EXTRACT = Set.of(
      new Extract("ToolTip.background", "background", ValueType.COLOR),
      new Extract("ToolTip.foreground", "foreground", ValueType.COLOR),
      new Extract("ToolTip.border", "border", ValueType.BORDER),
      new Extract("ToolTip.font", "fontFamily", ValueType.FONT_FAMILY),
      new Extract("ToolTip.font", "fontSize", ValueType.FONT_SIZE)
  );

  ThemeCssBuilder() {
  }

  JsonObject getLookAndFeelValues() {
    var uiDef = UIManager.getDefaults();


    var lookAndFeel = new JsonObject();
    lookAndFeel.add("checkBox", extract(uiDef, CHECK_BOX_EXTRACT));
    lookAndFeel.add("comboBox", extract(uiDef, COMBO_BOX_EXTRACT));
    lookAndFeel.add("label", extract(uiDef, LABEL_EXTRACT));
    lookAndFeel.add("list", extract(uiDef, LIST_EXTRACT));
    lookAndFeel.add("menuBar", extract(uiDef, MENU_BAR_EXTRACT));
    lookAndFeel.add("menuItem", extract(uiDef, MENU_ITEM_EXTRACT));
    lookAndFeel.add("menu", extract(uiDef, MENU_EXTRACT));
    lookAndFeel.add("menuCheckBox", extract(uiDef, MENU_CHECK_BOX_EXTRACT));
    lookAndFeel.add("menuRadioButton", extract(uiDef, MENU_RADIO_BUTTON_EXTRACT));
    lookAndFeel.add("panel", extract(uiDef, PANEL_EXTRACT));
    lookAndFeel.add("popupMenu", extract(uiDef, POPUP_MENU_EXTRACT));
    lookAndFeel.add("progressBar", extract(uiDef, PROGRESS_BAR_EXTRACT));
    lookAndFeel.add("radioButton", extract(uiDef, RADIO_BUTTON_EXTRACT));
    lookAndFeel.add("separator", extract(uiDef, SEPARATOR_EXTRACT));
    lookAndFeel.add("spinner", extract(uiDef, SPINNER_EXTRACT));
    lookAndFeel.add("table", extract(uiDef, TABLE_EXTRACT));
    lookAndFeel.add("tableHeader", extract(uiDef, TABLE_HEADER_EXTRACT));
    lookAndFeel.add("textArea", extract(uiDef, TEXT_AREA_EXTRACT));
    lookAndFeel.add("toolTip", extract(uiDef, TOOLTIP_EXTRACT));




    var buttonBorderVals = new BorderVals(
        uiDef.getColor("Button.borderColor"),
        uiDef.getColor("Button.disabledBorderColor"),
        uiDef.getColor("Button.focusedBorderColor"),
        null,
        null,
        null,
        null,
        uiDef.getColor("Button.startBorderColor"),
        uiDef.getColor("Button.endBorderColor"),
        uiDef.getColor("Button.hoverBorderColor"),
        uiDef.getInt("Button.borderWidth"),
        uiDef.getInt("Component.focusWidth"),
        uiDef.getInt("Button.innerFocusWidth"));

    var defaultBttonBorderVals = new BorderVals(
        uiDef.getColor("Button.default.borderColor"),
        uiDef.getColor("Button.disabledBorderColor"),
        uiDef.getColor("Button.default.focusedBorderColor"),
        null,
        null,
        null,
        null,
        uiDef.getColor("Button.default.startBorderColor"),
        uiDef.getColor("Button.default.endBorderColor"),
        uiDef.getColor("Button.default.hoverBorderColor"),
        uiDef.getInt("Button.default.borderWidth"),
        uiDef.getInt("Component.focusWidth"),
        uiDef.getInt("Button.default.innerFocusWidth"));

    var toolbarBorderVals = new BorderVals(
        uiDef.getColor("Button.borderColor"),
        uiDef.getColor("Button.disabledBorderColor"),
        uiDef.getColor("Button.focusedBorderColor"),
        null,
        null,
        null,
        null,
        uiDef.getColor("Button.startBorderColor"),
        uiDef.getColor("Button.endBorderColor"),
        uiDef.getColor("Button.hoverBorderColor"),
        uiDef.getInt("Button.borderWidth"),
        uiDef.getInt("Button.toolbar.focusWidth"),
        uiDef.getInt("Button.innerFocusWidth"));

    var toolbarPadding = uiDef.getInsets("Button.toolbar.spacingInsets");
    var toolbarMargin = uiDef.getInsets("Button.toolbar.margin");

    return lookAndFeel;

  }

  private JsonObject extract(UIDefaults uiDef, Set<Extract> extractValues) {
    var json = new JsonObject();
    for (var ex : extractValues) {
      switch (ex.type()) {
        case COLOR -> {
          var color = uiDef.getColor(ex.key());
          if (color != null) {
            json.addProperty(ex.jsonProperty(), formatColor(color));
          }
        }
        case FONT_FAMILY -> {
          var font = uiDef.getFont(ex.key());
          if (font != null) {
            json.addProperty(ex.jsonProperty(), font.getFamily());
          }
        }
        case FONT_SIZE -> {
          var font = uiDef.getFont(ex.key());
          if (font != null) {
            json.addProperty(ex.jsonProperty(), String.format("%dpx", font.getSize()));
          }
        }
        case INSETS -> {
          var insets = uiDef.getInsets(ex.key());
          if (insets != null) {
            var insetsJson = new JsonObject();
            insetsJson.addProperty("top", String.format("%dpx", insets.top));
            insetsJson.addProperty("left", String.format("%dpx", insets.left));
            insetsJson.addProperty("bottom",String.format("%dpx",  insets.bottom));
            insetsJson.addProperty("right", String.format("%dpx", insets.right));
            json.add(ex.jsonProperty(), insetsJson);
          }
        }
        case BORDER -> {
          var border = uiDef.getBorder(ex.key());
          if (border != null) {
            var borderJson = new JsonObject();
            var colour = uiDef.getColor("Component.borderColor");
            if (colour != null) {
              borderJson.addProperty("color", formatColor(colour));
            }
            var disabledColor = uiDef.getColor("Component.disabledBorderColor");
            if (disabledColor != null) {
              borderJson.addProperty("disabledColor", formatColor(disabledColor));
            }
            var focusedColor = uiDef.getColor("Component.focusedBorderColor");
            if (focusedColor != null) {
              borderJson.addProperty("focusedColor", formatColor(focusedColor));
            }
            var errorColor = uiDef.getColor("Component.errorBorderColor");
            if (errorColor != null) {
              borderJson.addProperty("errorColor", formatColor(errorColor));
            }
            var focusedErrorColor = uiDef.getColor("Component.focusedErrorBorderColor");
            if (focusedErrorColor != null) {
              borderJson.addProperty("focusedErrorColor", formatColor(focusedErrorColor));
            }
            var warningColor = uiDef.getColor("Component.warningBorderColor");
            if (warningColor != null) {
              borderJson.addProperty("warningColor", formatColor(warningColor));
            }
            borderJson.addProperty("width", uiDef.getInt("Component.borderWidth"));
            borderJson.addProperty("focusWidth", uiDef.getInt("Component.focusWidth"));
            borderJson.addProperty("innerFocusWidth", uiDef.getInt("Component.innerFocusWidth"));

            json.add(ex.jsonProperty(), borderJson);
          }
        }
        case DIMENSION -> {
          var dim = uiDef.getDimension(ex.key());
          if (dim != null) {
            var dimJson = new JsonObject();
            dimJson.addProperty("width", String.format("%dpx", dim.width));
            dimJson.addProperty("height", String.format("%dpx", dim.height));
            json.add(ex.jsonProperty(), dimJson);
          }
        }
        case INT -> {
          var val = uiDef.getInt(ex.key());
          json.addProperty(ex.jsonProperty(), val);
        }
        case STRING -> {
          var val = uiDef.getString(ex.key());
          json.addProperty(ex.jsonProperty(), val);
        }
        case BOOLEAN -> {
          var val = uiDef.getBoolean(ex.key());
          json.addProperty(ex.jsonProperty(), val);
        }
      }
    }
    return json;
  }

  private String formatFontSize(Font font) {
    return font.getSize() + "px";
  }

  private String formatFontFamily(Font font) {
    return font.getFamily();
  }


  private String formatInsets(Insets insets) {
    return String.format("%d %d %d %d", insets.top, insets.right, insets.bottom, insets.left);
  }

  private String formatColor(Color color) {
    return String.format("rgba(%d, %d, %d, %.02f)", color.getRed(), color.getGreen(),
        color.getBlue(), color.getAlpha() / 255.0);
  }


}
