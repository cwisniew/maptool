package net.rptools.maptool.client.framework.library;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import net.rptools.lib.memento.MementoBuilder;
import net.rptools.lib.memento.MementoBuilderParseException;
import net.rptools.maptool.client.framework.library.libtoken.LibTokenEmulation;
import net.rptools.maptool.client.framework.library.libtoken.LibTokenEmulationMementoBuilder;
import net.rptools.maptool.client.framework.library.libtoken.LibTokenEmulationMemento;

public class FrameworkLibraryMementoBuilder extends MementoBuilder<FrameworkLibraryMemento> {


  /** The id of the library. */
  private UUID id;
  /** The name of the library. */
  private String name;
  /** The namespace of the library. */
  private String namespace;
  /** The version of the library. */
  private String version;
  /** The GitHubUrl of the library. */
  private String gitHubUrl;

  /** The functions that have been defined and exported to MTScript by the library. */
  private Map<String, String> definedFunctions = new ConcurrentHashMap<>();

  /** The data values that have been defined and exported to MTScript by the library. */
  private Map<String, DataValue> dataValues = new ConcurrentHashMap<>();

  /** Has this library been created in compatibility mode. */
  private boolean compatibilityMode;

  transient private Set<LibTokenEmulationMemento> libTokenEmulation = ConcurrentHashMap.newKeySet();


  public FrameworkLibraryMementoBuilder() {
    super(FrameworkLibraryMemento.class);
  }


  public UUID getId() {
    return this.id;
  }

  public FrameworkLibraryMementoBuilder setId(UUID id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public FrameworkLibraryMementoBuilder setName(String name) {
    this.name = name;
    return this;
  }

  public String getNamespace() {
    return namespace;
  }

  public FrameworkLibraryMementoBuilder setNamespace(String namespace) {
    this.namespace = namespace;
    return this;
  }

  public String getVersion() {
    return version;
  }

  public FrameworkLibraryMementoBuilder setVersion(String version) {
    this.version = version;
    return this;
  }

  public String getGitHubUrl() {
    return gitHubUrl;
  }

  public FrameworkLibraryMementoBuilder setGitHubUrl(String gitHubUrl) {
    this.gitHubUrl = gitHubUrl;
    return this;
  }

  public Map<String, String> getDefinedFunctions() {
    return definedFunctions;
  }

  public FrameworkLibraryMementoBuilder setDefinedFunctions(Map<String, String> definedFunctions) {
    this.definedFunctions.clear();
    this.definedFunctions.putAll(definedFunctions);
    return this;
  }

  public Map<String, DataValue> getDataValues() {
    return dataValues;
  }

  public FrameworkLibraryMementoBuilder setDataValues(Map<String, DataValue> dataValues) {
    this.dataValues.clear();
    this.dataValues.putAll(dataValues);
    return this;
  }

  public Set<LibTokenEmulationMemento> getLibTokenEmulation() {
    return libTokenEmulation;
  }

  public FrameworkLibraryMementoBuilder setLibTokenEmulation(Set<LibTokenEmulation> libTokenEmulations) {
    this.libTokenEmulation.clear();
    var builder = new LibTokenEmulationMementoBuilder();
    libTokenEmulations.forEach(lte -> {
      var state = builder.fromState(lte.getState()).build();
      this.libTokenEmulation.add(state);
    });
    return this;
  }

  public boolean isCompatibilityMode() {
    return compatibilityMode;
  }

  public FrameworkLibraryMementoBuilder setCompatibilityMode(boolean compatibilityMode) {
    this.compatibilityMode = compatibilityMode;
    return this;
  }


  @Override
  public FrameworkLibraryMemento build() {
    return new FrameworkLibraryMemento(
        id, name, namespace, version, gitHubUrl, definedFunctions, dataValues, compatibilityMode, libTokenEmulation
    );
  }

  @Override
  public MementoBuilder<FrameworkLibraryMemento> fromState(FrameworkLibraryMemento state) {
    id = state.id();
    name = state.name();
    namespace = state.namespace();
    version = state.version();
    gitHubUrl = state.gitHubUrl();
    definedFunctions.clear();
    definedFunctions.putAll(state.definedFunctions());
    dataValues.clear();
    dataValues.putAll(state.dataValues());
    compatibilityMode = state.compatibilityMode();
    libTokenEmulation.addAll(state.libTokenEmulationMementos());
    return this;
  }


  @Override
  public MementoBuilder<FrameworkLibraryMemento> fromJson(JsonObject json) throws MementoBuilderParseException {
    Set<String> errors = new HashSet<>();
    if (json.has("id")) {
      try {
        setId(UUID.fromString(json.get("id").getAsString()));
      } catch (Exception e) {
        errors.add("id invalid");
      }
    } else {
      errors.add("id missing");
    }

    if (json.has("name")) {
      setName(json.get("name").getAsString());
    } else {
      errors.add("name missing");
    }

    if (json.has("namespace")) {
      setNamespace(json.get("namespace").getAsString());
    } else {
      errors.add("namespace missing");
    }

    if (json.has("gitHubUrl")) {
      setGitHubUrl(json.get("gitHubUrl").getAsString());
    } else {
      setGitHubUrl("");     // GitHubUrl is not requied.
    }

    if (json.has("version")) {
      setVersion(json.get("version").getAsString());
    } else {
      errors.add("version missing");
    }

    if (json.has("compatibilityMode")) {
      try {
        setCompatibilityMode(json.get("compatibilityMode").getAsBoolean());
      } catch (Exception e) {
        errors.add("compatibilityMode invalid");
      }
    } else {
      setCompatibilityMode(false); // Defaults to false if not present.
    }

    definedFunctions.clear();
    if (json.has("definedFunctions")) {
      JsonArray defined = json.get("definedFunctions").getAsJsonArray();
      defined.forEach(e -> {
        JsonObject jobj = e.getAsJsonObject();
        definedFunctions.put(jobj.get("name").getAsString(), jobj.get("path").getAsString());
      });
    }

    dataValues.clear();
    if (json.has("dataValues")) {
      JsonArray data = json.get("dataValues").getAsJsonArray();
     /* TODO: CDW dataValues.forEach(e -> {

      });*/
    }

    libTokenEmulation.clear();
    if (json.has("libTokens")) {
      JsonArray libTokens = json.getAsJsonArray("libTokens");
      LibTokenEmulationMementoBuilder builder = new LibTokenEmulationMementoBuilder();
      for (JsonElement lt : libTokens) {
        libTokenEmulation.add(builder.fromJson(lt.getAsJsonObject()).build());
      }
    }

    if (!errors.isEmpty()) {
      throw new MementoBuilderParseException(errors);
    }

    return this;
  }


  public JsonObject toJson() {
    JsonObject json = new Gson().toJsonTree(this).getAsJsonObject();
    JsonArray jsonArray = new JsonArray();
    json.add("libTokens", jsonArray);
    LibTokenEmulationMementoBuilder builder = new LibTokenEmulationMementoBuilder();
    libTokenEmulation.forEach(lte -> jsonArray.add( builder.fromState(lte).toJson()));

    return json;
  }

}
