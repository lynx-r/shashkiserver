///*
// * © Copyright
// *
// * NotationFenParserService.java is part of shashkiserver.
// *
// * shashkiserver is free software: you can redistribute it and/or modify
// * it under the terms of the GNU General Public License as published by
// * the Free Software Foundation, either version 3 of the License, or
// * (at your option) any later version.
// *
// * shashkiserver is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// * GNU General Public License for more details.
// *
// * You should have received a copy of the GNU General Public License
// * along with shashkiserver.  If not, see <https://www.gnu.org/licenses/>.
// *
// */
//
//package com.workingbit.shashkiapp.service;
//
//import com.workingbit.shashkiapp.domain.NotationFen;
//import com.workingbit.shashkiapp.grammar.NotationFenParser;
//import net.percederberg.grammatica.parser.Node;
//import net.percederberg.grammatica.parser.ParserCreationException;
//import net.percederberg.grammatica.parser.ParserLogException;
//import net.percederberg.grammatica.parser.Token;
//
//import java.io.BufferedReader;
//import java.io.StringReader;
//import java.util.LinkedList;
//
///**
// * Created by Aleksey Popryadukhin on 29/03/2018.
// */
//public class NotationFenParserService {
//
//  private static final String COLOR = "COLOR";
//  private static final String COLON = "COLON";
//  private static final String NUMSQUARE = "NUMSQUARE";
//  private static final String KING = "KING";
//  private static final String NUMERIC_SQUARE_SEQUENCE = "NumericSquareSequence";
//  private static final String NUMERIC_SQUARE_RANGE = "NumericSquareRange";
//
//
//  public NotationFen parse(String notation) throws ParserLogException, ParserCreationException {
//    BufferedReader bufferedReader = new BufferedReader(new StringReader(notation));
//    return parse(bufferedReader);
//  }
//
//
//  public NotationFen parse(BufferedReader bufferedReader) throws ParserCreationException, ParserLogException {
//    NotationFenParser notationParser = new NotationFenParser(bufferedReader);
//    notationParser.prepare();
//    Node parse = notationParser.parse();
//    NotationFen notationFen = new NotationFen();
//    parseGameBody(parse, notationFen);
//    return notationFen;
//  }
//
//  private void parseGameBody(Node fen, NotationFen notationFen) {
//    Token turn = (Token) fen.getChildAt(0);
//    notationFen.setTurn(turn.getImage());
//
//    Node childAt = fen.getChildAt(1);
//    boolean black = false;
//    for (int i = 0; i < childAt.getChildCount(); i++) {
//      Node childAt1 = childAt.getChildAt(i);
//      switch (childAt1.getName()) {
//        case COLON: {
//          break;
//        }
//        case COLOR: {
//          String color = ((Token) childAt1).getImage();
//          black = color.equals("B");
//          break;
//        }
//        case NUMERIC_SQUARE_SEQUENCE: {
//          for (int j = 0; j < childAt1.getChildCount(); j++) {
//            Node childAt2 = childAt1.getChildAt(j);
//            switch (childAt2.getName()) {
//              case NUMERIC_SQUARE_RANGE: {
//                for (int k = 0; k < childAt2.getChildCount(); k++) {
//                  Node childAt3 = childAt2.getChildAt(k);
//                  switch (childAt3.getName()) {
//                    case NUMSQUARE: {
//                      String num = ((Token) childAt3).getImage();
//                      NotationFen.Square square = new NotationFen.Square();
//                      if (black) {
//                        square = addSquare(square, notationFen.getBlack());
//                      } else {
//                        square = addSquare(square, notationFen.getWhite());
//                      }
//                      square.setNumber(num);
//                      break;
//                    }
//                    case KING: {
//                      NotationFen.Square square = new NotationFen.Square();
//                      square.setK(true);
//                      if (black) {
//                        notationFen.getBlack().getSquares().add(square);
//                      } else {
//                        notationFen.getWhite().getSquares().add(square);
//                      }
//                      break;
//                    }
//                  }
//                }
//                break;
//              }
//            }
//          }
//          break;
//        }
//      }
//    }
//  }
//
//  private NotationFen.Square addSquare(NotationFen.Square square, NotationFen.Sequence black2) {
//    boolean king;
//    LinkedList<NotationFen.Square> squares = black2.getSquares();
//    if (!squares.isEmpty()) {
//      king = squares.getLast().isK();
//      if (king) {
//        square = squares.getLast();
//      } else {
//        squares.add(square);
//      }
//    } else {
//      squares.add(square);
//    }
//    return square;
//  }
//}
