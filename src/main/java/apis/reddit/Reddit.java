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
import apis.utils.BaseUtil;
import apis.utils.BlobConverterImpl;
import com.google.gson.Gson;
import okhttp3.*;
import persistence.JSONPersistentManager;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static apis.models.APINames.IMGUR;
import static apis.models.APINames.REDDIT;

/**
 * @author Mario Teklic
 */

/**
 * Reddit social media can upload, download, check for new postentries for a specific keyword/subreddit
 */
public class Reddit extends SocialMedia {

    private static final Logger logger = Logger.getLogger(Reddit.class.getName());

    /**
     * Utilities which are used while uploading, download, searching
     */
    private RedditUtil redditUtil;

    /**
     * For uploading and searching once or in an given interval for new post entries.
     * Asynchron.
     */
    private RedditSubscriptionDeamon redditSubscriptionDeamon;

    /**
     * Token which is needed while uploading a new image
     */
    private Token token;

    /**
     * Manages the interval called search for new post entries
     */
    private ScheduledExecutorService executor;

    /**
     * Future of scheduler service
     */
    private ScheduledFuture scheduledFuture;

    /**
     * Default constructor. Prepares the subscription deamon, utils and the executor.
     */
    public Reddit() {
        this.redditUtil = new RedditUtil();
        this.redditSubscriptionDeamon = new RedditSubscriptionDeamon();
        executor = Executors.newSingleThreadScheduledExecutor();
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

            if(url == null || url.isEmpty())
                return false;

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
            if(!BaseUtil.hasErrorCode(respCode)){
                RedditPostResponse rpr = new Gson().fromJson(responseString, RedditPostResponse.class);
                logger.info("Uploaded: " + rpr.getJson().getData().getUrl());
                return true;
            }
        } catch (Exception e) {
            logger.info("Error while creating new post on reddit.");
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Listens for new post entries in imgur network for stored keywords.
     * Asynchron.
     * @param interval Interval in minutes
     */
    public void changeSchedulerPeriod(Integer interval) {
        if (scheduledFuture != null && !scheduledFuture.isCancelled())
            scheduledFuture.cancel(false);

        if (interval == null) {
            scheduledFuture = executor.schedule(this.redditSubscriptionDeamon, 5, TimeUnit.MINUTES);
        } else {
            scheduledFuture = executor.schedule(this.redditSubscriptionDeamon, interval, TimeUnit.MINUTES);
        }
    }

    /**
     * Returns if the Subscription deamon is running
     * @return
     */
    public boolean isSchedulerRunning(){
        return !scheduledFuture.isCancelled() && !scheduledFuture.isDone();
    }

    @Override
    public boolean subscribeToKeyword(String keyword) {
        this.redditUtil.storeKeyword(REDDIT, keyword);
        changeSchedulerPeriod(DEFAULT_INTERVALL);
        return true;
    }

    @Override
    public List<byte[]> getRecentMediaForKeyword(String keyword) {
        return Optional.ofNullable(this.redditSubscriptionDeamon.getRecentMediaForSubscribedKeywords(keyword))
                .orElseGet(Collections::emptyList)
                .stream()
                .map(entry -> BlobConverterImpl.downloadToByte(entry.getUrl()))
                .collect(Collectors.toList());
    }

    @Override
    public Token getToken() {
        return this.token;
    }

    @Override
    public void setToken(Token token) {
        this.token = token;
    }

    @Override
    public APINames getApiName() {
        return REDDIT;
    }

    @Override
    public List<String> getAllSubscribedKeywords() {
        try{
            return JSONPersistentManager.getInstance().getKeywordListForAPI(REDDIT);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    @Override
    public void unsubscribe() {
        if (!executor.isShutdown())
            executor.shutdown();
    }
}
