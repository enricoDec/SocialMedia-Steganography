package steganography.image;

import socialmediasteganography.SocialMediaSteganographyException;

/**
 * An Exception of this class is thrown when the attempt to read an image out of a medium failed.
 */
public class NoImageException extends SocialMediaSteganographyException {

    public NoImageException() {
        super();
    }

    public NoImageException(String message) {
        super(message);
    }

}
