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

package steganography.video;

import com.github.kokorin.jaffree.StreamType;
import com.github.kokorin.jaffree.ffprobe.FFprobe;
import com.github.kokorin.jaffree.ffprobe.FFprobeResult;
import com.github.kokorin.jaffree.ffprobe.Stream;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * @author : Enrico Gamil Toros de Chadarevian
 * Project name : ProjektStudiumSteganography
 * @version : 1.0
 * @since : 24-11-2020
 **/
public class Video {
    private final File ffmpegBin;
    private float frameRate;
    private long frameCount;
    private int frameWidth;
    private int frameHeight;
    private Long timebase;
    private final byte[] videoByteArray;
    private File audioFile;
    private String pixelformat;
    private String codec;
    private boolean hasAudioStream = false;

    public Video(byte[] videoByteArray, File ffmpegBin) throws UnsupportedEncodingException {
        this.videoByteArray = videoByteArray;
        this.ffmpegBin = ffmpegBin;
        //analyse the data (check if data has Video)
        if (videoByteArray == null || videoByteArray.length < 10)
            throw new IllegalArgumentException("videoByteArray can't be empty");
        if (!ffmpegBin.exists())
            throw new IllegalArgumentException("FFmpegBin is invalid");
        analyseVideo();
    }


    /**
     * Checks if given data is a Video and has a Video Stream
     * Sets the frame rate and frame count
     */
    private void analyseVideo() throws UnsupportedEncodingException {
        InputStream inputStream = new ByteArrayInputStream(videoByteArray);

        FFprobe ffprobe;
        ffprobe = FFprobe.atPath(ffmpegBin.toPath());

        FFprobeResult result = ffprobe
                .setInput(inputStream)
                .setShowStreams(true)
                .execute();

        //Check if given Video has a Video Stream or more then one
        if (result.getStreams().isEmpty())
            throw new UnsupportedEncodingException("Video has no streams");

        boolean hasVideoStream = false;
        for (Stream stream : result.getStreams()) {
            if (stream.getCodecType() == StreamType.VIDEO)
                hasVideoStream = true;
            if (stream.getCodecType() == StreamType.AUDIO)
                hasAudioStream = true;
        }
        if (!hasVideoStream)
            throw new UnsupportedEncodingException("No Video Stream in given Video");

        //Saving some info on frames
        this.frameCount = result.getStreams().get(0).getNbFrames();
        this.frameRate = (result.getStreams().get(0).getAvgFrameRate()).floatValue();
        this.frameWidth = result.getStreams().get(0).getWidth();
        this.frameHeight = result.getStreams().get(0).getHeight();
        this.codec = result.getStreams().get(0).getCodecName();
        this.pixelformat = result.getStreams().get(0).getPixFmt();
        //Time base for some reason is returned as String
        String[] strings = (result.getStreams().get(0).getTimeBase()).split("/");
        this.timebase = Long.valueOf(strings[1]);
    }

    public float getFrameRate() {
        return frameRate;
    }

    public long getFrameCount() {
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

    public File getAudioFile() {
        return audioFile;
    }

    public byte[] getVideoByteArray() {
        return videoByteArray;
    }

    public void setAudioFile(File audioFile) {
        this.audioFile = audioFile;
    }

    public String getCodec() {
        return codec;
    }

    public String getPixelformat() {
        return pixelformat;
    }

    public boolean hasAudioStream() {
        return hasAudioStream;
    }

    @Override
    public String toString() {
        return "Video{" +
                "ffmpegBin=" + ffmpegBin +
                ", frame rate=" + frameRate +
                ", frame count=" + frameCount +
                ", frame width=" + frameWidth +
                ", frame height=" + frameHeight +
                ", timebase=" + timebase +
                ", pixel format='" + pixelformat + '\'' +
                ", codec='" + codec + '\'' +
                ", has Audio Stream=" + hasAudioStream +
                '}';
    }
}
