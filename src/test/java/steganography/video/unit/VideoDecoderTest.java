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

package steganography.video.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import steganography.util.ByteArrayUtils;
import steganography.video.Video;
import steganography.video.encoders.VideoDecoder;
import steganography.video.exceptions.UnsupportedVideoTypeException;
import steganography.video.exceptions.VideoNotFoundException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;

public class VideoDecoderTest {
    private final File ffmpegBin = new File("src/main/resources");
    private final File carrier = new File("src/test/java/steganography/video/resources/Carrier.mp4");
    private final File carrier_no_audio = new File("src/test/java/steganography/video/resources/Carrier_no_Audio.mp4");
    private Video video;

    /**
     * Try to decode valid Video and check frame count
     */
    @Test
    public void decodeToPictureFrameCount() {
        try {
            this.video = new Video(ByteArrayUtils.read(carrier), ffmpegBin);
            VideoDecoder decoder = new VideoDecoder(video, ffmpegBin, true);
            Assertions.assertTrue(video.getFrameCount() >= decoder.decodeVideoToFrames(4).size());
        } catch (IOException | VideoNotFoundException | UnsupportedVideoTypeException e) {
            Assertions.fail("Couldn't decode Video");
        }
    }

    /**
     * Try to decode valid Video with no Audio Stream
     * TODO: fix this bug
     */
    @Test
    public void decodeToPictureNoAudio() {
        try {
            this.video = new Video(ByteArrayUtils.read(carrier_no_audio), ffmpegBin);
            VideoDecoder decoder = new VideoDecoder(video, ffmpegBin, true);
            List<byte[]> pictureList = decoder.decodeVideoToFrames(4);

            Assertions.assertNotNull(pictureList);

        } catch (IOException | VideoNotFoundException | UnsupportedVideoTypeException e) {
            Assertions.fail("Couldn't decode Video");
        }
    }

    /**
     * Check if Video is decoded to valid buff images
     */
    @Test
    public void decodeToPictureValidBufferedImage() {
        try {
            this.video = new Video(ByteArrayUtils.read(carrier), ffmpegBin);
            VideoDecoder decoder = new VideoDecoder(video, ffmpegBin, true);
            ImageIO.setUseCache(false);
            List<byte[]> imageList = decoder.decodeVideoToFrames(4);

            for (byte[] image : imageList) {
                BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(image));
                Assertions.assertNotNull(bufferedImage.getData());
            }

        } catch (IOException | VideoNotFoundException | UnsupportedVideoTypeException e) {
            Assertions.fail(e.toString());
        }
    }
}
