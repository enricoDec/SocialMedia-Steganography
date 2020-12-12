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

package steganography.video.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import steganography.image.exceptions.ImageCapacityException;
import steganography.image.exceptions.ImageWritingException;
import steganography.image.exceptions.NoImageException;
import steganography.image.exceptions.UnsupportedImageTypeException;
import steganography.util.ByteArrayUtils;
import steganography.video.VideoSteg;

import javax.imageio.ImageIO;
import java.io.*;
import java.util.Arrays;

/**
 * @author : Enrico Gamil Toros de Chadarevian
 * Project name : steganography
 * @version : 1.0
 * @since : 28-11-2020
 **/
public class VideoStegTest {

    /**
     * Multi Thread Test
     * Default Seed Integration Test
     */
    @Test
    public void encoderIntegrationTest() throws UnsupportedImageTypeException, NoImageException, ImageWritingException, ImageCapacityException {
        try {
            VideoSteg videoSteg = new VideoSteg();
            videoSteg.setDebug(true);
            ImageIO.setUseCache(false);
            videoSteg.setMaxEncodingThreads(4);
            videoSteg.setMaxDecodingThreads(4);
            byte[] encodedVideo = videoSteg.encode(ByteArrayUtils.read(new File("src/test/java/steganography/video/resources/video3.MP4")),
                    ByteArrayUtils.read(new File("src/test/java/steganography/video/resources/payload1.gif")));
            Assertions.assertNotNull(encodedVideo);
            Assertions.assertTrue(ByteArrayUtils.read(new File("src/test/java/steganography/video/resources/video3.MP4")).length < encodedVideo.length);

            byte[] decodedPayload = videoSteg.decode(encodedVideo);

            Assertions.assertTrue(Arrays.equals(ByteArrayUtils.read(new File("src/test/java/steganography/video/resources/payload1.gif")), decodedPayload));
        } catch (IOException e) {
            e.printStackTrace();
            Assertions.fail("Video could ne be read");
        }

    }

    /**
     * Multi Thread Test
     * Custom Seed Integration Test
     */
    @Test
    public void encoderIntegrationWithSeedTest() throws UnsupportedImageTypeException, NoImageException, ImageWritingException, ImageCapacityException {
        try {
            VideoSteg videoSteg = new VideoSteg();
            videoSteg.setDebug(true);
            ImageIO.setUseCache(false);
            videoSteg.setMaxEncodingThreads(4);
            videoSteg.setMaxDecodingThreads(4);
            byte[] encodedVideo = videoSteg.encode(ByteArrayUtils.read(new File("src/test/java/steganography/video/resources/video3.MP4")),
                    ByteArrayUtils.read(new File("src/test/java/steganography/video/resources/payload1.gif")) , 87143654783654L);
            Assertions.assertNotNull(encodedVideo);
            Assertions.assertTrue(ByteArrayUtils.read(new File("src/test/java/steganography/video/resources/video3.MP4")).length < encodedVideo.length);

            byte[] decodedPayload = videoSteg.decode(encodedVideo, 87143654783654L);

            Assertions.assertTrue(Arrays.equals(ByteArrayUtils.read(new File("src/test/java/steganography/video/resources/payload1.gif")), decodedPayload));
        } catch (IOException e) {
            e.printStackTrace();
            Assertions.fail("Video could ne be read");
        }
    }

    /**
     * Multi Thread Test
     * No Seed Integration Test
     */
    @Test
    public void encoderIntegrationNoSeedTest() throws UnsupportedImageTypeException, NoImageException, ImageWritingException, ImageCapacityException {
        try {
            VideoSteg videoSteg = new VideoSteg();
            videoSteg.setDebug(true);
            ImageIO.setUseCache(false);
            videoSteg.setMaxEncodingThreads(4);
            videoSteg.setMaxDecodingThreads(4);
            byte[] encodedVideo = videoSteg.encode(ByteArrayUtils.read(new File("src/test/java/steganography/video/resources/video3.MP4")),
                    ByteArrayUtils.read(new File("src/test/java/steganography/video/resources/payload1.gif")), 0);
            Assertions.assertNotNull(encodedVideo);
            Assertions.assertTrue(ByteArrayUtils.read(new File("src/test/java/steganography/video/resources/video3.MP4")).length < encodedVideo.length);

            byte[] decodedPayload = videoSteg.decode(encodedVideo, 0);

            Assertions.assertTrue(Arrays.equals(ByteArrayUtils.read(new File("src/test/java/steganography/video/resources/payload1.gif")), decodedPayload));
        } catch (IOException e) {
            e.printStackTrace();
            Assertions.fail("Video could ne be read");
        }
    }

    /**
     * Multi Thread Test
     * No Audio in Video
     */
    @Test
    public void encoderIntegrationNoAudioTest() throws UnsupportedImageTypeException, NoImageException, ImageWritingException, ImageCapacityException {
        try {
            VideoSteg videoSteg = new VideoSteg();
            videoSteg.setDebug(true);
            ImageIO.setUseCache(false);
            videoSteg.setMaxEncodingThreads(4);
            videoSteg.setMaxDecodingThreads(4);
            byte[] encodedVideo = videoSteg.encode(ByteArrayUtils.read(new File("src/test/java/steganography/video/resources/video2.MP4")),
                    ByteArrayUtils.read(new File("src/test/java/steganography/video/resources/payload1.gif")), 0);
            Assertions.assertNotNull(encodedVideo);
            Assertions.assertTrue(ByteArrayUtils.read(new File("src/test/java/steganography/video/resources/video2.MP4")).length < encodedVideo.length);

            byte[] decodedPayload = videoSteg.decode(encodedVideo, 0);

            Assertions.assertTrue(Arrays.equals(ByteArrayUtils.read(new File("src/test/java/steganography/video/resources/payload1.gif")), decodedPayload));
        } catch (IOException e) {
            e.printStackTrace();
            Assertions.fail("Video could ne be read");
        }
    }

    /**
     * Single Thread Test
     * Default Seed Integration Test
     */
    @Test
    public void encoderIntegrationSingleThreadTest() throws UnsupportedImageTypeException, NoImageException, ImageWritingException, ImageCapacityException {
        try {
            VideoSteg videoSteg = new VideoSteg();
            videoSteg.setDebug(true);
            ImageIO.setUseCache(false);
            videoSteg.setMaxEncodingThreads(1);
            videoSteg.setMaxDecodingThreads(1);
            byte[] encodedVideo = videoSteg.encode(ByteArrayUtils.read(new File("src/test/java/steganography/video/resources/video3.MP4")),
                    ByteArrayUtils.read(new File("src/test/java/steganography/video/resources/payload1.gif")));
            Assertions.assertNotNull(encodedVideo);
            Assertions.assertTrue(ByteArrayUtils.read(new File("src/test/java/steganography/video/resources/video3.MP4")).length < encodedVideo.length);

            byte[] decodedPayload = videoSteg.decode(encodedVideo);

            Assertions.assertTrue(Arrays.equals(ByteArrayUtils.read(new File("src/test/java/steganography/video/resources/payload1.gif")), decodedPayload));
        } catch (IOException e) {
            e.printStackTrace();
            Assertions.fail("Video could ne be read");
        }

    }

    /**
     * Single Thread Test
     * Custom Seed Integration Test
     */
    @Test
    public void encoderIntegrationWithSeedSingleThreadTest() throws UnsupportedImageTypeException, NoImageException, ImageWritingException, ImageCapacityException {
        try {
            VideoSteg videoSteg = new VideoSteg();
            videoSteg.setDebug(true);
            ImageIO.setUseCache(false);
            videoSteg.setMaxEncodingThreads(1);
            videoSteg.setMaxDecodingThreads(1);
            byte[] encodedVideo = videoSteg.encode(ByteArrayUtils.read(new File("src/test/java/steganography/video/resources/video3.MP4")),
                    ByteArrayUtils.read(new File("src/test/java/steganography/video/resources/payload1.gif")), 87143654783654L);
            Assertions.assertNotNull(encodedVideo);
            Assertions.assertTrue(ByteArrayUtils.read(new File("src/test/java/steganography/video/resources/video3.MP4")).length < encodedVideo.length);

            byte[] decodedPayload = videoSteg.decode(encodedVideo, 87143654783654L);

            Assertions.assertTrue(Arrays.equals(ByteArrayUtils.read(new File("src/test/java/steganography/video/resources/payload1.gif")), decodedPayload));
        } catch (IOException e) {
            e.printStackTrace();
            Assertions.fail("Video could ne be read");
        }
    }

    /**
     * Single Thread Test
     * No Seed Integration Test
     */
    @Test
    public void encoderIntegrationNoSeedSingleThreadTest() throws UnsupportedImageTypeException, NoImageException, ImageWritingException, ImageCapacityException {
        try {
            VideoSteg videoSteg = new VideoSteg();
            videoSteg.setDebug(true);
            ImageIO.setUseCache(false);
            videoSteg.setMaxEncodingThreads(1);
            videoSteg.setMaxDecodingThreads(1);
            byte[] encodedVideo = videoSteg.encode(ByteArrayUtils.read(new File("src/test/java/steganography/video/resources/video3.MP4")),
                    ByteArrayUtils.read(new File("src/test/java/steganography/video/resources/payload1.gif")), 0);
            Assertions.assertNotNull(encodedVideo);
            Assertions.assertTrue(ByteArrayUtils.read(new File("src/test/java/steganography/video/resources/video3.MP4")).length < encodedVideo.length);

            byte[] decodedPayload = videoSteg.decode(encodedVideo, 0);

            Assertions.assertTrue(Arrays.equals(ByteArrayUtils.read(new File("src/test/java/steganography/video/resources/payload1.gif")), decodedPayload));
        } catch (IOException e) {
            e.printStackTrace();
            Assertions.fail("Video could ne be read");
        }
    }

    /**
     * Single Thread Test
     * No Audio in Video
     */
    @Test
    public void encoderIntegrationNoAudioSingleThreadTest() throws UnsupportedImageTypeException, NoImageException, ImageWritingException, ImageCapacityException {
        try {
            VideoSteg videoSteg = new VideoSteg();
            videoSteg.setDebug(true);
            ImageIO.setUseCache(false);
            videoSteg.setMaxEncodingThreads(1);
            videoSteg.setMaxDecodingThreads(1);
            byte[] encodedVideo = videoSteg.encode(ByteArrayUtils.read(new File("src/test/java/steganography/video/resources/video2.MP4")),
                    ByteArrayUtils.read(new File("src/test/java/steganography/video/resources/payload1.gif")), 0);
            Assertions.assertNotNull(encodedVideo);
            Assertions.assertTrue(ByteArrayUtils.read(new File("src/test/java/steganography/video/resources/video2.MP4")).length < encodedVideo.length);

            byte[] decodedPayload = videoSteg.decode(encodedVideo, 0);

            Assertions.assertTrue(Arrays.equals(ByteArrayUtils.read(new File("src/test/java/steganography/video/resources/payload1.gif")), decodedPayload));
        } catch (IOException e) {
            e.printStackTrace();
            Assertions.fail("Video could ne be read");
        }
    }

    /**
     * Large Payload Test
     */
    @Test
    public void encoderIntegrationLargePayloadTest() throws UnsupportedImageTypeException, NoImageException, ImageWritingException, ImageCapacityException {
        try {
            VideoSteg videoSteg = new VideoSteg();
            videoSteg.setDebug(true);
            ImageIO.setUseCache(false);
            videoSteg.setMaxEncodingThreads(4);
            videoSteg.setMaxDecodingThreads(4);
            byte[] encodedVideo = videoSteg.encode(ByteArrayUtils.read(new File("src/test/java/steganography/video/resources/video2.MP4")),
                    ByteArrayUtils.read(new File("src/test/java/steganography/video/resources/video1.MP4")), 0);
            Assertions.assertNotNull(encodedVideo);
            Assertions.assertTrue(ByteArrayUtils.read(new File("src/test/java/steganography/video/resources/video2.MP4")).length < encodedVideo.length);

            byte[] decodedPayload = videoSteg.decode(encodedVideo, 0);

            FileOutputStream fileOutputStream = new FileOutputStream(new File("C:\\Users\\enric\\Desktop\\Test\\test.MP4"));
            fileOutputStream.write(decodedPayload);

            Assertions.assertTrue(Arrays.equals(ByteArrayUtils.read(new File("src/test/java/steganography/video/resources/video1.MP4")), decodedPayload));
        } catch (IOException e) {
            e.printStackTrace();
            Assertions.fail("Video could ne be read");
        }
    }

    /**
     * Payload Larger than capacity Test
     */
    @Test
    public void encoderIntegrationVeryLargePayloadTest() {
        VideoSteg videoSteg = new VideoSteg();
        videoSteg.setDebug(true);
        ImageIO.setUseCache(false);
        videoSteg.setMaxEncodingThreads(4);
        videoSteg.setMaxDecodingThreads(4);
        Assertions.assertThrows(IllegalArgumentException.class, () -> videoSteg.encode(ByteArrayUtils.read(new File("src/test/java/steganography/video/resources/video1.MP4")),
                ByteArrayUtils.read(new File("src/test/java/steganography/video/resources/video4.MP4")), 0)
        );
    }
}
