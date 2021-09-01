package net.rptools.maptool.api.frameworks;

import java.util.List;
import java.util.Set;
import net.rptools.maptool.api.ApiData;
import net.rptools.maptool.model.GUID;

public record LibTokenInfo(GUID id, String name, List<String> macroNames,
                           Set<String> propertyNames) implements ApiData {
  public LibTokenInfo {
    macroNames = List.copyOf(macroNames);
    propertyNames = Set.copyOf(propertyNames);
  }
}
