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

package steganography.audio.util;

/**
 * This class provides methods that make working with bits and bytes easier.
 * @author Richard Rudek
 */
public class BitByteConverter {

    /**
     * Converts a byte array to a 2d-array containing the bit representation of each byte.
     * @param bytes Byte array to convert into bits
     * @return Byte array:<br/>
     *         First dim = number of byte<br/>
     *         Second dim = the 8 bits for that byte
     */
    public static byte[][] byteToBits(byte[] bytes) {
        byte[][] bitArray = new byte[bytes.length][8];
        for (int byteNo = 0; byteNo < bytes.length; byteNo++) {
            bitArray[byteNo] = byteToBits(bytes[byteNo]);
        }
        return bitArray;
    }

    /**
     * Converts a byte into an array with length 8 that contains the bit representation of that byte.
     * @param byteToConvert Byte to convert into bits
     * @return Byte array - the 8 bits for that byte
     */
    public static byte[] byteToBits(byte byteToConvert) {
        byte[] bitArray = new byte[8];
        int counter = 7;
        for (int bitNo = 0; bitNo < 7; bitNo++) {
            bitArray[counter--] = (byte) (((byteToConvert & (byte) (1 << bitNo)) > 0) ? 1 : 0);
        }
        // set first bit manually because java only knows 2's complement,
        // otherwise the first bit would always be set to 0
        bitArray[0] = (byte) (byteToConvert > 0 ? 0 : 1);
        return bitArray;
    }

    /**
     * Converts 8 bits into a the byte they are representing.
     * @param bits Array of 8 bits to convert into a byte
     * @return byte - Byte representation of the bits
     * @throws IllegalArgumentException If the length of the array is not 8
     * or an element of the array is neither 0 nor 1
     */
    public static byte bitsToByte(byte[] bits) {
        if (bits.length != 8)
            throw new IllegalArgumentException("Can't convert into byte because there are not 8 bits");

        for (int i = 0; i < 8; i++) {
            if (bits[i] != 0 && bits[i] != 1) {
                throw new IllegalArgumentException("Can't convert into byte because bits["
                        + i + "] is neither 0 nor 1");
            }
        }

        int counter = 0;
        byte newByte = 0;
        for (int i = 7; i >= 0; i--) {
            if (bits[i] == 1)
                newByte += Math.pow(2, counter);
            counter++;
        }
        if (bits[0] == 1)
            newByte -= 128;

        return newByte;
    }
}
