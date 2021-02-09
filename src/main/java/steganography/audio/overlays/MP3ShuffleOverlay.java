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

package steganography.audio.overlays;

import steganography.audio.exception.AudioNotFoundException;

import java.util.Collections;
import java.util.Random;

/**
 * This class returns the MP3 data bytes randomized according to a seed.
 * @author Richard Rudek
 */
public class MP3ShuffleOverlay extends MP3SequenceOverlay {

    /**
     * Adds a shuffle overlay to a given byte array containing an MP3 file.
     * This overlay retrieves only the data bytes of the MP3 file and returns the bytes randomized according to a seed.
     * @param bytes byte array containing an MP§ file
     * @param seed seed to shuffle the byte order by
     * @throws AudioNotFoundException if the given byte array is null or does not contain an MP3 file
     */
    public MP3ShuffleOverlay(byte[] bytes, long seed) throws AudioNotFoundException {
        super(bytes, seed);
    }

    /**
     * Shuffles the data byte order randomly according to the seed.
     * @param seed the seed that influences the dataByteOrder
     */
    @Override
    protected void createOverlay(long seed) {
        Random r = new Random(seed);
        Collections.shuffle(this.dataByteOrder, r);
    }
}
