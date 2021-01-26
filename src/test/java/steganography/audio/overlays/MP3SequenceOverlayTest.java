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

package steganography.audio.overlays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import steganography.audio.exception.AudioNotFoundException;

public class MP3SequenceOverlayTest {

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                  constructor
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    public void sequenceConstructorTest_byteArrayIsNull_expectAudioNotFoundException() {
        Assertions.assertThrows(AudioNotFoundException.class, () ->
                new MP3SequenceOverlay(null, 0)
        );
    }

    @Test
    public void sequenceConstructorTest_byteArrayIsEmpty_expectAudioNotFoundException() {
        Assertions.assertThrows(AudioNotFoundException.class, () ->
                new MP3SequenceOverlay(new byte[] {}, 0)
        );
    }

    @Test
    public void sequenceConstructorTest_byteArrayHasNoAudio_expectAudioNotFoundException() {
        // create byte array with nonsensical data
        byte[] bytes = new byte[] {0, 10, 25, -13, -111, 127};

        Assertions.assertThrows(AudioNotFoundException.class, () ->
                new MP3SequenceOverlay(bytes, 0)
        );
    }
/*
    @Test
    public void sequenceConstructorTest_validParams() throws AudioNotFoundException {
        // create byte array with valid data
        byte[] bytes = new byte[] {};
        byte[] dataBytes = new byte[] {};

        AudioOverlay overlay = new MP3SequenceOverlay(bytes, 0);
        Assertions.assertNotNull(overlay);
        Assertions.assertTrue(Arrays.equals(dataBytes, overlay.getBytes()));
    }

    @Test
    public void sequenceConstructorTest_differentSeeds_expectSameBytes() throws AudioNotFoundException {
        // create byte array with valid data
        byte[] bytes = new byte[] {};

        AudioOverlay overlay1 = new MP3SequenceOverlay(bytes, 1);
        AudioOverlay overlay2 = new MP3SequenceOverlay(bytes, 2);

        Assertions.assertTrue(Arrays.equals(overlay1.getBytes(), overlay2.getBytes()));
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                    next()
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    public void sequenceNextTest_() throws AudioNotFoundException {
        byte[] bytes = new byte[] {};

        AudioOverlay overlay = new MP3SequenceOverlay(bytes, 0);
        overlay.next();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                 available()
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                  setByte()
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                  getByte()
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
*/
}
