package net.rptools.maptool.model;

import net.rptools.maptool.model.listeners.EventListenerConfig;
import net.rptools.maptool.model.listeners.ListenerType;
import net.rptools.maptool.server.proto.CampaignPropertiesDto;
import net.rptools.maptool.server.proto.TokenDto;
import net.rptools.maptool.server.proto.ZoneDto;
// Import other necessary DTOs if they are directly used by the test context
// (though mostly we'll rely on the model objects' toDto/fromDto methods)

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class EventListenerSerializationTest {

    private int listenerIdCounter;

    @BeforeEach
    void setUp() {
        listenerIdCounter = 0;
    }

    private EventListenerConfig createSampleListenerConfig(String namePrefix, ListenerType type) {
        return new EventListenerConfig(
                UUID.randomUUID().toString(), // Unique ID
                namePrefix + "_" + (++listenerIdCounter),
                type,
                type == ListenerType.MACRO_CODE ? "mt.token.onMove" : "mt.token.onDropTarget",
                type == ListenerType.MACRO_CODE ? "sendToChat('moved')" : (type == ListenerType.PREDEFINED_ACTION ? "mt.token.move" : "myAddon:doSomething"),
                true,
                listenerIdCounter
        );
    }

    private boolean compareEventListenerConfigs(EventListenerConfig elc1, EventListenerConfig elc2) {
        if (elc1 == elc2) return true;
        if (elc1 == null || elc2 == null) return false;
        return Objects.equals(elc1.getId(), elc2.getId()) &&
               Objects.equals(elc1.getName(), elc2.getName()) &&
               elc1.getListenerType() == elc2.getListenerType() &&
               Objects.equals(elc1.getTargetEvent(), elc2.getTargetEvent()) &&
               Objects.equals(elc1.getActionReference(), elc2.getActionReference()) &&
               elc1.isEnabled() == elc2.isEnabled() &&
               elc1.getExecutionOrder() == elc2.getExecutionOrder();
    }

    private void assertListenerListsEqual(List<EventListenerConfig> list1, List<EventListenerConfig> list2) {
        assertEquals(list1.size(), list2.size(), "Listener list sizes differ.");
        for (int i = 0; i < list1.size(); i++) {
            assertTrue(compareEventListenerConfigs(list1.get(i), list2.get(i)),
                       "Listener config at index " + i + " differs. Expected: " + list1.get(i) + ", Got: " + list2.get(i));
        }
    }

    @Test
    void testCampaignPropertiesSerialization_NoListeners() {
        CampaignProperties originalProps = new CampaignProperties();
        CampaignPropertiesDto dto = originalProps.toDto();
        CampaignProperties deserializedProps = CampaignProperties.fromDto(dto);

        assertTrue(deserializedProps.getGlobalInitiativeEventListeners().isEmpty());
        assertTrue(deserializedProps.getGlobalMapEventListeners().isEmpty());
        assertTrue(deserializedProps.getTokenTypeMap().isEmpty()); // Or check specific token types if they exist by default
    }

    @Test
    void testCampaignPropertiesSerialization_WithGlobalListeners() {
        CampaignProperties originalProps = new CampaignProperties();
        EventListenerConfig listener1 = createSampleListenerConfig("GlobalInit", ListenerType.MACRO_CODE);
        EventListenerConfig listener2 = createSampleListenerConfig("GlobalMap", ListenerType.PREDEFINED_ACTION);
        originalProps.addGlobalInitiativeEventListener(listener1);
        originalProps.addGlobalMapEventListener(listener2);

        CampaignPropertiesDto dto = originalProps.toDto();
        CampaignProperties deserializedProps = CampaignProperties.fromDto(dto);

        assertListenerListsEqual(originalProps.getGlobalInitiativeEventListeners(), deserializedProps.getGlobalInitiativeEventListeners());
        assertListenerListsEqual(originalProps.getGlobalMapEventListeners(), deserializedProps.getGlobalMapEventListeners());
    }

    @Test
    void testCampaignPropertiesSerialization_WithTokenTypeListeners() {
        CampaignProperties originalProps = new CampaignProperties();
        String tokenTypeName = "TestType";
        EventListenerConfig listener1 = createSampleListenerConfig("TypeDefault", ListenerType.ADD_ON_LISTENER);
        originalProps.addTokenTypeDefaultEventListener(tokenTypeName, listener1);
        // Add a property to ensure TokenTypeDetails is handled
        originalProps.getTokenTypeMap().computeIfAbsent(tokenTypeName, k -> new TokenTypeDetails()).getProperties().add(new TokenProperty("TestProp", "TP"));


        CampaignPropertiesDto dto = originalProps.toDto();
        CampaignProperties deserializedProps = CampaignProperties.fromDto(dto);

        assertNotNull(deserializedProps.getTokenTypeMap().get(tokenTypeName), "Token type details missing for " + tokenTypeName);
        assertListenerListsEqual(
            originalProps.getTokenTypeDefaultEventListeners(tokenTypeName),
            deserializedProps.getTokenTypeDefaultEventListeners(tokenTypeName)
        );
        assertEquals(1, deserializedProps.getTokenPropertyList(tokenTypeName).size(), "Token properties not deserialized correctly for " + tokenTypeName);
    }
    
    @Test
    void testCampaignPropertiesSerialization_Complex() {
        CampaignProperties originalProps = new CampaignProperties();
        originalProps.addGlobalInitiativeEventListener(createSampleListenerConfig("GI1", ListenerType.MACRO_CODE));
        originalProps.addGlobalMapEventListener(createSampleListenerConfig("GM1", ListenerType.PREDEFINED_ACTION));
        
        String typeName1 = "Warrior";
        originalProps.addTokenTypeDefaultEventListener(typeName1, createSampleListenerConfig("W_Default1", ListenerType.MACRO_CODE));
        originalProps.addTokenTypeDefaultEventListener(typeName1, createSampleListenerConfig("W_Default2", ListenerType.ADD_ON_LISTENER));
        originalProps.getTokenTypeMap().computeIfAbsent(typeName1, k -> new TokenTypeDetails()).getProperties().add(new TokenProperty("Strength", "Str"));


        String typeName2 = "Mage";
        originalProps.addTokenTypeDefaultEventListener(typeName2, createSampleListenerConfig("M_Default1", ListenerType.PREDEFINED_ACTION));
        originalProps.getTokenTypeMap().computeIfAbsent(typeName2, k -> new TokenTypeDetails()).getProperties().add(new TokenProperty("Intellect", "Int"));


        CampaignPropertiesDto dto = originalProps.toDto();
        CampaignProperties deserializedProps = CampaignProperties.fromDto(dto);

        assertListenerListsEqual(originalProps.getGlobalInitiativeEventListeners(), deserializedProps.getGlobalInitiativeEventListeners());
        assertListenerListsEqual(originalProps.getGlobalMapEventListeners(), deserializedProps.getGlobalMapEventListeners());
        assertListenerListsEqual(originalProps.getTokenTypeDefaultEventListeners(typeName1), deserializedProps.getTokenTypeDefaultEventListeners(typeName1));
        assertEquals(1, deserializedProps.getTokenPropertyList(typeName1).size());
        assertListenerListsEqual(originalProps.getTokenTypeDefaultEventListeners(typeName2), deserializedProps.getTokenTypeDefaultEventListeners(typeName2));
        assertEquals(1, deserializedProps.getTokenPropertyList(typeName2).size());
    }


    @Test
    void testTokenSerialization_NoListeners() {
        Token originalToken = new Token(); // Assuming default constructor
        originalToken.setName("TestToken");
        originalToken.setOverrideTokenTypeListeners(false);

        TokenDto dto = originalToken.toDto();
        Token deserializedToken = Token.fromDto(dto);

        assertTrue(deserializedToken.getEventListeners().isEmpty());
        assertFalse(deserializedToken.isOverrideTokenTypeListeners());
    }

    @Test
    void testTokenSerialization_WithListeners() {
        Token originalToken = new Token();
        originalToken.setName("ListenerToken");
        EventListenerConfig listener1 = createSampleListenerConfig("TokenSpecific", ListenerType.MACRO_CODE);
        originalToken.addEventListenerConfig(listener1);
        originalToken.setOverrideTokenTypeListeners(true);

        TokenDto dto = originalToken.toDto();
        Token deserializedToken = Token.fromDto(dto);

        assertListenerListsEqual(originalToken.getEventListeners(), deserializedToken.getEventListeners());
        assertTrue(deserializedToken.isOverrideTokenTypeListeners());
    }

    @Test
    void testZoneSerialization_NoListeners() {
        Zone originalZone = new Zone(); // Assuming default constructor
        originalZone.setName("TestZone");
        originalZone.setOverrideGlobalMapListeners(false);
        // Initialize grid for Zone.toDto()
        originalZone.setGrid(new SquareGrid());


        ZoneDto dto = originalZone.toDto();
        Zone deserializedZone = Zone.fromDto(dto);

        assertTrue(deserializedZone.getMapSpecificEventListeners().isEmpty());
        assertFalse(deserializedZone.isOverrideGlobalMapListeners());
    }

    @Test
    void testZoneSerialization_WithListeners() {
        Zone originalZone = new Zone();
        originalZone.setName("ListenerZone");
        EventListenerConfig listener1 = createSampleListenerConfig("ZoneSpecific", ListenerType.PREDEFINED_ACTION);
        originalZone.addMapSpecificEventListener(listener1);
        originalZone.setOverrideGlobalMapListeners(true);
        // Initialize grid for Zone.toDto()
        originalZone.setGrid(new SquareGrid());

        ZoneDto dto = originalZone.toDto();
        Zone deserializedZone = Zone.fromDto(dto);

        assertListenerListsEqual(originalZone.getMapSpecificEventListeners(), deserializedZone.getMapSpecificEventListeners());
        assertTrue(deserializedZone.isOverrideGlobalMapListeners());
    }
}
