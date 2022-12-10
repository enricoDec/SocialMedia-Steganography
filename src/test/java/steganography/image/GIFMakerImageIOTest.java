package steganography.image;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import steganography.image.exceptions.UnsupportedImageTypeException;
import steganography.util.ByteArrayUtils;

import java.io.File;
import java.io.IOException;

public class GIFMakerImageIOTest {
    byte[] animatedGif;
    private GIFMakerImageIO splicer;

    @BeforeEach
    public void before() throws IOException {
        File file = new File("src/test/resources/steganography/image/insta.gif");
        animatedGif = ByteArrayUtils.read(file);
        splicer = new GIFMakerImageIO();
    }

    @Test
    public void splitGifDecoder_correctInput_byte2Array() throws UnsupportedImageTypeException {
        byte[][] result = splicer.splitGIF(animatedGif);
        Assertions.assertEquals(27, result.length);

    }

    @Test
    public void splitGifDecoder_nullInput_NullPointerException() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            splicer.splitGIF(null);
        });
    }

    @Test
    public void splitGifDecoder_notAGIF_Exception() {
        Assertions.assertThrows(UnsupportedImageTypeException.class, () -> splicer.splitGIF(new byte[]{1, 2, 3, 4, 5,
                6, 7, 8, 9}));
    }

    @Test
    public void sequenceGifDecoder_frames_ByteArray() throws UnsupportedImageTypeException {
        byte[][] frames = splicer.splitGIF(animatedGif);
        int length = 0;
        for (int i = 0; i < frames.length; i++) {
            length = frames[i].length;
        }
        Assertions.assertTrue(length <= splicer.sequenzGIF(frames).length);
    }

    @Test
    public void sequenceGifDecoder_singeleFrame_ByteArray() throws UnsupportedImageTypeException {
        byte[][] frames = splicer.splitGIF(animatedGif);
        byte[][] test = new byte[1][];
        test[0] = frames[0];
        Assertions.assertTrue(frames[0].length <= splicer.sequenzGIF(test).length);
    }

    @Test
    public void sequenceGifDecoder_animatedGIF_ByteArray() {
        byte[][] test = new byte[1][];
        test[0] = animatedGif;

        Assertions.assertTrue(animatedGif.length <= splicer.sequenzGIF(test).length);
    }

    @Test
    public void sequenceGIFDecoder_null_NullpointerException() {
        Assertions.assertThrows(NullPointerException.class, () -> splicer.sequenzGIF(null));
    }
}
