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
package net.rptools.dicelib.expression.function;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import net.rptools.parser.Parser;
import net.rptools.parser.ParserException;
import net.rptools.parser.VariableResolver;
import net.rptools.parser.function.AbstractFunction;

/** Class used for Genesys and FFG Starwars dice rolls */
public class GenesysDice extends AbstractFunction {

  /** Create a new {@code GenesysDice} object. */
  public GenesysDice() {
    super(1, 1, false, "genesysDice", "ffgStarWarsDice");
  }

  /** An enumeration of the different types of dice that can be rolled. */
  public enum GenesysDiceType {
    BOOST(
        Map.of(SupportedDice.GENERIC, "j", SupportedDice.STARWARS, "&#xE93D;"),
        Map.of(
            SupportedDice.GENERIC,
            "genesysBoostDie",
            SupportedDice.STARWARS,
            "ffgStarWarsBoostDie"),
        Map.of(
            SupportedDice.GENERIC,
            "genesysBoostRoll",
            SupportedDice.STARWARS,
            "ffgStarWarsBoostRoll"),
        new GenesysDiceResult[] {
          GenesysDiceResult.NOTHING,
          GenesysDiceResult.NOTHING,
          GenesysDiceResult.SUCCESS,
          GenesysDiceResult.SUCCESS_AND_ADVANTAGE,
          GenesysDiceResult.TWO_ADVANTAGE,
          GenesysDiceResult.ADVANTAGE
        },
        "\\d+[bB]"),
    ABILITY(
        Map.of(SupportedDice.GENERIC, "k", SupportedDice.STARWARS, "&#xE93F;"),
        Map.of(
            SupportedDice.GENERIC,
            "genesysAbilityDie",
            SupportedDice.STARWARS,
            "ffgStarWarsAbilityDie"),
        Map.of(
            SupportedDice.GENERIC,
            "genesysAbilityRoll",
            SupportedDice.STARWARS,
            "ffgStarWarsAbilityRoll"),
        new GenesysDiceResult[] {
          GenesysDiceResult.NOTHING,
          GenesysDiceResult.SUCCESS,
          GenesysDiceResult.SUCCESS,
          GenesysDiceResult.TWO_SUCCESS,
          GenesysDiceResult.ADVANTAGE,
          GenesysDiceResult.ADVANTAGE,
          GenesysDiceResult.SUCCESS_AND_ADVANTAGE,
          GenesysDiceResult.TWO_ADVANTAGE
        },
        "\\d+[aA]"),
    PROFICIENCY(
        Map.of(SupportedDice.GENERIC, "l", SupportedDice.STARWARS, "&#xE941;"),
        Map.of(
            SupportedDice.GENERIC,
            "genesysProficiencyDie",
            SupportedDice.STARWARS,
            "ffgStarWarsProficiencyDie"),
        Map.of(
            SupportedDice.GENERIC,
            "genesysProficiencyRoll",
            SupportedDice.STARWARS,
            "ffgStarWarsProficiencyRoll"),
        new GenesysDiceResult[] {
          GenesysDiceResult.NOTHING,
          GenesysDiceResult.SUCCESS,
          GenesysDiceResult.SUCCESS,
          GenesysDiceResult.TWO_SUCCESS,
          GenesysDiceResult.TWO_SUCCESS,
          GenesysDiceResult.ADVANTAGE,
          GenesysDiceResult.SUCCESS_AND_ADVANTAGE,
          GenesysDiceResult.SUCCESS_AND_ADVANTAGE,
          GenesysDiceResult.SUCCESS_AND_ADVANTAGE,
          GenesysDiceResult.TWO_ADVANTAGE,
          GenesysDiceResult.TWO_ADVANTAGE,
          GenesysDiceResult.TRIUMPH
        },
        "\\d+[pP]"),
    SETBACK(
        Map.of(SupportedDice.GENERIC, "j", SupportedDice.STARWARS, "&#xE93D;"),
        Map.of(
            SupportedDice.GENERIC,
            "genesysSetbackDie",
            SupportedDice.STARWARS,
            "ffgStarWarsSetbackDie"),
        Map.of(
            SupportedDice.GENERIC,
            "genesysSetbackRoll",
            SupportedDice.STARWARS,
            "ffgStarWarsSetbackRoll"),
        new GenesysDiceResult[] {
          GenesysDiceResult.NOTHING,
          GenesysDiceResult.NOTHING,
          GenesysDiceResult.FAILURE,
          GenesysDiceResult.FAILURE,
          GenesysDiceResult.THREAT
        },
        "\\d+[sS]"),
    DIFFICULTY(
        Map.of(SupportedDice.GENERIC, "k", SupportedDice.STARWARS, "&#xE93F;"),
        Map.of(
            SupportedDice.GENERIC,
            "genesysDifficultyDie",
            SupportedDice.STARWARS,
            "ffgStarWarsDifficultyDie"),
        Map.of(
            SupportedDice.GENERIC,
            "genesysDifficultyRoll",
            SupportedDice.STARWARS,
            "ffgStarWarsDifficultyRoll"),
        new GenesysDiceResult[] {
          GenesysDiceResult.NOTHING,
          GenesysDiceResult.FAILURE,
          GenesysDiceResult.TWO_FAILURE,
          GenesysDiceResult.THREAT,
          GenesysDiceResult.THREAT,
          GenesysDiceResult.THREAT,
          GenesysDiceResult.TWO_THREAT,
          GenesysDiceResult.FAILURE_AND_THREAT
        },
        "\\d+[dD]"),
    CHALLENGE(
        Map.of(SupportedDice.GENERIC, "l", SupportedDice.STARWARS, "&#xE941;"),
        Map.of(
            SupportedDice.GENERIC,
            "genesysChallengeDie",
            SupportedDice.STARWARS,
            "ffgStarWarsChallengeDie"),
        Map.of(
            SupportedDice.GENERIC,
            "genesysChallengeRoll",
            SupportedDice.STARWARS,
            "ffgStarWarsChallengeRoll"),
        new GenesysDiceResult[] {
          GenesysDiceResult.NOTHING,
          GenesysDiceResult.FAILURE,
          GenesysDiceResult.FAILURE,
          GenesysDiceResult.TWO_FAILURE,
          GenesysDiceResult.TWO_FAILURE,
          GenesysDiceResult.THREAT,
          GenesysDiceResult.THREAT,
          GenesysDiceResult.FAILURE_AND_THREAT,
          GenesysDiceResult.FAILURE_AND_THREAT,
          GenesysDiceResult.TWO_THREAT,
          GenesysDiceResult.TWO_THREAT,
          GenesysDiceResult.DESPAIR
        },
        "\\d+[cC]"),
    ;

    private final Map<SupportedDice, String> symbol;
    private final Map<SupportedDice, String> cssDieClass;
    private final Map<SupportedDice, String> cssRollClass;
    private final String pattern;
    private final GenesysDiceResult[] faces;

    GenesysDiceType(
        Map<SupportedDice, String> symbol,
        Map<SupportedDice, String> cssDieClass,
        Map<SupportedDice, String> cssRollClass,
        GenesysDiceResult[] faces,
        String pattern) {
      this.symbol = symbol;
      this.cssDieClass = cssDieClass;
      this.cssRollClass = cssRollClass;
      this.pattern = pattern;
      this.faces = faces;
    }

    public String getSymbol(SupportedDice formatType) {
      return symbol.get(formatType);
    }

    public String getCssDieClass(SupportedDice formatType) {
      return cssDieClass.get(formatType);
    }

    public String getCssRollClass(SupportedDice formatType) {
      return cssRollClass.get(formatType);
    }

    public String getPattern() {
      return pattern;
    }

    public GenesysDiceResult[] getFaces() {
      return faces;
    }
  }

  enum GenesysDiceResult {
    NOTHING(
        0, 0, 0, 0, 0, 0, Map.of(SupportedDice.GENERIC, "_J", SupportedDice.STARWARS, "&#xE93E;")),
    FAILURE(
        0, 1, 0, 0, 0, 0, Map.of(SupportedDice.GENERIC, "f", SupportedDice.STARWARS, "&#xE905;")),
    ADVANTAGE(
        0, 0, 1, 0, 0, 0, Map.of(SupportedDice.GENERIC, "a", SupportedDice.STARWARS, "&#xE900;")),
    SUCCESS(
        1, 0, 0, 0, 0, 0, Map.of(SupportedDice.GENERIC, "s", SupportedDice.STARWARS, "&#xE90B;")),
    TRIUMPH(
        1, 0, 0, 0, 1, 0, Map.of(SupportedDice.GENERIC, "t", SupportedDice.STARWARS, "&#xE90F;")),
    THREAT(
        0, 0, 0, 1, 0, 0, Map.of(SupportedDice.GENERIC, "h", SupportedDice.STARWARS, "&#xE90D;")),
    DESPAIR(
        0, 1, 0, 0, 0, 1, Map.of(SupportedDice.GENERIC, "d", SupportedDice.STARWARS, "&#xE904;")),
    SUCCESS_AND_ADVANTAGE(
        1,
        0,
        1,
        0,
        0,
        0,
        Map.of(SupportedDice.GENERIC, "sa", SupportedDice.STARWARS, "&#xE90B;&#xE900;")),
    TWO_ADVANTAGE(
        0,
        0,
        2,
        0,
        0,
        0,
        Map.of(SupportedDice.GENERIC, "aa", SupportedDice.STARWARS, "&#xE900;&#xE900;")),
    TWO_SUCCESS(
        2,
        0,
        0,
        0,
        0,
        0,
        Map.of(SupportedDice.GENERIC, "ss", SupportedDice.STARWARS, "&#xE90B;&#xE90B;")),
    TWO_FAILURE(
        0,
        2,
        0,
        0,
        0,
        0,
        Map.of(SupportedDice.GENERIC, "ff", SupportedDice.STARWARS, "&#xE905;&#xE905;")),
    TWO_THREAT(
        0,
        0,
        0,
        2,
        0,
        0,
        Map.of(SupportedDice.GENERIC, "hh", SupportedDice.STARWARS, "&#xE90D;&#xE90D;")),
    FAILURE_AND_THREAT(
        0,
        1,
        0,
        1,
        0,
        0,
        Map.of(SupportedDice.GENERIC, "fh", SupportedDice.STARWARS, "&#xE905;&#xE90D;"));

    private final int successCount;
    private final int failureCount;
    private final int advantageCount;
    private final int threatCount;
    private final int triumphCount;
    private final int despairCount;
    private final Map<SupportedDice, String> symbols;

    GenesysDiceResult(
        int successCount,
        int failureCount,
        int advantageCount,
        int threatCount,
        int triumphCount,
        int despairCount,
        Map<SupportedDice, String> symbols) {
      this.successCount = successCount;
      this.failureCount = failureCount;
      this.advantageCount = advantageCount;
      this.threatCount = threatCount;
      this.triumphCount = triumphCount;
      this.despairCount = despairCount;
      this.symbols = symbols;
    }

    public int getSuccessCount() {
      return successCount;
    }

    public int getFailureCount() {
      return failureCount;
    }

    public int getAdvantageCount() {
      return advantageCount;
    }

    public int getDespairCount() {
      return despairCount;
    }

    public int getThreatCount() {
      return threatCount;
    }

    public int getTriumphCount() {
      return triumphCount;
    }

    public String getSymbols(SupportedDice formatType) {
      return symbols.get(formatType);
    }
  }

  public enum SupportedDice {
    GENERIC,
    STARWARS
  }

  private record GenesysExpressionResult(
      int successCount,
      int failureCount,
      int advantageCount,
      int threatCount,
      int triumphCount,
      int despairCount,
      Map<GenesysDiceType, GenesysDiceResult[]> diceResults,
      int netSuccess,
      int netAdvantage) {

    public GenesysExpressionResult(
        int successCount,
        int failureCount,
        int advantageCount,
        int threatCount,
        int triumphCount,
        int despairCount,
        Map<GenesysDiceType, GenesysDiceResult[]> diceResults) {
      this(
          successCount,
          failureCount,
          advantageCount,
          threatCount,
          triumphCount,
          despairCount,
          diceResults,
          successCount - failureCount,
          advantageCount - threatCount);
    }
  }

  @Override
  public Object childEvaluate(
      Parser parser, VariableResolver resolver, String functionName, List<Object> parameters)
      throws ParserException {
    var result = parseRoll(parameters.get(0).toString());

    var output =
        switch (functionName.toLowerCase()) {
          case "genesysdice" -> formatGensysDice(result, SupportedDice.GENERIC);
          case "ffgstarwarsdice" -> formatGensysDice(result, SupportedDice.STARWARS);
          default -> throw new ParserException("Unknown function: " + functionName);
        };

    return output;
  }

  private String formatRoll(
      GenesysDiceResult[] results, SupportedDice formatType, GenesysDiceType diceType) {
    var sb = new StringBuilder();
    System.out.print(diceType.name() + " : ");
    if (results.length > 0) {
      sb.append("<span class=\"genesys\">");
      sb.append("<span class=\"");
      sb.append(diceType.getCssDieClass(formatType));
      sb.append("\">");
      sb.append(diceType.getSymbol(formatType));
      sb.append("</span>");
      sb.append("<span class=\"");
      sb.append(diceType.getCssRollClass(formatType));
      sb.append("\">");
      for (var die : results) {
        sb.append(die.getSymbols(formatType));
        sb.append("&nbsp;");
        System.out.print(die.name() + ", ");
      }
      sb.append("</span>");
      sb.append("</span>");
    }
    System.out.println();
    return sb.toString();
  }

  private String formatGensysDice(GenesysExpressionResult result, SupportedDice formatType) {
    boolean first = true;
    var sb = new StringBuilder();
    sb.append("<span class=\"genesysRoll\">");
    var order =
        new GenesysDiceType[] {
          GenesysDiceType.PROFICIENCY,
          GenesysDiceType.ABILITY,
          GenesysDiceType.BOOST,
          GenesysDiceType.CHALLENGE,
          GenesysDiceType.DIFFICULTY,
          GenesysDiceType.SETBACK
        };
    for (var die : order) {
      if (result.diceResults.containsKey(die)) {
        if (!first) {
          sb.append("&nbsp;");
        }
        sb.append(formatRoll(result.diceResults.get(die), formatType, die));
        first = false;
      }
    }

    sb.append("</span> = ");
    if (formatType == SupportedDice.STARWARS) {
      sb.append("<span class=\"swRpgRoll\"><span class=\"swRpg\">");
    } else {
      sb.append("<span class=\"genesysRoll\"><span class=\"genesys\">");
    }

    sb.append(
        GenesysDiceResult.TRIUMPH.getSymbols(formatType).repeat(Math.max(0, result.triumphCount)));
    sb.append(
        GenesysDiceResult.DESPAIR.getSymbols(formatType).repeat(Math.max(0, result.despairCount)));
    int successToPrint = result.netSuccess - result.triumphCount;
    sb.append(GenesysDiceResult.SUCCESS.getSymbols(formatType).repeat(Math.max(0, successToPrint)));
    int failureToPrint = result.netSuccess * -1 - result.despairCount;
    sb.append(GenesysDiceResult.FAILURE.getSymbols(formatType).repeat(Math.max(0, failureToPrint)));
    sb.append(
        GenesysDiceResult.ADVANTAGE
            .getSymbols(formatType)
            .repeat(Math.max(0, result.netAdvantage)));
    sb.append(
        GenesysDiceResult.THREAT
            .getSymbols(formatType)
            .repeat(Math.max(0, result.netAdvantage * -1)));
    sb.append("</span></span>");
    if (result.netSuccess > 0) {
      sb.append("<span class=\"genesysSuccess\">(Success)</span>");
    } else {
      sb.append("<span class=\"genesysFailure\">(Failure)</span>");
    }
    sb.append("</span>");
    return sb.toString();
  }

  private GenesysExpressionResult parseRoll(String rollString) throws ParserException {
    int successCount = 0;
    int failureCount = 0;
    int advantageCount = 0;
    int threatCount = 0;
    int triumphCount = 0;
    int despairCount = 0;

    var diceResults = new HashMap<GenesysDiceType, GenesysDiceResult[]>();
    var condensedRoll = rollString.replaceAll("\\s", "");

    int consumed = 0;
    for (var die : GenesysDiceType.values()) {
      int count = 0;
      String[] matches =
          Pattern.compile(die.getPattern())
              .matcher(condensedRoll)
              .results()
              .map(MatchResult::group)
              .toArray(String[]::new);
      if (matches.length == 1) {
        count = Integer.parseInt(matches[0].replaceAll("[A-z]", ""));
        var results = rollDie(die, count);
        for (var d : results) {
          successCount += d.getSuccessCount();
          failureCount += d.getFailureCount();
          advantageCount += d.getAdvantageCount();
          threatCount += d.getThreatCount();
          triumphCount += d.getTriumphCount();
          despairCount += d.getDespairCount();
        }
        diceResults.put(die, results);
        consumed += matches[0].length();
      } else if (matches.length > 1) {
        throw new ParserException("Bad format for GeneSys Dice: " + rollString);
      }
    }

    if (consumed != condensedRoll.length()) {
      throw new ParserException("Bad format for GeneSys Dice: " + rollString);
    }

    return new GenesysExpressionResult(
        successCount,
        failureCount,
        advantageCount,
        threatCount,
        triumphCount,
        despairCount,
        diceResults);
  }

  private GenesysDiceResult[] rollDie(GenesysDiceType die, int count) {
    var results = new GenesysDiceResult[count];
    System.out.print(die.name() + " : ");
    for (int i = 0; i < count; i++) {
      var faces = die.getFaces();
      int roll = DiceHelper.rollDice(1, faces.length) - 1;
      results[i] = faces[roll];
      System.out.print(roll + ", ");
    }
    System.out.println();
    return results;
  }
}
