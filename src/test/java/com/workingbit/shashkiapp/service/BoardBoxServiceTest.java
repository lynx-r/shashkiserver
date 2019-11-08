/*
 * © Copyright
 *
 * BoardBoxServiceTest.java is part of shashkiserver.
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

//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class BoardBoxServiceTest {
//
//  @Autowired
//  private BoardBoxService boardBoxService;
//
//  private Mono<BoardBox> testBoardBox;
//
//  @Before
//  public void init() {
//    CreateBoardBoxRequest createBoardBoxRequest = new CreateBoardBoxRequest(true, false, 1,
//        EnumRule.RUSSIAN, ObjectId.get(), ObjectId.get(), EnumEditBoardBoxMode.EDIT);
////    testBoardBox = boardBoxService.createBoardBox(createBoardBoxRequest);
////    assertNotNull(testBoardBox);
//  }
//
//  @Test
//  public void createBoardBox() {
//    assertNotNull(boardBoxService);
//
//    CreateBoardBoxRequest createBoardBoxRequest = new CreateBoardBoxRequest(true, false, 1,
//        EnumRule.RUSSIAN, ObjectId.get(), ObjectId.get(), EnumEditBoardBoxMode.EDIT);
////    Mono<BoardBox> boardBox = boardBoxService.createBoardBox(createBoardBoxRequest);
////    BoardBox bb = boardBox.block();
////    System.out.println(JsonUtils.dataToJsonPretty(bb));
////    assertNotNull(bb);
//  }
//
//  @Test
//  public void testValidMove() {
////    testBoardBox
////        .map(bb -> boardBoxService.moveDiff(bb, "g3", "h4"))
////        .doOnNext(p -> {
////          System.out.println(p.toPrettyString());
////          assertNotNull(p);
////        })
////        .subscribe();
//  }
//
//  @Test
//  public void testHighlightBoardBox() {
////    testBoardBox
////        .map(bb -> boardBoxService.highlightBoard(bb, "g3"))
////        .doOnNext(p -> {
////          System.out.println(p.toPrettyString());
////          assertNotNull(p);
////        })
////        .subscribe();
//  }
//}
