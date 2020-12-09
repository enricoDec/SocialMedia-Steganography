/*
 * Copyright (c) 2020
 * Contributed by Richard Rudek
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

package steganography.audio.exception;

import steganography.exceptions.MediaCapacityException;

/**
 * Thrown if the capacity of an audio file is not enough to encode the payload.
 * @author Richard Rudek
 */
public class AudioCapacityException extends MediaCapacityException {

    public AudioCapacityException() {
        super();
    }

    public AudioCapacityException(String s) {
        super(s);
    }
}
