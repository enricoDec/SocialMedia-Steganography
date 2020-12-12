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

package steganography.image;

import steganography.util.BufferedImageCoordinateOverlay;

import java.awt.image.BufferedImage;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RemoveTransparentShuffleOverlay implements BufferedImageCoordinateOverlay {

    private BufferedImage bufferedImage;
    private List<Integer> pixelOrder;
    private int currentPosition = -1;
    private int currentX = 0;
    private int currentY = 0;

    public RemoveTransparentShuffleOverlay(BufferedImage bufferedImage, long seed) {
        this.bufferedImage = bufferedImage;
        createOverlay();
        removeTransparent();
        Collections.shuffle(this.pixelOrder, new Random(seed));
    }

    private void removeTransparent() {

    }

    private void createOverlay() {
        this.pixelOrder = new ArrayList<>();
        for(int y = 0; y < this.bufferedImage.getHeight(); y++) {
            for (int x = 0; x < this.bufferedImage.getWidth(); x++) {
                int pixel = this.bufferedImage.getRGB(x, y);
                if(((pixel >> 24) & 0xff) != 0)
                    this.pixelOrder.add(x + (y * this.bufferedImage.getWidth()));
            }
        }
    }

    public void useColors(int[][] colors) {

    }

    @Override
    public int next() throws NoSuchElementException {
        if (++currentPosition >= this.pixelOrder.size())
            throw new NoSuchElementException("No pixels left.");

        this.currentX = this.pixelOrder.get(this.currentPosition) % this.bufferedImage.getWidth();
        this.currentY = this.pixelOrder.get(this.currentPosition) / this.bufferedImage.getWidth();
        int pixel = this.bufferedImage.getRGB(this.currentX, this.currentY);
        return pixel;
    }

    @Override
    public void setPixel(int value) {
        if (currentPosition < 0 || this.currentPosition >= this.pixelOrder.size())
            throw new NoSuchElementException("No pixel at current position.");

        this.bufferedImage.setRGB(this.currentX, this.currentY, value);
    }

    @Override
    public int available() {
        return this.pixelOrder.size() - this.currentPosition -1;
    }

    @Override
    public BufferedImage getBufferedImage() {
        return this.bufferedImage;
    }
}
