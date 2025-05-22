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
package net.rptools.maptool.model.addons;

import java.util.List;
import java.util.Map;

public interface EventListenerProvider {
    /**
     * Returns a list of event listeners this add-on can provide.
     * The actionReference in an EventListenerConfig would be "addonId:listenerName".
     * @return A list of declarations.
     */
    List<AddOnEventListenerDeclaration> getProvidedEventListeners();

    /**
     * Executes a listener provided by this add-on.
     * @param listenerName The name of the listener to execute (matches AddOnEventListenerDeclaration.listenerName).
     * @param eventContext An object or map containing event-specific data (e.g., token ID, map ID, parameters from the triggering event).
     * @return True if the event should be consumed (for vetoable events), false otherwise.
     */
    boolean executeEventListener(String listenerName, Map<String, Object> eventContext);
}
