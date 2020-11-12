/*
 * Copyright (c) 2020
 * Contributed by Henk-Joas Lubig
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

package steganography.util;

public abstract class BuffImgEncoder {

    protected BufferedImageCoordinateOverlay overlay;

    public BuffImgEncoder(BufferedImageCoordinateOverlay overlay) throws IllegalArgumentException {
        this.overlay = overlay;
    }

    /**
     * Encodes the payload in the sequence of pixels provided by the overlay
     * given to the constructor.
     * @param payload payload or "message" to encode
     */
    public abstract void encode(byte[] payload);

    /**
     * Decodes pixels in the sequence provided by the overlay
     * given to the constructor, according to its algorithm
     * and returns the result as a byte array.
     * Decoding will continue until the byte arrays length is
     * equal to bLength.
     * @param bLength number of bytes to decode
     * @return decoded bytes
     */
    public abstract byte[] decode(int bLength);
}
