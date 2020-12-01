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
import steganography.image.AnimatedGif;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class AnimatedGifTest {

    public byte[] animatedGif;
    private static String  path = "src/main/resources/";
    private AnimatedGif splicer;
    private int numberOfimg = 0;

    @BeforeEach
    public void before() {
        AnimatedGif giffer = new AnimatedGif();
        File file = new File(path + "doggy.gif");

        //create FileInputStream which obtains input bytes from a file in a file system
        //FileInputStream is meant for reading streams of raw bytes such as image data. For reading streams of characters, consider using FileReader.

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        try {
            FileInputStream fis = new FileInputStream(file);
            for (int readNum; (readNum = fis.read(buf)) != -1;) {
                //Writes to this byte array output stream
                bos.write(buf, 0, readNum);
                System.out.println("read " + readNum + " bytes,");

            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        animatedGif = bos.toByteArray();
        splicer = new AnimatedGif();
    }


    public void splitGif_correctInput_byte2Array() {
        try {
            byte[][] result = splicer.splitGif(animatedGif);
            Assertions.assertEquals(79, result.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void splitGifDecoder_correctInput_byte2Array() {
            byte[][] result = splicer.splitGifDecoder(animatedGif);
            Assertions.assertEquals(79, result.length);

    }

    @Test
    public void splitGifDecoder_nullInput_NullPointerExceptiom() {
        Assertions.assertThrows(NullPointerException.class, () -> {splicer.splitGifDecoder(null);});
    }
}
