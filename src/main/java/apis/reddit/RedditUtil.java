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
import apis.models.MyDate;
import apis.models.PostEntry;
import apis.reddit.models.RedditAboutResponse;
import apis.reddit.models.RedditGetResponse;
import apis.utils.BaseUtil;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class RedditUtil extends BaseUtil {

    private final static Logger logger = Logger.getLogger(Reddit.class.getName());

    public String getUrl(RedditGetResponse.ResponseChildData data){
        return this.encodeUrl(data.getData().getPreview().getImages().getSource().getUrl());
    }

    public MyDate getTimestamp(RedditGetResponse.ResponseChildData data, boolean inUTC){
        String info;
        if(inUTC){
            info = data.getData().getCreated_utc();
        }else{
            info = data.getData().getCreated();
        }

        return this.getTimestamp(info);
    }

    /**
     * Returns a list of Postentries (downloadlinks and timestamps) from a json-String
     * @param responseString JSON String (Reddit response)
     * @return
     */
    public List<PostEntry> getPosts(String responseString){
        List<PostEntry> postEntries = new ArrayList<>();

        System.out.println("REDDIT JSON RESPONSE: " + responseString);
        try{
            RedditGetResponse responseArray = new Gson().fromJson(responseString, RedditGetResponse.class);
        for(RedditGetResponse.ResponseChildData child : responseArray.getData().getChildren()){
            if(child != null && !this.hasNullObjects(child)){
                postEntries.add(new PostEntry(this.encodeUrl(this.getUrl(child)), this.getTimestamp(child, false), ".png"));
            }
        }

        }catch (Exception e){
            e.printStackTrace();
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

    public boolean isImageUploadAllowed(String subreddit){
        try {
            URL url = new URL(RedditConstants.BASE +
                    RedditConstants.SUBREDDIT_PREFIX + subreddit +
                    "/about" + RedditConstants.AS_JSON);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(RedditConstants.GET);
            con.setRequestProperty("User-agent", RedditConstants.APP_NAME);
            con.setDoOutput(true);

            String responseString = "";

            if (!this.hasErrorCode(con.getResponseCode())) {
                responseString = new BufferedReader(new InputStreamReader(con.getInputStream())).lines().collect(Collectors.joining());
                logger.info("Response Code: " + con.getResponseCode() + ". No error.");
            } else {
                logger.info("Response Code: " + con.getResponseCode() + ". Has error.");
                return false;
            }

            RedditAboutResponse redditAboutResponse = new Gson().fromJson(responseString, RedditAboutResponse.class);
            return redditAboutResponse.getData().isAllow_images();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
