package net.rptools.maptool.events;

import net.rptools.maptool.model.Zone;

/**
 * Event raised when a new {@link Zone} is added.
 */
public class ZoneActivatedEvent extends ZoneEvent{

  private final Zone oldZone;

  /**
   * Creates a new {@code ZoneAddedEvent}.
   *
   * @param activatedZone The {@link Zone} that was changed to.
   * @param deactivatedZone The {@link Zone} that was previously active.
   */
  public ZoneActivatedEvent(Zone activatedZone, Zone deactivatedZone) {
    super(activatedZone);
    oldZone = deactivatedZone;
  }

  /**
   * Returns the {@link Zone} that was previously active.
   * @return the {@link Zone} that was previously active.
   */
  public Zone getOldZone() {
    return oldZone;
  }
}
