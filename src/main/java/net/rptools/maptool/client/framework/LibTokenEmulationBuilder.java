package net.rptools.maptool.client.framework;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.rptools.lib.memento.MementoBuilder;
import net.rptools.lib.memento.MementoBuilderParseException;

public class LibTokenEmulationBuilder extends MementoBuilder<LibTokenEmulationMemento> {

  private String name;
  private String version;
  private Map<String, String> definedFunctions = new HashMap<>();
  private UUID frameworkId;


  public LibTokenEmulationBuilder() {
    super(LibTokenEmulationMemento.class);
  }

  public String getName() {
    return name;
  }

  public LibTokenEmulationBuilder setName(String name) {
    this.name = name;
    return this;
  }

  public String getVersion() {
    return version;
  }

  public LibTokenEmulationBuilder setVersion(String version) {
    this.version = version;
    return this;
  }

  public Map<String, String> getDefinedFunctions() {
    return Map.copyOf(definedFunctions);
  }

  public LibTokenEmulationBuilder clearDefinedFunctions() {
    definedFunctions.clear();
    return this;
  }

  public LibTokenEmulationBuilder setDefinedFunctions(Map<String, String> functions) {
    definedFunctions.clear();
    definedFunctions.putAll(functions);
    return this;
  }

  public LibTokenEmulationBuilder addDefinedFunction(String name, String path) {
    definedFunctions.put(name, path);
    return this;
  }

  public UUID getFrameworkId() {
    return frameworkId;
  }

  public LibTokenEmulationBuilder setFrameworkId(UUID frameworkId) {
    this.frameworkId = frameworkId;
    return this;
  }

  @Override
  public LibTokenEmulationMemento build() {
    return new LibTokenEmulationMemento(name, version, definedFunctions);
  }

  @Override
  public MementoBuilder<LibTokenEmulationMemento> fromState(LibTokenEmulationMemento state) {
    name = state.name();
    version = state.version();
    definedFunctions.clear();
    definedFunctions.putAll(state.definedFunctions());
    return this;
  }

  @Override
  public MementoBuilder<LibTokenEmulationMemento> fromJson(JsonObject json)
      throws MementoBuilderParseException {
    Set<String> errors = new HashSet<>();

    if (json.has("name")) {
      name = json.get("name").getAsString();
    } else {
      errors.add("name required for lib token.");
    }

    if (json.has("version")) {
      version = json.get("version").getAsString();
    } else {
      errors.add("version required for lib token.");
    }

    definedFunctions.clear();
    if (json.has("definedFunctions")) {
      json.getAsJsonArray("definedFunctions").forEach(f -> {
        if (f instanceof JsonObject jo) {
          if (jo.has("name") && jo.has("path")) {
            definedFunctions.put(jo.get("name").getAsString(), jo.get("version").getAsString());
          } else {
            errors.add("lib token invalid defined function");
          }
        } else {
          errors.add("lib token definedFunctions should be an array.");
        }
      });

    }

    if (!errors.isEmpty()) {
      throw new MementoBuilderParseException(errors);
    }

    return this;
  }

  public JsonObject toJson() {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("name", name);
    jsonObject.addProperty("version", version);
    JsonArray funcs = new JsonArray();
    jsonObject.add("definedFunctions", funcs);
    definedFunctions.forEach((key, value) -> {
      JsonObject fn = new JsonObject();
      fn.addProperty("name", key);
      fn.addProperty("path", value);
      funcs.add(fn);
    });
    return jsonObject;
  }
}
