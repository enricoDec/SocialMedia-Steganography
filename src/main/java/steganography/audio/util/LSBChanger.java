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

import steganography.audio.mp3.overlays.AudioOverlay;

import java.util.NoSuchElementException;

/**
 * This class can read from and write to the least significant bits of a byte array.
 * @author Richard Rudek
 */
public class LSBChanger {

    private final AudioOverlay OVERLAY;

    public LSBChanger(AudioOverlay overlay) {
        this.OVERLAY = overlay;
    }

    /**
     * Takes the byte array message and writes the message into the least significant bits of the byte array carrier
     * using the overlay given in the constructor.
     * @param message bytes to encode
     * @return the given byte array with the message encoded into the least significant bits
     * @throws IllegalArgumentException if the message does not fit into the overlays bytes
     */
    public byte[] encode(byte[] message) throws IllegalArgumentException {
        byte[][] messageBytesAndBits = BitByteConverter.byteToBits(message);
        if (messageBytesAndBits.length > this.OVERLAY.available())
            throw new IllegalArgumentException("Message (requires " + messageBytesAndBits.length +
                    " bytes) does not fit into overlay (" + this.OVERLAY.available() + " bytes available).");

        for (byte[] messageBytes : messageBytesAndBits) {
            for (byte messageBit : messageBytes) {
                // get the next byte
                byte currentByte = this.OVERLAY.next();
                // get the bit representation and change the last bit
                byte[] currentBits = BitByteConverter.byteToBits(currentByte);
                if (currentBits[7] != messageBit)
                    currentBits[7] = messageBit;
                // set the changed byte in this overlay
                this.OVERLAY.setByte(BitByteConverter.bitsToByte(currentBits));
            }
        }
        return this.OVERLAY.getBytes();
    }

    /**
     * Reads a message from this overlays byte array until length bytes have been read.
     * @param length amount of bytes in the message
     * @return the message as a byte array
     * @throws IllegalArgumentException if there are not enough bytes to read the message from
     */
    public byte[] decode(int length) throws IllegalArgumentException {
        if (length * 8 > this.OVERLAY.available())
            throw new IllegalArgumentException("Message could not be read from byte array because only " +
                    (this.OVERLAY.available() / 8) + " bytes are available in the MP3 file, but " + length +
                    " bytes are required to get the whole message.");

        byte[] message = new byte[length];
        byte[] tempByte = new byte[8];
        int tempByteCounter = 0;
        int messageCounter = 0;
        for (int i = 0; i < length * 8; i++) {
            byte[] currentBits = BitByteConverter.byteToBits(this.OVERLAY.next());
            tempByte[tempByteCounter++] = currentBits[7];
            if (tempByteCounter == 8) {
                message[messageCounter++] = BitByteConverter.bitsToByte(tempByte);
                tempByteCounter = 0;
            }
        }
        return message;
    }
}
