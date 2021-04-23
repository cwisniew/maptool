package net.rptools.maptool.client.framework.library;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import net.rptools.lib.memento.Memento;

public record FrameworkLibraryMemento(
    UUID id,
    String name,
    String namespace,
    String version,
    String gitHubUrl,
    Map<String, String> definedFunctions,
    Map<String, DataValue> dataValues,
    boolean compatibilityMode
) implements Memento<FrameworkLibrary> {

  public FrameworkLibraryMemento(
      UUID id,
      String name,
      String namespace,
      String version,
      String gitHubUrl,
      Map<String, String> definedFunctions,
      Map<String, DataValue> dataValues,
      boolean compatibilityMode
  ) {
    this.id = Objects.requireNonNull(id);
    this.name = Objects.requireNonNull(name);
    this.namespace = Objects.requireNonNull(namespace);
    this.version = Objects.requireNonNull(version);
    this.gitHubUrl = Objects.requireNonNull(gitHubUrl);
    this.definedFunctions = Map.copyOf(definedFunctions);
    this.dataValues = Map.copyOf(dataValues);
    this.compatibilityMode = compatibilityMode;
  }


  @Override
  public Class<FrameworkLibrary> getOriginatorClass() {
    return FrameworkLibrary.class;
  }

}
