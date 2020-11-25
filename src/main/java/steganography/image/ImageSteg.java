/*
 * Copyright (c) 2020
 * Contributed by Henk-Joas Lubig
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

package steganography.image;

import steganography.Steganography;
import steganography.util.BuffImgEncoder;
import steganography.util.BuffImgAndFormat;
import steganography.util.BufferedImageCoordinateOverlay;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;

public class ImageSteg implements Steganography {

    private static final int DEFAULT_SEED = 1732341558;
    private static final int HEADER_SIGNATURE = 1349075561;

    @Override
    public byte[] encode(byte[] carrier, byte[] payload) throws IOException {
        return encode(carrier, payload, DEFAULT_SEED);
    }

    @Override
    public byte[] encode(byte[] carrier, byte[] payload, long seed) throws IOException {
        BuffImgAndFormat buffImgAndFormat = carrier2BufferedImage(carrier);

        BufferedImageCoordinateOverlay overlay = new RemoveTransparentShuffleOverlay(buffImgAndFormat.getBufferedImage(), seed);
        BuffImgEncoder encoder = new PixelBit(overlay);
        encoder.encode(int2bytes(HEADER_SIGNATURE));
        encoder.encode(int2bytes(payload.length));
        encoder.encode(payload);

        return bufferedImage2byteArray(overlay.getBufferedImage(), buffImgAndFormat.getFormat());
    }

    @Override
    public byte[] decode(byte[] steganographicData) throws IOException {
        return decode(steganographicData, DEFAULT_SEED);
    }

    @Override
    public byte[] decode(byte[] steganographicData, long seed) throws IOException {
        BuffImgAndFormat buffImgAndFormat = carrier2BufferedImage(steganographicData);

        BufferedImageCoordinateOverlay overlay = new RemoveTransparentShuffleOverlay(buffImgAndFormat.getBufferedImage(), seed);
        BuffImgEncoder encoder = new PixelBit(overlay);

        // decode 4 bytes and compare them to header signature
        if (bytesToInt(encoder.decode(4)) != HEADER_SIGNATURE) {
            // TODO: Specialized Exception
            throw new UnsupportedEncodingException("No steganographic encoding found.");
        }

        // decode the next 4 bytes to get the amount of bytes to read
        int length = bytesToInt(encoder.decode(4));

        return encoder.decode(length);
    }

    @Override
    public boolean isSteganographicData(byte[] data) throws IOException {
        BuffImgAndFormat buffImgAndFormat = carrier2BufferedImage(data);

        BufferedImageCoordinateOverlay overlay = new ShuffleOverlay(buffImgAndFormat.getBufferedImage(), DEFAULT_SEED);
        BuffImgEncoder encoder = new PixelBit(overlay);

        return bytesToInt(encoder.decode(4)) == HEADER_SIGNATURE;
    }

    /**
     * Returns the maximum number of bytes that can be encoded in the given image.
     * @param image image to potentially encode bytes in
     * @param withTransparent should transparent pixels account to the capacity
     * @return the payload-capacity of image
     */
    public int getImageCapacity(byte[] image, boolean withTransparent) throws IOException {
        BufferedImage bufferedImage = carrier2BufferedImage(image).getBufferedImage();
        int capacity;
        if (!withTransparent) {
            capacity = bufferedImage.getWidth() * bufferedImage.getHeight();
        } else {
            capacity = countIntransparent(bufferedImage);
        }
        return capacity / 8;
    }

    private int countIntransparent(BufferedImage bufferedImage) {
        int count = 0;
        for(int y = 0; y < bufferedImage.getHeight(); y++) {
            for (int x = 0; x < bufferedImage.getWidth(); x++) {
                int pixel = bufferedImage.getRGB(x, y);
                if(((pixel >> 24) & 0xff) != 0)
                    count++;
            }
        }
        return count;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////
    //                                       UTIL
    ////////////////////////////////////////////////////////////////////////////////////////////

    private BuffImgAndFormat carrier2BufferedImage(byte[] carrier) throws IOException {
        BuffImgAndFormat buffImgAndFormat;

        try(ImageInputStream imageInputStream = new MemoryCacheImageInputStream(new ByteArrayInputStream(carrier))) {
            Iterator<ImageReader> readers = ImageIO.getImageReaders(imageInputStream);

            if (readers.hasNext()) {
                ImageReader reader = readers.next();

                try {
                    reader.setInput(imageInputStream);

                    buffImgAndFormat = new BuffImgAndFormat(reader.read(0), reader.getFormatName());

                } finally {
                    reader.dispose();
                }
            } else {
                // TODO: Specialized Exception
                throw new UnsupportedEncodingException("No image could be read from input.");
            }
        }

        return buffImgAndFormat;
    }

    private byte[] bufferedImage2byteArray(BufferedImage image, String format) throws IOException {
        ByteArrayOutputStream resultImage = new ByteArrayOutputStream();

        if (!ImageIO.write(image, format, resultImage)) {
            // TODO: Specialized Exception
            throw new UnsupportedEncodingException("Could not write image. Unknown, internal failure");
        }

        return resultImage.toByteArray();
    }

    private byte[] int2bytes(int integer) {
        return new byte[] {
                (byte) ((integer >> 24) & 0xFF),
                (byte) ((integer >> 16) & 0xFF),
                (byte) ((integer >> 8) & 0xFF),
                (byte) (integer & 0xFF)
        };
    }

    private int bytesToInt(byte[] b)
    {
        return   b[3] & 0xFF |
                (b[2] & 0xFF) << 8 |
                (b[1] & 0xFF) << 16 |
                (b[0] & 0xFF) << 24;
    }
}
