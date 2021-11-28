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
package net.rptools.maptool.model.gamedata.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import net.rptools.maptool.model.Asset;
import net.rptools.maptool.model.gamedata.InvalidDataOperation;

/** The IntegerDataValue class represents a data value that is a list. */
public final class JsonArrayDataValue implements DataValue {

  /** The name of the value. */
  private final String name;
  /** The value. */
  private final JsonArray values;

  /** Has no value been set. */
  private final boolean undefined;

  /** The tags of the value. */
  private final Set<String> tags;

  /**
   * Creates a new JsonArrayDataValue with the given value and tags.
   *
   * @note You can't store an Asset in a JsonArray you will have to convert it to another value
   *     first.
   * @param name the name of the value.
   * @param values the values.
   * @param tags the tags.
   * @throws InvalidDataOperation if the values can not be stored in a JsonArray.
   */
  JsonArrayDataValue(String name, Collection<DataValue> values, Set<String> tags) {
    var array = new JsonArray();
    values.forEach(
        v -> {
          switch (v.getDataType()) {
            case BOOLEAN -> array.add(v.asBoolean());
            case LONG -> array.add(v.asLong());
            case DOUBLE -> array.add(v.asDouble());
            case STRING -> array.add(v.asString());
            case JSON_ARRAY -> array.add(v.asJsonArray());
            case JSON_OBJECT -> array.add(v.asJsonObject());
            case UNDEFINED -> array.add(JsonNull.INSTANCE);
          }
        });
    this.name = name;
    this.values = array;
    this.undefined = false;
    this.tags = Set.copyOf(tags);
  }

  /**
   * Creates a new JsonArrayDataValue with the given value and no tags.
   *
   * @note You can't store an Asset in a JsonArray you will have to convert it to another value
   *     first.
   * @param name the name of the value.
   * @param values the values.
   * @throws InvalidDataOperation if the values can not be stored in a JsonArray.
   */
  JsonArrayDataValue(String name, Collection<DataValue> values) {
    this(name, values, Set.of());
  }

  /**
   * Creates a new JsonArrayDataValue with the given value and tags.
   *
   * @param name the name of the value.
   * @param values the values.
   * @param tags the tags.
   */
  JsonArrayDataValue(String name, JsonArray values, Set<String> tags) {
    this.name = name;
    this.values = values != null ? values.deepCopy() : new JsonArray();
    this.undefined = false;
    this.tags = Set.copyOf(tags);
  }

  /**
   * Creates a new JsonArrayDataValue with the given value and no tags.
   *
   * @param name the name of the value.
   * @param values the values.
   */
  JsonArrayDataValue(String name, JsonArray values) {
    this(name, values, Set.of());
  }

  /**
   * Creates a new JsonArrayDataValue wit an undefined value with the specified tags.
   *
   * @param name the name of the value.
   * @param tags the tags.
   */
  JsonArrayDataValue(String name, Set<String> tags) {
    this.name = name;
    this.values = null;
    this.undefined = true;
    this.tags = Set.copyOf(tags);
  }

  /**
   * Creates a new JsonArrayDataValue wit an undefined value with no tags.
   *
   * @param name the name of the value.
   */
  JsonArrayDataValue(String name) {
    this(name, new HashSet<String>());
  }

  /**
   * Creates a new JsonArrayDataValue with the given value and new tags.
   *
   * @param data Value the value.
   * @param tags the tags.
   */
  private JsonArrayDataValue(JsonArrayDataValue data, Set<String> tags) {
    name = data.name;
    values = data.values != null ? data.values.deepCopy() : null;
    undefined = data.undefined;
    this.tags = Set.copyOf(tags);
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public DataType getDataType() {
    return DataType.JSON_ARRAY;
  }

  @Override
  public Set<String> getTags() {
    return tags;
  }

  @Override
  public DataValue withTags(Set<String> tags) {
    return new JsonArrayDataValue(this, tags);
  }

  @Override
  public boolean canBeConvertedTo(DataType dataType) {
    if (undefined) {
      return false;
    } else {
      return switch (dataType) {
        case LONG, DOUBLE, BOOLEAN, STRING, JSON_OBJECT, UNDEFINED -> false;
        case JSON_ARRAY, ASSET -> true;
      };
    }
  }

  @Override
  public long asLong() {
    if (undefined) {
      throw InvalidDataOperation.createUndefined(name);
    } else {
      throw InvalidDataOperation.createInvalidConversion(DataType.JSON_ARRAY, DataType.LONG);
    }
  }

  @Override
  public double asDouble() {
    if (undefined) {
      throw InvalidDataOperation.createUndefined(name);
    } else {
      throw InvalidDataOperation.createInvalidConversion(DataType.JSON_ARRAY, DataType.DOUBLE);
    }
  }

  @Override
  public String asString() {
    if (undefined) {
      throw InvalidDataOperation.createUndefined(name);
    } else {
      throw InvalidDataOperation.createInvalidConversion(DataType.JSON_ARRAY, DataType.STRING);
    }
  }

  @Override
  public boolean asBoolean() {
    if (undefined) {
      throw InvalidDataOperation.createUndefined(name);
    } else {
      throw InvalidDataOperation.createInvalidConversion(DataType.JSON_ARRAY, DataType.BOOLEAN);
    }
  }

  @Override
  public JsonArray asJsonArray() {
    if (undefined) {
      throw InvalidDataOperation.createUndefined(name);
    } else {
      return values.deepCopy();
    }
  }

  @Override
  public JsonObject asJsonObject() {
    if (undefined) {
      throw InvalidDataOperation.createUndefined(name);
    } else {
      throw InvalidDataOperation.createInvalidConversion(DataType.JSON_ARRAY, DataType.JSON_OBJECT);
    }
  }

  @Override
  public boolean isUndefined() {
    return undefined;
  }

  @Override
  public Asset asAsset() {
    if (undefined) {
      throw InvalidDataOperation.createUndefined(name);
    } else {
      throw InvalidDataOperation.createInvalidConversion(DataType.JSON_ARRAY, DataType.ASSET);
    }
  }

  @Override
  public String toString() {
    return "JsonArrayDataValue{"
        + "name='"
        + name
        + '\''
        + ", values="
        + values
        + ", undefined="
        + undefined
        + '}';
  }
}
