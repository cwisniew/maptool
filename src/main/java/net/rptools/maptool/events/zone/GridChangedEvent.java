package net.rptools.maptool.events.zone;

import net.rptools.maptool.model.Grid;
import net.rptools.maptool.model.Zone;

/**
 * Event fired when the {@link Grid} is Changed for a {@link Zone}.
 */
public class GridChangedEvent  extends ZoneEvent {

  private final Grid grid;

  /**
   * Creates a new {@code ZoneEvent}.
   *
   * @param grid The {@link Grid} that changed.
   * @param zone The {@link Zone} that the event occurred for.
   */
  public GridChangedEvent(Grid grid, Zone zone) {
    super(zone);
    this.grid = grid;
  }

  /**
   * Returns the {@link Grid} that changed.
   * @return the {@link Grid} that changed.
   */
  public Grid getGrid() {
    return grid;
  }
}
