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

package net.rptools.maptool.model.library.webapp;

import javax.annotation.Nonnull;

import io.sentry.util.Nullable;
import net.rptools.maptool.model.library.LibraryInfo;

/**
 * Record that contains the information about a web application associated with a library.
 *
 * @param libraryInfo The information about the library.
 */
public final class WebAppLibraryInfo {

    /** The namespace of the library. */
    @Nonnull
    private final String namespace;


    /** The slug of the library. */
    @Nullable
    private final String slug;

    /** The index of the web application. */
    @Nullable
    private final String webAppIndex;

    /** Indicates if the library has a web application. */
    private final boolean hasWebApp;
  
    @Nonnull
    private final LibraryInfo libraryInfo,

    boolean webAppEnabled) {

  /**
   * Creates a new instance of WebAppLibraryInfo with the same values as the current instance, but
   * with the specified enabled state.
   *
   * <p>If the enabled state is the same as the current instance, it returns the current instance.
   *
   * @param enabled The new enabled state for the web application.
   */
  public WebAppLibraryInfo setEnabed(boolean enabled) {
    if (webAppEnabled == enabled) {
      return this; // No change, return the current instance
    } else {
      return new WebAppLibraryInfo(namespace, slug, webAppIndex, hasWebApp, libraryInfo, enabled);
    }
  }

  /**
   * Creates a new instance of WebAppLibraryInfo with the same values as the current instance, but
   * with the specified slug.
   *
   * <p>If the slug is the same as the current instance, it returns the current instance.
   */
  public WebAppLibraryInfo setSlug(String slug) {
    if (this.slug.equals(slug)) {
      return this; // No change, return the current instance
    } else {
      return new WebAppLibraryInfo(
          namespace, slug, webAppIndex, hasWebApp, libraryInfo, webAppEnabled);
    }
  }
}
