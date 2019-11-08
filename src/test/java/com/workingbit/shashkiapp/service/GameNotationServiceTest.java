/*
 * © Copyright
 *
 * GameNotationServiceTest.java is part of shashkiserver.
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

import org.junit.Test;

public class GameNotationServiceTest {

  @Test
  public void cellTouch() {

    var gmService = GameNotationService.forRulesRussianCheckers();

    gmService.cellTouch("c3");
    gmService.cellTouch("b4");
    gmService.cellTouch("b6");
    gmService.cellTouch("a5");

    System.out.println(gmService.getGameNotation().flattenStrokes());
  }
}
