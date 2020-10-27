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
import apis.imgur.ImgurUtil;
import apis.reddit.Reddit;
import apis.utils.BlobConverterImpl;

import java.io.IOException;
import java.util.List;

public class App {
    public static void main(String[] args) throws IOException {

        SocialMedia imgur = new Imgur();
        //imgur.subscribeToKeyword("nature");

        byte[] byts = BlobConverterImpl.downloadToByte("https://i.imgur.com/PRBdEij.jpeg");
        System.out.println(byts.length);
        boolean result = imgur.postToSocialNetwork(byts, "yodababy");
        /* SocialMedia reddit = new Reddit();

        reddit.subscribeToKeyword("nature");
        List<byte[]> byts = reddit.getRecentMediaForKeyword("nature");

        System.out.println((byts.size()+1) + " treffer.");
        //reddit.setToken(new RedditToken("1L-t-mee2bsrW7zkS4IHC_FzeYU"));

        ImgurUtil imgurUtil = new ImgurUtil();
        imgurUtil.uploadPicture(byts.get(byts.size()-1), "nature");
*/



      /*  byte[] pic = reddit.getRecentMediaForHashtag("java");
        for(byte b : pic){
            System.out.println(b);
        }*/
        //System.out.println(reddit.postToSocialNetwork(pic, "meinHashtag"));
        //System.out.println(((Reddit) reddit).getModhash());
        //reddit.signInSocialNetwork("sharksystember","Mktcs1995");
        //((Reddit) reddit).tokenize();
    }
}
