/*
 * © Copyright
 *
 * BoardCell.java is part of shashkiserver.
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

package com.workingbit.shashkiapp.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class BoardCell {

  public static final int EMPTY_CELL = 0;
  public static final int WHITE_PIECE = 1;
  public static final int BLACK_PIECE = -1;
  private static final int EMPTY_CELL_OPPOSITE = 100;
  private int condition;
  private boolean highlight;
  private boolean kingPiece;
  private int row;
  private int col;

  public BoardCell(int row, int col) {
    highlight = false;
    kingPiece = false;
    condition = EMPTY_CELL;
    this.row = row;
    this.col = col;
  }

  public BoardCell(BoardCell cell) {
    if (cell == null) {
      condition = EMPTY_CELL;
      highlight = false;
      kingPiece = false;
      row = -1;
      col = -1;
    } else {
      condition = cell.getCondition();
      highlight = cell.isHighlight();
      kingPiece = cell.isKingPiece();
      row = cell.getRow();
      col = cell.getCol();
    }

  }

  public int getOppositeCondition() {
    if (condition == WHITE_PIECE) {
      return BLACK_PIECE;
    } else if (condition == BLACK_PIECE) {
      return WHITE_PIECE;
    }

    return EMPTY_CELL_OPPOSITE;
  }

  public void copyCell(BoardCell cell) {
    highlight = cell.isHighlight();
    kingPiece = cell.isKingPiece();
    condition = cell.getCondition();
  }

  public void clearCell() {
    highlight = false;
    kingPiece = false;
    condition = EMPTY_CELL;
  }

  @Override
  public String toString() {
    return "BoardCell{"
        + "condition=" + condition
        + ", highlight=" + highlight
        + ", kingPiece=" + kingPiece
        + ", row=" + row
        + ", col=" + col
        + "}";
  }

}
