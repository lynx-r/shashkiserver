/*
 * © Copyright
 *
 * FenConstants.java is part of shashkiserver.
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

package com.workingbit.shashkiapp.grammar;

/**
 * An interface with constants for the parser and tokenizer.
 *
 * @author Wieger Wesselink, <wieger at 10x10 dot org>
 * @version 1.2
 */
interface FenConstants {

  /**
   * A token identity constant.
   */
  int COLOR = 1001;

  /**
   * A token identity constant.
   */
  int KING = 1002;

  /**
   * A token identity constant.
   */
  int ALPHASQUARE = 1003;

  /**
   * A token identity constant.
   */
  int NUMSQUARE = 1004;

  /**
   * A token identity constant.
   */
  int HYPHEN = 1005;

  /**
   * A token identity constant.
   */
  int COMMA = 1006;

  /**
   * A token identity constant.
   */
  int COLON = 1007;

  /**
   * A token identity constant.
   */
  int DOT = 1008;

  /**
   * A token identity constant.
   */
  int WHITESPACE = 1009;

  /**
   * A production node identity constant.
   */
  int FEN = 2001;

  /**
   * A production node identity constant.
   */
  int NUMERIC_SQUARES = 2002;

  /**
   * A production node identity constant.
   */
  int NUMERIC_SQUARE_SEQUENCE = 2003;

  /**
   * A production node identity constant.
   */
  int NUMERIC_SQUARE_RANGE = 2004;

  /**
   * A production node identity constant.
   */
  int ALPHA_NUMERIC_SQUARES = 2005;

  /**
   * A production node identity constant.
   */
  int ALPHA_NUMERIC_SQUARE_SEQUENCE = 2006;
}
