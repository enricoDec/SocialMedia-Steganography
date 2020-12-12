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
 * This class can read from and write to the least significant bits of a byte array.
 * @author Richard Rudek
 */
public class LSBChanger {

    /**
     * Takes the byte array message and writes the message into the least significant bits of the byte array carrier.
     * @param carrier bytes to write into
     * @param message bytes to encode
     * @return The given byte array with the message encoded into the least significant bits
     */
    public static byte[] writeToByteArray(byte[] carrier, byte[] message) {
        byte[][] messageBytesAndBits = BitByteConverter.byteToBits(message);
        int counter = 0;
        for (byte[] messageBytes : messageBytesAndBits) {
            for (byte messageBit : messageBytes) {
                byte[] currentBits = BitByteConverter.byteToBits(carrier[counter]);
                if (currentBits[7] != messageBit)
                    currentBits[7] = messageBit;
                carrier[counter] = BitByteConverter.bitsToByte(currentBits);
                counter++;
            }
        }
        return carrier;
    }

    /**
     * Reads a message from the byte array encodedCarrier starting at the byte with the number startingPosition
     * until length bytes have been read.
     * @param encodedCarrier byte array containing a message
     * @param startingPosition starting position to start the reading process
     * @param length amount of bytes in the message
     * @return The message as a byte array
     */
    public static byte[] readFromByteArray(byte[] encodedCarrier, int startingPosition, int length) {
        byte[] message = new byte[length];
        int messageCounter = 0;
        byte[] tempByte = new byte[8];
        int tempByteCounter = 0;
        for (int i = startingPosition; i < length * 8; i++) {
            byte[] currentBits = BitByteConverter.byteToBits(encodedCarrier[i]);
            tempByte[tempByteCounter++] = currentBits[7];
            if (tempByteCounter == 8) {
                message[messageCounter++] = BitByteConverter.bitsToByte(tempByte);
                tempByteCounter = 0;
            }
        }
        return message;
    }
}
