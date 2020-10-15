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

public interface Steganography {

    /**
     * Takes some data and conceals it
     * in a carrier (container used to hide data)
     *
     * @param carrier carrier used to hide the data
     * @param payload data to hide
     * @return steganographic data
     */
    public byte[] hide(byte[] carrier, byte[] payload);

    /**
     * Retrieves data from a steganographic file
     *
     * @param steganographicData Data containing data to extract
     * @return retrieved data
     */
    public byte[] unhide(byte[] steganographicData);

    boolean isSteganographicData(byte[] data);
}
