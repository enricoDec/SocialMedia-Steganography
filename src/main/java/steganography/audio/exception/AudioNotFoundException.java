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

import steganography.exceptions.MediaNotFoundException;

/**
 * Thrown if an audio file could be read from the given media.
 *
 * @author Richard Rudek
 */
public class AudioNotFoundException extends MediaNotFoundException {

    public AudioNotFoundException() {
        super();
    }

    public AudioNotFoundException(String s) {
        super(s);
    }
}
