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

package apis.reddit;

import apis.SubscriptionDeamon;
import apis.utils.BlobConverterImpl;
import apis.models.MyDate;
import apis.models.PostEntry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;
import java.util.stream.Collectors;

public class RedditSubscriptionDeamon implements SubscriptionDeamon {

    private static final Logger logger = Logger.getLogger(RedditSubscriptionDeamon.class.getName());
    private RedditUtil redditUtil;

    private String subscriptionKeyword;
    private List<PostEntry> latestPostEntries;
    private MyDate latestPostTimestamp;
    private boolean newPostAvailable;

    /**
     * Subcription for a Keyword in a Social Media
     *
     * @param subscriptionKeyword The keyword
     */
    public RedditSubscriptionDeamon(String subscriptionKeyword) {
        SimpleFormatter fmt = new SimpleFormatter();
        StreamHandler sh = new StreamHandler(System.out, fmt);
        logger.addHandler(sh);
        this.subscriptionKeyword = subscriptionKeyword;
        this.redditUtil = new RedditUtil();
    }

    @Override
    public void run() {
        this.newPostAvailable = this.checkForNewPostEntries();
    }

    /**
     * @return
     */
    @Override
    public boolean checkForNewPostEntries() {
        logger.info("Check for new post entries for keyword '" + this.subscriptionKeyword + "' ...");
        List<PostEntry> oldPostEntries = this.latestPostEntries;
        MyDate oldPostTimestamp = this.latestPostTimestamp;

        //Pull
        if(this.getRecentMedia() != null) {
            //Check by null
            if (oldPostEntries == null && this.latestPostEntries != null) {
                return true;
            }

            //Check by timestamp
            if (oldPostTimestamp != null && this.latestPostTimestamp.compareTo(oldPostTimestamp) > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Searches for the given keyword. The old keyword which was decleared for the subscription will stay for the search.
     * @param keyword
     * @return
     */
    @Override
    public List<byte[]> getRecentMediaForKeyword(String keyword) {
        String temp = this.subscriptionKeyword;
        this.subscriptionKeyword = keyword;
        List<byte[]> recentMedia = this.getRecentMedia();
        this.subscriptionKeyword = temp;
        return recentMedia;
    }

    @Override
    public List<byte[]> getRecentMedia() {
        try {
            URL url = new URL(
                    RedditConstants.BASE +
                            RedditConstants.SUBREDDIT_PREFIX + this.subscriptionKeyword +
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

            System.out.println(responseString);
            List<PostEntry> postEntries = this.redditUtil.getPosts(responseString);
            this.setLatestPostEntries(postEntries);
            this.setLatestPostTimestamp(this.redditUtil.getLatestTimestamp(postEntries));

            List<byte[]> byteList = new ArrayList<>();
            for (PostEntry pe : postEntries) {
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

    @Override
    public String getSubscriptionKeyword() {
        return subscriptionKeyword;
    }

    @Override
    public void setSubscriptionKeyword(String subscriptionKeyword) {
        this.subscriptionKeyword = subscriptionKeyword;
    }

    @Override
    public List<PostEntry> getLatestPostEntries() {
        return latestPostEntries;
    }

    @Override
    public void setLatestPostEntries(List<PostEntry> latestPostEntries) {
        this.latestPostEntries = latestPostEntries;
    }

    @Override
    public MyDate getLatestPostTimestamp() {
        return latestPostTimestamp;
    }

    @Override
    public void setLatestPostTimestamp(MyDate latestPostTimestamp) {
        this.latestPostTimestamp = latestPostTimestamp;
    }

    @Override
    public boolean isNewPostAvailable() {
        return newPostAvailable;
    }

    @Override
    public void setNewPostAvailable(boolean newPostAvailable) {
        this.newPostAvailable = newPostAvailable;
    }
}
