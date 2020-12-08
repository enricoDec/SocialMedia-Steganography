package steganography.audio.mp3.overlays;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.util.NoSuchElementException;

public class MP3ShuffleOverlay extends MP3SequenceOverlay {

    public MP3ShuffleOverlay(byte[] bytes, long seed) throws UnsupportedAudioFileException {
        super(bytes, seed);
    }

    protected MP3ShuffleOverlay(byte[] bytes) throws UnsupportedAudioFileException {
        super(bytes);
    }

    @Override
    public byte next() throws NoSuchElementException {
        return 0;
    }

    @Override
    public int available() {
        return 0;
    }

    @Override
    public void setByte(byte value) throws NoSuchElementException {

    }

    @Override
    public byte[] getBytes() {
        return new byte[0];
    }
}
