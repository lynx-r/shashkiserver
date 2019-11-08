/*
 * © Copyright
 *
 * DeepClone.java is part of shashkiserver.
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

import com.rits.cloning.Cloner;

/**
 * Created by Aleksey Popryaduhin on 10:38 20/09/2017.
 */
@SuppressWarnings("unchecked")
public interface DeepClone {
  default <T> T deepClone() {
    Cloner cloner = Cloner.shared();
//    cloner.setDumpClonedClasses(true);
    return cloner.deepClone((T) this);
  }
}
