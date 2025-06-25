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
package net.rptools.maptool.client.ui.macrobuttons.panels;

import com.google.common.eventbus.Subscribe;
import com.jidesoft.docking.DockableFrame;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.List;
import javax.swing.*;
import net.rptools.maptool.client.AppUtil;
import net.rptools.maptool.client.MapTool;
import net.rptools.maptool.client.ui.MapToolFrameIF;
import net.rptools.maptool.client.ui.MapToolSwingFrame; // Added for MTFrame and casting
import net.rptools.maptool.client.ui.MapToolSwingFrame.MTFrame; // Changed to MapToolSwingFrame.MTFrame
import net.rptools.maptool.client.ui.theme.Icons;
import net.rptools.maptool.client.ui.theme.RessourceManager;
import net.rptools.maptool.client.ui.zone.SelectionModel;
import net.rptools.maptool.events.MapToolEventBus;
import net.rptools.maptool.language.I18N;
import net.rptools.maptool.model.GUID;
import net.rptools.maptool.model.MacroButtonProperties;
import net.rptools.maptool.model.Token;
import net.rptools.maptool.model.tokens.TokenMacroChanged;
import net.rptools.maptool.model.tokens.TokenPanelChanged;
import net.rptools.maptool.model.zones.TokenEdited;
import net.rptools.maptool.model.zones.TokensRemoved;

public class ImpersonatePanel extends AbstractMacroPanel {
  private boolean currentlyImpersonating = false;

  public ImpersonatePanel() {
    setPanelClass("ImpersonatePanel");
    new MapToolEventBus().getMainEventBus().register(this);
  }

  public void init() {
    MapToolFrameIF frameIF = MapTool.getFrame();
    if (frameIF == null) {
        clear();
        return;
    }

    DockingManager dm = frameIF.getDockingManager(); // Call on interface
    DockableFrame impersonateDockableFrame = null;
    if (dm != null) { // Check if we have a docking manager (i.e., not headless)
        impersonateDockableFrame = dm.getFrame(MapToolSwingFrame.MTFrame.IMPERSONATED.name());
    }

    boolean panelVisible = false;
    if (impersonateDockableFrame != null) {
        panelVisible = (impersonateDockableFrame.isVisible() && !impersonateDockableFrame.isAutohide()) || impersonateDockableFrame.isAutohideShowing();
    }

    if (!panelVisible || frameIF.getCurrentZoneRenderer() == null) {
        if (panelVisible) clear();
        return;
    }

    List<Token> selectedTokenList = frameIF.getCurrentZoneRenderer().getSelectedTokensList();
    if (currentlyImpersonating && getToken() != null) {
        Token token = getToken();
        DockableFrame impFrame = frameIF.getFrame(MapToolSwingFrame.MTFrame.IMPERSONATED); // Call on IF
        if (impFrame != null) { // Check for headless
            impFrame.setFrameIcon(token.getIcon(16, 16));
        }
        if (frameIF instanceof MapToolSwingFrame) { // Still need cast for this specific setFrameTitle
            ((MapToolSwingFrame) frameIF).setFrameTitle(MapToolSwingFrame.MTFrame.IMPERSONATED, getTitle(token));
        }
        addArea(getTokenId());
    } else if (selectedTokenList.size() == 1) {
        final Token t = selectedTokenList.get(0);

        if (AppUtil.playerOwns(t)) {
          JButton button =
              new JButton(
                  I18N.getText("panel.Impersonate.button.impersonateSelected"), t.getIcon(16, 16)) {
                private static final long serialVersionUID = 1L;

                @Override
                public Insets getInsets() {
                  return new Insets(2, 2, 2, 2);
                }
              };
          button.addMouseListener(
              new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent event) {
                  MapTool.getFrame().getCommandPanel().commitCommand("/im " + t.getId());
                }
              });
          button.setBackground(null);
          add(button);
        }
      }
    }
  }

  public void startImpersonating(Token token) {
    stopImpersonating();
    setTokenId(token);
    currentlyImpersonating = true;
    token.setBeingImpersonated(true);
    reset();
  }

  public void stopImpersonating() {
    Token token = getToken();
    if (token != null) {
      token.setBeingImpersonated(false);
    }
    setTokenId((GUID) null);
    currentlyImpersonating = false;
    clear();
  }

  public String getTitle(Token token) {
    if (token.getGMName() != null && token.getGMName().trim().length() > 0) {
      return token.getName() + " (" + token.getGMName() + ")";
    } else {
      return token.getName();
    }
  }

  @Override
  public void clear() {
    removeAll();
    MapToolFrameIF frameIF = MapTool.getFrame();
    if (frameIF != null) {
        DockableFrame impersonateDockableFrame = frameIF.getFrame(MapToolSwingFrame.MTFrame.IMPERSONATED); // Call on IF
        if (impersonateDockableFrame != null) { // Check for headless
             impersonateDockableFrame.setFrameIcon(RessourceManager.getSmallIcon(Icons.WINDOW_IMPERSONATED_MACROS));
        }
        if (frameIF instanceof MapToolSwingFrame) { // Still need cast for this specific setFrameTitle
            ((MapToolSwingFrame) frameIF).setFrameTitle(MapToolSwingFrame.MTFrame.IMPERSONATED, I18N.getString(MapToolSwingFrame.MTFrame.IMPERSONATED.getPropertyName()));
        }
    }
    if (getTokenId() == null) {
      currentlyImpersonating = false;
    }
    doLayout();
    revalidate();
    repaint();
  }

  @Override
  public void reset() {
    clear();
    init();
  }

  /** Resets the panel only if no token is impersonated. */
  public void resetIfNotImpersonating() {
    if (!currentlyImpersonating || getToken() == null) {
      reset();
    }
  }

  @Subscribe
  private void onSelectionChanged(SelectionModel.SelectionChanged event) {
    SwingUtilities.invokeLater(
        () -> {
          reset();
        });
  }

  @Subscribe
  private void onTokenMacroChanged(TokenMacroChanged event) {
    SwingUtilities.invokeLater(
        () -> {
          resetIfAnyImpersonated(Collections.singletonList(event.token()));
        });
  }

  @Subscribe
  private void onTokenPanelChanged(TokenPanelChanged event) {
    SwingUtilities.invokeLater(
        () -> {
          resetIfAnyImpersonated(Collections.singletonList(event.token()));
        });
  }

  @Subscribe
  private void onTokensRemoved(TokensRemoved event) {
    SwingUtilities.invokeLater(
        () -> {
          resetIfAnyImpersonated(event.tokens());
        });
  }

  @Subscribe
  private void onTokensEdited(TokenEdited event) {
    SwingUtilities.invokeLater(
        () -> {
          resetIfAnyImpersonated(Collections.singletonList(event.token()));
        });
  }

  private void resetIfAnyImpersonated(List<Token> tokens) {
    // Only resets if the impersonated token is among those changed/deleted
    if (isImpersonatedAmongList(tokens)) {
      reset();
    }
  }

  private boolean isTokenImpersonated(Token token) {
    return token != null && getTokenId() != null && token.getId().equals(getTokenId());
  }

  private boolean isImpersonatedAmongList(List<Token> list) {
    for (Token token : list) {
      if (isTokenImpersonated(token)) {
        return true;
      }
    }
    return false;
  }

  @Override
  protected List<MacroButtonProperties> getMacroButtonProperties() {
    /* this is not going to be called for this panel */
    return null;
  }

  /**
   * This method is currently not used and (likely) hasn't been kept up to date with the rest of the
   * code. I've marked it as deprecated to reflect this and to warn anyone who calls it.
   */
  @Deprecated
  public void addCancelButton() {
    JButton button =
        new JButton("Cancel Impersonation", RessourceManager.getSmallIcon(Icons.ACTION_CANCEL)) {
          @Override
          public Insets getInsets() {
            return new Insets(3, 3, 3, 3);
          }
        };
    button.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent event) {
            MapTool.getFrame().getCommandPanel().commitCommand("/im");
          }
        });
    button.setBackground(null);
    add(button);
  }
}
