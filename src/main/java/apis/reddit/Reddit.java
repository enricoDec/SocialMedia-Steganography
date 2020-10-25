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
import apis.reddit.models.RedditToken;
import apis.utils.ParameterStringBuilder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Reddit implements SocialMedia {

    private static final Logger logger = Logger.getLogger(Reddit.class.getName());

    private RedditUtil redditUtil;
    private SubscriptionDeamon subscriptionDeamon;
    private Token<RedditToken> token;
    private List<MediaType> supportedMedia;
    private String latestReponse;

    private TimeUnit timeUnit;
    private Integer interval;

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
                            RedditConstants.SUBREDDIT_PREFIX +
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
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        this.subscriptionDeamon = new SubscriptionDeamon(keyword);

        if(this.interval == null || this.timeUnit == null){
            executor.scheduleAtFixedRate(this.subscriptionDeamon,0 ,5, TimeUnit.MINUTES);
        }else{
            executor.scheduleAtFixedRate(this.subscriptionDeamon,0 ,this.interval, this.timeUnit);
        }

        return true;
    }

    public void changeSubscriptionInterval(TimeUnit timeUnit, Integer interval){
        this.timeUnit = timeUnit;
        this.interval = interval;
    }

    @Override
    public List<byte[]> getRecentMediaForKeyword(String keyword) {
        //Should not be calleable... should be only callable for the threaded deamon
        return this.subscriptionDeamon.getRecentMediaForKeyword(keyword);
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

    public String getLatestReponse() {
        return latestReponse;
    }

    public void setLatestReponse(String latestReponse) {
        this.latestReponse = latestReponse;
    }
}
