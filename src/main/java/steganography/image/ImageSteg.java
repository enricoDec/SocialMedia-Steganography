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
import steganography.image.encoders.GIFTableDecoder;
import steganography.exceptions.UnknownStegFormatException;
import steganography.image.encoders.PixelBit;
import steganography.image.encoders.PixelIndex;
import steganography.image.exceptions.ImageCapacityException;
import steganography.image.exceptions.ImageWritingException;
import steganography.image.exceptions.NoImageException;
import steganography.image.exceptions.UnsupportedImageTypeException;
import steganography.image.overlays.ShuffleOverlay;
import steganography.image.encoders.BuffImgEncoder;
import steganography.image.overlays.BufferedImageCoordinateOverlay;
import steganography.image.overlays.TableOverlay;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ImageSteg implements Steganography {

    public static final int DEFAULT_SEED = 1732341558;
    private static final int HEADER_SIGNATURE = 1349075561;
    private boolean useDefaultHeader = true;

    // @Override
    // public void useDefaultHeader(boolean useDefaultHeader) {
    //     // TODO: Might be problematic decoding, length has to be given from user
    //     // this.useDefaultHeader = useDefaultHeader;
    // }

    @Override
    public byte[] encode(byte[] carrier, byte[] payload)
            throws IOException, UnsupportedImageTypeException, NoImageException,
                    ImageWritingException, ImageCapacityException {

        return encode(carrier, payload, DEFAULT_SEED);
    }

    @Override
    public byte[] encode(byte[] carrier, byte[] payload, long seed)
            throws IOException, NoImageException, UnsupportedImageTypeException,
                    ImageWritingException, ImageCapacityException {

        BuffImgAndFormat buffImgAndFormat = carrier2BufferedImage(carrier);

        int type = buffImgAndFormat.getBufferedImage().getType();

        BuffImgEncoder encoder = getEncoder(buffImgAndFormat.getBufferedImage(), seed);

        if (this.useDefaultHeader) {
            encoder.encode(int2bytes(HEADER_SIGNATURE));
            encoder.encode(int2bytes(payload.length));
        }
        encoder.encode(payload);

        return bufferedImage2byteArray(encoder.getOverlay().getBufferedImage(), buffImgAndFormat.getFormat());
    }

    @Override
    public byte[] decode(byte[] steganographicData)
            throws IOException, UnsupportedImageTypeException, NoImageException, UnknownStegFormatException {

        return decode(steganographicData, DEFAULT_SEED);
    }

    @Override
    public byte[] decode(byte[] steganographicData, long seed)
            throws IOException, NoImageException, UnsupportedImageTypeException, UnknownStegFormatException {

        BuffImgAndFormat buffImgAndFormat = carrier2BufferedImage(steganographicData);

        BuffImgEncoder encoder = getEncoder(buffImgAndFormat.getBufferedImage(), seed);

        // TODO: only do this if useDefaultHeader == true, but length has to be given from user
        // decode 4 bytes and compare them to header signature
        if (bytesToInt(encoder.decode(4)) != HEADER_SIGNATURE) {
            throw new UnknownStegFormatException("No steganographic encoding found.");
        }

        // decode the next 4 bytes to get the amount of bytes to read
        int length = bytesToInt(encoder.decode(4));

        return encoder.decode(length);
    }

    @Override
    public boolean isSteganographicData(byte[] data)
            throws IOException, NoImageException, UnsupportedImageTypeException {

        return isSteganographicData(data, DEFAULT_SEED);
    }

    @Override
    public boolean isSteganographicData(byte[] data, long seed)
            throws IOException, NoImageException, UnsupportedImageTypeException {

        BuffImgAndFormat buffImgAndFormat = carrier2BufferedImage(data);

        BufferedImageCoordinateOverlay overlay = new ShuffleOverlay(buffImgAndFormat.getBufferedImage(), seed);
        BuffImgEncoder encoder = new PixelBit(overlay);

        return bytesToInt(encoder.decode(4)) == HEADER_SIGNATURE;
    }

    /**
     * Returns the maximum number of bytes that can be encoded in the given image.
     * @param image image to potentially encode bytes in
     * @param subtractDefaultHeader should the length of the default header be subtracted from the capacity?
     * @param withTransparent should transparent pixels account to the capacity?
     * @return the payload-capacity of image
     */
    public int getImageCapacity(byte[] image, boolean subtractDefaultHeader, boolean withTransparent)
            throws IOException, NoImageException {

        BufferedImage bufferedImage = carrier2BufferedImage(image).getBufferedImage();
        int capacity;
        if (bufferedImage.getType() == BufferedImage.TYPE_BYTE_INDEXED) {
            try {
                BuffImgEncoder encoder = getEncoder(bufferedImage,DEFAULT_SEED);
                capacity = encoder.getOverlay().available() / 8;

                return (subtractDefaultHeader) ? capacity - 8 : capacity;
            } catch (UnsupportedImageTypeException e) {
                e.printStackTrace();
                System.out.println("Cannot handle format");
            }
        }
        if (!withTransparent) {
            capacity = bufferedImage.getWidth() * bufferedImage.getHeight();
        } else {
            capacity = countIntransparent(bufferedImage);
        }
        capacity /= 8;

        return (subtractDefaultHeader && capacity >= 8) ? (capacity - 8) : capacity;
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

    /**
     * Determines and returns the suitable encoder (and overlay) for the given bufferedImage according to its type.
     * @param bufferedImage image to get the encoder for
     * @param seed to hand to the overlay
     * @return BuffImgEncoder with set BufferedImageCoordinateOverlay, chosen accordingly to the images type
     * @throws UnsupportedImageTypeException if the images type is not supported by any known encoder / overlay
     */
    private BuffImgEncoder getEncoder(BufferedImage bufferedImage, long seed)
            throws UnsupportedImageTypeException {

        int type = bufferedImage.getType();

        switch (type) {

            // Types for PixelBit Algorithm TODO: Add IgnoreAreaOverlay as optional by enum?
            //----------------------------------------------------------------------------------
            case BufferedImage.TYPE_4BYTE_ABGR:
            case BufferedImage.TYPE_3BYTE_BGR:
            case BufferedImage.TYPE_INT_ARGB:
            case BufferedImage.TYPE_INT_RGB:
            case BufferedImage.TYPE_INT_BGR:
                return new PixelBit(new ShuffleOverlay(bufferedImage, seed));

            // Type(s) for ColorCouple Algorithm
            //----------------------------------------------------------------------------------
            case BufferedImage.TYPE_BYTE_INDEXED:
                GIFTableDecoder tableDecoder = new GIFTableDecoder();
                try {
                    Map<Integer, List<Integer>> colorCouple = tableDecoder.getColorCouples(tableDecoder.saveColorTable(bufferedImage2byteArray(bufferedImage,"gif")));
                    return new PixelIndex(new TableOverlay(bufferedImage,seed,colorCouple),colorCouple,seed);
                } catch (IOException | ImageWritingException e) {
                    e.printStackTrace();
                }
                // return overlay8Bit

            // Types that have not been tested, but are probably suitable for PixelBit Algorithm
            //----------------------------------------------------------------------------------
            case BufferedImage.TYPE_4BYTE_ABGR_PRE:
            case BufferedImage.TYPE_INT_ARGB_PRE:
                // TODO: Test those types (find them first)
                throw new UnsupportedImageTypeException("Image type is not supported because untested.");

            // Types that will (probably) not be supported - explicit for completion reasons
            //----------------------------------------------------------------------------------
            case BufferedImage.TYPE_BYTE_BINARY:
            case BufferedImage.TYPE_BYTE_GRAY:
            case BufferedImage.TYPE_CUSTOM:
            case BufferedImage.TYPE_USHORT_555_RGB:
            case BufferedImage.TYPE_USHORT_565_RGB:
            case BufferedImage.TYPE_USHORT_GRAY:
            default:
                throw new UnsupportedImageTypeException("Image type is not supported");
        }
    }

    private BuffImgAndFormat carrier2BufferedImage(byte[] carrier)
            throws IOException, NoImageException {

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
                throw new NoImageException("No image could be read from input.");
            }
        }

        return buffImgAndFormat;
    }

    private byte[] bufferedImage2byteArray(BufferedImage image, String format)
            throws IOException, ImageWritingException {

        ByteArrayOutputStream resultImage = new ByteArrayOutputStream();

        if (!ImageIO.write(image, format, resultImage)) {
            throw new ImageWritingException("Could not write image. Unknown, internal error");
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

    private int bytesToInt(byte[] b) {
        return   b[3] & 0xFF |
                (b[2] & 0xFF) << 8 |
                (b[1] & 0xFF) << 16 |
                (b[0] & 0xFF) << 24;
    }

    private static class BuffImgAndFormat {

        private final BufferedImage bufferedImage;

        private final String format;

        public BuffImgAndFormat(BufferedImage bufferedImage, String format) {
            this.bufferedImage = bufferedImage;
            this.format = format;
        }

        public BufferedImage getBufferedImage() {
            return bufferedImage;
        }

        public String getFormat() {
            return format;
        }
    }
}
