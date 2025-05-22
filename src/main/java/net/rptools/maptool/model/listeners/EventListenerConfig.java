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
package net.rptools.maptool.model.listeners;

import java.util.Objects;

public class EventListenerConfig {
    private String id; // Unique ID for this listener config, useful for management
    private String name; // User-friendly name for the listener
    private ListenerType listenerType;
    private String targetEvent; // The specific event it listens to (e.g., "mt.token.onMove")
    private String actionReference; // For MACRO_CODE: the macro code itself.
                                  // For PREDEFINED_ACTION: the name of the action (e.g., "mt.token.move").
                                  // For ADD_ON_LISTENER: an identifier for the add-on's listener
                                  // (e.g., "myAddonName:onTokenDropListener").
    private boolean enabled;
    private int executionOrder; // Lower numbers execute first

    // Constructors
    public EventListenerConfig(String id, String name, ListenerType listenerType, String targetEvent, String actionReference, boolean enabled, int executionOrder) {
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.listenerType = Objects.requireNonNull(listenerType);
        this.targetEvent = Objects.requireNonNull(targetEvent);
        // For MACRO_CODE, actionReference can be an empty string for an empty macro.
        // For other types, it's an identifier, so it should not be null.
        this.actionReference = Objects.requireNonNull(actionReference, "actionReference cannot be null");
        this.enabled = enabled;
        this.executionOrder = executionOrder;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public ListenerType getListenerType() { return listenerType; }
    public String getTargetEvent() { return targetEvent; }
    public String getActionReference() { return actionReference; }
    public boolean isEnabled() { return enabled; }
    public int getExecutionOrder() { return executionOrder; }

    // Setters
    public void setId(String id) { this.id = Objects.requireNonNull(id); } // ID should probably be immutable once set.
    public void setName(String name) { this.name = Objects.requireNonNull(name); }
    public void setListenerType(ListenerType listenerType) { this.listenerType = Objects.requireNonNull(listenerType); }
    public void setTargetEvent(String targetEvent) { this.targetEvent = Objects.requireNonNull(targetEvent); }
    public void setActionReference(String actionReference) { this.actionReference = Objects.requireNonNull(actionReference); }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public void setExecutionOrder(int executionOrder) { this.executionOrder = executionOrder; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventListenerConfig that = (EventListenerConfig) o;
        return id.equals(that.id); // ID should be unique
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "EventListenerConfig{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", listenerType=" + listenerType +
                ", targetEvent='" + targetEvent + '\'' +
                ", actionReferenceLength=" + (actionReference != null ? actionReference.length() : 0) + // Avoid printing long macro string
                ", enabled=" + enabled +
                ", executionOrder=" + executionOrder +
                '}';
    }
}
