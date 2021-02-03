/*
 * Copyright (c) 2020
 * Contributed by Enrico de Chadarevian
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

package steganography.video.encoders;

import java.io.IOException;
import java.util.List;

public interface IDecoder {

    /**
     * Decode a Video to single Pictures (that can be read as Buff Images) as byte[]
     *
     * @param nThread Number of Threads to use to decode
     * @return List of pictures decoded from Video
     * @throws IOException if a read or write fails
     */
    List<byte[]> toPictureByteArray(int nThread) throws IOException;
}
