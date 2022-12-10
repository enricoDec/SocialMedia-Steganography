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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import steganography.audio.exception.AudioCapacityException;
import steganography.audio.mock.MockMP3Overlay;
import steganography.audio.overlays.AudioOverlay;
import steganography.exceptions.UnknownStegFormatException;

import java.nio.charset.StandardCharsets;

public class LSBChangerTest {

    private final byte[] bytes = new byte[]{
            -128,  // 1000 0000
            -124,  // 1000 0100
            -1,  // 1111 1111
            0,  // 0000 0000
            0,  // 0000 0000
            1,  // 0000 0001
            4,  // 0000 0100
            127   // 0111 1111
    };
    private LSBChanger lsb;

    @BeforeEach
    public void getLSBChanger() {
        AudioOverlay overlay = new MockMP3Overlay(this.bytes);
        this.lsb = new LSBChanger(overlay);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                  Encode
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    public void encodeNull_ExpectNoChanges() throws AudioCapacityException {
        byte[] encoded = this.lsb.encode(null);

        Assertions.assertArrayEquals(this.bytes, encoded);
    }

    @Test
    public void encodeEmptyString_ExpectNoChanges() throws AudioCapacityException {
        byte[] message = "".getBytes(StandardCharsets.UTF_8);

        byte[] encoded = this.lsb.encode(message);

        Assertions.assertArrayEquals(this.bytes, encoded);
    }

    @Test
    public void encodeMessageWithMissingCapacity_ExpectAudioCapacityException() {
        Assertions.assertThrows(AudioCapacityException.class, () ->
                this.lsb.encode("Aa".getBytes(StandardCharsets.UTF_8))
        );
    }

    @Test
    public void encodeMessageWithExactlyEnoughCapacity_ExpectChangedBytes() throws AudioCapacityException {
        // A = 65 = 0100 0001
        byte[] expected = new byte[]{
                -128,  // 1000 0000
                -123,  // 1000 0101
                -2,  // 1111 1110
                0,  // 0000 0000
                0,  // 0000 0000
                0,  // 0000 0000
                4,  // 0000 0100
                127   // 0111 1111
        };

        byte[] message = "A".getBytes(StandardCharsets.UTF_8);
        byte[] encoded = this.lsb.encode(message);

        Assertions.assertArrayEquals(expected, encoded);
    }

    @Test
    public void encodeMessageWithEnoughCapacity_ExpectCorrectChanges() throws AudioCapacityException {
        byte[] bytes = new byte[]{
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0
        };
        LSBChanger customLsbEncode = new LSBChanger(new MockMP3Overlay(bytes));

        /* create message with bytes:
         * decimal -> binary
         *  80 -> 0101 0000
         * 101 -> 0110 0101
         *  98 -> 0110 0010
         *  98 -> 0110 0010
         * 108 -> 0110 1100
         * 101 -> 0110 0101
         * 115 -> 0111 0011
         */
        byte[] message = "Pebbles".getBytes(StandardCharsets.UTF_8);

        byte[] encoded = customLsbEncode.encode(message);

        byte[] expected = new byte[]{
                0, 1, 0, 1, 0, 0, 0, 0,   // P
                0, 1, 1, 0, 0, 1, 0, 1,   // e
                0, 1, 1, 0, 0, 0, 1, 0,   // b
                0, 1, 1, 0, 0, 0, 1, 0,   // b
                0, 1, 1, 0, 1, 1, 0, 0,   // l
                0, 1, 1, 0, 0, 1, 0, 1,   // e
                0, 1, 1, 1, 0, 0, 1, 1,   // s
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0
        };

        Assertions.assertArrayEquals(expected, encoded);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                  Decode
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    public void decodeNegativeLength_ExpectIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                this.lsb.decode(-1)
        );
    }

    @Test
    public void decodeLengthZero_ExpectIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                this.lsb.decode(0)
        );
    }

    @Test
    public void decodeLengthTooBig_ExpectUnknownStegFormatException() {
        Assertions.assertThrows(UnknownStegFormatException.class, () ->
                this.lsb.decode(100)
        );
    }

    @Test
    public void decodeSingleCharacter() throws UnknownStegFormatException {
        byte[] message = this.lsb.decode(1);

        Assertions.assertEquals(1, message.length);
        Assertions.assertEquals("%", new String(message, StandardCharsets.UTF_8));
    }

    @Test
    public void decodeMessage() throws UnknownStegFormatException {
        byte[] bytes = new byte[]{
                0, 1, 0, 1, 0, 0, 0, 0,
                0, 1, 1, 0, 0, 1, 0, 1,
                0, 1, 1, 0, 0, 0, 1, 0,
                0, 1, 1, 0, 0, 0, 1, 0,
                0, 1, 1, 0, 1, 1, 0, 0,
                0, 1, 1, 0, 0, 1, 0, 1,
                0, 1, 1, 1, 0, 0, 1, 1,

                101, 41, 55, 99, -74, -42, 0, 17,
                61, 0, -12, 0, 122, 0, 0, -128,
                92, 0, -83, 83, 0, 127, -1, 1
        };
        LSBChanger customLsbDecode = new LSBChanger(new MockMP3Overlay(bytes));

        byte[] message = customLsbDecode.decode(7);

        Assertions.assertEquals(7, message.length);
        Assertions.assertEquals("Pebbles", new String(message, StandardCharsets.UTF_8));
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                           Encode + Decode
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    public void encodeCharacterAndDecode_ExpectSameCharacter() throws UnknownStegFormatException,
            AudioCapacityException {
        String messageString = "A";
        byte[] encoded = this.lsb.encode(messageString.getBytes(StandardCharsets.UTF_8));

        LSBChanger customLsbIntegration = new LSBChanger(new MockMP3Overlay(encoded));
        byte[] decoded = customLsbIntegration.decode(1);

        String decodedString = new String(decoded, StandardCharsets.UTF_8);

        Assertions.assertEquals(messageString, decodedString);
    }
}
