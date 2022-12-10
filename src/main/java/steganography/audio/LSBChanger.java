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

package steganography.audio;

import steganography.audio.exception.AudioCapacityException;
import steganography.audio.overlays.AudioOverlay;
import steganography.exceptions.UnknownStegFormatException;

import java.util.NoSuchElementException;

/**
 * This class can read from and write to the least significant bits of a byte array.
 *
 * @author Richard Rudek
 */
public class LSBChanger {

    private final AudioOverlay overlay;

    public LSBChanger(AudioOverlay overlay) {
        this.overlay = overlay;
    }

    /**
     * Takes the byte array message and writes the message into the least significant bits of the byte array carrier
     * using the overlay given in the constructor.<br>
     * If the message is null, nothing will change.
     *
     * @param message bytes to encode
     * @return the given byte array with the message encoded into the least significant bits
     * @throws AudioCapacityException if the message does not fit into the overlays bytes
     */
    public byte[] encode(byte[] message) throws AudioCapacityException {
        if (message != null) {
            if (message.length * 8 > this.overlay.available())
                throw new AudioCapacityException("Message (requires " + message.length +
                        " bytes) does not fit into overlay (" + (this.overlay.available() / 8) + " bytes available).");

            byte[][] messageBytesAndBits = BitByteConverter.byteToBits(message);

            try {
                for (byte[] messageBytes : messageBytesAndBits) {
                    for (byte messageBit : messageBytes) {
                        // get the next byte
                        byte currentByte = this.overlay.next();
                        // get the bit representation and change the last bit
                        byte[] currentBits = BitByteConverter.byteToBits(currentByte);
                        if (currentBits[7] != messageBit)
                            currentBits[7] = messageBit;
                        // set the changed byte in this overlay
                        this.overlay.setByte(BitByteConverter.bitsToByte(currentBits));
                    }
                }
            } catch (NoSuchElementException e) {
                throw new AudioCapacityException(e.getMessage());
            }
        }

        return this.overlay.getBytes();
    }

    /**
     * Reads a message from this overlays byte array until length bytes have been read.
     *
     * @param length amount of bytes in the message
     * @return the message as a byte array
     * @throws UnknownStegFormatException if there are not enough bytes to read the message from
     * @throws IllegalArgumentException   if the length is less than 1
     */
    public byte[] decode(int length) throws UnknownStegFormatException {
        if (length < 1)
            throw new IllegalArgumentException("Can't read message of length less than 1 (length was " + length + ").");

        if (length * 8 > this.overlay.available()) {
            throw new UnknownStegFormatException("Message could not be read from byte array because only " +
                    (this.overlay.available() / 8) + " bytes are available in the MP3 file, but " + length +
                    " bytes are required to get the whole message.");
        }

        byte[] message = new byte[length];
        byte[] tempByte = new byte[8];
        int tempByteCounter = 0;
        int messageCounter = 0;

        for (int i = 0; i < length * 8; i++) {
            byte[] currentBits = BitByteConverter.byteToBits(this.overlay.next());
            tempByte[tempByteCounter++] = currentBits[7];
            if (tempByteCounter == 8) {
                message[messageCounter++] = BitByteConverter.bitsToByte(tempByte);
                tempByteCounter = 0;
            }
        }

        return message;
    }
}
