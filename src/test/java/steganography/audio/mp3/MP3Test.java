package steganography.audio.mp3;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import socialmediasteganography.SocialMediaSteganographyException;
import steganography.Steganography;
import steganography.image.ImageWritingException;
import steganography.image.NoImageException;
import steganography.image.UnknownStegFormatException;
import steganography.image.UnsupportedImageTypeException;
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
    public void encode2Characters_Expect80Changes() throws IOException, SocialMediaSteganographyException {
        // read bytes
        byte[] mp3Bytes = ByteArrayUtils.read(new File(this.pathToTestFile));
        byte[] encodedBytes = this.mp3Steg.encode(mp3Bytes, "AA".getBytes(StandardCharsets.UTF_8));
        for (int i = 0; i < encodedBytes.length; i++) {
            int counter = 0;
            if (encodedBytes[i] != mp3Bytes[i])
                counter++;
            Assertions.assertNotEquals(mp3Bytes, encodedBytes);
            Assertions.assertEquals(80, counter);
        }
    }

    @Test
    public void encodeSimpleStringInMp3_ExpectSameStringAfterDecode() throws IOException, SocialMediaSteganographyException {
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
