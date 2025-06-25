package net.rptools.maptool.client.ui;

import net.rptools.maptool.client.AppConstants;
import net.rptools.maptool.client.AppUtil;
import net.rptools.maptool.client.MapTool;
import net.rptools.maptool.client.swing.DragImageGlassPane;
import net.rptools.maptool.client.swing.ImageChooserDialog;
import net.rptools.maptool.client.tool.Toolbox;
import net.rptools.maptool.client.ui.assetpanel.AssetPanel;
import net.rptools.maptool.client.ui.commandpanel.CommandPanel;
import net.rptools.maptool.client.ui.connections.ClientConnectionPanel;
import net.rptools.maptool.client.ui.htmlframe.HTMLOverlayPanel;
import net.rptools.maptool.client.ui.lookuptable.LookupTablePanel;
import net.rptools.maptool.client.ui.macrobuttons.panels.*;
import net.rptools.maptool.client.ui.tokenpanel.InitiativePanel;
import net.rptools.maptool.client.ui.zone.PointerOverlay;
import net.rptools.maptool.client.ui.zone.ZoneMiniMapPanel;
import net.rptools.maptool.client.ui.zone.ZoneRenderer;
import net.rptools.maptool.model.GUID;
import net.rptools.maptool.model.Token;
import net.rptools.maptool.model.Zone;
import net.rptools.maptool.model.drawing.Pen;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import java.awt.Color;
import java.io.File;
import java.util.List;

// ChatNotificationTimers is now a public static inner class of MapToolSwingFrame.
// import net.rptools.maptool.client.ui.MapToolSwingFrame.ChatNotificationTimers; // Not needed if fully qualified


public interface MapToolFrameIF {

    // Core lifecycle and visibility
    void setVisible(boolean b);
    boolean isVisible();
    void close();
    void closingMaintenance();
    boolean confirmClose();

    // Frame state and properties
    String getStatusMessage();
    void setStatusMessage(final String message);
    void setTitle();
    void setTitleViaRenderer(ZoneRenderer renderer);

    // Access to major components/panels
    AssetPanel getAssetPanel();
    ClientConnectionPanel getConnectionPanel();
    InitiativePanel getInitiativePanel();
    CommandPanel getCommandPanel();
    Toolbox getToolbox();
    ZoneMiniMapPanel getZoneMiniMapPanel();
    PointerOverlay getPointerOverlay();
    HTMLOverlayPanel getOverlayPanel();
    LookupTablePanel getLookupTablePanel();
    CampaignPanel getCampaignPanel();
    GmPanel getGmPanel();
    GlobalPanel getGlobalPanel();
    SelectionPanel getSelectionPanel();
    ImpersonatePanel getImpersonatePanel();

    // Dialogs and Choosers
    ImageChooserDialog getImageChooserDialog();
    JFileChooser getLoadPropsFileChooser();
    JFileChooser getLoadFileChooser();
    JFileChooser getSaveCmpgnFileChooser();
    JFileChooser getSaveCampaignPropsFileChooser();
    JFileChooser getSaveTokenFileChooser();
    JFileChooser getSaveMapFileChooser();
    JFileChooser getSaveFileChooser();
    JFileChooser getSaveMacroFileChooser();
    JFileChooser getSaveMacroSetFileChooser();
    JFileChooser getLoadMacroFileChooser();
    JFileChooser getLoadMacroSetFileChooser();
    JFileChooser getSaveTableFileChooser();
    JFileChooser getLoadTableFileChooser();
    void showTokenPropertiesDialog(Token token, ZoneRenderer zr);
    void showAboutDialog();

    // File Filters
    FileFilter getCmpgnFileFilter();
    FileFilter getMapFileFilter();
    FileFilter getDungeonDraftFilter();

    // Zone and Renderer Management
    List<ZoneRenderer> getZoneRenderers();
    ZoneRenderer getCurrentZoneRenderer();
    void addZoneRenderer(ZoneRenderer renderer);
    void removeZoneRenderer(ZoneRenderer renderer);
    void clearZoneRendererList();
    void setCurrentZoneRenderer(ZoneRenderer renderer);
    ZoneRenderer getZoneRenderer(Zone zone);
    ZoneRenderer getZoneRenderer(GUID zoneGUID);
    ZoneRenderer getZoneRenderer(String zoneName);
    void refresh();

    // Drawing related
    Pen getPen();
    boolean isPaintDrawingMeasurement();
    void setPaintDrawingMeasurement(boolean aPaintDrawingMeasurements);
    void updateDrawTree();

    // Token related
    void updateTokenTree();
    void clearTokenTree();

    // Asset Management
    void addAssetRoot(File rootDir);

    // Progress Indication
    void startIndeterminateAction();
    void endIndeterminateAction();
    void startDeterminateAction(int totalWork);
    void updateDeterminateActionProgress(int additionalWorkCompleted);
    void endDeterminateAction();

    // Fullscreen
    void showFullScreen();
    void showWindowed();
    boolean isFullScreen();

    // UI specific updates / interactions
    void showCommandPanel();
    void hideCommandPanel();
    boolean isCommandPanelVisible();
    DragImageGlassPane getDragImageGlassPane();

    // Chat notifications
    // This will require ChatNotificationTimers to be accessible. It is now MapToolSwingFrame.ChatNotificationTimers
    net.rptools.maptool.client.ui.MapToolSwingFrame.ChatNotificationTimers getChatNotificationTimers();
    Color getChatTypingLabelColor();
    void setChatTypingLabelColor(Color color);

    // Panel Resets
    void resetTokenPanels();
    void resetPanels();

    // KeyStroke updates
    void updateKeyStrokes();

    // Docking related methods (potentially returning null or dummy in headless)
    com.jidesoft.docking.DockingManager getDockingManager();
    com.jidesoft.docking.DockableFrame getFrame(MapToolSwingFrame.MTFrame mtFrame); // MTFrame is Swing specific
}
