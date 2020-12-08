package steganography.image;

import socialmediasteganography.SocialMediaSteganographyException;

/**
 * Thrown if the format of a steganographicly encoded (hidden) message cannot be determined and therefore
 * the message cannot be decoded. The same applies, if there is no hidden message.
 */
public class UnknownStegFormatException extends SocialMediaSteganographyException {

    public UnknownStegFormatException() {
        super();
    }

    public UnknownStegFormatException(String message) {
        super(message);
    }
}
