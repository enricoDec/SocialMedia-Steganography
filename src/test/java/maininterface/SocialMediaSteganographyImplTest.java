package maininterface;

import apis.MediaType;
import apis.SocialMedia;
import apis.models.APINames;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import socialmediasteganography.SocialMediaSteganography;
import socialmediasteganography.SocialMediaSteganographyImpl;
import steganography.exceptions.*;
import steganography.image.ImageSteg;
import steganography.util.ByteArrayUtils;


import java.io.File;
import java.io.IOException;

public class SocialMediaSteganographyImplTest {
    SocialMediaSteganography exampleSteganography;
    String loadFilePath = "src/test/resources/steganography/image/baum.png";
    String keyword = "test";
    MediaType mediaType = MediaType.PNG;
    APINames apiNames = APINames.TUMBLR;
    byte[] payload = new byte[] {1,2,3,4,5,6,7,8,9,10,11,12,13};
    byte[] carrier;

    @BeforeEach
    public void before() throws IOException {
        exampleSteganography = new SocialMediaSteganographyImpl();
        carrier = ByteArrayUtils.read(new File(loadFilePath));

    }

    @Test
    public void encodeCarrierCorrectInputByteArray() throws UnsupportedMediaTypeException, MediaNotFoundException, MediaCapacityException, MediaReassemblingException, IOException {
        byte[] test = exampleSteganography.encodeCarrier(carrier,payload,mediaType);
        Assertions.assertTrue(carrier.length <= test.length);
    }

    @Test
    public void encodeCarrierCarrierNullException() throws UnsupportedMediaTypeException, MediaNotFoundException, MediaCapacityException, MediaReassemblingException, IOException {
        Assertions.assertThrows(NullPointerException.class,() -> {
            exampleSteganography.encodeCarrier(null,payload,mediaType);
        });
    }
    @Test
    public void encodeCarrierMediaTypeNullException() throws UnsupportedMediaTypeException, MediaNotFoundException, MediaCapacityException, MediaReassemblingException, IOException {
        Assertions.assertThrows(NullPointerException.class,() -> {
            exampleSteganography.encodeCarrier(carrier,payload,null);
        });
    }

    @Test
    public void encodeCarrierpayloadNullByteArray() throws UnsupportedMediaTypeException, MediaNotFoundException, MediaCapacityException, MediaReassemblingException, IOException {
        byte[] test = exampleSteganography.encodeCarrier(carrier,payload,mediaType);
        Assertions.assertTrue(carrier.length <= test.length);
    }

    @Test
    public void encodeCarrierpayloadToLongNull() throws UnsupportedMediaTypeException, MediaNotFoundException, MediaCapacityException, MediaReassemblingException, IOException {
        byte[] test = exampleSteganography.encodeCarrier(carrier,payload,mediaType);
    }


    public void postToSocialMediaCorrectInputTrue() {
        Assertions.assertTrue(exampleSteganography.postToSocialMedia(carrier, apiNames,keyword,mediaType));
    }

    @Test
    public void postToSocialMediacarrierNullNullPointer() {
        Assertions.assertThrows(NullPointerException.class,() -> {
            exampleSteganography.postToSocialMedia(null, apiNames,keyword,mediaType);
        });
    }

    @Test
    public void postToSocialAPINameNullNullPointer() {
        Assertions.assertThrows(NullPointerException.class,() -> {
            exampleSteganography.postToSocialMedia(null, apiNames,keyword,mediaType);
        });
    }

    @Test
    public void postToSocialKeywordNullNullPointer() {
        Assertions.assertThrows(NullPointerException.class,() -> {
            exampleSteganography.postToSocialMedia(carrier, apiNames,null,mediaType);
        });
    }
    @Test
    public void postToSocialMediaTypeNullNullPointer() {
        Assertions.assertThrows(NullPointerException.class,() -> {
            exampleSteganography.postToSocialMedia(carrier, apiNames,keyword,null);
        });
    }

    @Test
    public void decodeCarrierCorrectInputByteArray() throws UnsupportedMediaTypeException, MediaNotFoundException, MediaCapacityException, MediaReassemblingException, IOException, UnknownStegFormatException {
        byte[] encoded =  exampleSteganography.encodeCarrier(carrier,payload,mediaType);
        byte[] message = exampleSteganography.decodeCarrier(mediaType,encoded);
        Assertions.assertArrayEquals(message,payload);
    }

    @Test
    public void decodeCarrierMediaTypeNullUnsupportedMediaTypeException() {
        Assertions.assertThrows(NullPointerException.class,() -> {
            exampleSteganography.decodeCarrier(null, carrier);
        });
    }

    @Test
    public void decodeCarrierCarrierNullUnsupportedMediaTypeException() {
        Assertions.assertThrows(NullPointerException.class,() -> {
            exampleSteganography.decodeCarrier(mediaType, null);
        });
    }


    public void subscribeToSocialMediaCorrectInputSocialMedia() {
        SocialMedia socialMedia = exampleSteganography.subscribeToSocialMedia(keyword,apiNames);
        Assertions.assertTrue(ImageSteg.class.isInstance(socialMedia));
    }

    @Test
    public void subscribeToSocialMediaKeywordNullNullPointer() {
        SocialMedia socialMedia = exampleSteganography.subscribeToSocialMedia(null,apiNames);
        Assertions.assertTrue(socialMedia == null);
    }

    @Test
    public void subscribeToSocialMediaApiNameNullNullPointer() {
        Assertions.assertThrows(NullPointerException.class,() -> {
            exampleSteganography.subscribeToSocialMedia(keyword,null);
        });
    }


    public void encodeAndPostCarrierCorrectInputTrue() throws UnsupportedMediaTypeException, IOException, MediaNotFoundException, MediaReassemblingException, MediaCapacityException {
        Assertions.assertTrue(exampleSteganography.encodeAndPost(apiNames, keyword, carrier, payload, mediaType));
    }


    public void encodeAndPostFileCorrectInputTrue() throws UnsupportedMediaTypeException, IOException, MediaNotFoundException, MediaReassemblingException, MediaCapacityException{
        Assertions.assertEquals(true, exampleSteganography.encodeAndPost(apiNames, keyword, loadFilePath, payload, mediaType));
    }


    public void encodeAndPostCarrierFalseReturn() throws UnsupportedMediaTypeException, IOException, MediaNotFoundException, MediaReassemblingException, MediaCapacityException {
        Assertions.assertFalse(exampleSteganography.encodeAndPost(apiNames, keyword, carrier, payload, mediaType));
    }

    @Test
    public void encodeAndPostCarrierNull() throws UnsupportedMediaTypeException, IOException, MediaNotFoundException, MediaReassemblingException, MediaCapacityException{
        Assertions.assertThrows(NullPointerException.class,() ->{
            exampleSteganography.encodeAndPost(apiNames, keyword, (byte[])null, payload, mediaType);
        });
    }

    @Test
    public void encodeAndPostPayloadNull() throws UnsupportedMediaTypeException, IOException, MediaNotFoundException, MediaReassemblingException, MediaCapacityException{
        Assertions.assertThrows(NullPointerException.class,() ->{
            exampleSteganography.encodeAndPost(apiNames, keyword, carrier, null, mediaType);
        });
    }

    @Test
    public void encodeAndPostMediaTypeNull() throws UnsupportedMediaTypeException, IOException, MediaNotFoundException, MediaReassemblingException, MediaCapacityException{
        Assertions.assertThrows(NullPointerException.class,() ->{
            exampleSteganography.encodeAndPost(apiNames, keyword, carrier, payload, null);
        });
    }

    @Test
    public void encodeAndPostApiNamesNull() throws UnsupportedMediaTypeException, IOException, MediaNotFoundException, MediaReassemblingException, MediaCapacityException{
        Assertions.assertThrows(NullPointerException.class,() ->{
            exampleSteganography.encodeAndPost(null, keyword, carrier, payload, mediaType);
        });
    }

    @Test
    public void encodeAndPostKeywordNull() throws UnsupportedMediaTypeException, IOException, MediaNotFoundException, MediaReassemblingException, MediaCapacityException{
        Assertions.assertThrows(NullPointerException.class,() ->{
            exampleSteganography.encodeAndPost(apiNames, null, carrier, payload, mediaType);
        });
    }
}
