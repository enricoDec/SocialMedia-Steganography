package steganography.audio.mp3.overlays;

import java.util.NoSuchElementException;

/**
 * The implementing classes should be able to returns data bytes of an audio file independent
 * from their order in the file.
 */
public interface AudioOverlay {
    /**
     * Returns the next modifiable byte.
     * @return Byte
     * @throws NoSuchElementException if there are no more modifiable bytes
     */
    byte next() throws NoSuchElementException;

    /**
     * Returns the number of bytes that are available to modify and have not been returned by next().
     * @return number of available bytes
     */
    int available();

    /**
     * Sets the current byte to the given value.
     * @param value the value to set the current byte to
     * @throws NoSuchElementException if setByte() is called before the first call to next(),
     * or if setByte() is called after the last call to next() produced a NoSuchElementException
     */
    void setByte(byte value) throws NoSuchElementException;

    /**
     * Returns the byte array this object holds.
     * @return byte array held by this object
     */
    byte[] getBytes();
}
