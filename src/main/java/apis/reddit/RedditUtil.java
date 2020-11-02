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

package apis.reddit;

import apis.MediaType;
import apis.models.PostEntry;
import apis.reddit.models.RedditGetResponse;
import apis.utils.BaseUtil;
import com.google.gson.Gson;

import java.util.*;
import java.util.logging.Logger;

public class RedditUtil extends BaseUtil {

    private final static Logger logger = Logger.getLogger(Reddit.class.getName());

    public String getUrl(RedditGetResponse.ResponseChildData data){
        return this.encodeUrl(data.getData().getPreview().getImages().getSource().getUrl());
    }

    /**
     * Returns a list of Postentries (downloadlinks and timestamps) from a json-String
     * @param responseString JSON String (Reddit response)
     * @return
     */
    public List<PostEntry> getPosts(String responseString){
        List<PostEntry> postEntries = new ArrayList<>();
        RedditGetResponse responseArray = new Gson().fromJson(responseString, RedditGetResponse.class);

        for(RedditGetResponse.ResponseChildData child : responseArray.getData().getChildren()){
            if(child != null && !this.hasNullObjects(child)){
                postEntries.add(new PostEntry(this.encodeUrl(this.getUrl(child)), this.getTimestamp(child, false)));
            }
        }
        this.sortPostEntries(postEntries);
        return postEntries;
    }

    public boolean hasNullObjects(RedditGetResponse.ResponseChildData responseChildData){
        try{
            this.getTimestamp(responseChildData, false);
            this.getUrl(responseChildData);
        }catch (Exception e){
            return true;
        }
        return false;
    }

    public MediaType getMediaType(String url){
        if(url.contains(MediaType.BMP.name().toLowerCase())){
            return MediaType.BMP;
        }else if(url.contains(MediaType.GIF.name().toLowerCase())){
            return MediaType.GIF;
        }else if(url.contains(MediaType.JPEG.name().toLowerCase())){
            return MediaType.JPEG;
        }else if(url.contains(MediaType.PNG.name().toLowerCase())){
            return MediaType.PNG;
        }else if(url.contains(MediaType.TIFF.name().toLowerCase())){
            return MediaType.TIFF;
        }else if(url.contains(MediaType.JPG.name().toLowerCase())){
            return MediaType.JPG;
        }
        return null;
    }
}
