package net.rptools.maptool.model.framework.namespace;

import java.util.Set;

public interface ReservedLibraryNamespaces {


  /**
   * Checks if the given namespace starts with a reserved prefix.
   * @param namespace the namespace to check.
   * @return {@code true} if the namespace starts with a reserved prefix.
   */
  boolean usesReservedNamespace(String namespace);

  /**
   * Returns the reserved prefix this library name starts with.
   *
   * @param name the name of the library.
   * @return the reserved prefix this library starts with or {@code null} if it does not start with
   *     a reserved prefix.
   */
  String getReservedPrefix(String name);

  }
