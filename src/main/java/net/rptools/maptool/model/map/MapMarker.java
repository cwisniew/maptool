package net.rptools.maptool.model.map;

import java.awt.Color;
import java.util.UUID;

/**
 * A Map Marker.
 */
public class MapMarker {
  /** The id of the marker. */
  private final UUID id;
  /** The key of the map marker. */
  private String mapKey;
  /** The name of the marker. */
  private String name;

  /** Description of the marker. */
  private String description = "This is a description for testing purposes.";

  /** The notes of the marker. */
  private String notes;
  /** The GM notes of the marker. */
  private String gmNotes;
  /** Is the marker visible to players. */
  private boolean visibleToPlayers;
  /** The X location of the marker. */
  private int iconX;
  /** The Y location of the marker. */
  private int iconY;

  private Color fillColor = Color.YELLOW;

  private Color textColor = Color.BLACK;

  private Color borderColor = Color.BLACK;

  /** Create a new marker. */
  public MapMarker(String name, String mapKey) {
    id = UUID.randomUUID();
    this.name = name;
    this.mapKey = mapKey;
  }

  public MapMarker(String name, String mapKey, int x, int y) {
    this(name, mapKey);
    this.iconX = x;
    this.iconY = y;
  }


  public UUID getId() {
    return id;
  }

  public int getIconX() {
    return iconX;
  }

  public void setIconX(int x) {
    iconX = x;
  }

  public int getIconY() {
    return iconY;
  }

  public void setIconY(int y) {
    iconY = y;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getMapKey() {
    return mapKey;
  }

  public void setMapKey(String mapKey) {
    this.mapKey = mapKey;
  }

  public Color getFillColor() {
    return fillColor;
  }

  public void setFillColor(Color fillColor) {
    this.fillColor = fillColor;
  }

  public Color getTextColor() {
    return textColor;
  }

  public void setTextColor(Color textColor) {
    this.textColor = textColor;
  }

  public Color getBorderColor() {
    return borderColor;
  }

  public void setBorderColor(Color borderColor) {
    this.borderColor = borderColor;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public String getGMNotes() {
    return gmNotes;
  }

  public void setGMNotes(String gmNotes) {
    this.gmNotes = gmNotes;
  }

  public boolean isVisibleToPlayers() {
    return visibleToPlayers;
  }

  public void setVisibleToPlayers(boolean visibleToPlayers) {
    this.visibleToPlayers = visibleToPlayers;
  }
}
