/*
 * © Copyright
 *
 * Stroke.java is part of shashkiserver.
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

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.joining;

/**
 * Created by Aleksey Popryaduhin on 21:29 03/10/2017.
 */
@Data
public class Stroke {
  private Integer notationNumber;
  private List<Move> whiteMoves;
  private List<Move> blackMoves;
  private String whiteMovesStrength;
  private String blackMovesStrength;
  private boolean ellipses;
  private String comment;
  /**
   * Начиная с этого хода идет скрытая задача
   */
  private boolean task;
  private boolean selected;

  public Stroke() {
    whiteMoves = new ArrayList<>();
    blackMoves = new ArrayList<>();
  }

  public Stroke(Integer notationNumber) {
    this();
    this.notationNumber = notationNumber;
  }

  @Override
  public String toString() {
    return "Stroke{"
        + ", whiteMoves=" + whiteMoves.stream().map(Move::toString).collect(joining("\n"))
        + ", blackMoves=" + blackMoves.stream().map(Move::toString).collect(joining("\n"))
        + "}";
  }

}
