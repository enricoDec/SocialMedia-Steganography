package steganography.audio.mp3.overlays;

/**
 * This enum contains every overlay that can be used to encode or decode messages into or from MP3 files.
 */
public enum MP3Overlays {
    /**
     * Goes through MP3 files and reads/writes from/to data bytes in order
     */
    SEQUENCE_OVERLAY,

    /**
     * Goes through MP3 files and reads/writes from/to shuffled data bytes according to a seed
     */
    SHUFFLE_OVERLAY
}
