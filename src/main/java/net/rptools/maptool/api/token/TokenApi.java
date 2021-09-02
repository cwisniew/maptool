package net.rptools.maptool.api.token;

import java.util.List;
import net.rptools.maptool.client.MapTool;
import net.rptools.maptool.client.ui.zone.ZoneRenderer;
import net.rptools.maptool.model.Token;
import net.rptools.maptool.model.Zone;

public class TokenApi {

  /**
   * Finds the specified token.
   *
   * @param identifier the identifier of the token (name, GM name, or GUID).
   * @param zoneName the name of the zone. If null, check current zone.
   * @return the token, or null if none found.
   */
  public Token findToken(String identifier, String zoneName) {
    if (identifier == null) {
      return null;
    }
    if (zoneName == null || zoneName.length() == 0) {
      ZoneRenderer zr = MapTool.getFrame().getCurrentZoneRenderer();
      return zr == null ? null : zr.getZone().resolveToken(identifier);
    } else {
      List<ZoneRenderer> zrenderers = MapTool.getFrame().getZoneRenderers();
      for (ZoneRenderer zr : zrenderers) {
        Zone zone = zr.getZone();
        if (zone.getName().equalsIgnoreCase(zoneName)) {
          Token token = zone.resolveToken(identifier);
          if (token != null) {
            return token;
          }
        }
      }
    }
    return null;
  }
}
