/*
 * © Copyright
 *
 * Utils.java is part of shashkiserver.
 *
 * shashkiserver is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * shashkiserver is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with shashkiserver.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package com.workingbit.shashkiapp.util;

import com.workingbit.shashkiapp.domain.ArticlesContainer;
import org.jetbrains.annotations.NotNull;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Created by Aleksey Popryaduhin on 12:01 12/08/2017.
 */
public class Utils {

  @NotNull
  private static String RANDOM_STR_SEP = "-";


  public static String getRandomString(int length) {
    return getSecureRandomString(length);
  }

  private static String getSecureRandomString(int length) {
    try {
      //Initialize SecureRandom
      //This is a lengthy operation, to be done only upon
      //initialization of the application
      SecureRandom prng = SecureRandom.getInstance("SHA1PRNG");

      //generate a random bytes
      byte[] bytes = new byte[length];
      prng.nextBytes(bytes);

      //get its digest
      return hexEncode(bytes).substring(0, length);
    } catch (NoSuchAlgorithmException ex) {
      return null;
    }
  }

  /**
   * The byte[] returned by MessageDigest does not have a nice
   * textual representation, so some form of encoding is usually performed.
   * <p>
   * This implementation follows the example of David Flanagan's book
   * "Java In A Nutshell", and converts a byte array into a String
   * of hex characters.
   * <p>
   * Another popular alternative is to use a "Base64" encoding.
   */
  static private String hexEncode(byte[] aInput) {
    StringBuilder result = new StringBuilder();
    char[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    for (int idx = 0; idx < aInput.length; ++idx) {
      byte b = aInput[idx];
      result.append(digits[(b & 0xf0) >> 4]);
      result.append(digits[b & 0x0f]);
    }
    return result.toString();
  }

  public static void setArticleHru(ArticlesContainer article, boolean present) {
    article.setHumanReadableUrl(article.getHumanReadableUrl() + (present ? RANDOM_STR_SEP + getRandomString4() : ""));
  }

  private static String getRandomString4() {
    return getRandomString(4);
  }

}
