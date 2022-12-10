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

package steganography.image.overlays;

import steganography.image.exceptions.UnsupportedImageTypeException;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Overlay Class for images using the Color-Couple-Algorithem
 *
 * @author Henk-Joas Lubig, Selina Wernike
 */
public class TableOverlay extends ShuffleOverlay {
    private final Map<Integer, List<Integer>> colorCouple;

    public TableOverlay(BufferedImage bufferedImage, long seed, Map<Integer, List<Integer>> colorCouple) throws UnsupportedImageTypeException {
        super(bufferedImage, seed);
        this.colorCouple = colorCouple;
    }

    /**
     * @param type pixel deth of image
     * @return true when image is a pixel depth of 8
     */
    @Override
    protected boolean typeAccepted(int type) {
        int pixelSize = this.bufferedImage.getColorModel().getPixelSize();

        return pixelSize == 8;
    }

    @Override
    protected void initOverlay() {
        this.pixelOrder = new ArrayList<>();
        for (int y = 0; y < this.bufferedImage.getHeight(); y++) {
            for (int x = 0; x < this.bufferedImage.getWidth(); x++) {
                int pixel = this.bufferedImage.getRGB(x, y);
                if (this.colorCouple.containsKey(pixel)) {
                    this.pixelOrder.add(x + (y * this.bufferedImage.getWidth()));
                }
            }
        }
    }
}
