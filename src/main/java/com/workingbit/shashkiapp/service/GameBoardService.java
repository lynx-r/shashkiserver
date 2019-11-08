/*
 * © Copyright
 *
 * GameBoardService.java is part of shashkiserver.
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

public final class GameBoardService {

  private final static EnumDirection[] kingDirections = EnumDirection.values();
  private final static EnumDirection[] eatDirections = EnumDirection.values();
  private final static EnumDirection[] whiteDirections = new EnumDirection[]{EnumDirection.RIGHT_FORWARD, EnumDirection.LEFT_FORWARD};
  private final static EnumDirection[] blackDirections = new EnumDirection[]{EnumDirection.RIGHT_BOTTOM, EnumDirection.LEFT_BOTTOM};
  private int cellCount;
  private BoardCell[][] cells = new BoardCell[cellCount][cellCount];

  private GameBoardService() {
  }

  public GameBoardService(NotationFen notationFen) {
    cells = notationFen.getCells();
    cellCount = notationFen.getRule().getCellCount();
  }

  public GameBoardService(GameNotation gameNotation) {
    cellCount = gameNotation.getNotationFen().getRule().getCellCount();
    gameNotation.flattenStrokes().forEach(c -> setCell(c.getRow(), c.getCol(), c));
  }

  public BoardCell[][] getCells() {
    return cells;
  }

  public BoardCell getCell(String notation) {
    int col = notation.charAt(0) - 'a';
    int row = cellCount - Character.digit(notation.charAt(1), 10);
    return cells[row][col];
  }

  public BoardCell getCell(int row, int col) {
    return cells[row][col];
  }

  public void setCell(int row, int col, BoardCell cell) {
    cells[row][col].copyCell(cell);
  }

  public boolean hasWon(Player player) {
    return getAllAvailableMoves(player.getOpposite()).isEmpty();
  }

  public List<Move> getAllAvailableMoves(Player player) {
    LinkedList<BoardCell> pieceCells = new LinkedList<>();
    for (int row = 0; row < cellCount; ++row) {
      for (int col = 0; col < cellCount; ++col) {
        if (cells[row][col].getCondition() == player.getPieceColor())
          pieceCells.add(cells[row][col]);
      }
    }

    LinkedList<Move> availableMoves = new LinkedList<>();
    for (BoardCell cell : pieceCells) {
      availableMoves.addAll(getEatMoves(cell));
    }
    if (!availableMoves.isEmpty())
      return availableMoves;
    for (BoardCell cell : pieceCells) {
      availableMoves.addAll(getNormalMoves(cell));
    }
    return availableMoves;
  }

  public List<Move> getAvailableMoves(BoardCell cell) {
    LinkedList<Move> availableMoves = new LinkedList<>(getEatMoves(cell));
    if (!availableMoves.isEmpty())
      return availableMoves;
    availableMoves.addAll(getNormalMoves(cell));
    return availableMoves;
  }

  private List<Move> getNormalMoves(BoardCell cell) {
    LinkedList<Move> normalMoves = new LinkedList<>();
    if (cell.getCondition() == BoardCell.EMPTY_CELL)
      return normalMoves;

    int toRow = 0;
    int toCol = 0;
    int maxCountOfSteps = (cell.isKingPiece()) ? (cellCount - 1) : 1;
    for (EnumDirection d : getDirections(cell)) {
      for (int step = 1; step <= maxCountOfSteps; ++step) {
        switch (d) {
          case RIGHT_BOTTOM:
            toRow = cell.getRow() + step;
            toCol = cell.getCol() + step;
            break;
          case RIGHT_FORWARD:
            toRow = cell.getRow() - step;
            toCol = cell.getCol() + step;
            break;
          case LEFT_BOTTOM:
            toRow = cell.getRow() + step;
            toCol = cell.getCol() - step;
            break;
          case LEFT_FORWARD:
            toRow = cell.getRow() - step;
            toCol = cell.getCol() - step;
            break;
        }
        if (toRow < 0 || toCol < 0 || toRow >= cellCount || toCol >= cellCount)
          break;
        if (cells[toRow][toCol].getCondition() != BoardCell.EMPTY_CELL)
          break;
        Move move = new Move(cell, cells[toRow][toCol]);
        normalMoves.add(move);
      }
    }

    return normalMoves;
  }

  private List<Move> getEatMoves(BoardCell cell) {
    LinkedList<Move> eatMoves = new LinkedList<>();
    int toRow = 0;
    int toCol = 0;
    int eatRow = 0;
    int eatCol = 0;
    int maxCountOfSteps = (cell.isKingPiece()) ? (cellCount - 1) : 1;
    for (EnumDirection d : eatDirections) {
      BoardCell eatCell = null;
      LinkedList<Move> allHighlightedMoves = new LinkedList<>();
      LinkedList<Move> nextEatMoves = new LinkedList<>();
      for (int step = 1; step <= maxCountOfSteps; ++step) {
        int toStep = (eatCell == null) ? (step + 1) : step;
        switch (d) {
          case RIGHT_BOTTOM:
            eatRow = cell.getRow() + step;
            eatCol = cell.getCol() + step;
            toRow = cell.getRow() + toStep;
            toCol = cell.getCol() + toStep;
            break;
          case RIGHT_FORWARD:
            eatRow = cell.getRow() - step;
            eatCol = cell.getCol() + step;
            toRow = cell.getRow() - toStep;
            toCol = cell.getCol() + toStep;
            break;
          case LEFT_BOTTOM:
            eatRow = cell.getRow() + step;
            eatCol = cell.getCol() - step;
            toRow = cell.getRow() + toStep;
            toCol = cell.getCol() - toStep;
            break;
          case LEFT_FORWARD:
            eatRow = cell.getRow() - step;
            eatCol = cell.getCol() - step;
            toRow = cell.getRow() - toStep;
            toCol = cell.getCol() - toStep;
            break;
        }
        if (toRow < 0 || toCol < 0 || toRow >= cellCount || toCol >= cellCount)
          break;
        if (cells[toRow][toCol].getCondition() == BoardCell.EMPTY_CELL
            && cells[eatRow][eatCol].getOppositeCondition() == cell.getCondition()
            && eatCell == null) {
          eatCell = cells[eatRow][eatCol];
          step++;
        }
        if ((cells[toRow][toCol].getCondition() == cells[eatRow][eatCol].getCondition()
            && cell.getOppositeCondition() == cells[eatRow][eatCol].getCondition())
            || cell.getCondition() == cells[toRow][toCol].getCondition()
            || cell.getCondition() == cells[eatRow][eatCol].getCondition())
          break;
        if (eatCell == null)
          continue;
        if (cells[toRow][toCol].getCondition() != BoardCell.EMPTY_CELL)
          break;
        Move move = new Move(cell, cells[toRow][toCol]);
        move.setEat(eatCell);
        allHighlightedMoves.add(move);
        if (isExistsNextEatMove(cell, cells[toRow][toCol], d)) {
          nextEatMoves.add(move);
        }
      }
      if (!nextEatMoves.isEmpty())
        eatMoves.addAll(nextEatMoves);
      else
        eatMoves.addAll(allHighlightedMoves);
    }

    return eatMoves;
  }

  public boolean isExistsNextEatMove(BoardCell piece, BoardCell fromCell, EnumDirection from) {
    int toRow = 0;
    int toCol = 0;
    int eatRow = 0;
    int eatCol = 0;
    int maxCountOfSteps = (piece.isKingPiece()) ? (cellCount - 1) : 1;
    for (EnumDirection d : eatDirections) {
      if (d == from || d == getOppositeDirection(from))
        continue;
      for (int step = 1; step <= maxCountOfSteps; ++step) {
        switch (d) {
          case RIGHT_BOTTOM:
            eatRow = fromCell.getRow() + step;
            eatCol = fromCell.getCol() + step;
            toRow = fromCell.getRow() + (step + 1);
            toCol = fromCell.getCol() + (step + 1);
            break;
          case RIGHT_FORWARD:
            eatRow = fromCell.getRow() - step;
            eatCol = fromCell.getCol() + step;
            toRow = fromCell.getRow() - (step + 1);
            toCol = fromCell.getCol() + (step + 1);
            break;
          case LEFT_BOTTOM:
            eatRow = fromCell.getRow() + step;
            eatCol = fromCell.getCol() - step;
            toRow = fromCell.getRow() + (step + 1);
            toCol = fromCell.getCol() - (step + 1);
            break;
          case LEFT_FORWARD:
            eatRow = fromCell.getRow() - step;
            eatCol = fromCell.getCol() - step;
            toRow = fromCell.getRow() - (step + 1);
            toCol = fromCell.getCol() - (step + 1);
            break;
        }
        if (toRow < 0 || toCol < 0 || toRow >= cellCount || toCol >= cellCount)
          break;
        if (cells[toRow][toCol].getCondition() == BoardCell.EMPTY_CELL
            && cells[eatRow][eatCol].getOppositeCondition() == piece.getCondition()) {
          return true;
        }
      }
    }
    return false;
  }

  public void doMove(Move move) {
    move.getTo().copyCell(move.getFrom());
    move.getFrom().clearCell();
    if (move.getEat() != null) {
      move.getEat().clearCell();
    }
    if (move.getTo().getCondition() == BoardCell.WHITE_PIECE) {
      if (move.getTo().getRow() == 0) {
        move.getTo().setKingPiece(true);
      }
    }
    if (move.getTo().getCondition() == BoardCell.BLACK_PIECE) {
      if (move.getTo().getRow() == cellCount - 1) {
        move.getTo().setKingPiece(true);
      }
    }
  }

  private EnumDirection[] getDirections(BoardCell cell) {
    if (cell.isKingPiece()) {
      return kingDirections;
    } else if (cell.getCondition() == BoardCell.WHITE_PIECE) {
      return whiteDirections;
    } else {
      return blackDirections;
    }
  }

  private EnumDirection getOppositeDirection(EnumDirection direction) {
    if (direction == null)
      return null;
    switch (direction) {
      case RIGHT_FORWARD:
        return EnumDirection.LEFT_BOTTOM;
      case LEFT_FORWARD:
        return EnumDirection.RIGHT_BOTTOM;
      case RIGHT_BOTTOM:
        return EnumDirection.LEFT_FORWARD;
      case LEFT_BOTTOM:
        return EnumDirection.RIGHT_FORWARD;
    }
    return null;
  }
}
