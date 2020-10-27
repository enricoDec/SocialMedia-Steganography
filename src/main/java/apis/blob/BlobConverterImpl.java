/*
 * Copyright (c) 2020
 * Contributed by Mario Teklic
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

package apis.blob;

import apis.MediaType;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BlobConverterImpl {

    private static final Logger logger = LogManager.getLogger(BlobConverterImpl.class);

    public static byte[] downloadMedia(File blob) {
        if(blob.isFile()){
            try(FileInputStream fis = new FileInputStream(blob)){
                logger.info("Converting file to bytes: " + blob);
                return IOUtils.toByteArray(fis);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                logger.info("File not found: " + blob);
            } catch (IOException e) {
                e.printStackTrace();
                logger.info("IOException: " + blob);
            }
        }
        logger.info("Convertion of blob to bytes was null...");
        return null;
    }

    public static byte[] downloadMedia(String downloadLink) throws IOException {
        return IOUtils.toByteArray(new URL(downloadLink).openStream());
    }

    public static List<byte[]> blobsToBytes(File blobFolder) {
        if(blobFolder.isDirectory()){
            logger.info("Converting files from folder '" + blobFolder.getAbsolutePath() + "' to bytes...");
            List<byte[]> blobBytes = new ArrayList<>();
            for(File blob : blobFolder.listFiles()){
                if(blob.isFile()){
                    blobBytes.add(BlobConverterImpl.downloadMedia(blob));
                }
            }
            return blobBytes;
        }
        logger.info(blobFolder + "is not a folder. Try to convert as a file.");
        return null;
    }

    public static void byteToFile(byte[] data, String filename) throws IOException {
        FileUtils.writeByteArrayToFile(new File(filename), data);
    }

    public static void convertToBlob(byte[] data, MediaType mediaType) throws IOException {
        InputStream is = new ByteArrayInputStream(data);
        OutputStream os = new FileOutputStream("temp." + mediaType.name().toLowerCase());
        IOUtils.copy(is, os);
    }
}
