package steganography.video.exceptions;

import steganography.exceptions.MediaNotFoundException;

/**
 * @author : Enrico Gamil Toros
 * Project name : ProjektSteganography
 * @version : 1.0
 * @since : 05.01.21
 * <p>
 * Thrown if a Video stream was not found in the given video carrier
 **/
public class VideoNotFoundException extends MediaNotFoundException {

    public VideoNotFoundException() {
        super();
    }

    public VideoNotFoundException(String s) {
        super(s);
    }
}
