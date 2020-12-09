package steganography.image.exceptions;

import steganography.exceptions.MediaNotFoundException;

/**
 * Thrown if the attempt to read an image failed.
 */
public class NoImageException extends MediaNotFoundException {

    public NoImageException() {
        super();
    }

    public NoImageException(String message) {
        super(message);
    }

}
