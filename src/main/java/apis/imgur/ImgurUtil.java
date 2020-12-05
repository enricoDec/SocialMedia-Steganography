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

/**
 * @author Mario Teklic
 */

/**
 * Imgur Utilities
 */
public class ImgurUtil extends BaseUtil {

    private static final Logger logger = Logger.getLogger(ImgurUtil.class.getName());

    /**
     * Converts a response String in json-format from an Imgur Response, to PostEntry-Objects
     * @param responseString JSON String (Imgur response)
     * @return Returns a sorted list of Postentries (downloadlinks and timestamps) from a json-String
     */
    public List<PostEntry> getPosts(String responseString){
        System.out.println(responseString);
        List<PostEntry> postEntries = new ArrayList<>();
        ImgurGetResponse responseObject = new Gson().fromJson(responseString, ImgurGetResponse.class);

        for(ImgurGetResponse.ImgurData child : responseObject.getData()){
            if(child != null){
                if(supportedFormat(child.getLink())){
                    postEntries.add(new PostEntry(child.getLink(), this.getTimestamp(child.getDatetime()), child.getType()));
                }
            }
        }
        return postEntries;
    }

    /**
     * Returns which media types are supported by this network
     */
    private boolean supportedFormat(String link) {
        return link.contains(".png");
    }

}
