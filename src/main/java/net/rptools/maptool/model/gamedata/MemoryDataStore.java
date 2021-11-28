/*
 * This software Copyright by the RPTools.net development team, and
 * licensed under the Affero GPL Version 3 or, at your option, any later
 * version.
 *
 * MapTool Source Code is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * You should have received a copy of the GNU Affero General Public
 * License * along with this source Code.  If not, please visit
 * <http://www.gnu.org/licenses/> and specifically the Affero license
 * text at <http://www.gnu.org/licenses/agpl.html>.
 */
package net.rptools.maptool.model.gamedata;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import net.rptools.lib.MD5Key;
import net.rptools.maptool.model.Asset;
import net.rptools.maptool.model.gamedata.data.DataType;
import net.rptools.maptool.model.gamedata.data.DataValue;
import net.rptools.maptool.model.gamedata.data.DataValueFactory;
import net.rptools.maptool.model.gamedata.proto.GameDataDto;
import net.rptools.maptool.model.gamedata.proto.GameDataValueDto;
import org.apache.log4j.Logger;

/** Class that implements the DataStore interface. */
public class MemoryDataStore implements DataStore {

  /** Lock used for accessing/updating the data store. */
  private ReentrantLock lock = new ReentrantLock();

  private record PropertyTypeNamespace(String propertyType, String namespace) {}

  /** Class used to cache definitions. */
  private final Map<String, Set<String>> propertyTypeNamespaceMap = new HashMap<>();

  private final Map<PropertyTypeNamespace, Map<String, DataValue>> namespaceDataMap =
      new HashMap<>();

  private final Map<PropertyTypeNamespace, Map<String, Set<String>>> tagPropertyMap =
      new HashMap<>();

  /** Class for logging. */
  private static final Logger log = Logger.getLogger(MemoryDataStore.class);

  /** Creates a new MemoryDataStore. */
  MemoryDataStore() {}

  /**
   * Returns if the namespace exists for the property type.
   *
   * @return if the namespace exists for the property type.
   */
  private boolean checkPropertyNamespace(String propertyType, String namespace) {
    try {
      lock.lock();
      if (propertyTypeNamespaceMap.containsKey(propertyType)) {
        var propertyTypeNamespaces = propertyTypeNamespaceMap.get(propertyType);
        return propertyTypeNamespaces.contains(namespace);
      }
      return false;
    } finally {
      lock.unlock();
    }
  }

  @Override
  public CompletableFuture<Set<String>> getPropertyTypes() {
    return CompletableFuture.supplyAsync(
        () -> {
          try {
            lock.lock();
            return new HashSet<>(propertyTypeNamespaceMap.keySet());
          } finally {
            lock.unlock();
          }
        });
  }

  @Override
  public CompletableFuture<Set<String>> getPropertyNamespaces(String type) {
    return CompletableFuture.supplyAsync(
        () -> {
          try {
            lock.lock();
            var propertyTypeNamespace = propertyTypeNamespaceMap.get(type);
            if (propertyTypeNamespace != null) {
              return new HashSet<>(propertyTypeNamespace);
            } else {
              return new HashSet<>();
            }
          } finally {
            lock.unlock();
          }
        });
  }

  @Override
  public CompletableFuture<Boolean> hasPropertyNamespace(String type, String namespace) {
    return CompletableFuture.completedFuture(checkPropertyNamespace(type, namespace));
  }

  /**
   * Returns the data value for the given property type, namespace and name. This will return null
   * if the data does not exist.
   *
   * @param type the property type.
   * @param namespace the property namespace.
   * @param name the property name.
   * @return the data value.
   */
  private DataValue getData(String type, String namespace, String name) {
    try {
      lock.lock();
      var propertyTypeNamespace = new PropertyTypeNamespace(type, namespace);
      var values = namespaceDataMap.get(propertyTypeNamespace);
      return values != null ? values.get(name) : null;
    } finally {
      lock.unlock();
    }
  }

  @Override
  public CompletableFuture<DataType> getPropertyDataType(
      String type, String namespace, String name) {
    return CompletableFuture.supplyAsync(
        () -> {
          try {
            lock.lock();
            var data = getData(type, namespace, name);
            return data == null ? DataType.UNDEFINED : data.getDataType();
          } finally {
            lock.unlock();
          }
        });
  }

  @Override
  public CompletableFuture<Map<String, DataType>> getPropertyDataTypeMap(
      String type, String namespace) {
    return CompletableFuture.supplyAsync(
        () -> {
          try {
            lock.lock();
            var dataTypeMap = new HashMap<String, DataType>();
            var values = namespaceDataMap.get(new PropertyTypeNamespace(type, namespace));
            if (values != null) {
              for (var value : values.values()) {
                dataTypeMap.put(value.getName(), value.getDataType());
              }
            }
            return dataTypeMap;
          } finally {
            lock.unlock();
          }
        });
  }

  @Override
  public CompletableFuture<Boolean> hasProperty(String type, String namespace, String name) {
    return CompletableFuture.supplyAsync(
        () -> {
          try {
            lock.lock();
            var def = getData(type, namespace, name);
            if (def != null) {
              return Boolean.TRUE;
            } else {
              return Boolean.FALSE;
            }
          } finally {
            lock.unlock();
          }
        });
  }

  @Override
  public CompletableFuture<Boolean> isPropertyDefined(String type, String namespace, String name) {
    return CompletableFuture.supplyAsync(
        () -> {
          try {
            lock.lock();
            var data = getData(type, namespace, name);
            if (data != null) {
              return !data.isUndefined();
            } else {
              return Boolean.FALSE;
            }
          } finally {
            lock.unlock();
          }
        });
  }

  @Override
  public CompletableFuture<DataValue> getProperty(String type, String namespace, String name) {
    return CompletableFuture.supplyAsync(
        () -> {
          try {
            lock.lock();
            var prop = getData(type, namespace, name);
            return Objects.requireNonNullElseGet(prop, () -> DataValueFactory.undefined(name));
          } finally {
            lock.unlock();
          }
        });
  }

  @Override
  public CompletableFuture<Set<DataValue>> getProperties(String type, String namespace) {
    return CompletableFuture.supplyAsync(
        () -> {
          try {
            lock.lock();
            var values = namespaceDataMap.get(new PropertyTypeNamespace(type, namespace));
            if (values != null) {
              return Set.copyOf(values.values());
            } else {
              return Set.of();
            }
          } finally {
            lock.unlock();
          }
        });
  }

  @Override
  public CompletableFuture<Set<DataValue>> getProperties(
      String type, String namespace, String tag) {
    return CompletableFuture.supplyAsync(
        () -> {
          try {
            lock.lock();
            var propertyType = new PropertyTypeNamespace(type, namespace);
            var tags = tagPropertyMap.get(propertyType);
            var data = namespaceDataMap.get(propertyType);
            if (tags != null && tags.containsKey(tag) && data != null) {
              return tags.get(tag).stream()
                  .filter(data::containsKey)
                  .map(data::get)
                  .collect(Collectors.toSet());
            } else {
              return Set.of();
            }
          } finally {
            lock.unlock();
          }
        });
  }

  /**
   * Method to set the data value for the given property type, namespace and name.
   *
   * @param type the property type.
   * @param namespace the property namespace.
   * @param value the data value.
   * @param overwriteTags whether to overwrite the tags.
   */
  private DataValue setData(String type, String namespace, DataValue value, boolean overwriteTags) {
    try {
      lock.lock();
      if (!checkPropertyNamespace(type, namespace)) {
        throw InvalidDataOperation.createNamespaceDoesNotExist(namespace, type);
      }

      var existing = getData(type, namespace, value.getName());
      DataValue setValue;

      if (overwriteTags) {
        setValue = value;
      } else {
        if (existing != null) {
          setValue = value.withTags(existing.getTags());
        } else {
          setValue = value.withTags(Set.of());
        }
      }

      // If no value exists we can put anything there, if a value exists we have to check type
      // is correct
      var dataMap =
          namespaceDataMap.computeIfAbsent(
              new PropertyTypeNamespace(type, namespace), k -> new ConcurrentHashMap<>());
      if (existing == null) {

        dataMap.put(value.getName(), value);
      } else {
        var newValue = DataType.convert(value, existing.getDataType());
        dataMap.put(newValue.getName(), newValue);
        setValue = newValue;
      }

      if (overwriteTags) {
        adjustTags(
            type,
            namespace,
            value.getName(),
            existing != null ? existing.getTags() : Set.of(),
            value.getTags());
      }

      return setValue;
    } finally {
      lock.unlock();
    }
  }

  /**
   * Adjust the tags for the given property type, namespace and name.
   *
   * @param type the property type.
   * @param namespace the property namespace.
   * @param name the property name.
   * @param oldTags the old tags.
   * @param newTags the new tags.
   */
  private void adjustTags(
      String type, String namespace, String name, Set<String> oldTags, Set<String> newTags) {
    try {
      lock.lock();
      var removeTags = new HashSet<>(oldTags);
      removeTags.removeAll(newTags);

      var addTags = new HashSet<>(newTags);
      addTags.removeAll(oldTags);

      var propertyType = new PropertyTypeNamespace(type, namespace);
      var tagMap = tagPropertyMap.computeIfAbsent(propertyType, k -> new HashMap<>());
      for (var tag : removeTags) {
        if (tagMap.containsKey(tag)) {
          tagMap.get(tag).remove(name);
        }
      }

      for (var tag : addTags) {
        var tagSet = tagMap.computeIfAbsent(tag, k -> new HashSet<>());
        tagSet.add(name);
      }
    } finally {
      lock.unlock();
    }
  }

  @Override
  public CompletableFuture<DataValue> setProperty(String type, String namespace, DataValue value) {
    return CompletableFuture.supplyAsync(() -> setData(type, namespace, value, false));
  }

  @Override
  public CompletableFuture<DataValue> setPropertyAndTags(
      String type, String namespace, DataValue value) {
    return setProperty(type, namespace, value, value.getTags());
  }

  @Override
  public CompletableFuture<DataValue> setProperty(
      String type, String namespace, DataValue value, Set<String> tags) {
    return null;
  }

  @Override
  public CompletableFuture<DataValue> setLongProperty(
      String type, String namespace, String name, long value) {
    return CompletableFuture.supplyAsync(
        () -> setData(type, namespace, DataValueFactory.fromLong(name, value), false));
  }

  @Override
  public CompletableFuture<DataValue> setLongProperty(
      String type, String namespace, String name, long value, Set<String> tags) {
    return CompletableFuture.supplyAsync(
        () ->
            setData(type, namespace, DataValueFactory.fromLong(name, value).withTags(tags), true));
  }

  @Override
  public CompletableFuture<DataValue> setDoubleProperty(
      String type, String namespace, String name, double value) {
    return CompletableFuture.supplyAsync(
        () -> setData(type, namespace, DataValueFactory.fromDouble(name, value), false));
  }

  @Override
  public CompletableFuture<DataValue> setDoubleProperty(
      String type, String namespace, String name, double value, Set<String> tags) {
    return CompletableFuture.supplyAsync(
        () ->
            setData(
                type, namespace, DataValueFactory.fromDouble(name, value).withTags(tags), true));
  }

  @Override
  public CompletableFuture<DataValue> setStringProperty(
      String type, String namespace, String name, String value) {
    return CompletableFuture.supplyAsync(
        () -> setData(type, namespace, DataValueFactory.fromString(name, value), false));
  }

  @Override
  public CompletableFuture<DataValue> setStringProperty(
      String type, String namespace, String name, String value, Set<String> tags) {
    return CompletableFuture.supplyAsync(
        () ->
            setData(
                type, namespace, DataValueFactory.fromString(name, value).withTags(tags), true));
  }

  @Override
  public CompletableFuture<DataValue> setBooleanProperty(
      String type, String namespace, String name, boolean value) {
    return CompletableFuture.supplyAsync(
        () -> setData(type, namespace, DataValueFactory.fromBoolean(name, value), false));
  }

  @Override
  public CompletableFuture<DataValue> setBooleanProperty(
      String type, String namespace, String name, boolean value, Set<String> tags) {
    return CompletableFuture.supplyAsync(
        () ->
            setData(
                type, namespace, DataValueFactory.fromBoolean(name, value).withTags(tags), true));
  }

  @Override
  public CompletableFuture<DataValue> setJsonArrayProperty(
      String type, String namespace, String name, JsonArray value) {
    return CompletableFuture.supplyAsync(
        () -> setData(type, namespace, DataValueFactory.fromJsonArray(name, value), false));
  }

  @Override
  public CompletableFuture<DataValue> setJsonArrayProperty(
      String type, String namespace, String name, JsonArray value, Set<String> tags) {
    return CompletableFuture.supplyAsync(
        () ->
            setData(
                type, namespace, DataValueFactory.fromJsonArray(name, value).withTags(tags), true));
  }

  @Override
  public CompletableFuture<DataValue> setJsonObjectProperty(
      String type, String namespace, String name, JsonObject value) {
    return CompletableFuture.supplyAsync(
        () -> setData(type, namespace, DataValueFactory.fromJsonObject(name, value), false));
  }

  @Override
  public CompletableFuture<DataValue> setJsonObjectProperty(
      String type, String namespace, String name, JsonObject value, Set<String> tags) {
    return CompletableFuture.supplyAsync(
        () ->
            setData(
                type,
                namespace,
                DataValueFactory.fromJsonObject(name, value).withTags(tags),
                true));
  }

  @Override
  public CompletableFuture<DataValue> setAssetProperty(
      String type, String namespace, String name, Asset value) {
    return CompletableFuture.supplyAsync(
        () -> setData(type, namespace, DataValueFactory.fromAsset(name, value), false));
  }

  @Override
  public CompletableFuture<DataValue> setAssetProperty(
      String type, String namespace, String name, Asset value, Set<String> tags) {
    return CompletableFuture.supplyAsync(
        () ->
            setData(type, namespace, DataValueFactory.fromAsset(name, value).withTags(tags), true));
  }

  @Override
  public CompletableFuture<Void> removeProperty(String type, String namespace, String name) {
    return CompletableFuture.supplyAsync(
        () -> {
          try {
            lock.lock();
            var key = new PropertyTypeNamespace(type, namespace);
            var dataMap = namespaceDataMap.get(key);
            if (dataMap != null) {
              var data = dataMap.get(name);
              if (data != null) {
                dataMap.remove(name);
                var tagMap = tagPropertyMap.get(key);
                for (String tag : data.getTags()) {
                  tagMap.get(tag).remove(name);
                }
              }
            }
            return null;
          } finally {
            lock.unlock();
          }
        });
  }

  /**
   * Creates a new namespace populated with the given data.
   *
   * @param propertyType the property type.
   * @param namespace the namespace.
   * @param initialData the initial data.
   */
  private void createDataNamespace(
      String propertyType, String namespace, Collection<DataValue> initialData) {

    try {
      lock.lock();
      Set<String> namespaces =
          propertyTypeNamespaceMap.computeIfAbsent(propertyType, k -> new HashSet<>());

      namespaces.add(namespace);

      var dataMap =
          namespaceDataMap.computeIfAbsent(
              new PropertyTypeNamespace(propertyType, namespace), k -> new HashMap<>());

      for (var dataValue : initialData) {
        dataMap.put(dataValue.getName(), dataValue);
      }
    } finally {
      lock.unlock();
    }
  }

  @Override
  public CompletableFuture<Void> createNamespace(String propertyType, String namespace) {
    return CompletableFuture.supplyAsync(
        () -> {
          createDataNamespace(propertyType, namespace, List.of());
          return null;
        });
  }

  @Override
  public CompletableFuture<Void> createNamespaceWithInitialData(
      String propertyType, String namespace, Collection<DataValue> initialData) {
    return CompletableFuture.supplyAsync(
        () -> {
          createDataNamespace(propertyType, namespace, initialData);
          return null;
        });
  }

  @Override
  public CompletableFuture<Void> createNamespaceWithTypes(
      String propertyType, String namespace, Map<String, DataType> dataTypes) {
    return CompletableFuture.supplyAsync(
        () -> {
          createDataNamespace(
              propertyType,
              namespace,
              dataTypes.entrySet().stream()
                  .map(e -> DataValueFactory.undefined(e.getKey(), e.getValue()))
                  .toList());
          return null;
        });
  }

  @Override
  public CompletableFuture<GameDataDto> toDto(String type, String namespace) {
    return CompletableFuture.supplyAsync(
        () -> {
          try {
            lock.lock();
            var builder = GameDataDto.newBuilder();
            builder.setType(type);
            builder.setNamespace(namespace);
            for (var data : getProperties(type, namespace).join()) {
              var dataDto = gameValueToDto(data);
              builder.addValues(dataDto);
            }
            return builder.build();
          } finally {
            lock.unlock();
          }
        });
  }

  @Override
  public CompletableFuture<DataValue> setTags(
      String type, String namespace, String name, Set<String> tags) {
    return CompletableFuture.supplyAsync(
        () -> {
          try {
            lock.lock();
            var data = getData(type, namespace, name);
            if (data == null) {
              throw InvalidDataOperation.createUndefined(name);
            }
            return setData(type, namespace, data.withTags(tags), true);
          } finally {
            lock.unlock();
          }
        });
  }

  @Override
  public CompletableFuture<DataValue> addTags(
      String type, String namespace, String name, Set<String> tags) {
    return CompletableFuture.supplyAsync(
        () -> {
          try {
            lock.lock();
            var data = getData(type, namespace, name);
            if (data == null) {
              throw InvalidDataOperation.createUndefined(name);
            }
            var tagSet = new HashSet<String>(data.getTags());
            tagSet.addAll(tags);
            return setData(type, namespace, data.withTags(tagSet), true);
          } finally {
            lock.unlock();
          }
        });
  }

  @Override
  public CompletableFuture<DataValue> removeTags(
      String type, String namespace, String name, Set<String> tags) {
    return CompletableFuture.supplyAsync(
        () -> {
          try {
            lock.lock();
            var data = getData(type, namespace, name);
            var resultTags = new HashSet<>(data.getTags());
            resultTags.removeAll(tags);
            return setData(type, namespace, data.withTags(resultTags), true);
          } finally {
            lock.unlock();
          }
        });
  }

  @Override
  public CompletableFuture<GameDataValueDto> toDto(DataValue data) {
    return CompletableFuture.supplyAsync(() -> gameValueToDto(data));
  }

  /**
   * Converts a {@link DataValue} to a {@link GameDataValueDto}.
   *
   * @param data The {@link DataValue} to convert.
   * @return The converted {@link GameDataValueDto}.
   */
  private GameDataValueDto gameValueToDto(DataValue data) {
    try {
      lock.lock();
      var gson = new Gson();
      var dataBuilder = GameDataValueDto.newBuilder();
      dataBuilder.setName(data.getName());
      switch (data.getDataType()) {
        case LONG -> {
          if (data.isUndefined()) {
            dataBuilder.setUndefinedLongValue(true);
          } else {
            dataBuilder.setLongValue(data.asLong());
          }
        }
        case DOUBLE -> {
          if (data.isUndefined()) {
            dataBuilder.setUndefinedDoubleValue(true);
          } else {
            dataBuilder.setDoubleValue(data.asDouble());
          }
        }
        case BOOLEAN -> {
          if (data.isUndefined()) {
            dataBuilder.setUndefinedBooleanValue(true);
          } else {
            dataBuilder.setBooleanValue(data.asBoolean());
          }
        }
        case STRING -> {
          if (data.isUndefined()) {
            dataBuilder.setUndefinedStringValue(true);
          } else {
            dataBuilder.setStringValue(data.asString());
          }
        }
        case JSON_ARRAY -> {
          if (data.isUndefined()) {
            dataBuilder.setUndefinedJsonArrayValue(true);
          } else {
            dataBuilder.setJsonValue(gson.toJson(data.asJsonArray()));
          }
        }
        case JSON_OBJECT -> {
          if (data.isUndefined()) {
            dataBuilder.setUndefinedJsonObjectValue(true);
          } else {
            dataBuilder.setJsonValue(gson.toJson(data.asJsonObject()));
          }
        }
        case ASSET -> {
          if (data.isUndefined()) {
            dataBuilder.setUndefinedAssetValue(true);
          } else {
            dataBuilder.setAssetValue(data.asAsset().getMD5Key().toString());
          }
        }
        case UNDEFINED -> dataBuilder.setUndefinedValue(true);
      }
      dataBuilder.addAllTags(data.getTags());

      return dataBuilder.build();
    } finally {
      lock.unlock();
    }
  }

  @Override
  public CompletableFuture<Set<MD5Key>> getAssets() {
    return CompletableFuture.supplyAsync(
        () -> {
          try {
            lock.lock();
            return namespaceDataMap.values().stream()
                .flatMap(m -> m.values().stream())
                .filter(d -> d.getDataType() == DataType.ASSET)
                .map(a -> a.asAsset().getMD5Key())
                .collect(Collectors.toSet());
          } finally {
            lock.unlock();
          }
        });
  }

  @Override
  public void clear() {
    try {
      lock.lock();
      propertyTypeNamespaceMap.clear();
      namespaceDataMap.clear();
    } finally {
      lock.unlock();
    }
  }

  @Override
  public CompletableFuture<Void> clearNamespace(String propertyType, String namespace) {
    return CompletableFuture.supplyAsync(
        () -> {
          try {
            lock.lock();
            namespaceDataMap.remove(new PropertyTypeNamespace(propertyType, namespace));
            return null;
          } finally {
            lock.unlock();
          }
        });
  }
}
