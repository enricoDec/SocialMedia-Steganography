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


import apis.imgur.Imgur;
import apis.reddit.Reddit;
import apis.utils.BlobConverterImpl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class App {
    public static void main(String[] args) throws IOException, InterruptedException {

        /*
        //Upload on Imgur

         SocialMedia imgur = new Imgur();
         imgur.setToken(new Token("0d5ce353c61cbb597df3497669e7c4e85f072e2a", 123));
         byte[] byts = BlobConverterImpl.downloadToByte("https://www.webdesign-aus-braunschweig.de/images/bilder-bearbeiten/png_877x877px_quality-2.png");
         imgur.postToSocialNetwork(byts, "testword");

        */
        

        //Upload on Reddit
        /*
        SocialMedia reddit = new Reddit();
        List<byte[]> resultList = reddit.getRecentMediaForKeyword("nature");
        byte[] tmpImage = resultList.get(resultList.size()-1);
        reddit.setToken(new Token("668533834712-LUw9kEH1saPGx5UwQBdZuE-hfrJKTg", 123124));
        reddit.postToSocialNetwork(tmpImage, "test");
        */


        /**
         //Subscribe Imgur & Reddit

        SocialMedia imgur = new Imgur();
        imgur.changeSubscriptionInterval(TimeUnit.MINUTES, 5);
        imgur.subscribeToKeyword("test");

        SocialMedia reddit = new Reddit();
        reddit.subscribeToKeyword("nature");
        */
    }
}
