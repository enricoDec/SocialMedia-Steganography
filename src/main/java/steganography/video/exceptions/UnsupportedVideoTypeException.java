package steganography.video.exceptions;

import steganography.exceptions.UnsupportedMediaTypeException;

/**
 * @author : Enrico Gamil Toros
 * Project name : ProjektSteganography
 * @version : 1.0
 * @since : 05.01.21
 * <p>
 * Thrown if the used encoding is not supported
 **/
public class UnsupportedVideoTypeException extends UnsupportedMediaTypeException {

    public UnsupportedVideoTypeException() {
        super();
    }

    public UnsupportedVideoTypeException(String message) {
        super(message);
    }
}
