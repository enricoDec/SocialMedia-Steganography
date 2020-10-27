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
import apis.imgur.models.ImgurPostResponse;
import apis.models.PostEntry;
import apis.utils.BaseUtil;
import com.google.gson.Gson;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static apis.utils.BlobConverterImpl.byteToFile;

public class ImgurUtil extends BaseUtil {

    public static Boolean successfullUploaded;

    public static String latestLink;

    private static final Logger logger = Logger.getLogger(ImgurUtil.class.getName());

    private ImgurAPI imgurAPI = ImgurAPI.retrofit.create(ImgurAPI.class);

    public boolean uploadPicture(byte[] file, String keyword) {
        latestLink = null;
        String filename = "tempFile";
        File f = null;

        try {
            f = new File(filename);
            byteToFile(file, filename);
        } catch (IOException e) {
            logger.info("Error converting byte-array to file.");
            logger.info(e.getMessage());
            return false;
        }

        if(f == null || !f.exists()){
            logger.info("Converted file does not exist");
        }

        final Call<ImgurPostResponse> call = imgurAPI.postImage(keyword,
                keyword,
                MultipartBody.Part.createFormData(
                        "image", filename, RequestBody.create(MediaType.parse("image/*"), f)
                ));

        call.enqueue(new Callback<ImgurPostResponse>(){
            @Override
            public void onResponse(Call<ImgurPostResponse> call, Response<ImgurPostResponse> response) {
                if (response == null) {
                    logger.info("Upload not successfull.");
                    successfullUploaded = false;
                    return;
                }
                if (response.isSuccessful()) {
                    logger.info("Upload successfull. URL: " + "http://imgur.com/" + response.body().data.id);
                    successfullUploaded = true;
                    latestLink = "http://imgur.com/" + response.body().data.id;
                }
            }

            @Override
            public void onFailure(Call<ImgurPostResponse> call, Throwable throwable) {
                logger.info("Upload not successfull. Unknown error occured.");
                successfullUploaded = false;
            }
        });

        if(successfullUploaded != null && successfullUploaded){
            successfullUploaded = null;
            return true;
        }else{
            successfullUploaded = null;
            return false;
        }
    }

    /**
     * Returns a list of Postentries (downloadlinks and timestamps) from a json-String
     * @param responseString JSON String (Reddit response)
     * @return
     */
    public List<PostEntry> getPosts(String responseString){
        List<PostEntry> postEntries = new ArrayList<>();
        ImgurGetResponse responseObject = new Gson().fromJson(responseString, ImgurGetResponse.class);

        for(ImgurGetResponse.ImgurData child : responseObject.getData()){
            if(child != null){
                postEntries.add(new PostEntry(child.getLink(), this.getTimestamp(child.getDatetime())));
            }
        }
        this.sortPostEntries(postEntries);
        return postEntries;
    }


    public void setLatestLink(String latestLink) {
        this.latestLink = latestLink;
    }

    public String getLatestLink() {
        return latestLink;
    }


}
