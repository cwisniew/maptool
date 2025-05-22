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
package net.rptools.maptool.model;

import net.rptools.maptool.model.listeners.EventListenerConfig;
import java.util.ArrayList;
import java.util.List;

public class TokenTypeDetails {
    private List<TokenProperty> properties = new ArrayList<>();
    private List<EventListenerConfig> defaultEventListeners = new ArrayList<>();

    public TokenTypeDetails() {} // Default constructor

    public List<TokenProperty> getProperties() {
        return properties;
    }

    public void setProperties(List<TokenProperty> properties) {
        this.properties = properties != null ? properties : new ArrayList<>();
    }

    public List<EventListenerConfig> getDefaultEventListeners() {
        return defaultEventListeners;
    }

    public void setDefaultEventListeners(List<EventListenerConfig> listeners) {
        this.defaultEventListeners = listeners != null ? listeners : new ArrayList<>();
    }
}
