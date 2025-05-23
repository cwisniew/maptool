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
package net.rptools.maptool.model.listeners.predefined;

import net.rptools.maptool.client.MapTool; // Corrected import
import net.rptools.maptool.model.Campaign;
import net.rptools.maptool.model.CampaignProperties;
import net.rptools.maptool.model.GUID;
import net.rptools.maptool.model.Token;
import net.rptools.maptool.model.Zone;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MtTokenSetStateAction implements PredefinedAction {

    private static final Logger LOGGER = LogManager.getLogger(MtTokenSetStateAction.class);
    public static final String ACTION_NAME = "mt.token.setState";

    public MtTokenSetStateAction() {
        // Constructor is empty.
    }

    @Override
    public String getName() {
        return ACTION_NAME;
    }

    @Override
    public boolean execute(Map<String, Object> eventContext, String actionParameters) throws ActionExecutionException {
        // 1. Validate and Parse actionParameters
        if (actionParameters == null || actionParameters.trim().isEmpty()) {
            throw new ActionExecutionException("Action parameters (e.g., 'StateName=Value' or 'StateName') cannot be empty for " + ACTION_NAME);
        }

        String stateName;
        Object stateValue;

        String[] parts = actionParameters.split("=", 2);
        stateName = parts[0].trim();
        if (stateName.isEmpty()) {
            throw new ActionExecutionException("State name cannot be empty in actionParameters: '" + actionParameters + "'");
        }

        // Validate stateName against campaign properties
        Campaign campaign = MapTool.getCampaign();
        CampaignProperties campaignProps = campaign.getCampaignProperties();
        boolean isKnownState = campaignProps.getTokenStatesMap().containsKey(stateName);
        boolean isKnownBar = campaignProps.getTokenBarsMap().containsKey(stateName);

        if (!isKnownState && !isKnownBar) {
            // If not a globally defined state/bar, it might be a custom/dynamic one.
            // Depending on desired strictness, either throw an exception or allow it.
            // For now, let's allow it, as Token.setState() can handle arbitrary properties.
            // If strictness is desired, uncomment the following:
            // throw new ActionExecutionException("State or bar name '" + stateName + "' is not a defined boolean state or bar in campaign properties.");
            LOGGER.debug("State name '" + stateName + "' is not a globally defined boolean state or bar. Proceeding to set it as a general token state/property.");
        }


        if (parts.length == 2) {
            String valueStr = parts[1].trim();
            if (valueStr.equalsIgnoreCase("true")) {
                stateValue = Boolean.TRUE;
            } else if (valueStr.equalsIgnoreCase("false")) {
                stateValue = Boolean.FALSE;
            } else if (valueStr.equalsIgnoreCase("null")) {
                stateValue = null; // To clear the state or set to default via Token.setState(name, null)
            } else {
                try {
                    stateValue = new BigDecimal(valueStr);
                } catch (NumberFormatException e) {
                    // If not boolean, not null, and not a number, it could be a string value for a property
                    // However, this action is primarily for states/bars.
                    // For now, we'll throw an error if it's not a recognized format for states/bars.
                    // If arbitrary string properties should be set, this might need adjustment or a different action.
                    throw new ActionExecutionException("Invalid state value: '" + valueStr + "'. Expected boolean (true/false), a number, or 'null'.");
                }
            }
        } else {
            // Only state name provided, implies setting a boolean state to true.
            stateValue = Boolean.TRUE;
        }
        
        // Refined type checking based on known states/bars
        if (isKnownState && stateValue instanceof BigDecimal) {
            // If it's a known boolean state (like "Prone"), but a number is given.
            // Token.setState might handle this (e.g. 0=false, non-zero=true), or we can be strict.
            LOGGER.warn("Setting boolean state '" + stateName + "' with numeric value '" + stateValue + "'. Behavior depends on Token.setState() handling.");
            // For stricter handling: throw new ActionExecutionException("Type mismatch: State '" + stateName + "' is boolean, but numeric value '" + stateValue + "' was provided.");
        }
        if (isKnownBar && stateValue instanceof Boolean) {
            // If it's a known bar (like "HP"), but a boolean is given.
            // Convert true to 1, false to 0 for bars, or let Token.setState handle it.
            // For now, let Token.setState handle it, but log a warning.
             LOGGER.warn("Setting bar '" + stateName + "' with boolean value '" + stateValue + "'. Behavior depends on Token.setState() handling (e.g. true may become 1, false 0).");
            // For stricter handling: throw new ActionExecutionException("Type mismatch: Bar '" + stateName + "' expects a number, but boolean value '" + stateValue + "' was provided.");
        }


        // 2. Retrieve Token(s) from eventContext
        Object tokensObj = eventContext.get("tokens");
        if (tokensObj == null) {
            tokensObj = eventContext.get("token"); // Fallback to singular "token"
        }

        if (tokensObj == null) {
            throw new ActionExecutionException("No 'tokens' or 'token' found in eventContext for " + ACTION_NAME);
        }

        List<Token> targetTokens = new ArrayList<>();
        if (tokensObj instanceof List) {
            for (Object item : (List<?>) tokensObj) {
                if (item instanceof Token) {
                    targetTokens.add((Token) item);
                } else if (item instanceof GUID) {
                    Token t = findTokenInAnyZone((GUID) item, eventContext, campaign);
                    if (t != null) {
                        targetTokens.add(t);
                    } else {
                        LOGGER.warn("Could not find token with GUID: " + item + " for " + ACTION_NAME);
                    }
                } else if (item != null) {
                    LOGGER.warn("Unsupported token item type in list: " + item.getClass().getName());
                }
            }
        } else if (tokensObj instanceof Token) {
            targetTokens.add((Token) tokensObj);
        } else if (tokensObj instanceof GUID) {
            Token t = findTokenInAnyZone((GUID) tokensObj, eventContext, campaign);
            if (t != null) {
                targetTokens.add(t);
            } else {
                LOGGER.warn("Could not find token with GUID: " + tokensObj + " for " + ACTION_NAME);
            }
        } else {
            throw new ActionExecutionException("Invalid 'tokens' or 'token' type in eventContext: " + tokensObj.getClass().getName());
        }

        if (targetTokens.isEmpty()) {
            LOGGER.warn(ACTION_NAME + ": No valid tokens found to apply state '" + stateName + "'.");
            return true; // No tokens to process, not an error of the action itself.
        }

        // 3. Process each token
        boolean allSucceeded = true;
        for (Token token : targetTokens) {
            if (token == null) { // Should not happen if list construction is correct
                LOGGER.warn("Encountered a null token in the target list for " + ACTION_NAME);
                continue;
            }
            
            Zone tokenZone = campaign.getZone(token.getZoneId());
            if (tokenZone == null) {
                // This case should be rare if token objects are valid and have a zone ID.
                // If it happens, it might be a more fundamental issue.
                LOGGER.error("Could not find zone (ID: " + token.getZoneId() + ") for token " + token.getName() + " (" + token.getId() + ") while setting state '" + stateName + "'. Skipping this token.");
                allSucceeded = false;
                continue;
            }

            try {
                LOGGER.debug("Setting state '{}' to '{}' for token {} ({}) in zone {}", stateName, stateValue, token.getName(), token.getId(), tokenZone.getId());
                token.setState(stateName, stateValue);

                // Persist changes
                // This ensures that the change is saved and propagated to other clients if connected.
                // MapTool.serverCommand().putToken() is the method that handles this.
                // If the server is not connected (e.g., local campaign), it updates the local model.
                MapTool.serverCommand().putToken(tokenZone.getId(), token);
                
                // Additionally, explicitly notify local listeners about the token change,
                // as putToken might not cover all local UI updates immediately or in all scenarios.
                tokenZone.tokenChanged(token);

            } catch (Exception e) {
                LOGGER.error("Error setting state '" + stateName + "' for token " + token.getName() + " (" + token.getId() + ")", e);
                allSucceeded = false; // Mark as partial success or error
            }
        }
        return allSucceeded;
    }

    /**
     * Helper method to find a token by its ID.
     * It first checks the zone specified in the eventContext if available,
     * then searches all zones in the current campaign.
     */
    private Token findTokenInAnyZone(GUID tokenId, Map<String, Object> eventContext, Campaign campaign) {
        if (tokenId == null || campaign == null) {
            return null;
        }

        Object zoneIdObj = eventContext.get("zoneId");
        if (zoneIdObj instanceof GUID) {
            Zone zone = campaign.getZone((GUID) zoneIdObj);
            if (zone != null) {
                Token token = zone.getToken(tokenId);
                if (token != null) {
                    return token;
                }
            }
        }

        // Fallback: search all zones if zoneId not provided or token not in specified zone
        for (Zone z : campaign.getZones()) {
            Token token = z.getToken(tokenId);
            if (token != null) {
                return token;
            }
        }
        return null;
    }
}
