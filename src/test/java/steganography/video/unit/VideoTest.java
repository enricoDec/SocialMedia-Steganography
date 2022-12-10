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
import steganography.video.exceptions.UnsupportedVideoTypeException;
import steganography.video.exceptions.VideoNotFoundException;

import java.io.File;
import java.io.IOException;

/**
 * @author : Enrico Gamil Toros de Chadarevian
 * Project name : steganography
 * @version : 1.0
 * @since : 28-11-2020
 **/
public class VideoTest {
    private final File ffmpegBin = new File("src/main/resources");
    private final File carrier = new File("src/test/java/steganography/video/resources/Carrier.mp4");

    /**
     * Try to pass invalid data
     * Bad Test
     */
    @Test
    public void decodeToPictureWrongData() {
        byte[] randomData = new byte[100];

        try {
            new Video(randomData, ffmpegBin);
        } catch (RuntimeException | VideoNotFoundException | UnsupportedVideoTypeException e) {
            //do nothing
        }
    }

    /**
     * Check if Video data is read correctly
     * Good Test
     */
    @Test
    public void decodeToPictureMetadata() {
        try {
            Video video = new Video(ByteArrayUtils.read(carrier), ffmpegBin);
            Assertions.assertNotNull(video.getCodec());
            Assertions.assertNotEquals(0L, video.getTimebase());
            Assertions.assertNotEquals(0.0f, video.getFrameRate());
            Assertions.assertNotEquals(0L, video.getFrameCount());
            Assertions.assertNotEquals(0, video.getFrameWidth());
            Assertions.assertNotEquals(0, video.getFrameHeight());
            Assertions.assertNotNull(video.getVideoByteArray());
            Assertions.assertNotNull(video.getPixelformat());
        } catch (IOException | VideoNotFoundException | UnsupportedVideoTypeException e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }

    /**
     * Try to pass empty data
     * Bad Test
     */
    @Test
    public void decodeToPictureEmptyData() {
        try {
            new Video(new byte[0], ffmpegBin);
        } catch (UnsupportedVideoTypeException e) {
            //do nothing
        } catch (VideoNotFoundException e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }

    /**
     * Try to pass invalid Path
     */
    @Test
    public void decodeToPictureInvalidFFmpegPath() {
        try {
            new Video(ByteArrayUtils.read(carrier), new File(""));
        } catch (IllegalArgumentException e) {
            // do nothing
        } catch (IOException | VideoNotFoundException | UnsupportedVideoTypeException e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }
}
