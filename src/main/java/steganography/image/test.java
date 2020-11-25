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

import steganography.Steganography;
import steganography.util.BufferedImageCoordinateOverlay;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class test {

    public static void main( String[] args ) {
        String pathToImage = "../testFiles/mariot.png";

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
            long startTime = System.currentTimeMillis();
            imageIntermediate = steganography.encode(fis.readAllBytes(), baos.toByteArray());
            System.out.println("Encoding time (ms): " + (System.currentTimeMillis() - startTime));
        } catch (IOException e) {
            e.printStackTrace();
        }

        ////// WRITE IMAGE
        try (FileOutputStream fos = new FileOutputStream("../testFiles/camera_lens_String_noTP.png")) {
            assert imageIntermediate != null;
            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imageIntermediate));
            ImageIO.write(bufferedImage, "png", fos);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ////// DECODE
        try {
            Steganography steganography = new ImageSteg();

            long startTime = System.currentTimeMillis();

            DataInputStream dis = new DataInputStream(
                    new ByteArrayInputStream(
                            steganography.decode(imageIntermediate)));

            System.out.println("Decoding time (ms): " + (System.currentTimeMillis() - startTime));

            System.out.println(dis.readUTF());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
