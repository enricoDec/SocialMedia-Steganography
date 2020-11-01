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
import java.util.List;

public class App {
    public static void main(String[] args) throws IOException {

        //Upload on Imgur

        /**

         SocialMedia imgur = new Imgur();
         imgur.setToken(new Token("56854d0e9b12d3f894145fb7abe5819e29de05a6", 123));
         //imgur.subscribeToKeyword("nature");

         byte[] byts = BlobConverterImpl.downloadToByte("https://i.imgur.com/PRBdEij.jpeg");
         imgur.postToSocialNetwork(byts, "testword");

         */

        //Upload on Reddit

        /**


         */

        SocialMedia reddit = new Reddit();
        reddit.setToken(new Token("668533834712-8n-oexRjkmlqqmToL_7kn-F3k-bLZw", 12123));
        byte[] byts = BlobConverterImpl.downloadToByte("https://i.imgur.com/PRBdEij.jpeg");

        reddit.postToSocialNetwork(byts, "test");


    }
}
