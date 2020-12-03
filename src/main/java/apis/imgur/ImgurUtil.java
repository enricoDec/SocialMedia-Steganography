/*
 * Copyright (c) 2020
 * Contributed by Mario Teklic
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

package apis.imgur;

import apis.imgur.models.ImgurGetResponse;
import apis.models.PostEntry;
import apis.utils.BaseUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;


public class ImgurUtil extends BaseUtil {

    public static String latestLink;

    private static final Logger logger = Logger.getLogger(ImgurUtil.class.getName());

    public ImgurUtil(){
        SimpleFormatter fmt = new SimpleFormatter();
        StreamHandler sh = new StreamHandler(System.out, fmt);
        logger.addHandler(sh);
    }

    /**
     * Returns a sorted list of Postentries (downloadlinks and timestamps) from a json-String
     * @param responseString JSON String (Reddit response)
     * @return
     */
    public List<PostEntry> getPosts(String responseString){
        List<PostEntry> postEntries = new ArrayList<>();
        ImgurGetResponse responseObject = new Gson().fromJson(responseString, ImgurGetResponse.class);

        for(ImgurGetResponse.ImgurData child : responseObject.getData()){
            if(child != null){
                if(supportedFormat(child.getLink())){
                    postEntries.add(new PostEntry(child.getLink(), this.getTimestamp(child.getDatetime()), child.getType()));
                }
            }
        }
        this.sortPostEntries(postEntries);
        return postEntries;
    }

    private boolean supportedFormat(String link) {
        return link.contains(".png");
    }


    public void setLatestLink(String latestLink) {
        this.latestLink = latestLink;
    }

    public String getLatestLink() {
        return latestLink;
    }

}
