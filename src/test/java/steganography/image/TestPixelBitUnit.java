/*
 * Copyright (c) 2020
 * Contributed by Henk-Joas Lubig
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

package steganography.image;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import steganography.image.encoders.PixelBit;
import steganography.image.exceptions.ImageCapacityException;
import steganography.image.mocks.MockOverlay;

import java.util.Random;

public class TestPixelBitUnit {

    @Test
    void testFlipSingleIntegerToMatchPayload() throws ImageCapacityException {
        int input = new Random().nextInt();
        boolean inputIsOne = PixelBit.pixelIsOne(input);

        byte[] payload = inputIsOne ?  new byte[]{0} : new byte[]{(byte)(1 << 7)};

        MockOverlay mockOverlay = new MockOverlay(new int[]{input, 0, 0, 0, 0, 0, 0, 0});
        PixelBit encoder = new PixelBit(mockOverlay);

        encoder.encode(payload);

        boolean outputIsOne = PixelBit.pixelIsOne(mockOverlay.getMockPixels()[0]);

        Assertions.assertNotEquals(inputIsOne, outputIsOne);
    }

    @Test
    void testFlipAll8IntegersToMatchPayload() throws ImageCapacityException {
        Random random = new Random();
        int[] input = new int[8];
        byte payloadByte = 0;
        for (int i = 0; i < 8; i++) {
            input[i] = random.nextInt();
            payloadByte = (byte)(payloadByte << 1);
            if (PixelBit.pixelIsOne(input[i]))
                payloadByte = (byte)(payloadByte | 1);
        }

        // flip payload to mismatch all integers
        byte[] payload = new byte[]{(byte) ~payloadByte};

        MockOverlay mockOverlay = new MockOverlay(input);
        PixelBit encoder = new PixelBit(mockOverlay);

        encoder.encode(payload);

        int[] output = mockOverlay.getMockPixels();
        byte resultByte = 0;
        for (int i = 0; i < 8; i++) {
            resultByte = (byte)(resultByte << 1);
            if (PixelBit.pixelIsOne(output[i]))
                resultByte = (byte)(resultByte | 1);
        }

        Assertions.assertEquals(payloadByte, (byte) ~resultByte);
    }

    @Test
    void testFlipNoneOf8IntegersToMatchPayload() throws ImageCapacityException {
        Random random = new Random();
        int[] input = new int[8];
        byte payloadByte = 0;
        for (int i = 0; i < 8; i++) {
            input[i] = random.nextInt();
            payloadByte = (byte)(payloadByte << 1);
            if (PixelBit.pixelIsOne(input[i]))
                payloadByte = (byte)(payloadByte | 1);
        }

        byte[] payload = new byte[]{payloadByte};

        MockOverlay mockOverlay = new MockOverlay(input);
        PixelBit encoder = new PixelBit(mockOverlay);

        encoder.encode(payload);

        int[] output = mockOverlay.getMockPixels();

        Assertions.assertArrayEquals(input, output);
    }
}
