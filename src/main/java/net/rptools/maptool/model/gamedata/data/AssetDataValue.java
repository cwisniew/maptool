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
import net.rptools.lib.MD5Key;
import net.rptools.maptool.model.Asset;
import net.rptools.maptool.model.Asset.Type;
import net.rptools.maptool.model.AssetManager;
import net.rptools.maptool.model.gamedata.InvalidDataOperation;

/** Class to represent an asset data value. */
public class AssetDataValue implements DataValue {

  private final String name;
  private final MD5Key asset;
  private final boolean jsonObject;
  private final boolean jsonArray;
  private final boolean undefined;
  private final Set<String> tags;

  /**
   * Creates a new AssetDataValue from the given asset with the given tags.
   *
   * @param name the name of the value.
   * @param asset the asset to use.
   * @param tags the tags to use.
   */
  AssetDataValue(String name, Asset asset, Set<String> tags) {
    this.name = name;
    this.asset = asset.getMD5Key();
    if (asset.getType() == Type.JSON) {
      var json = asset.getDataAsJson();
      jsonObject = json.isJsonObject();
      jsonArray = json.isJsonArray();
    } else {
      jsonObject = false;
      jsonArray = false;
    }
    undefined = false;
    this.tags = Set.copyOf(tags);
  }

  /**
   * Creates a new AssetDataValue from the given AssetDataValue with the given tags.
   *
   * @param data the data to use.
   * @param tags the tags to use.
   */
  private AssetDataValue(AssetDataValue data, Set<String> tags) {
    name = data.name;
    asset = data.asset;
    jsonObject = data.jsonObject;
    jsonArray = data.jsonArray;
    undefined = data.undefined;
    this.tags = Set.copyOf(tags);
  }

  /**
   * Creates a new AssetDataValue from the given asset with no tags.
   *
   * @param name the name of the value.
   * @param asset the asset to use.
   */
  AssetDataValue(String name, Asset asset) {
    this(name, asset, Set.of());
  }

  /**
   * Creates a new AssetDataValue with an undefined value with the given tags.
   *
   * @param name the name of the value.
   * @param tags the tags to use.
   */
  AssetDataValue(String name, Set<String> tags) {
    this.name = name;
    this.asset = null;
    undefined = true;
    jsonObject = false;
    jsonArray = false;
    this.tags = Set.copyOf(tags);
  }

  /**
   * Creates a new AssetDataValue with and undefined value with no tags.
   *
   * @param name the name of the value.
   */
  AssetDataValue(String name) {
    this(name, Set.of());
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public DataType getDataType() {
    return DataType.ASSET;
  }

  @Override
  public Set<String> getTags() {
    return tags;
  }

  @Override
  public DataValue withTags(Set<String> tags) {
    return new AssetDataValue(this, tags);
  }

  @Override
  public boolean canBeConvertedTo(DataType dataType) {
    return switch (dataType) {
      case LONG, DOUBLE, BOOLEAN, UNDEFINED -> false;
      case STRING, ASSET -> true;
      case JSON_ARRAY -> jsonArray;
      case JSON_OBJECT -> jsonObject;
    };
  }

  @Override
  public long asLong() {
    if (undefined) {
      throw InvalidDataOperation.createUndefined(name);
    } else {
      throw InvalidDataOperation.createInvalidConversion(DataType.ASSET, DataType.LONG);
    }
  }

  @Override
  public double asDouble() {
    if (undefined) {
      throw InvalidDataOperation.createUndefined(name);
    } else {
      throw InvalidDataOperation.createInvalidConversion(DataType.ASSET, DataType.DOUBLE);
    }
  }

  @Override
  public String asString() {
    if (undefined) {
      throw InvalidDataOperation.createUndefined(name);
    } else {
      throw InvalidDataOperation.createInvalidConversion(DataType.ASSET, DataType.DOUBLE);
    }
  }

  @Override
  public boolean asBoolean() {
    if (undefined) {
      throw InvalidDataOperation.createUndefined(name);
    } else {
      throw InvalidDataOperation.createInvalidConversion(DataType.ASSET, DataType.BOOLEAN);
    }
  }

  @Override
  public JsonArray asJsonArray() {
    if (undefined) {
      throw InvalidDataOperation.createUndefined(name);
    } else if (!jsonArray) {
      throw InvalidDataOperation.createInvalidConversion(DataType.ASSET, DataType.BOOLEAN);
    } else {
      return AssetManager.getAssetAndWait(asset).getDataAsJson().getAsJsonArray();
    }
  }

  @Override
  public JsonObject asJsonObject() {
    if (undefined) {
      throw InvalidDataOperation.createUndefined(name);
    } else if (!jsonObject) {
      throw InvalidDataOperation.createInvalidConversion(DataType.ASSET, DataType.BOOLEAN);
    } else {
      return AssetManager.getAssetAndWait(asset).getDataAsJson().getAsJsonObject();
    }
  }

  @Override
  public boolean isUndefined() {
    return undefined;
  }

  @Override
  public Asset asAsset() {
    return AssetManager.getAssetAndWait(asset);
  }

  @Override
  public String toString() {
    return "AssetDataValue{" + "name='" + name + '\'' + ", asset=" + asset + '}';
  }
}
