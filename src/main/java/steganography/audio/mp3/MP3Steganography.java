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
import steganography.audio.LSBChanger;
import steganography.audio.exception.AudioCapacityException;
import steganography.audio.exception.AudioNotFoundException;
import steganography.audio.overlays.AudioOverlay;
import steganography.audio.overlays.MP3Overlays;
import steganography.audio.overlays.MP3SequenceOverlay;
import steganography.audio.overlays.MP3ShuffleOverlay;
import steganography.exceptions.UnknownStegFormatException;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * This class can encode and decode messages from a byte array containing an MP3 audio file using an overlay.
 *
 * @author Richard Rudek
 */
public class MP3Steganography implements Steganography {

    /**
     * The 4 Bytes used to identify messages hidden in mp3 files
     */
    private static final String HEADER_IDENTIFIER = "HAIM";
    /**
     * The default seed used, if none is provided
     */
    private static final long DEFAULT_SEED = 5571009188606006082L;
    /**
     * The overlay used to hide the message
     */
    private final MP3Overlays overlay;


    /**
     * Creates an instance using the {@link MP3Overlays MP3Overlay} provided.
     *
     * @param overlay overlay to use
     */
    public MP3Steganography(MP3Overlays overlay) {
        this.overlay = overlay;
    }

    /**
     * Creates an instance using the default overlay.<br>
     * The default is MP3Overlays.SEQUENCE_OVERLAY.
     */
    public MP3Steganography() {
        this(MP3Overlays.SHUFFLE_OVERLAY);
    }

    /**
     * Returns the AudioOverlay instance depending on this.overlay .
     *
     * @param bytes the bytes containing the mp3 file
     * @param seed  the seed to use for encoding or decoding
     * @return Instance of an {@link AudioOverlay}
     * @throws AudioNotFoundException If the given bytes either don't contain an mp3 file or
     *                                the mp3 file is not supported by this algorithm .
     */
    private AudioOverlay getOverlay(byte[] bytes, long seed) throws AudioNotFoundException {
        AudioOverlay overlay;
        switch (this.overlay) {
            case SEQUENCE_OVERLAY:
                overlay = new MP3SequenceOverlay(bytes, seed);
                break;
            case SHUFFLE_OVERLAY:
            default:
                overlay = new MP3ShuffleOverlay(bytes, seed);
                break;
        }
        return overlay;
    }

    /**
     * Conceals the given payload in the carrier (which is an mp3 file) using a default seed.
     *
     * @param carrier a byte array containing an mp3 file
     * @param payload a byte array containing the message
     * @return The given carrier with the hidden message
     * @throws AudioNotFoundException If the given bytes either don't contain an mp3 file or
     *                                the mp3 file is not supported by this algorithm
     * @throws AudioCapacityException If the payload does not fit into the carrier
     * @throws NullPointerException   If the carrier and or payload are null or have length 0
     */
    @Override
    public byte[] encode(byte[] carrier, byte[] payload)
            throws AudioNotFoundException, AudioCapacityException, NullPointerException {
        return encode(carrier, payload, DEFAULT_SEED);
    }

    /**
     * Conceals the given payload in the carrier (which is an mp3 file) using the given seed.
     *
     * @param carrier a byte array containing an mp3 file
     * @param payload a byte array containing the message
     * @return the given carrier with the hidden message
     * @throws AudioNotFoundException If the given bytes either don't contain an mp3 file or
     *                                the mp3 file is not supported by this algorithm
     * @throws AudioCapacityException If the payload does not fit into the carrier
     * @throws NullPointerException   If the carrier and or payload are null or have length 0
     */
    @Override
    public byte[] encode(byte[] carrier, byte[] payload, long seed)
            throws AudioNotFoundException, AudioCapacityException, NullPointerException {
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
     * Attempts to retrieve a hidden message from the given byte array (which contains an mp3 file)
     * using the default seed.
     *
     * @param steganographicData a byte array containing an mp3 file that has a message hidden within
     * @return a byte array containing the hidden message
     * @throws UnknownStegFormatException if the message could not be read from the given byte array.
     *                                    This can happen if the file got changed after encoding
     *                                    (e.g. file gets compressed when uploading it to a social media).
     * @throws AudioNotFoundException     If the given bytes either don't contain an mp3 file or
     *                                    the mp3 file is not supported by this algorithm
     * @throws NullPointerException       If the given byte array is null or has length 0
     */
    @Override
    public byte[] decode(byte[] steganographicData)
            throws UnknownStegFormatException, AudioNotFoundException, NullPointerException {
        return decode(steganographicData, DEFAULT_SEED);
    }

    /**
     * Attempts to retrieve a hidden message from the given byte array (which contains an mp3 file)
     * using the given seed.
     *
     * @param steganographicData a byte array containing an mp3 file that has a message hidden within
     * @return a byte array containing the hidden message
     * @throws UnknownStegFormatException if the message could not be read from the given byte array.
     * @throws AudioNotFoundException     If the given bytes either don't contain an mp3 file or
     *                                    the mp3 file is not supported by this algorithm
     * @throws NullPointerException       If the given byte array is null or has length 0
     */
    @Override
    public byte[] decode(byte[] steganographicData, long seed)
            throws UnknownStegFormatException, AudioNotFoundException, NullPointerException {
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
            throw new UnknownStegFormatException("No hidden Message found.");

        // if header was found, decode message length
        int length = ByteBuffer.wrap(lsbChanger.decode(4)).getInt();

        // decode message according to decoded message length
        message = lsbChanger.decode(length);

        System.out.println("[INFO] Finished decoding from MP3 file.");
        System.out.println();
        return message;
    }

    /**
     * Checks if the given byte array contains a message that was hidden using the default seed.
     *
     * @return true, if a message was found
     * @throws AudioNotFoundException If the given bytes either don't contain an mp3 file or
     *                                the mp3 file is not supported by this algorithm
     * @throws NullPointerException   If the given byte array is null or has length 0
     */
    @Override
    public boolean isSteganographicData(byte[] data) throws AudioNotFoundException {
        return isSteganographicData(data, DEFAULT_SEED);
    }

    /**
     * Checks if the given byte array contains a message that was hidden using the given seed.
     *
     * @return true, if a message was found
     * @throws AudioNotFoundException If the given bytes either don't contain an mp3 file or
     *                                the mp3 file is not supported by this algorithm
     * @throws NullPointerException   If the given byte array is null or has length 0
     */
    @Override
    public boolean isSteganographicData(byte[] data, long seed) throws AudioNotFoundException {
        if (data == null || data.length == 0)
            throw new NullPointerException("Data is null or has length 0");

        // create overlay and decoder
        AudioOverlay overlay = getOverlay(data, seed);
        LSBChanger lsbChanger = new LSBChanger(overlay);

        // try to decode header
        byte[] possibleHeader;

        try {
            possibleHeader = lsbChanger.decode(4);
        } catch (UnknownStegFormatException e) {
            return false;
        }

        return new String(possibleHeader, StandardCharsets.US_ASCII).equals(HEADER_IDENTIFIER);
    }

    /**
     * Adds a 64 bit (8 bytes) header to the message to identify it when the message is retrieved.
     * The Header is composed like this:<br>
     * <ul>
     *     <li>Bits 0 - 31: MP3 Identifier (MP3+)</li>
     *     <li>Bits 32 - 64: Length of Message in Bytes</li>
     * </ul>
     *
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
        byte[] messageLength = new byte[]{
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
