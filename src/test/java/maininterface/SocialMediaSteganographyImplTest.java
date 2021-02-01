package maininterface;

import apis.MediaType;
import apis.SocialMedia;
import apis.models.APINames;
import org.junit.jupiter.api.BeforeEach;
import socialmediasteganography.SocialMediaSteganography;
import socialmediasteganography.SocialMediaSteganographyImpl;
import steganography.exceptions.*;
import steganography.util.ByteArrayUtils;

import java.io.File;
import java.io.IOException;

public class SocialMediaSteganographyImplTest {
    SocialMediaSteganography exampleSteganography;
    String loadFilePath = "example/picture.png";
    String keyword = "test";
    MediaType mediaType = MediaType.PNG;
    APINames apiNames = APINames.TUMBLR;
    byte[] payload = new byte[] {1,2,3,4,5,6,7,8,9,10,11,12,13};

    @BeforeEach
    public void before() throws UnsupportedMediaTypeException, MediaCapacityException, MediaNotFoundException, MediaReassemblingException, IOException, UnknownStegFormatException {
        SocialMediaSteganography steganography = new SocialMediaSteganographyImpl();
        // Encode A MediaType from File
        steganography.encodeAndPost(apiNames,keyword,loadFilePath,payload,mediaType);
        // Read the File yourself, for example using ByteUtils
        File file = new File(loadFilePath);
        byte[] testBytes = ByteArrayUtils.read(file);
        steganography.encodeAndPost(apiNames,keyword,testBytes,payload,mediaType);

        // Download the file to a given Path
        String savePath = "example/save.png";
        steganography.saveEncodePicture(loadFilePath,mediaType,payload,savePath);
        //Or with a byteArray
        steganography.saveEncodedPicture(testBytes,payload,mediaType,savePath);

        // If you want to download Dtata from social Media you can subscribe to a
        // Social Media and get the respective Class
        SocialMedia socialMedia = steganography.subscribeToSocialMedia(keyword,apiNames);

        // For just encoding
        byte[] steganographicCarrier = steganography.encodeCarrier(testBytes,payload,mediaType);
        // Only posting
        steganography.postToSocialMedia(steganographicCarrier,apiNames,keyword,mediaType);
        // For decoding
        steganography.decodeCarrier(mediaType,steganographicCarrier);
        // Add a seed for saver encoding
        steganography.setSeed(475635375L);
        steganography.encodeCarrier(testBytes,payload,mediaType);

    }
}
