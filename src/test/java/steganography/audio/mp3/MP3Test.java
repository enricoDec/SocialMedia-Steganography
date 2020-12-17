package steganography.audio.mp3;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import socialmediasteganography.SocialMediaSteganographyException;
import steganography.Steganography;
import steganography.audio.overlays.MP3Overlays;
import steganography.util.ByteArrayUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class MP3Test {

    private Steganography mp3Steg;
    private String pathToTestFile;

    @BeforeEach
    public void setSteganography() {
        this.mp3Steg = new MP3Steganography(MP3Overlays.SEQUENCE_OVERLAY);
        this.pathToTestFile = "src/test/resources/steganography/audio/mp3/testFile.mp3";
    }

    @Test
    public void encodeSimpleStringIntoByteArray_ExpectSameStringAfterDecode()
            throws IOException, SocialMediaSteganographyException {
        // read bytes
        byte[] mp3Bytes = ByteArrayUtils.read(new File(this.pathToTestFile));

        // create message byte array
        String message = "Hello World!";
        byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);

        // encode message
        byte[] encodedBytes = this.mp3Steg.encode(mp3Bytes, messageBytes);

        // read message
        byte[] decodedMessageBytes = this.mp3Steg.decode(encodedBytes);
        String decodedMessage = new String(decodedMessageBytes, StandardCharsets.UTF_8);

        // assert messages are the same
        Assertions.assertEquals(message, decodedMessage);
    }

    @Test
    public void encodeSimpleStringIntoFile_ExpectSameStringAfterDecode()
            throws IOException, SocialMediaSteganographyException {
        String pathToEncodedFile = "src/test/resources/steganography/audio/mp3/testFileEncoded.mp3";

        // read bytes
        byte[] mp3Bytes = ByteArrayUtils.read(new File(this.pathToTestFile));

        // create message byte array
        String message = "Hello World!";
        byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);

        // encode message
        byte[] encodedBytes = this.mp3Steg.encode(mp3Bytes, messageBytes);

        // to file
        File testFileEncoded = new File(pathToEncodedFile);
        Files.write(testFileEncoded.toPath(), encodedBytes);

        // read bytes and delete file
        byte[] encodedBytesFromFile = ByteArrayUtils.read(testFileEncoded);
        testFileEncoded.delete();

        // decode message
        byte[] messageFromFile = this.mp3Steg.decode(encodedBytesFromFile);
        String decodedMessage = new String(messageFromFile, StandardCharsets.UTF_8);

        // assert messages are the same
        Assertions.assertEquals(message, decodedMessage);
    }
}
