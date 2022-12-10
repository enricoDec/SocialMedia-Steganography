package steganography.image;

import steganography.image.exceptions.UnsupportedImageTypeException;

/**
 * The class splits an animated gif into several single frame gifs or vice versa
 *
 * @author Selina Wernike
 */
public interface IGIFMaker {

    /**
     * Splits the GIF into singular Frames
     *
     * @param animatedGIF GIF as an byte array
     * @return byte[][] Array containing each frame as an byte array
     * @throws UnsupportedImageTypeException thrown if image isn,t an GIF Image or has a global color table
     */
    byte[][] splitGIF(byte[] animatedGIF) throws UnsupportedImageTypeException;

    /**
     * Sequenzes several frames to a single GIF
     *
     * @param framesGIF gif frames as byte arrays
     * @return byte[] a single gif as an byte array
     */
    byte[] sequenzGIF(byte[][] framesGIF);
}
