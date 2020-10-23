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

public interface RedditConstants {
    String APP_NAME = "SharksystemsStega";
    String APP_KEY = "Eo97Fa1dmOfZhQ";
    String APP_SECRET = "Cbv2mblDgF6LiKQNXcZI7BaICC4";

    //********* KEY - VALUE * LOGIN ***************************************

    String KEY_CLIENT_ID = "client_id=";
    String VAL_CLIENT_ID = "Cbv2mblDgF6LiKQNXcZI7BaICC4";

    String KEY_RESPONSE_TYPE = "response_type=";
    String VAL_RESPONSE_TYPE = "code";

    String KEY_STATE = "state=";
    String VAL_STATE = "randomString";

    String KEY_REDIRECT_URI = "redirect_uri=";
    String VAL_REDIRECT_URI = "http://www.sharksystem.net/";

    String KEY_DURATION = "duration=";
    String VAL_DURATION_TEMP = "temporary"; //for up- and downloading
    String VAL_DURATION_PERM = "permanent"; //for register for a hashtag

    String KEY_SCOPE = "scope=";
    String VAL_SCOPE_GET = "read";
    String VAL_SCOPE_POST = "";

    String KEY_RAW_JSON = "raw_json=";
    String VAL_RAW_JSON_TRUE = "1";


    //********* PATH COMPONENTS *******************************************

    String BASE = "https://www.reddit.com";
    String API = "/api";
    String ME = "/v1/me";
    String AUTH = "/v1/authorize";
    String LOGIN = "/login";

    String STATIC_SUBREDDIT = "/r/nature";

    String POST_PATH = "/upload_sr_img";

    String GET_PATH = "/...";



    //************ HTTP METHODS ****************************************

    String POST = "POST";
    String GET = "GET";


    //********* KEY - VALUE * POST ***************************************

    String KEY_FILE = "file=";
    String KEY_FORMID = "formid=";
    String KEY_HEADER = "header=";
    String KEY_IMG_TYPE = "img_type=";
    String VAL_IMG_TYPE = "jpg";
    String KEY_NAME = "name=";
    String KEY_UPLOAD_TYPE = "upload_type";
    String VAL_UPLOAD_TYPE = "img";
    String KEY_UH = "uh=";
    String AS_JSON = ".json";
    String KEY_USERNAME = "username=";
    String KEY_PASSWORD = "password=";
}
