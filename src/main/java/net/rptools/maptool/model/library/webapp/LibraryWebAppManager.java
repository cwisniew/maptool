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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.rptools.maptool.util.RandomStringGenerator;

/**
 * Manages web applications for libraries, allowing for adding, removing, and retrieving web app
 * information by namespace or slug.
 */
public class LibraryWebAppManager {

  /** A lock to ensure thread-safe access to the web application information. */
  private final ReentrantLock lock = new ReentrantLock();

  /** A map to store web applications by their namespace. */
  private final Map<String, WebAppLibraryInfo> namespaceWebAppMap = new HashMap<>();

  /** A map to store web applications by their slug. */
  private final Map<String, WebAppLibraryInfo> slugWebAppMap = new HashMap<>();

  /** A map to store web applications by their namespace. */
  public RandomStringGenerator randomStringGenerator = new RandomStringGenerator();

  /**
   * Adds a web application to the manager. If a web application with the same namespace already
   * exists, it will be replaced. If the slug already exists for a different namespace, generate a
   * new slug.
   *
   * @param webApp The web application information to add.
   * @return A WebAppLibraryInfo object representing the added web application which will contain
   *     the updated slug if it was changed due to a conflict.
   */
  public WebAppLibraryInfo putWebApp(@Nonnull WebAppLibraryInfo webApp) {
    try {
      lock.lock();
      // Make sure the slug is unique across all web apps, we are not expect to even into the
      // hundreads of web apps so conflics of this random slug generator should be quite rare.
      // So this simple loop should be sufficient.
      String slug = webApp.slug();
      while (slugWebAppMap.containsKey(slug)
          && !slugWebAppMap.get(slug).namespace().equals(webApp.namespace())) {
        // Generate a new slug if it conflicts with an existing one
        slug = randomStringGenerator.generateString(4);
      }

      var updatedWebApp = webApp.setSlug(slug);

      namespaceWebAppMap.put(updatedWebApp.namespace(), updatedWebApp);
      slugWebAppMap.put(updatedWebApp.slug(), updatedWebApp);
      return updatedWebApp;

    } finally {
      lock.unlock();
    }
  }

  /**
   * Removes a web application from the manager by its namespace.
   *
   * @param namespace The namespace of the web application to remove.
   */
  public void removeWebApp(@Nonnull String namespace) {
    try {
      lock.lock();
      WebAppLibraryInfo webApp = namespaceWebAppMap.remove(namespace);
      if (webApp != null) {
        namespaceWebAppMap.remove(webApp.namespace());
        slugWebAppMap.remove(webApp.slug());
      }
    } finally {
      lock.unlock();
    }
  }

  /**
   * Retrieves a web application by its namespace.
   *
   * @param namespace The namespace of the web application to retrieve.
   * @return The web application information, or {@code null} if not found.
   */
  public @Nullable WebAppLibraryInfo getWebAppByNamespace(@Nonnull String namespace) {
    try {
      lock.lock();
      return namespaceWebAppMap.get(namespace);
    } finally {
      lock.unlock();
    }
  }

  /**
   * Retrieves a web application by its slug.
   *
   * @param slug The slug of the web application to retrieve.
   * @return The web application information, or {@code null} if not found.
   */
  public @Nullable WebAppLibraryInfo getWebAppBySlug(@Nonnull String slug) {
    try {
      lock.lock();
      return slugWebAppMap.get(slug);
    } finally {
      lock.unlock();
    }
  }

  /**
   * Checks if a web application exists for the given namespace.
   *
   * @param namespace The namespace to check.
   * @return {@code true} if a web application exists for the namespace, otherwise {@code false}.
   * @throws IllegalArgumentException there is no web app for the given namespace.
   */
  public void setEnableWebApp(@Nonnull String namespace, boolean enable) {
    try {
      lock.lock();
      var webApp = namespaceWebAppMap.get(namespace);
      if (webApp == null) {
        throw new IllegalArgumentException("No web app found for namespace: " + namespace);
      }
      var updatedWebApp = webApp.setEnabed(enable);
      namespaceWebAppMap.put(updatedWebApp.namespace(), updatedWebApp);
      slugWebAppMap.put(updatedWebApp.slug(), updatedWebApp);

    } finally {
      lock.unlock();
    }
  }

  /**
   * Sets the slug for a web application in the given namespace. If the slug already exists for
   * another web application, it will not replace it and will return {@code false}.
   *
   * @param namespace The namespace of the web application.
   * @param slug The new slug to set for the web application.
   * @return {@code true} if the slug was set successfully, otherwise {@code false}.
   */
  public boolean setSlug(@Nonnull String namespace, @Nonnull String slug) {
    try {
      lock.lock();
      WebAppLibraryInfo existingWebApp = getWebAppBySlug(slug);
      if (existingWebApp != null && !existingWebApp.namespace().equals(namespace)) {
        // Slug already exists for a different namespace
        return false;
      }
      WebAppLibraryInfo webApp = namespaceWebAppMap.get(namespace);
      if (webApp != null) {
        namespaceWebAppMap.put(webApp.namespace(), webApp.setSlug(slug));
        slugWebAppMap.put(slug, webApp.setSlug(slug));
      }
      return true;
    } finally {
      lock.unlock();
    }
  }

  /**
   * Checks if a web application is enabled for the given namespace. u *
   *
   * @param namespace The namespace to check.
   * @return {@code true} if the web application is enabled, otherwise {@code false}.
   */
  public boolean isEnabled(@Nonnull String namespace) {
    try {
      lock.lock();
      WebAppLibraryInfo webApp = namespaceWebAppMap.get(namespace);
      return webApp != null && webApp.webAppEnabled();
    } finally {
      lock.unlock();
    }
  }

  /** Clears all web applications from the manager. */
  public void clear() {
    try {
      lock.lock();
      namespaceWebAppMap.clear();
      slugWebAppMap.clear();
    } finally {
      lock.unlock();
    }
  }
}
