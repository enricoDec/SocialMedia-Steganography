package steganography.image.exceptions;

import socialmediasteganography.SocialMediaSteganographyException;
import steganography.exceptions.MediaReassemblingException;

/**
 * Thrown if an image could not be reassembled successfully after encoding a message in it.
 */
public class ImageWritingException extends MediaReassemblingException {
    public ImageWritingException() {
        super();
    }

    public ImageWritingException(String message) {
        super(message);
    }
}
