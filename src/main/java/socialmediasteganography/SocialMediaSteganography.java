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
import steganography.exceptions.*;

import java.io.IOException;
import java.util.List;

public interface SocialMediaSteganography {

    /**
     * Encodes payload in carrier and posts the result to socialMedia
     * @param apiNames API Name
     * @param carrier data used to encode the payload in
     * @param payload payload to encode
     * @param mediaType The type of the carrier (e.g. PNG.GIF)
     * @param keyword The name under which the carrier is posted to social Media
     * @return boolean true, when carrier is successfully posted
     * @throws UnsupportedMediaTypeException if the MediaType is not supported
     * @throws MediaNotFoundException if f the intended media (e.g. Image, Video, ...) could not be read from data
     * @throws MediaCapacityException if the payload doesn't fit in the carrier
     * @throws IOException if there is a problem with reading Data from carrier or payload
     * @throws MediaReassemblingException if a problem occurred during writing of the result media
     */
    boolean encodeAndPost(APINames apiNames, String keyword, byte[] carrier, byte[] payload, MediaType mediaType) throws UnsupportedMediaTypeException, MediaNotFoundException, MediaCapacityException, MediaReassemblingException, IOException;


    /** Loads carrier from file path and uses encodeAndPost
     * @param path Path to a Media file
     * @param apiNames API Name
     * @param payload payload to encode
     * @param mediaType The type of the carrier (e.g. PNG.GIF)
     * @param keyword The name under which the carrier is posted to social Media
     * @return boolean true, when carrier is successfully posted
     * @throws UnsupportedMediaTypeException if the MediaType is not supported
     * @throws MediaNotFoundException if f the intended media (e.g. Image, Video, ...) could not be read from data
     * @throws MediaCapacityException if the payload doesn't fit in the carrier
     * @throws IOException if there is a problem with reading Data from carrier or payload
     * @throws MediaReassemblingException if a problem occurred during writing of the result media
     * @see socialmediasteganography.SocialMediaSteganography#encodeAndPost(APINames, String, byte[], byte[], MediaType)
     */
    boolean encodeAndPost(APINames apiNames, String keyword, String path, byte[] payload, MediaType mediaType) throws IOException, UnsupportedMediaTypeException, MediaNotFoundException, MediaReassemblingException, MediaCapacityException;

    /** Loads carrier from file path and uses encodeAndPost
     * @param apiNames API Name
     * @param payload payload to encode
     * @param carrier data used to encode the payload in
     * @param mediaType The type of the carrier (e.g. PNG.GIF)
     * @param keyword The name under which the carrier is posted to social Media
     * @param token Token class with tokens for Social Media
     * @return boolean true, when carrier is successfully posted
     * @throws UnsupportedMediaTypeException if the MediaType is not supported
     * @throws MediaNotFoundException if f the intended media (e.g. Image, Video, ...) could not be read from data
     * @throws MediaCapacityException if the payload doesn't fit in the carrier
     * @throws IOException if there is a problem with reading Data from carrier or payload
     * @throws MediaReassemblingException if a problem occurred during writing of the result media
     * @see socialmediasteganography.SocialMediaSteganography#encodeAndPost(APINames, String, byte[], byte[], MediaType)
     */
    boolean encodeAndPost(APINames apiNames, String keyword, byte[] carrier, byte[] payload, MediaType mediaType,Token token) throws IOException, UnsupportedMediaTypeException, MediaNotFoundException, MediaReassemblingException, MediaCapacityException;
    /**
     * Save encoded Media to given path as a file
     * @param carrier data used to encode the payload in
     * @param payload payload to encode
     * @param mediaType The type of the carrier (e.g. PNG.GIF)
     * @param savepath The path to which the generated file is saved
     * @throws UnsupportedMediaTypeException if the MediaType is not supported
     * @throws MediaNotFoundException if f the intended media (e.g. Image, Video, ...) could not be read from data
     * @throws MediaCapacityException if the payload doesn't fit in the carrier
     * @throws IOException if there is a problem with reading Data from carrier or payload
     * @throws MediaReassemblingException if a problem occurred during writing of the result media
     */
    void saveEncodedPicture(byte[] carrier, byte[] payload, MediaType mediaType, String savepath) throws UnsupportedMediaTypeException, MediaNotFoundException, MediaCapacityException, MediaReassemblingException, IOException;

    /** Load Media from a file and save encoded Media to given path as a file
     * @see socialmediasteganography.SocialMediaSteganography#saveEncodedPicture(byte[], byte[], MediaType, String)
     * @param payload payload to encode
     * @param mediaType The type of the carrier (e.g. PNG.GIF)
     * @param savepath The path to which the generated file is saved
     * @throws UnsupportedMediaTypeException if the MediaType is not supported
     * @throws MediaNotFoundException if f the intended media (e.g. Image, Video, ...) could not be read from data
     * @throws MediaCapacityException if the payload doesn't fit in the carrier
     * @throws IOException if there is a problem with reading Data from carrier or payload
     * @throws MediaReassemblingException if a problem occurred during writing of the result media
     *
     * @param filepath the path to the file thath needs to be encoded
     */
    void saveEncodePicture(String filepath, MediaType mediaType, byte[] payload, String savepath) throws IOException, UnsupportedMediaTypeException, MediaNotFoundException, MediaReassemblingException, MediaCapacityException;

    /**
     * Post media to Social Media, if no Token is given set Parameter beforehand
     * e.g. Tumbler.setApiKey = "..." and Tumbler.setApiSecretKey
     * @param carrier data that should be uploaded
     * @param apiNames API Name
     * @param keyword The name under which the carrier is posted to social Media
     * @param mediaType The type of the carrier (e.g. PNG.GIF)
     * @return boolean true when carrier was successfully posted
     */
    boolean postToSocialMedia(byte[] carrier, APINames apiNames, String keyword, MediaType mediaType);
    /**
     * Post media to Social Media
     * @param carrier data that should be uploaded
     * @param apiNames API Name
     * @param keyword The name under which the carrier is posted to social Media
     * @param mediaType The type of the carrier (e.g. PNG.GIF)
     * @param token The token used to access Social Media
     * @return boolean true when carrier was successfully posted
     */
    boolean postToSocialMedia(byte[] carrier, APINames apiNames, String keyword, MediaType mediaType, Token token);

    /**
     * Encodes payload into carrier for a given mediatype
     * @param carrier data used to encode the payload in
     * @param payload payload to encode
     * @param mediaType The type of the carrier (e.g. PNG.GIF)
     * @return byte[] carrier with encoded payload
     * @throws UnsupportedMediaTypeException if the MediaType is not supported
     * @throws MediaNotFoundException if f the intended media (e.g. Image, Video, ...) could not be read from data
     * @throws MediaCapacityException if the payload doesn't fit in the carrier
     * @throws IOException if there is a problem with reading Data from carrier or payload
     * @throws MediaReassemblingException if a problem occurred during writing of the result media
     */
    byte[] encodeCarrier(byte[] carrier, byte[] payload, MediaType mediaType) throws UnsupportedMediaTypeException, MediaCapacityException, MediaNotFoundException, MediaReassemblingException, IOException;

    /**
     * Subscribes a keyword to a social media
     * @param keyword The name under which the carrier is posted to social Media
     * @param apiNames API Name
     * @return Social Media The Social Media Class that can be used for interacting with given API
     */
    SocialMedia subscribeToSocialMedia(String keyword,APINames apiNames);

    /**
     * Gets Media for a certain Keyword and checks if it is a steganographic picture.
     * If it is, proceeds to decode the picture and return payload
     * @param keyword keyword under which the media was posted
     * @param apiNames name of the api used
     * @param mediaType media type in which the payload is decoded
     * @return payloads all payloads that could be read
     */
    byte[][] getMediaAndDecode(String keyword,APINames apiNames,MediaType mediaType);
    /**
     * Decodes hidden payload from carrier
     * @param mediaType The type of the carrier (e.g. PNG.GIF)
     * @param carrier data with encoded payload
     * @return payload decoded payload
     * @throws UnsupportedMediaTypeException if the MediaType is not supported
     * @throws IOException if a problem occurs during reading of steganographicData
     * @throws MediaNotFoundException if the intended media (e.g. Image, Video, ...) could not be read from steganographicData
     */
    byte[] decodeCarrier(MediaType mediaType, byte[] carrier) throws UnsupportedMediaTypeException, UnknownStegFormatException, MediaNotFoundException, IOException;

    /**
     * Set the seed for decoding or encoding
     * @param seed The seed to initialize random function
     */
    void setSeed(Long seed);
}
