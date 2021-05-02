package net.rptools.maptool.client.framework.library.libtoken;

import java.util.Map;
import net.rptools.lib.memento.Memento;

public record LibTokenEmulationMemento(
    String name,
    String version,
    Map<String, String> definedFunctions,
    Map<String, String> properties
) implements Memento<LibTokenEmulation> {


  @Override
  public Class<LibTokenEmulation> getOriginatorClass() {
    return LibTokenEmulation.class;
  }
}
