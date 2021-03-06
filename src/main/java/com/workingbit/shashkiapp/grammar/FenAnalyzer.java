/*
 * © Copyright
 *
 * FenAnalyzer.java is part of shashkiserver.
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

import net.percederberg.grammatica.parser.*;

/**
 * A class providing callback methods for the parser.
 *
 * @author Wieger Wesselink, <wieger at 10x10 dot org>
 * @version 1.2
 */
abstract class FenAnalyzer extends Analyzer {

  /**
   * Called when entering a parse tree node.
   *
   * @param node the node being entered
   * @throws ParseException if the node analysis discovered errors
   */
  protected void enter(Node node) throws ParseException {
    switch (node.getId()) {
      case FenConstants.COLOR:
        enterColor((Token) node);
        break;
      case FenConstants.KING:
        enterKing((Token) node);
        break;
      case FenConstants.ALPHASQUARE:
        enterAlphasquare((Token) node);
        break;
      case FenConstants.NUMSQUARE:
        enterNumsquare((Token) node);
        break;
      case FenConstants.HYPHEN:
        enterHyphen((Token) node);
        break;
      case FenConstants.COMMA:
        enterComma((Token) node);
        break;
      case FenConstants.COLON:
        enterColon((Token) node);
        break;
      case FenConstants.DOT:
        enterDot((Token) node);
        break;
      case FenConstants.FEN:
        enterFen((Production) node);
        break;
      case FenConstants.NUMERIC_SQUARES:
        enterNumericSquares((Production) node);
        break;
      case FenConstants.NUMERIC_SQUARE_SEQUENCE:
        enterNumericSquareSequence((Production) node);
        break;
      case FenConstants.NUMERIC_SQUARE_RANGE:
        enterNumericSquareRange((Production) node);
        break;
      case FenConstants.ALPHA_NUMERIC_SQUARES:
        enterAlphaNumericSquares((Production) node);
        break;
      case FenConstants.ALPHA_NUMERIC_SQUARE_SEQUENCE:
        enterAlphaNumericSquareSequence((Production) node);
        break;
    }
  }

  /**
   * Called when exiting a parse tree node.
   *
   * @param node the node being exited
   * @return the node to add to the parse tree, or
   * null if no parse tree should be created
   * @throws ParseException if the node analysis discovered errors
   */
  protected Node exit(Node node) throws ParseException {
    switch (node.getId()) {
      case FenConstants.COLOR:
        return exitColor((Token) node);
      case FenConstants.KING:
        return exitKing((Token) node);
      case FenConstants.ALPHASQUARE:
        return exitAlphasquare((Token) node);
      case FenConstants.NUMSQUARE:
        return exitNumsquare((Token) node);
      case FenConstants.HYPHEN:
        return exitHyphen((Token) node);
      case FenConstants.COMMA:
        return exitComma((Token) node);
      case FenConstants.COLON:
        return exitColon((Token) node);
      case FenConstants.DOT:
        return exitDot((Token) node);
      case FenConstants.FEN:
        return exitFen((Production) node);
      case FenConstants.NUMERIC_SQUARES:
        return exitNumericSquares((Production) node);
      case FenConstants.NUMERIC_SQUARE_SEQUENCE:
        return exitNumericSquareSequence((Production) node);
      case FenConstants.NUMERIC_SQUARE_RANGE:
        return exitNumericSquareRange((Production) node);
      case FenConstants.ALPHA_NUMERIC_SQUARES:
        return exitAlphaNumericSquares((Production) node);
      case FenConstants.ALPHA_NUMERIC_SQUARE_SEQUENCE:
        return exitAlphaNumericSquareSequence((Production) node);
    }
    return node;
  }

  /**
   * Called when adding a child to a parse tree node.
   *
   * @param node  the parent node
   * @param child the child node, or null
   * @throws ParseException if the node analysis discovered errors
   */
  protected void child(Production node, Node child)
      throws ParseException {

    switch (node.getId()) {
      case FenConstants.FEN:
        childFen(node, child);
        break;
      case FenConstants.NUMERIC_SQUARES:
        childNumericSquares(node, child);
        break;
      case FenConstants.NUMERIC_SQUARE_SEQUENCE:
        childNumericSquareSequence(node, child);
        break;
      case FenConstants.NUMERIC_SQUARE_RANGE:
        childNumericSquareRange(node, child);
        break;
      case FenConstants.ALPHA_NUMERIC_SQUARES:
        childAlphaNumericSquares(node, child);
        break;
      case FenConstants.ALPHA_NUMERIC_SQUARE_SEQUENCE:
        childAlphaNumericSquareSequence(node, child);
        break;
    }
  }

  /**
   * Called when entering a parse tree node.
   *
   * @param node the node being entered
   * @throws ParseException if the node analysis discovered errors
   */
  protected void enterColor(Token node) throws ParseException {
  }

  /**
   * Called when exiting a parse tree node.
   *
   * @param node the node being exited
   * @return the node to add to the parse tree, or
   * null if no parse tree should be created
   * @throws ParseException if the node analysis discovered errors
   */
  protected Node exitColor(Token node) throws ParseException {
    return node;
  }

  /**
   * Called when entering a parse tree node.
   *
   * @param node the node being entered
   * @throws ParseException if the node analysis discovered errors
   */
  protected void enterKing(Token node) throws ParseException {
  }

  /**
   * Called when exiting a parse tree node.
   *
   * @param node the node being exited
   * @return the node to add to the parse tree, or
   * null if no parse tree should be created
   * @throws ParseException if the node analysis discovered errors
   */
  protected Node exitKing(Token node) throws ParseException {
    return node;
  }

  /**
   * Called when entering a parse tree node.
   *
   * @param node the node being entered
   * @throws ParseException if the node analysis discovered errors
   */
  protected void enterAlphasquare(Token node) throws ParseException {
  }

  /**
   * Called when exiting a parse tree node.
   *
   * @param node the node being exited
   * @return the node to add to the parse tree, or
   * null if no parse tree should be created
   * @throws ParseException if the node analysis discovered errors
   */
  protected Node exitAlphasquare(Token node) throws ParseException {
    return node;
  }

  /**
   * Called when entering a parse tree node.
   *
   * @param node the node being entered
   * @throws ParseException if the node analysis discovered errors
   */
  protected void enterNumsquare(Token node) throws ParseException {
  }

  /**
   * Called when exiting a parse tree node.
   *
   * @param node the node being exited
   * @return the node to add to the parse tree, or
   * null if no parse tree should be created
   * @throws ParseException if the node analysis discovered errors
   */
  protected Node exitNumsquare(Token node) throws ParseException {
    return node;
  }

  /**
   * Called when entering a parse tree node.
   *
   * @param node the node being entered
   * @throws ParseException if the node analysis discovered errors
   */
  protected void enterHyphen(Token node) throws ParseException {
  }

  /**
   * Called when exiting a parse tree node.
   *
   * @param node the node being exited
   * @return the node to add to the parse tree, or
   * null if no parse tree should be created
   * @throws ParseException if the node analysis discovered errors
   */
  protected Node exitHyphen(Token node) throws ParseException {
    return node;
  }

  /**
   * Called when entering a parse tree node.
   *
   * @param node the node being entered
   * @throws ParseException if the node analysis discovered errors
   */
  protected void enterComma(Token node) throws ParseException {
  }

  /**
   * Called when exiting a parse tree node.
   *
   * @param node the node being exited
   * @return the node to add to the parse tree, or
   * null if no parse tree should be created
   * @throws ParseException if the node analysis discovered errors
   */
  protected Node exitComma(Token node) throws ParseException {
    return node;
  }

  /**
   * Called when entering a parse tree node.
   *
   * @param node the node being entered
   * @throws ParseException if the node analysis discovered errors
   */
  protected void enterColon(Token node) throws ParseException {
  }

  /**
   * Called when exiting a parse tree node.
   *
   * @param node the node being exited
   * @return the node to add to the parse tree, or
   * null if no parse tree should be created
   * @throws ParseException if the node analysis discovered errors
   */
  protected Node exitColon(Token node) throws ParseException {
    return node;
  }

  /**
   * Called when entering a parse tree node.
   *
   * @param node the node being entered
   * @throws ParseException if the node analysis discovered errors
   */
  protected void enterDot(Token node) throws ParseException {
  }

  /**
   * Called when exiting a parse tree node.
   *
   * @param node the node being exited
   * @return the node to add to the parse tree, or
   * null if no parse tree should be created
   * @throws ParseException if the node analysis discovered errors
   */
  protected Node exitDot(Token node) throws ParseException {
    return node;
  }

  /**
   * Called when entering a parse tree node.
   *
   * @param node the node being entered
   * @throws ParseException if the node analysis discovered errors
   */
  protected void enterFen(Production node) throws ParseException {
  }

  /**
   * Called when exiting a parse tree node.
   *
   * @param node the node being exited
   * @return the node to add to the parse tree, or
   * null if no parse tree should be created
   * @throws ParseException if the node analysis discovered errors
   */
  protected Node exitFen(Production node) throws ParseException {
    return node;
  }

  /**
   * Called when adding a child to a parse tree node.
   *
   * @param node  the parent node
   * @param child the child node, or null
   * @throws ParseException if the node analysis discovered errors
   */
  protected void childFen(Production node, Node child)
      throws ParseException {

    node.addChild(child);
  }

  /**
   * Called when entering a parse tree node.
   *
   * @param node the node being entered
   * @throws ParseException if the node analysis discovered errors
   */
  protected void enterNumericSquares(Production node)
      throws ParseException {
  }

  /**
   * Called when exiting a parse tree node.
   *
   * @param node the node being exited
   * @return the node to add to the parse tree, or
   * null if no parse tree should be created
   * @throws ParseException if the node analysis discovered errors
   */
  protected Node exitNumericSquares(Production node)
      throws ParseException {

    return node;
  }

  /**
   * Called when adding a child to a parse tree node.
   *
   * @param node  the parent node
   * @param child the child node, or null
   * @throws ParseException if the node analysis discovered errors
   */
  protected void childNumericSquares(Production node, Node child)
      throws ParseException {

    node.addChild(child);
  }

  /**
   * Called when entering a parse tree node.
   *
   * @param node the node being entered
   * @throws ParseException if the node analysis discovered errors
   */
  protected void enterNumericSquareSequence(Production node)
      throws ParseException {
  }

  /**
   * Called when exiting a parse tree node.
   *
   * @param node the node being exited
   * @return the node to add to the parse tree, or
   * null if no parse tree should be created
   * @throws ParseException if the node analysis discovered errors
   */
  protected Node exitNumericSquareSequence(Production node)
      throws ParseException {

    return node;
  }

  /**
   * Called when adding a child to a parse tree node.
   *
   * @param node  the parent node
   * @param child the child node, or null
   * @throws ParseException if the node analysis discovered errors
   */
  protected void childNumericSquareSequence(Production node, Node child)
      throws ParseException {

    node.addChild(child);
  }

  /**
   * Called when entering a parse tree node.
   *
   * @param node the node being entered
   * @throws ParseException if the node analysis discovered errors
   */
  protected void enterNumericSquareRange(Production node)
      throws ParseException {
  }

  /**
   * Called when exiting a parse tree node.
   *
   * @param node the node being exited
   * @return the node to add to the parse tree, or
   * null if no parse tree should be created
   * @throws ParseException if the node analysis discovered errors
   */
  protected Node exitNumericSquareRange(Production node)
      throws ParseException {

    return node;
  }

  /**
   * Called when adding a child to a parse tree node.
   *
   * @param node  the parent node
   * @param child the child node, or null
   * @throws ParseException if the node analysis discovered errors
   */
  protected void childNumericSquareRange(Production node, Node child)
      throws ParseException {

    node.addChild(child);
  }

  /**
   * Called when entering a parse tree node.
   *
   * @param node the node being entered
   * @throws ParseException if the node analysis discovered errors
   */
  protected void enterAlphaNumericSquares(Production node)
      throws ParseException {
  }

  /**
   * Called when exiting a parse tree node.
   *
   * @param node the node being exited
   * @return the node to add to the parse tree, or
   * null if no parse tree should be created
   * @throws ParseException if the node analysis discovered errors
   */
  protected Node exitAlphaNumericSquares(Production node)
      throws ParseException {

    return node;
  }

  /**
   * Called when adding a child to a parse tree node.
   *
   * @param node  the parent node
   * @param child the child node, or null
   * @throws ParseException if the node analysis discovered errors
   */
  protected void childAlphaNumericSquares(Production node, Node child)
      throws ParseException {

    node.addChild(child);
  }

  /**
   * Called when entering a parse tree node.
   *
   * @param node the node being entered
   * @throws ParseException if the node analysis discovered errors
   */
  protected void enterAlphaNumericSquareSequence(Production node)
      throws ParseException {
  }

  /**
   * Called when exiting a parse tree node.
   *
   * @param node the node being exited
   * @return the node to add to the parse tree, or
   * null if no parse tree should be created
   * @throws ParseException if the node analysis discovered errors
   */
  protected Node exitAlphaNumericSquareSequence(Production node)
      throws ParseException {

    return node;
  }

  /**
   * Called when adding a child to a parse tree node.
   *
   * @param node  the parent node
   * @param child the child node, or null
   * @throws ParseException if the node analysis discovered errors
   */
  protected void childAlphaNumericSquareSequence(Production node, Node child)
      throws ParseException {

    node.addChild(child);
  }
}
