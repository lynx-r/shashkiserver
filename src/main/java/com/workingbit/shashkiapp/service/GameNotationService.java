/*
 * © Copyright
 *
 * GameNotationService.java is part of shashkiserver.
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

package com.workingbit.shashkiapp.service;

import com.workingbit.shashkiapp.domain.*;

import java.util.LinkedList;
import java.util.List;

public class GameNotationService {

  private GameBoardService gameBoardService;
  private GameNotation gameNotation;

  private GameNotationService(EnumRule rule, Player player) {
    NotationFen nf = NotationFenService.standardNotation(rule, player);
    gameBoardService = new GameBoardService(nf);
    gameNotation = new GameNotation(nf, player);
  }

  private GameNotationService(NotationFen notationFen) {
    gameBoardService = new GameBoardService(notationFen);
    gameNotation = new GameNotation(notationFen, notationFen.getPlayer());
  }

  public static GameNotationService fromGameNotation(GameNotation gameNotation) {
    var gnService = new GameNotationService(gameNotation.getNotationFen());
    gnService.gameNotation = GameNotation.createGameNotation(gameNotation);
    gameNotation.flattenStrokes()
        .forEach(c -> gnService.gameBoardService.setCell(c.getRow(), c.getCol(), c));
    return gnService;
  }

  public static GameNotationService forRulesRussianCheckers() {
    return new GameNotationService(EnumRule.RUSSIAN, Player.WHITE);
  }

  public GameNotation cellTouch(BoardCell cell, boolean skipAddMovesToNotation) {
    if (gameNotation.getWinner() != null) {
      return gameNotation;
    }
    if (cell.getCondition() == gameNotation.getPlayer().getPieceColor()) {
      return highlightMoves(cell);
    } else {
      return doMove(cell, skipAddMovesToNotation);
    }
  }

  private GameNotation highlightMoves(BoardCell cell) {
    if (gameNotation.getPreviousCell() != null) {
      List<Move> moves = gameBoardService.getAvailableMoves(gameNotation.getPreviousCell());
      for (Move m : moves) {
        m.getTo().setHighlight(false);
      }

      gameNotation.getPreviousCell().setHighlight(false);
    }

    boolean moveIsAvailable = false;
    for (Move m : gameBoardService.getAllAvailableMoves(gameNotation.getPlayer())) {
      if (m.getFrom().equals(cell)) {
        moveIsAvailable = true;
        break;
      }
    }

    if (!moveIsAvailable) {
      return gameNotation;
    }

    gameNotation.setPreviousCell(cell);
    cell.setHighlight(true);
    List<Move> moves = gameBoardService.getAvailableMoves(cell);
    for (Move m : moves) {
      m.getTo().setHighlight(true);
    }

    gameNotation.setAvailable(moves);
    return gameNotation;
  }

  private GameNotation doMove(BoardCell cell, boolean skipAddMovesToNotation) {
    if (gameNotation.getPreviousCell() == null) {
      return gameNotation;
    }

    BoardCell previousCell = gameNotation.getRequiredMoveCell() == gameNotation.getPreviousCell()
        ? null : gameNotation.getRequiredMoveCell();
    gameNotation.setRequiredMoveCell(previousCell);

    gameNotation.getPreviousCell().setHighlight(false);
    List<Move> moves = gameBoardService.getAvailableMoves(gameNotation.getPreviousCell());
    for (Move m : moves) {
      m.getTo().setHighlight(false);
    }

    if (!skipAddMovesToNotation) {
      gameNotation.setPreviousCell(null);
    }
    for (Move m : moves) {
      if (m.getTo().getRow() == cell.getRow() && m.getTo().getCol() == cell.getCol()) {
        if (m.getEat() != null) {
          gameBoardService.doMove(m);
          boolean existsNextEatMove = gameBoardService.isExistsNextEatMove(m.getTo(), m.getTo(), null);
          addStrokeWhenWhitePlayerBeats(existsNextEatMove);
          addMoveToNotation(m, skipAddMovesToNotation);
          if (existsNextEatMove) {
            gameNotation.setRequiredMoveCell(m.getTo());
            return highlightMoves(gameNotation.getRequiredMoveCell());
          }
        } else {
          addStrokeWhenWhitePlayer();
          gameBoardService.doMove(m);
          addMoveToNotation(m, skipAddMovesToNotation);
        }

        if (gameBoardService.hasWon(gameNotation.getPlayer())) {
          gameNotation.setWinner(gameNotation.getPlayer());
          return gameNotation;
        }

        gameNotation.oppositePlayer();
      }

    }

    return gameNotation;
  }

  private void addStrokeWhenWhitePlayerBeats(boolean existsNextEatMove) {
    LinkedList<Stroke> strokes = gameNotation.getStrokes();
    if (!strokes.isEmpty()) {
      if (!gameNotation.isWhiteFirstEat()) {
        addStrokeWhenWhitePlayer();
        gameNotation.setWhiteFirstEat(existsNextEatMove);
      } else if (!existsNextEatMove) {
        gameNotation.setWhiteFirstEat(false);
      }
    } else {
      if (gameNotation.getPlayer().isWhite()) {
        Stroke s = new Stroke(1);
        s.setSelected(true);
        strokes.add(s);
      }
    }
  }

  private void addStrokeWhenWhitePlayer() {
    if (gameNotation.getPlayer().isWhite()) {
      var strokes = gameNotation.getStrokes();
      Stroke lastStroke = strokes.getLast();
      lastStroke.setSelected(false);
      var bMoves = lastStroke.getBlackMoves();
      if (!bMoves.isEmpty()) {
        bMoves.get(bMoves.size() - 1).setSelected(false);
      }
      var nNum = lastStroke.getNotationNumber() + 1;
      Stroke nextStroke = new Stroke(nNum);
      nextStroke.setSelected(true);
      strokes.add(nextStroke);
    }
  }

  private void addMoveToNotation(Move m, boolean skipAddMovesToNotation) {
    if (skipAddMovesToNotation) {
      return;
    }
    m.setSelected(true);
    Stroke stroke = gameNotation.getStrokes().getLast();
    List<Move> whiteMoves = stroke.getWhiteMoves();
    List<Move> blackMoves = stroke.getBlackMoves();
    if (gameNotation.getPlayer().isWhite()) {
      if (!whiteMoves.isEmpty()) {
        whiteMoves.get(whiteMoves.size() - 1).setSelected(false);
      }
      whiteMoves.add(m);
    } else {
      if (!blackMoves.isEmpty()) {
        blackMoves.get(blackMoves.size() - 1).setSelected(false);
      }
      if (!whiteMoves.isEmpty()) {
        whiteMoves.get(whiteMoves.size() - 1).setSelected(false);
      }
      blackMoves.add(m);
    }
  }

  public GameNotation getGameNotation() {
    return gameNotation;
  }

}
