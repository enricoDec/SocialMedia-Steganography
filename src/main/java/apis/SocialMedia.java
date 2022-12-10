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

import apis.models.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


public abstract class SocialMedia {

    public static final Integer DEFAULT_INTERVALL = 5;
    private static final Logger logger = Logger.getLogger(SocialMedia.class.getName());
    List<SocialMediaListener> socialMediaListeners = new ArrayList<SocialMediaListener>();
    private List<String> message;

    public void addAsListener(SocialMediaListener socialMediaListener) {
        socialMediaListeners.add(socialMediaListener);
    }

    public void removeAsListener(SocialMediaListener socialMediaListener) {
        socialMediaListeners.remove(socialMediaListener);
    }

    public void updateListeners(List<String> msgList) {
        this.message = msgList;
        message.stream().forEach(msg -> logger.info("Update contains: " + msg));
        for (SocialMediaListener socialMediaListener : socialMediaListeners) {
            logger.info("Update Listener");
            socialMediaListener.updateSocialMediaMessage(message);
        }
    }

    //TODO setMessage?

    public abstract Token getToken();

    public abstract void setToken(Token token);

    /**
     * Post media on this Social Media under the keyword if you don't have a token yet
     *
     * @param media     data to upload
     * @param mediaType media type
     * @param keyword   keyword to search this post by
     * @return true if successful
     */
    public abstract boolean postToSocialNetwork(byte[] media, MediaType mediaType, String keyword);

    /**
     * Post media on this Social Media under the keyword if you already have your Token
     *
     * @param media     data to upload
     * @param mediaType media type
     * @param token     Token
     * @param keyword   keyword to search this post by
     * @return true if successful
     */
    public abstract boolean postToSocialNetwork(byte[] media, MediaType mediaType, String keyword, Token token);

    /**
     * Subscribe to a keyword (Hashtag / Title / ...)
     *
     * @param keyword keyword to subscribe to
     * @return true if successful
     */
    public abstract boolean subscribeToKeyword(String keyword);

    public abstract boolean unsubscribeKeyword(String keyword);

    /**
     * Get Medias posted under keyword
     *
     * @param keyword hashtag
     * @return true if successful
     */
    public abstract List<byte[]> getRecentMediaForKeyword(String keyword);

    public abstract void stopSearch();

    public abstract void startSearch();

    public abstract void changeSchedulerPeriod(Integer interval);

    public abstract String getApiName();

    public abstract List<String> getAllSubscribedKeywords();

    public abstract void setBlogname(String blogname);

}
