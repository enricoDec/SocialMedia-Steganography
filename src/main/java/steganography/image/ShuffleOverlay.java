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

import steganography.util.BufferedImageCoordinateOverlay;

import java.awt.image.BufferedImage;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ShuffleOverlay extends SerialOverlay {

    protected Random random;

    protected ShuffleOverlay(BufferedImage bufferedImage) throws UnsupportedEncodingException {
        super(bufferedImage);
    }

    public ShuffleOverlay(BufferedImage bufferedImage, long seed) throws UnsupportedEncodingException {
        super(bufferedImage);

        this.random = new Random(seed);
        createOverlay();
    }

    protected void createOverlay() {
        super.createOverlay();
        Collections.shuffle(this.pixelOrder, this.random);
    }
}
