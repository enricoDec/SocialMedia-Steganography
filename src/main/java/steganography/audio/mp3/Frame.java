/*
 * Copyright (c) 2020
 * Contributed by Richard Rudek
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

package steganography.audio.mp3;

/**
 * This class holds information about a frame in an MP3 file.
 * @author Richard Rudek
 */
class Frame {
    /**
     * The length of an MP3 header in bytes
     */
    public static final int HEADER_LENGTH = 4;
    /**
     * The length of the checksum following an MP3 header in bytes
     */
    public static final int CHECKSUM_LENGTH = 2;
    /**
     * The number of the byte in an MP3 file where this frame begins
     */
    private int startingByte;
    /**
     * The length of the entire frame including the header in bytes
     */
    private int length;
    private boolean crcProtected;
    private int bitrate;
    private int samplingRate;
    private boolean padded;
    private boolean valid;


    Frame() {
        this.startingByte = -1;
        this.length = -1;
        this.crcProtected = false;
        this.bitrate = -1;
        this.samplingRate = -1;
        this.padded = false;
        this.valid = false;
    }


    int getStartingByte() {
        return startingByte;
    }

    void setStartingByte(int startingByte) {
        this.startingByte = startingByte;
    }

    int getLength() {
        return length;
    }

    void setLength(int length) {
        this.length = length;
    }

    boolean isCrcProtected() {
        return crcProtected;
    }

    void setCrcProtected(boolean crc) {
        this.crcProtected = crc;
    }

    int getBitrate() {
        return bitrate;
    }

    void setBitrate(int bitrate) {
        this.bitrate = bitrate;
    }

    int getSamplingRate() {
        return samplingRate;
    }

    void setSamplingRate(int samplingRate) {
        this.samplingRate = samplingRate;
    }

    boolean isPadded() {
        return padded;
    }

    void setPadded(boolean padded) {
        this.padded = padded;
    }

    boolean isValid() {
        return valid;
    }

    void setValid(boolean valid) {
        this.valid = valid;
    }
}
