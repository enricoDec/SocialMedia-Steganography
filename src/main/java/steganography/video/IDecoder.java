package steganography.video;

import java.io.IOException;
import java.util.List;

public interface IDecoder {

    /**
     * Decode a Video to single Pictures (that can be read as Buff Images)
     * @param nThread number of Threads to use to decode
     * @return list of pictures decoded from Video
     */
    List<byte[]> toPictureByteArray(int nThread) throws IOException;
}
