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

import java.util.ArrayList;
import java.util.List;

public abstract class SocialMedia {
    private String message;

    List<SocialMediaListener> socialMediaListeners = new ArrayList<SocialMediaListener>();

    public void addAsListener(SocialMediaListener socialMediaListener){
        socialMediaListeners.add(socialMediaListener);
    }

    public void removeAsListener(SocialMediaListener socialMediaListener){
        socialMediaListeners.remove(socialMediaListener);
    }

    private void updateListeners(){
        for(SocialMediaListener socialMediaListener : socialMediaListeners){
            socialMediaListener.updateSocialMediaMessage(message);
        }
    }


    abstract Token<?> getToken();

    abstract void setToken(Token<?> token);

    /**
     * Post media on this Social Media under the keyword
     * @param media data to upload
     * @param keyword keyword to search this post by
     * @return true if successful
     */
    abstract boolean postToSocialNetwork(byte[] media, String keyword);

    /**
     * Subscribe to a keyword (Hashtag / Title / ...)
     * @param keyword keyword to subscribe to
     * @return true if successful
     */
     abstract boolean subscribeToKeyword(String keyword);

    /**
     * Get Medias posted under keyword
     * @param keyword hashtag
     * @return true if successful
     */
    abstract List<byte[]> getRecentMediaForKeyword(String keyword);
}
