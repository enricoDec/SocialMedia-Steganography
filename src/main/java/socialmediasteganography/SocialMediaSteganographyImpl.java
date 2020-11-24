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
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class SocialMediaSteganographyImpl implements SocialMediaSteganography{

    private static final Logger logger = Logger.getLogger(SocialMediaSteganographyImpl.class.getName());

    private Steganography steganography;

    public SocialMediaSteganographyImpl(Steganography steganography) {
        this.steganography = steganography;
    }

    @Override
    public boolean encodeAndPost(SocialMedia socialMedia, byte[] carrier, byte[] payload) {
        try{
            byte[] bytes = steganography.encode(carrier, payload);

            /* //Zum pruefen, ob man das Bild nach dem enkodieren noch (manuell) öffnen kann.
                InputStream is = new ByteArrayInputStream(bytes);
                BufferedImage bImg = ImageIO.read(is);
                ImageIO.write(bImg, "png", new File("myfile.png"));
            */

            return socialMedia.postToSocialNetwork(bytes, "test");
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public List<byte[]> searchForHiddenMessages(SocialMedia socialMedia, String keyword) {
        List<byte[]> resultsInBytes = socialMedia.getRecentMediaForKeyword(keyword);
        List<byte[]> decodedResultsInBytes = new ArrayList<>();

        try {
            for(byte[] b : resultsInBytes){
                byte[] bDecoded = steganography.decode(b);
                if(bDecoded.length > 0)
                    decodedResultsInBytes.add(bDecoded);
                logger.info("Decoded successfully.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("Decoding failed.");
        }
        return decodedResultsInBytes;
    }
}
