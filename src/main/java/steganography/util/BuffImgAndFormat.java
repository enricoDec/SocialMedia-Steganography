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

package steganography.util;

import java.awt.image.BufferedImage;

public class BuffImgAndFormat {

    private final BufferedImage bufferedImage;

    private final String format;

    public BuffImgAndFormat(BufferedImage bufferedImage, String format) {
        this.bufferedImage = bufferedImage;
        this.format = format;
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public String getFormat() {
        return format;
    }
}
