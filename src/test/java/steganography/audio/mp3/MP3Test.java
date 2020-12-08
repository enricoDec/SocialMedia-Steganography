package steganography.audio.mp3;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import steganography.Steganography;
import steganography.util.ByteArrayUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class MP3Test {

    private Steganography mp3Steg;
    private String pathToTestFile;

    @BeforeEach
    public void setSteganography() {
        this.mp3Steg = new MP3Steganography();
        this.pathToTestFile = "src/test/resources/steganography/audio/mp3/testFile.mp3";
    }

    @Test
    public void encodeSimpleStringInMp3_ExpectSameStringAfterDecode() throws IOException {
        // read bytes
        byte[] mp3Bytes = ByteArrayUtils.read(new File(this.pathToTestFile));

        // create message byte array
        String message = "Hello World!";
        byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);

        // encode message
        byte[] encodedBytes = this.mp3Steg.encode(mp3Bytes, messageBytes);  // TODO starts at byte 49

        // read message
        byte[] decodedMessageBytes = this.mp3Steg.decode(encodedBytes);     // TODO starts at byte 257
        String decodedMessage = new String(decodedMessageBytes, StandardCharsets.UTF_8);

        // assert messages are the same
        Assertions.assertEquals(message, decodedMessage);
    }
}
