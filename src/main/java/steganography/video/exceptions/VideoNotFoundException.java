package steganography.video.exceptions;

import steganography.exceptions.MediaNotFoundException;

/**
 * @author : Enrico Gamil Toros
 * Project name : ProjektSteganography
 * @version : 1.0
 * @since : 05.01.21
 **/
public class VideoNotFoundException extends MediaNotFoundException {

    public VideoNotFoundException() {
        super();
    }

    public VideoNotFoundException(String s) {
        super(s);
    }
}
