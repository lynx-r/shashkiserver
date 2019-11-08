/*
 * © Copyright
 *
 * GameNotation.java is part of shashkiserver.
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


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
public class GameNotation {

  /**
   * Some possible tags:
   * "Игрок белыми"
   * "Игрок черными"
   * "Событие"
   * "Место"
   * "Раунд"
   * "Дата"
   * "Результат"
   * "Тип игры"
   * "#tag"
   */
  private LinkedList<Tag> tags;
  private NotationFen notationFen;
  private BoardCell previousCell;
  private BoardCell requiredMoveCell;
  private LinkedList<Stroke> strokes;
  private List<Move> available;
  /**
   * Flag when white ate first draught. Blocks adding new strokes until white finishes his stroke
   */
  @JsonIgnore
  private boolean whiteFirstEat;
  private Player player;
  private Player winner;

  public GameNotation() {
    tags = new LinkedList<>(List.of(
        new Tag("Игрок белыми", ""),
        new Tag("Игрок черными", ""),
        new Tag("Событие", ""),
        new Tag("Место", ""),
        new Tag("Раунд", ""),
        new Tag("Дата", ""),
        new Tag("Результат", ""),
        new Tag("Тип игры", "")
    ));
    strokes = new LinkedList<>();
    Stroke zeroStroke = new Stroke(0);
    zeroStroke.setSelected(true);
    strokes.add(zeroStroke);
    available = new ArrayList<>();
  }

  public GameNotation(NotationFen notationFen, Player player) {
    this();
    this.notationFen = notationFen.deepClone();
    this.player = player;
  }

  public static GameNotation createGameNotation(GameNotation copy) {
    var gn = new GameNotation(copy.getNotationFen(), copy.getPlayer());
    gn.tags = copy.getTags();
    gn.notationFen = copy.getNotationFen().deepClone();
    gn.strokes = copy.getStrokes();
    gn.player = copy.getPlayer();
    gn.whiteFirstEat = copy.isWhiteFirstEat();
    gn.winner = copy.getWinner();
    gn.setGameNotationPreviousAndRequiredMoveCells(copy.getPreviousCell(), copy.getRequiredMoveCell(),
        copy.getNotationFen().getCells());
    return gn;
  }

  public void oppositePlayer() {
    player = player.getOpposite();
  }

  public List<BoardCell> flattenStrokes() {
    return strokes
        .stream()
        .map(s -> Stream.concat(s.getWhiteMoves().stream(), s.getBlackMoves().stream())
            .map(m -> List.of(m.getFrom(), m.getEat() != null ? m.getEat() : new BoardCell(), m.getTo()))
            .flatMap(List::stream)
            .collect(Collectors.toList())
        )
        .flatMap(List::stream)
        .collect(Collectors.toList());
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", GameNotation.class.getSimpleName() + "[", "]")
        .add("previousCell=" + previousCell)
        .add("\nrequiredMoveCell=" + requiredMoveCell)
        .add("\nstrokes=" + strokes.stream().map(Stroke::toString).collect(Collectors.joining("\n")))
        .add("\nplayer=" + player + "\n")
        .toString();
  }

  private void setGameNotationPreviousAndRequiredMoveCells(BoardCell prevCell, BoardCell reqCell, BoardCell[][] cells) {
    if (prevCell != null) {
      BoardCell cell = cells[prevCell.getRow()][prevCell.getCol()];
      cell.copyCell(prevCell);
      this.previousCell = cell;
    }
    if (reqCell != null) {
      BoardCell cell = cells[reqCell.getRow()][reqCell.getCol()];
      cell.copyCell(reqCell);
      requiredMoveCell = cell;
    }
  }
}
