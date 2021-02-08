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
import steganography.video.exceptions.UnsupportedVideoTypeException;
import steganography.video.exceptions.VideoNotFoundException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * @author : Enrico Gamil Toros de Chadarevian
 * Project name : ProjektStudiumSteganography
 * @version : 1.0
 * @since : 24-11-2020
 * <p>
 * This class represents a Video
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
    private List<Long> ptsList = null;

    /**
     * Video POJO
     *
     * @param videoByteArray Video as byte array
     * @param ffmpegBin      path to the bin of ffmpeg
     * @throws VideoNotFoundException        If no Video found in the stream
     * @throws UnsupportedVideoTypeException If found Video in the stream has not supported codec
     */
    public Video(byte[] videoByteArray, File ffmpegBin) throws VideoNotFoundException, UnsupportedVideoTypeException {
        this.videoByteArray = videoByteArray;
        this.ffmpegBin = ffmpegBin;
        //analyse the data (check if data has Video)
        if (videoByteArray == null || videoByteArray.length < 10)
            throw new UnsupportedVideoTypeException("Video can't be empty");
        if (!ffmpegBin.exists())
            throw new IllegalArgumentException("FFmpegBin is invalid");
        analyseVideo();
    }


    /**
     * Uses FFProbe to read information about a Video and saves them as attributes of this Object
     */
    private void analyseVideo() throws VideoNotFoundException {
        InputStream inputStream = new ByteArrayInputStream(videoByteArray);

        FFprobe ffprobe;
        ffprobe = FFprobe.atPath(ffmpegBin.toPath());

        FFprobeResult result = ffprobe
                .setInput(inputStream)
                .setShowStreams(true)
                .execute();

        //Check if given Video has no Streams
        if (result.getStreams().isEmpty())
            throw new VideoNotFoundException("Video has no streams");

        //Check if Video stream is present
        boolean hasVideoStream = false;
        for (Stream stream : result.getStreams()) {
            if (stream.getCodecType() == StreamType.VIDEO)
                hasVideoStream = true;
            if (stream.getCodecType() == StreamType.AUDIO)
                hasAudioStream = true;
        }
        if (!hasVideoStream)
            throw new VideoNotFoundException("No Video Stream in given Video");

        //Saving some info on frames
        this.frameCount = result.getStreams().get(0).getNbFrames();
        this.frameRate = (result.getStreams().get(0).getAvgFrameRate()).floatValue();
        this.frameWidth = result.getStreams().get(0).getWidth();
        this.frameHeight = result.getStreams().get(0).getHeight();
        this.codec = result.getStreams().get(0).getCodecName();
        this.pixelformat = result.getStreams().get(0).getPixFmt();
        String[] strings = (result.getStreams().get(0).getTimeBase()).split("/");
        this.timebase = Long.valueOf(strings[1]);
    }

    /**
     * Get the frame rate of the Video
     *
     * @return frame rate as float
     */
    public float getFrameRate() {
        return frameRate;
    }

    /**
     * Get the number of frames in the Video
     *
     * @return number of frames as integer
     */
    public long getFrameCount() {
        return frameCount;
    }

    /**
     * Get the Frame Width
     *
     * @return frame width
     */
    public int getFrameWidth() {
        return frameWidth;
    }

    /**
     * Get the Frame Height
     *
     * @return frame heigth
     */
    public int getFrameHeight() {
        return frameHeight;
    }

    /**
     * Get the timebase of the Video
     *
     * @return timebase
     */
    public Long getTimebase() {
        return timebase;
    }

    /**
     * Get the Audio stream of the Video (only one stream supported)
     *
     * @return get audio of video
     */
    public File getAudioFile() {
        return audioFile;
    }

    /**
     * Set the audio file of the Video
     *
     * @param audioFile File of audio
     */
    public void setAudioFile(File audioFile) {
        this.audioFile = audioFile;
    }

    /**
     * Get the byte array of the Video
     *
     * @return video as byte array
     */
    public byte[] getVideoByteArray() {
        return videoByteArray;
    }

    /**
     * Get the Codec of the Video
     *
     * @return codec
     */
    public String getCodec() {
        return codec;
    }

    /**
     * Get the format of the pixels of the Video as String
     *
     * @return pixel format
     */
    public String getPixelformat() {
        return pixelformat;
    }

    /**
     * Returns true if video has audio stream, else false
     *
     * @return if Video has audio stream
     */
    public boolean hasAudioStream() {
        return hasAudioStream;
    }

    /**
     * Get list of pts
     *
     * @return pts list
     */
    public List<Long> getPtsList() {
        return ptsList;
    }

    /**
     * Set pts
     *
     * @param ptsList pts list
     */
    public void setPtsList(List<Long> ptsList) {
        this.ptsList = ptsList;
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
