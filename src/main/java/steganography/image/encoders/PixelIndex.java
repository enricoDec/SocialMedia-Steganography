package steganography.image.encoders;

import steganography.image.encoders.PixelBit;
import steganography.image.overlays.BufferedImageCoordinateOverlay;


import java.util.List;
import java.util.Map;
import java.util.Random;


public class PixelIndex extends PixelBit {
    private final Map<Integer,List<Integer>> colorCouple;
    private final Random random;

    public PixelIndex(BufferedImageCoordinateOverlay overlay, Map<Integer,List<Integer>> colorCouple, long seed) throws IllegalArgumentException {
        super(overlay);
        this.colorCouple = colorCouple;
        random = new Random(seed);
    }

    @Override
    protected int changePixelValue(int pixelARGB) {
        List<Integer> color = this.colorCouple.get(pixelARGB);
        int index = random.nextInt(color.size());
        return color.get(index);
    }
}
