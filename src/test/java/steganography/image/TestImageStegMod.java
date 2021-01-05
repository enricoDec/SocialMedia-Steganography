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
import steganography.image.exceptions.NoImageException;
import steganography.image.exceptions.UnsupportedImageTypeException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class TestImageStegMod {

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                          EN-DECODING
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    // SUCCESS
    // ------------------------------------

    @Test
    void given_PNGAndSeedNoHeader_when_encodingAndDecodingString_expect_success()
            throws UnsupportedMediaTypeException, MediaNotFoundException,
            MediaReassemblingException, UnknownStegFormatException, MediaCapacityException, IOException {
        System.out.println("given_PNGAndSeedNoHeader_when_encodingAndDecodingString_expect_success:");
        String pathToImage = "src/test/resources/steganography/image/baum.png";

        String loremIpsum = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor " +
                "invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et " +
                "justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum " +
                "dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod " +
                "tempor invidunt ut labor";

        byte[] imageIntermediate;

        /////// ENCODE
        long seed = 121212L;
            Steganography steganography = new ImageStegMod();
            byte[] imageInput = Files.readAllBytes(new File(pathToImage).toPath());

            long startTime = System.currentTimeMillis();
            imageIntermediate = steganography.encode(imageInput, loremIpsum.getBytes(), seed);
            System.out.println("Encoding time (ms): " + (System.currentTimeMillis() - startTime));

/*

        ////// WRITE IMAGE
        try (FileOutputStream fos = new FileOutputStream("src/test/resources/steganography/image/baumENC.png")) {
            assert imageIntermediate != null;
            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imageIntermediate));
            ImageIO.write(bufferedImage, "png", fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
*/


        ////// DECODE
        steganography = new ImageStegMod();

        startTime = System.currentTimeMillis();

        byte[] result = steganography.decode(imageIntermediate, seed);

        System.out.println("Decoding time (ms): " + (System.currentTimeMillis() - startTime));

        Assertions.assertEquals(new String(result), loremIpsum);
    }

    @Test
    void given_PNGNoHeaderNoSeed_when_encodingAndDecodingString_expect_success()
            throws UnsupportedMediaTypeException, MediaNotFoundException,
            MediaReassemblingException, UnknownStegFormatException, MediaCapacityException, IOException {

        System.out.println("given_PNGNoHeaderNoSeed_when_encodingAndDecodingString_expect_success:");
        String pathToImage = "src/test/resources/steganography/image/baum.png";

        String loremIpsum = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor " +
                "invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et " +
                "justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum " +
                "dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod " +
                "tempor invidunt ut labor";

        byte[] imageIntermediate;

        /////// ENCODE
        Steganography steganography = new ImageStegMod();
        byte[] imageInput = Files.readAllBytes(new File(pathToImage).toPath());

        long startTime = System.currentTimeMillis();
        imageIntermediate = steganography.encode(imageInput, loremIpsum.getBytes());
        System.out.println("Encoding time (ms): " + (System.currentTimeMillis() - startTime));
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
        steganography = new ImageStegMod();

        startTime = System.currentTimeMillis();

        byte[] result = steganography.decode(imageIntermediate);

        System.out.println("Decoding time (ms): " + (System.currentTimeMillis() - startTime));

        Assertions.assertEquals(new String(result), loremIpsum);
    }

    @Test
    void given_BMPAndSeedNoHeader_when_encodingAndDecodingString_expect_success()
            throws UnsupportedMediaTypeException, MediaNotFoundException,
            MediaReassemblingException, UnknownStegFormatException, MediaCapacityException, IOException {
        System.out.println("given_BMPAndSeedNoHeader_when_encodingAndDecodingString_expect_success:");
        String pathToImage = "src/test/resources/steganography/image/baum.bmp";

        String loremIpsum = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor " +
                "invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et " +
                "justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum " +
                "dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod " +
                "tempor invidunt ut labor";

        byte[] imageIntermediate;

        /////// ENCODE
        long seed = 121212L;

        Steganography steganography = new ImageStegMod();
        byte[] imageInput = Files.readAllBytes(new File(pathToImage).toPath());

        long startTime = System.currentTimeMillis();
        imageIntermediate = steganography.encode(imageInput, loremIpsum.getBytes(), seed);
        System.out.println("Encoding time (ms): " + (System.currentTimeMillis() - startTime));

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
        steganography = new ImageStegMod();

        startTime = System.currentTimeMillis();

        byte[] result = steganography.decode(imageIntermediate, seed);

        System.out.println("Decoding time (ms): " + (System.currentTimeMillis() - startTime));

        Assertions.assertEquals(new String(result), loremIpsum);
    }

    @Test
    void given_BMPNoHeaderNoSeed_when_encodingAndDecodingString_expect_success()
            throws UnsupportedMediaTypeException, MediaNotFoundException,
            MediaReassemblingException, UnknownStegFormatException, MediaCapacityException, IOException {

        System.out.println("given_BMPNoHeaderNoSeed_when_encodingAndDecodingString_expect_success:");
        String pathToImage = "src/test/resources/steganography/image/baum.bmp";

        String loremIpsum = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor " +
                "invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et " +
                "justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum " +
                "dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod " +
                "tempor invidunt ut labor";

        byte[] imageIntermediate;

        /////// ENCODE
        Steganography steganography = new ImageStegMod();
        byte[] imageInput = Files.readAllBytes(new File(pathToImage).toPath());

        long startTime = System.currentTimeMillis();
        imageIntermediate = steganography.encode(imageInput, loremIpsum.getBytes());
        System.out.println("Encoding time (ms): " + (System.currentTimeMillis() - startTime));
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
        steganography = new ImageStegMod();

        startTime = System.currentTimeMillis();

        byte[] result = steganography.decode(imageIntermediate);

        System.out.println("Decoding time (ms): " + (System.currentTimeMillis() - startTime));

        Assertions.assertEquals(new String(result), loremIpsum);
    }


    // UNSUPPORTED_FORMATS
    // ------------------------------------

    @Test
    void given_BMPWithTransparency_when_encoding_expect_UnsupportedImageTypeException() throws IOException {

        String pathToImage = "src/test/resources/steganography/image/baumTP.bmp";

        String loremIpsum = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor " +
                "invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et " +
                "justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum " +
                "dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod " +
                "tempor invidunt ut labor";

        long seed = 121212L;
        Steganography steganography = new ImageStegMod();
        byte[] imageInput = Files.readAllBytes(new File(pathToImage).toPath());

        Assertions.assertThrows(
                UnsupportedImageTypeException.class,
                () -> steganography.encode(imageInput, loremIpsum.getBytes(), seed)
        );
    }

    @Test
    void given_JPG_when_encoding_expect_UnsupportedImageTypeException() throws IOException {

        BufferedImage bufferedImage = new BufferedImage(1, 1, BufferedImage.TYPE_3BYTE_BGR);

        String loremIpsum = "Hello World";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "jpg", baos);

        Assertions.assertThrows(
                UnsupportedImageTypeException.class,
                () -> new ImageStegMod().encode(baos.toByteArray(), loremIpsum.getBytes())
        );
    }

    @Test
    void given_JPG_when_decoding_expect_UnsupportedImageTypeException() throws IOException {

        BufferedImage bufferedImage = new BufferedImage(1, 1, BufferedImage.TYPE_3BYTE_BGR);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "jpg", baos);

        Assertions.assertThrows(
                UnsupportedImageTypeException.class,
                () -> new ImageStegMod().decode(baos.toByteArray())
        );
    }

    @Test
    void given_TIFF_when_encoding_expect_UnsupportedImageTypeException() throws IOException {

        BufferedImage bufferedImage = new BufferedImage(1, 1, BufferedImage.TYPE_3BYTE_BGR);

        String loremIpsum = "Hello World";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "tiff", baos);

        Assertions.assertThrows(
                UnsupportedImageTypeException.class,
                () -> new ImageStegMod().encode(baos.toByteArray(), loremIpsum.getBytes())
        );

    }

    @Test
    void given_TIFF_when_decoding_expect_UnsupportedImageTypeException() throws IOException {

        BufferedImage bufferedImage = new BufferedImage(1, 1, BufferedImage.TYPE_3BYTE_BGR);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "tiff", baos);

        Assertions.assertThrows(
                UnsupportedImageTypeException.class,
                () -> new ImageStegMod().decode(baos.toByteArray())
        );

    }

    @Test
    void given_WBMP_when_encoding_expect_UnsupportedImageTypeException() throws IOException {

        BufferedImage bufferedImage = new BufferedImage(1, 1, BufferedImage.TYPE_BYTE_BINARY);

        String loremIpsum = "Hello World";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "wbmp", baos);

        Assertions.assertThrows(
                UnsupportedImageTypeException.class,
                () -> new ImageStegMod().encode(baos.toByteArray(), loremIpsum.getBytes())
        );

    }

    @Test
    void given_WBMP_when_decoding_expect_UnsupportedImageTypeException() throws IOException {

        BufferedImage bufferedImage = new BufferedImage(1, 1, BufferedImage.TYPE_BYTE_BINARY);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "wbmp", baos);

        Assertions.assertThrows(
                UnsupportedImageTypeException.class,
                () -> new ImageStegMod().decode(baos.toByteArray())
        );

    }


    // NULL_VALUES
    // ------------------------------------

    @Test
    void given_imageIsNull_when_encoding_expect_NullPointerException() {

        Assertions.assertThrows(
                NullPointerException.class,
                () -> new ImageStegMod().encode(null, "Hello World".getBytes())
        );
    }

    @Test
    void given_imageIsEmpty_when_encoding_expect_NoImageException() {

        Assertions.assertThrows(
                NoImageException.class,
                () -> new ImageStegMod().encode(new byte[0], "Hello World".getBytes())
        );
    }

    @Test
    void given_imageIsNull_when_decoding_expect_NullPointerException() {

        Assertions.assertThrows(
                NullPointerException.class,
                () -> new ImageStegMod().decode(null)
        );
    }

    @Test
    void given_payloadIsNull_when_encoding_expect_NullPointerException() {

        String pathToImage = "src/test/resources/steganography/image/baum.bmp";

        Assertions.assertThrows(
                NullPointerException.class,
                () -> new ImageStegMod().encode(Files.readAllBytes(new File(pathToImage).toPath()), null)
        );
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                          IMAGE CAPACITY
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    // SUCCESS
    // ------------------------------------

    @Test
    void given_80PixelPNGNoDefaultHeader_when_getCapacity_expect_10() throws IOException, UnsupportedImageTypeException, NoImageException {

        BufferedImage bufferedImage = new BufferedImage(8, 10, BufferedImage.TYPE_3BYTE_BGR);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", baos);

        Assertions.assertEquals(
                10,
                new ImageSteg(false, false).getImageCapacity(
                        baos.toByteArray()
                )
        );

    }
}