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

package socialmediasteganography;


import apis.SocialMedia;
import apis.models.Token;
import apis.imgur.Imgur;
import apis.reddit.Reddit;
import apis.utils.BlobConverterImpl;
import persistence.JSONPersistentManager;
import persistence.PersistenceDummy;
import steganography.image.ImageSteg;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mario Teklic
 */


public class App {
    public static void main(String[] args) throws IOException {
        JSONPersistentManager.getInstance().setJsonPersistentHelper(new PersistenceDummy());

        //Setup
        SocialMedia socialMedia = new Imgur();
        socialMedia.setToken(new Token("e1e5f0ff327cb45aa5440f456297317cbcb77859", 100));
        SocialMediaSteganography sms = new SocialMediaSteganographyImpl(new ImageSteg());

        //Carrier, Payload
        byte[] byts = BlobConverterImpl.downloadToByte("https://compress-or-die.com/public/understanding-png/assets/lena-dirty-transparency-corrected-cv.png");
        String payload = "This is a secret message which was built by ProjectStudiumSteganography";

        //Encode and Post
        //sms.encodeAndPost(socialMedia, byts, payload.getBytes());

        //Search in social media for pictures and try to decode
        //List<byte[]> results = sms.searchForHiddenMessages(socialMedia, "test");
        socialMedia.subscribeToKeyword("nature");

/*
        //Auswertung als Strings
        List<String> messages = new ArrayList<>();
        for (byte[] b : results) {
            if (b.length > 0) {
                String msg = new String(b);
                messages.add(msg);
                System.out.println(msg);
            } else {
                System.out.println("No bytes for message type found");
            }
        }
*/

        /*
         //Zum testen, ob dieses Bild korrekt runtergeladen wurde.

        InputStream is = new ByteArrayInputStream(byts);
        BufferedImage bImg = ImageIO.read(is);
        ImageIO.write(bImg, "png", new File("myfile.png"));
        */

    /*
        //Upload on Imgur

         SocialMedia imgur = new Imgur();
         imgur.setToken(new Token("0d5ce353c61cbb597df3497669e7c4e85f072e2a", 123));
         byte[] byts = BlobConverterImpl.downloadToByte("https://i.imgur.com/SJZyZQ1.png");
         imgur.postToSocialNetwork(byts, "testword");
*/

        
/*
        //Upload on Reddit

        SocialMedia reddit = new Reddit();
        List<byte[]> resultList = reddit.getRecentMediaForKeyword("nature");
        byte[] tmpImage = resultList.get(resultList.size()-1);
        reddit.setToken(new Token("668533834712-q_itL79dEvxBQgWCRsktUkpTBScVQw", 123124));
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
