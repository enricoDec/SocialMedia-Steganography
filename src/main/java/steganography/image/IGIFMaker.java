package steganography.image;

import steganography.image.exceptions.UnsupportedImageTypeException;
/**
 * @author Selina Wernike
 * The class splits an animated gif into several single frame gifs or vice versa
 */
public interface IGIFMaker {

    public byte[][] splitGIF(byte[] animatedGIF) throws UnsupportedImageTypeException;
    public byte[] sequenzGIF(byte[][] framesGIF);
}
