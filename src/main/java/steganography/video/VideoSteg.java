/*
 * Copyright (c) 2020
 * Contributed by Enrico de Chadarevian
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

package steganography.video;

import steganography.Steganography;
import steganography.exceptions.MediaNotFoundException;
import steganography.exceptions.UnknownStegFormatException;
import steganography.exceptions.UnsupportedMediaTypeException;
import steganography.image.*;
import steganography.image.exceptions.ImageCapacityException;
import steganography.image.exceptions.ImageWritingException;
import steganography.image.exceptions.NoImageException;
import steganography.image.exceptions.UnsupportedImageTypeException;
import steganography.util.ImageSequenceUtils;
import steganography.video.encoders.VideoDecoder;
import steganography.video.encoders.VideoEncoder;
import steganography.video.exceptions.UnsupportedVideoTypeException;
import steganography.video.exceptions.VideoCapacityException;
import steganography.video.exceptions.VideoNotFoundException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author : Enrico Gamil Toros de Chadarevian
 * Project name : ProjektStudiumSteganography
 * @version : 1.0
 * @since : 23-11-2020
 **/
public class VideoSteg implements Steganography {
    private int maxEncodingThreads = 1;
    private int maxDecodingThreads = 1;
    private boolean debug = false;
    private long startTime = System.currentTimeMillis();
    private File ffmpegBin = new File("src/main/resources");
    private final long seed = ImageSteg.DEFAULT_SEED;

    /**
     * Use {@link #setMaxEncodingThreads(int)} and {@link #setMaxDecodingThreads(int)} to enable multithreading (by default single threaded).
     * <p>
     * We also highly recommend to set call {@link javax.imageio.ImageIO#setUseCache(boolean)}.
     * This will make the decoding way faster since the images will be stored in-memory and not cached on disk (will be Memory demanding)
     */
    @Override
    public byte[] encode(byte[] carrier, byte[] payload)
            throws IOException, UnsupportedImageTypeException, NoImageException,
            ImageWritingException, ImageCapacityException, VideoCapacityException,
            VideoNotFoundException, UnsupportedVideoTypeException {
        return encode(carrier, payload, this.seed);
    }

    /**
     * Use {@link #setMaxEncodingThreads(int)} and {@link #setMaxDecodingThreads(int)} to enable multithreading (by default single threaded).
     * <p>
     * We also highly recommend to set call {@link javax.imageio.ImageIO#setUseCache(boolean)}.
     * This will make the decoding way faster since the images will be stored in-memory and not cached on disk (will be Memory demanding)
     */
    @Override
    public byte[] encode(byte[] carrier, byte[] payload, long seed)
            throws IOException, VideoCapacityException, UnsupportedImageTypeException,
            NoImageException, ImageWritingException, ImageCapacityException, VideoNotFoundException, UnsupportedVideoTypeException {
        //Decode Video to Single Frames
        Video video = new Video(carrier, ffmpegBin);
        //List used to save the single frames decoded from the carrier
        VideoDecoder videoDecoder = new VideoDecoder(video, ffmpegBin, debug);
        List<byte[]> imageList = videoDecoder.toPictureByteArray(maxDecodingThreads);
        if (debug) {
            log("Decoding Video Frames to images....");
            log("Video decoded in: " + (System.currentTimeMillis() - startTime) + "ms" + " (" + ((System.currentTimeMillis() - startTime) / 1000) + "s)");
            log("Encoding secret message into images...");
        }
        List<byte[]> stegImagesList = encodeUsingHenkAlgo(imageList, payload, seed);
        if (debug) {
            startTime = System.currentTimeMillis();
            log("All " + stegImagesList.size() + " images encoded in: " + (System.currentTimeMillis() - startTime) + "ms" + " (" + ((System.currentTimeMillis() - startTime) / 1000) + "s)");
        }
        //Encode Images to Video
        VideoEncoder videoEncoder = new VideoEncoder(video, ffmpegBin, debug);
        return videoEncoder.imagesToVideo(stegImagesList);
    }

    /**
     * Encodes a given list of byte[] of Pictures using the "Henk-Algorithm"
     *
     * @param imageList list of Pictures as byte[]
     * @param payload   payload (secret) as byte[]
     * @param seed      seed to be used for distribution
     * @return Encoded list of Pictures
     */
    private List<byte[]> encodeUsingHenkAlgo(List<byte[]> imageList, byte[] payload, long seed)
            throws IOException, VideoCapacityException, UnsupportedImageTypeException,
            NoImageException, ImageWritingException, ImageCapacityException {
        long maxVideoCapacity = getVideoCapacity(imageList);
        if (payload.length > maxVideoCapacity)
            throw new VideoCapacityException("Payload is too big for carrier. " + "Max Carrier capacity: " + maxVideoCapacity + " Bytes. "
                    + "Payload Bytes: " + payload.length);

        //Single Threaded
        if (maxEncodingThreads == 1) {
            List<byte[]> stegImageList = new ArrayList<>();
            List<byte[]> payloadChunk = ImageSequenceUtils.sequenceDistribution(imageList, payload);
            int i = 0;
            for (byte[] image : imageList) {
                if (payloadChunk.get(i) != null) {
                    ImageSteg imageSteg = new ImageSteg();
                    stegImageList.add(imageSteg.encode(image, payloadChunk.get(i), seed));
                    i++;
                } else {
                    stegImageList.add(image);
                }
            }
            return stegImageList;
        } else {
            //Multithreaded
            return multiThreadingEncode(imageList, payload, seed);
        }
    }

    /**
     * Multithreaded version of {@link #encodeUsingHenkAlgo(List, byte[], long)}
     *
     * @param imageList list of images to encode
     * @param payload   payload in byte[]
     * @param seed      seed to be used for distribution
     * @return list of encoded images
     * @throws IOException if any IO errors
     */
    private List<byte[]> multiThreadingEncode(List<byte[]> imageList, byte[] payload, long seed)
            throws IOException, NoImageException, UnsupportedImageTypeException {
        ExecutorService taskExecutor = Executors.newFixedThreadPool(maxEncodingThreads);
        List<byte[]> payloadChunk = ImageSequenceUtils.sequenceDistribution(imageList, payload);

        //Make list of callable tasks that will be run by threads
        List<Callable<byte[]>> taskList = new ArrayList<>();
        for (int i = 0; i < imageList.size(); i++) {
            int finalI = i;

            //Each thread encodes a single image and returns result
            taskList.add(() -> {
                        if (payloadChunk.get(finalI) != null) {
                            //ImageSteg is not threads safe yet, so need to make an instance for each thread
                            ImageSteg steganography = new ImageSteg();
                            return steganography.encode(imageList.get(finalI), payloadChunk.get(finalI), seed);
                        } else {
                            return imageList.get(finalI);
                        }
                    }
            );
        }

        if (debug) {
            log("All Callable tasks initialized" + System.lineSeparator() + "Running " + maxDecodingThreads + " Threads");
        }

        //Execute all tasks and add result to result list
        List<Future<byte[]>> futureList;
        List<byte[]> resultList = new ArrayList<>();
        try {
            futureList = taskExecutor.invokeAll(taskList);
            //Wait for all results
            for (Future<byte[]> result : futureList) {
                resultList.add(result.get());
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            taskExecutor.shutdown();
        }
        return resultList;
    }

    /**
     * Extract the hidden Payload from a Steganographic Video
     */
    @Override
    public byte[] decode(byte[] steganographicData)
            throws IOException, VideoNotFoundException, UnsupportedVideoTypeException {
        return decode(steganographicData, this.seed);
    }

    /**
     * Extract the hidden Payload from a Steganographic Video using a custom seed
     */
    @Override
    public byte[] decode(byte[] steganographicData, long seed)
            throws IOException, VideoNotFoundException, UnsupportedVideoTypeException {
        Video video = new Video(steganographicData, ffmpegBin);

        //Decode Video Frames to pictures
        VideoDecoder videoDecoder = new VideoDecoder(video, ffmpegBin, debug);
        List<byte[]> imageList = videoDecoder.toPictureByteArray(maxDecodingThreads);

        return decodeUsingHenkAlgo(imageList, seed);
    }

    /**
     * Decode list of images using "Henk-algorithm"
     *
     * @param imageList List of images to be decoded
     * @param seed      seed to use to decode
     * @return decoded byte[]
     * @throws IOException If any IO errors
     */
    private byte[] decodeUsingHenkAlgo(List<byte[]> imageList, long seed) throws IOException {
        if (maxEncodingThreads == 1) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int i = 0;
            for (byte[] bytes : imageList) {
                Steganography steganography = new ImageSteg();
                try {
                    byteArrayOutputStream.write(steganography.decode(bytes, seed));
                } catch (UnsupportedEncodingException | UnknownStegFormatException | MediaNotFoundException | UnsupportedMediaTypeException e) {
                    if (debug)
                        log("Decoded Frame (" + i + "/" + imageList.size() + ")");
                    return byteArrayOutputStream.toByteArray();
                }
                if (debug)
                    log("Decoded Frame (" + i + "/" + imageList.size() + ")");
                i++;
            }
            return byteArrayOutputStream.toByteArray();
        } else {
            return multiThreadingDecode(imageList, seed);
        }
    }

    /**
     * Multi threaded version of {@link #decodeUsingHenkAlgo(List, long)}
     *
     * @param imageList list of images to decode
     * @param seed      seed to be used to decode
     * @return decoded byte[]
     * @throws IOException If any IO errors
     */
    private byte[] multiThreadingDecode(List<byte[]> imageList, long seed) throws IOException {
        ExecutorService taskExecutor = Executors.newFixedThreadPool(maxDecodingThreads);

        //Make list of callable tasks that will be run by threads
        List<Callable<byte[]>> taskList = new ArrayList<>();
        for (int i = 0; i < imageList.size(); i++) {
            int finalI = i;

            //Each thread decodes a single image and returns result
            taskList.add(() -> {
                        try {
                            //ImageSteg is not threads safe yet, so need to make an instance for each thread
                            ImageSteg steganography = new ImageSteg();
                            if (debug)
                                log("Decoded Frame (" + finalI + "/" + imageList.size() + ")");
                            return steganography.decode(imageList.get(finalI), seed);
                        } catch (UnknownStegFormatException e) {
                            if (debug)
                                log("Decoded Frame (" + finalI + "/" + imageList.size() + ")");
                            return null;
                        }
                    }
            );
        }

        if (debug) {
            log("All Callable tasks initialized" + System.lineSeparator() + "Running " + maxDecodingThreads + " Threads");
        }

        //Execute all tasks
        List<Future<byte[]>> futureList;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            futureList = taskExecutor.invokeAll(taskList);
            //Wait for all results
            for (Future<byte[]> result : futureList) {
                byte[] futureByte = result.get();
                if (futureByte != null) //Empty Frame
                    byteArrayOutputStream.write(futureByte);
                else
                    System.out.println("Empty Frame");
            }
            return byteArrayOutputStream.toByteArray();

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            taskExecutor.shutdown();
        }

        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public boolean isSteganographicData(byte[] data)
            throws IOException, UnsupportedMediaTypeException, MediaNotFoundException {
        return isSteganographicData(data, ImageSteg.DEFAULT_SEED);
    }

    @Override
    public boolean isSteganographicData(byte[] data, long seed)
            throws IOException, MediaNotFoundException, UnsupportedMediaTypeException {
        //List used to save the single frames decoded from the carrier
        List<byte[]> imageList;

        //Decode Video to Single Frames
        Video video = new Video(data, ffmpegBin);
        VideoDecoder videoDecoder = new VideoDecoder(video, ffmpegBin, debug);
        imageList = videoDecoder.toPictureByteArray(maxDecodingThreads);

        boolean isSteganographicData = true;
        for (byte[] image : imageList) {
            if (!new ImageSteg().isSteganographicData(image, seed))
                isSteganographicData = false;
        }
        return isSteganographicData;
    }

    /**
     * Set the number of threads used to encode the pictures
     * WARNING very memory expensive might overflow max JVM heap
     * VM option: -Xmx might help
     * By default using 1 Threads
     * To use multithreading set maxEncodingThreads to > 1
     *
     * @param maxEncodingThreads max number of Threads used to encode
     */
    public void setMaxEncodingThreads(int maxEncodingThreads) {
        this.maxEncodingThreads = maxEncodingThreads;
    }

    /**
     * Set the number of threads used to decode the pictures
     * WARNING very memory expensive might overflow max JVM heap
     * VM option: -Xmx might help
     * By default using 1 Threads
     * To use multithreading set maxDecodingThreads to > 1
     *
     * @param maxDecodingThreads max number of Threads used to decode
     */
    public void setMaxDecodingThreads(int maxDecodingThreads) {
        this.maxDecodingThreads = maxDecodingThreads;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    /**
     * Returns the maximum number of bytes that can be encoded in the given video.
     *
     * @param carrier carrier to be used (Video)
     * @return max amount of total number of bytes that can be encoded in the carrier
     * @throws IOException if IO Exception occurs
     */
    public long getVideoCapacity(byte[] carrier)
            throws IOException, NoImageException, UnsupportedImageTypeException, VideoNotFoundException, UnsupportedVideoTypeException {
        VideoDecoder videoDecoder = new VideoDecoder(new Video(carrier, this.ffmpegBin), this.ffmpegBin, this.debug);
        List<byte[]> pictureList = videoDecoder.toPictureByteArray(maxDecodingThreads);
        ImageSteg imageSteg = new ImageSteg();

        long totalCapacity = 0;
        for (byte[] picture : pictureList) {
            totalCapacity += imageSteg.getImageCapacity(picture);
        }
        return totalCapacity;
    }

    /**
     * Returns the maximum number of bytes that can be encoded in the given video.
     *
     * @param pictureList list of pictures that will be encoded
     * @return max amount of total number of bytes that can be encoded in the carrier
     * @throws IOException if IO Exception occurs
     */
    public long getVideoCapacity(List<byte[]> pictureList)
            throws IOException, NoImageException, UnsupportedImageTypeException {
        ImageSteg imageSteg = new ImageSteg();

        long totalCapacity = 0;
        for (byte[] picture : pictureList) {
            totalCapacity += imageSteg.getImageCapacity(picture);
        }
        return totalCapacity;
    }

    /**
     * Logging
     *
     * @param message message to log
     */
    private void log(String message) {
        System.out.println(message);
    }

    public void setFfmpegBin(File ffmpegBin) {
        this.ffmpegBin = ffmpegBin;
    }

    public File getFfmpegBin() {
        return ffmpegBin;
    }
}
