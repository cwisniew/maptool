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

package net.rptools.maptool.webapp.api;

import net.rptools.maptool.client.MapTool;
import net.rptools.maptool.client.swing.MemoryStatusBar;

/**
 * Provides information about the MapTool application, including version, locale, and memory usage.
 * This class is designed to be used in a web application context, allowing clients to retrieve
 * application details via an API endpoint.
 */
public class Info {

  /**
   * Represents the locale information of the application, including language, country, locale
   * string, and variant.
   *
   * @param language The language code (e.g., "en").
   * @param country The country code (e.g., "US").
   * @param locale The locale string representation (e.g., "en_US").
   * @param variant The variant of the locale (e.g., "POSIX").
   */
  public record Locale(String language, String country, String locale, String variant) {}

  /**
   * Represents memory usage statistics of the application, including maximum memory, used memory,
   * and the maximum memory used at any point.
   *
   * @param max The maximum memory available to the application in bytes.
   * @param used The amount of memory currently used by the application in bytes.
   * @param maxUsed The maximum amount of memory used by the application at any point in bytes.
   */
  public record Memory(long max, long used, long maxUsed) {}

  /** The version of the MapTool Application. */
  private final String version;

  /** The locale information of the application. */
  private final Locale locale;

  /** Memory usage statistics of the application. */
  private final Memory memory;

  /**
   * Private constructor to initialize the Info object with the current MapTool version, locale, and
   * memory usage statistics.
   */
  private Info() {
    version = MapTool.getVersion();
    var loc = java.util.Locale.getDefault();
    locale = new Locale(loc.getLanguage(), loc.getCountry(), loc.toString(), loc.getVariant());
    memory =
        new Memory(
            Runtime.getRuntime().maxMemory(),
            Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory(),
            MemoryStatusBar.getInstance().getLargestMemoryUsed());
  }

  /**
   * Static factory method to create an instance of the Info class.
   *
   * @return A new instance of Info containing the current application details.
   */
  public static Info getInfo() {
    return new Info();
  }

  /**
   * Gets the version of the MapTool application.
   *
   * @return The version string of the application.
   */
  public String getVersion() {
    return version;
  }

  /**
   * Gets the memory usage statistics of the application.
   *
   * @return A Memory object containing memory statistics.
   */
  public Memory getMemory() {
    return memory;
  }

  /**
   * Gets the locale information of the application.
   *
   * @return A Locale object containing locale details.
   */
  public Locale getLocale() {
    return locale;
  }
}
