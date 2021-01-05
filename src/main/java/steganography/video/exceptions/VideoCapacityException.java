package steganography.video.exceptions;

import steganography.exceptions.MediaCapacityException;

/**
 * @author : Enrico Gamil Toros
 * Project name : ProjektSteganography
 * @version : 1.0
 * @since : 05.01.21
 **/
public class VideoCapacityException extends MediaCapacityException {

    public VideoCapacityException() {
        super();
    }

    public VideoCapacityException(String message) {
        super(message);
    }
}
