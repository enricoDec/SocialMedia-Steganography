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

package steganography.image.overlays;

import steganography.image.exceptions.UnsupportedImageTypeException;

import java.awt.image.BufferedImage;
import java.util.*;

/**
 * This class returns Pixels of the underlying Bitmap in a random order determined by the seed
 * that is given to its constructor
 */
public class ShuffleOverlay extends SequenceOverlay {

    protected Random random;

    public ShuffleOverlay(BufferedImage bufferedImage, long seed) throws UnsupportedImageTypeException {
        super(bufferedImage);
        this.random = new Random(seed);
    }
/*

    public ShuffleOverlay(BufferedImage bufferedImage, long seed) throws UnsupportedImageTypeException {
        super(bufferedImage);

        this.random = new Random(seed);
        createOverlay();
        shufflePixelOrder();
    }
*/

    protected void createOverlay() {
        super.createOverlay();
        shufflePixelOrder();
    }

    private void shufflePixelOrder() {
        Collections.shuffle(this.pixelOrder, this.random);
    }
}
