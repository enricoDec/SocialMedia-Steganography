/*
 * Copyright (c) 2020
 * Contributed by Selina Wernike
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

/**
 * @author Selina Wernike
 * This Class transforms a byte into a hex string and vice versa
 */
package steganography.util;

public class ByteHex {

    public static byte hexToByte(String hex) {
        int firstDigit = toDigit(hex.charAt(0));
        int secondDigit = toDigit(hex.charAt(1));
        return (byte) ((firstDigit << 4) + secondDigit);
    }

    private static int toDigit(char hexChar) {
        int digit = Character.digit(hexChar, 16);
        if(digit == -1) {
            throw new IllegalArgumentException("Invalid Hexadecimal Character: "+ hexChar);
        }
        return digit;
    }

    public static String byteToHex(byte b) {
        char[] hexDigits = new char[2];
        hexDigits[0] = Character.forDigit((b >> 4) & 0xF, 16);
        hexDigits[1] = Character.forDigit((b & 0xF),16);
        return new String(hexDigits);
    }
}
