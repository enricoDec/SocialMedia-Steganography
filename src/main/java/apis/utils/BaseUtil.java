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
import apis.reddit.models.RedditGetResponse;

import java.net.URLEncoder;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class BaseUtil {

    public void sortPostEntries(List<PostEntry> postEntries){
        Collections.sort(postEntries);
    }

    public MyDate getLatestTimestamp(List<PostEntry> postEntries){
        if(postEntries.size() > 0){
            return postEntries.get(postEntries.size()-1).getDate();
        }
        return null;
    }

    public MyDate getTimestamp(String info){
        double msDouble = Double.parseDouble(info);
        return new MyDate(new Date((long)msDouble*1000));
    }

    public static boolean hasErrorCode(int responseCode) {
        if (100 <= responseCode && responseCode <= 399) {
            return false;
        } else {
            return true;
        }
    }

    public static String encodeUrl(String url){
        return url.replace("amp;", "");
    }
}
