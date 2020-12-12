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

package steganography.video;

import com.github.kokorin.jaffree.ffmpeg.*;
import steganography.util.ByteArrayUtils;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.List;

/**
 * @author : Enrico Gamil Toros de Chadarevian
 * Project name : ProjektStudiumSteganography
 * @version : 1.0
 * @since : 23-11-2020
 **/
public class VideoEncoder implements IEncoder {
    private final File ffmpegBin;
    private final boolean logging;
    Video video;

    public VideoEncoder(Video video, File ffmpegBin, boolean logging) {
        this.logging = logging;
        this.ffmpegBin = ffmpegBin;
        this.video = video;
    }

    /**
     * Encode a list of images to a video
     *
     * @param stegImages list of images to be encoded
     * @return Encoded Video as .avi
     * @throws IOException IOException
     */
    public byte[] imagesToVideo(List<byte[]> stegImages) throws IOException {
        File tempFile = File.createTempFile("VideoSteganography-", ".avi");
        tempFile.deleteOnExit();
        SeekableByteChannel sbc = Files.newByteChannel(tempFile.toPath(), StandardOpenOption.WRITE);

        FrameProducer frameProducer = new FrameProducer() {
            int frameCounter = 0;

            @Override
            public List<Stream> produceStreams() {
                return Collections.singletonList(new Stream()
                        .setType(Stream.Type.VIDEO)
                        .setTimebase(video.getTimebase())
                        .setResolution(video.getFrameWidth(), video.getFrameHeight())
                );
            }

            @Override
            public Frame produce() {
                if (frameCounter + 1 > stegImages.size()) {
                    //All frames consumed
                    return null;
                }
                if (logging)
                    System.out.println("(Pictures -> Video): (" + frameCounter + "/" + stegImages.size() + ")");

                Frame videoFrame = null;
                try {
                    videoFrame = new Frame(0, video.getPtsList().get(frameCounter),
                            ImageIO.read(new ByteArrayInputStream(stegImages.get(frameCounter))));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                frameCounter++;

                return videoFrame;
            }
        };

        //Create Video with Audio if available
        if (video.hasAudioStream()) {
            FFmpeg.atPath(ffmpegBin.toPath())
                    .addInput(FrameInput.withProducer(frameProducer)
                            .setFrameRate(video.getFrameRate()))
                    //Audio Stream
                    .addInput(PipeInput.pumpFrom(new FileInputStream(video.getAudioFile())))
                    .setOverwriteOutput(true)
                    .addOutput(ChannelOutput.toChannel(tempFile.getName(), sbc))
                    .addArguments("-c:v", "png")
                    .execute();
        } else {
            //Create Video with no Audio
            FFmpeg.atPath(ffmpegBin.toPath())
                    .addInput(FrameInput.withProducer(frameProducer)
                            .setFrameRate(video.getFrameRate()))
                    .setOverwriteOutput(true)
                    .addOutput(ChannelOutput.toChannel(tempFile.getName(), sbc))
                    .addArguments("-c:v", "png")
                    .execute();
        }

        return ByteArrayUtils.read(tempFile);
    }
}
