package net.rptools.maptool.events;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

/**
 * Utility Class for MapTool {@link EventBus}es.
 */
public class MapToolEventBus {

  /** Mapping between {@link EventBus} and name. */
  private static final Map<String, EventBus> eventBusMap = new ConcurrentHashMap<>();

  /** The name of the main {@link EventBus}. */
  private static final String DEFAULT_EVENT_BUS_NAME = "main-mt-event-queue'";

  /**
   * Creates a new {@code MapToolEventBus} object.
   */
  public MapToolEventBus() {
    eventBusMap.computeIfAbsent(DEFAULT_EVENT_BUS_NAME, key -> new AsyncEventBus(key, Executors.newSingleThreadExecutor()));
  }

  /**
   * Returns the main {@link EventBus} for Map Tool.
   * @return the main {@link EventBus} for Map Tool.
   */
  public EventBus getMainEventBus() {
    return eventBusMap.get(DEFAULT_EVENT_BUS_NAME);
  }
}
