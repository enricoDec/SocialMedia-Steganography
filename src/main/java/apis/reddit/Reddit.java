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

import apis.SocialMedia;
import apis.models.APINames;
import apis.models.Token;
import apis.imgur.Imgur;
import apis.interceptors.BearerInterceptor;
import apis.reddit.models.RedditPostResponse;
import com.google.gson.Gson;
import okhttp3.*;
import persistence.JSONPersistentManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static apis.models.APINames.REDDIT;

public class Reddit extends SocialMedia {

    /**
     * TODO Keywords, Timestamp
     * Reddit, Imgur
     */

    private static final Logger logger = Logger.getLogger(Reddit.class.getName());
    private RedditUtil redditUtil;
    private List<RedditPostResponse> uploadedFiles;
    private RedditSubscriptionDeamon redditSubscriptionDeamon;
    private Token token;
    private List<MediaType> supportedMedia;
    private String latestReponse;

    private TimeUnit timeUnit;
    private Integer interval;

    public Reddit() {
        this.redditUtil = new RedditUtil();
        this.uploadedFiles = new ArrayList<>();
        this.loadSupportedMedias();
    }

    private void loadSupportedMedias() {
        this.supportedMedia = new ArrayList<>();
    }

    @Override
    public boolean postToSocialNetwork(byte[] media, String hashtag) {
        if (this.token == null) {
            logger.info("User not logged in.");
            return false;
        }else if(!this.redditUtil.isImageUploadAllowed(hashtag)){
            logger.info("Subreddit '" + hashtag + "' does not allow to upload images.");
            return false;
        }

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new BearerInterceptor()).build();

        RequestBody mBody = null;

        try {

            String url = Imgur.uploadPicture(media, hashtag).data.link;

            mBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("title", "Hello World")
                    .addFormDataPart("kind", "image")
                    .addFormDataPart("text", "Baby Yoda.")
                    .addFormDataPart("sr", hashtag)
                    .addFormDataPart("resubmit", "true")
                    .addFormDataPart("send_replies", "true")
                    .addFormDataPart("api_type", "json")
                    .addFormDataPart("url", url)
                    .build();

            Request request = new Request.Builder()
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .headers(Headers.of("Authorization", ("Bearer " + this.token.getToken())))
                    .url(RedditConstants.OAUTH_BASE +
                            RedditConstants.UPLOAD_PATH)
                    .post(mBody)
                    .build();

            Response response = client.newCall(request).execute();
            String responseString = response.body().string();
            int respCode = response.code();
            logger.info("Response code: " + respCode);
            if(199 < respCode && respCode < 399){
                RedditPostResponse rpr = new Gson().fromJson(responseString, RedditPostResponse.class);
                logger.info("Uploaded: " + rpr.getJson().getData().getUrl());
                this.uploadedFiles.add(rpr);
                return true;
            }
        } catch (Exception e) {
            logger.info("Error while creating new post on reddit.");
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean subscribeToKeyword(String keyword) {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        this.redditSubscriptionDeamon = new RedditSubscriptionDeamon(keyword);

        if (this.interval == null || this.timeUnit == null) {
            executor.scheduleAtFixedRate(this.redditSubscriptionDeamon, 0, 30, TimeUnit.MINUTES);
        } else {
            executor.scheduleAtFixedRate(this.redditSubscriptionDeamon, 0, this.interval, this.timeUnit);
        }

        JSONPersistentManager.getInstance().addKeywordForAPI(REDDIT, keyword);

        return true;
    }

    public void changeSubscriptionInterval(TimeUnit timeUnit, Integer interval) {
        this.timeUnit = timeUnit;
        this.interval = interval;
    }

    @Override
    public List<byte[]> getRecentMediaForKeyword(String keyword) {
        return new RedditSubscriptionDeamon(keyword).getRecentMedia();
    }

    public boolean supportsMediaType(MediaType mediaType) {
        return this.supportedMedia.contains(mediaType);
    }

    @Override
    public Token getToken() {
        return this.token;
    }

    @Override
    public void setToken(Token token) {
        this.token = token;
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
