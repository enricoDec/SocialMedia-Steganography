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
 * This is a look up class for bit rates used in mp3 files.
 * @author Richard Rudek
 */
class BitRateLookUp {
    /**
     * Tries to find the Bitrate of an MP3 frame.
     * @param mpegVersion MPEG Version of the frame<br/>
     *                    MPEG Version 1 = 1<br/>
     *                    MPEG Version 2 or 2.5 = 2<br/>
     * @param layer Layer of the frame<br/>
     *              Layer 1 = 1<br/>
     *              Layer 2 = 2<br/>
     *              Layer 3 = 3<br/>
     * @param index Bitrate index of the frame (should be between 0 and 14 inclusive)<br/>
     * @return int - Bitrate according to MPEG Version, Layer and Bitrate index of the frame
     * @throws IllegalArgumentException if any of the parameters are invalid
     * @throws NoSuchElementException if there is no value for the given parameters
     */
    static int getValueForBitrate(int mpegVersion, int layer, int index)
            throws IllegalArgumentException, NoSuchElementException {
        if (mpegVersion < 1 || mpegVersion > 2)
            throw new IllegalArgumentException("MPEG version " + mpegVersion + " not supported");

        if (layer < 1 || layer > 3)
            throw new IllegalArgumentException("Layer " + layer + " not supported");

        if (index < 0 || index > 14)
            throw new IllegalArgumentException("Invalid Bitrate index (value was " + index + ")");

        if (index == 0)
            throw new NoSuchElementException("Free Bitrate is not supported (value was " + index + ")");

        int bitrate = -1;

        if (mpegVersion == 1 && layer == 1)
            bitrate = getValueForV1AndL1(index);
        if (mpegVersion == 1 && layer == 2)
            bitrate = getValueForV1AndL2(index);
        if (mpegVersion == 1 && layer == 3)
            bitrate = getValueForV1AndL3(index);
        if (mpegVersion == 2 && layer == 1)
            bitrate = getValueForV2AndL1(index);
        if (mpegVersion == 2 && (layer == 2 || layer == 3))
            bitrate = getValueForV2AndL2OrL3(index);

        if (bitrate == -1)
            throw new NoSuchElementException("Could not resolve Bitrate for MPEG version " + mpegVersion
                    + ", Layer " + layer + " and index " + index + ". This is most likely a bug.");

        return bitrate;
    }

    private static int getValueForV1AndL1(int index) {
        int bitrate = -1;
        switch (index) {
            case 1: bitrate = 32; break;
            case 2: bitrate = 64; break;
            case 3: bitrate = 96; break;
            case 4: bitrate = 128; break;
            case 5: bitrate = 160; break;
            case 6: bitrate = 192; break;
            case 7: bitrate = 224; break;
            case 8: bitrate = 256; break;
            case 9: bitrate = 288; break;
            case 10: bitrate = 320; break;
            case 11: bitrate = 352; break;
            case 12: bitrate = 384; break;
            case 13: bitrate = 416; break;
            case 14: bitrate = 448; break;
        }
        return bitrate;
    }

    private static int getValueForV1AndL2(int index) {
        int bitrate = -1;
        switch (index) {
            case 1: bitrate = 32; break;
            case 2: bitrate = 48; break;
            case 3: bitrate = 56; break;
            case 4: bitrate = 64; break;
            case 5: bitrate = 80; break;
            case 6: bitrate = 96; break;
            case 7: bitrate = 112; break;
            case 8: bitrate = 128; break;
            case 9: bitrate = 160; break;
            case 10: bitrate = 192; break;
            case 11: bitrate = 224; break;
            case 12: bitrate = 256; break;
            case 13: bitrate = 320; break;
            case 14: bitrate = 384; break;
        }
        return bitrate;
    }

    private static int getValueForV1AndL3(int index) {
        int bitrate = -1;
        switch (index) {
            case 1: bitrate = 32; break;
            case 2: bitrate = 40; break;
            case 3: bitrate = 48; break;
            case 4: bitrate = 56; break;
            case 5: bitrate = 64; break;
            case 6: bitrate = 80; break;
            case 7: bitrate = 96; break;
            case 8: bitrate = 112; break;
            case 9: bitrate = 128; break;
            case 10: bitrate = 160; break;
            case 11: bitrate = 192; break;
            case 12: bitrate = 224; break;
            case 13: bitrate = 256; break;
            case 14: bitrate = 320; break;
        }
        return bitrate;
    }

    private static int getValueForV2AndL1(int index) {
        int bitrate = -1;
        switch (index) {
            case 1: bitrate = 32; break;
            case 2: bitrate = 48; break;
            case 3: bitrate = 56; break;
            case 4: bitrate = 64; break;
            case 5: bitrate = 80; break;
            case 6: bitrate = 96; break;
            case 7: bitrate = 112; break;
            case 8: bitrate = 128; break;
            case 9: bitrate = 144; break;
            case 10: bitrate = 160; break;
            case 11: bitrate = 176; break;
            case 12: bitrate = 192; break;
            case 13: bitrate = 224; break;
            case 14: bitrate = 256; break;
        }
        return bitrate;
    }

    private static int getValueForV2AndL2OrL3(int index) {
        int bitrate = -1;
        switch (index) {
            case 1: bitrate = 8; break;
            case 2: bitrate = 16; break;
            case 3: bitrate = 24; break;
            case 4: bitrate = 32; break;
            case 5: bitrate = 40; break;
            case 6: bitrate = 48; break;
            case 7: bitrate = 56; break;
            case 8: bitrate = 64; break;
            case 9: bitrate = 80; break;
            case 10: bitrate = 96; break;
            case 11: bitrate = 112; break;
            case 12: bitrate = 128; break;
            case 13: bitrate = 144; break;
            case 14: bitrate = 160; break;
        }
        return bitrate;
    }
}
