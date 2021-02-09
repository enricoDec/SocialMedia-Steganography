/*
 * Copyright (c) 2020
 * Contributed by NAME HERE
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

package apis.tumblr;

import apis.MediaType;
import apis.SocialMedia;
import apis.models.APINames;
import apis.models.Token;
import com.github.scribejava.apis.TumblrApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.oauth.OAuth10aService;
import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.exceptions.JumblrException;
import com.tumblr.jumblr.types.*;
import persistence.JSONPersistentManager;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.logging.Logger;

/**
 * Tumblr Social Media to upload PNG and MP3. search posts with keyword
 */
public class Tumblr extends SocialMedia {

    private static final Logger logger = Logger.getLogger(Tumblr.class.getName());

    /**
     * service to handle Authorization
     */
    private OAuth10aService service;

    private OAuth1RequestToken requestToken;

    /**
     * oAuth 1 Token containing AccessToken and AccessTokenSecret
     */
    private Token token;

    /**
     * OAuth Consumer Key for Application
     */
    static String apiKey = null;

    /**
     * Secret Key for Application
     */
    static String apiSecret = null;

    /**
     * callback URL for Oauth Authorization Flow
     */
    private final String callbackURL = "https://example.com";

    /**
     * handles requests to Tumblr API
     */
    private JumblrClient tumblrClient;

    /**
     * blogname to access or post content to
     */
    private String blogName;

    private List<String> postURLsForKeyword;

    /**
     * List of tags/keywords/hashtag to add to an upload or look for in posts
     */
    List<String> tags = new ArrayList<>();

    public Tumblr(){
        tumblrClient = new JumblrClient(apiKey, apiSecret);
        this.token = new Token();
    }


    /**
     * OAuth 1 Authorization workflow to get Request Token, authorize the Application and receive Authorization URL,
     * after authorization verifier has to be set to obtain Token containing accessToken and accessTokenSecret
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws IOException
     */
    public void login() throws InterruptedException, ExecutionException, IOException {

        service = new ServiceBuilder(apiKey)
                .apiSecret(apiSecret)
                .callback(this.callbackURL)
                .build(TumblrApi.instance());
        final Scanner in = new Scanner(System.in);

        System.out.println("Enter the username of the Blog you want to log in");
        setBlogname(in.nextLine());


        System.out.println("=== Tumblr's OAuth Workflow ===");
        System.out.println();

        // Obtain the Request Token
        System.out.println("Fetching the Request Token...");
        requestToken = service.getRequestToken();
        System.out.println("Got the Request Token!");
        System.out.println();

        System.out.println("Now go and authorize ScribeJava here:");
        System.out.println(service.getAuthorizationUrl(requestToken));
        System.out.println("And paste the verifier here");
        System.out.print(">>");
        final String oauthVerifier = in.nextLine();
        System.out.println();

        // Trade the Request Token and Verifier for the Access Token
        System.out.println("Trading the Request Token for an Access Token...");
        final OAuth1AccessToken accessToken = service.getAccessToken(requestToken, oauthVerifier);
        System.out.println("Got the Access Token!");
        System.out.println("(The raw response looks like this: " + accessToken.getRawResponse() + "')");
        System.out.println();
        this.token.setAccessToken(accessToken.getToken());
        this.token.setAccessTokenSecret(accessToken.getTokenSecret());
        setToken(this.token);

    }

    /**
     * gets Authorization URL for USer to log in to Tumblr
     * @return Authorization URL
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws IOException
     */
    public String  getAuthorizationURL() throws InterruptedException, ExecutionException, IOException {
        String authURL;
        service = new ServiceBuilder(apiKey)
                .apiSecret(apiSecret)
                .callback(this.callbackURL)
                .build(TumblrApi.instance());
        final Scanner in = new Scanner(System.in);


        // Obtain the Request Token
        final OAuth1RequestToken requestToken = service.getRequestToken();

        authURL = service.getAuthorizationUrl(requestToken);

        return authURL;
    }

    /**
     * traded verifier and requestToken for accessToken and AccessTokenSecret
     * @param verifier
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws IOException
     */
    public void getAccessTokenAndSecret(String verifier) throws InterruptedException, ExecutionException, IOException {
        final String oauthVerifier = verifier;

        final OAuth1AccessToken accessToken = service.getAccessToken(requestToken, oauthVerifier);
        this.token.setAccessToken(accessToken.getToken());
        this.token.setAccessTokenSecret(accessToken.getTokenSecret());
        setToken(this.token);

    }

    /**
     * set accessToken and TokenSecret to null to log in new user afterwards
     */
    public void loginNewUser(){
        this.token.setAccessToken(null);
        this.token.setAccessTokenSecret(null);
        try {
            login();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * get the oAuth 1 Token
     * @return Token object containing accessToken and TokenSecret
     */
    @Override
    public Token getToken() {
        return this.token;
    }

    /**
     * set O Auth 1 Token to access Tumblr API
     * @param token containing accessToken and TokenSecret
     */
    @Override
    public void setToken(Token token) {
        this.token = token;
        this.tumblrClient.setToken(this.token.getAccessToken(), this.token.getAccessTokenSecret());
    }

    /**
     * sets Application Consumer key which is needed to access Tumblr API
     * @param key
     */
    public static void setApiKey(String key){
        apiKey = key;
    }

    /**
     * sets Application Secret which is needed to access Tumblr API
     * @param secret
     */
    public static void setApiSecret(String secret){
        apiSecret = secret;
    }

    /**
     * posts a given byte[] containing some media to Tumblr with a keyword
     * starts login process if no token available
     * @param media data to upload
     * @param mediaType i.e. PNG, MP3, gif
     * @param keyword keyword to search this post by
     * @return
     */
    @Override
    public boolean postToSocialNetwork(byte[] media, MediaType mediaType, String keyword) {
        if(keyword == null){
            throw new NullPointerException();
        }
        if(this.token.getAccessToken() == null || this.token.getAccessTokenSecret() == null){
            try {
                login();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Long postId = null;
        switch(mediaType){
            case MP3:
                postId = this.postAudio(media, keyword);
                break;
            case PNG:
                postId = this.postPhoto(media, keyword);
                break;

        }
        if (postId != null){
            return true;
        }
        return false;
    }

    /**
     * posts media to Tumblr if a token already exists to skip Authorization workflow
     * @param media data to upload
     * @param mediaType
     * @param keyword keyword to search this post by
     * @param token
     * @return
     */
    @Override
    public boolean postToSocialNetwork(byte[] media, MediaType mediaType, String keyword, Token token) {
        setToken(token);
        boolean bool = postToSocialNetwork(media, mediaType, keyword);
        return bool;
    }

    /**
     * search for keyword in tumblrposts
     * @param keyword keyword to subscribe to
     * @return list of short URL, only last 20 entries from which comments and medias which are not PNG and MP3 are filtered out
     */
    @Override
    public boolean subscribeToKeyword(String keyword) {

            tags.add(keyword);
            for (Post post : tumblrClient.tagged(keyword, null)) {
                if (post.getType() == Post.PostType.PHOTO || post.getType() == Post.PostType.AUDIO)
                    postURLsForKeyword.add(post.getShortUrl());
            }
            printPostURLs();
            return true;
    }

    /**
     * prints all saved Post URLs
     */
    public void printPostURLs(){
        for(String url : postURLsForKeyword){
            System.out.println(url);
        }
    }

    @Override
    public boolean unsubscribeKeyword(String keyword) {return false;}



    @Override
    public void changeSchedulerPeriod(Integer interval) { }


    @Override
    public void startSearch() { }

    @Override
    public List<byte[]> getRecentMediaForKeyword(String keyword) {
        // not used for Tumblr as only Posts Url can be searched, download of medias has to be done manually
        // instead getPostUrlForKeyword(String Keyword) is used for Tumblr
        // see method below
        return null;
    }


    @Override
    public void stopSearch() {}

    /**
     * returns API name
     * @return
     */
    @Override
    public String getApiName() {
        return APINames.TUMBLR.toString();
    }

    /**
     * returns all subscribed keywords
     * @return
     */
    @Override
    public List<String> getAllSubscribedKeywords() {
        try {
            return JSONPersistentManager.getInstance().getKeywordListForAPI(APINames.TUMBLR);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    /**
     * set blogname
     * @param blogname
     */
    @Override
    public void setBlogname(String blogname) {
        this.blogName = blogname;
    }


    /**
     * used to post Audio
     * For Audio the File object is enough,
     * no special "Audio"-Object has to be instanciated from File
     * with a File as input
     */
    /**
     * Posts Audio File to Tumblr
     * @param media to post
     * @param keyword
     * @return Post id if upload was successfull
     */
    public Long postAudio(byte[] media, String keyword){

        File audioFile = null;
        AudioPost audioPost = null;

        try {
            FileOutputStream fileOutputStream = new FileOutputStream("src/main/resources/audioAfterEncode.mp3");
            fileOutputStream.write(media);
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        audioFile = new File("src/main/resources/audioAfterEncode.mp3");
        List<String> tags = new ArrayList<>();
        tags.add(keyword);

        try {
            audioPost = this.tumblrClient.newPost(this.blogName, AudioPost.class);
            audioPost.setData(audioFile);
            audioPost.setTags(tags);
            audioPost.save();
            Files.deleteIfExists(audioFile.toPath());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (JumblrException ex) {
            System.out.println("(" + ex.getResponseCode() + ") " + ex.getMessage());
        }
        if(audioPost.getId() != null){
            System.out.println(" uploaded MP3 successfull");
        }
        return audioPost.getId();

    }

    /**
     * used to post Photos and GIFs
     * For Photos and GIFs a "Photo"-Object needs to be created
     * with a File as input
     */
    /**
     * creates Photo Post on Tumblr
     * @param media to post
     * @param keyword
     * @return post id if upload was successfull
     */
    public Long postPhoto(byte[] media, String keyword){

        File photoFile = new File("src/main/resources/photoTest.png");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(photoFile);
            fileOutputStream.write(media);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Photo photo = new Photo(photoFile);
        PhotoPost photoPost = null;

        List<String> tags = new ArrayList<>();
        tags.add(keyword);
        try {
            photoPost = this.tumblrClient.newPost(this.blogName, PhotoPost.class);
            photoPost.setPhoto(photo);
            photoPost.setTags(tags);
            photoPost.save();
            Files.deleteIfExists(photoFile.toPath());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (JumblrException ex) {
            System.out.println("(" + ex.getResponseCode() + ") " + ex.getMessage() + "to post photo");
        }
        if(photoPost.getId() != null){
            System.out.println("uploaded PNG successfull");
        }
        return photoPost.getId();
    }
}
