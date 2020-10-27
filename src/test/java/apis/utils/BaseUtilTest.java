/*
 * Copyright (c) 2020
 * Contributed by NAME HERE
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package apis.utils;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BaseUtilTest {

    private BaseUtil baseUtil = new BaseUtil();

    @Test
    void sortPostEntries() {
    }

    @Test
    void getLatestTimestamp() {
    }

    @Test
    void getTimestamp() {
    }

    @Test
    void testGetTimestamp() {
    }

    @Test
    void hasErrorCode() {
    }

    @Test
    void encodeUrl() {
        String s = "thisamp;isamp;anamp;example";
        s = baseUtil.encodeUrl(s);
        Assertions.assertEquals("thisisanexample", s);
    }
}