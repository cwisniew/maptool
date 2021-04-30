package net.rptools.maptool.client.framework;

import java.util.Map;
import net.rptools.lib.memento.Memento;

public record LibTokenEmulationMemento(
    String name,
    String version,
    Map<String, String> definedFunctions
) implements Memento<LibTokenEmulation> {


  @Override
  public Class<LibTokenEmulation> getOriginatorClass() {
    return LibTokenEmulation.class;
  }
}
