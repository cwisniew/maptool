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

/** The IntegerDataValue class represents a data value that is a Boolean. */
public final class BooleanDataValue implements DataValue {

  /** The name of the value. */
  private final String name;
  /** The value. */
  private final boolean value;

  /** Has no value been set. */
  private final boolean undefined;

  /** The tags for this data value. */
  private final Set<String> tags;

  /**
   * Creates a new BooleanDataValue with the given value and tags.
   *
   * @param name The name of the value.
   * @param value The value.
   * @param tags The tags for this data value.
   */
  BooleanDataValue(String name, boolean value, Set<String> tags) {
    this.name = name;
    this.value = value;
    this.undefined = false;
    this.tags = Set.copyOf(tags);
  }

  /**
   * Creates a new BooleanDataValue with the given value and na tags.
   *
   * @param name The name of the value.
   * @param value The value.
   */
  BooleanDataValue(String name, boolean value) {
    this(name, value, Set.of());
  }

  /**
   * Creates a new undefined BooleanDataValue with the specified tags.
   *
   * @param name The name of the value.
   * @oaram tags The tags for this data value.
   */
  BooleanDataValue(String name, Set<String> tags) {
    this.name = name;
    this.value = false;
    this.undefined = true;
    this.tags = Set.copyOf(tags);
  }

  /**
   * Creates a new undefined BooleanDataValue with no tags.
   *
   * @param name The name of the value.
   */
  BooleanDataValue(String name) {
    this(name, Set.of());
  }

  /**
   * Creates a new BooleanDataValue from the values of the passed in DataValue and new tags.
   *
   * @param data Value The DataValue to copy.
   * @param tags The new tags for the new DataValue.
   */
  private BooleanDataValue(BooleanDataValue data, Set<String> tags) {
    name = data.name;
    value = data.value;
    undefined = data.undefined;
    this.tags = Set.copyOf(tags);
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public DataType getDataType() {
    return DataType.BOOLEAN;
  }

  @Override
  public Set<String> getTags() {
    return tags;
  }

  @Override
  public DataValue withTags(Set<String> tags) {
    return new BooleanDataValue(this, tags);
  }

  @Override
  public boolean canBeConvertedTo(DataType dataType) {
    if (undefined) {
      return false;
    } else {
      return switch (dataType) {
        case LONG, DOUBLE, BOOLEAN, STRING, JSON_ARRAY -> true;
        case JSON_OBJECT, UNDEFINED, ASSET -> false;
      };
    }
  }

  @Override
  public long asLong() {
    if (undefined) {
      throw InvalidDataOperation.createUndefined(name);
    } else {
      return value ? 1 : 0;
    }
  }

  @Override
  public double asDouble() {
    if (undefined) {
      throw InvalidDataOperation.createUndefined(name);
    } else {
      return value ? 1.0 : 0.0;
    }
  }

  @Override
  public String asString() {
    if (undefined) {
      throw InvalidDataOperation.createUndefined(name);
    } else {
      return Boolean.toString(value);
    }
  }

  @Override
  public boolean asBoolean() {
    if (undefined) {
      throw InvalidDataOperation.createUndefined(name);
    } else {
      return value;
    }
  }

  @Override
  public JsonArray asJsonArray() {
    if (undefined) {
      throw InvalidDataOperation.createUndefined(name);
    } else {
      var array = new JsonArray();
      array.add(value);
      return array;
    }
  }

  @Override
  public JsonObject asJsonObject() {
    if (undefined) {
      throw InvalidDataOperation.createUndefined(name);
    } else {
      throw InvalidDataOperation.createInvalidConversion(DataType.BOOLEAN, DataType.JSON_OBJECT);
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
      throw InvalidDataOperation.createInvalidConversion(DataType.BOOLEAN, DataType.ASSET);
    }
  }

  @Override
  public String toString() {
    return "BooleanDataValue{"
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
