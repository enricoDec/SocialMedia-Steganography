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

import apis.models.MyDate;
import apis.models.PostEntry;
import apis.reddit.Reddit;
import apis.reddit.RedditUtil;
import apis.reddit.models.RedditGetResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class BaseUtilTest {

    private BaseUtil baseUtil = new BaseUtil();

    @Test
    void sortPostEntries() {
        Date d1 = new Date();
        d1.setTime(100);
        MyDate m1 = new MyDate(d1);
        PostEntry p1 = new PostEntry("", m1);

        Date d2 = new Date();
        d2.setTime(10000);
        MyDate m2 = new MyDate(d1);
        PostEntry p2 = new PostEntry("", m2);

        Date d3 = new Date();
        d3.setTime(1000000);
        MyDate m3 = new MyDate(d1);
        PostEntry p3 = new PostEntry("", m3);

        Date d4 = new Date();
        d4.setTime(100000000);
        MyDate m4 = new MyDate(d1);
        PostEntry p4 = new PostEntry("", m4);

        Date d5 = new Date();
        d5.setTime(2000000000);
        MyDate m5 = new MyDate(d1);
        PostEntry p5 = new PostEntry("", m5);

        List<PostEntry> list = new ArrayList<>();
        list.add(p1);
        list.add(p2);
        list.add(p3);
        list.add(p4);
        list.add(p5);

        baseUtil.sortPostEntries(list);

        Assertions.assertEquals(p1, list.get(0));
        Assertions.assertEquals(p2, list.get(1));
        Assertions.assertEquals(p3, list.get(2));
        Assertions.assertEquals(p4, list.get(3));
        Assertions.assertEquals(p5, list.get(4));
    }

    @Test
    void getLatestTimestamp() {
        Date d1 = new Date();
        d1.setTime(100);
        MyDate m1 = new MyDate(d1);
        PostEntry p1 = new PostEntry("", m1);

        Date d2 = new Date();
        d2.setTime(10000);
        MyDate m2 = new MyDate(d1);
        PostEntry p2 = new PostEntry("", m2);

        Date d3 = new Date();
        d3.setTime(1000000);
        MyDate m3 = new MyDate(d1);
        PostEntry p3 = new PostEntry("", m3);

        Date d4 = new Date();
        d4.setTime(100000000);
        MyDate m4 = new MyDate(d1);
        PostEntry p4 = new PostEntry("", m4);

        Date d5 = new Date();
        d5.setTime(2000000000);
        MyDate m5 = new MyDate(d1);
        PostEntry p5 = new PostEntry("", m5);

        List<PostEntry> list = new ArrayList<>();
        list.add(p1);
        list.add(p2);
        list.add(p3);
        list.add(p4);
        list.add(p5);

        MyDate latest = baseUtil.getLatestTimestamp(list);

        Assertions.assertEquals(d1.getTime(), latest.getTime());
    }

    @Test
    void testGetTimestamp_byString() {
        String dateValue = "1603813248";
        MyDate myDate = baseUtil.getTimestamp(dateValue);

        Assertions.assertEquals(2020, myDate.getYear());
        Assertions.assertEquals(10, myDate.getMonth());
        Assertions.assertEquals(27, myDate.getDay());
        Assertions.assertEquals(16, myDate.getHour());
        Assertions.assertEquals(40, myDate.getMinute());
        Assertions.assertEquals(48, myDate.getSecond());

    }

    @Test
    void testGetTimestamp_byResponseData() {
        String dateValue = "1603813248";
        RedditGetResponse.ResponseChildData data = new RedditGetResponse.ResponseChildData();
        RedditGetResponse.ChildData cData = new RedditGetResponse.ChildData();
        cData.setCreated_utc(dateValue);
        data.setData(cData);

        MyDate myDate = new RedditUtil().getTimestamp(data, true);

        Assertions.assertEquals(2020, myDate.getYear());
        Assertions.assertEquals(10, myDate.getMonth());
        Assertions.assertEquals(27, myDate.getDay());
        Assertions.assertEquals(16, myDate.getHour());
        Assertions.assertEquals(40, myDate.getMinute());
        Assertions.assertEquals(48, myDate.getSecond());
    }

    @Test
    void hasErrorCode() {
        int e1 = 400;
        int e2 = 404;
        int g1 = 200;

        Assertions.assertTrue(baseUtil.hasErrorCode(e1));
        Assertions.assertTrue(baseUtil.hasErrorCode(e2));
        Assertions.assertFalse(baseUtil.hasErrorCode(g1));
    }

    @Test
    void encodeUrl() {
        String s = "thisamp;isamp;anamp;example";
        s = baseUtil.encodeUrl(s);
        Assertions.assertEquals("thisisanexample", s);
    }
}
