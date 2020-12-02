package steganography.video;

import java.io.IOException;
import java.util.List;

public interface IEncoder {

    /**
     * Encodes a List of images to a Video
     * (PNG Slideshow codec should be used)
     * @param stegImages list of Stenographic images
     * @return Encoded Video byte[]
     * @throws IOException IOException
     */
    byte[] imagesToVideo(List<byte[]> stegImages) throws IOException;
}
