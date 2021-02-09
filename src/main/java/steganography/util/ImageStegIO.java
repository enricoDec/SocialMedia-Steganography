package steganography.util;

import steganography.image.encoders.BuffImgEncoder;
import steganography.image.exceptions.ImageWritingException;
import steganography.image.exceptions.NoImageException;
import steganography.image.exceptions.UnsupportedImageTypeException;

import java.io.IOException;

/**
 * Classes which implement this interface exist to handle reading and writing of images to and from byte arrays
 * as well as choosing the appropriate encoders (and their overlays) for the given image. They hold on to the image
 * during its en- or decoding.
 */
public interface ImageStegIO {

    /**
     * <p>Returns the image in its current state (Output-Image) as a byte Array.</p>
     * @return the image in its current state as a byte array
     * @throws IOException if there was an error during writing of the image representation to a byte array
     * @throws ImageWritingException if the image was not written to a byte array for unknown reasons
     */
    byte[] getImageAsByteArray() throws IOException, ImageWritingException;

    /**
     * <p>Returns the images format.</p>
     * @return the images format (png, bmp, ...) as a String
     * @throws UnsupportedImageTypeException if the image type read from input is not supported
     * @throws IOException if there was an error during reading of input
     * @throws NoImageException if no image could be read from input
     */
    String getFormat() throws UnsupportedImageTypeException, IOException, NoImageException;

    /**
     * <p>Determines and returns the suitable encoder (and overlay) for the image according to its type.</p>
     * @param seed to hand to the overlay
     * @return BuffImgEncoder with set PixelCoordinateOverlay, chosen accordingly to the images type
     * @throws UnsupportedImageTypeException if the images type is not supported by any known encoder / overlay
     * @throws IOException if there was an error during reading of input
     * @throws NoImageException if no image could be read from input
     */
    BuffImgEncoder getEncoder(long seed) throws UnsupportedImageTypeException, IOException, NoImageException;

}
