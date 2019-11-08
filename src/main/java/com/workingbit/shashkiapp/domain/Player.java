/*
 * © Copyright
 *
 * Player.java is part of shashkiserver.
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

public enum Player {
  WHITE,
  BLACK;

  public Player getOpposite() {
    return (this.equals(WHITE)) ? BLACK : WHITE;
  }

  public int getPieceColor() {
    return (this.equals(WHITE)) ? BoardCell.WHITE_PIECE : BoardCell.BLACK_PIECE;
  }

  public String getPlayerName() {
    return (this.equals(WHITE)) ? "White" : "Black";
  }

  public boolean isWhite() {
    return this.equals(WHITE);
  }

}
