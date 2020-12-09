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

import java.util.NoSuchElementException;

/**
 * The implementing classes should be able to returns data bytes of an audio file independent
 * from their order in the file.
 * @author Richard Rudek
 */
public interface AudioOverlay {
    /**
     * Returns the next modifiable byte.
     * @return Byte
     * @throws NoSuchElementException if there are no more modifiable bytes
     */
    byte next() throws NoSuchElementException;

    /**
     * Returns the number of bytes that are available to modify and have not been returned by next().
     * @return number of available bytes
     */
    int available();

    /**
     * Sets the current byte to the given value.
     * @param value the value to set the current byte to
     * @throws NoSuchElementException if setByte() is called before the first call to next(),
     * or if setByte() is called after the last call to next() produced a NoSuchElementException
     */
    void setByte(byte value) throws NoSuchElementException;

    /**
     * Returns the byte array this object holds.
     * @return byte array held by this object
     */
    byte[] getBytes();
}
