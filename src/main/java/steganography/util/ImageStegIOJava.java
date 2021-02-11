package steganography.util;

import steganography.image.encoders.BuffImgEncoder;
import steganography.image.encoders.GIFTableDecoder;
import steganography.image.encoders.PixelBit;
import steganography.image.encoders.PixelIndex;
import steganography.image.exceptions.ImageWritingException;
import steganography.image.exceptions.NoImageException;
import steganography.image.exceptions.UnsupportedImageTypeException;
import steganography.image.overlays.PixelCoordinateOverlay;
import steganography.image.overlays.RemoveTransparentShuffleOverlay;
import steganography.image.overlays.ShuffleOverlay;
import steganography.image.overlays.TableOverlay;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * This Class exists to handle reading and writing of BufferedImages to and from byte arrays
 * as well as choosing the appropriate encoders (and their overlays) for the given image. It holds on to the image
 * during its en- or decoding.
 */
public class ImageStegIOJava implements ImageStegIO{

    /**
     * The given input. Remains unchanged throughout.
     */
    protected final byte[] input;

    /**
     * Info on whether to use an overlay that uses transparent pixels
     */
    protected final boolean useTransparent;

    /**
     * The BufferedImage to handle the In- and Output of
     */
    private BufferedImage bufferedImage;

    /**
     * The format of the image as recognized while reading input
     */
    private String format;

    /**
     * A set of supported formats to look up
     */
    protected static final Set<String> SUPPORTED_FORMATS = new HashSet<>(
            Arrays.asList("bmp", "BMP", "gif", "GIF", "png", "PNG")
    );

    /**
     * <p>Creates an object that exists to handle reading and writing of BufferedImages to and from byte arrays
     * as well as choosing the appropriate encoders (and their overlays) for the given image. It holds on to the image
     * during its en- or decoding.</p>
     * <p>The image will only be processed if the methods getFormat() or getEncoder() are called.</p>
     * @param image the image to handle In- and Output of
     * @param useTransparent if true, returned encoders will use fully transparent pixels
     */
    public ImageStegIOJava(byte[] image, boolean useTransparent) {
        this.input = image;
        this.useTransparent = useTransparent;
    }

    private void processImage(byte[] carrier)
            throws IOException, NoImageException, UnsupportedImageTypeException {

        try(ImageInputStream imageInputStream = new MemoryCacheImageInputStream(new ByteArrayInputStream(carrier))) {
            Iterator<ImageReader> readers = ImageIO.getImageReaders(imageInputStream);

            if (readers.hasNext()) {
                ImageReader reader = readers.next();

                if (!formatSupported(reader.getFormatName()))
                    throw new UnsupportedImageTypeException(
                            "The Image format (" +
                                    reader.getFormatName() +
                                    ") is not supported."
                    );

                try {
                    reader.setInput(imageInputStream);

                    BufferedImage buffImg = reader.read(0);

                    if (reader.getFormatName().equalsIgnoreCase("bmp") && buffImg.getColorModel().hasAlpha())
                        throw new UnsupportedImageTypeException(
                                "Image format (bmp containing transparency) is not supported."
                        );

                    this.bufferedImage = buffImg;
                    this.format = reader.getFormatName();

                } finally {
                    reader.dispose();
                }
            } else {
                throw new NoImageException("No image could be read from input.");
            }
        }
    }

    private boolean formatSupported(String formatName) {
        return SUPPORTED_FORMATS.contains(formatName);
    }

    /**
     * <p>Returns the image in its current state (Output-Image) as a byte Array.</p>
     * <p>If the image was not yet processed, return == input</p>
     * @return the image in its current state as a byte array
     * @throws IOException if there was an error during writing of BufferedImage to a byte array
     * @throws ImageWritingException if the image was not written to a byte array for unknown reasons
     */
    @Override
    public byte[] getImageAsByteArray() throws IOException, ImageWritingException {
        if (this.bufferedImage == null)
            return input;

        ByteArrayOutputStream resultImage = new ByteArrayOutputStream();

        if (!ImageIO.write(this.bufferedImage, this.format, resultImage)) {
            throw new ImageWritingException("Could not write image. Unknown, internal error");
        }

        return resultImage.toByteArray();
    }

    /**
     * <p>Returns the images format.</p>
     * <p>Processes the image if necessary.</p>
     * @return the images format (png, bmp, ...) as a String
     * @throws UnsupportedImageTypeException if the image type read from input is not supported
     * @throws IOException if there was an error during reading of input
     * @throws NoImageException if no image could be read from input
     */
    @Override
    public String getFormat() throws UnsupportedImageTypeException, IOException, NoImageException {
        if (this.bufferedImage == null)
            processImage(this.input);

        return this.format;
    }

     /**
      * <p>Determines and returns the suitable encoder (and overlay) for the image according to its type.</p>
      * <p>Processes the image if it was not processed already.</p>
      * @param seed to hand to the overlay
      * @return BuffImgEncoder with set PixelCoordinateOverlay, chosen accordingly to the images type
      * @throws UnsupportedImageTypeException if the images type is not supported by any known encoder / overlay
      * @throws IOException if there was an error during reading of input
      * @throws NoImageException if no image could be read from input
      */
    @Override
    public BuffImgEncoder getEncoder(long seed)
            throws UnsupportedImageTypeException, IOException, NoImageException {
        if (this.bufferedImage == null)
            processImage(this.input);

        int type = bufferedImage.getType();

        switch (type) {

            // Types for PixelBit Algorithm
            //----------------------------------------------------------------------------------
            case BufferedImage.TYPE_4BYTE_ABGR:
            case BufferedImage.TYPE_3BYTE_BGR:
            case BufferedImage.TYPE_INT_ARGB:
            case BufferedImage.TYPE_INT_RGB:
            case BufferedImage.TYPE_INT_BGR:
            // Types that have not been tested, but should be suitable for PixelBit Algorithm
            //----------------------------------------------------------------------------------
            case BufferedImage.TYPE_4BYTE_ABGR_PRE: // could not be found or artificially created
            case BufferedImage.TYPE_INT_ARGB_PRE: // could not be found or artificially created
                return new PixelBit(getOverlay(this.bufferedImage, seed));

            // Type(s) for ColorCouple Algorithm
            //----------------------------------------------------------------------------------
            case BufferedImage.TYPE_BYTE_INDEXED:
                if (this.format.equalsIgnoreCase("png"))
                    throw new UnsupportedImageTypeException("Format PNG with type 13 is not supported.");

                GIFTableDecoder tableDecoder = new GIFTableDecoder();
                try {
                    Map<Integer, List<Integer>> colorCouple = tableDecoder.getColorCouples(
                            tableDecoder.saveColorTable(getImageAsByteArray())
                    );
                    return new PixelIndex(new TableOverlay(this.bufferedImage, seed, colorCouple), colorCouple, seed);
                } catch (IOException | ImageWritingException e) {
                    e.printStackTrace();
                }

            // Types that are not supported - explicit for completion reasons
            //----------------------------------------------------------------------------------
            case BufferedImage.TYPE_BYTE_BINARY:
            case BufferedImage.TYPE_BYTE_GRAY:
            case BufferedImage.TYPE_CUSTOM:
            case BufferedImage.TYPE_USHORT_555_RGB:
            case BufferedImage.TYPE_USHORT_565_RGB:
            case BufferedImage.TYPE_USHORT_GRAY:
            default:
                throw new UnsupportedImageTypeException("Image type (BufferedImage.TYPE = " + type + ") is not supported");
        }
    }

    /**
     * Returns an overlay according to the global variable useTransparent
     * @param bufferedImage BufferedImage to hand to overlay
     * @param seed Seed to hand to overlay
     * @return ShuffleOverlay or RemoveTransparentShuffleOverlay
     * @throws UnsupportedImageTypeException if the image type is not supported by the overlay
     */
    protected PixelCoordinateOverlay getOverlay(BufferedImage bufferedImage, long seed)
            throws UnsupportedImageTypeException {

        return this.useTransparent ?
                new ShuffleOverlay(bufferedImage, seed) :
                new RemoveTransparentShuffleOverlay(bufferedImage, seed);
    }
}
