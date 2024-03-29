package steganography.exceptions;

import socialmediasteganography.SocialMediaSteganographyException;

/**
 * Thrown if the medias capacity is too small to fit the message to hide
 */
public class MediaCapacityException extends SocialMediaSteganographyException {

    public MediaCapacityException() {
        super();
    }

    public MediaCapacityException(String message) {
        super(message);
    }
}
