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

import apis.reddit.models.MyDate;
import apis.reddit.models.PostEntry;
import apis.utils.BlobConverterImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class SubscriptionDeamon implements Runnable{

    private static final Logger logger = Logger.getLogger(SubscriptionDeamon.class.getName());
    private RedditUtil redditUtil;

    private String subscriptionKeyword;
    private List<PostEntry> latestPostEntries;
    private MyDate latestPostTimestamp;
    private boolean newPostAvailable;

    /**
     * Subcription for a Keyword in a Social Media
     * @param subscriptionKeyword The keyword
     */
    public SubscriptionDeamon(String subscriptionKeyword){
        this.subscriptionKeyword = subscriptionKeyword;
        this.redditUtil = new RedditUtil();
    }

    @Override
    public void run() {
        this.newPostAvailable = this.checkForNewPostEntries();
    }

    /**
     *
     * @return
     */
    public boolean checkForNewPostEntries(){
        List<PostEntry> oldPostEntries = this.latestPostEntries;
        MyDate oldPostTimestamp = this.latestPostTimestamp;

        //Pull
        this.getRecentMediaForKeyword();

        for(PostEntry pe : this.latestPostEntries){
            System.out.println("Entry:" + pe.getUrl());
        }

        //Check by null
        if(oldPostEntries == null && this.latestPostEntries != null ){
            return true;
        }

        //Check by timestamp
        if(oldPostTimestamp != null && this.latestPostTimestamp.compareTo(oldPostTimestamp) > 0){
            return true;
        }

        return false;
    }

    public List<byte[]> getRecentMediaForKeyword() {
        try {
            URL url = new URL(
                    RedditConstants.BASE +
                            RedditConstants.SUBREDDIT_PREFIX + this.subscriptionKeyword +//Hier hashtage
                            RedditConstants.AS_JSON + "?" +
                            RedditConstants.KEY_SORT +
                            RedditConstants.VAL_DATE);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(RedditConstants.GET);
            con.setRequestProperty("User-agent", RedditConstants.APP_NAME);
            con.setDoOutput(true);

            String responseString = "";

            if (!this.redditUtil.hasErrorCode(con.getResponseCode())) {
                responseString = new BufferedReader(new InputStreamReader(con.getInputStream())).lines().collect(Collectors.joining());
                logger.info("Response Code: " + con.getResponseCode() + ". No error.");
            } else {
                logger.info("Response Code: " + con.getResponseCode() + ". Has error.");
                return null;
            }

            logger.info(String.valueOf(con.getURL()));

            List<PostEntry> postEntries = this.redditUtil.getPosts(responseString);
            this.setLatestPostEntries(postEntries);
            this.setLatestPostTimestamp(this.redditUtil.getLatestTimestamp(postEntries));

            List<byte[]> byteList = new ArrayList<>();
            for(PostEntry pe : postEntries){
                byteList.add(BlobConverterImpl.downloadToByte(pe.getUrl()));
            }

            return byteList;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getSubscriptionKeyword() {
        return subscriptionKeyword;
    }

    public void setSubscriptionKeyword(String subscriptionKeyword) {
        this.subscriptionKeyword = subscriptionKeyword;
    }

    public List<PostEntry> getLatestPostEntries() {
        return latestPostEntries;
    }

    public void setLatestPostEntries(List<PostEntry> latestPostEntries) {
        this.latestPostEntries = latestPostEntries;
    }

    public MyDate getLatestPostTimestamp() {
        return latestPostTimestamp;
    }

    public void setLatestPostTimestamp(MyDate latestPostTimestamp) {
        this.latestPostTimestamp = latestPostTimestamp;
    }
}
