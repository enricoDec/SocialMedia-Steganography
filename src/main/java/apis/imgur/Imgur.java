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

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

public class Imgur implements SocialMedia {

    private ImgurUtil imgurUtil;
    private ImgurSubscriptionDeamon imgurSubscriptionDeamon;
    private TimeUnit timeUnit;
    private Integer interval;

    public Imgur(){
        this.imgurUtil = new ImgurUtil();
    }

    /**
     * Imgur needs no token.
     * @return null
     */
    @Override
    public Token getToken() {
        return null;
    }

    @Override
    public void setToken(Token token) {
    }

    @Override
    public boolean postToSocialNetwork(byte[] media, String keyword) {
        return this.imgurUtil.uploadPicture(media, keyword);
    }

    @Override
    public boolean subscribeToKeyword(String keyword) {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        this.imgurSubscriptionDeamon = new ImgurSubscriptionDeamon(keyword);

        if(this.interval == null || this.timeUnit == null){
            executor.scheduleAtFixedRate(this.imgurSubscriptionDeamon,0 ,5, TimeUnit.MINUTES);
        }else{
            executor.scheduleAtFixedRate(this.imgurSubscriptionDeamon,0 ,this.interval, this.timeUnit);
        }
        return true;
    }

    public void changeSubscriptionInterval(TimeUnit timeUnit, Integer interval){
        this.timeUnit = timeUnit;
        this.interval = interval;
    }

    @Override
    public List<byte[]> getRecentMediaForKeyword(String keyword) {
        return this.imgurSubscriptionDeamon.getRecentMediaForKeyword(keyword);
    }
}
