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

package apis.utils;

import apis.MediaType;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BlobConverterImpl {

    /**
     * Downloads a media for an example an image
     * @param downloadLink
     * @return Bytearray which represents the media
     * @throws IOException
     */
    public static byte[] downloadToByte(String downloadLink) throws IOException {
        return IOUtils.toByteArray(new URL(downloadLink).openStream());
    }

    /**
     * Creates a file called 'temp.' with mediatype extenstion.
     * Example: temp.jpg
     * @param data Data as bytearray
     * @param mediaType
     * @throws IOException
     */
    public static void convertToBlob(byte[] data, MediaType mediaType) throws IOException {
        InputStream is = new ByteArrayInputStream(data);
        OutputStream os = new FileOutputStream("temp." + mediaType.name().toLowerCase());
        IOUtils.copy(is, os);
    }
}
