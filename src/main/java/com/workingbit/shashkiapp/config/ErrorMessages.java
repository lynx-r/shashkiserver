/*
 * © Copyright
 *
 * ErrorMessages.java is part of shashkiserver.
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

package com.workingbit.shashkiapp.config;

import java.text.MessageFormat;

public class ErrorMessages {

  public static final String ERROR_HEADER_NAME = "errors";

  public static final String USER_NOT_FOUND = "user_not_found";
  public static final String USERNAME_BUSY = "email_busy";

  public static final String UNABLE_TO_GET_ARTICLES = "Не удалось загрузить статьи";
  public static final String UNABLE_TO_CREATE_ARTICLE = "Не удалось добавить статью";
  public static final String ARTICLE_WITH_ID_NOT_FOUND = "Запрашиваемая статья не найдена";
  public static final String UNABLE_TO_SAVE_ARTICLE = "Не удалось сохранить статью";
  public static final String UNABLE_TO_CREATE_BOARD = "Не удалось создать доску";
  public static final String BOARD_WITH_ID_NOT_FOUND = "Доска не найдена";
  public static final String UNABLE_TO_ADD_DRAUGHT = "Не удалось добавить шашку";
  public static final String UNABLE_TO_HIGHLIGHT_BOARD = "Не удалось подсветить доску";
  public static final String UNABLE_TO_MOVE = "Недопустимый ход";
  public static final String UNABLE_TO_REDO = "Не удалось повторить ход";
  public static final String UNABLE_TO_UNDO = "Не удалось возвратить ход";
  public static final String UNABLE_TO_CHANGE_TURN = "Не удалось изменить последовательность ходов";
  public static final String UNABLE_TO_FORK = "Не удалось добавить вариант";
  public static final String UNABLE_TO_SWITCH = "Не удалось переключиться на доску";

  public static final String MALFORMED_REQUEST = "Malformed request params";
  public static final String IGNORE_VK_API_SIGN = "Ignore VK API Sign";
  public static final String UNABLE_TO_SAVE_BOARD = "Не удалось сохранить доску";
  public static final String UNABLE_TO_LOAD_BOARD = "Не удалось загрузить доску";
  public static final String UNABLE_TO_REGISTER = "Не удалось зарегистрироваться или имя %s уже занято";
  public static final String INVALID_USERNAME_OR_PASSWORD = "Не верный логин или пароль";
  public static final String UNABLE_TO_AUTHENTICATE = "Ваш аккаунт не найден на сайте";
  public static final String UNABLE_TO_ASSIGN_ROLE = "Не удалось назначить роль";
  public static final String UNABLE_TO_LOGOUT = "Не удалось выйти с сайта";
  public static final String NOT_OWNER = "Не владелец";
  public static final String RESOURCE_NOT_FOUND = "Запрашиваемый ресурс не найден";
  public static final String ENITY_NOT_FOUND = "Запрашиваемый объект не найден";
  public static final String INTERNAL_SERVER_ERROR = "Ошибка на сервере";
  public static final String FORBIDDEN = "Неверный логин или пароль";
  public static final String FIRSTNAME_NOT_NULL = "Поле <Имя> не может быть пустым";
  public static final String FIRSTNAME_CONSTRAINTS = "Минимальная длина поля <Имя> 2 символа, максимальная 64";
  public static final String LASTNAME_NOT_NULL = "Поле <Фамилия> не может быть пустым";
  public static final String LASTTNAME_CONSTRAINTS = "Минимальная длина поля <Фамилия> 2 символа, максимальная 64";
  public static final String MIDDLENAME_NOT_NULL = "Поле <Отчество> не может быть пустым";
  public static final String MIDDLENAME_CONSTRAINTS = "Минимальная длина поля <Отчество> 2 символа, максимальная 64";
  public static final String PASSWORD_NOT_NULL = "Поле <Пароль> не может быть пустым";
  public static final String PASSWORD_CONSTRAINTS = "Минимальная длина поля <Пароль> 64 символа";
  public static final String RANK_NOT_NULL = "Поле <Разряд> не может быть пустым";
  public static final String INVALID_INTERNAL_REQUEST = "Не верный внутренний запрос";
  public static final String UNPARSABLE_PDN_CONTENT = "Не удалось обработать pdn";
  public static final String UNABLE_TO_PARSE_PDN = "Не удалось обработать pdn";
  public static final String UNABLE_TO_IMPORT_NOTATION = "Не удалось импортировать нотацию";
  public static final String NO_CONTENT = "Нет контента";
  public static final String USERNAME_IS_BUSY = "Имя пользователя занято";
  public static final String ARTICLE_IS_DELETED = "Статья удалена";
  public static final String INVALID_HIGHLIGHT = "Ходы шашки не могут быть подсвечены";
  public static final String UNSUPPORTED_ENCODING = "Кодировка не поддерживается";
  public static final String UNABLE_TO_SAVE_ENTITY = "Не удалось сохранить объект";
  public static final String EXPECTED_ONE_RESULT = "Ожидается один результат, получено много";
  public static final String UNABLE_TO_CHANGE_USERNAME = "Не удалось изменить имя пользователя";
  public static final String UNABLE_TO_MOVE_WHEN_POINTER_NOT_LAST = "Ходить можно только когда указатель * на последнем ходе";
  public static final String IMPORT_SHORT_UNSUPPORTED = "Импорт сокращенной нотации не поддерживается";
  public static final String UNABLE_TO_REMOVE_VARIANT = "Не удалось удалить вариант";
  public static final String DUPLICATE_SUBSCRIBER = "Вы уже подписаны на рассылку";
  public static final String UNABLE_TO_REMOVE_BOARDBOXES = "Не удалось удалить данные статьи";
  public static final String INVALID_EMAIL = "Не корректный e-mail";
  public static final String EMAIL_NOT_BLANK = "Почта не может быть пустой";

  public static final MessageFormat INVALID_ARTICLE_TITLE = new MessageFormat("В заголовке минимум {} символа");

}
