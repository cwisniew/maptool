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

import java.util.Map;

/**
 * Interface for predefined actions that can be triggered by event listeners.
 */
public interface PredefinedAction {

    /**
     * Executes the predefined action.
     *
     * @param eventContext A map containing data from the triggering event.
     *                     Common keys might include:
     *                     - "tokens": A List of Token objects or GUIDs.
     *                     - "token": A single Token or GUID.
     *                     - "zoneId": The GUID of the zone where the event occurred.
     *                     - "triggeringEventName": The name of the event that triggered this action.
     * @param actionParameters A string containing additional parameters specific to this action,
     *                         as configured in EventListenerConfig.actionReference.
     * @return true if the action was successful and, for vetoable "request" events, if the subsequent
     *         action should be allowed to proceed. Return false to veto (if applicable).
     * @throws ActionExecutionException if an error occurs during action execution.
     */
    boolean execute(Map<String, Object> eventContext, String actionParameters) throws ActionExecutionException;

    /**
     * A unique name/identifier for this predefined action (e.g., "mt.token.move").
     * This name is used to register and look up the action.
     * @return The unique name of the action.
     */
    String getName();
}
