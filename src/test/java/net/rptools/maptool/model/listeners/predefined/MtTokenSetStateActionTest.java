package net.rptools.maptool.model.listeners.predefined;

import net.rptools.maptool.client.MapTool;
import net.rptools.maptool.client.MapToolClient;
import net.rptools.maptool.model.Campaign;
import net.rptools.maptool.model.CampaignProperties;
import net.rptools.maptool.model.GUID;
import net.rptools.maptool.model.Token;
import net.rptools.maptool.model.Zone;
import net.rptools.maptool.server.ServerCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MtTokenSetStateActionTest {

    @Mock
    private Campaign campaign;
    @Mock
    private CampaignProperties campaignProperties;
    @Mock
    private Zone zone;
    @Mock
    private Token token;
    @Mock
    private ServerCommand serverCommand;
    @Mock
    private MapToolClient mapToolClient;


    @InjectMocks
    private MtTokenSetStateAction action;

    private Map<String, Object> eventContext;
    private GUID zoneId;
    private GUID tokenId;

    @BeforeEach
    void setUp() {
        eventContext = new HashMap<>();
        zoneId = new GUID();
        tokenId = new GUID();

        // Default mocks for happy paths
        lenient().when(campaign.getZone(zoneId)).thenReturn(zone);
        lenient().when(zone.getToken(tokenId)).thenReturn(token);
        lenient().when(zone.getId()).thenReturn(zoneId);
        lenient().when(token.getId()).thenReturn(tokenId);
        lenient().when(token.getZoneId()).thenReturn(zoneId);
        lenient().when(token.getName()).thenReturn("TestToken");
        lenient().when(campaign.getCampaignProperties()).thenReturn(campaignProperties);
        lenient().when(campaignProperties.getTokenStatesMap()).thenReturn(new HashMap<>()); // Assume no predefined boolean states unless specified by test
        lenient().when(campaignProperties.getTokenBarsMap()).thenReturn(new HashMap<>()); // Assume no predefined bars unless specified by test
    }

    private void setupMockMapTool() {
        // Need to mock static MapTool.getCampaign(), MapTool.serverCommand(), etc.
        // This requires Mockito's inline mock maker or equivalent setup if not already default.
        // For simplicity, assuming this works. If not, tests might need to be structured differently
        // or use PowerMockito, or a test-specific MapTool instance/accessor.
    }

    @Test
    void getName_shouldReturnCorrectActionName() {
        assertEquals(MtTokenSetStateAction.ACTION_NAME, action.getName());
    }

    // Test successful execution for various scenarios
    @Test
    void execute_setBooleanStateTrue_shorthand() throws ActionExecutionException {
        try (MockedStatic<MapTool> mockedMapTool = Mockito.mockStatic(MapTool.class)) {
            mockedMapTool.when(MapTool::getCampaign).thenReturn(campaign);
            mockedMapTool.when(MapTool::serverCommand).thenReturn(serverCommand);
            // For tokenZone.tokenChanged(token)
            lenient().when(MapTool.getFrame()).thenReturn(null); // Avoids NPE if getFrame().getZoneRenderer() is called indirectly

            eventContext.put("token", token);
            String actionParams = "Prone";

            assertTrue(action.execute(eventContext, actionParams));
            verify(token).setState("Prone", Boolean.TRUE);
            verify(serverCommand).putToken(zoneId, token);
            verify(zone).tokenChanged(token);
        }
    }

    @Test
    void execute_setBooleanStateTrue_explicit() throws ActionExecutionException {
         try (MockedStatic<MapTool> mockedMapTool = Mockito.mockStatic(MapTool.class)) {
            mockedMapTool.when(MapTool::getCampaign).thenReturn(campaign);
            mockedMapTool.when(MapTool::serverCommand).thenReturn(serverCommand);
            lenient().when(MapTool.getFrame()).thenReturn(null);


            eventContext.put("token", token);
            String actionParams = "Hidden=true";

            assertTrue(action.execute(eventContext, actionParams));
            verify(token).setState("Hidden", Boolean.TRUE);
            verify(serverCommand).putToken(zoneId, token);
        }
    }

    @Test
    void execute_setBooleanStateFalse() throws ActionExecutionException {
        try (MockedStatic<MapTool> mockedMapTool = Mockito.mockStatic(MapTool.class)) {
            mockedMapTool.when(MapTool::getCampaign).thenReturn(campaign);
            mockedMapTool.when(MapTool::serverCommand).thenReturn(serverCommand);
            lenient().when(MapTool.getFrame()).thenReturn(null);

            eventContext.put("token", token);
            String actionParams = "Hidden=false";

            assertTrue(action.execute(eventContext, actionParams));
            verify(token).setState("Hidden", Boolean.FALSE);
            verify(serverCommand).putToken(zoneId, token);
        }
    }
    
    @Test
    void execute_setBarValue() throws ActionExecutionException {
        try (MockedStatic<MapTool> mockedMapTool = Mockito.mockStatic(MapTool.class)) {
            mockedMapTool.when(MapTool::getCampaign).thenReturn(campaign);
            mockedMapTool.when(MapTool::serverCommand).thenReturn(serverCommand);
            lenient().when(MapTool.getFrame()).thenReturn(null);

            eventContext.put("token", token);
            String actionParams = "HP=25";

            assertTrue(action.execute(eventContext, actionParams));
            verify(token).setState("HP", new BigDecimal("25"));
            verify(serverCommand).putToken(zoneId, token);
        }
    }

    @Test
    void execute_clearStateWithNull() throws ActionExecutionException {
         try (MockedStatic<MapTool> mockedMapTool = Mockito.mockStatic(MapTool.class)) {
            mockedMapTool.when(MapTool::getCampaign).thenReturn(campaign);
            mockedMapTool.when(MapTool::serverCommand).thenReturn(serverCommand);
            lenient().when(MapTool.getFrame()).thenReturn(null);

            eventContext.put("token", token);
            String actionParams = "SomeState=null";

            assertTrue(action.execute(eventContext, actionParams));
            verify(token).setState("SomeState", null);
            verify(serverCommand).putToken(zoneId, token);
        }
    }

    @Test
    void execute_multipleTokensInList() throws ActionExecutionException {
        try (MockedStatic<MapTool> mockedMapTool = Mockito.mockStatic(MapTool.class)) {
            mockedMapTool.when(MapTool::getCampaign).thenReturn(campaign);
            mockedMapTool.when(MapTool::serverCommand).thenReturn(serverCommand);
            lenient().when(MapTool.getFrame()).thenReturn(null);

            Token token2 = mock(Token.class);
            GUID tokenId2 = new GUID();
            when(token2.getId()).thenReturn(tokenId2);
            when(token2.getZoneId()).thenReturn(zoneId);
            when(token2.getName()).thenReturn("Token2");

            List<Object> tokens = new ArrayList<>();
            tokens.add(token);
            tokens.add(token2);
            eventContext.put("tokens", tokens);
            String actionParams = "Engaged";

            assertTrue(action.execute(eventContext, actionParams));
            verify(token).setState("Engaged", Boolean.TRUE);
            verify(serverCommand).putToken(zoneId, token);
            verify(token2).setState("Engaged", Boolean.TRUE);
            verify(serverCommand).putToken(zoneId, token2);
        }
    }
    
    @Test
    void execute_tokenAsGuidInList_foundInContextZone() throws ActionExecutionException {
        try (MockedStatic<MapTool> mockedMapTool = Mockito.mockStatic(MapTool.class)) {
            mockedMapTool.when(MapTool::getCampaign).thenReturn(campaign);
            mockedMapTool.when(MapTool::serverCommand).thenReturn(serverCommand);
            lenient().when(MapTool.getFrame()).thenReturn(null);

            List<Object> tokens = new ArrayList<>();
            tokens.add(tokenId);
            eventContext.put("tokens", tokens);
            eventContext.put("zoneId", zoneId); // Provide zone context
            String actionParams = "Marked";

            assertTrue(action.execute(eventContext, actionParams));
            verify(token).setState("Marked", Boolean.TRUE);
            verify(serverCommand).putToken(zoneId, token);
        }
    }


    // Test error conditions
    @Test
    void execute_emptyActionParameters_throwsException() {
        eventContext.put("token", token);
        ActionExecutionException exception = assertThrows(ActionExecutionException.class, () -> {
            action.execute(eventContext, "");
        });
        assertTrue(exception.getMessage().contains("cannot be empty"));
    }

    @Test
    void execute_malformedActionParameters_noStateName_throwsException() {
        eventContext.put("token", token);
        ActionExecutionException exception = assertThrows(ActionExecutionException.class, () -> {
            action.execute(eventContext, "=true");
        });
        assertTrue(exception.getMessage().contains("State name cannot be empty"));
    }
    
    @Test
    void execute_malformedActionParameters_invalidValue() {
        eventContext.put("token", token);
        ActionExecutionException exception = assertThrows(ActionExecutionException.class, () -> {
            action.execute(eventContext, "HP=NotANumberOrBoolean");
        });
        assertTrue(exception.getMessage().contains("Invalid state value"));
    }

    @Test
    void execute_noTokenInContext_throwsException() {
        String actionParams = "Prone";
        ActionExecutionException exception = assertThrows(ActionExecutionException.class, () -> {
            action.execute(eventContext, actionParams); // No "token" or "tokens" in context
        });
        assertTrue(exception.getMessage().contains("No 'tokens' or 'token' found"));
    }

    @Test
    void execute_tokenGuidNotFound_logsWarningAndSucceeds() throws ActionExecutionException {
         try (MockedStatic<MapTool> mockedMapTool = Mockito.mockStatic(MapTool.class)) {
            mockedMapTool.when(MapTool::getCampaign).thenReturn(campaign);
            // Ensure findTokenInAnyZone returns null for this specific GUID
            when(campaign.getZone(any(GUID.class))).thenReturn(null); // General case for zones
            when(campaign.getZones()).thenReturn(List.of(zone)); // Make sure it iterates zones
            when(zone.getToken(any(GUID.class))).thenReturn(null); // Specific token not found in iterated zone

            GUID nonExistentTokenId = new GUID();
            eventContext.put("token", nonExistentTokenId);
            String actionParams = "Prone";

            // Should not throw, but log a warning (verification of log is hard with Mockito static unless more setup)
            assertTrue(action.execute(eventContext, actionParams));
            verify(token, never()).setState(anyString(), any()); // Original token mock should not be called
        }
    }
    
    @Test
    void execute_tokenZoneNotFound_logsErrorAndReturnsFalseForThatToken() throws ActionExecutionException {
        try (MockedStatic<MapTool> mockedMapTool = Mockito.mockStatic(MapTool.class)) {
            mockedMapTool.when(MapTool::getCampaign).thenReturn(campaign);
            mockedMapTool.when(MapTool::serverCommand).thenReturn(serverCommand); // Needed if one token succeeds
            lenient().when(MapTool.getFrame()).thenReturn(null);

            Token token1 = mock(Token.class);
            GUID tokenId1 = new GUID();
            GUID zoneId1 = new GUID();
            when(token1.getId()).thenReturn(tokenId1);
            when(token1.getZoneId()).thenReturn(zoneId1); // This zone won't be found by campaign.getZone(zoneId1)
            when(token1.getName()).thenReturn("TokenInNullZone");

            when(campaign.getZone(zoneId1)).thenReturn(null); // Simulate zone not found for token1

            Token token2 = token; // Our standard mock token that is in a valid zone
            
            List<Object> tokens = List.of(token1, token2);
            eventContext.put("tokens", tokens);
            String actionParams = "MyState=true";

            assertFalse(action.execute(eventContext, actionParams)); // Should be false as one token failed

            verify(token1, never()).setState(anyString(), any()); // token1 should fail due to no zone
            verify(token2).setState("MyState", Boolean.TRUE);    // token2 should succeed
            verify(serverCommand).putToken(zoneId, token2);      // serverCommand only for successful token
        }
    }
}
