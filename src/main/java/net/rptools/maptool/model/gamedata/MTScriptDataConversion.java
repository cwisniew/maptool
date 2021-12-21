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
import com.google.gson.JsonElement;
import java.math.BigDecimal;
import net.rptools.maptool.model.gamedata.data.DataType;
import net.rptools.maptool.model.gamedata.data.DataValue;
import net.rptools.maptool.model.gamedata.data.DataValueFactory;

/** Class for converting between GameData and MT Macro Script types. */
public class MTScriptDataConversion {

  /**
   * Converts a DataValue to a MT Script type. For Asset DataValues the asset handle is returned. If
   * you want to return the actual contents of an Asset DataValue use {@link
   * #convertToMTScriptDereferenceType(DataValue)}
   *
   * @param value The DataValue to convert.
   * @return The converted value.
   */
  public Object convertToMTScriptType(DataValue value) {
    if (value == null || value.isUndefined()) {
      return "";
    }

    return switch (value.getDataType()) {
      case LONG -> BigDecimal.valueOf(value.asLong());
      case DOUBLE -> BigDecimal.valueOf(value.asDouble());
      case STRING -> value.asString();
      case BOOLEAN -> value.asBoolean() ? BigDecimal.ONE : BigDecimal.ZERO;
      case JSON_OBJECT -> value.asJsonObject();
      case JSON_ARRAY -> value.asJsonArray();
      case ASSET -> "asset://" + value.asAsset().getMD5Key();
      case UNDEFINED -> "";
    };
  }

  /**
   * Converts a DataType to a MT Script type fetching the contents of an Asset and returning that
   * instead of just and asset handle where it makes sense. If the asset is a type that can not be
   * handled in MTScript then the asset handle will be returned.
   *
   * @param value The DataValue to convert.
   * @return The converted value.
   */
  public Object convertToMTScriptDereferenceType(DataValue value) {
    if (value != null && value.getDataType() == DataType.ASSET) {
      return switch (value.asAsset().getType()) {
        case HTML, TEXT, MARKDOWN, CSS, JAVASCRIPT, XML -> value.asAsset().getDataAsString();
        case JSON -> value.asAsset().getDataAsJson();
        default -> convertToMTScriptType(value);
      };
    } else {
      return convertToMTScriptType(value);
    }
  }


  /**
   * Returns a MTScript type coerced to a DataValue.
   * @param name the name of the variable/property.
   * @param value the value to coerce.
   * @return the coerced DataValue.
   */
  public DataValue coerceToMTScriptType(String name, Object value) {
    if (value == null) {
      return DataValueFactory.undefined(name);
    }

    // First check if its a number type
    if (value instanceof Number n) {
      if (n.longValue() == n.floatValue()) {
        return DataValueFactory.fromLong(name, n.intValue());
      } else {
        return DataValueFactory.fromDouble(name, n.intValue());
      }
    }

    // Next check if its a JSON type
    if (value instanceof JsonElement ele) {
      if (ele.isJsonArray()) {
        return DataValueFactory.fromJsonArray(name, ele.getAsJsonArray());
      } else if (ele.isJsonObject()) {
        return DataValueFactory.fromJsonObject(name, ele.getAsJsonObject());
      } else if (ele.isJsonPrimitive()) {
        var primitive = ele.getAsJsonPrimitive();
        if (primitive.isBoolean()) {
          return DataValueFactory.fromBoolean(name, primitive.getAsBoolean());
        } else if (primitive.isNumber()) {
          if (primitive.getAsDouble() == primitive.getAsLong()) {
            return DataValueFactory.fromLong(name, primitive.getAsLong());
          } else {
            return DataValueFactory.fromDouble(name, primitive.getAsDouble());
          }
        } else {
          return DataValueFactory.fromString(name, ele.getAsString());
        }
      } else {
        return DataValueFactory.undefined(name);
      }
    }

    // Otherwise convert to a string
    String sval = value.toString();

    // See if it's a number
    try {
      double dval = Double.parseDouble(sval);
      if (dval == (long) dval) {
        return DataValueFactory.fromLong(name, (long) dval);
      } else {
        return DataValueFactory.fromDouble(name, dval) ;
      }
    } catch (NumberFormatException e) {
      // Do nothing, its not a number
    }
    return DataValueFactory.fromString(name, sval);

  }
}
