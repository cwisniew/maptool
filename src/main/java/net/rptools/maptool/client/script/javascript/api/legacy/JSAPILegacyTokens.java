/*
 * This software Copyright by the RPTools.net development team, and
 * licensed under the Affero GPL Version 3 or, at your option, any later
 * version.
 *
 * MapTool Source Code is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * You should have received a copy of the GNU Affero General Public
 * License * along with this source Code.  If not, please visit
 * <http://www.gnu.org/licenses/> and specifically the Affero license
 * text at <http://www.gnu.org/licenses/agpl.html>.
 */
package net.rptools.maptool.client.script.javascript.api;

import java.util.ArrayList;
import java.util.List;
import net.rptools.maptool.client.MapTool;
import net.rptools.maptool.client.script.javascript.*;
import net.rptools.maptool.client.ui.zone.ZoneRenderer;
import net.rptools.maptool.model.Token;
import org.graalvm.polyglot.HostAccess;

public class JSAPILegacyTokens implements MapToolJSAPIInterface {
  @Override
  public String serializeToString() {
    return "MapToolLegacy.tokens";
  }

  @HostAccess.Export
  public List<Object> getMapTokens() {
    return getMapTokens(MapTool.getFrame().getCurrentZoneRenderer());
  }

  @HostAccess.Export
  public List<Object> getMapTokens(String zoneName) {

    return getMapTokens(MapTool.getFrame().getZoneRenderer(zoneName));
  }

  public List<Object> getMapTokens(ZoneRenderer zr) {
    final List<Object> tokens = new ArrayList<>();
    boolean trusted = JSScriptEngine.inTrustedContext();
    String playerId = MapTool.getPlayer().getName();
    zr.getZone()
        .getTokens()
        .forEach(
            (t -> {
              if (trusted || t.isOwner(playerId)) {
                tokens.add(new JSAPILegacyToken(t));
              }
            }));

    return tokens;
  }

  @HostAccess.Export
  public JSAPILegacyToken getTokenByName(String tokenName) {
    boolean trusted = JSScriptEngine.inTrustedContext();
    String playerId = MapTool.getPlayer().getName();
    for (ZoneRenderer z : MapTool.getFrame().getZoneRenderers()) {
      if (trusted || z.getZone().isVisible()) {
        Token t = z.getZone().getTokenByName(tokenName);
        if (t != null && (trusted || t.isOwner(playerId))) {
          return new JSAPILegacyToken(t);
        }
      }
    }
    return null;
  }

  @HostAccess.Export
  public List<JSAPILegacyToken> getSelectedTokens() {
    List<Token> tokens = MapTool.getFrame().getCurrentZoneRenderer().getSelectedTokensList();
    List<JSAPILegacyToken> out_tokens = new ArrayList<JSAPILegacyToken>();
    for (Token token : tokens) {
      out_tokens.add(new JSAPILegacyToken(token));
    }
    return out_tokens;
  }

  @HostAccess.Export
  public JSAPILegacyToken getSelected() {
    List<Token> tokens = MapTool.getFrame().getCurrentZoneRenderer().getSelectedTokensList();
    if (tokens.size() > 0) {
      return new JSAPILegacyToken(tokens.get(0));
    }
    return null;
  }

  @HostAccess.Export
  public JSAPILegacyToken getTokenByID(String uuid) {
    JSAPILegacyToken token = new JSAPILegacyToken(uuid);
    if (JSScriptEngine.inTrustedContext() || token.isOwner(MapTool.getPlayer().getName())) {
      return token;
    }
    return null;
  }
}
