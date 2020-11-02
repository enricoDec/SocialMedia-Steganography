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

import apis.SocialMedia;
import apis.Token;
import apis.imgur.models.ImgurPostResponse;
import apis.reddit.BearerInterceptor;
import apis.utils.BlobConverterImpl;
import com.google.gson.Gson;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Imgur implements SocialMedia {

    private final static Logger logger = Logger.getLogger(Imgur.class.getName());
    private ImgurSubscriptionDeamon imgurSubscriptionDeamon;
    private TimeUnit timeUnit;
    private Integer interval;
    private Token token;
    private List<ImgurPostResponse> uploadedFiles;

    public Imgur() {
        this.uploadedFiles = new ArrayList<>();
    }

    /**
     * Imgur needs no token.
     *
     * @return null
     */
    @Override
    public Token getToken() {
        return this.token;
    }

    @Override
    public void setToken(Token token) {
        this.token = token;
    }

    @Override
    public boolean postToSocialNetwork(byte[] media, String keyword) {
        System.out.println(this.getToken() == null);
        System.out.println(this.getToken().getToken() == null);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new BearerInterceptor(this.getToken().getToken())).build();

        String filename = "tmp_" + System.currentTimeMillis() + ".jpg";
        RequestBody body = null;
        try {
            body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("image", filename, RequestBody.create(BlobConverterImpl.byteToFile(media, "tmp.jpg"), MediaType.parse("image/*")))
                    .addFormDataPart("title", keyword)
                    .addFormDataPart("description", "Hello World!")
                    .build();

            Request request = new Request.Builder()
                    .headers(Headers.of("Authorization", ("Bearer " + this.token.getToken())))
                    .url(ImgurConstants.UPLOAD_URL + ".json")
                    .post(body)
                    .build();

            Response response = client.newCall(request).execute();
            Gson gson = new Gson();
            ImgurPostResponse ipr = gson.fromJson(response.body().string(), ImgurPostResponse.class);
            this.uploadedFiles.add(ipr);
            logger.info("Successfull uploaded.\nURL: " + ipr.data.link);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean subscribeToKeyword(String keyword) {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        this.imgurSubscriptionDeamon = new ImgurSubscriptionDeamon(keyword);

        if (this.interval == null || this.timeUnit == null) {
            executor.scheduleAtFixedRate(this.imgurSubscriptionDeamon, 0, 5, TimeUnit.MINUTES);
        } else {
            executor.scheduleAtFixedRate(this.imgurSubscriptionDeamon, 0, this.interval, this.timeUnit);
        }
        return true;
    }

    public void changeSubscriptionInterval(TimeUnit timeUnit, Integer interval) {
        this.timeUnit = timeUnit;
        this.interval = interval;
    }

    @Override
    public List<byte[]> getRecentMediaForKeyword(String keyword) {
        return this.imgurSubscriptionDeamon.getRecentMediaForKeyword(keyword);
    }
}
