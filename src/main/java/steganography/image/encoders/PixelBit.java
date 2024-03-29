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

package steganography.image.encoders;

import steganography.image.exceptions.ImageCapacityException;
import steganography.image.overlays.PixelCoordinateOverlay;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Implementation of RandomLSB, an algorithm to encode hidden messages into images
 *
 * @author Henk-Joas Lubig
 */
public class PixelBit extends BuffImgEncoder {
    private int numOfChannels = 3;

    public PixelBit(PixelCoordinateOverlay overlay) throws IllegalArgumentException {
        super(overlay);
    }

    /**
     * <p>In this algorithm, if the return of this function is true, the given pixel represents a bit-value of 1.
     * If it is false, the pixel represents a bit-value of 0.</p>
     * <p>Returns true, if the sum of the individual bytes of pixelARGB is an uneven number ((A+R+G+B) mod 2 == 1).</p>
     * <p>Differently put: It determines whether the amount of 1's in the least significant bits
     * of each individual byte of pixelARGB is uneven.</p>
     *
     * @param pixelARGB pixel that represents a bit.
     * @return true if the given pixel represents a 1 bit.
     */
    public static boolean pixelIsOne(int pixelARGB) {
        return (
                (pixelARGB & 1) ^
                        (pixelARGB >> 8 & 1) ^
                        (pixelARGB >> 16 & 1) ^
                        (pixelARGB >> 24 & 1)
        ) > 0;
    }

    /**
     * Returns the number of color channels this algorithm is currently choosing from
     * to encode data. Cannot be greater than 4 or smaller than 1.
     *
     * @return the number of color channels currently used
     */
    public int getNumberOfChannels() {
        return this.numOfChannels;
    }

    /**
     * <p>Sets the number of color channels this algorithm is choosing from
     * to encode data. Cannot be greater than 4 or smaller than 1.</p>
     * <p>If the value is 1, this algorithm acts as a normal LSB-Encoder.</p>
     * <ul>
     *     <li>1: Blue channel</li>
     *     <li>2: Green and Blue channel</li>
     *     <li>3: Red, Green and Blue channel</li>
     *     <li>4: Alpha, Red, Green and Blue channel</li>
     * </ul>
     *
     * @param numberOfChannels number of channels to use
     */
    public void setNumberOfChannels(int numberOfChannels) {
        if (numberOfChannels > 4 || numberOfChannels < 1)
            throw new IllegalArgumentException("Number of channels can only be a number between " +
                    "1 (inclusive) and 4 (inclusive)");
        this.numOfChannels = numberOfChannels;
    }

    @Override
    public void encode(byte[] payload) throws ImageCapacityException {
        if ((payload.length * 8) > overlay.available()) {
            String sb = "More Bits of payload (" +
                    payload.length * 8 +
                    ") than pixels available (" +
                    this.overlay.available() + ")";
            throw new ImageCapacityException(sb);
        }

        for (byte bite : payload) {
            for (int bitNo = 7; bitNo >= 0; bitNo--) {
                // turn bit to boolean (0 or 1) -> true if 1
                boolean bitIsOne = (bite & (1 << bitNo)) > 0;
                // get current pixel
                int pixelARGB = this.overlay.next();
                // true if pixel represents 1
                boolean pixelIsOne = pixelIsOne(pixelARGB);
                // if payload bit != pixelBit -> flip pixelBit
                if (bitIsOne != pixelIsOne)
                    this.overlay.setPixel(changePixelValue(pixelARGB));
            }
        }
    }

    @Override
    public byte[] decode(int bLength) {

        if (bLength > this.overlay.available() / 8)
            throw new IndexOutOfBoundsException("bLength cannot be greater than the images capacity of " +
                    this.overlay.available() / 8 + " bytes");

        // true = 1; false = 0;
        List<Boolean> pixelBitList = new ArrayList<>();
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        for (int i = 0; i < bLength; i++) {
            for (int j = 0; j < 8; j++) {
                pixelBitList.add(pixelIsOne(this.overlay.next()));
            }
            result.write(
                    bits2Byte(
                            pixelBitList.toArray(new Boolean[0])
                    )
            );
            pixelBitList.clear();
        }
        return result.toByteArray();
    }

    /**
     * Turns an array of 8 booleans into a byte
     *
     * @param pixelByte
     * @return
     */
    private byte bits2Byte(Boolean[] pixelByte) {
        if (pixelByte.length != 8)
            throw new ArrayIndexOutOfBoundsException("bits2byte: Array must have length of exactly 8");

        int result = 0;
        for (Boolean pixelBit : pixelByte) {
            result = (result << 1);
            if (pixelBit)
                result = (result | 1);
        }

        return (byte) (result & 0xff);
    }

    /**
     * <p>Changes the value of a random color channel (ARGB) of the given pixel
     * by +1 or -1 (randomly, but avoiding overflow).</p>
     * <p>Since a pixel represents a bit, this method "flips" it.
     * (By changing the outcome of (A+R+G+B) &amp; 1 == 0)</p>
     *
     * @param pixelARGB the pixelValue to change
     * @return the changed pixelValue
     */
    protected int changePixelValue(int pixelARGB) {
        Random rng = new Random();

        // pick random channel
        int channelPick = rng.nextInt(this.numOfChannels) * 8;
        // extract the byte of picked channel
        int channel = ((pixelARGB >> channelPick) & 0xff);

        // check if addition or subtraction would cause overflow and prevent it
        // (not channel ^ 1, because change would not be random)
        int addition;
        // if all bits are 1, subtract 1
        if ((channel & 0xff) == 0xff) {
            addition = -1;
            // if all bits are 0, add 1
        } else if (channel == 0) {
            addition = 1;
        } else {
            // if there is no overflow add or subtract 1 at random
            addition = (rng.nextBoolean() ? 1 : -1);
        }
        channel += addition;

        // put modified byte back to its place in the int
        return (pixelARGB | (0xff << channelPick)) & ~((~channel & 0xff) << channelPick);
        // overwrite previous picked byte in original int (pxInt) with 1111 1111
        // invert channel, position it in another int and invert again -> 11..channel..11
        // bitwise AND replaces old byte with channel and keeps the rest of pxInt
    }
}
