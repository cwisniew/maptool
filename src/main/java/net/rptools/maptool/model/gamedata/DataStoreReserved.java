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
package net.rptools.maptool.model.gamedata;

import java.util.Set;

/** Class that contains information about reserved data store types and namespaces. */
class DataStoreReserved {

  /** Prefixes for reserved types. */
  private static final Set<String> RESERVED_TYPE_PREFIXES =
      Set.of("mt:", "maptool:", "rptools:", "internal:");

  /** Prefixes for reserved namespaces. */
  private static final Set<String> RESERVED_NAMESPACE_PREFIXES =
      Set.of("net.rptools", "info.rptools");

  /**
   * Checks if the type or namespace is reserved.
   *
   * @param type the type to check.
   * @param namespace the namespace to check.
   * @return {@code true} if the type is reserved.
   */
  public boolean isReserved(String type, String namespace) {
    return isReservedType(type) || isReservedNamespace(namespace);
  }

  /**
   * Checks if the type is reserved.
   *
   * @param type the type to check.
   * @return {@code true} if the type is reserved.
   */
  public boolean isReservedType(String type) {
    return RESERVED_TYPE_PREFIXES.stream().anyMatch(type::startsWith);
  }

  /**
   * Checks if the namespace is reserved.
   *
   * @param namespace the namespace to check.
   * @return {@code true} if the namespace is reserved.
   */
  public boolean isReservedNamespace(String namespace) {
    return RESERVED_NAMESPACE_PREFIXES.stream().anyMatch(namespace::startsWith);
  }
}
