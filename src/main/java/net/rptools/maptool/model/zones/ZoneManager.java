package net.rptools.maptool.model.zones;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;
import net.rptools.maptool.model.GUID;
import net.rptools.maptool.model.Zone;

public class ZoneManager {

  /** The name of the root zone group. This is a constant and should not be changed. */
  public static final String ROOT_ZONE_GROUP_NAME = "Root";

  /** The id of the root zone group. This is a constant and should not be changed. */
  public static final UUID ROOT_ZONE_GROUP_ID =
      UUID.nameUUIDFromBytes(ROOT_ZONE_GROUP_NAME.getBytes(StandardCharsets.UTF_8));

  private final ReentrantLock lock = new ReentrantLock();
  private final ZoneGroupTreeNode root = new ZoneGroupTreeNode(ROOT_ZONE_GROUP_ID,
      ROOT_ZONE_GROUP_NAME);

  private Map<GUID, Zone> zoneMap = new HashMap<>();

  private Map<Zone, ZoneGroupTreeNode> zoneGroupMap = new HashMap<>();

  public void addZone(Zone zone) {
    lock.lock();
    try {
      addZone(zone, root);
    } finally {
      lock.unlock();
    }
  }

  public void addZone(Zone zone, ZoneGroupTreeNode zoneGroup) {
    lock.lock();
    try {
      removeZoneFromGroup(zone);
      zoneGroup.addZone(zone);
      zoneMap.put(zone.getId(), zone);
      // TODO: CDW send event
    } finally {
      lock.unlock();
    }
  }

  public void addZone(Zone zone, UUID groupId) {
    lock.lock();
    try {
      var zoneGroup = zoneGroupMap.get(groupId);
      if (zoneGroup == null) {
        throw new IllegalArgumentException("No zone group with id " + groupId);
        // TODO: CDW
      }
      addZone(zone, zoneGroup);
    } finally {
      lock.unlock();
    }
  }

  private void removeZoneFromGroup(Zone zone) {
    lock.lock();
    try {
      var oldZoneGroup = zoneGroupMap.get(zone);
      if (oldZoneGroup != null) {
        oldZoneGroup.removeZone(zone);
      }
    } finally {
      lock.unlock();
    }
  }

  public void removeZone(Zone zone) {
    lock.lock();
    try {
      removeZoneFromGroup(zone);
      zoneMap.remove(zone.getId());
      // TODO: CDW send event
    } finally {
      lock.unlock();
    }
  }

}
