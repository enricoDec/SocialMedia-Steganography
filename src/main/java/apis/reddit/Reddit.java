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
import apis.Token;
import okhttp3.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

public class Reddit implements SocialMedia {

    private static final Logger logger = Logger.getLogger(Reddit.class.getName());

    private RedditUtil redditUtil;
    private RedditSubscriptionDeamon redditSubscriptionDeamon;
    private Token token;
    private List<MediaType> supportedMedia;
    private String latestReponse;

    private TimeUnit timeUnit;
    private Integer interval;

    public Reddit() {
        SimpleFormatter fmt = new SimpleFormatter();
        StreamHandler sh = new StreamHandler(System.out, fmt);
        logger.addHandler(sh);
        this.redditUtil = new RedditUtil();
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
        }

        /*ImgurUtil imgur = new ImgurUtil();
        if (!imgur.uploadPicture(media, hashtag)) {
            logger.info("Upload not successfull.");
            return false;
        }
        String imgUrl = imgur.getLatestLink() + ".jpg";*/
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new BearerInterceptor(this.getToken().getToken())).build();

        RequestBody mBody = null;
        String filename = "";

        try {

            filename = "tmp_" + System.currentTimeMillis() + ".jpg";
            mBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(RedditConstants.KEY_FILE, filename, RequestBody.create(media, MediaType.parse("image/jpg")))
                    .addFormDataPart(RedditConstants.KEY_HEADER, "1")
                    .addFormDataPart("api_type", "json")
                    .addFormDataPart(RedditConstants.KEY_IMG_TYPE, RedditConstants.VAL_IMG_TYPE)
                    .addFormDataPart(RedditConstants.KEY_NAME, "testname")
                    .addFormDataPart(RedditConstants.KEY_UPLOAD_TYPE, RedditConstants.VAL_UPLOAD_TYPE)
                    .build();


            /**
             * RequestBody body = new FormBody.Builder().
             *                 add(RedditConstants.KEY_FILE, imgUrl).
             *                 add(RedditConstants.KEY_HEADER, "1").
             *                 add(RedditConstants.KEY_IMG_TYPE, RedditConstants.VAL_IMG_TYPE).
             *                 add(RedditConstants.KEY_NAME, "testname").
             *                 add(RedditConstants.KEY_UPLOAD_TYPE, RedditConstants.VAL_UPLOAD_TYPE)
             *                 .build();
             */

            //https://oauth.reddit.com//https://oauth.reddit.com//https://oauth.reddit.com
            Request request = new Request.Builder()
                    .url("https://oauth.reddit.com" +
                            RedditConstants.SUBREDDIT_PREFIX +
                            hashtag +
                            "/api" +
                            RedditConstants.POST_PATH)
                    .post(mBody)
                    .build();

            Response response = client.newCall(request).execute();
            String responseString = response.body().string();
            System.out.println("Response String: " + responseString);
        } catch (Exception e) {
            logger.info("Error while creating request body.");
            e.printStackTrace();
        }

        File f = new File(filename);
        if(f.exists()){
            f.delete();
        }


        /*
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                logger.info("Failed to upload.");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                logger.info("Reponse: " + response.headers());
                String resp = response.body().string();
                logger.info(resp);
            }
        });*/

/*
            URL url = new URL(
                    RedditConstants.BASE +
                            RedditConstants.SUBREDDIT_PREFIX +
                            hashtag +
                            RedditConstants.POST_PATH);

            logger.info(url.toString());

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(RedditConstants.POST);
            con.setRequestProperty("Authorization", "Bearer " + token.getAuth().toString());
            con.setRequestProperty("User-Agent", RedditConstants.APP_NAME + " by User");

            Map<String, String> params = new HashMap<>();
            params.put(RedditConstants.KEY_FILE, imgUrl);
            params.put(RedditConstants.KEY_HEADER, "1");
            params.put(RedditConstants.KEY_IMG_TYPE, RedditConstants.VAL_IMG_TYPE);
            params.put(RedditConstants.KEY_NAME, "testname");
            params.put(RedditConstants.KEY_UPLOAD_TYPE, RedditConstants.VAL_UPLOAD_TYPE);

            con.setDoOutput(true);
            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            out.writeBytes(ParameterStringBuilder.getParamsString(params));
            out.flush();
            out.close();

            logger.info(con.getResponseMessage());
            return true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        return false;
    }

    @Override
    public boolean subscribeToKeyword(String keyword) {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        this.redditSubscriptionDeamon = new RedditSubscriptionDeamon(keyword);

        if (this.interval == null || this.timeUnit == null) {
            executor.scheduleAtFixedRate(this.redditSubscriptionDeamon, 0, 5, TimeUnit.MINUTES);
        } else {
            executor.scheduleAtFixedRate(this.redditSubscriptionDeamon, 0, this.interval, this.timeUnit);
        }

        return true;
    }

    public void changeSubscriptionInterval(TimeUnit timeUnit, Integer interval) {
        this.timeUnit = timeUnit;
        this.interval = interval;
    }

    @Override
    public List<byte[]> getRecentMediaForKeyword(String keyword) {
        if (this.redditSubscriptionDeamon == null) {
            logger.info("Subscription Deamon is not running.");
            return null;
        }
        //Should not be calleable... should be only callable for the threaded deamon
        return this.redditSubscriptionDeamon.getRecentMediaForKeyword(keyword);
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
