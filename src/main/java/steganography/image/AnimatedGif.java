/*
 * Copyright (c) 2020
 * Contributed by Selina Wernike
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
/**
 * @author Selina Wernike
 * This Klass will Split an animated gif into individual frames and vice versa
 * https://stackoverflow.com/questions/8933893/convert-each-animated-gif-frame-to-a-separate-bufferedimage
 */
package steganography.image;

import steganography.Steganography;
import steganography.exceptions.*;
import steganography.image.exceptions.NoImageException;
import steganography.image.exceptions.UnsupportedImageTypeException;
import steganography.util.ImageSequenceUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * The class splits an animated gif into several single frame gifs or vice versa
 * @author Selina Wernike
 */
public class AnimatedGif implements Steganography{
        private IGIFMaker maker;

    public AnimatedGif() {
        maker = new GIFMakerImageIO();
    }

    @Override
    public byte[] encode(byte[] carrier, byte[] payload) throws IOException, MediaNotFoundException, UnsupportedMediaTypeException, MediaReassemblingException, MediaCapacityException {
        return encode(payload,carrier, ImageSteg.DEFAULT_SEED);
    }

    /**
     * Encodes a payload into the frames of an animated Gif and returns a gif
     * @see steganography.image.GIFMakerImageIO#splitGIF(byte[])
     * @see steganography.image.GIFMakerImageIO#sequenzGIF(byte[][])
     */
    @Override
        public byte[] encode(byte[] payload, byte[] animatedGif, long seed) throws IOException, MediaNotFoundException, UnsupportedMediaTypeException, MediaReassemblingException, MediaCapacityException {
            Steganography steg = new ImageSteg(); //ImageStegIO
            if (animatedGif != null && payload != null) {
                byte[][] gifFrames = maker.splitGIF(animatedGif);
                byte[][] encoded = gifFrames;
                List<byte[]> payloads = ImageSequenceUtils.sequenceDistribution(Arrays.asList(gifFrames),payload);
                for(int i = 0; i < payloads.size();i++) {
                    if(payloads.get(i) != null) {
                        encoded[i] = steg.encode(gifFrames[i], payloads.get(i), seed);
                    }

                }
                return maker.sequenzGIF(encoded);
            }
            throw new NullPointerException("Image or payload are null");
        }

    @Override
    public byte[] decode(byte[] steganographicData) throws IOException, MediaNotFoundException, UnsupportedMediaTypeException, UnknownStegFormatException {
        return decode(steganographicData, ImageSteg.DEFAULT_SEED);
    }

    /**
     * Decodes a payload from an animated GIF
     * @see steganography.image.GIFMakerImageIO#splitGIF(byte[])
     * @see steganography.image.GIFMakerImageIO#sequenzGIF(byte[][])
     */
    @Override
        public byte[] decode(byte[] stegGif, long seed) throws UnsupportedImageTypeException, NoImageException, IOException {
            ImageSteg steg = new ImageSteg();
            byte[][] gifFrames = maker.splitGIF(stegGif);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try {


                for (byte[] frame : gifFrames) {

                    byte[] decoded = steg.decode(frame, seed);
                    if (decoded != null && decoded.length >= 1) {
                        bos.write(decoded);
                    }
                }

                return bos.toByteArray();
            } catch (UnknownStegFormatException e) {
                return bos.toByteArray();
            } finally {
                bos.close();
            }
        }

    @Override
    public boolean isSteganographicData(byte[] data) throws IOException, MediaNotFoundException, UnsupportedMediaTypeException {
        return isSteganographicData(data, ImageSteg.DEFAULT_SEED);
    }

    @Override
    public boolean isSteganographicData(byte[] data, long seed) throws IOException, MediaNotFoundException, UnsupportedMediaTypeException {
        return isSteganographicData(data,seed);
    }
}
