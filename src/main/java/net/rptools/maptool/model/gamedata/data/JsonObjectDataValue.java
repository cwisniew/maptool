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
import com.google.gson.JsonObject;
import java.util.Set;
import net.rptools.maptool.model.Asset;
import net.rptools.maptool.model.gamedata.InvalidDataOperation;

/** The IntegerDataValue class represents a data value that is a map. */
public final class JsonObjectDataValue implements DataValue {

  /** The name of the value. */
  private final String name;
  /** The value. */
  private final JsonObject value;

  /** Has no value been set. */
  private final boolean undefined;

  /** The tag for the data value. */
  private final Set<String> tags;

  /**
   * Creates a new JsonObjectDataValue with the specified tags.
   *
   * @param name the name of the value
   * @param value the values.
   * @param tags the tags for the data value.
   */
  JsonObjectDataValue(String name, JsonObject value, Set<String> tags) {
    this.name = name;
    this.value = value != null ? value.deepCopy() : new JsonObject();
    this.undefined = false;
    this.tags = Set.copyOf(tags);
  }

  /**
   * Creates a new JsonObjectDataValue with an undefined value.
   *
   * @param name the name of the value
   * @param tags the tags for the data value.
   */
  JsonObjectDataValue(String name, Set<String> tags) {
    this.name = name;
    this.value = null;
    this.undefined = true;
    this.tags = Set.copyOf(tags);
  }

  /**
   * Creates a new JsonObjectDataValue with no tags.
   *
   * @param name the name of the value
   * @param value the value
   */
  JsonObjectDataValue(String name, JsonObject value) {
    this(name, value, Set.of());
  }

  /**
   * Creates a new undefined JsonObjectDataValue with no tags.
   *
   * @param name the name of the value
   */
  JsonObjectDataValue(String name) {
    this(name, Set.of());
  }

  /**
   * Creates a new JsonObjectDataValue with the specified value and new tags.
   *
   * @param data Value the value.
   * @param tags the tags for the data value.
   */
  private JsonObjectDataValue(JsonObjectDataValue data, Set<String> tags) {
    name = data.name;
    value = data.value != null ? data.value.deepCopy() : null;
    undefined = data.undefined;
    this.tags = Set.copyOf(tags);
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public DataType getDataType() {
    return DataType.JSON_OBJECT;
  }

  @Override
  public Set<String> getTags() {
    return tags;
  }

  @Override
  public DataValue withTags(Set<String> tags) {
    return new JsonObjectDataValue(this, tags);
  }

  @Override
  public boolean canBeConvertedTo(DataType dataType) {
    if (undefined) {
      return false;
    } else {
      return switch (dataType) {
        case LONG, DOUBLE, BOOLEAN, STRING, JSON_ARRAY, UNDEFINED, ASSET -> false;
        case JSON_OBJECT -> true;
      };
    }
  }

  @Override
  public long asLong() {
    if (undefined) {
      throw InvalidDataOperation.createUndefined(name);
    } else {
      throw InvalidDataOperation.createInvalidConversion(DataType.JSON_OBJECT, DataType.LONG);
    }
  }

  @Override
  public double asDouble() {
    if (undefined) {
      throw InvalidDataOperation.createUndefined(name);
    } else {
      throw InvalidDataOperation.createInvalidConversion(DataType.JSON_OBJECT, DataType.DOUBLE);
    }
  }

  @Override
  public String asString() {
    if (undefined) {
      throw InvalidDataOperation.createUndefined(name);
    } else {
      throw InvalidDataOperation.createInvalidConversion(DataType.JSON_OBJECT, DataType.STRING);
    }
  }

  @Override
  public boolean asBoolean() {
    if (undefined) {
      throw InvalidDataOperation.createUndefined(name);
    } else {
      throw InvalidDataOperation.createInvalidConversion(DataType.JSON_OBJECT, DataType.BOOLEAN);
    }
  }

  @Override
  public JsonArray asJsonArray() {
    if (undefined) {
      throw InvalidDataOperation.createUndefined(name);
    } else {
      throw InvalidDataOperation.createInvalidConversion(DataType.JSON_OBJECT, DataType.JSON_ARRAY);
    }
  }

  @Override
  public JsonObject asJsonObject() {
    if (undefined) {
      throw InvalidDataOperation.createUndefined(name);
    } else {
      return value.deepCopy();
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
      throw InvalidDataOperation.createInvalidConversion(DataType.JSON_OBJECT, DataType.ASSET);
    }
  }

  @Override
  public String toString() {
    return "JsonObjectDataValue{"
        + "name='"
        + name
        + '\''
        + ", value="
        + value
        + ", undefined="
        + undefined
        + '}';
  }
}
