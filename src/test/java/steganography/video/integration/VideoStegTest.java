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
import steganography.video.Video;
import steganography.video.VideoSteg;
import steganography.video.exceptions.UnsupportedVideoTypeException;
import steganography.video.exceptions.VideoCapacityException;
import steganography.video.exceptions.VideoNotFoundException;

import javax.imageio.ImageIO;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * @author : Enrico Gamil Toros de Chadarevian
 * Project name : steganography
 * @version : 1.0
 * @since : 28-11-2020
 **/
public class VideoStegTest {
    private final byte[] payload = "Hallo Welt".getBytes(StandardCharsets.UTF_8);
    private final File carrier = new File("src/test/java/steganography/video/resources/Carrier.mp4");
    private final File carrier_no_audio = new File("src/test/java/steganography/video/resources/Carrier_no_Audio.mp4");

    /**
     * Multi Thread Test
     * Default Seed Integration Test
     * Good Test
     */
    @Test
    public void encoderIntegrationTest() {
        try {
            //Encode
            VideoSteg videoSteg = new VideoSteg();
            videoSteg.setDebug(true);
            ImageIO.setUseCache(false);
            videoSteg.setMaxEncodingThreads(4);
            videoSteg.setMaxDecodingThreads(4);
            byte[] encodedVideo = videoSteg.encode(ByteArrayUtils.read(carrier), payload);

            //Assert result Video is not null
            Assertions.assertNotNull(encodedVideo);
            //Assert result Video is bigger than Original Video
            Assertions.assertTrue(ByteArrayUtils.read(carrier).length < encodedVideo.length);

            //Decode
            byte[] decodedPayload = videoSteg.decode(encodedVideo);

            //Assert decoded payload is same as original payload
            Assertions.assertTrue(Arrays.equals(payload, decodedPayload));
        } catch (IOException | ImageWritingException | NoImageException | UnsupportedImageTypeException | ImageCapacityException | UnsupportedVideoTypeException | VideoNotFoundException | VideoCapacityException e) {
            e.printStackTrace();
            Assertions.fail("Video could ne be read");
        }
    }

    /**
     * Multi Thread Test
     * Custom Seed Integration Test
     * Good Test
     */
    @Test
    public void encoderIntegrationWithSeedTest() {
        try {
            //Encode
            VideoSteg videoSteg = new VideoSteg();
            videoSteg.setDebug(true);
            ImageIO.setUseCache(false);
            videoSteg.setMaxEncodingThreads(4);
            videoSteg.setMaxDecodingThreads(4);
            byte[] encodedVideo = videoSteg.encode(ByteArrayUtils.read(carrier), payload, 87143654783654L);

            //Assert result Video is not null and bigger than Original Video
            Assertions.assertNotNull(encodedVideo);
            Assertions.assertTrue(ByteArrayUtils.read(carrier).length < encodedVideo.length);

            //Decode with same Seed
            byte[] decodedPayload = videoSteg.decode(encodedVideo, 87143654783654L);

            //Assert decoded payload is same as original payload
            System.out.println(new String(decodedPayload, StandardCharsets.UTF_8) + " test");
            Assertions.assertTrue(Arrays.equals(payload, decodedPayload));
        } catch (IOException | UnsupportedImageTypeException | NoImageException | ImageWritingException | ImageCapacityException | VideoCapacityException | VideoNotFoundException | UnsupportedVideoTypeException e) {
            e.printStackTrace();
            Assertions.fail("Video could ne be read");
        }
    }

    /**
     * Multi Thread Test
     * No Seed Integration Test
     * Good Test
     */
    @Test
    public void encoderIntegrationNoSeedTest() {
        try {
            VideoSteg videoSteg = new VideoSteg();
            videoSteg.setDebug(true);
            ImageIO.setUseCache(false);
            videoSteg.setMaxEncodingThreads(4);
            videoSteg.setMaxDecodingThreads(4);
            byte[] encodedVideo = videoSteg.encode(ByteArrayUtils.read(carrier), payload, 0);
            Assertions.assertNotNull(encodedVideo);
            Assertions.assertTrue(ByteArrayUtils.read(carrier).length < encodedVideo.length);

            byte[] decodedPayload = videoSteg.decode(encodedVideo, 0);

            Assertions.assertTrue(Arrays.equals(payload, decodedPayload));
        } catch (IOException | UnsupportedImageTypeException | NoImageException | ImageWritingException | ImageCapacityException | VideoCapacityException | VideoNotFoundException | UnsupportedVideoTypeException e) {
            e.printStackTrace();
            Assertions.fail("Video could ne be read");
        }
    }

    /**
     * Multi Thread Test
     * No Audio in Video
     * Good Test
     */
    @Test
    public void encoderIntegrationNoAudioTest() {
        try {
            VideoSteg videoSteg = new VideoSteg();
            videoSteg.setDebug(true);
            ImageIO.setUseCache(false);
            videoSteg.setMaxEncodingThreads(4);
            videoSteg.setMaxDecodingThreads(4);
            byte[] encodedVideo = videoSteg.encode(ByteArrayUtils.read(carrier), payload, 0);
            Assertions.assertNotNull(encodedVideo);
            Assertions.assertTrue(ByteArrayUtils.read(carrier).length < encodedVideo.length);

            byte[] decodedPayload = videoSteg.decode(encodedVideo, 0);

            Assertions.assertTrue(Arrays.equals(payload, decodedPayload));
        } catch (IOException | UnsupportedImageTypeException | NoImageException | ImageWritingException | ImageCapacityException | VideoCapacityException | VideoNotFoundException | UnsupportedVideoTypeException e) {
            e.printStackTrace();
            Assertions.fail("Video could ne be read");
        }
    }

    /**
     * Single Thread Test
     * Default Seed Integration Test
     */
    @Test
    public void encoderIntegrationSingleThreadTest() {
        try {
            VideoSteg videoSteg = new VideoSteg();
            videoSteg.setDebug(true);
            ImageIO.setUseCache(false);
            videoSteg.setMaxEncodingThreads(1);
            videoSteg.setMaxDecodingThreads(1);
            byte[] encodedVideo = videoSteg.encode(ByteArrayUtils.read(carrier), payload);
            Assertions.assertNotNull(encodedVideo);
            Assertions.assertTrue(ByteArrayUtils.read(carrier).length < encodedVideo.length);

            byte[] decodedPayload = videoSteg.decode(encodedVideo);

            Assertions.assertTrue(Arrays.equals(payload, decodedPayload));
        } catch (IOException | ImageWritingException | NoImageException | UnsupportedImageTypeException | ImageCapacityException | VideoCapacityException | VideoNotFoundException | UnsupportedVideoTypeException e) {
            e.printStackTrace();
            Assertions.fail("Video could ne be read");
        }

    }

    /**
     * Single Thread Test
     * Custom Seed Integration Test
     */
    @Test
    public void encoderIntegrationWithSeedSingleThreadTest() {
        try {
            VideoSteg videoSteg = new VideoSteg();
            videoSteg.setDebug(true);
            ImageIO.setUseCache(false);
            videoSteg.setMaxEncodingThreads(1);
            videoSteg.setMaxDecodingThreads(1);
            byte[] encodedVideo = videoSteg.encode(ByteArrayUtils.read(carrier), payload, 87143654783654L);
            Assertions.assertNotNull(encodedVideo);
            Assertions.assertTrue(ByteArrayUtils.read(carrier).length < encodedVideo.length);

            byte[] decodedPayload = videoSteg.decode(encodedVideo, 87143654783654L);

            Assertions.assertTrue(Arrays.equals(payload, decodedPayload));
        } catch (IOException | UnsupportedImageTypeException | NoImageException | ImageWritingException | ImageCapacityException | VideoCapacityException | VideoNotFoundException | UnsupportedVideoTypeException e) {
            e.printStackTrace();
            Assertions.fail("Video could ne be read");
        }
    }

    /**
     * Single Thread Test
     * No Seed Integration Test
     */
    @Test
    public void encoderIntegrationSeed0SingleThreadTest() {
        try {
            VideoSteg videoSteg = new VideoSteg();
            videoSteg.setDebug(true);
            ImageIO.setUseCache(false);
            videoSteg.setMaxEncodingThreads(1);
            videoSteg.setMaxDecodingThreads(1);
            byte[] encodedVideo = videoSteg.encode(ByteArrayUtils.read(carrier), payload, 0);
            Assertions.assertNotNull(encodedVideo);
            Assertions.assertTrue(ByteArrayUtils.read(carrier).length < encodedVideo.length);

            byte[] decodedPayload = videoSteg.decode(encodedVideo, 0);

            Assertions.assertTrue(Arrays.equals(payload, decodedPayload));
        } catch (IOException | UnsupportedImageTypeException | NoImageException | ImageWritingException | ImageCapacityException | VideoNotFoundException | UnsupportedVideoTypeException | VideoCapacityException e) {
            e.printStackTrace();
            Assertions.fail("Video could ne be read");
        }
    }

    /**
     * Single Thread Test
     * No Audio in Video
     */

    public void encoderIntegrationNoAudioSingleThreadTest() {
        try {
            VideoSteg videoSteg = new VideoSteg();
            videoSteg.setDebug(true);
            ImageIO.setUseCache(false);
            videoSteg.setMaxEncodingThreads(1);
            videoSteg.setMaxDecodingThreads(1);
            byte[] encodedVideo = videoSteg.encode(ByteArrayUtils.read(carrier_no_audio), payload, 0);
            Assertions.assertNotNull(encodedVideo);
            Assertions.assertTrue(ByteArrayUtils.read(carrier_no_audio).length < encodedVideo.length);

            Video stegVideo = new Video(encodedVideo, videoSteg.getFfmpegBin());
            Assertions.assertFalse(stegVideo.hasAudioStream());

            byte[] decodedPayload = videoSteg.decode(encodedVideo);
            Assertions.assertTrue(Arrays.equals(ByteArrayUtils.read(carrier_no_audio), decodedPayload));
        } catch (IOException | UnsupportedImageTypeException | NoImageException | ImageWritingException | ImageCapacityException | VideoCapacityException | VideoNotFoundException | UnsupportedVideoTypeException e) {
            e.printStackTrace();
            Assertions.fail("Video could ne be read");
        }
    }

    /**
     * Try to encode a Large Payload in the carrier
     * Rand Test
     */
    @Test
    public void encoderIntegrationLargePayloadTest() {
        File randomFile = null;
        RandomAccessFile largePayload = null;
        try {
            VideoSteg videoSteg = new VideoSteg();
            videoSteg.setDebug(false);
            ImageIO.setUseCache(false);
            videoSteg.setMaxEncodingThreads(4);
            videoSteg.setMaxDecodingThreads(4);

            randomFile = new File("largePayload");
            largePayload = new RandomAccessFile(randomFile, "rw");
            long maxPayloadBytes = videoSteg.getVideoCapacity(ByteArrayUtils.read(carrier));
            largePayload.setLength(maxPayloadBytes);
            byte[] buffer = new byte[(int) maxPayloadBytes - 1];
            largePayload.readFully(buffer);

            byte[] encodedVideo = videoSteg.encode(ByteArrayUtils.read(carrier), buffer);
            Assertions.assertNotNull(encodedVideo);
            Assertions.assertTrue(ByteArrayUtils.read(carrier).length < encodedVideo.length);

            byte[] decodedPayload = videoSteg.decode(encodedVideo);

            Assertions.assertTrue(Arrays.equals(buffer, decodedPayload));
        } catch (IOException | UnsupportedImageTypeException | NoImageException | ImageWritingException | ImageCapacityException | VideoNotFoundException | UnsupportedVideoTypeException | VideoCapacityException e) {
            e.printStackTrace();
            Assertions.fail("Video could not be read");
        }
        try {
            largePayload.close();
        } catch (IOException e) {
            Assertions.fail(e.toString());
        }
        randomFile.deleteOnExit();
    }

    /**
     * Payload Larger than capacity Test
     * Bad Test
     */
    @Test
    public void encoderIntegrationVeryLargePayloadTest() throws IOException, NoImageException, UnsupportedImageTypeException, VideoNotFoundException, UnsupportedVideoTypeException {
        VideoSteg videoSteg = new VideoSteg();
        videoSteg.setDebug(true);
        ImageIO.setUseCache(false);
        videoSteg.setMaxEncodingThreads(4);
        videoSteg.setMaxDecodingThreads(4);

        long maxPayloadBytes = videoSteg.getVideoCapacity(ByteArrayUtils.read(carrier));

        File randomFile = new File("largePayload");
        RandomAccessFile largePayload = new RandomAccessFile(randomFile, "rw");
        largePayload.setLength(maxPayloadBytes + 1);
        byte[] buffer = new byte[(int) maxPayloadBytes + 1];
        largePayload.readFully(buffer);
        Assertions.assertThrows(VideoCapacityException.class, () -> videoSteg.encode(ByteArrayUtils.read(carrier), buffer)
        );
        largePayload.close();
        randomFile.deleteOnExit();
    }
}
