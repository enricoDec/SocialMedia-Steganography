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
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.Random;


public class TableOverlay extends ShuffleOverlay {
    private int[] colorTable;
    private Map<Integer, List<Integer>> colorCouple;
    private Random position;

    public TableOverlay(BufferedImage bufferedImage, long seed,Map<Integer, List<Integer>> colorCouple, int[] colorTable ) throws UnsupportedEncodingException {
        super(bufferedImage);
        this.position = new Random(seed);
        this.colorCouple = colorCouple;
        this.colorTable = colorTable;
        createOverlay();
        Collections.shuffle(this.pixelOrder, new Random(seed));
    }

    /**
     * Changes the color of a pixel according to the values in ColorCouple
     * @param value The value determins wether the pixel value is even or uneven.
     */
    @Override
    public void setPixel(int value) {
        List<Integer> color = this.colorCouple.get(this.bufferedImage.getRGB(this.currentX, this.currentY));
            int index = position.nextInt(color.size());
            this.bufferedImage.setRGB(this.currentX, this.currentY, color.get(index));


    }

    @Override
    protected void createOverlay() {
        this.pixelOrder = new ArrayList<>();
        for(int y = 0; y < this.bufferedImage.getHeight(); y++) {
            for (int x = 0; x < this.bufferedImage.getWidth(); x++) {
                int pixel = this.bufferedImage.getRGB(x, y);
                if(this.colorCouple.containsKey(pixel))
                    this.pixelOrder.add(x + (y * this.bufferedImage.getWidth()));
            }
        }
    }
}
