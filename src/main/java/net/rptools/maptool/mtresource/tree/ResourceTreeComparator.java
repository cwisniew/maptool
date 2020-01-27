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
package net.rptools.maptool.mtresource.tree;

import java.util.Comparator;

public class ResourceTreeComparator implements Comparator<ResourceTreeNode> {

  @Override
  public int compare(ResourceTreeNode o1, ResourceTreeNode o2) {
    if (o1.isDirectory() && o2.isDirectory()) {
      return o1.getDirectoryName().compareTo(o2.getDirectoryName());
    } else if (o1.isDirectory()) {
      return -1; // Directory comes before non directory
    } else if (o2.isDirectory()) {
      return 1; // Directory comes before non directory
    } else {
      return o1.getResource().getName().compareTo(o2.getResource().getName());
    }
  }
}
