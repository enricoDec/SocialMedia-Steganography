package steganography.audio.mp3.overlays;

import steganography.audio.mp3.MP3File;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * This class returns the data bytes of an MP3 file in order.
 */
public class MP3SequenceOverlay implements AudioOverlay {

    private final byte[] MP3_BYTES;
    private List<Integer> dataByteOrder;
    private int currentPosition = -1;

    /**
     * Adds a sequence overlay to a given MP3 file. This overlay retrieves only the data bytes of the MP3 file and
     * returns the bytes in order.
     * @param bytes byte array containing an MP§ file
     * @param seed would normally be used to influence the overlay (e.g. shuffling).
     *             Obviously, this cannot be used in the a sequence overlay.
     * @throws UnsupportedAudioFileException if the given byte array does not contain an MP3 file
     */
    public MP3SequenceOverlay(byte[] bytes, long seed) throws UnsupportedAudioFileException {
        this(bytes);
    }

    protected MP3SequenceOverlay(byte[] bytes) throws UnsupportedAudioFileException {
        MP3File mp3File = new MP3File(bytes);
        if (!mp3File.findAllFrames())
            throw new UnsupportedAudioFileException("The given byte array is not a valid MP3 file.");
        this.MP3_BYTES = mp3File.getMP3Bytes();
        createOverlay(mp3File);
    }

    protected void createOverlay(MP3File mp3File) {
        this.dataByteOrder = mp3File.getModifiablePositions();
    }

    @Override
    public byte next() throws NoSuchElementException {
        if (++this.currentPosition >= this.dataByteOrder.size())
            throw new NoSuchElementException("No more bytes left.");

        return this.MP3_BYTES[this.dataByteOrder.get(this.currentPosition)];
    }

    @Override
    public int available() {
        return this.dataByteOrder.size() - this.currentPosition - 1;
    }

    @Override
    public void setByte(byte value) throws NoSuchElementException {
        this.MP3_BYTES[this.dataByteOrder.get(this.currentPosition)] = value;
    }

    @Override
    public byte[] getBytes() {
        return this.MP3_BYTES;
    }
}
