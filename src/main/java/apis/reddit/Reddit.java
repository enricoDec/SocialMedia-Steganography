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

import apis.*;
import apis.reddit.models.MyDate;
import apis.reddit.models.PostEntry;
import apis.reddit.models.RedditToken;
import apis.utils.BlobConverterImpl;
import apis.utils.ParameterStringBuilder;
import com.google.gson.Gson;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Reddit implements SocialMedia {

    private static final Logger logger = Logger.getLogger(Reddit.class.getName());

    private RedditUtil redditUtil;
    private boolean subscribed;
    private Token<RedditToken> token;
    private List<MediaType> supportedMedia;
    private MyDate latestPostTimestamp;
    private String latestReponse;
    private List<PostEntry> latestPostEntries;
    private Boolean newPostAvailable;
    private List<PostEntry[]> newPosts;

    public Reddit() {
        this.redditUtil = new RedditUtil();
        this.loadSupportedMedias();
    }

    private void loadSupportedMedias() {
        this.supportedMedia = new ArrayList<>();
        this.supportedMedia.add(MediaType.JPEG);
        this.supportedMedia.add(MediaType.BMP);
        this.supportedMedia.add(MediaType.PNG);
        this.supportedMedia.add(MediaType.GIF);
        this.supportedMedia.add(MediaType.TIFF);
        this.supportedMedia.add(MediaType.JPG);
    }

    @Override
    public boolean postToSocialNetwork(byte[] media, String hashtag) {
        if (this.token == null) {
            logger.info("User not logged in.");
            return false;
        }

        try {
            URL url = new URL(
                    RedditConstants.BASE +
                            RedditConstants.STATIC_SUBREDDIT +
                            RedditConstants.POST_PATH);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(RedditConstants.POST);
            con.setRequestProperty("Content-Type", "application/json");

            Map<String, String> params = new HashMap<>();
            params.put(RedditConstants.KEY_FILE, "");
            params.put(RedditConstants.KEY_HEADER, "");
            params.put(RedditConstants.KEY_IMG_TYPE, RedditConstants.VAL_IMG_TYPE);
            params.put(RedditConstants.KEY_NAME, "testname");
            //params.put(RedditConstants.KEY_UH, this.getModhash());
            params.put(RedditConstants.KEY_UPLOAD_TYPE, RedditConstants.VAL_UPLOAD_TYPE);


            con.setDoOutput(true);
            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            out.writeBytes(ParameterStringBuilder.getParamsString(params));
            out.flush();
            out.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean subscribeToKeyword(String keyword) {
        if(this.latestPostEntries == null){
            this.getRecentMediaForKeyword(keyword);
        }

        MyDate latesTimestamp = this.redditUtil.getLatestTimestamp(this.latestPostEntries);

        //compareTo: 1 == this is newser
        if(this.latestPostTimestamp == null || latesTimestamp.compareTo(this.latestPostTimestamp) == 1){
            this.newPostAvailable = true;

            /**
             * TODO
             * Neue liste mit alter liste vergleichen: alle neuen zu this.newPostentries
             */


        }else {
            this.newPostAvailable = false;
        }

        return true;
    }

    public boolean checkForNewPostEntries(String keyword){
        if(this.latestPostEntries == null){
            this.getRecentMediaForKeyword(keyword);
        }

        MyDate latesTimestamp = this.redditUtil.getLatestTimestamp(this.latestPostEntries);

        //compareTo: 1 == this is newer
        if(this.latestPostTimestamp == null || latesTimestamp.compareTo(this.latestPostTimestamp) == 1){
            this.newPostAvailable = true;
            return true;
        }else {
            this.newPostAvailable = false;
            return false;
        }
    }

    @Override
    public List<byte[]> getRecentMediaForKeyword(String keyword) {
        try {
            URL url = new URL(
                    RedditConstants.BASE +
                            RedditConstants.STATIC_SUBREDDIT + //Hier hashtage
                            RedditConstants.AS_JSON + "?" +
                    RedditConstants.KEY_SORT +
                    RedditConstants.VAL_DATE);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(RedditConstants.GET);
            con.setRequestProperty("User-agent", RedditConstants.APP_NAME);
            con.setDoOutput(true);

            BufferedReader br;

            if (!this.hasErrorCode(con.getResponseCode())) {
                logger.info("Response Code: " + con.getResponseCode() + ". No error.");
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
                logger.info("Response Code: " + con.getResponseCode() + ". Has error.");
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }

            String responseString = br.lines().collect(Collectors.joining());
            logger.info(String.valueOf(con.getURL()));

            List<PostEntry> postEntries = this.redditUtil.getPosts(responseString);
            this.latestPostEntries = postEntries;

            List<byte[]> byteList = new ArrayList<>();
            for(PostEntry pe : postEntries){
                byteList.add(BlobConverterImpl.downloadToByte(pe.getUrl()));
            }

            return byteList;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getProperty(String key) {
        Map json = new Gson().fromJson(key, Map.class);
        return (String) json.get(key);
    }

    public boolean hasErrorCode(int responseCode) {
        if (100 <= responseCode && responseCode <= 399) {
            return false;
        } else {
            return true;
        }
    }

    public boolean supportsMediaType(MediaType mediaType) {
        return this.supportedMedia.contains(mediaType);
    }

    @Override
    public Token<?> getToken() {
        return this.token;
    }

    @Override
    public void setToken(Token<?> token) {
        if(token instanceof RedditToken){
            this.token = (Token<RedditToken>) token;
        }else{
            logger.info("Must be instance of 'RedditToken'");
        }
    }

    public List<MediaType> getSupportedMedia() {
        return supportedMedia;
    }

    public void setSupportedMedia(List<MediaType> supportedMedia) {
        this.supportedMedia = supportedMedia;
    }

    public MyDate getLatestPostTimestamp() {
        return latestPostTimestamp;
    }

    public void setLatestPostTimestamp(MyDate latestPostTimestamp) {
        this.latestPostTimestamp = latestPostTimestamp;
    }

    public String getLatestReponse() {
        return latestReponse;
    }

    public void setLatestReponse(String latestReponse) {
        this.latestReponse = latestReponse;
    }

    public Boolean getNewPostAvailable() {
        return newPostAvailable;
    }

    public void setNewPostAvailable(Boolean newPostAvailable) {
        this.newPostAvailable = newPostAvailable;
    }
}
