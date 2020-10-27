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

package apis.reddit.imgur;

import apis.blob.BlobConverterImpl;
import apis.reddit.RedditConstants;
import apis.utils.ParameterStringBuilder;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.*;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import static apis.blob.BlobConverterImpl.byteToFile;

public class ImgurUploader {

    public static Boolean successfullUploaded;

    private static final Logger logger = Logger.getLogger(ImgurUploader.class.getName());

    ImgurAPI imgurAPI = ImgurAPI.retrofit.create(ImgurAPI.class);

    public boolean uploadPicture(byte[] file) {
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

        final Call<ImgurResponse> call = imgurAPI.postImage("Temporary Title",
                "Temporary Upload",
                MultipartBody.Part.createFormData(
                        "image", filename, RequestBody.create(MediaType.parse("image/*"), f)
                ));


        call.enqueue(new Callback<ImgurResponse>(){
            @Override
            public void onResponse(Call<ImgurResponse> call, Response<ImgurResponse> response) {
                if (response == null) {
                    logger.info("Upload not successfull.");
                    successfullUploaded = false;
                    return;
                }
                if (response.isSuccessful()) {
                    logger.info("Upload successfull. ULR: " + "http://imgur.com/" + response.body().data.id);
                    successfullUploaded = true;
                }
            }

            @Override
            public void onFailure(Call<ImgurResponse> call, Throwable throwable) {
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
}
