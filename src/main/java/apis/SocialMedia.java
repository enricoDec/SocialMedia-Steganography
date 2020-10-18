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

package apis;

public interface SocialMedia {

    /**
     * Sign in Social Network
     * @param Username username
     * @param password password
     * @return true if successful
     */
    boolean signInSocialNetwork(String Username, String password);

    /**
     * Post data on Social Media
     * @param media data to upload
     * @param hashtag hastag
     * @return true if successful
     */
    boolean postToSocialNetwork(byte[] media, String hashtag);

    /**
     * Subscribe to a hashtag
     * @param hashtag hashtag
     * @return true if successful
     */
    boolean subscribeToHashtag(String hashtag);

    /**
     * Get Medias
     * @param hashtag hashtag
     * @return true if successful
     */
    byte[] getRecentMediaForHashtag(String hashtag);
}