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

package apis.models;

/**
 * resembles oAuth1 Tokens
 * oAuth1 Token consists of a pair of tokens
 * accessToken and accessTokenSecret
 */
public class Token {

    /**
     * Code of the accessToken for oAuth1
     */
    private String accessToken;

    /**
     * code of accessTokenSecret for oAuth1
     */
    private String accessTokenSecret;

    public Token() {
    }

    public Token(String acessToken, String accessTokenSecret) {
        this.accessToken = acessToken;
        this.accessTokenSecret = accessTokenSecret;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String token) {
        this.accessToken = token;
    }

    public String getAccessTokenSecret() {
        return accessTokenSecret;
    }

    public void setAccessTokenSecret(String token) {
        this.accessTokenSecret = token;
    }
}
