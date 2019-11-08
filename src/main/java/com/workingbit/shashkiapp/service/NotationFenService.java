/*
 * © Copyright
 *
 * NotationFenService.java is part of shashkiserver.
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


import com.workingbit.shashkiapp.domain.BoardCell;
import com.workingbit.shashkiapp.domain.EnumRule;
import com.workingbit.shashkiapp.domain.NotationFen;
import com.workingbit.shashkiapp.domain.Player;

public class NotationFenService {

  public static NotationFen standardNotation(EnumRule rule, Player player) {
    NotationFen nf = new NotationFen(rule, player);
    BoardCell[][] cells = new BoardCell[rule.getCellCount()][rule.getCellCount()];
    for (int row = 0; row < rule.getCellCount(); ++row) {
      for (int col = 0; col < rule.getCellCount(); ++col) {
        cells[row][col] = new BoardCell(row, col);

        if (row < rule.getPieceRowsCount())
          cells[row][col].setCondition(BoardCell.BLACK_PIECE);
        else if (row > ((rule.getCellCount() - 1) - rule.getPieceRowsCount()))
          cells[row][col].setCondition(BoardCell.WHITE_PIECE);
        if ((row + col) % 2 == 0)
          cells[row][col].setCondition(BoardCell.EMPTY_CELL);
      }
    }
    nf.setCells(cells);
    return nf;
  }

}
