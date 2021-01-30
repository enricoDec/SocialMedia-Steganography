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
import steganography.exceptions.*;

import java.io.IOException;
import java.util.List;

public interface SocialMediaSteganography {

    /**
     * Encodes payload in carrier and posts the result to socialMedia
     * @param apiNames API Names
     * @param carrier data used to encode payload in
     * @param payload payload to encode
     * @return
     */
    boolean encodeAndPost(APINames apiNames, String keyword, byte[] carrier, byte[] payload, MediaType mediaType) throws UnsupportedMediaTypeException, MediaNotFoundException, MediaCapacityException, MediaReassemblingException, IOException;


    /**
     * @param apiNames
     * @param keyword
     * @param path Path to file
     * @param payload
     * @param mediaType
     * @return
     */
    boolean encodeAndPost(APINames apiNames, String keyword, String path, byte[] payload, MediaType mediaType) throws IOException, UnsupportedMediaTypeException, MediaNotFoundException, MediaReassemblingException, MediaCapacityException;

    /**
     * Save encoded Media to given path as a file
     * @param carrier
     * @param payload
     * @param mediaType
     * @param savepath
     */
    void saveEncodedPicture(byte[] carrier, byte[] payload, MediaType mediaType, String savepath) throws UnsupportedMediaTypeException, MediaNotFoundException, MediaCapacityException, MediaReassemblingException, IOException;

    /**
     * Save encoded Media to given path as a file
     * @param filepath
     * @param mediaType
     * @param payload
     * @param savepath
     */
    void saveEncodePicture(String filepath, MediaType mediaType, byte[] payload, String savepath) throws IOException, UnsupportedMediaTypeException, MediaNotFoundException, MediaReassemblingException, MediaCapacityException;

    /**
     * Post media to Social Media
     * @param apiNames
     * @param keyword
     * @param mediaType
     */
    boolean postToSocialMedia(byte[] carrier, APINames apiNames, String keyword, MediaType mediaType);

    /**
     * Encodes payload into carrier for a given mediatype
     * @param carrier
     * @param payload
     * @param mediaType
     * @return
     */
    byte[] encodeCarrier(byte[] carrier, byte[] payload, MediaType mediaType) throws UnsupportedMediaTypeException, MediaCapacityException, MediaNotFoundException, MediaReassemblingException, IOException;

    /**
     * Subscribes a keyword to a social media
     * @param keyword
     * @param apiNames
     * @return
     */
    SocialMedia subscribeToSocialMedia(String keyword,APINames apiNames);

    /**
     * Decodes hidden payload from carrier
     * @param mediaType
     * @param carrier
     * @return
     */
    byte[] decodeCarrier(MediaType mediaType, byte[] carrier) throws UnsupportedMediaTypeException, UnknownStegFormatException, MediaNotFoundException, IOException;

    void setSeed(Long seed);
}
