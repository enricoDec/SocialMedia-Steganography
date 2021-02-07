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


import apis.MediaType;
import apis.SocialMedia;
import apis.models.APINames;
import apis.models.Token;
import apis.tumblr.Tumblr;
import apis.tumblr.TumblrConstants;
import persistence.JSONPersistentManager;
import persistence.PersistenceDummy;
import steganography.exceptions.MediaCapacityException;
import steganography.exceptions.MediaNotFoundException;
import steganography.exceptions.MediaReassemblingException;
import steganography.exceptions.UnsupportedMediaTypeException;
import steganography.image.ImageSteg;
import steganography.util.ByteArrayUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


public class App {
    public static void main(String[] args) throws IOException {

        File file = new File("src/test/resources/steganography/image/baum.png");
        byte[] carrier = ByteArrayUtils.read(file);


        String payload = "hallotest";
        SocialMediaSteganography sms = new SocialMediaSteganographyImpl();
        Tumblr.setApiKey("OfpsSPZAf9mClIvgVAKY3Hhg63Y09riZ9AMmbbI0hQVMdS4uYR");
        Tumblr.setApiSecret("H2yGuhhwd7g6eXIYE0OHpkL7fEd9laDWPHArjipezGyq9dFheF");


        //token in constants only for testing purpose
        Token token = new Token(TumblrConstants.accessToken, TumblrConstants.accessTokenSecret);


        //encode and post png
        try {
            sms.encodeAndPost(APINames.TUMBLR, "katze",
                    carrier,
                    payload.getBytes(StandardCharsets.UTF_8), MediaType.PNG, token);
        } catch (UnsupportedMediaTypeException e) {
            e.printStackTrace();
        } catch (MediaNotFoundException e) {
            e.printStackTrace();
        } catch (MediaReassemblingException e) {
            e.printStackTrace();
        } catch (MediaCapacityException e) {
            e.printStackTrace();
        }


        //encode and post mp3
        /*try {
            sms.encodeAndPost(APINames.TUMBLR, "katze",
                    "/home/marfen/Documents/Studium/Projekt_Steganographie/ProjektStudiumSteganography/src/main/java/apis/tumblr/medias/mp3ToDecode.mp3",
                    payload.getBytes(StandardCharsets.UTF_8), MediaType.MP3);
        } catch (UnsupportedMediaTypeException e) {
            e.printStackTrace();
        } catch (MediaNotFoundException e) {
            e.printStackTrace();
        } catch (MediaReassemblingException e) {
            e.printStackTrace();
        } catch (MediaCapacityException e) {
            e.printStackTrace();
        }*/

    }
}
