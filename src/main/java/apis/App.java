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

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class App {
    public static void main(String[] args) throws IOException, InterruptedException {



        /*
        //Upload on Imgur

         SocialMedia imgur = new Imgur();
         imgur.setToken(new Token("d1ee40fb83f13086ece3a6dd942f72e7ac42c16c", 123));
         byte[] byts = BlobConverterImpl.downloadToByte("https://i.imgur.com/PRBdEij.jpeg");
         imgur.postToSocialNetwork(byts, "testword");

         */


        /*
        //Upload on Reddit

        SocialMedia reddit = new Reddit();
        List<byte[]> resultList = reddit.getRecentMediaForKeyword("nature");
        byte[] tmpImage = resultList.get(resultList.size()-1);
        reddit.setToken(new Token("668533834712-X4pJXG9FiecZVk7xcmY1D3CrSE7axQ", 123124));
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
