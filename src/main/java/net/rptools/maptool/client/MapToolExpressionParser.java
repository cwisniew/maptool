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
package net.rptools.maptool.client;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.rptools.common.expression.ExpressionParser;
import net.rptools.maptool.client.functions.*;
import net.rptools.maptool.client.functions.json.JSONMacroFunctions;
import net.rptools.maptool.client.script.javascript.*;
import net.rptools.maptool.client.script.javascript.api.*;
import net.rptools.maptool.model.framework.LibraryManager;
import net.rptools.maptool.servicelocator.MapToolServiceLocator;
import net.rptools.parser.Expression;
import net.rptools.parser.Parser;
import net.rptools.parser.ParserException;
import net.rptools.parser.function.AbstractFunction;
import net.rptools.parser.function.Function;

public class MapToolExpressionParser extends ExpressionParser {

  private static List<Function> mapToolParserFunctions(LibraryManager libraryManager) {
    return Stream.of(
            AbortFunction.getInstance(),
            AssertFunction.getInstance(),
            AddAllToInitiativeFunction.getInstance(),
            ChatFunction.getInstance(),
            CurrentInitiativeFunction.getInstance(),
            DefineMacroFunction.getInstance(),
            EvalMacroFunctions.getInstance(),
            ExecFunction.getInstance(),
            FindTokenFunctions.getInstance(),
            HasImpersonated.getInstance(),
            InitiativeRoundFunction.getInstance(),
            InputFunction.getInstance(),
            IsTrustedFunction.getInstance(),
            JSONMacroFunctions.getInstance(),
            LookupTableFunction.getInstance(),
            MacroArgsFunctions.getInstance(),
            new MacroDialogFunctions(libraryManager),
            MacroFunctions.getInstance(),
            new MacroLinkFunction(libraryManager),
            MapFunctions.getInstance(),
            MiscInitiativeFunction.getInstance(),
            PlayerNameFunctions.getInstance(),
            RemoveAllFromInitiativeFunction.getInstance(),
            ReturnFunction.getInstance(),
            SoundFunctions.getInstance(),
            StateImageFunction.getInstance(),
            BarImageFunction.getInstance(),
            StringFunctions.getInstance(),
            StrListFunctions.getInstance(),
            StrPropFunctions.getInstance(),
            SwitchTokenFunction.getInstance(),
            TokenBarFunction.getInstance(),
            TokenCopyDeleteFunctions.getInstance(),
            TokenGMNameFunction.getInstance(),
            TokenHaloFunction.getInstance(),
            TokenImage.getInstance(),
            TokenInitFunction.getInstance(),
            TokenInitHoldFunction.getInstance(),
            TokenLabelFunction.getInstance(),
            TokenLightFunctions.getInstance(),
            TokenLocationFunctions.getInstance(),
            TokenNameFunction.getInstance(),
            new TokenPropertyFunctions(libraryManager),
            TokenRemoveFromInitiativeFunction.getInstance(),
            TokenSelectionFunctions.getInstance(),
            TokenSightFunctions.getInstance(),
            TokenSpeechFunctions.getInstance(),
            TokenStateFunction.getInstance(),
            TokenVisibleFunction.getInstance(),
            isVisibleFunction.getInstance(),
            getInfoFunction.getInstance(),
            TokenMoveFunctions.getInstance(),
            FogOfWarFunctions.getInstance(),
            Topology_Functions.getInstance(),
            ZoomFunctions.getInstance(),
            ParserPropertyFunctions.getInstance(),
            MathFunctions.getInstance(),
            new MacroJavaScriptBridge(libraryManager),
            DrawingGetterFunctions.getInstance(),
            DrawingSetterFunctions.getInstance(),
            DrawingMiscFunctions.getInstance(),
            ExportDataFunctions.getInstance(),
            RESTfulFunctions.getInstance(),
            HeroLabFunctions.getInstance(),
            LogFunctions.getInstance(),
            LastRolledFunction.getInstance(),
            Base64Functions.getInstance(),
            TokenTerrainModifierFunctions.getInstance(),
            TestFunctions.getInstance(),
            TextLabelFunctions.getInstance(),
            TokenSpeechNameFunction.getInstance(),
            new MarkDownFunctions(),
            new PlayerFunctions(),
            new LibraryFunctions(libraryManager))
        .collect(Collectors.toList());
    }

  /*
   * MapToolServiceLocator is used as a small stepping stone to decoupling the MapTool cod2
   * See https://github.com/RPTools/maptool/issues/3123 for more details.
   */
  private static final LibraryManager libraryManager =
      MapToolServiceLocator.getMapToolServices().getLibraryManager();

  public MapToolExpressionParser() {
    super.getParser().addFunctions(mapToolParserFunctions(libraryManager));
  }

  public static List<Function> getMacroFunctions() {
    /*
     * MapToolServiceLocator is used as a small stepping stone to decoupling the MapTool cod2
     * See https://github.com/RPTools/maptool/issues/3123 for more details.
     */
    LibraryManager libraryManager =
        MapToolServiceLocator.getMapToolServices().getLibraryManager();
    return mapToolParserFunctions(libraryManager);
  }

  /**
   * Override dicelib's parser creation to inject our expression caching parser
   *
   * @return instance of parser
   */
  @Override
  protected Parser createParser() {
    return new ExpressionCachingParser();
  }

  /** Parser implementation that caches expressions in a soft value cache */
  private static class ExpressionCachingParser extends Parser {

    private final Cache<String, Expression> expressionCache =
        CacheBuilder.newBuilder().softValues().build();

    @Override
    public Expression parseExpression(String expression) throws ParserException {
      // Expression exp = super.parseExpression(expression);
      Expression exp = expressionCache.getIfPresent(expression);
      if (exp == null) {
        exp = super.parseExpression(expression);
        expressionCache.put(expression, exp);
      }
      return exp;
    }

    /**
     * Functions are only passed to the parser once, on initial create User defined functions are
     * injected here if defined in UserDefinedMacroFunctions
     *
     * @param functionName the name of the function
     * @return Either user defined function or function known to parser
     */
    @Override
    public Function getFunction(String functionName) {
      // check javascript UDFs first.
      if (functionName.startsWith("js.") || functionName.startsWith("ujs.")) {
        if (JSMacro.isFunctionDefined(functionName)) {
          return new JSMacro(libraryManager);
        }
      }

      // check user defined functions next
      UserDefinedMacroFunctions userFunctions = new UserDefinedMacroFunctions(libraryManager);
      if (userFunctions.isFunctionDefined(functionName)) return userFunctions;

      // let parser do its thing
      return super.getFunction(functionName);
    }
  }
}
