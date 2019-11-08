/*
 * © Copyright
 *
 * PdnReadingConstants.java is part of shashkiserver.
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
 * @version 1.5
 */
interface PdnReadingConstants {

  /**
   * A token identity constant.
   */
  int WIN1 = 1001;

  /**
   * A token identity constant.
   */
  int DRAW1 = 1002;

  /**
   * A token identity constant.
   */
  int LOSS1 = 1003;

  /**
   * A token identity constant.
   */
  int WIN2 = 1004;

  /**
   * A token identity constant.
   */
  int DRAW2 = 1005;

  /**
   * A token identity constant.
   */
  int LOSS2 = 1006;

  /**
   * A token identity constant.
   */
  int DOUBLEFORFEIT = 1007;

  /**
   * A token identity constant.
   */
  int ELLIPSES = 1008;

  /**
   * A token identity constant.
   */
  int MOVENUMBER = 1009;

  /**
   * A token identity constant.
   */
  int NUMERICMOVE = 1010;

  /**
   * A token identity constant.
   */
  int ALPHANUMERICMOVE = 1011;

  /**
   * A token identity constant.
   */
  int SHORTMOVE = 1012;

  /**
   * A token identity constant.
   */
  int MOVESTRENGTH1 = 1013;

  /**
   * A token identity constant.
   */
  int MOVESTRENGTH2 = 1014;

  /**
   * A token identity constant.
   */
  int NAG = 1015;

  /**
   * A token identity constant.
   */
  int LPAREN = 1016;

  /**
   * A token identity constant.
   */
  int RPAREN = 1017;

  /**
   * A token identity constant.
   */
  int LBRACKET = 1018;

  /**
   * A token identity constant.
   */
  int RBRACKET = 1019;

  /**
   * A token identity constant.
   */
  int ASTERISK = 1020;

  /**
   * A token identity constant.
   */
  int SETUP = 1021;

  /**
   * A token identity constant.
   */
  int STRING = 1022;

  /**
   * A token identity constant.
   */
  int COMMENT = 1023;

  /**
   * A token identity constant.
   */
  int IDENTIFIER = 1024;

  /**
   * A token identity constant.
   */
  int WHITESPACE = 1025;

  /**
   * A token identity constant.
   */
  int LINECOMMENT = 1026;

  /**
   * A production node identity constant.
   */
  int PDN_FILE = 2001;

  /**
   * A production node identity constant.
   */
  int GAME_SEPARATOR = 2002;

  /**
   * A production node identity constant.
   */
  int GAME = 2003;

  /**
   * A production node identity constant.
   */
  int GAME_HEADER = 2004;

  /**
   * A production node identity constant.
   */
  int GAME_BODY = 2005;

  /**
   * A production node identity constant.
   */
  int PDN_TAG = 2006;

  /**
   * A production node identity constant.
   */
  int GAME_MOVE = 2007;

  /**
   * A production node identity constant.
   */
  int VARIATION = 2008;

  /**
   * A production node identity constant.
   */
  int MOVE = 2009;

  /**
   * A production node identity constant.
   */
  int GAME_RESULT = 2010;

  /**
   * A production node identity constant.
   */
  int RESULT1 = 2011;

  /**
   * A production node identity constant.
   */
  int RESULT2 = 2012;

  /**
   * A production node identity constant.
   */
  int MOVE_STRENGTH = 2013;
}
