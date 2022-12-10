package socialmediasteganography;

import apis.MediaType;
import apis.SocialMedia;
import apis.models.APINames;
import apis.models.Token;
import apis.tumblr.Tumblr;
import steganography.Steganography;
import steganography.audio.mp3.MP3Steganography;
import steganography.exceptions.*;
import steganography.image.AnimatedGif;
import steganography.image.ImageSteg;
import steganography.util.ByteArrayUtils;
import steganography.video.VideoSteg;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SocialMediaSteganographyImpl implements SocialMediaSteganography {
    private Long seed;

    @Override
    public boolean encodeAndPost(APINames apiNames, String keyword, byte[] carrier, byte[] payload,
                                 MediaType mediaType, Token token, String username) throws UnsupportedMediaTypeException, MediaNotFoundException, MediaCapacityException, MediaReassemblingException, IOException {
        byte[] encoded = encodeCarrier(carrier, payload, mediaType);
        return postToSocialMedia(encoded, apiNames, keyword, mediaType, token, username);
    }

    @Override
    public boolean encodeAndPost(APINames apiNames, String keyword, byte[] carrier, byte[] payload,
                                 MediaType mediaType) throws UnsupportedMediaTypeException, MediaNotFoundException,
            MediaCapacityException, MediaReassemblingException, IOException {
        byte[] encoded = encodeCarrier(carrier, payload, mediaType);
        return postToSocialMedia(encoded, apiNames, keyword, mediaType);
    }

    @Override
    public boolean encodeAndPost(APINames apiNames, String keyword, String path, byte[] payload, MediaType mediaType) throws IOException, UnsupportedMediaTypeException, MediaNotFoundException, MediaReassemblingException, MediaCapacityException {
        File file = new File(path);
        byte[] carrier = ByteArrayUtils.read(file);
        return encodeAndPost(apiNames, keyword, carrier, payload, mediaType);
    }

    @Override
    public void saveEncodedPicture(byte[] carrier, byte[] payload, MediaType mediaType, String savepath) throws UnsupportedMediaTypeException, MediaNotFoundException, MediaCapacityException, MediaReassemblingException, IOException {
        byte[] encoded = encodeCarrier(carrier, payload, mediaType);
        if (encoded == null) {
            throw new UnsupportedMediaTypeException("Unsupported");
        }
        File file = new File(savepath);
        try {
            Files.write(file.toPath(), encoded);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveEncodePicture(String filepath, MediaType mediaType, byte[] payload, String savepath) throws IOException, UnsupportedMediaTypeException, MediaNotFoundException, MediaReassemblingException, MediaCapacityException {
        File file = new File(filepath);
        byte[] carrier = ByteArrayUtils.read(file);
        saveEncodedPicture(carrier, payload, mediaType, savepath);
    }

    @Override
    public boolean postToSocialMedia(byte[] carrier, APINames apiNames, String keyword, MediaType mediaType) {
        SocialMedia socialMedia = getSocialMediaByApiName(apiNames);
        if (socialMedia != null) {
            return socialMedia.postToSocialNetwork(carrier, mediaType, keyword);

        }
        throw new NullPointerException("Social Media not supported");
    }

    @Override
    public boolean postToSocialMedia(byte[] carrier, APINames apiNames, String keyword, MediaType mediaType,
                                     Token token, String username) {
        SocialMedia socialMedia = getSocialMediaByApiName(apiNames);
        if (socialMedia != null) {
            if (token != null) {
                socialMedia.setToken(token);
                socialMedia.setBlogname(username);
                return socialMedia.postToSocialNetwork(carrier, mediaType, keyword, token);
            } else {
                throw new NullPointerException("Token is null");
            }
        }
        throw new NullPointerException("Social Media not supported");
    }

    @Override
    public byte[] encodeCarrier(byte[] carrier, byte[] payload, MediaType mediaType) throws UnsupportedMediaTypeException, MediaCapacityException, MediaNotFoundException, MediaReassemblingException, IOException {
        if (mediaType == null) {
            throw new NullPointerException("Media Type is null");
        }
        Steganography steganography = getSteganographyByMediaType(mediaType);
        if (steganography != null) {
            if (seed == null) {
                return steganography.encode(carrier, payload);
            }
            return steganography.encode(carrier, payload, seed);
        }
        return null;
    }

    @Override
    public SocialMedia subscribeToSocialMedia(String keyword, APINames apiNames) {
        SocialMedia socialMedia = getSocialMediaByApiName(apiNames);
        if (socialMedia != null) {
            if (socialMedia.subscribeToKeyword(keyword)) {
                return socialMedia;
            }
            return null;
        }
        throw new NullPointerException("Social Media is unsupported");
    }

    @Override
    public List<byte[]> getMediaAndDecode(String keyword, APINames apiNames, MediaType mediaType) throws UnsupportedMediaTypeException {
        SocialMedia socialMedia = getSocialMediaByApiName(apiNames);
        if (socialMedia != null) {
            List<byte[]> downloads = socialMedia.getRecentMediaForKeyword(keyword);
            if (downloads.size() > 0) {
                Steganography steganography = getSteganographyByMediaType(mediaType);
                if (steganography != null) {
                    List<byte[]> payloads = new ArrayList<>();
                    for (byte[] data : downloads) {
                        try { // If exception are thrown while decoding they will be ignored to continue decoding
                            if (steganography.isSteganographicData(data)) {
                                if (seed != null) {
                                    payloads.add(steganography.decode(data, seed));
                                } else {
                                    payloads.add(steganography.decode(data));
                                }
                            }
                        } catch (IOException | MediaNotFoundException | UnsupportedMediaTypeException |
                                 UnknownStegFormatException exception) {
                            continue;
                        }
                    }
                    return payloads;
                } else {
                    throw new UnsupportedMediaTypeException("unsupported Media Type");
                }
            }
            return null;
        }
        throw new NullPointerException("Social Media or Media ");
    }

    @Override
    public byte[] decodeCarrier(MediaType mediaType, byte[] carrier) throws UnsupportedMediaTypeException,
            UnknownStegFormatException, MediaNotFoundException, IOException {
        Steganography steganography = getSteganographyByMediaType(mediaType);
        if (steganography != null) {
            if (seed == null) {
                return steganography.decode(carrier);
            }
            return steganography.decode(carrier, seed);
        }
        throw new UnsupportedMediaTypeException("Unsupported media");
    }

    @Override
    public void setSeed(Long seed) {
        this.seed = seed;
    }

    private Steganography getSteganographyByMediaType(MediaType mediaType) {
        switch (mediaType) {
            case PNG:
                return new ImageSteg();
            case BMP:
                return new ImageSteg();
            case GIF:
                return new AnimatedGif();
            case MP3:
                return new MP3Steganography();
            case AVI:
                return new VideoSteg();
            default:
                return null;
        }
    }

    private SocialMedia getSocialMediaByApiName(APINames apiNames) {
        if (Objects.requireNonNull(apiNames) == APINames.TUMBLR) {
            SocialMedia tumblr = new Tumblr();
            return tumblr;
        }
        return null;
    }

}
