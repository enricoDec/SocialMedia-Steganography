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

import com.github.kokorin.jaffree.StreamType;
import com.github.kokorin.jaffree.ffmpeg.*;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : Enrico Gamil Toros de Chadarevian
 * Project name : ProjektStudiumSteganography
 * @version : 1.0
 * @since : 23-11-2020
 **/
public class VideoDecoder {
    private final File ffmpegBin;
    private String soundPath;
    private final byte[] videoByteArray;
    private final List<Long> ptsList = new ArrayList<>();
    private final boolean logging;
    Video video;

    public VideoDecoder(byte[] videoByteArray, Video video, File ffmpegBin, boolean logging) {
        this.ffmpegBin = ffmpegBin;
        this.videoByteArray = videoByteArray;
        this.logging = logging;
        this.video = video;
    }

    public List<byte[]> toPictureByteArray() throws IllegalArgumentException {
        List<byte[]> imageList = new ArrayList<>();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(videoByteArray);


        FFmpeg.atPath(ffmpegBin.toPath())
                .addInput(PipeInput.pumpFrom(inputStream))
                .addOutput(FrameOutput
                        .withConsumer(
                                new FrameConsumer() {
                                    int frameNumber = 1;

                                    @Override
                                    public void consumeStreams(List<Stream> streams) {
                                        // TODO: Consume Audio (Demux)
                                    }

                                    @Override
                                    public void consume(Frame frame) {
                                        // End of Stream
                                        if (frame == null) {
                                            return;
                                        }
                                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                        try {
                                            ImageIO.write(frame.getImage(), "png", byteArrayOutputStream);
                                            ptsList.add(frame.getPts());
                                            if (logging && frameNumber % 2 == 0)
                                                System.out.println("Decoded Frame (" + frameNumber + "/" + video.getFrameCount() + ")");
                                            frameNumber++;
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        imageList.add(byteArrayOutputStream.toByteArray());
                                    }
                                }
                        )
                        .disableStream(StreamType.SUBTITLE)
                        .disableStream(StreamType.DATA)
                        .disableStream(StreamType.AUDIO)
                )
                .execute();
        return imageList;
    }

    public List<Long> getPtsList() {
        return ptsList;
    }
}
