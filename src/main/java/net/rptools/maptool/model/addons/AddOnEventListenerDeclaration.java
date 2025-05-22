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

public class AddOnEventListenerDeclaration {
    private String listenerName; // e.g., "onTokenMove" - unique within the add-on
    private String descriptiveLabel; // e.g., "Log token movements to chat"
    private String targetEvent; // e.g., "mt.token.onMove" - the event it subscribes to

    public AddOnEventListenerDeclaration(String listenerName, String descriptiveLabel, String targetEvent) {
        this.listenerName = listenerName;
        this.descriptiveLabel = descriptiveLabel;
        this.targetEvent = targetEvent;
    }

    public String getListenerName() { return listenerName; }
    public String getDescriptiveLabel() { return descriptiveLabel; }
    public String getTargetEvent() { return targetEvent; }
}
