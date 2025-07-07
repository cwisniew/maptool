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
package net.rptools.maptool.util;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Random;

/**
 * Utility class for generating random Strings useful for uniqueness, to provide a random sequence
 * of characters that can be used for indentifiers or unique keys.
 */
public class RandomStringGenerator {
  /**
   * Valid characters that can be used to make up the password. Potentially ambiguous characters
   * such as lower case L, uppercase i, one, zero, uppercase o are not present.
   */
  public static final byte[] ELIGIBLE_CHARACTERS =
      "abcdefghijkmnopqrstuvwxyzABCDEFGHJKLMNPQRSTUVWZY23456789".getBytes(StandardCharsets.UTF_8);

  /** {@link Random} number generator used to create the password. */
  private final Random random = new SecureRandom();

  /**
   * Returns a new random string between {@code minLength} and {@code maxLength}.
   *
   * @param minLength the minimum length of the string.
   * @param maxLength the maximum length of the string.
   * @return the random string.
   */
  public String generateString(int minLength, int maxLength) {
    int length = minLength + random.nextInt(maxLength - minLength + 1);
    return generateString(length);
  }

  /**
   * Returns a new random string of the specified length
   *
   * @param length the length of the string.
   * @return the new random string.
   */
  public String generateString(int length) {
    return random
        .ints(0, ELIGIBLE_CHARACTERS.length)
        .limit(length)
        .map(i -> ELIGIBLE_CHARACTERS[i])
        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
        .toString();
  }
}
