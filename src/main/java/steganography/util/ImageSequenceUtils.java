/*
 * Copyright (c) 2020
 * Contributed by Enrico de Chadarevian
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

package steganography.util;

import steganography.image.ImageSteg;
import steganography.image.NoImageException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageSequenceUtils {

    /**
     * Takes a payload and splits it in parts that fit in each image
     * List entry is null if no payload split is present for the image
     *
     * @param imageList list of image to be used to split payload
     * @param payload   payload to be split
     * @return list of payload splits (index is equal to order of image list).
     * Entry is Null if no payload for image
     * @throws IOException if IO Exception is thrown during read operations
     */
    public static List<byte[]> sequenceDistribution(List<byte[]> imageList, byte[] payload) throws IOException, NoImageException {
        int chunkCursor = 0;
        List<byte[]> payloadSplitted = new ArrayList<>();
        ImageSteg steganography = new ImageSteg();
        for (byte[] image : imageList) {
            //If entire payload copied, just add null
            if (chunkCursor >= payload.length) {
                payloadSplitted.add(null);
            } else {
                //Distribute payload into frames
                int maxImagePayload = steganography.getImageCapacity(image, true, false);
                //New copy of payload array that holds max amount of payload the current image can hold
                byte[] payloadChunk;
                //If payload left to be encoded is bigger than what the current image can hold, encode as much as possible
                if (payload.length - chunkCursor > maxImagePayload) {
                    payloadChunk = new byte[maxImagePayload];
                } else {
                    // else encode only payload length
                    payloadChunk = new byte[payload.length - chunkCursor];
                }
                System.arraycopy(payload, chunkCursor, payloadChunk, 0, payloadChunk.length);
                payloadSplitted.add(payloadChunk);
                chunkCursor += payloadChunk.length;
            }
        }
        return payloadSplitted;
    }

    public static List<byte[]> shuffleDistribution(){
        return null;
    }
}
