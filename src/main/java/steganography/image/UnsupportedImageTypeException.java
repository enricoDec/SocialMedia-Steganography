package steganography.image;

import socialmediasteganography.SocialMediaSteganographyException;

/**
 * An Exception of this class is thrown when an operation was called concerning an image that is not
 * suitable for the type of image.
 */
public class UnsupportedImageTypeException extends SocialMediaSteganographyException {

    public UnsupportedImageTypeException() {
        super();
    }

    public UnsupportedImageTypeException(String message) {
        super(message);
    }
}
