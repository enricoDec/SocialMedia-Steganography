package steganography.image.exceptions;

import steganography.exceptions.MediaCapacityException;

public class ImageCapacityException extends MediaCapacityException {

    public ImageCapacityException() {
        super();
    }

    public ImageCapacityException(String message) {
        super(message);
    }
}
