package steganography.util;

import steganography.image.encoders.BuffImgEncoder;
import steganography.image.exceptions.ImageWritingException;
import steganography.image.exceptions.NoImageException;
import steganography.image.exceptions.UnsupportedImageTypeException;

import java.io.IOException;

public interface ImageStegIO {

    /**
     * Returns the image in its current state (Output-Image) as a Byte Array
     */
    byte[] getImageAsByteArray() throws IOException, ImageWritingException;

    /**
     * Returns the format of the image
     */
    String getFormat() throws UnsupportedImageTypeException, IOException, NoImageException;

    /**
     * Returns an appropriate encoder to encode the image with
     */
    BuffImgEncoder getEncoder(long seed) throws UnsupportedImageTypeException;

}
