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

import apis.SubscriptionDeamon;
import apis.models.APINames;
import apis.reddit.RedditConstants;
import apis.models.MyDate;
import apis.models.PostEntry;
import apis.utils.BaseUtil;
import persistence.JSONPersistentManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static apis.models.APINames.IMGUR;

public class ImgurSubscriptionDeamon implements SubscriptionDeamon {

    private static final Logger logger = Logger.getLogger(ImgurSubscriptionDeamon.class.getName());
    private ImgurUtil imgurUtil;

    private final String BASE_URI = "https://api.imgur.com/3/";
    private final String SEARCH_URI = "gallery/search/time?q=";

    private boolean newPostAvailable;
    private List<PostEntry> latestPostEntries;

    public ImgurSubscriptionDeamon() {
        this.imgurUtil = new ImgurUtil();
    }

    @Override
    public void run() {
        //bool newPostAvailable will be setted in getRecentMediaForSubscribedKeywords()
        this.latestPostEntries = this.getRecentMediaForSubscribedKeywords(null);
    }

    private List<PostEntry> getRecentMedia(String onceUsedKeyword) {
        List<String> keywords = imgurUtil.getKeywordList(IMGUR, onceUsedKeyword);

        if (keywords == null || keywords.size() == 0) {
            logger.info("No keyword(s) were set.");
            return null;
        }

        List<PostEntry> resultList = new ArrayList<>();

        for (String keyword : keywords) {
            logger.info("Check for new post entries for keyword '" + keyword + "' ...");

            try {
                URL url = new URL(
                        BASE_URI + SEARCH_URI + keyword);

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod(RedditConstants.GET);
                con.setRequestProperty("User-agent", ImgurConstants.APP_NAME);
                con.setRequestProperty("Authorization", "Client-ID " + ImgurConstants.CLIENT_ID);
                con.setDoOutput(true);

                String responseString = "";

                if (!BaseUtil.hasErrorCode(con.getResponseCode())) {
                    responseString = new BufferedReader(new InputStreamReader(con.getInputStream())).lines().collect(Collectors.joining());
                    logger.info("Response Code: " + con.getResponseCode() + ". No error.");
                } else {
                    logger.info("Response Code: " + con.getResponseCode() + ". Has error.");
                    return null;
                }

                logger.info(String.valueOf(con.getURL()));
                resultList.addAll(this.imgurUtil.getPosts(responseString));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        logger.info((resultList.size()) + " postentries found.");
        return resultList;
    }

    /**
     * TODO in reddit übernehmen
     */

    /**
     * Searches for new post entries for a specific keyword, or for all stored keywords.
     *
     * @param keyword If onceUsedKeyword is null, every stored keyword will be processed.
     * @return
     */
    @Override
    public List<PostEntry> getRecentMediaForSubscribedKeywords(String keyword) {
        List<PostEntry> tmp = this.getRecentMedia(keyword);

        if (tmp != null) {
            Collections.sort(tmp);
            tmp = BaseUtil.elimateOldPostEntries(imgurUtil.getLatestStoredTimestamp(IMGUR), tmp);
            if (tmp.size() > 0) {
                newPostAvailable = true;

                /**
                 * TODO 0 oder letztes element.
                 */

                imgurUtil.setLatestPostTimestamp(IMGUR, tmp.get(0).getDate());
                latestPostEntries = tmp;
                logger.info("New media found.");
                return tmp;
            }
        }

        logger.info("No new media found.");
        latestPostEntries = null;
        newPostAvailable = false;
        return null;
    }


    @Override
    public boolean isNewPostAvailable() {
        return this.newPostAvailable;
    }

    public List<PostEntry> getLatestPostEntries() {
        return this.latestPostEntries;
    }
}
