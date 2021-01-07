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

package steganography.image;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import steganography.exceptions.*;
import steganography.image.AnimatedGif;
import steganography.image.exceptions.UnsupportedImageTypeException;
import steganography.util.ByteArrayUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class AnimatedGifTest {

    private byte[] animatedGif;
    private AnimatedGif splicer;


    @BeforeEach
    public void before() throws IOException {
        AnimatedGif giffer = new AnimatedGif();
        File file = new File("src/test/resources/steganography/image/insta.gif");

        animatedGif = ByteArrayUtils.read(file);
        splicer = new AnimatedGif();
    }

    @Deprecated
    public void splitGif_correctInput_byte2Array() {
        try {
            byte[][] result = splicer.splitGif(animatedGif);
            Assertions.assertEquals(27, result.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void splitGifDecoder_correctInput_byte2Array() throws UnsupportedImageTypeException {
            byte[][] result = splicer.splitGifDecoder(animatedGif);
            Assertions.assertEquals(27, result.length);

    }

    @Test
    public void splitGifDecoder_nullInput_NullPointerException() {
        Assertions.assertThrows(NullPointerException.class, () -> {splicer.splitGifDecoder(null);});
    }

    @Test
    public void splitGifDecoder_notAGIF_Exception() {
        Assertions.assertThrows(UnsupportedImageTypeException.class, () -> splicer.splitGifDecoder(new byte[] {1,2,3,4,5,6,7,8,9}));
    }

    @Test
    public void sequenceGifDecoder_frames_ByteArray() throws UnsupportedImageTypeException {
        byte[][] frames = splicer.splitGifDecoder(animatedGif);
        int length = 0;
        for(int i = 0; i < frames.length; i++) {
            length = frames[i].length;
        }
        Assertions.assertTrue(length <= splicer.sequenceGifDecoder(frames).length);
    }

    @Test
    public void sequenceGifDecoder_singeleFrame_ByteArray() throws UnsupportedImageTypeException {
        byte[][] frames = splicer.splitGifDecoder(animatedGif);
        byte[][] test = new byte[1][];
        test[0] = frames[0];
        Assertions.assertTrue(frames[0].length <= splicer.sequenceGifDecoder(test).length);
    }

    @Test
    public void sequenceGifDecoder_animatedGIF_ByteArray() {
        byte[][] test = new byte[1][];
        test[0] = animatedGif;

        Assertions.assertTrue(animatedGif.length <= splicer.sequenceGifDecoder(test).length);
    }

    @Test
    public void sequenceGIFDecoder_null_NullpointerException() {
        Assertions.assertThrows(NullPointerException.class, () -> splicer.sequenceGifDecoder(null));
    }

    @Test
    public void encode_gif_ByteArray() throws UnsupportedMediaTypeException, MediaCapacityException, MediaNotFoundException, MediaReassemblingException, IOException {
        Assertions.assertTrue(splicer.encode(animatedGif, new byte[]{1,2,3,4,5,6,7}).length > animatedGif.length);
    }

    @Test
    public void encode_notAGIF_UnsupportedImageException() {
        Assertions.assertThrows(UnsupportedImageTypeException.class, () -> splicer.encode(new byte[] {2,6,9,4,5,5},new byte[]{1,2,3,4,5,6,7}));
    }

    @Test
    public void encode_carrierNull_NullPointerException() {
        Assertions.assertThrows(NullPointerException.class, () -> splicer.encode(null,new byte[]{1,2,3,4,5,6,7}));

    }

    // needs to be reviseted
    public void encode_payloadToLong_UnsupportedImageException() {
        Assertions.assertThrows(UnsupportedImageTypeException.class, () -> splicer.encode(animatedGif, animatedGif));

    }

    @Test
    public void encode_nullPayload_NullpointerException() {
        Assertions.assertThrows(NullPointerException.class, () -> splicer.encode(animatedGif, null));
    }

    @Test
    public void decode_correctInput_byteArray() throws IOException, UnsupportedMediaTypeException, MediaNotFoundException, MediaReassemblingException, MediaCapacityException, UnknownStegFormatException {
        byte[] textIn =  ByteArrayUtils.read(new File("src/test/resources/payload/test.txt"));
        byte[] message = splicer.decode(splicer.encode(animatedGif, textIn));
        Assertions.assertArrayEquals(textIn, message);

    }

    @Test
    public void decode_NotAGif_UnssupportedImageException() {
        Assertions.assertThrows(UnsupportedImageTypeException.class, () -> splicer.decode(new byte[] {2,3,4,5,6,7}));
    }

    @Test
    public void decode_Null_NullpointerException() {
        Assertions.assertThrows(NullPointerException.class, () -> splicer.decode(null));
    }

}
