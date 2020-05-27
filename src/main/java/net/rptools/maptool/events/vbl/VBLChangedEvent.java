package net.rptools.maptool.events.vbl;

import net.rptools.maptool.model.Zone;

public class VBLChangedEvent {

  private final Zone zone;

  public VBLChangedEvent(Zone zone) {
    this.zone = zone;
  }

  /**
   *
   * @return
   */
  public Zone getZone() {
    return zone;
  }

}
