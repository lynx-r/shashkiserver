/*
 * © Copyright
 *
 * EnumRule.java is part of shashkiserver.
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

/**
 * Created by Aleksey Popryaduhin on 09:32 10/08/2017.
 */
public enum EnumRule {

  RUSSIAN(8, 3),
  RUSSIAN_GIVEAWAY(8, 3),
  INTERNATIONAL(10, 4),
  INTERNATIONAL_GIVEAWAY(10, 4);

  /**
   * Размерность где, знак указывает на правила
   */
  private int cellCount;
  private int pieceRowsCount;

  EnumRule(int cellCount, int pieceRowsCount) {
    this.cellCount = cellCount;
    this.pieceRowsCount = pieceRowsCount;
  }

  public int getCellCount() {
    return cellCount;
  }

  public void setCellCount(int cellCount) {
    this.cellCount = cellCount;
  }

  public int getPieceRowsCount() {
    return pieceRowsCount;
  }

  public void setPieceRowsCount(int pieceRowsCount) {
    this.pieceRowsCount = pieceRowsCount;
  }

}
