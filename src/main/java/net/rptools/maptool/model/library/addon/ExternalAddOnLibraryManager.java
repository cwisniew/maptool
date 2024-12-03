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
package net.rptools.maptool.model.library.addon;

import com.google.common.eventbus.Subscribe;
import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import net.rptools.maptool.client.AppPreferences;
import net.rptools.maptool.client.MapTool;
import net.rptools.maptool.events.MapToolEventBus;
import net.rptools.maptool.language.I18N;
import net.rptools.maptool.model.library.AddOnsAddedEvent;
import net.rptools.maptool.model.library.AddOnsRemovedEvent;
import net.rptools.maptool.model.library.ExternalAddonsUpdateEvent;
import net.rptools.maptool.model.library.LibraryInfo;

/**
 * Manages the external add-on libraries that are baked by the file system. This manager will watch
 * the external add-on library directory for changes and will update the add-on libraries available
 * but will not automatically update the add-on libraries that MapTool has loaded.
 */
public class ExternalAddOnLibraryManager {

  /** The add-on library manager that is used to register the external add-on libraries. */
  private final AddOnLibraryManager addOnLibraryManager;

  /** The add-on libraries that are registered. */
  private final Map<String, ExternalLibraryInfo> namespaceInfoMap = new ConcurrentHashMap<>();


  /**
   * @param path the path to watch for add-on libraries.
   * @param initialised whether the external add-on library manager is initialised.
   * @param enabled whether the external add-on library manager is enabled.
   * @param refreshMs the amount of time to sleep between refreshes.
   */
  private record ExternalDirectoryInfo(Path path, boolean initialised, boolean enabled,
                                       int refreshMs) {}

  /** Information about the external add-on library directory. */
  private AtomicReference<ExternalDirectoryInfo> externalDirectoryInfo =
      new AtomicReference<>(new ExternalDirectoryInfo(null, false, false, 2 * 60 * 1000));

  /** The thread for refreshing the external add-on libraries. */
  private Thread refreshThread;


  /**
   * Creates a new instance of the external add-on library manager. {@code init()} must be called
   * after construction.
   *
   * @param addOnLibraryManager the add-on library manager used to register the add-on libraries.
   */
  public ExternalAddOnLibraryManager(AddOnLibraryManager addOnLibraryManager) {
    this.addOnLibraryManager = addOnLibraryManager;
  }

  /**
   * Initializes the external add-on library manager. It is safe to call {@see
   * setExternalLibraryPath(Path)} and {@see setEnabled(boolean)} before calling this method, but no
   * directory watching will occur until this method is called.
   *
   * @throws IOException if an error occurs.
   * @throws IllegalStateException if the external add-on library manager has already been
   *     initialised.
   */
  public void init() throws IOException {
    if (externalDirectoryInfo.get().initialised) {
      throw new IllegalStateException("External add-on library manager already initialised");
    }
    var path = Path.of(AppPreferences.externalAddOnLibrariesPath.get());
    var enabled = AppPreferences.externalAddOnLibrariesEnabled.get();
    var refreshMs = AppPreferences.externalAddOnLibrariesRefreshInterval.get() * 60 * 1000;
    externalDirectoryInfo.set(new ExternalDirectoryInfo(path, true, enabled, refreshMs));

    startWatching();

    var eventBus = new MapToolEventBus().getMainEventBus();
    eventBus.register(this);
  }

  /**
   * Handles the event when an add-on library is added to MapTool.
   *
   * @param event the add-on library that was added to MapTool.
   */
  @Subscribe
  public void onLibraryAdded(AddOnsAddedEvent event) {
    boolean updated = false;
    for (LibraryInfo libraryInfo : event.addOns()) {
      var namespace = libraryInfo.namespace().toLowerCase();
      var path = externalDirectoryInfo.get().path();
      if (path == null) {
        return;
      }
      if (namespaceInfoMap.containsKey(namespace)) {
        var oldInfo = namespaceInfoMap.get(namespace);
        var newInfo =
            new ExternalLibraryInfo(
                namespace,
                oldInfo.libraryInfo(),
                false,
                true,
                oldInfo.backingDirectory(),
                path.relativize(oldInfo.backingDirectory()).toString());
        namespaceInfoMap.put(namespace, newInfo);
        updated = true;
      }
    }
    if (updated) {
      new MapToolEventBus().getMainEventBus().post(new ExternalAddonsUpdateEvent());
    }
  }

  /**
   * Handles the event when an add-on library is removed from MapTool.
   *
   * @param event the add-on library that was removed from MapTool.
   */
  @Subscribe
  public void onLibraryRemoved(AddOnsRemovedEvent event) {
    boolean updated = false;
    for (LibraryInfo libraryInfo : event.addOns()) {
      var namespace = libraryInfo.namespace().toLowerCase();
      var path = externalDirectoryInfo.get().path();
      if (path == null) {
        return;
      }
      if (namespaceInfoMap.containsKey(namespace)) {
        var oldInfo = namespaceInfoMap.get(namespace);
        var newInfo =
            new ExternalLibraryInfo(
                namespace,
                oldInfo.libraryInfo(),
                true,
                false,
                oldInfo.backingDirectory(),
                path.relativize(oldInfo.backingDirectory()).toString());
        namespaceInfoMap.put(namespace, newInfo);
        updated = true;
      }
    }
    if (updated) {
      new MapToolEventBus().getMainEventBus().post(new ExternalAddonsUpdateEvent());
    }
  }

  /**
   * Registers an external add-on library.
   *
   * @param info Information about the add-on library to register.
   */
  private void registerExternalAddOnLibrary(ExternalLibraryInfo info) {
    boolean isInstalled = addOnLibraryManager.isNamespaceRegistered(info.namespace());
    var path = externalDirectoryInfo.get().path();
    if (path == null) {
      return;
    }
    var externalInfo =
        new ExternalLibraryInfo(
            info.namespace(),
            info.libraryInfo(),
            true,
            isInstalled,
            info.backingDirectory(),
            path.relativize(info.backingDirectory()).toString());
    namespaceInfoMap.put(info.namespace().toLowerCase(), externalInfo);
  }

  /** Clears the external add-on libraries. */
  private void clearExternalAddOnLibrary() {
    namespaceInfoMap.clear();
  }

  /**
   * Deregisters an external add-on library.
   *
   * @param path the backing path of the add-on library to deregister.
   */
  public void deregisterExternalAddOnLibrary(Path path) {
    namespaceInfoMap.values().stream()
        .filter(info -> info.backingDirectory().equals(path))
        .findFirst()
        .ifPresent(info -> namespaceInfoMap.remove(info.namespace().toLowerCase()));
    var eventBus = new MapToolEventBus().getMainEventBus();
    eventBus.post(new ExternalAddonsUpdateEvent());
  }

  /**
   * Refreshes an external add-on library.
   *
   * @param path the path to the add-on library.
   * @throws IOException if an error occurs.
   */
  public void refreshExternalAddOnLibrary(Path path) throws IOException {
    registerExternalAddOnLibrary(path); // Allows us to change behaviour later without breaking API
  }

  /**
   * Registers an external add-on library.
   *
   * @param path the path to the add-on library.
   * @throws IOException if an error occurs.
   */
  public void registerExternalAddOnLibrary(Path path) throws IOException {
    var lib = new AddOnLibraryImporter().getLibraryInfoFromDirectory(path);
    if (lib == null) {
      return;
    }
    boolean isInstalled = addOnLibraryManager.isNamespaceRegistered(lib.namespace());
    var extpath = externalDirectoryInfo.get().path();
    if (extpath == null) {
      return;
    }
    var info =
        new ExternalLibraryInfo(
            lib.namespace(),
            lib,
            false,
            isInstalled,
            path,
            extpath.relativize(path).toString());
    registerExternalAddOnLibrary(info);
    var eventBus = new MapToolEventBus().getMainEventBus();
    eventBus.post(new ExternalAddonsUpdateEvent());
  }

  /**
   * Gets the external libraries that have been registered.
   *
   * @return the external libraries.
   */
  public List<ExternalLibraryInfo> getLibraries() {
    return new ArrayList<>(namespaceInfoMap.values());
  }

  /**
   * Is the external add-on library manager enabled.
   *
   * @return {@code true} if the external add-on library manager is enabled.
   */
  public boolean isEnabled() {
    return externalDirectoryInfo.get().enabled();
  }

  /**
   * Sets the enabled state of the external add-on library manager.
   *
   * @param enabled the enabled state.
   * @throws IOException if an error occurs.
   */
  public void setEnabled(boolean enabled) throws IOException {
    var info = externalDirectoryInfo.get();
    if (info.enabled() == enabled) {
      return;
    }
    if (enabled) {
      startWatching();
    } else {
      stopWatching();
    }
  }

  /**
   * Gets the path to the external add-on libraries.
   *
   * @return the path to the external add-on libraries.
   */
  public Path getExternalLibraryPath() {
    return externalDirectoryInfo.get().path();
  }

  /**
   * Sets the path to the external add-on libraries.
   *
   * @param path the path to the external add-on libraries.
   * @throws IOException if an error occurs.
   */
  public void setExternalLibraryPath(Path path) throws IOException {
    refreshAll();
    var info = externalDirectoryInfo.get();
    if (path != null && path.equals(info.path())) {
      return;
    }
    stopWatching();
    if (path != null && info.enabled()) {
        startWatching();
    }
  }

  /** Stops watching the external add-on library directory. */
  private void stopWatching() throws IOException {
    var info = externalDirectoryInfo.get();
    externalDirectoryInfo.set(new ExternalDirectoryInfo(info.path(), info.initialised(), false, info.refreshMs()));
    if (refreshThread != null) {
      refreshThread.interrupt();
      refreshThread = null;
      clearExternalAddOnLibrary();
    }
  }

  /**
   * Starts watching the external add-on library directory.
   *
   */
  private void startWatching() throws IOException {
    var info = externalDirectoryInfo.get();
    externalDirectoryInfo.set(new ExternalDirectoryInfo(info.path(), info.initialised(), true, info.refreshMs()));
    refreshAll();
    if (refreshThread == null) {
        refreshThread =
            new Thread(() -> {
                  while (externalDirectoryInfo.get().enabled()) {
                    var extInfo = externalDirectoryInfo.get();
                    try {
                      Thread.sleep(extInfo.refreshMs());
                      refreshAll();
                    } catch (InterruptedException ie) {
                      // Ignore
                    } catch (IOException e) {
                    EventQueue.invokeLater(
                      () ->
                          MapTool.showError(
                              I18N.getText("library.dialog.read.failed", extInfo.path())));
                }
              }
        });
        refreshThread.start();
      }
    }

  /**
   * Refreshes all the external add-on libraries.
   *
   * @throws IOException if an error occurs.
   */
  private void refreshAll() throws IOException {
    clearExternalAddOnLibrary();
    var info = externalDirectoryInfo.get();
    if (!info.initialised() || !info.enabled() || info.path() == null) {
      return;
    }

    File[] directories = info.path.toFile().listFiles(File::isDirectory);
    if (directories != null) {
      for (File directory : directories) {
        registerExternalAddOnLibrary(directory.toPath());
      }
    }
  }

  /**
   * Makes the add-on library with the specified namespace available to MapTool.
   *
   * @param namespace the namespace of the add-on library to make available.
   */
  public void importLibrary(String namespace) throws IOException {
    if (externalDirectoryInfo.get().enabled()) {
      var libInfo = namespaceInfoMap.get(namespace.toLowerCase());
      if (libInfo != null) {
        var lib = new AddOnLibraryImporter().importFromDirectory(libInfo.backingDirectory());
        addOnLibraryManager.registerLibrary(lib);
      }
    }
  }

  /**
   * Checks to see if the specified path is an ignored sub-path when watching the external add-on
   *
   * @param path the path to check.
   * @return {@code true} if the path is an ignored sub-path.
   */
  private boolean isIgnoredSubPath(Path path) {
    if (path.startsWith(".git")) {
      return true;
    }

    if (path.getFileName().toString().toLowerCase().endsWith(".mtlib")) {
      return true;
    }

    return false;
  }

  /**
   * Sets the refresh interval for the external add-on libraries.
   * @param minutes the number of minutes to wait between refreshes.
   *
   * @throws IOException if an error occurs.
   */
  public void setRefreshInterval(int minutes) throws IOException {
    int refreshSleepMS = minutes * 60 * 1000;
    var info = externalDirectoryInfo.get();
    if (info.refreshMs() == refreshSleepMS) {
      return;
    }
    externalDirectoryInfo.set(new ExternalDirectoryInfo(info.path(), info.initialised(), info.enabled(), refreshSleepMS));
    if (info.enabled()) {
      stopWatching();
      startWatching();
    }
  }
}
