/*
 * Copyright (c) 2020
 * Contributed by NAME HERE
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

package steganography.image.mocks;

import steganography.image.overlays.PixelCoordinateOverlay;

import java.awt.image.BufferedImage;
import java.util.NoSuchElementException;

public class MockOverlay implements PixelCoordinateOverlay {

    private final int[] mockPixels;
    private int position = -1;

    public MockOverlay(int[] mockPixels) {
        this.mockPixels = mockPixels;
    }

    @Override
    public int next() throws NoSuchElementException {
        return this.mockPixels[++this.position];
    }

    @Override
    public void setPixel(int value) throws NoSuchElementException {
        this.mockPixels[this.position] = value;
    }

    @Override
    public int available() {
        return this.mockPixels.length - this.position -1;
    }

    public int[] getMockPixels() {
        return this.mockPixels;
    }

    @Override
    public BufferedImage getBufferedImage() {
        return null;
    }
}
