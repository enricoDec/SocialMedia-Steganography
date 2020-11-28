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

import apis.models.APINames;
import apis.models.MyDate;
import apis.models.PostEntry;
import apis.reddit.models.RedditGetResponse;
import persistence.JSONPersistentManager;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class BaseUtil {

    public void sortPostEntries(List<PostEntry> postEntries){
        Collections.sort(postEntries);
    }

    public void setLatestPostTimestamp(APINames network, MyDate latestPostTimestamp) {
        JSONPersistentManager.getInstance().setLastTimeCheckedForAPI(network, latestPostTimestamp.getTime());
    }

    public MyDate getLatestStoredTimestamp(APINames network) {
        MyDate oldPostTimestamp = null;

        try {
            String oldPostTimestampString = JSONPersistentManager.getInstance().getLastTimeCheckedForAPI(network);
            oldPostTimestamp = new MyDate(new Date(Long.valueOf(oldPostTimestampString)));
        } catch (Exception e) {
            e.printStackTrace();
            oldPostTimestamp = new MyDate(new Date(0));
        }

        return oldPostTimestamp;
    }

    public List<String> getKeywordList(APINames network, String onceUsedKeyword){
        List<String> keywords = new ArrayList<>();

        if(onceUsedKeyword != null){
            keywords.add(onceUsedKeyword);
        }else{
            try {
                keywords = JSONPersistentManager.getInstance().getKeywordListForAPI(network);
                keywords.removeIf(String::isEmpty);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (onceUsedKeyword == null && keywords == null || keywords.size() == 0) {
            return null;
        }
        return keywords;
    }

    public MyDate getTimestamp(String info){
        return new MyDate(new Date(Long.valueOf(info)));
    }

    public static boolean hasErrorCode(int responseCode) {
        if (199 <= responseCode && responseCode <= 299) {
            return false;
        } else {
            return true;
        }
    }

    public static List<PostEntry> elimateOldPostEntries(MyDate latestStoredTimestamp, List<PostEntry> postEntries){
        //If current postEntry's timestamp is not newer than latestStored, filter it.
        return postEntries
                .stream()
                .filter(postEntry -> postEntry.getDate().compareTo(latestStoredTimestamp) != 1)
                .collect(Collectors.toList());
    }

    public static String encodeUrl(String url){
        return url.replace("amp;", "");
    }
}
