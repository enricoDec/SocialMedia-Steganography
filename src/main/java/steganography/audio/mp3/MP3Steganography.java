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

package steganography.audio.mp3;

import steganography.Steganography;
import steganography.audio.util.LSBChanger;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * This class can encode and decode messages from a byte array containing an MP3 audio file.
 * @author Richard Rudek
 */
public class MP3Steganography implements Steganography {

    private static final String HEADER_IDENTIFIER = "HAIM"; // Hide Audio In Mp3

    /**
     * @throws IOException if carrier is not an MP3 file or if the payload does not fit into the carrier
     */
    @Override
    public byte[] encode(byte[] carrier, byte[] payload) throws IOException {
        payload = addMP3SteganographyHeader(payload);

        // check if message fits into carrier
        if (payload.length * 8 > carrier.length)
            throw new IOException("Message is longer than carrier.");

        // find the modifiable bytes in the carrier
        byte[] carrierDataBytes = removeUnmodifiableBytes(carrier);

        // check if message fits into carriers data bytes
        if (payload.length * 8 > carrierDataBytes.length)
            throw new IOException("Payload doesn't fit into carrier. " + payload.length * 8 +
                    " bytes are required, but only " + carrier.length + " bytes are supplied.");

        // encode payload
        return LSBChanger.writeToByteArray(carrierDataBytes, payload);
    }

    @Override
    public byte[] encode(byte[] carrier, byte[] payload, long seed) throws IOException {
        // TODO use a distribution algorithm
        System.err.println("Audio steganography does not support seeds yet.");
        return encode(carrier, payload);
    }

    /**
     * @throws IOException if the given byte array doesn't contain an mp3 file.
     */
    @Override
    public byte[] decode(byte[] steganographicData) throws IOException {
        // find all frames in mp3 bytes
        MP3File file = new MP3File(steganographicData);
        if (!file.findAllFrames())
            throw new IOException("The given byte array does not contain any MP3 headers.");

        // get data bytes
        byte[] dataBytes = removeUnmodifiableBytes(steganographicData);

        // search for header identifier
        byte[] possibleHeader = LSBChanger.readFromByteArray(dataBytes, 0, 4);
        if (!new String(possibleHeader, StandardCharsets.US_ASCII).equals(HEADER_IDENTIFIER))
            throw new IOException("The steganographic data does not contain a hidden message.");

        // find length of message
        byte[] messageLength = LSBChanger.readFromByteArray(dataBytes, 4, 4);
        int length = ByteBuffer.wrap(messageLength).getInt();

        // retrieve message
        return LSBChanger.readFromByteArray(dataBytes, 8, length);
    }

    @Override
    public byte[] decode(byte[] steganographicData, long seed) throws IOException {
        // TODO use a distribution algorithm
        System.err.println("Audio steganography does not support seeds yet.");
        return decode(steganographicData);
    }

    /**
     * @throws IOException if the given byte array does not contain an MP3 file.
     */
    @Override
    public boolean isSteganographicData(byte[] data) throws IOException {
        // find all frames in mp3 bytes
        MP3File file = new MP3File(data);
        if (!file.findAllFrames())
            throw new IOException("The given byte array does not contain any MP3 headers.");

        // get data bytes
        byte[] dataBytes = removeUnmodifiableBytes(data);

        // search for header identifier
        byte[] possibleHeader = LSBChanger.readFromByteArray(dataBytes, 0, 4);

        return new String(possibleHeader, StandardCharsets.US_ASCII).equals(HEADER_IDENTIFIER);
    }

    private byte[] removeUnmodifiableBytes(byte[] carrier) throws IOException {
        MP3File file = new MP3File(carrier);
        if (!file.findAllFrames())
            throw new IOException("No modifiable bytes found in carrier, can't encode payload. " +
                    "The supplied carrier is not an MP3 file.");
        List<Integer> modifiableBytes = file.getModifiablePositions();
        byte[] carrierDataBytes = new byte[modifiableBytes.size()];
        for (int i = 0; i < modifiableBytes.size(); i++) {
            carrierDataBytes[i] = carrier[modifiableBytes.get(i)];
        }
        return carrierDataBytes;
    }

    /**
     * Adds a 64 bit header to the message to identify it when the message is retrieved.
     * The Header is composed like this:<br/>
     * <ul>
     *     <li>Bits 0 - 31: MP3 Identifier (MP3+)</li>
     *     <li>Bits 32 - 64: Length of Message in Bytes</li>
     * </ul>
     * @param payload the message that should be hidden in the mp3 file
     * @return byte[] - message with added steganographic header
     */
    private byte[] addMP3SteganographyHeader(byte[] payload) {
        byte[] headerAndMessage = new byte[payload.length + 8];

        // add identifier
        byte[] identifier = HEADER_IDENTIFIER.getBytes(StandardCharsets.US_ASCII);
        System.arraycopy(identifier, 0, headerAndMessage, 0, identifier.length);

        // add length
        int value = payload.length;
        byte[] messageLength = new byte[] {
                (byte) (value >>> 24),
                (byte) (value >>> 16),
                (byte) (value >>> 8),
                (byte) value
        };
        System.arraycopy(messageLength, 0, headerAndMessage, 4, messageLength.length);

        // add original message
        System.arraycopy(payload, 0, headerAndMessage, 8, payload.length);
        return headerAndMessage;
    }
}
