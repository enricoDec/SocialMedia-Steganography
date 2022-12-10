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
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * This class returns Pixels of the underlying BufferedImage in order from top left (x=0, y=0)
 * to bottom right (x=bufferedImage.getWidth(), y=bufferedImage.getHeight()).
 */
public class SequenceOverlay implements PixelCoordinateOverlay {

    protected final BufferedImage bufferedImage;
    protected List<Integer> pixelOrder;
    protected int currentPosition = -1;
    protected int currentX = 0;
    protected int currentY = 0;

    /**
     * Creates a SequenceOverlay that returns Pixels of the underlying BufferedImage in order from top left (x=0, y=0)
     * to bottom right (x=bufferedImage.getWidth(), y=bufferedImage.getHeight()).
     *
     * @param bufferedImage the BufferedImage to represent the pixels of.
     * @throws UnsupportedImageTypeException if the type of bufferedImage is bnot supported by this overlay
     */
    public SequenceOverlay(BufferedImage bufferedImage) throws UnsupportedImageTypeException {
        this.bufferedImage = bufferedImage;

        int type = this.bufferedImage.getType();
        if (!this.typeAccepted(type))
            throw new UnsupportedImageTypeException("This overlay doesn't support images of type " + type);
    }

    /**
     * <p>Checks whether the type of the given image is accepted by this overlay.</p>
     * <p>Overwritten by subclasses to apply their own rules for acceptance.</p>
     *
     * @param type representation of an image type as an int of BufferedImage.imageType
     *             <p>(type doesn't matter, for this overlay, but it may for subclasses)</p>
     * @return true if the images type is accepted by this overlay
     */
    protected boolean typeAccepted(int type) {
        int pixelSize = this.bufferedImage.getColorModel().getPixelSize();

        return pixelSize == 24 || pixelSize == 32;
    }

    /**
     * <p>Creates the overlay as an independent method to address pixels without using
     * BufferedImages coordinates.</p>
     * <p>Subclasses overwrite this method to use their own logic of creating the overlay.</p>
     */
    protected void createOverlay() {
        this.pixelOrder =
                IntStream.range(0, bufferedImage.getHeight() * bufferedImage.getWidth())
                        .boxed()
                        .collect(Collectors.toList());
    }

    @Override
    public int next() throws NoSuchElementException {
        if (this.pixelOrder == null)
            createOverlay();

        if (++currentPosition >= this.pixelOrder.size())
            throw new NoSuchElementException("No pixels left.");

        this.currentX = this.pixelOrder.get(this.currentPosition) % this.bufferedImage.getWidth();
        this.currentY = this.pixelOrder.get(this.currentPosition) / this.bufferedImage.getWidth();
        return this.bufferedImage.getRGB(this.currentX, this.currentY);
    }

    @Override
    public void setPixel(int value) {
        if (this.pixelOrder == null)
            createOverlay();

        if (currentPosition < 0 || this.currentPosition >= this.pixelOrder.size())
            throw new NoSuchElementException("No pixel at current position.");
        this.bufferedImage.setRGB(this.currentX, this.currentY, (value));
    }

    @Override
    public int available() {
        if (this.pixelOrder == null)
            createOverlay();

        return this.pixelOrder.size() - this.currentPosition - 1;
    }
}

