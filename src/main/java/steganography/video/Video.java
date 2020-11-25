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
import com.github.kokorin.jaffree.ffprobe.FFprobe;
import com.github.kokorin.jaffree.ffprobe.FFprobeResult;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

/**
 * @author : Enrico Gamil Toros de Chadarevian
 * Project name : ProjektStudiumSteganography
 * @version : 1.0
 * @since : 24-11-2020
 **/
public class Video {
    private final File ffmpegBin;
    private float frameRate;
    private int frameCount;
    private int frameWidth;
    private int frameHeight;
    private Long timebase;
    private final byte[] videoByteArray;

    public Video(byte[] videoByteArray, File ffmpegBin) {
        this.videoByteArray = videoByteArray;
        this.ffmpegBin = ffmpegBin;
        //analyse the data (check if data has Video)
        analyseVideo();
    }


    /**
     * Checks if given data is a Video and has a Video Stream
     * Sets the frame rate and frame count
     */
    private void analyseVideo() {
        InputStream inputStream = new ByteArrayInputStream(videoByteArray);

        FFprobe ffprobe;
        ffprobe = FFprobe.atPath(ffmpegBin.toPath());

        FFprobeResult result = ffprobe
                .setInput(inputStream)
                .setShowStreams(true)
                .setSelectStreams(StreamType.VIDEO)
                .execute();


        //Check if given Video has a Video Stream or more then one
        if (result.getStreams().isEmpty() || result.getStreams().get(0).getCodecType() != StreamType.VIDEO)
            throw new IllegalArgumentException("No Video Stream in given Video");
        if (result.getStreams().size() != 1)
            throw new IllegalArgumentException("Multiple Video Streams in given Video");

        //Saving some info on frames
        this.frameCount = result.getStreams().get(0).getNbFrames();
        // Some people just hate OOP I guess
        this.frameRate = (result.getStreams().get(0).getAvgFrameRate()).floatValue();
        this.frameWidth = result.getStreams().get(0).getWidth();
        this.frameHeight = result.getStreams().get(0).getHeight();
        //Time base for some reason is returned as String
        String[] strings = (result.getStreams().get(0).getTimeBase()).split("/");
        this.timebase = Long.valueOf(strings[1]);
    }

    public float getFrameRate() {
        return frameRate;
    }

    public int getFrameCount() {
        return frameCount;
    }

    public int getFrameWidth() {
        return frameWidth;
    }

    public int getFrameHeight() {
        return frameHeight;
    }

    public Long getTimebase() {
        return timebase;
    }
}
