package net.rptools.maptool.client.events;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

// Import all event classes and marker interfaces here
// e.g., import net.rptools.maptool.client.events.MapToolEvent;
// import net.rptools.maptool.client.events.PlayerConnected;
// ... and so on for all relevant events and interfaces
// It's critical to import all event types that are tested.
// For example:
// import net.rptools.maptool.client.events.PlayerEvent;
// import net.rptools.maptool.client.events.ZoneEvent;
// import net.rptools.maptool.client.events.SaveStateChangedEvent;
// import net.rptools.maptool.client.events.ZoneContentChangedEvent;
// import net.rptools.maptool.client.events.LabelEvent;
// import net.rptools.maptool.client.events.TokenEvent;
// import net.rptools.maptool.client.events.InitiativeEvent;
// import net.rptools.maptool.client.events.DrawableEvent;
// import net.rptools.maptool.client.events.BlockingLayerEvent;
// import net.rptools.maptool.client.events.ZoneVisibilityEvent;
// import net.rptools.maptool.client.events.CampaignPropertiesEvent;
// import net.rptools.maptool.client.events.GameDataEvent;

// Import all event classes being tested
import net.rptools.maptool.client.events.*;


class EventInterfaceTest {

    @Test
    void testMarkerInterfacesExist() {
        // Test that a few key marker interfaces can be loaded
        assertTrue(MapToolEvent.class.isInterface());
        assertTrue(PlayerEvent.class.isInterface());
        assertTrue(ZoneEvent.class.isInterface());
        assertTrue(SaveStateChangedEvent.class.isInterface());
        assertTrue(ZoneContentChangedEvent.class.isInterface());
        assertTrue(LabelEvent.class.isInterface());
        assertTrue(TokenEvent.class.isInterface());
        assertTrue(InitiativeEvent.class.isInterface());
        assertTrue(DrawableEvent.class.isInterface());
        assertTrue(BlockingLayerEvent.class.isInterface());
        assertTrue(ZoneVisibilityEvent.class.isInterface());
        assertTrue(CampaignPropertiesEvent.class.isInterface());
        assertTrue(GameDataEvent.class.isInterface());
        assertTrue(AddOnEvent.class.isInterface());
        assertTrue(TokenHoverEvent.class.isInterface());
    }

    @Test
    void testLabelEventHierarchy() {
        assertTrue(ZoneContentChangedEvent.class.isAssignableFrom(LabelEvent.class));
        assertTrue(SaveStateChangedEvent.class.isAssignableFrom(LabelEvent.class));
    }

    @Test
    void testTokenEventHierarchy() {
        assertTrue(ZoneContentChangedEvent.class.isAssignableFrom(TokenEvent.class));
        assertTrue(SaveStateChangedEvent.class.isAssignableFrom(TokenEvent.class));
    }

    // Tests for existing events implementing new interfaces
    @Test
    void testPlayerConnectedInterfaces() {
        assertTrue(MapToolEvent.class.isAssignableFrom(PlayerConnected.class));
        assertTrue(PlayerEvent.class.isAssignableFrom(PlayerConnected.class));
    }

    @Test
    void testOverlayVisibilityChangedInterfaces() {
        assertTrue(MapToolEvent.class.isAssignableFrom(OverlayVisibilityChanged.class));
    }

    @Test
    void testZoneActivatedInterfaces() {
        assertTrue(MapToolEvent.class.isAssignableFrom(ZoneActivated.class));
        assertTrue(ZoneEvent.class.isAssignableFrom(ZoneActivated.class));
    }
    
    @Test
    void testPlayerDisconnectedInterfaces() {
        assertTrue(MapToolEvent.class.isAssignableFrom(PlayerDisconnected.class));
        assertTrue(PlayerEvent.class.isAssignableFrom(PlayerDisconnected.class));
    }

    @Test
    void testPlayerStatusChangedInterfaces() {
        assertTrue(MapToolEvent.class.isAssignableFrom(PlayerStatusChanged.class));
        assertTrue(PlayerEvent.class.isAssignableFrom(PlayerStatusChanged.class));
    }

    @Test
    void testPreferencesChangedInterfaces() {
        assertTrue(MapToolEvent.class.isAssignableFrom(PreferencesChanged.class));
    }

    @Test
    void testServerDisconnectedInterfaces() {
        assertTrue(MapToolEvent.class.isAssignableFrom(ServerDisconnected.class));
    }

    @Test
    void testTokenHoverEnterInterfaces() {
        assertTrue(MapToolEvent.class.isAssignableFrom(TokenHoverEnter.class));
        assertTrue(TokenHoverEvent.class.isAssignableFrom(TokenHoverEnter.class));
    }

    @Test
    void testTokenHoverExitInterfaces() {
        assertTrue(MapToolEvent.class.isAssignableFrom(TokenHoverExit.class));
        assertTrue(TokenHoverEvent.class.isAssignableFrom(TokenHoverExit.class));
    }

    @Test
    void testZoneDeactivatedInterfaces() {
        assertTrue(MapToolEvent.class.isAssignableFrom(ZoneDeactivated.class));
        assertTrue(ZoneEvent.class.isAssignableFrom(ZoneDeactivated.class));
    }

    @Test
    void testZoneLoadingInterfaces() {
        assertTrue(MapToolEvent.class.isAssignableFrom(ZoneLoading.class));
        assertTrue(ZoneEvent.class.isAssignableFrom(ZoneLoading.class));
    }

    @Test
    void testZoneLoadedInterfaces() {
        assertTrue(MapToolEvent.class.isAssignableFrom(ZoneLoaded.class));
        assertTrue(ZoneEvent.class.isAssignableFrom(ZoneLoaded.class));
    }

    @Test
    void testChatMessageAddedInterfaces() {
        assertTrue(MapToolEvent.class.isAssignableFrom(ChatMessageAdded.class));
    }

    // Tests for new events
    @Test
    void testZoneLandingChangedInterfaces() {
        assertTrue(MapToolEvent.class.isAssignableFrom(ZoneLandingChanged.class));
        assertTrue(ZoneEvent.class.isAssignableFrom(ZoneLandingChanged.class));
        assertTrue(SaveStateChangedEvent.class.isAssignableFrom(ZoneLandingChanged.class));
    }

    @Test
    void testThemeLoadedInterfaces() {
        assertTrue(MapToolEvent.class.isAssignableFrom(ThemeLoaded.class));
    }

    @Test
    void testInitiativeListChangedInterfaces() {
        assertTrue(MapToolEvent.class.isAssignableFrom(InitiativeListChanged.class));
        assertTrue(InitiativeEvent.class.isAssignableFrom(InitiativeListChanged.class));
        assertTrue(SaveStateChangedEvent.class.isAssignableFrom(InitiativeListChanged.class));
    }
    
    @Test
    void testDrawableAddedInterfaces() {
        assertTrue(MapToolEvent.class.isAssignableFrom(DrawableAdded.class));
        assertTrue(DrawableEvent.class.isAssignableFrom(DrawableAdded.class));
        assertTrue(ZoneContentChangedEvent.class.isAssignableFrom(DrawableAdded.class));
        assertTrue(SaveStateChangedEvent.class.isAssignableFrom(DrawableAdded.class));
    }

    @Test
    void testLabelAddedInterfaces() {
        assertTrue(MapToolEvent.class.isAssignableFrom(LabelAdded.class));
        assertTrue(LabelEvent.class.isAssignableFrom(LabelAdded.class));
    }

    @Test
    void testTokensAddedInterfaces() {
        assertTrue(MapToolEvent.class.isAssignableFrom(TokensAdded.class));
        assertTrue(TokenEvent.class.isAssignableFrom(TokensAdded.class));
    }

    @Test
    void testMaskTopologyChangedInterfaces() {
        assertTrue(MapToolEvent.class.isAssignableFrom(MaskTopologyChanged.class));
        assertTrue(BlockingLayerEvent.class.isAssignableFrom(MaskTopologyChanged.class));
        assertTrue(ZoneContentChangedEvent.class.isAssignableFrom(MaskTopologyChanged.class));
        assertTrue(ZoneVisibilityEvent.class.isAssignableFrom(MaskTopologyChanged.class));
        assertTrue(SaveStateChangedEvent.class.isAssignableFrom(MaskTopologyChanged.class));
    }
    
    @Test
    void testSightChangedInterfaces() {
        assertTrue(MapToolEvent.class.isAssignableFrom(SightChanged.class));
        assertTrue(CampaignPropertiesEvent.class.isAssignableFrom(SightChanged.class));
        assertTrue(SaveStateChangedEvent.class.isAssignableFrom(SightChanged.class));
    }

    @Test
    void testGameDataUpdatedInterfaces() {
        assertTrue(MapToolEvent.class.isAssignableFrom(GameDataUpdated.class));
        assertTrue(GameDataEvent.class.isAssignableFrom(GameDataUpdated.class));
        assertTrue(SaveStateChangedEvent.class.isAssignableFrom(GameDataUpdated.class));
    }
    
    // Expanding with more new events
    @Test
    void testZoneRenamedInterfaces() {
        assertTrue(MapToolEvent.class.isAssignableFrom(ZoneRenamed.class));
        assertTrue(ZoneEvent.class.isAssignableFrom(ZoneRenamed.class));
        assertTrue(SaveStateChangedEvent.class.isAssignableFrom(ZoneRenamed.class));
    }

    @Test
    void testTokenPanelChangedInterfaces() {
        assertTrue(MapToolEvent.class.isAssignableFrom(TokenPanelChanged.class));
    }

    @Test
    void testInitiativeChangedInterfaces() {
        assertTrue(MapToolEvent.class.isAssignableFrom(InitiativeChanged.class));
        assertTrue(InitiativeEvent.class.isAssignableFrom(InitiativeChanged.class));
        assertTrue(SaveStateChangedEvent.class.isAssignableFrom(InitiativeChanged.class));
    }

    @Test
    void testTokenInitiativeChangedInterfaces() {
        assertTrue(MapToolEvent.class.isAssignableFrom(TokenInitiativeChanged.class));
        assertTrue(InitiativeEvent.class.isAssignableFrom(TokenInitiativeChanged.class));
        assertTrue(SaveStateChangedEvent.class.isAssignableFrom(TokenInitiativeChanged.class));
    }

    @Test
    void testBoardChangedInterfaces() {
        assertTrue(MapToolEvent.class.isAssignableFrom(BoardChanged.class));
        assertTrue(ZoneContentChangedEvent.class.isAssignableFrom(BoardChanged.class));
    }

    @Test
    void testGridChangedInterfaces() {
        assertTrue(MapToolEvent.class.isAssignableFrom(GridChanged.class));
        assertTrue(ZoneContentChangedEvent.class.isAssignableFrom(GridChanged.class));
        assertTrue(SaveStateChangedEvent.class.isAssignableFrom(GridChanged.class));
    }

    @Test
    void testFogChangedInterfaces() {
        assertTrue(MapToolEvent.class.isAssignableFrom(FogChanged.class));
        assertTrue(ZoneContentChangedEvent.class.isAssignableFrom(FogChanged.class));
        assertTrue(SaveStateChangedEvent.class.isAssignableFrom(FogChanged.class));
    }

    @Test
    void testDrawableRemovedInterfaces() {
        assertTrue(MapToolEvent.class.isAssignableFrom(DrawableRemoved.class));
        assertTrue(DrawableEvent.class.isAssignableFrom(DrawableRemoved.class));
        assertTrue(ZoneContentChangedEvent.class.isAssignableFrom(DrawableRemoved.class));
        assertTrue(SaveStateChangedEvent.class.isAssignableFrom(DrawableRemoved.class));
    }

    @Test
    void testLabelRemovedInterfaces() {
        assertTrue(MapToolEvent.class.isAssignableFrom(LabelRemoved.class));
        assertTrue(LabelEvent.class.isAssignableFrom(LabelRemoved.class));
    }

    @Test
    void testLabelChangedInterfaces() {
        assertTrue(MapToolEvent.class.isAssignableFrom(LabelChanged.class));
        assertTrue(LabelEvent.class.isAssignableFrom(LabelChanged.class));
    }

    @Test
    void testWallTopologyChangedInterfaces() {
        assertTrue(MapToolEvent.class.isAssignableFrom(WallTopologyChanged.class));
        assertTrue(BlockingLayerEvent.class.isAssignableFrom(WallTopologyChanged.class));
        assertTrue(ZoneContentChangedEvent.class.isAssignableFrom(WallTopologyChanged.class));
        assertTrue(ZoneVisibilityEvent.class.isAssignableFrom(WallTopologyChanged.class));
        assertTrue(SaveStateChangedEvent.class.isAssignableFrom(WallTopologyChanged.class));
    }

    @Test
    void testZoneLightingChangedInterfaces() {
        assertTrue(MapToolEvent.class.isAssignableFrom(ZoneLightingChanged.class));
        assertTrue(ZoneContentChangedEvent.class.isAssignableFrom(ZoneLightingChanged.class));
        assertTrue(SaveStateChangedEvent.class.isAssignableFrom(ZoneLightingChanged.class));
        assertTrue(ZoneVisibilityEvent.class.isAssignableFrom(ZoneLightingChanged.class));
    }

    @Test
    void testZoneVisionTypeChangedInterfaces() {
        assertTrue(MapToolEvent.class.isAssignableFrom(ZoneVisionTypeChanged.class));
        assertTrue(ZoneContentChangedEvent.class.isAssignableFrom(ZoneVisionTypeChanged.class));
        assertTrue(ZoneVisibilityEvent.class.isAssignableFrom(ZoneVisionTypeChanged.class));
        assertTrue(SaveStateChangedEvent.class.isAssignableFrom(ZoneVisionTypeChanged.class));
    }

    @Test
    void testTokensRemovedInterfaces() {
        assertTrue(MapToolEvent.class.isAssignableFrom(TokensRemoved.class));
        assertTrue(TokenEvent.class.isAssignableFrom(TokensRemoved.class));
    }

    @Test
    void testTokensChangedInterfaces() {
        assertTrue(MapToolEvent.class.isAssignableFrom(TokensChanged.class));
        assertTrue(TokenEvent.class.isAssignableFrom(TokensChanged.class));
    }

    @Test
    void testTokenMacroChangedInterfaces() {
        assertTrue(MapToolEvent.class.isAssignableFrom(TokenMacroChanged.class));
        assertTrue(TokenEvent.class.isAssignableFrom(TokenMacroChanged.class));
    }

    @Test
    void testTokenEditedInterfaces() {
        assertTrue(MapToolEvent.class.isAssignableFrom(TokenEdited.class));
        assertTrue(TokenEvent.class.isAssignableFrom(TokenEdited.class));
    }

    @Test
    void testSelectionChangedInterfaces() {
        assertTrue(MapToolEvent.class.isAssignableFrom(SelectionChanged.class));
        assertTrue(ZoneContentChangedEvent.class.isAssignableFrom(SelectionChanged.class));
    }

    @Test
    void testAddOnAddedInterfaces() {
        assertTrue(MapToolEvent.class.isAssignableFrom(AddOnAdded.class));
        assertTrue(AddOnEvent.class.isAssignableFrom(AddOnAdded.class));
        assertTrue(SaveStateChangedEvent.class.isAssignableFrom(AddOnAdded.class));
    }

    @Test
    void testAddOnRemovedInterfaces() {
        assertTrue(MapToolEvent.class.isAssignableFrom(AddOnRemoved.class));
        assertTrue(AddOnEvent.class.isAssignableFrom(AddOnRemoved.class));
        assertTrue(SaveStateChangedEvent.class.isAssignableFrom(AddOnRemoved.class));
    }

    @Test
    void testRepositoriesChangedInterfaces() {
        assertTrue(MapToolEvent.class.isAssignableFrom(RepositoriesChanged.class));
        assertTrue(SaveStateChangedEvent.class.isAssignableFrom(RepositoriesChanged.class));
    }

    @Test
    void testBarsChangedInterfaces() {
        assertTrue(MapToolEvent.class.isAssignableFrom(BarsChanged.class));
        assertTrue(CampaignPropertiesEvent.class.isAssignableFrom(BarsChanged.class));
        assertTrue(SaveStateChangedEvent.class.isAssignableFrom(BarsChanged.class));
    }

    @Test
    void testStatesChangedInterfaces() {
        assertTrue(MapToolEvent.class.isAssignableFrom(StatesChanged.class));
        assertTrue(CampaignPropertiesEvent.class.isAssignableFrom(StatesChanged.class));
        assertTrue(SaveStateChangedEvent.class.isAssignableFrom(StatesChanged.class));
    }

    @Test
    void testGameDataNamespaceAddedInterfaces() {
        assertTrue(MapToolEvent.class.isAssignableFrom(GameDataNamespaceAdded.class));
        assertTrue(GameDataEvent.class.isAssignableFrom(GameDataNamespaceAdded.class));
        assertTrue(SaveStateChangedEvent.class.isAssignableFrom(GameDataNamespaceAdded.class));
    }

    @Test
    void testGameDataNamespaceRemovedInterfaces() {
        assertTrue(MapToolEvent.class.isAssignableFrom(GameDataNamespaceRemoved.class));
        assertTrue(GameDataEvent.class.isAssignableFrom(GameDataNamespaceRemoved.class));
        assertTrue(SaveStateChangedEvent.class.isAssignableFrom(GameDataNamespaceRemoved.class));
    }

    @Test
    void testGameDataAddedInterfaces() {
        assertTrue(MapToolEvent.class.isAssignableFrom(GameDataAdded.class));
        assertTrue(GameDataEvent.class.isAssignableFrom(GameDataAdded.class));
        assertTrue(SaveStateChangedEvent.class.isAssignableFrom(GameDataAdded.class));
    }

    @Test
    void testGameDataRemovedInterfaces() {
        assertTrue(MapToolEvent.class.isAssignableFrom(GameDataRemoved.class));
        assertTrue(GameDataEvent.class.isAssignableFrom(GameDataRemoved.class));
        assertTrue(SaveStateChangedEvent.class.isAssignableFrom(GameDataRemoved.class));
    }
}
