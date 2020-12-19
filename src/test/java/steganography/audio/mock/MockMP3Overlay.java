package steganography.audio.mock;

import steganography.audio.overlays.AudioOverlay;

import java.util.NoSuchElementException;

public class MockMP3Overlay implements AudioOverlay {

    private final byte[] bytes;
    private int currentByte = -1;

    public MockMP3Overlay(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public byte next() throws NoSuchElementException {
        if (++this.currentByte >= this.bytes.length)
            throw new NoSuchElementException("No more bytes left.");

        return this.bytes[this.currentByte];
    }

    @Override
    public int available() {
        return this.bytes.length - this.currentByte - 1;
    }

    @Override
    public void setByte(byte value) throws NoSuchElementException {
        if (this.currentByte == -1 || this.currentByte >= this.bytes.length)
            throw new NoSuchElementException("Current position is invalid");

        this.bytes[this.currentByte] = value;
    }

    @Override
    public byte[] getBytes() {
        return this.bytes;
    }
}
