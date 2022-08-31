package net.rptools.maptool.model.map;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Bookmark {
  private final UUID id;
  private String mapKey;
  private String name;
  private String notes;
  private String gmNotes;
  private boolean visibleToPlayers;
  private Point iconLocation;
  private final Set<Point> locations = new HashSet<>();
  private final Set<Encounter> encounters = new HashSet<>();

  public Bookmark(String name) {
    id = UUID.randomUUID();
    this.name = name;
  }
}
