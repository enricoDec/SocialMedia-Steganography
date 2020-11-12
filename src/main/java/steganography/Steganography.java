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

package steganography;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public interface Steganography {

    /**
     * Takes some data and conceals it in a carrier (container used to hide data).
     *
     * @param carrier carrier used to hide the data
     * @param payload data to hide
     * @return steganographic data
     */
    byte[] encode(byte[] carrier, byte[] payload) throws IOException;

    /**
     * conceals payload in carrier (container used to hide payload)
     * using the given seed
     *
     * @param carrier carrier used to hide the data
     * @param payload data to hide
     * @param seed affects the resulting steganographic data (similar to a password)
     * @return steganographic data
     */
    byte[] encode(byte[] carrier, byte[] payload, long seed) throws IOException;

    /**
     * Retrieves hidden message from a steganographic file
     *
     * @param steganographicData Data containing data to extract
     * @return retrieved data
     */
    byte[] decode(byte[] steganographicData) throws IOException;

    /**
     * Retrieves hidden message from a steganographic file
     *
     * @param steganographicData Data containing data to extract
     * @param seed seed that was used to encode the given stenographicData
     * @return hidden message
     */
    byte[] decode(byte[] steganographicData, long seed) throws IOException;

    /**
     * Tests if the given data has a hidden message encoded in it
     * @param data data to test
     * @return true if the given data has a hidden message encoded in it
     */
    boolean isSteganographicData(byte[] data) throws IOException;
}
