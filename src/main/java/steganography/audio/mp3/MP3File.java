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

import steganography.audio.BitByteConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * This class represents an MP3 file.
 * @author Richard Rudek
 */
public class MP3File {
    /**
     * The byte array containing an MP3 file
     */
    private final byte[] mp3Bytes;

    /**
     * The number of frames
     */
    private int frameCount = -1;

    /**
     * The list containing every Frame in this MP3File
     */
    private List<Frame> frames = null;


    /**
     * This constructs an MP3File object with the given bytes.
     * @param mp3Bytes the byte array containing the bytes of an MP3 file
     */
    public MP3File(byte[] mp3Bytes) {
        this.mp3Bytes = mp3Bytes;
    }

    /**
     * Returns the byte array this class was given.
     */
    public byte[] getMP3Bytes() {
        return this.mp3Bytes;
    }

    /**
     * Returns the number of frames in this MP3 file.
     * @return int - Number of frames,<br/>
     *         -1 if there was no prior search for headers
     */
    public int getFrameCount() {
        return frameCount;
    }

    /**
     * Returns the information of each frame in this MP3 file. The header is included in each frame.
     * @return - null, if findAllFrames() has not been called or there are no frames<br/>
     *         - List containing Frames
     */
    public List<Frame> getFrames() {
        return this.frames;
    }

    /**
     * Returns the positions of bytes that are safe to modify.
     * @return List of Integers - all positions of modifiable bytes in this MP3 file
     * @throws IllegalArgumentException if there are no frames.<br/>
     *                                  This can happen when findAllFrames has not been called prior to this method
     *                                  or this file is not an MP3 file.
     */
    public List<Integer> getModifiablePositions() throws IllegalArgumentException {
        if (this.frames == null)
            throw new IllegalArgumentException("There are no frames. Therefore, there are no modifiable bytes. " +
                    "Make sure findAllFrames() has been called!");

        List<Integer> result = new ArrayList<>();

        int frameCounter = 0;
        Frame currentFrame = this.frames.get(frameCounter);

        // loop through mp3 bytes starting at first frame to find data bytes
        for (int i = currentFrame.getStartingByte(); i < this.mp3Bytes.length; i++) {
            // get the next frame
            currentFrame = this.frames.get(frameCounter);

            // skip header (and checksum)
            if (i == currentFrame.getStartingByte()) {
                i += Frame.HEADER_LENGTH;
                i += currentFrame.isCrcProtected() ? Frame.CHECKSUM_LENGTH : 0;
            }

            // add the data byte to the list of modifiable bytes
            result.add(i);

            // look at the next frame when loop has gone through every data byte of the current frame
            if (i == (currentFrame.getStartingByte() + currentFrame.getLength() - 1))
                frameCounter++;

            // stop if loop went through every frame
            if (frameCounter == this.frameCount)
                break;
        }
        System.out.println("[INFO] Found " + result.size() + " modifiable Positions in the MP3 byte array.");
        return result;
    }

    /**
     * Attempts to find {@link Frame frames} by searching for MP3 frame headers and
     * saves their information in this MP3File.
     * @return true, if frames have been found<br/>
     *         false, if there are none
     */
    public boolean findAllFrames() {
        System.out.println("[INFO] Starting the search for the frames in the MP3 byte array.");
        if (this.frames == null) {
            this.frames = new ArrayList<>();
            this.frameCount = findFrames();
        }
        System.out.println(this.frameCount + " frames found.");

        if (this.frameCount == 0) {
            this.frames = null;
        }

        return this.frames != null;
    }

    /**
     * Searches the MP3 byte array for its headers and saves them.
     * @return int - Number of headers found
     */
    private int findFrames() {
        int lastPosition = 0;
        int framesFound = 0;

        while (lastPosition != -1) {
            try {
                // find the next frame
                Frame frame = findNextFrame(lastPosition);

                // if there is another frame, save the previous
                // it can only be saved now, because the length has to be determined
                this.frames.add(frame);

                // set counting variables accordingly
                framesFound++;
                lastPosition += frame.getLength();
            } catch (NoSuchElementException e) {
                // set counting variables accordingly
                lastPosition = -1;
            }
        }

        return framesFound;
    }

    /**
     * Find the next frame starting at byte searchStart.
     * @param searchStart Position in the byte array from which the next frame is searched
     * @return {@link Frame} - Object containing information about the header and frame
     * @throws NoSuchElementException if there is no header after searchStart
     */
    private Frame findNextFrame(int searchStart) throws NoSuchElementException {
        Frame frame = new Frame();

        for (int i = searchStart; i < this.mp3Bytes.length; i++) {
            // find a byte were all bits are set to 1
            // in java: 1111 1111 = -1
            if (this.mp3Bytes[i] != -1) {
                continue;
            }

            // frame candidate found, check next bits
            byte[] bitsOfNextByte = BitByteConverter.byteToBits(this.mp3Bytes[i+1]);
            if (bitsOfNextByte[0] == 0 || bitsOfNextByte[1] == 0 || bitsOfNextByte[2] == 0) {
                // this and next bytes are no frame, so skip loop
                i++;
                continue;
            }

            // frame (probably) found
            if (frame.getStartingByte() == -1) {
                // starting position is not set --> set it
                frame.setStartingByte(i);
                continue;
            } else {
                // starting position has been set --> set length
                frame.setLength(i - frame.getStartingByte());
            }

            try {
                if (!validateFrame(frame).isValid()) {
                    throw new IllegalArgumentException("Not valid");
                }
                // frame found, break loop
                break;
            } catch (IllegalArgumentException e) {
                // frame is not valid, reset starting position
                frame.setStartingByte(i);
            }
        }

        if (frame.getStartingByte() == -1 || frame.getLength() == -1) {
            // no frame found --> throw exception
            throw new NoSuchElementException("Could not find a frame after position " + searchStart);
        }
        return frame;
    }

    /**
     * Checks if the given frame is valid.
     * If it is, this method corrects the frames fields
     * @param frame MP3 frame to validate
     * @return {@link Frame} - The given Frame with adjusted fields
     * @throws IllegalArgumentException if the frame is not supported or invalid
     */
    private Frame validateFrame(Frame frame) throws IllegalArgumentException {
        frame.setValid(true);

        // get the bits, that have to be checked
        byte[] bytesToValidate = new byte[6];
        System.arraycopy(this.mp3Bytes, frame.getStartingByte(), bytesToValidate, 0, 6);
        byte[][] bitsToValidate = BitByteConverter.byteToBits(bytesToValidate);

        // --------------------------------------------------------------------------------------------------------- \\
        // ------------------------------------------- VALIDATE HEADER --------------------------------------------- \\
        // --------------------------------------------------------------------------------------------------------- \\

        // check for frame synchronizer
        // 11111111 111***** ******** ********
        // 11x 1 --> first byte = -1 and first 3 bits of second byte are 1
        if (bytesToValidate[0] != -1 || bitsToValidate[1][0] != 1 ||
                bitsToValidate[1][1] != 1 || bitsToValidate[1][2] != 1) {
            frame.setValid(false);
            throw new IllegalArgumentException("Frame sync invalid");
        }

        // MPEG version id
        // ******** ***##*** ******** ********
        // 00 = v2.5
        // 01 = reserved/invalid
        // 10 = v2
        // 11 = v1
        if (bitsToValidate[1][3] == 0 && bitsToValidate[1][4] == 1) {
            frame.setValid(false);
            throw new IllegalArgumentException("MPEG version is invalid");
        }

        float mpegVersion = -1f;
        if (bitsToValidate[1][3] == 0 && bitsToValidate[1][4] == 0) {
            mpegVersion = 2.5f;
        }
        if (bitsToValidate[1][3] == 1 && bitsToValidate[1][4] == 0) {
            mpegVersion = 2f;
        }
        if (bitsToValidate[1][3] == 1 && bitsToValidate[1][4] == 1) {
            mpegVersion = 1f;
        }

        // Layer
        // ******** *****##* ******** ********
        // 00 = reserved/invalid
        // 01 = Layer III
        // 10 = Layer II
        // 11 = Layer I
        if (bitsToValidate[1][5] == 0 && bitsToValidate[1][6] == 0) {
            frame.setValid(false);
            throw new IllegalArgumentException("Layer is invalid");
        }

        int layer = -1;
        if (bitsToValidate[1][5] == 0 && bitsToValidate[1][6] == 1) {
            layer = 3;
        }
        if (bitsToValidate[1][5] == 1 && bitsToValidate[1][6] == 0) {
            layer = 2;
        }
        if (bitsToValidate[1][5] == 1 && bitsToValidate[1][6] == 1) {
            layer = 1;
        }

        // CRC
        // ******** *******# ******** ********
        // 0 = protected by CRC
        // 1 = not protected
        frame.setCrcProtected(bitsToValidate[1][7] == 0);
        if (frame.isCrcProtected()) {
            frame.setValid(false);
            throw new IllegalArgumentException("CRC16 is not supported");
        }

        // bitrate
        // ******** ******** ####**** ********
        // 0000 = free
        // 0001 = 32 kbps
        // 0010 = 40 kbps
        // 0011 = 48 kbps
        // 0100 = 56 kbps
        // 0101 = 64 kbps
        // 0110 = 80 kbps
        // 0111 = 96 kbps
        // 1000 = 112 kbps
        // 1001 = 128 kbps
        // 1010 = 160 kbps
        // 1011 = 192 kbps
        // 1100 = 224 kbps
        // 1101 = 256 kbps
        // 1110 = 320 kbps
        // 1111 = bad/invalid
        int bitrateValue = BitByteConverter.bitsToByte(
                new byte[] {
                        0, 0, 0, 0,
                        bitsToValidate[2][0], bitsToValidate[2][1], bitsToValidate[2][2], bitsToValidate[2][3]
                }
        );

        // sampling rate
        // ******** ******** ****##** ********
        // 00 = 44100 Hz
        // 01 = 48000 Hz
        // 10 = 32000 Hz
        // 11 = reserved/invalid
        int samplingRateValue = BitByteConverter.bitsToByte(
                new byte[] {
                        0, 0, 0, 0, 0, 0, bitsToValidate[2][4], bitsToValidate[2][5]
                }
        );

        // padding
        // ******** ******** ******#* ********
        // 0 = frame is not padded
        // 1 = frame is padded
        frame.setPadded(bitsToValidate[2][6] == 1);

        // private bit
        // ******** ******** *******# ********
        // can be used freely - can be safely ignored

        // channel
        // ******** ******** ******** ##******
        // 00 = stereo
        // 01 = joint stereo
        // 10 = dual
        // 11 = mono
        // can be safely ignored

        // mode extension
        // ******** ******** ******** **##****
        // is only relevant if channel is joint stereo
        //
        // bits | Intensity Stereo  | MS Stereo
        // -----|-------------------|------------
        // 00   | off               | off
        // 01   | on                | off
        // 10   | off               | on
        // 11   | on                | on
        //
        // can be safely ignored

        // copyright
        // ******** ******** ******** ****#***
        // 0 = audio is not copyrighted
        // 1 = audio is copyrighted
        // can be safely ignored

        // original
        // ******** ******** ******** *****#**
        // 0 = copy
        // 1 = original media
        // can be safely ignored

        // emphasis
        // ******** ******** ******** ******##
        // 00 = none
        // 01 = 50/15
        // 10 = reserved/invalid
        // 11 = ccit j.17
        if (bitsToValidate[3][6] == 1 && bitsToValidate[3][7] == 1) {
            frame.setValid(false);
            throw new IllegalArgumentException("Emphasis is invalid");
        }

        // ---------------------------------------------------------------------------------------------------------- \\
        // --------------------------------------------- LOOK UP VALUES --------------------------------------------- \\
        // ---------------------------------------------------------------------------------------------------------- \\

        try {
            // Bitrate
            // cast float to int for convenience
            // (2f and 2.5f will cast to 2 since there is no difference between the versions)
            // and look up bitrate
            frame.setBitrate(BitRateLookUp.getValueForBitrate((int) mpegVersion, layer, bitrateValue));

            // Sampling rate
            frame.setSamplingRate(SamplingRateLookUp.getValueForSamplingRate(mpegVersion, samplingRateValue));
        } catch (IllegalArgumentException | NoSuchElementException e) {
            throw new IllegalArgumentException(e.getMessage());
        }

        // --------------------------------------------------------------------------------------------------------- \\
        // ---------------------------------------- VALIDATE FRAME LENGTH ------------------------------------------ \\
        // --------------------------------------------------------------------------------------------------------- \\

        if (layer == 1) {
            // length = (12 * (bitrate * 1000) / samplingRate + padding) * 4     // includes header
            // bitrate * 1000 because the formula requires bits per ms
            frame.setLength(
                    (12 * (frame.getBitrate() * 1000) / frame.getSamplingRate() + (frame.isPadded() ? 4 : 0)) * 4
            );
        } else if (layer == 2 || layer == 3) {
            // length = (144 * (bitrate * 1000) / samplingRate ) + padding       // includes header
            frame.setLength(
                    144 * (frame.getBitrate() * 1000) / frame.getSamplingRate() + (frame.isPadded() ? 1 : 0)
            );
        } else {
            frame.setValid(false);
            throw new IllegalArgumentException("Layer is invalid." +
                    "This should have been thrown earlier, most likely a bug");
        }

        // check if frame length is valid
        if (frame.getStartingByte() + frame.getLength() > this.mp3Bytes.length) {
            frame.setValid(false);
            throw new IllegalArgumentException("Length is invalid");
        }

        return frame;
    }
}
