package net.rptools.maptool.model.map;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Room {
  private final UUID id;
  private String name;
  private String mapKey;
  private String notes;
  private String gmNotes;
  private boolean visibleToPlayers;
  private final List<Point> vertices = new ArrayList<>();
  private final Set<Encounter> encounters = new HashSet<>();
  private MapMarker bookmark;

  public Room(String name) {
    id = UUID.randomUUID();
    this.name = name;
  }

}
