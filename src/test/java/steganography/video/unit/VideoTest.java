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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import steganography.video.Video;

import java.io.*;

/**
 * @author : Enrico Gamil Toros de Chadarevian
 * Project name : steganography
 * @version : 1.0
 * @since : 28-11-2020
 **/
public class VideoTest {
    private File ffmpegBin = new File("src/main/resources");

    @BeforeEach
    public void beforeEach() {
    }

    @Test
    /**
     * Check if Video data is read correctly
     */
    public void decodeToPictureMetadata() {
        try {
            Video video = new Video(new FileInputStream(new File("src/test/java/steganography/video/resources/video1.mp4")).readAllBytes(), ffmpegBin);
            Assertions.assertNotNull(video.getCodec());
            Assertions.assertNotEquals(0, video.getTimebase());
            Assertions.assertNotEquals(0, video.getFrameRate());
            Assertions.assertNotEquals(0, video.getFrameCount());
            Assertions.assertNotEquals(0, video.getFrameWidth());
            Assertions.assertNotEquals(0, video.getFrameHeight());
            Assertions.assertNotNull(video.getVideoByteArray());
            Assertions.assertNotNull(video.getPixelformat());
        } catch (IOException e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }

    @Test
    /**
     * Try to pass invalid data
     */
    public void decodeToPictureWrongData() {
        try {
            new Video(new FileInputStream(new File("src/test/java/steganography/video/resources/audio.mp3")).readAllBytes(), ffmpegBin);
        } catch (UnsupportedEncodingException e) {
            //do nothing
        } catch (IOException e) {
            Assertions.fail();
        }
    }

    @Test
    /**
     * Try to pass empty data
     */
    public void decodeToPictureEmptyData() {
        try {
            new Video(new byte[0], ffmpegBin);
        } catch (IllegalArgumentException e) {
            //do nothing
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }

    @Test
    /**
     * Try to pass invalid Path
     */
    public void decodeToPictureInvalidFFmpegPath() {
        try {
            new Video(new FileInputStream(new File("src/test/java/steganography/video/resources/audio.mp3")).readAllBytes(), new File(""));
        } catch (IllegalArgumentException e) {
            // do nothing
        } catch (IOException e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }
}
