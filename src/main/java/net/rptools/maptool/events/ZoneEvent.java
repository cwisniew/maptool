package net.rptools.maptool.events;

import net.rptools.maptool.model.Zone;

/**
 * Abstract base class for all Zone based events.
 */
abstract class ZoneEvent {

  /** Zone that the event {@link Zone} occured for. */
  private final Zone zone;

  /**
   * Creates a new {@code ZoneEvent}.
   * @param zone The {@link Zone} that the event occured for.
   */
  ZoneEvent(Zone zone) {
    this.zone = zone;
  }

  /**
   * Returns the {@link Zone} that the event occured for.
   * @return
   */
  public Zone getZone() {
    return zone;
  }

}
