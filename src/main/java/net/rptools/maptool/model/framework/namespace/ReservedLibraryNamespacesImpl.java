package net.rptools.maptool.model.framework.namespace;

import java.util.Set;

public class ReservedLibraryNamespacesImpl implements ReservedLibraryNamespaces {

  /** The reserved library name prefixes. */
  private static final Set<String> RESERVED_PREFIXES =
      Set.of(
          "rptools.",
          "maptool.",
          "maptools.",
          "tokentool.",
          "net.rptools.",
          "internal.",
          "_",
          "builtin.",
          "standard.",
          ".");


  public boolean usesReservedNamespace(String namespace) {
    String lowerName = namespace.toLowerCase();
    return RESERVED_PREFIXES.stream().anyMatch(lowerName::startsWith);
  }


  @Override
  public String getReservedPrefix(String name) {
    String lowerName = name.toLowerCase();
    return RESERVED_PREFIXES.stream().filter(lowerName::startsWith).findFirst().orElse("");
  }
}
