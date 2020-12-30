/*
 * Copyright (c) 2020
 * Contributed by Selina Wernike
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * @author Selina Wernike
 * This Klass will Split an animated gif into individual frames and vice versa
 * https://stackoverflow.com/questions/8933893/convert-each-animated-gif-frame-to-a-separate-bufferedimage
 */
package steganography.image;

import steganography.Steganography;
import steganography.exceptions.*;
import steganography.image.encoders.GifDecoder;
import steganography.image.exceptions.NoImageException;
import steganography.image.exceptions.UnsupportedImageTypeException;
import steganography.util.ByteArrayUtils;
import steganography.util.ImageSequenceUtils;

import javax.imageio.*;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Selina Wernike
 * The class splits an animated gif into several single frame gifs or vice versa
 */
public class AnimatedGif implements Steganography{
        private static String  path = "src/main/resources/";
        private static IIOMetadata[] metadataArray;
        private static int noi = 0;
        private int[] delay;
        private int nop = 0;
        private IIOMetadata[] metadataForImages;


    @Override
    public byte[] encode(byte[] carrier, byte[] payload) throws IOException, MediaNotFoundException, UnsupportedMediaTypeException, MediaReassemblingException, MediaCapacityException {
        return encode(payload,carrier, ImageSteg.DEFAULT_SEED);
    }

    @Override
        public byte[] encode(byte[] payload, byte[] animatedGif, long seed) throws IOException, MediaNotFoundException, UnsupportedMediaTypeException, MediaReassemblingException, MediaCapacityException {
            Steganography steg = new ImageSteg();
            if (animatedGif != null && payload != null) {
                byte[][] gifFrames = splitGifDecoder(animatedGif);
                List<byte[]> frames = Arrays.asList(gifFrames);
                List<byte[]> payloads = ImageSequenceUtils.sequenceDistribution(frames,payload);
                for(int i = 0; i < payloads.size();i++) {
                    if(payloads.get(i) != null) {
                        gifFrames[i] = steg.encode(gifFrames[i], payloads.get(i), seed);
                    }

                }
                return sequenceGifDecoder(gifFrames);
            }
            throw new NullPointerException("Image or payload are null");
        }

    @Override
    public byte[] decode(byte[] steganographicData) throws IOException, MediaNotFoundException, UnsupportedMediaTypeException, UnknownStegFormatException {
        return decode(steganographicData, ImageSteg.DEFAULT_SEED);
    }

    @Override
        public byte[] decode(byte[] stegGif, long seed) throws UnsupportedImageTypeException, NoImageException, IOException {
            ImageSteg steg = new ImageSteg();
            byte[][] gifFrames = splitGifDecoder(stegGif);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try {


                for (byte[] frame : gifFrames) {

                    byte[] decoded = steg.decode(frame, seed);
                    if (decoded != null && decoded.length >= 1) {
                        bos.write(decoded);
                    }
                }

                return bos.toByteArray();
            } catch (UnknownStegFormatException e) {
                return bos.toByteArray();
            } finally {
                bos.close();
            }
        }

    /**
     *
     * @param animatedGif a byte array with the animated gif, that needs to be splitted
     * @return {byte[][]} output an two dimensional array which contains all gif data
     * @throws IOException
     */
        public byte[][] splitGif(byte[] animatedGif) throws IOException {
            ImageReader reader = (ImageReader)ImageIO.getImageReadersByFormatName("gif").next();
            ByteArrayInputStream input = new ByteArrayInputStream(animatedGif);
            ImageInputStream ciis = ImageIO.createImageInputStream(input);
            reader.setInput(ciis, false);
            nop = reader.getNumImages(true);
            metadataForImages = new IIOMetadata[nop];
            byte[][] output = new byte[nop][];
            BufferedImage master = null;

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

        public byte[][] splitGifDecoder(byte[] animatedGif) throws NullPointerException, UnsupportedImageTypeException {
            try {
                GifDecoder.GifImage gif = GifDecoder.read(animatedGif);
                nop = gif.getFrameCount();
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

        public byte[] sequenceGifDecoder(byte[][] gifs) {
            ImageWriter writer = (ImageWriter)ImageIO.getImageWritersByFormatName("gif").next();
            ImageWriteParam param = writer.getDefaultWriteParam();
            ImageReader reader = (ImageReader)ImageIO.getImageReadersByFormatName("gif").next();
            FileInputStream in;
            try( ByteArrayOutputStream bos = new ByteArrayOutputStream();ImageOutputStream out = new FileImageOutputStream(new File(path + "sequenz.gif"));) {

                writer.setOutput(out);
                writer.prepareWriteSequence(null);

                for (int i = 0; i < gifs.length; i++) {
                    ByteArrayInputStream readInput = new ByteArrayInputStream(gifs[i]);
                    BufferedImage bufferedImage = ImageIO.read(readInput);
                    ImageTypeSpecifier specifier = ImageTypeSpecifier.createFromBufferedImageType(bufferedImage.getType());
                    IIOMetadata newMetadata = writer.getDefaultImageMetadata(specifier,param);
                    ByteArrayInputStream ioInput = new ByteArrayInputStream(gifs[i]);
                    ImageInputStream ciis = ImageIO.createImageInputStream(ioInput);
                    reader.setInput(ciis, false);
                    IIOImage frame = reader.readAll(0,null);
                    if (delay != null) {
                        newMetadata = createMetadata(delay[i], frame, newMetadata);
                        // Cannot change Metadata since its read-only at the moment.
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

        private  IIOMetadata createMetadata(int delay, IIOImage gifFrame, IIOMetadata newMetadata) throws IIOInvalidTreeException {
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
            return newMetadata;
        }

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

        public byte[] sequenzGIF(byte[][] gifs)  {
            ImageWriter writer = (ImageWriter)ImageIO.getImageWritersByFormatName("gif").next();
            ImageWriteParam param = writer.getDefaultWriteParam();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
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
    public static void main(String[] args) {
            AnimatedGif giffer = new AnimatedGif();
            File input = new File("src/test/resources/steganography/image/insta.gif");
            File payloadFile = new File(path + "test.txt");
            BufferedImage payloadPicture;
        try(FileOutputStream out = new FileOutputStream(new File(path + "steg.txt"));) {
            byte[] gif = ByteArrayUtils.read(input);
            byte[] payload = ByteArrayUtils.read(payloadFile);
            byte[] hiddenGif = giffer.encode(gif, payload);
            byte[] message = giffer.decode(hiddenGif);
           out.write(message);

        } catch (IOException | MediaNotFoundException | UnsupportedMediaTypeException | MediaReassemblingException | MediaCapacityException e) {
            e.printStackTrace();
        } catch (UnknownStegFormatException e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean isSteganographicData(byte[] data) throws IOException, MediaNotFoundException, UnsupportedMediaTypeException {
        return isSteganographicData(data, ImageSteg.DEFAULT_SEED);
    }

    @Override
    public boolean isSteganographicData(byte[] data, long seed) throws IOException, MediaNotFoundException, UnsupportedMediaTypeException {
        return false;
    }
}
