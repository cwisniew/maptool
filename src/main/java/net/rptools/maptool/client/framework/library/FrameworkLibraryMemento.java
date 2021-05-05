package net.rptools.maptool.client.framework.library;

import java.util.*;

import net.rptools.lib.memento.Memento;
import net.rptools.maptool.client.framework.library.libtoken.LibTokenEmulation;
import net.rptools.maptool.client.framework.library.libtoken.LibTokenEmulationMementoBuilder;
import net.rptools.maptool.client.framework.library.libtoken.LibTokenEmulationMemento;

public record FrameworkLibraryMemento(
    UUID id,
    String name,
    String namespace,
    String version,
    String gitHubUrl,
    Map<String, String> definedFunctions,
    Map<String, DataValue> dataValues,
    boolean compatibilityMode,
    Set<LibTokenEmulationMemento> libTokenEmulationMementos
) implements Memento<FrameworkLibrary> {

  public FrameworkLibraryMemento(
      UUID id,
      String name,
      String namespace,
      String version,
      String gitHubUrl,
      Map<String, String> definedFunctions,
      Map<String, DataValue> dataValues,
      boolean compatibilityMode,
      Set<LibTokenEmulationMemento> libTokenEmulationMementos
  ) {
    this.id = Objects.requireNonNull(id);
    this.name = Objects.requireNonNull(name);
    this.namespace = Objects.requireNonNull(namespace);
    this.version = Objects.requireNonNull(version);
    this.gitHubUrl = Objects.requireNonNull(gitHubUrl);
    this.definedFunctions = Map.copyOf(definedFunctions);
    this.dataValues = Map.copyOf(dataValues);
    this.compatibilityMode = compatibilityMode;
    this.libTokenEmulationMementos = Set.copyOf(libTokenEmulationMementos);
  }


  @Override
  public Class<FrameworkLibrary> getOriginatorClass() {
    return FrameworkLibrary.class;
  }


  public Set<LibTokenEmulation> libTokenEmulation() {
    Set<LibTokenEmulation> libTokens = new HashSet<>();
    LibTokenEmulationMementoBuilder builder = new LibTokenEmulationMementoBuilder();
    libTokenEmulationMementos.forEach(lte -> {
      libTokens.add(new LibTokenEmulation(builder.fromState(lte).build()));
    });

    return libTokens;
  }
}
