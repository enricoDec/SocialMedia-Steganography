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
import apis.reddit.Reddit;
import steganography.Steganography;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Mario Teklic
 */


public class SocialMediaSteganographyImpl implements SocialMediaSteganography {

    private static final Logger logger = Logger.getLogger(SocialMediaSteganographyImpl.class.getName());

    private Steganography steganography;

    public SocialMediaSteganographyImpl(Steganography steganography) {
        this.steganography = steganography;
    }

    @Override
    public boolean encodeAndPost(SocialMedia socialMedia, byte[] carrier, byte[] payload) {
        try {
            byte[] bytes = steganography.encode(carrier, payload);

            //Zum pruefen, ob man das Bild nach dem enkodieren noch (manuell) öffnen kann.
            InputStream is = new ByteArrayInputStream(bytes);
            BufferedImage bImg = ImageIO.read(is);
            ImageIO.write(bImg, "png", new File("myfileEncoded.png"));


            return socialMedia.postToSocialNetwork(bytes, "test");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<byte[]> searchForHiddenMessages(SocialMedia socialMedia, String keyword) {
        List<byte[]> recentMedias = socialMedia.getRecentMediaForKeyword(keyword);
        List<byte[]> decodedMedias = new ArrayList<>();

        if(recentMedias == null){
            logger.info("No media with class " + socialMedia.getClass() + " for keyword '" + keyword + "' was found.") ;
            return null;
        }

        for(int i = 0; i < recentMedias.size(); i++){
            try {
                byte[] bDecoded = steganography.decode(recentMedias.get(i));
                if (bDecoded.length > 0) {
                    decodedMedias.add(bDecoded);
                    logger.info("Decoded successfully.");
                    System.out.println("Hidden message was: " + new String(bDecoded));
                }
            } catch (IOException e) {
                logger.info("Decoding failed for entry on index " + i + " with an " + e.getClass().getSimpleName() + ".");
            }
        }
        return decodedMedias;
    }
}
