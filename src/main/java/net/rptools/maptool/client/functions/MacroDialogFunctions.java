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
package net.rptools.maptool.client.functions;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;
import net.rptools.maptool.client.MapTool;
import net.rptools.maptool.client.ui.htmlframe.*;
import net.rptools.maptool.client.ui.htmlframe.HTMLFrameFactory.FrameType;
import net.rptools.maptool.language.I18N;
import net.rptools.maptool.model.library.Library;
import net.rptools.maptool.model.library.LibraryManager;
import net.rptools.maptool.util.FunctionUtil;
import net.rptools.parser.Parser;
import net.rptools.parser.ParserException;
import net.rptools.parser.VariableResolver;
import net.rptools.parser.function.AbstractFunction;

public class MacroDialogFunctions extends AbstractFunction {
  private static final MacroDialogFunctions instance = new MacroDialogFunctions();

  private MacroDialogFunctions() {
    super(
        1,
        5,
        "isDialogVisible",
        "isFrameVisible",
        "isOverlayRegistered",
        "closeDialog",
        "resetFrame",
        "closeFrame",
        "closeOverlay",
        "setOverlayVisible",
        "isOverlayVisible",
        "isOverlayLocked",
        "getFrameProperties",
        "getDialogProperties",
        "getOverlayProperties",
        "runJsFunction",
        "html.frame",
        "html.dialog",
        "html.frame5",
        "html.dialog5",
        "html.overlay");
  }

  public static MacroDialogFunctions getInstance() {
    return instance;
  }

  @Override
  public Object childEvaluate(
      Parser parser, VariableResolver resolver, String functionName, List<Object> parameters)
      throws ParserException {
    // Macros can not interact with internal frames/dialogs/overlays
    if (parameters.size() > 0 && HTMLFrameFactory.isInternalOnly(parameters.get(0).toString())) {
      throw new ParserException(
          I18N.getText("msg.error.frame.reservedName", parameters.get(0).toString()));
    }

    String fnLowerCase = functionName.toLowerCase();

    // Note: parameter validation (checkNumberParam) should happen AFTER headless checks
    // if the function isn't supposed to run at all in headless mode (throws exception).
    // For functions that return a default value, param validation might be skippable
    // in headless, but doing it first is safer if the number of params could affect
    // which default is returned (not the case here, but good practice).
    // For this refactoring, we'll keep checkNumberParam where it was if the function proceeds.

    switch (fnLowerCase) {
      case "isdialogvisible":
        FunctionUtil.checkNumberParam(functionName, parameters, 1, 1);
        Object fdvResult = FunctionUtil.checkHeadlessAndReturnDefault(functionName, BigDecimal.ZERO);
        if (fdvResult != FunctionUtil.PROCEED_WITH_NORMAL_EXECUTION) {
          return fdvResult;
        }
        return HTMLFrameFactory.isVisible(false, parameters.get(0).toString())
            ? BigDecimal.ONE
            : BigDecimal.ZERO;
      case "isframevisible":
        FunctionUtil.checkNumberParam(functionName, parameters, 1, 1);
        Object ffvResult = FunctionUtil.checkHeadlessAndReturnDefault(functionName, BigDecimal.ZERO);
        if (ffvResult != FunctionUtil.PROCEED_WITH_NORMAL_EXECUTION) {
          return ffvResult;
        }
        return HTMLFrameFactory.isVisible(true, parameters.get(0).toString())
            ? BigDecimal.ONE
            : BigDecimal.ZERO;
      case "isoverlayregistered":
        FunctionUtil.checkNumberParam(functionName, parameters, 1, 1);
        Object forResult = FunctionUtil.checkHeadlessAndReturnDefault(functionName, BigDecimal.ZERO);
        if (forResult != FunctionUtil.PROCEED_WITH_NORMAL_EXECUTION) {
          return forResult;
        }
        return isOverlayRegistered(parameters.get(0).toString()) ? BigDecimal.ONE : BigDecimal.ZERO;
      case "closedialog":
        FunctionUtil.checkHeadlessAndThrow(functionName);
        FunctionUtil.checkNumberParam(functionName, parameters, 1, 1);
        HTMLFrameFactory.close(false, parameters.get(0).toString());
        return "";
      case "closeframe":
        FunctionUtil.checkHeadlessAndThrow(functionName);
        FunctionUtil.checkNumberParam(functionName, parameters, 1, 1);
        HTMLFrameFactory.close(true, parameters.get(0).toString());
        return "";
      case "closeoverlay":
        FunctionUtil.checkHeadlessAndThrow(functionName);
        FunctionUtil.checkNumberParam(functionName, parameters, 1, 1);
        removeOverlay(parameters.get(0).toString());
        return "";
      case "setoverlayvisible":
        FunctionUtil.checkHeadlessAndThrow(functionName);
        FunctionUtil.checkNumberParam(functionName, parameters, 2, 2);
        String sovName = parameters.get(0).toString();
        BigDecimal sovParam = FunctionUtil.paramAsBigDecimal(functionName, parameters, 1, false);
        setOverlayVisible(sovName, sovParam.equals(BigDecimal.ONE));
        return "";
      case "isoverlayvisible":
        FunctionUtil.checkNumberParam(functionName, parameters, 1, 1);
        Object fovResult = FunctionUtil.checkHeadlessAndReturnDefault(functionName, BigDecimal.ZERO);
        if (fovResult != FunctionUtil.PROCEED_WITH_NORMAL_EXECUTION) {
          return fovResult;
        }
        return isOverlayVisible(parameters.get(0).toString()) ? BigDecimal.ONE : BigDecimal.ZERO;
      case "isoverlaylocked":
        FunctionUtil.checkNumberParam(functionName, parameters, 1, 1);
        Object folResult = FunctionUtil.checkHeadlessAndReturnDefault(functionName, BigDecimal.ZERO);
        if (folResult != FunctionUtil.PROCEED_WITH_NORMAL_EXECUTION) {
          return folResult;
        }
        return isOverlayLocked(parameters.get(0).toString()) ? BigDecimal.ONE : BigDecimal.ZERO;
      case "resetframe":
        FunctionUtil.checkHeadlessAndThrow(functionName);
        FunctionUtil.checkNumberParam(functionName, parameters, 1, 1);
        HTMLFrame.center(parameters.get(0).toString());
        return "";
      case "getframeproperties":
        FunctionUtil.checkNumberParam(functionName, parameters, 1, 1);
        Object gfpResult = FunctionUtil.checkHeadlessAndReturnDefault(functionName, "");
        if (gfpResult != FunctionUtil.PROCEED_WITH_NORMAL_EXECUTION) {
          return gfpResult;
        }
        Optional<JsonObject> gfpProps = HTMLFrame.getFrameProperties(parameters.get(0).toString());
        return gfpProps.isPresent() ? gfpProps.get() : "";
      case "getdialogproperties":
        FunctionUtil.checkNumberParam(functionName, parameters, 1, 1);
        Object gdpResult = FunctionUtil.checkHeadlessAndReturnDefault(functionName, "");
        if (gdpResult != FunctionUtil.PROCEED_WITH_NORMAL_EXECUTION) {
          return gdpResult;
        }
        Optional<JsonObject> gdpProps = HTMLDialog.getDialogProperties(parameters.get(0).toString());
        return gdpProps.isPresent() ? gdpProps.get() : "";
      case "getoverlayproperties":
        FunctionUtil.checkNumberParam(functionName, parameters, 1, 1);
        String gopName = parameters.get(0).toString();
        Object gopResult = FunctionUtil.checkHeadlessAndReturnDefault(functionName, gopName.equals("*") ? "[]" : "");
        if (gopResult != FunctionUtil.PROCEED_WITH_NORMAL_EXECUTION) {
          return gopResult;
        }
        return getOverlayProperties(gopName);
      case "runjsfunction":
        FunctionUtil.checkHeadlessAndThrow(functionName);
        FunctionUtil.checkNumberParam(functionName, parameters, 4, 5);
        String rjfName = parameters.get(0).toString();
        String rjfType = parameters.get(1).toString().trim().toLowerCase();
        String rjfFunc = parameters.get(2).toString();
      String rjfThisArg = parameters.get(3).toString();
      JsonArray rjfArgsArray;
      if (parameters.size() > 4) {
        rjfArgsArray = FunctionUtil.paramAsJsonArray(functionName, parameters, 4);
      } else {
        rjfArgsArray = new JsonArray();
      }
      runJsFunction(rjfName, rjfType, rjfFunc, rjfThisArg, rjfArgsArray);
      return "";
      default:
        // Handle html.* functions
        if (fnLowerCase.startsWith("html.")) {
          FunctionUtil.checkHeadlessAndThrow(functionName); // All html.* functions create UI
          FunctionUtil.checkNumberParam(functionName, parameters, 1, 3);
          String htmlName = parameters.get(0).toString();
          String htmlOpts = parameters.size() > 2 ? parameters.get(2).toString() : "";
          URL htmlUrl = null;
          try {
            htmlUrl = new URI(parameters.get(1).toString()).toURL();
          } catch (MalformedURLException | URISyntaxException e) {
            throw new ParserException(e);
          }

          return switch (fnLowerCase) {
            case "html.frame5" -> showURL(htmlName, htmlUrl, htmlOpts, FrameType.FRAME, true);
            case "html.dialog5" -> showURL(htmlName, htmlUrl, htmlOpts, FrameType.DIALOG, true);
            case "html.frame" -> showURL(htmlName, htmlUrl, htmlOpts, FrameType.FRAME, false);
            case "html.dialog" -> showURL(htmlName, htmlUrl, htmlOpts, FrameType.DIALOG, false);
            case "html.overlay" -> showURL(htmlName, htmlUrl, htmlOpts, FrameType.OVERLAY, true);
            default -> throw new ParserException(I18N.getText("macro.function.html5.unknownType"));
          };
        }
        // If no case matched
        throw new ParserException(I18N.getText("macro.function.general.unknownFunction", functionName));
    }
  }

  private String showURL(String name, URL url, String opts, FrameType frameType, boolean isHTML5)
      throws ParserException {
    // This method is only called by html.* functions which are already guarded by checkHeadlessAndThrow

    try {
      Optional<Library> library = new LibraryManager().getLibrary(url).get();
      if (library.isEmpty()) {
        throw new ParserException(
            I18N.getText("macro.function.html5.invalidURI", url.toExternalForm()));
      }

      if (!library.get().locationExists(url).get()) {
        throw new ParserException(
            I18N.getText("macro.function.html5.invalidURI", url.toExternalForm()));
      }
    } catch (InterruptedException | ExecutionException | IOException e) {
      throw new ParserException(
          I18N.getText("macro.function.html5.invalidURI", url.toExternalForm()));
    }
    HTMLContent htmlContent = HTMLContent.fromURL(url);
    HTMLFrameFactory.show(name, frameType, true, opts, htmlContent);
    return "";
  }
      // Old html.* handling was here, now moved into the switch under default.

    // throw new ParserException(I18N.getText("macro.function.general.unknownFunction", functionName));


  /**
   * Returns the overlay properties. If the name is found, returns a json object of the properties;
   * if the name is "*", returns a json array of all the overlays; if the name is not found, returns
   * an empty string.
   *
   * @param name the name of the overlay, or a "*" for all overlays
   * @return either a json array, a json object, or an empty string
   */
  private Object getOverlayProperties(String name) {
    // This method is only called by getOverlayProperties which is already guarded
    if (name.equals("*")) {
      ConcurrentSkipListSet<HTMLOverlayManager> overlays =
          MapTool.getFrame().getOverlayPanel().getOverlays();
      JsonArray jarr = new JsonArray();
      for (HTMLOverlayManager overlay : overlays) {
        jarr.add(overlay.getProperties());
      }
      return jarr;
    } else {
      HTMLOverlayManager overlay = MapTool.getFrame().getOverlayPanel().getOverlay(name);
      if (overlay != null) {
        return overlay.getProperties();
      } else {
        return "";
      }
    }
  }

  /**
   * Removes one overlay, or all of them.
   *
   * @param name the name of the overlay, or "*" if removing all overlays.
   */
  private void removeOverlay(String name) {
    if (name.equals("*")) {
      MapTool.getFrame().getOverlayPanel().removeAllOverlays();
    } else {
      MapTool.getFrame().getOverlayPanel().removeOverlay(name);
    }
  }

  /**
   * Sets the visible status of the Overlay
   *
   * @param name the name of the overlay
   * @param visible true or false
   */
  private void setOverlayVisible(String name, boolean visible) {
    HTMLOverlayManager overlay = MapTool.getFrame().getOverlayPanel().getOverlay(name);
    if (overlay != null) {
      overlay.setVisible(visible);
    }
  }

  /**
   * Gets the visible status of the Overlay
   *
   * @param name the name of the overlay
   */
  private boolean isOverlayVisible(String name) {
    HTMLOverlayManager overlay = MapTool.getFrame().getOverlayPanel().getOverlay(name);
    if (overlay != null) {
      return overlay.isVisible();
    }
    return false;
  }

  private boolean isOverlayLocked(String name) {
    HTMLOverlayManager overlay = MapTool.getFrame().getOverlayPanel().getOverlay(name);
    if (overlay != null) {
      return overlay.getLocked();
    }
    return false;
  }

  /**
   * Verify the function and thisarg identifier, then run the script.
   *
   * @param name the name of the frame, dialog or overlay
   * @param type the type of the element - eithe frame, dialog or overlay
   * @param func the name of the function
   * @param thisArg the thisarg argument
   * @param argsArray the arguments of the function
   * @throws ParserException if the name, type, function or thisarg are incorrect
   */
  private void runJsFunction(
      String name, String type, String func, String thisArg, JsonArray argsArray)
      throws ParserException {
    String fName = "runJsFunction";

    // Valid regex match for an identifier.
    Pattern idPattern = Pattern.compile("^[a-zA-Z_$][0-9a-zA-Z_$.]*$");

    // Check validity of function namepublic boolean runScript
    if (!idPattern.matcher(func).matches()) {
      throw new ParserException(I18N.getText("msg.error.dialog.js.id", fName, func));
    }
    // Check validity of thisarg
    if (!idPattern.matcher(thisArg).matches()) {
      throw new ParserException(I18N.getText("msg.error.dialog.js.id", fName, thisArg));
    }

    // Wrap the script in a function to check if the document is complenet/wait for the document to
    // be complete before trying to run it
    String script =
        """
        (function() {
          function waitForComplete() {
            if (document.readyState === "complete") {
              %s.apply(%s, %s);
            } else {
              setTimeout(waitForComplete, 50);
            }
          };

          waitForComplete();
        })();
        """
            .formatted(func, thisArg, argsArray.toString());

    // Execute the script
    boolean executed;
    if (type.equals("frame") || type.equals("frame5")) {
      executed = HTMLFrame.runScript(name, script);
    } else if (type.equals("dialog") || type.equals("dialog5")) {
      executed = HTMLDialog.runScript(name, script);
    } else if (type.equals("overlay")) {
      executed = MapTool.getFrame().getOverlayPanel().runScript(name, script);
    } else {
      throw new ParserException(I18N.getText("msg.error.dialog.js.type", fName, type));
    }
    if (!executed) {
      throw new ParserException(I18N.getText("msg.error.dialog.js.name", fName, type, name));
    }
  }

  /**
   * Returns whether the overlay is visible.
   *
   * @param name the name of the overlay
   * @return true if it is visible, false otherwise
   */
  private boolean isOverlayRegistered(String name) {
    return MapTool.getFrame().getOverlayPanel().isRegistered(name);
  }
}
