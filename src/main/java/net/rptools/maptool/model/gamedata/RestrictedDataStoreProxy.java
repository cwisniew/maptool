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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import net.rptools.lib.MD5Key;
import net.rptools.maptool.model.Asset;
import net.rptools.maptool.model.gamedata.data.DataType;
import net.rptools.maptool.model.gamedata.data.DataValue;
import net.rptools.maptool.model.gamedata.proto.GameDataDto;
import net.rptools.maptool.model.gamedata.proto.GameDataValueDto;
import org.apache.log4j.Logger;

class RestrictedDataStoreProxy implements DataStore {

  private final DataStore dataStore;
  /** Class for logging. */
  private static final Logger log = Logger.getLogger(RestrictedDataStoreProxy.class);

  @Override
  public CompletableFuture<Set<String>> getPropertyTypes() {
    return dataStore.getPropertyTypes();
  }

  @Override
  public CompletableFuture<Set<String>> getPropertyNamespaces(String type) {
    return dataStore.getPropertyNamespaces(type);
  }

  @Override
  public CompletableFuture<Boolean> hasPropertyNamespace(String type, String namespace) {
    return dataStore.hasPropertyNamespace(type, namespace);
  }

  @Override
  public CompletableFuture<DataType> getPropertyDataType(
      String type, String namespace, String name) {
    return dataStore.getPropertyDataType(type, namespace, name);
  }

  @Override
  public CompletableFuture<Map<String, DataType>> getPropertyDataTypeMap(
      String type, String namespace) {
    return dataStore.getPropertyDataTypeMap(type, namespace);
  }

  @Override
  public CompletableFuture<Boolean> hasProperty(String type, String namespace, String name) {
    return dataStore.hasProperty(type, namespace, name);
  }

  @Override
  public CompletableFuture<Boolean> isPropertyDefined(String type, String namespace, String name) {
    return dataStore.isPropertyDefined(type, namespace, name);
  }

  @Override
  public CompletableFuture<DataValue> getProperty(String type, String namespace, String name) {
    return dataStore.getProperty(type, namespace, name);
  }

  @Override
  public CompletableFuture<Set<DataValue>> getProperties(String type, String namespace) {
    return dataStore.getProperties(type, namespace);
  }

  @Override
  public CompletableFuture<DataValue> setProperty(String type, String namespace, DataValue value) {
    if (new DataStoreReserved().isReserved(type, namespace)) {
      throw InvalidDataOperation.createReservedTypeOrNamespace(type, namespace);
    }
    return dataStore.setProperty(type, namespace, value);
  }

  @Override
  public CompletableFuture<DataValue> setLongProperty(
      String type, String namespace, String name, long value) {
    if (new DataStoreReserved().isReserved(type, namespace)) {
      throw InvalidDataOperation.createReservedTypeOrNamespace(type, namespace);
    }
    return dataStore.setLongProperty(type, namespace, name, value);
  }

  @Override
  public CompletableFuture<DataValue> setDoubleProperty(
      String type, String namespace, String name, double value) {
    if (new DataStoreReserved().isReserved(type, namespace)) {
      throw InvalidDataOperation.createReservedTypeOrNamespace(type, namespace);
    }
    return dataStore.setDoubleProperty(type, namespace, name, value);
  }

  @Override
  public CompletableFuture<DataValue> setStringProperty(
      String type, String namespace, String name, String value) {
    if (new DataStoreReserved().isReserved(type, namespace)) {
      throw InvalidDataOperation.createReservedTypeOrNamespace(type, namespace);
    }
    return dataStore.setStringProperty(type, namespace, name, value);
  }

  @Override
  public CompletableFuture<DataValue> setBooleanProperty(
      String type, String namespace, String name, boolean value) {
    if (new DataStoreReserved().isReserved(type, namespace)) {
      throw InvalidDataOperation.createReservedTypeOrNamespace(type, namespace);
    }
    return dataStore.setBooleanProperty(type, namespace, name, value);
  }

  @Override
  public CompletableFuture<DataValue> setJsonArrayProperty(
      String type, String namespace, String name, JsonArray value) {
    if (new DataStoreReserved().isReserved(type, namespace)) {
      throw InvalidDataOperation.createReservedTypeOrNamespace(type, namespace);
    }
    return dataStore.setJsonArrayProperty(type, namespace, name, value);
  }

  @Override
  public CompletableFuture<DataValue> setJsonObjectProperty(
      String type, String namespace, String name, JsonObject value) {
    if (new DataStoreReserved().isReserved(type, namespace)) {
      throw InvalidDataOperation.createReservedTypeOrNamespace(type, namespace);
    }
    return dataStore.setJsonObjectProperty(type, namespace, name, value);
  }

  @Override
  public CompletableFuture<DataValue> setAssetProperty(
      String type, String namespace, String name, Asset value) {
    if (new DataStoreReserved().isReserved(type, namespace)) {
      throw InvalidDataOperation.createReservedTypeOrNamespace(type, namespace);
    }
    return dataStore.setAssetProperty(type, namespace, name, value);
  }

  @Override
  public CompletableFuture<Void> removeProperty(String type, String namespace, String name) {
    if (new DataStoreReserved().isReserved(type, namespace)) {
      throw InvalidDataOperation.createReservedTypeOrNamespace(type, namespace);
    }
    return dataStore.removeProperty(type, namespace, name);
  }

  @Override
  public CompletableFuture<Void> createNamespace(String propertyType, String namespace) {
    if (new DataStoreReserved().isReserved(propertyType, namespace)) {
      throw InvalidDataOperation.createReservedTypeOrNamespace(propertyType, namespace);
    }
    return dataStore.createNamespace(propertyType, namespace);
  }

  @Override
  public CompletableFuture<Void> createNamespaceWithInitialData(
      String propertyType, String namespace, Collection<DataValue> initialData) {
    if (new DataStoreReserved().isReserved(propertyType, namespace)) {
      throw InvalidDataOperation.createReservedTypeOrNamespace(propertyType, namespace);
    }
    return dataStore.createNamespaceWithInitialData(propertyType, namespace, initialData);
  }

  @Override
  public CompletableFuture<Void> createNamespaceWithTypes(
      String propertyType, String namespace, Map<String, DataType> dataTypes) {
    if (new DataStoreReserved().isReserved(propertyType, namespace)) {
      throw InvalidDataOperation.createReservedTypeOrNamespace(propertyType, namespace);
    }
    return dataStore.createNamespaceWithTypes(propertyType, namespace, dataTypes);
  }

  @Override
  public CompletableFuture<GameDataDto> toDto(String type, String namespace) {
    return dataStore.toDto(type, namespace);
  }

  @Override
  public CompletableFuture<Set<MD5Key>> getAssets() {
    return dataStore.getAssets();
  }

  @Override
  public void clear() {
    throw InvalidDataOperation.createNotOnRestrictedStore();
  }

  @Override
  public CompletableFuture<Void> clearNamespace(String propertyType, String namespace) {
    if (new DataStoreReserved().isReserved(propertyType, namespace)) {
      throw InvalidDataOperation.createReservedTypeOrNamespace(propertyType, namespace);
    }
    return dataStore.clearNamespace(propertyType, namespace);
  }

  @Override
  public CompletableFuture<GameDataValueDto> toDto(DataValue value) {
    return dataStore.toDto(value);
  }

  public RestrictedDataStoreProxy(DataStore delegate) {
    this.dataStore = delegate;
  }
}
