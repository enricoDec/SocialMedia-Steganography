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

package apis.reddit;

import apis.MediaType;
import apis.reddit.models.MyDate;
import apis.reddit.models.PostEntry;
import apis.reddit.models.RedditResponse;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class RedditUtil {

    private final static Logger logger = Logger.getLogger(Reddit.class.getName());

    public RedditUtil(){
        logger.info("RedditUtil init.");
    }

    public MyDate getLatestTimestamp(List<PostEntry> postEntries){
        if(postEntries.size() > 0){
            return postEntries.get(postEntries.size()-1).getDate();
        }
        return null;
    }

    public MyDate getTimestamp(RedditResponse.ResponseChildData data, boolean inUTC){
        String info;
        if(inUTC){
            info = data.getData().getCreated_utc();
        }else{
            info = data.getData().getCreated();
        }

        double msDouble = Double.parseDouble(info);
        return new MyDate(new Date((long)msDouble*1000));
    }

    public String getUrl(RedditResponse.ResponseChildData data){
        return this.encodeUrl(data.getData().getPreview().getImages().getSource().getUrl());
    }

    /**
     * Returns a list of Postentries (downloadlinks and timestamps) from a json-String
     * @param responseString JSON String (Reddit response)
     * @return
     */
    public List<PostEntry> getPosts(String responseString){
        List<PostEntry> postEntries = new ArrayList<>();
        RedditResponse responseArray = new Gson().fromJson(responseString, RedditResponse.class);

        for(RedditResponse.ResponseChildData child : responseArray.getData().getChildren()){
            if(child != null && !this.hasNullObjects(child)){
                postEntries.add(new PostEntry(this.encodeUrl(this.getUrl(child)), this.getTimestamp(child, false)));
            }
        }
        this.sortPostEntries(postEntries);
        return postEntries;
    }

    public boolean hasNullObjects(RedditResponse.ResponseChildData responseChildData){
        try{
            this.getTimestamp(responseChildData, false);
            this.getUrl(responseChildData);
        }catch (Exception e){
            return true;
        }
        return false;
    }

    public String encodeUrl(String url){
        return url.replace("amp;s", "s");
    }

    public void sortPostEntries(List<PostEntry> postEntries){
        Collections.sort(postEntries);
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
