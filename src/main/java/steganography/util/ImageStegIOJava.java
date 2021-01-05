package steganography.util;

import steganography.image.ImageSteg;
import steganography.image.encoders.BuffImgEncoder;
import steganography.image.encoders.GIFTableDecoder;
import steganography.image.encoders.PixelBit;
import steganography.image.encoders.PixelIndex;
import steganography.image.exceptions.ImageWritingException;
import steganography.image.exceptions.NoImageException;
import steganography.image.exceptions.UnsupportedImageTypeException;
import steganography.image.overlays.BufferedImageCoordinateOverlay;
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

public class ImageStegIOJava implements ImageStegIO{

    private final byte[] input;
    private final boolean useTransparent;

    private BufferedImage bufferedImage;

    private String format;

    private static final Set<String> SUPPORTED_FORMATS = new HashSet<>(
            Arrays.asList("bmp", "BMP", "gif", "GIF", "png", "PNG")
    );

    public ImageStegIOJava(byte[] image)
            throws UnsupportedImageTypeException, IOException, NoImageException {

        this.input = image;
        this.useTransparent = false;
        processImage(this.input);
    }

    public ImageStegIOJava(byte[] image, boolean useTransparent)
            throws UnsupportedImageTypeException, IOException, NoImageException {

        this.input = image;
        this.useTransparent = useTransparent;
        processImage(this.input);
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

    @Override
    public String getFormat() {
        return this.format;
    }

     /**
     * Determines and returns the suitable encoder (and overlay) for the given bufferedImage according to its type.
     * @param seed to hand to the overlay
     * @return BuffImgEncoder with set BufferedImageCoordinateOverlay, chosen accordingly to the images type
     * @throws UnsupportedImageTypeException if the images type is not supported by any known encoder / overlay
     */
    @Override
    public BuffImgEncoder getEncoder(long seed)
            throws UnsupportedImageTypeException {

        int type = bufferedImage.getType();

        switch (type) {

            // Types for PixelBit Algorithm
            //----------------------------------------------------------------------------------
            case BufferedImage.TYPE_4BYTE_ABGR:
            case BufferedImage.TYPE_3BYTE_BGR:
            case BufferedImage.TYPE_INT_ARGB:
            case BufferedImage.TYPE_INT_RGB:
            case BufferedImage.TYPE_INT_BGR:
                return new PixelBit(getOverlay(this.bufferedImage, seed));

            // Type(s) for ColorCouple Algorithm
            //----------------------------------------------------------------------------------
            case BufferedImage.TYPE_BYTE_INDEXED:
                GIFTableDecoder tableDecoder = new GIFTableDecoder();
                try {
                    Map<Integer, List<Integer>> colorCouple = tableDecoder.getColorCouples(
                            tableDecoder.saveColorTable(getImageAsByteArray())
                    );
                    return new PixelIndex(new TableOverlay(this.bufferedImage, seed, colorCouple), colorCouple, seed);
                } catch (IOException | ImageWritingException e) {
                    e.printStackTrace();
                }
                // return overlay8Bit

                // Types that have not been tested, but are probably suitable for PixelBit Algorithm
                //----------------------------------------------------------------------------------
            case BufferedImage.TYPE_4BYTE_ABGR_PRE:
            case BufferedImage.TYPE_INT_ARGB_PRE:
                // TODO: Test those types (could not find them)
                throw new UnsupportedImageTypeException("Image type is not supported because untested.");

                // Types that will (probably) not be supported - explicit for completion reasons
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
     * Returns overlay according to global variable useTransparent
     * @param bufferedImage BufferedImage to hand to overlay
     * @param seed Seed to hand to overlay
     * @return ShuffleOverlay or RemoveTransparentShuffleOverlay
     * @throws UnsupportedImageTypeException if the image type is not supported by the overlay
     */
    private BufferedImageCoordinateOverlay getOverlay(BufferedImage bufferedImage, long seed)
            throws UnsupportedImageTypeException {

        return this.useTransparent ?
                new ShuffleOverlay(bufferedImage, seed) :
                new RemoveTransparentShuffleOverlay(bufferedImage, seed);
    }
}
