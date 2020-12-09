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
import steganography.audio.exception.AudioCapacityException;
import steganography.audio.exception.AudioNotFoundException;
import steganography.audio.overlays.AudioOverlay;
import steganography.audio.overlays.MP3Overlays;
import steganography.audio.overlays.MP3SequenceOverlay;
import steganography.audio.overlays.MP3ShuffleOverlay;
import steganography.audio.util.LSBChanger;
import steganography.exceptions.MediaCapacityException;
import steganography.exceptions.MediaNotFoundException;
import steganography.exceptions.UnknownStegFormatException;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * This class can encode and decode messages from a byte array containing an MP3 audio file using an overlay.
 * @author Richard Rudek
 */
public class MP3Steganography implements Steganography {

    private static final String HEADER_IDENTIFIER = "HAIM";
    private static final long DEFAULT_SEED = 5571009188606006082L;

    private final MP3Overlays OVERLAY;

    /**
     * Set the {@link MP3Overlays MP3Overlay} to use.
     * @param overlay overlay to use
     */
    public MP3Steganography(MP3Overlays overlay) {
        this.OVERLAY = overlay;
    }

    /**
     * Creates an instance using the default overlay.
     * Default = SEQUENCE_ENCODER
     */
    public MP3Steganography() {
        this(MP3Overlays.SEQUENCE_OVERLAY);
    }

    private AudioOverlay getOverlay(byte[] bytes, long seed) throws AudioNotFoundException {
        AudioOverlay overlay;
        switch (this.OVERLAY) {
            case SHUFFLE_OVERLAY:
                overlay = new MP3ShuffleOverlay(bytes, seed);
                break;
            case SEQUENCE_OVERLAY:
            default:
                overlay = new MP3SequenceOverlay(bytes, seed);
                break;
        }
        return overlay;
    }

    /**
     * @throws
     */
    @Override
    public byte[] encode(byte[] carrier, byte[] payload)
            throws AudioNotFoundException, AudioCapacityException {
        return encode(carrier, payload, DEFAULT_SEED);
    }

    /**
     * @throws
     */
    @Override
    public byte[] encode(byte[] carrier, byte[] payload, long seed)
            throws AudioNotFoundException, AudioCapacityException {
        if (carrier == null || carrier.length == 0 || payload == null || payload.length == 0)
            throw new NullPointerException("Carrier or payload are null or have length 0");

        System.out.println("[INFO] Starting to encode into MP3 file.");

        // add mp3 steganography header
        payload = addMP3SteganographyHeader(payload);

        // check if payload fits into carrier
        if (payload.length * 8 > carrier.length)
            throw new AudioCapacityException("Message is longer than carrier.");

        // start encoding
        AudioOverlay overlay;
        byte[] result;
        // create overlay (also checks if the bytes are a valid mp3 file)
        overlay = getOverlay(carrier, seed);
        // get the encoder
        LSBChanger lsbChanger = new LSBChanger(overlay);
        // encode the payload
        result = lsbChanger.encode(payload);

        System.out.println("[INFO] Finished encoding into MP3 file.");
        System.out.println();
        return result;
    }

    /**
     * @throws
     */
    @Override
    public byte[] decode(byte[] steganographicData) throws UnknownStegFormatException, AudioNotFoundException {
        return decode(steganographicData, DEFAULT_SEED);
    }

    /**
     * @throws
     */
    @Override
    public byte[] decode(byte[] steganographicData, long seed) throws UnknownStegFormatException, AudioNotFoundException {
        if (steganographicData == null || steganographicData.length == 0)
            throw new NullPointerException("steganographicData is null or has length 0");

        System.out.println("[INFO] Starting to decode from MP3 file.");

        byte[] message;

        // create overlay (also checks if the bytes are a valid mp3 file)
        AudioOverlay overlay = getOverlay(steganographicData, seed);
        // get the decoder
        LSBChanger lsbChanger = new LSBChanger(overlay);

        // decode the header
        byte[] header = lsbChanger.decode(4);
        if (!new String(header, StandardCharsets.US_ASCII).equals(HEADER_IDENTIFIER))
            throw new IllegalArgumentException("No hidden Message found.");

        // if header was found, decode message length
        int length = ByteBuffer.wrap(lsbChanger.decode(4)).getInt();

        // decode message according to decoded message length
        message = lsbChanger.decode(length);

        System.out.println("[INFO] Finished decoding from MP3 file.");
        System.out.println();
        return message;
    }

    /**
     * @throws
     */
    @Override
    public boolean isSteganographicData(byte[] data) throws AudioNotFoundException {
        return isSteganographicData(data, DEFAULT_SEED);
    }

    /**
     * @throws
     */
    @Override
    public boolean isSteganographicData(byte[] data, long seed) throws AudioNotFoundException {
        byte[] possibleHeader;
        // create overlay and decoder
        AudioOverlay overlay = getOverlay(data, seed);
        LSBChanger lsbChanger = new LSBChanger(overlay);

        // try to decode header
        try {
            possibleHeader = lsbChanger.decode(4);
        } catch (UnknownStegFormatException e) {
            return false;
        }
        return new String(possibleHeader, StandardCharsets.US_ASCII).equals(HEADER_IDENTIFIER);
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
        byte[] headerAndMessage = new byte[8 + payload.length];

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
