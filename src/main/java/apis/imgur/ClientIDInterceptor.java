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

import apis.reddit.RedditConstants;
import okhttp3.*;
import okio.Buffer;

import java.io.IOException;
import java.util.Base64;
import java.util.logging.Logger;

public class ClientIDInterceptor implements Interceptor {

    private static final Logger logger = Logger.getLogger(ClientIDInterceptor.class.getName());
    private String token;

    public ClientIDInterceptor(String token){
        this.token = token;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request tokenedRequest = request.newBuilder()
                .addHeader("User-Agent", RedditConstants.APP_NAME + " by User")
                .build();

        logger.info("Sending post to " + tokenedRequest.url() + " with headers: " + tokenedRequest.headers());
        logger.info("Method: " + request.method());
        Buffer buffer = new Buffer();
        tokenedRequest.body().writeTo(buffer);
        logger.info("Body: " + buffer.readUtf8());

        Response response = chain.proceed(request);
        logger.info("Received response for " + response.request().url() + "\nHeaders: " + response.headers());

        return response;
    }
}
