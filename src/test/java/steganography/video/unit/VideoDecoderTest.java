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
import steganography.video.VideoDecoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;

public class VideoDecoderTest {
    private File ffmpegBin = new File("src/main/resources");
    private Video video;

    @BeforeEach
    public void beforeEach() {
    }

    @Test
    /**
     * Try to decode valid Video and check frame count
     */
    public void decodeToPictureFrameCount(){
        try {
            FileInputStream fileInputStream = new FileInputStream(new File("src/test/java/steganography/video/resources/video1.mp4"));
            this.video = new Video(fileInputStream.readAllBytes(), ffmpegBin);
            VideoDecoder decoder = new VideoDecoder(video, ffmpegBin, true);
            Assertions.assertEquals(video.getFrameCount(), decoder.toPictureByteArray(2).size());
        } catch (IOException e) {
            Assertions.fail("Couldn't decode Video");
        }
    }

    @Test
    /**
     * Try to decode valid Video with no Audio Stream
     */
    public void decodeToPictureNoAudio(){
        try {
            FileInputStream fileInputStream = new FileInputStream(new File("src/test/java/steganography/video/resources/video2.MP4"));
            this.video = new Video(fileInputStream.readAllBytes(), ffmpegBin);
            VideoDecoder decoder = new VideoDecoder(video, ffmpegBin, true);
            Assertions.assertEquals(video.getFrameCount(), decoder.toPictureByteArray(2).size());
        } catch (IOException e) {
            e.printStackTrace();
            Assertions.fail("Couldn't decode Video");
        }
    }

    @Test
    /**
     * Try to decode audio
     */
    public void decodeAudioToPicture(){
        try {
            FileInputStream fileInputStream = new FileInputStream(new File("src/test/java/steganography/video/resources/audio.mp3"));
            Assertions.assertThrows(UnsupportedEncodingException.class, () -> new Video(fileInputStream.readAllBytes(), ffmpegBin));
        } catch (IOException e) {
            Assertions.fail(e.toString());
        }
    }

    @Test
    /**
     * Check if Video is decoded to valid buff images
     */
    public void decodeToPictureValidBufferedImage(){
        try {
            FileInputStream fileInputStream = new FileInputStream(new File("src/test/java/steganography/video/resources/video1.mp4"));
            this.video = new Video(fileInputStream.readAllBytes(), ffmpegBin);
            VideoDecoder decoder = new VideoDecoder(video, ffmpegBin, true);
            ImageIO.setUseCache(false);
            List<byte[]> imageList = decoder.toPictureByteArray(4);

            for (byte[] image: imageList) {
                BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(image));
                Assertions.assertNotNull(bufferedImage.getData());
            }

        } catch (IOException e) {
            Assertions.fail(e.toString());
        }
    }
}
