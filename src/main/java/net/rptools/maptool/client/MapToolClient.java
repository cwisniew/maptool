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
package net.rptools.maptool.client;

import static net.rptools.maptool.model.player.PlayerDatabaseFactory.PlayerDatabaseType.LOCAL_PLAYER;
import static net.rptools.maptool.model.player.PlayerDatabaseFactory.PlayerDatabaseType.PERSONAL_SERVER;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import net.rptools.clientserver.simple.connection.Connection;
import net.rptools.maptool.language.I18N;
import net.rptools.maptool.model.Campaign;
import net.rptools.maptool.model.CampaignFactory;
import net.rptools.maptool.model.campaign.CampaignManager;
import net.rptools.maptool.model.player.LocalPlayer;
import net.rptools.maptool.model.player.PlayerDatabase;
import net.rptools.maptool.model.player.PlayerDatabaseFactory;
import net.rptools.maptool.server.ServerCommand;
import net.rptools.maptool.server.ServerConfig;
import net.rptools.maptool.server.ServerPolicy;

/**
 * The client side of a client-server channel.
 *
 * <p>This has nothing to do with the GUI, but represents those parts of the client that are needed
 * to interact with a server. Most of this used to exist as global state in {@link
 * net.rptools.maptool.client.MapTool} and elsewhere.
 */
public class MapToolClient {
  private final LocalPlayer player;
  private final PlayerDatabase playerDatabase;
  private final IMapToolConnection conn;
  private Campaign campaign;
  private ServerPolicy serverPolicy;
  private final ServerCommand serverCommand;

  private boolean disconnectExpected = false;

  /** Creates a client for a personal server. */
  public MapToolClient() {
    this.campaign = new Campaign();

    try {
      PlayerDatabaseFactory.setCurrentPlayerDatabase(PERSONAL_SERVER);
      playerDatabase = PlayerDatabaseFactory.getCurrentPlayerDatabase();

      String username = AppPreferences.getDefaultUserName();
      player = (LocalPlayer) playerDatabase.getPlayer(username);

      serverPolicy = new ServerPolicy();

      conn = new NilMapToolConnection();
      conn.addDisconnectHandler(conn -> onDisconnect(true, conn));
      serverCommand = new ServerCommandClientImpl(this);
      conn.onCompleted(
          () -> {
            conn.addMessageHandler(new ClientMessageHandler(this));
          });
    } catch (Exception e) {
      throw new RuntimeException("Unable to start personal server", e);
    }
  }

  public MapToolClient(LocalPlayer player, ServerConfig config) {
    this.campaign = new Campaign();

    this.player = player;
    this.serverPolicy = new ServerPolicy();

    PlayerDatabaseFactory.setCurrentPlayerDatabase(LOCAL_PLAYER);
    playerDatabase = PlayerDatabaseFactory.getCurrentPlayerDatabase();

    conn = new MapToolConnection(config, player);
    conn.addDisconnectHandler(conn -> onDisconnect(false, conn));
    serverCommand = new ServerCommandClientImpl(this);
    conn.onCompleted(
        () -> {
          conn.addMessageHandler(new ClientMessageHandler(this));
        });
  }

  public void start() throws IOException {
    conn.start();
  }

  public void close() throws IOException {
    if (conn.isAlive()) {
      conn.close();
    }
  }

  public void expectDisconnection() {
    disconnectExpected = true;
  }

  public ServerCommand getServerCommand() {
    return serverCommand;
  }

  public LocalPlayer getPlayer() {
    return player;
  }

  public PlayerDatabase getPlayerDatabase() {
    return playerDatabase;
  }

  public IMapToolConnection getConnection() {
    return conn;
  }

  public ServerPolicy getServerPolicy() {
    return serverPolicy;
  }

  public Campaign getCampaign() {
    return this.campaign;
  }

  public void setCampaign(Campaign campaign) {
    this.campaign = campaign;
  }

  public void setServerPolicy(ServerPolicy serverPolicy, boolean sendToServer) {
    this.serverPolicy = serverPolicy;
    if (sendToServer) {
      this.serverCommand.setServerPolicy(serverPolicy);
    }
  }

  private void onDisconnect(boolean isLocalServer, Connection connection) {
    if (!isLocalServer || !disconnectExpected) {
      // Update internal state
      MapTool.disconnect();
    }

    if (disconnectExpected) {
      // expected disconnect from someone else's server
      if (!isLocalServer) {
        // hide map so player doesn't get a brief GM view
        MapTool.getFrame().setCurrentZoneRenderer(null);
      }
    } else {
      var errorText = I18N.getText("msg.error.server.disconnected");
      var connectionError = connection.getError();
      var errorMessage = errorText + (connectionError != null ? (": " + connectionError) : "");
      MapTool.showError(errorMessage);

      // hide map so player doesn't get a brief GM view
      MapTool.getFrame().setCurrentZoneRenderer(null);
      MapTool.getFrame().getToolbarPanel().getMapselect().setVisible(true);
      MapTool.getFrame().getAssetPanel().enableAssets();
      new CampaignManager().clearCampaignData();
      MapTool.getFrame().getToolbarPanel().setTokenSelectionGroupEnabled(true);
      try {
        MapTool.startPersonalServer(CampaignFactory.createBasicCampaign());
      } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
        MapTool.showError(I18N.getText("msg.error.server.cantrestart"), e);
      }
    }
  }
}
