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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ByteArrayUtils {
    /**
     * Util method, returns a File as a byte array
     *
     * @param file File to be read
     * @return file as byte array
     * @throws IOException read/write Exception
     */
    public static synchronized byte[] read(File file) throws IOException {
        return Files.readAllBytes(file.toPath());
    }
}
