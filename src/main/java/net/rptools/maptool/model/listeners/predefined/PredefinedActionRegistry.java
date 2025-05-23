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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader; // For potential future extensibility
import java.util.Set;

/**
 * Registry for PredefinedAction implementations.
 * This allows the event dispatch system to find and execute predefined actions by name.
 */
public class PredefinedActionRegistry {
    private static final PredefinedActionRegistry INSTANCE = new PredefinedActionRegistry();
    private final Map<String, PredefinedAction> actionMap = new HashMap<>();

    private PredefinedActionRegistry() {
        // Actions will be registered here, possibly via ServiceLoader in the future
        // or by direct instantiation and registration in a static block or dedicated method.
        // For now, this constructor remains empty, actual action registration
        // will occur as each action is defined and implemented.
        // Example: loadActions();
    }

    public static PredefinedActionRegistry getInstance() {
        return INSTANCE;
    }

    public void registerAction(PredefinedAction action) {
        if (action != null && action.getName() != null && !action.getName().trim().isEmpty()) {
            actionMap.put(action.getName(), action);
        }
    }

    public PredefinedAction getAction(String name) {
        return actionMap.get(name);
    }

    public Set<String> getAllActionNames() {
        return Collections.unmodifiableSet(actionMap.keySet());
    }

    // Example of how actions could be loaded (call this from constructor or an init method)
    /*
    private void loadActions() {
        // Using ServiceLoader (preferred for extensibility if actions can be in different modules/plugins)
        ServiceLoader<PredefinedAction> loader = ServiceLoader.load(PredefinedAction.class);
        for (PredefinedAction action : loader) {
            registerAction(action);
        }

        // Manual registration (if all actions are known and in the same codebase)
        // registerAction(new YourMtTokenMoveAction());
        // registerAction(new YourMtTokenSetStateAction());
    }
    */
}
