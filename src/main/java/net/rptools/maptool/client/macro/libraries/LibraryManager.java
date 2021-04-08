package net.rptools.maptool.client.macro.libraries;

import java.util.HashSet;
import java.util.Set;

public class LibraryManager {
  private final Set<Library> libraries = new HashSet<>();
  private final Set<LibTokenEmulation> libTokenEmulations = new HashSet<>();

  public void addLibrary(Library library) {
    libraries.add(library);
  }

}
