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

import java.util.NoSuchElementException;

/**
 * This is a look up class for sampling rates used in mp3 files.
 * @author Richard Rudek
 */
class SamplingRateLookUp {

    /**
     * Tries to find the Sampling rate of an MP3 frame.
     * @param mpegVersion MPEG Version of the frame<br/>
     *                    MPEG Version 1 = 1f<br/>
     *                    MPEG Version 2 = 2f<br/>
     *                    MPEG Version 2.5 = 2.5f
     * @param index Sampling rate index of the frame (should be between 0 and 2 inclusive)
     * @return int - Sampling rate according to MPEG Version and Sampling rate index of the frame
     * @throws IllegalArgumentException if any of the parameters are invalid
     * @throws NoSuchElementException if there is no value for the given parameters
     */
    static int getValueForSamplingRate(float mpegVersion, int index)
            throws IllegalArgumentException, NoSuchElementException {
        // check if parameters are valid
        if (mpegVersion != 1f && mpegVersion != 2f && mpegVersion != 2.5f)
            throw new IllegalArgumentException("MPEG version " + mpegVersion + " not supported");

        if (index < 0 || index > 2)
            throw new IllegalArgumentException("Invalid Sampling rate index (value was " + index + ")");

        // find sampling rate for given mpeg version
        int samplingRate = -1;

        if (mpegVersion == 1f)
            samplingRate = getValueForV1(index);

        if (mpegVersion == 2f)
            samplingRate = getValueForV2(index);

        if (mpegVersion == 2.5f)
            samplingRate = getValueForV2Point5(index);

        // if sampling rate  is still not assigned, something went wrong
        if (samplingRate == -1)
            throw new NoSuchElementException("Could not resolve Sampling rate for MPEG version " + mpegVersion
                    + " and index " + index + ". This is most likely a bug.");

        return samplingRate;
    }

    private static int getValueForV1(int index) {
        int samplingRate = -1;
        switch (index) {
            case 0: samplingRate = 44100; break;
            case 1: samplingRate = 48000; break;
            case 2: samplingRate = 32000; break;
        }
        return samplingRate;
    }

    private static int getValueForV2(int index) {
        int samplingRate = -1;
        switch (index) {
            case 0: samplingRate = 22050; break;
            case 1: samplingRate = 24000; break;
            case 2: samplingRate = 16000; break;
        }
        return samplingRate;
    }

    private static int getValueForV2Point5(int index) {
        int samplingRate = -1;
        switch (index) {
            case 0: samplingRate = 11025; break;
            case 1: samplingRate = 12000; break;
            case 2: samplingRate = 8000; break;
        }
        return samplingRate;
    }
}
