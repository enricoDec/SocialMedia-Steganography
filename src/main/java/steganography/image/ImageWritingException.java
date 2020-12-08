package steganography.image;

import socialmediasteganography.SocialMediaSteganographyException;

/**
 * Thrown if an image could not be written successfully to an OutputStream
 */
public class ImageWritingException extends SocialMediaSteganographyException {
    public ImageWritingException() {
        super();
    }

    public ImageWritingException(String message) {
        super(message);
    }
}
