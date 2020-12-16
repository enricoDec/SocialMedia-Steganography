/*
 * Copyright (c) 2020
 * Contributed by Henk Lubig
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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SerialOverlay implements BufferedImageCoordinateOverlay {

    protected final BufferedImage bufferedImage;
    protected List<Integer> pixelOrder;
    protected int currentPosition = -1;
    protected int currentX = 0;
    protected int currentY = 0;

    protected SerialOverlay(BufferedImage bufferedImage) throws UnsupportedImageTypeException {
        this.bufferedImage = bufferedImage;

        int type = this.bufferedImage.getType();
        if (!this.typeAccepted(type))
            throw new UnsupportedImageTypeException("This overlay doesn't support images of type " + type);
    }

    public SerialOverlay(BufferedImage bufferedImage, long seed) throws UnsupportedImageTypeException {
        this(bufferedImage);
        createOverlay();
    }

    protected boolean typeAccepted(int type) {
        // boolean accept;
        //
        // switch (type) {
        //     case BufferedImage.TYPE_INT_ARGB:
        //     case BufferedImage.TYPE_INT_RGB:
        //     case BufferedImage.TYPE_INT_BGR:
        //     case BufferedImage.TYPE_4BYTE_ABGR:
        //         accept = true;
        //         break;
        //     default: accept = false;
        // }
        //
        // return accept;
        int pixelSize = this.bufferedImage.getColorModel().getPixelSize();

        return pixelSize == 24 || pixelSize == 32;
    }

    protected void createOverlay() {
        if (this.pixelOrder == null) {
            this.pixelOrder =
                    IntStream.range(0, bufferedImage.getHeight() * bufferedImage.getWidth())
                            .boxed()
                            .collect(Collectors.toList());
        }
    }

    @Override
    public int next() throws NoSuchElementException {
        if (++currentPosition >= this.pixelOrder.size())
            throw new NoSuchElementException("No pixels left.");

        this.currentX = this.pixelOrder.get(this.currentPosition) % this.bufferedImage.getWidth();
        this.currentY = this.pixelOrder.get(this.currentPosition) / this.bufferedImage.getWidth();
        return this.bufferedImage.getRGB(this.currentX, this.currentY);
    }
// Overflow bei berechnung des Color Couple und kleine unterschiede zwischen Get und set Pixel
    @Override
    public void setPixel(int value) {
        if (currentPosition < 0 || this.currentPosition >= this.pixelOrder.size())
            throw new NoSuchElementException("No pixel at current position.");
        int getColor = (this.bufferedImage.getRGB(this.currentX, this.currentY));
        this.bufferedImage.setRGB(this.currentX, this.currentY, (value));

            //System.out.println("In: Alpha= " + ((value >> 24) & 0xFF) + ", Red= " + ((value >> 16) & 0xFF) + ", Green= " + ((value >> 8) & 0xFF) + ", Blue= " + (value & 0xFF) + "\n" +
              //      "Out: " + "Alpha= " + ((getColor >> 24) & 0xFF) + ", Red= " + ((getColor >> 16) & 0xFF) + ", Green= " + ((getColor >> 8) & 0xFF) + ", Blue= " + (getColor & 0xFF));

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

