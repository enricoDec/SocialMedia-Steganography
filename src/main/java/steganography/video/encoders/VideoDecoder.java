/*
 * Copyright (c) 2020
 * Contributed by Enrico de Chadarevian
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

package steganography.video.encoders;

import com.github.kokorin.jaffree.StreamType;
import com.github.kokorin.jaffree.ffmpeg.*;
import steganography.video.Video;

import javax.imageio.ImageIO;
import java.io.*;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author : Enrico Gamil Toros de Chadarevian
 * Project name : ProjektStudiumSteganography
 * @version : 1.0
 * @since : 23-11-2020
 **/
public class VideoDecoder implements IDecoder {
    private final File ffmpegBin;
    private final byte[] videoByteArray;
    private final List<Long> ptsList = new ArrayList<>();
    private final boolean logging;
    Video video;

    public VideoDecoder(Video video, File ffmpegBin, boolean logging) {
        this.ffmpegBin = ffmpegBin;
        this.videoByteArray = video.getVideoByteArray();
        this.logging = logging;
        this.video = video;
    }

    /**
     * Decode a Video into a list of single Pictures
     *
     * @return list of pictures
     * @throws IllegalArgumentException IllegalArgumentException
     * @throws IOException              IOException
     */
    public List<byte[]> toPictureByteArray(int nThread) throws IllegalArgumentException, IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(videoByteArray);

        //Temp file to save muxed audio channel
        File soundFile = File.createTempFile("VideoSteganography-", ".mp3");
        soundFile.deleteOnExit();
        SeekableByteChannel sbc = Files.newByteChannel(soundFile.toPath(), StandardOpenOption.WRITE);

        //Executor service that will run FrameConsume consume()
        ExecutorService taskExecutor = Executors.newFixedThreadPool(nThread);
        //List of callable objects that will return a map of byte[] of the list
        List<Callable<Map<byte[], Long>>> taskList = new ArrayList<>();

        // for each frame FrameConsumer calls consume(), we create a callable Object each time
        FrameConsumer frameConsumer = new FrameConsumer() {
            int frameNumber = 1;

            @Override
            public void consumeStreams(List<Stream> streams) {
            }

            @Override
            public void consume(Frame frame) {
                taskList.add(() -> {
                            Map<byte[], Long> map = null;
                            // End of Stream
                            if (frame == null) {
                                return null;
                            }
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            try {
                                ImageIO.write(frame.getImage(), "png", byteArrayOutputStream);
                                map = Collections.singletonMap(byteArrayOutputStream.toByteArray(), frame.getPts());
                                if (logging && frameNumber % 2 == 0) {
                                    System.out.println("(Video -> Picture): (" + frameNumber + "/" + video.getFrameCount() + ")");
                                }
                                frameNumber++;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return map;
                        }
                );
            }

        };


        // Video with Audio Stream
        if (video.hasAudioStream()) {
            FFmpeg.atPath(ffmpegBin.toPath())
                    .addInput(PipeInput.pumpFrom(inputStream))
                    //Create Audio File
                    .addOutput(ChannelOutput
                            .toChannel(soundFile.getName(), sbc)
                            .disableStream(StreamType.VIDEO)
                            .disableStream(StreamType.DATA)
                            .disableStream(StreamType.SUBTITLE)
                    )
                    .addOutput(FrameOutput
                            .withConsumer(frameConsumer)
                            .setFrameCount(StreamType.VIDEO, video.getFrameCount())
                            .setFrameRate(video.getFrameRate())
                            .disableStream(StreamType.SUBTITLE)
                            .disableStream(StreamType.DATA)
                            .disableStream(StreamType.AUDIO)
                    )
                    .setOverwriteOutput(true)
                    .execute();
        } else {
            //Video with no Audio Stream
            FFmpeg.atPath(ffmpegBin.toPath())
                    .addInput(PipeInput.pumpFrom(inputStream))
                    .addOutput(FrameOutput
                            .withConsumer(frameConsumer)
                            .setFrameCount(StreamType.VIDEO, video.getFrameCount())
                            .setFrameRate(video.getFrameRate())
                            .disableStream(StreamType.SUBTITLE)
                            .disableStream(StreamType.DATA)
                            .disableStream(StreamType.AUDIO)
                    )
                    .setOverwriteOutput(true)
                    .execute();
        }

        //Execute all tasks
        List<byte[]> decodedImages = new ArrayList<>();
        List<Future<Map<byte[], Long>>> futureList;
        try {
            futureList = taskExecutor.invokeAll(taskList);
            //Wait for all results
            for (Future<Map<byte[], Long>> result : futureList) {
                if (result.get() != null) {
                    result.get().forEach(
                            (k, v) -> {
                                decodedImages.add(k);
                                ptsList.add(v);
                            }
                    );
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            taskExecutor.shutdown();
        }
        video.setAudioFile(soundFile);
        video.setPtsList(this.ptsList);

        return decodedImages;
    }
}
