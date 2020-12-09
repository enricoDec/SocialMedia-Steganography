/*
 * Copyright (c) 2020
 * Contributed by Henk Lubig
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
import org.junit.jupiter.api.Test;
import steganography.Steganography;
import steganography.exceptions.*;
import steganography.image.encoders.PixelBit;

import java.io.*;

public class TestImageSteg {

    @Test
    void testEncodingAndDecodingStringWithDefaultHeaderWithSeed()
            throws UnsupportedMediaTypeException, MediaNotFoundException,
            MediaReassemblingException, UnknownStegFormatException, MediaCapacityException {
        System.out.println("testEncodingAndDecodingStringWithDefaultHeader:");
        String pathToImage = "../testFiles/camera_lens.png";

        String loremIpsum = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labor";
        // better ?:
        //loremIpsum.getBytes();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        try {
            dos.writeUTF(loremIpsum);
        } catch (IOException e) {
            // pff
        }

        byte[] imageIntermediate = null;

        /////// ENCODE
        PixelBit encoder;
        long seed = 121212L;
        try (
                FileInputStream fis = new FileInputStream(pathToImage)
        ){
            Steganography steganography = new ImageSteg();
            ByteArrayOutputStream imgStream = new ByteArrayOutputStream();

            while (fis.available() > 0)
                imgStream.write(fis.read());

            long startTime = System.currentTimeMillis();
            imageIntermediate = steganography.encode(imgStream.toByteArray(), baos.toByteArray(), seed);
            System.out.println("Encoding time (ms): " + (System.currentTimeMillis() - startTime));
        } catch (IOException e) {
            e.printStackTrace();
        }
/*

        ////// WRITE IMAGE
        try (FileOutputStream fos = new FileOutputStream("../testFiles/camera_lens_String_noTP.png")) {
            assert imageIntermediate != null;
            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imageIntermediate));
            ImageIO.write(bufferedImage, "png", fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
*/

        ////// DECODE
        try {
            Steganography steganography = new ImageSteg();

            long startTime = System.currentTimeMillis();

            DataInputStream dis = new DataInputStream(
                    new ByteArrayInputStream(
                            steganography.decode(imageIntermediate, seed)));

            System.out.println("Decoding time (ms): " + (System.currentTimeMillis() - startTime));

            Assertions.assertEquals(dis.readUTF(), loremIpsum);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testEncodingAndDecodingStringWithDefaultHeaderWithoutSeed()
            throws UnsupportedMediaTypeException, MediaNotFoundException,
            MediaReassemblingException, UnknownStegFormatException, MediaCapacityException {

        System.out.println("testEncodingAndDecodingStringWithDefaultHeaderAndSeed:");
        String pathToImage = "../testFiles/camera_lens.png";

        String loremIpsum = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labor";
        // better ?:
        //loremIpsum.getBytes();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        try {
            dos.writeUTF(loremIpsum);
        } catch (IOException e) {
            // pff
        }

        byte[] imageIntermediate = null;

        /////// ENCODE
        PixelBit encoder;
        long seed = 121212L;
        try (
                FileInputStream fis = new FileInputStream(pathToImage)
        ){
            Steganography steganography = new ImageSteg();
            ByteArrayOutputStream imgStream = new ByteArrayOutputStream();

            while (fis.available() > 0)
                imgStream.write(fis.read());

            long startTime = System.currentTimeMillis();
            imageIntermediate = steganography.encode(imgStream.toByteArray(), baos.toByteArray());
            System.out.println("Encoding time (ms): " + (System.currentTimeMillis() - startTime));
        } catch (IOException e) {
            e.printStackTrace();
        }
/*

        ////// WRITE IMAGE
        try (FileOutputStream fos = new FileOutputStream("../testFiles/camera_lens_String_noTP.png")) {
            assert imageIntermediate != null;
            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imageIntermediate));
            ImageIO.write(bufferedImage, "png", fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
*/

        ////// DECODE
        try {
            Steganography steganography = new ImageSteg();

            long startTime = System.currentTimeMillis();

            DataInputStream dis = new DataInputStream(
                    new ByteArrayInputStream(
                            steganography.decode(imageIntermediate)));

            System.out.println("Decoding time (ms): " + (System.currentTimeMillis() - startTime));

            Assertions.assertEquals(dis.readUTF(), loremIpsum);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
