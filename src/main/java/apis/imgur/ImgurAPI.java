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

package apis.imgur;

import apis.imgur.models.ImgurResponse;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.http.*;


public interface ImgurAPI {
    String server = "https://api.imgur.com/3/";

    @Multipart
    @Headers({"Authorization: Client-ID 6d628f37c5f9729"})
    @POST("image")
    Call<ImgurResponse> postImage(
            @Query("title") String title,
            @Query("description") String description,
            @Part MultipartBody.Part file);

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(server)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
