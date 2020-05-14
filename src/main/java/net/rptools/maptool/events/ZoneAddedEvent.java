package net.rptools.maptool.events;

import net.rptools.maptool.model.Zone;

/**
 * Event raised when a new {@link Zone} is added.
 */
public class ZoneAddedEvent extends ZoneEvent{

  /**
   * Creates a new {@code ZoneAddedEvent}.
   *
   * @param zone The {@link Zone} that was added.
   */
  public ZoneAddedEvent(Zone zone) {
    super(zone);
  }
}
