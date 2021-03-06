/*
 * © Copyright
 *
 * PdnReadingTokenizer.java is part of shashkiserver.
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

import net.percederberg.grammatica.parser.ParserCreationException;
import net.percederberg.grammatica.parser.TokenPattern;
import net.percederberg.grammatica.parser.Tokenizer;

import java.io.Reader;

/**
 * A character stream tokenizer.
 *
 * @author Wieger Wesselink, <wieger at 10x10 dot org>
 * @version 1.5
 */
class PdnReadingTokenizer extends Tokenizer {

  /**
   * Creates a new tokenizer for the specified input stream.
   *
   * @param input the input stream to read
   * @throws ParserCreationException if the tokenizer couldn't be
   *                                 initialized correctly
   */
  public PdnReadingTokenizer(Reader input)
      throws ParserCreationException {

    super(input, false);
    createPatterns();
  }

  /**
   * Initializes the tokenizer by creating all the token patterns.
   *
   * @throws ParserCreationException if the tokenizer couldn't be
   *                                 initialized correctly
   */
  private void createPatterns() throws ParserCreationException {
    TokenPattern pattern;

    pattern = new TokenPattern(PdnReadingConstants.WIN1,
        "WIN1",
        TokenPattern.STRING_TYPE,
        "1-0");
    addPattern(pattern);

    pattern = new TokenPattern(PdnReadingConstants.DRAW1,
        "DRAW1",
        TokenPattern.STRING_TYPE,
        "1/2-1/2");
    addPattern(pattern);

    pattern = new TokenPattern(PdnReadingConstants.LOSS1,
        "LOSS1",
        TokenPattern.STRING_TYPE,
        "0-1");
    addPattern(pattern);

    pattern = new TokenPattern(PdnReadingConstants.WIN2,
        "WIN2",
        TokenPattern.STRING_TYPE,
        "2-0");
    addPattern(pattern);

    pattern = new TokenPattern(PdnReadingConstants.DRAW2,
        "DRAW2",
        TokenPattern.STRING_TYPE,
        "1-1");
    addPattern(pattern);

    pattern = new TokenPattern(PdnReadingConstants.LOSS2,
        "LOSS2",
        TokenPattern.STRING_TYPE,
        "0-2");
    addPattern(pattern);

    pattern = new TokenPattern(PdnReadingConstants.DOUBLEFORFEIT,
        "DOUBLEFORFEIT",
        TokenPattern.STRING_TYPE,
        "0-0");
    addPattern(pattern);

    pattern = new TokenPattern(PdnReadingConstants.ELLIPSES,
        "ELLIPSES",
        TokenPattern.STRING_TYPE,
        "...");
    addPattern(pattern);

    pattern = new TokenPattern(PdnReadingConstants.MOVENUMBER,
        "MOVENUMBER",
        TokenPattern.REGEXP_TYPE,
        "[0-9]+\\.(\\.\\.)?");
    addPattern(pattern);

    pattern = new TokenPattern(PdnReadingConstants.NUMERICMOVE,
        "NUMERICMOVE",
        TokenPattern.REGEXP_TYPE,
        "[0-9][0-9]?(\\s*[-x:]\\s*[0-9][0-9]?)+");
    addPattern(pattern);

    pattern = new TokenPattern(PdnReadingConstants.ALPHANUMERICMOVE,
        "ALPHANUMERICMOVE",
        TokenPattern.REGEXP_TYPE,
        "([a-j][0-9][0-9]?(\\s*[x:]\\s*[a-j][0-9][0-9]?)+)|([a-j][0-9][0-9]?\\s*[-]?\\s*[a-j][0-9][0-9]?)");
    addPattern(pattern);

    pattern = new TokenPattern(PdnReadingConstants.SHORTMOVE,
        "SHORTMOVE",
        TokenPattern.REGEXP_TYPE,
        "([a-j]([x:][a-j])*([x:][a-j][0-9][0-9]?))|([a-j][a-j][0-9][0-9]?)");
    addPattern(pattern);

    pattern = new TokenPattern(PdnReadingConstants.MOVESTRENGTH1,
        "MOVESTRENGTH1",
        TokenPattern.REGEXP_TYPE,
        "[!?]+");
    addPattern(pattern);

    pattern = new TokenPattern(PdnReadingConstants.MOVESTRENGTH2,
        "MOVESTRENGTH2",
        TokenPattern.REGEXP_TYPE,
        "\\([!?]+\\)");
    addPattern(pattern);

    pattern = new TokenPattern(PdnReadingConstants.NAG,
        "NAG",
        TokenPattern.REGEXP_TYPE,
        "\\$[0-9]+");
    addPattern(pattern);

    pattern = new TokenPattern(PdnReadingConstants.LPAREN,
        "LPAREN",
        TokenPattern.STRING_TYPE,
        "(");
    addPattern(pattern);

    pattern = new TokenPattern(PdnReadingConstants.RPAREN,
        "RPAREN",
        TokenPattern.STRING_TYPE,
        ")");
    addPattern(pattern);

    pattern = new TokenPattern(PdnReadingConstants.LBRACKET,
        "LBRACKET",
        TokenPattern.STRING_TYPE,
        "[");
    addPattern(pattern);

    pattern = new TokenPattern(PdnReadingConstants.RBRACKET,
        "RBRACKET",
        TokenPattern.STRING_TYPE,
        "]");
    addPattern(pattern);

    pattern = new TokenPattern(PdnReadingConstants.ASTERISK,
        "ASTERISK",
        TokenPattern.STRING_TYPE,
        "*");
    addPattern(pattern);

    pattern = new TokenPattern(PdnReadingConstants.SETUP,
        "SETUP",
        TokenPattern.REGEXP_TYPE,
        "/[^\\/]*/");
    addPattern(pattern);

    pattern = new TokenPattern(PdnReadingConstants.STRING,
        "STRING",
        TokenPattern.REGEXP_TYPE,
        "\"([^\"]|\\\\\")*\"");
    addPattern(pattern);

    pattern = new TokenPattern(PdnReadingConstants.COMMENT,
        "COMMENT",
        TokenPattern.REGEXP_TYPE,
        "\\{([^}]|(\\\\\\}))*\\}");
    addPattern(pattern);

    pattern = new TokenPattern(PdnReadingConstants.IDENTIFIER,
        "IDENTIFIER",
        TokenPattern.REGEXP_TYPE,
        "[A-Z][a-zA-Z0-9_]*");
    addPattern(pattern);

    pattern = new TokenPattern(PdnReadingConstants.WHITESPACE,
        "WHITESPACE",
        TokenPattern.REGEXP_TYPE,
        "[ \\t\\n\\r]+");
    pattern.setIgnore();
    addPattern(pattern);

    pattern = new TokenPattern(PdnReadingConstants.LINECOMMENT,
        "LINECOMMENT",
        TokenPattern.REGEXP_TYPE,
        "%.*");
    pattern.setIgnore();
    addPattern(pattern);
  }
}
