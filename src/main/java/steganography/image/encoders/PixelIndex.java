package steganography.image.encoders;

import steganography.image.overlays.PixelCoordinateOverlay;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * The Pixel-Bit-Algorithm for Images using the Color-Couple-Algorithem
 *
 * @author Henk-Joas Lubig, Selina Wernike
 */
public class PixelIndex extends PixelBit {
    private final Map<Integer, List<Integer>> colorCouple;
    private final Random random;

    public PixelIndex(PixelCoordinateOverlay overlay, Map<Integer, List<Integer>> colorCouple, long seed) throws IllegalArgumentException {
        super(overlay);
        this.colorCouple = colorCouple;
        random = new Random(seed);
    }

    /**
     * Returns a new Color based on the Color Couple in the Map
     *
     * @param pixelARGB the pixelValue to change
     * @return int The new Color for that pixel
     */
    @Override
    protected int changePixelValue(int pixelARGB) {
        List<Integer> color = this.colorCouple.get(pixelARGB);
        int index = random.nextInt(color.size());
        return color.get(index);
    }
}
