package net.rptools.maptool.events;

import net.rptools.maptool.model.Zone;


/**
 * Event raised when a {@link Zone} is removed.
 */
public class ZoneRemovedEvent extends ZoneEvent{

  /**
   * Creates a new {@code ZoneAddedEvent}.
   *
   * @param zone The {@link Zone} that was removed..
   */
  public ZoneRemovedEvent(Zone zone) {
    super(zone);
  }
}
