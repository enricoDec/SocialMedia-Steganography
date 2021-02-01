package steganography.image;

import steganography.image.encoders.GifDecoder;
import steganography.image.exceptions.UnsupportedImageTypeException;
import steganography.util.ByteArrayUtils;

import javax.imageio.*;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class GIFMakerImageIO implements IGIFMaker{
    int[] delay;
    IIOMetadata[] metadataForImages;
    private static String  path = "src/main/resources/";

    /**
     *
     * @param animatedGIF a byte array with the animated gif, that needs to be splitted
     * @return byte[][] output an two dimensional array which contains all gif data
     * @throws UnsupportedImageTypeException When image is not a gif
     */
    @Override
    public byte[][] splitGIF(byte[] animatedGIF) throws UnsupportedImageTypeException {
        try {
            GifDecoder.GifImage gif = GifDecoder.read(animatedGIF);
            int nop = gif.getFrameCount();
            delay = new int[nop];
            byte[][] output = new byte[nop][];
            for (int i = 0; i < nop; i++) {
                delay[i] = gif.getDelay(i);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                BufferedImage image = gif.getFrame(i);
                ImageIO.write(image, "GIF", bos);
                output[i] = bos.toByteArray();

            }
            return output;
        } catch (IOException e) {
            throw new UnsupportedImageTypeException("This method only supports gif files");
        }
    }

    /**
     * Creates a GIF from several single gif. If a delay exists it will be set in the Metadata
     * @param framesGIF Single Gif images
     * @return byte[] Single GIF that loops
     */
    @Override
    public byte[] sequenzGIF(byte[][] framesGIF) {
        ImageWriter writer = ImageIO.getImageWritersByFormatName("gif").next();
        ImageWriteParam param = writer.getDefaultWriteParam();
        ImageReader reader = ImageIO.getImageReadersByFormatName("gif").next();
        try(ImageOutputStream out = new FileImageOutputStream(new File(path + "sequenz.gif"));) {

            writer.setOutput(out);
            writer.prepareWriteSequence(null);

            for (int i = 0; i < framesGIF.length; i++) {
                ByteArrayInputStream readInput = new ByteArrayInputStream(framesGIF[i]);
                BufferedImage bufferedImage = ImageIO.read(readInput);
                ImageTypeSpecifier specifier = ImageTypeSpecifier.createFromBufferedImageType(bufferedImage.getType());
                IIOMetadata newMetadata = writer.getDefaultImageMetadata(specifier,param);

                ByteArrayInputStream ioInput = new ByteArrayInputStream(framesGIF[i]);
                ImageInputStream ciis = ImageIO.createImageInputStream(ioInput);
                reader.setInput(ciis, false);
                IIOImage frame = reader.readAll(0,null);
                if (delay != null) {
                    createMetadata(delay[i], frame, newMetadata);
                    writer.writeToSequence(new IIOImage(bufferedImage, null, newMetadata), param);
                } else
                {
                    writer.writeToSequence(frame,param);
                }

            }
            writer.endWriteSequence();
            return ByteArrayUtils.read(new File(path + "sequenz.gif"));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
        return null;
    }

    /**
     * Creates Metadata for new sequenzed GIF. Uses the Metadata from the Single GIF as base
     * @param delay The delay of the Image in the GIF
     * @param gifFrame The current Frame for which Metadata needs to be created
     * @param newMetadata The metadata that is created
     * @throws IIOInvalidTreeException When metadata can't be correctly created
     */
    private  void createMetadata(int delay, IIOImage gifFrame, IIOMetadata newMetadata) throws IIOInvalidTreeException {
        IIOMetadata metadata = gifFrame.getMetadata();
        String name = metadata.getNativeMetadataFormatName();
        IIOMetadataNode root = (IIOMetadataNode) metadata.getAsTree(name);

        IIOMetadataNode graphicsControlExtensionNote = getNode(root,"GraphicControlExtension");
        graphicsControlExtensionNote.setAttribute("delayTime", Integer.toString(delay));
        IIOMetadataNode child = new IIOMetadataNode("ApplicationExtension");
        IIOMetadataNode appExtensionsNode = getNode(root, "ApplicationExtensions");
        child.setAttribute("applicationID", "NETSCAPE");
        child.setAttribute("authenticationCode", "2.0");
        child.setUserObject(new byte[] { 0x1, (byte) (0 & 0xFF), (byte) ((0 >> 8) & 0xFF)});
        appExtensionsNode.appendChild(child);

        newMetadata.setFromTree(name, root);
    }

    /**
     * Gets a Node from root by name
     * @param root Root Node
     * @param nodeName Name of the Node that is searched for.
     * @return IIOMetadataNode Node with given Name, that is part (Might delete)
     */
    private static IIOMetadataNode getNode(IIOMetadataNode root, String nodeName) {
        int nNodes = root.getLength();
        for (int i = 0; i < nNodes; i++) {
            if(root.item(i).getNodeName().equalsIgnoreCase(nodeName)) {
                return (IIOMetadataNode) root.item(i);
            }
        }
        IIOMetadataNode node = new IIOMetadataNode(nodeName);
        root.appendChild(node);
        return (node);
    }

    @Deprecated
    public byte[][] splitGifImageIO(byte[] animatedGif) throws IOException {
        ImageReader reader = ImageIO.getImageReadersByFormatName("gif").next();
        ByteArrayInputStream input = new ByteArrayInputStream(animatedGif);
        ImageInputStream ciis = ImageIO.createImageInputStream(input);
        reader.setInput(ciis, false);
        int nop = reader.getNumImages(true);
        metadataForImages = new IIOMetadata[nop];
        byte[][] output = new byte[nop][];


        for (int i = 0; i < nop; i++) {
            BufferedImage image = reader.read(i);
            IIOMetadata metadata = reader.getImageMetadata(i);
            metadataForImages[i] = metadata;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(image, "GIF", bos);
            output[i] = bos.toByteArray();
        }
        return output;
    }

    @Deprecated
    public byte[] sequenzGIFImageIO(byte[][] gifs)  {
        ImageWriter writer = (ImageWriter)ImageIO.getImageWritersByFormatName("gif").next();
        ImageWriteParam param = writer.getDefaultWriteParam();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int nop = gifs.length;
        try {


            ImageOutputStream out = ImageIO.createImageOutputStream(bos);
            writer.setOutput(out);
            writer.prepareWriteSequence(null);
            for (int i = 0; i < nop; i++) {
                ByteArrayInputStream input = new ByteArrayInputStream(gifs[i]);
                BufferedImage next = ImageIO.read(input);
                writer.writeToSequence(new IIOImage(next,null,metadataForImages[i]),param);

            }
            writer.endWriteSequence();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bos.toByteArray();
    }
}
