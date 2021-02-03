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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.logging.Logger;

public class Tumblr extends SocialMedia {

    private static final Logger logger = Logger.getLogger(Tumblr.class.getName());


    private String accessToken = null;
    private String accessTokenSecret = null;
    private boolean loggedIn = false;


    JumblrClient tumblrClient;
    String blogName;
    List<String> postURLsForKeyword;

    List<String> tags = new ArrayList<>();

    public Tumblr(){
        tumblrClient = new JumblrClient(TumblrConstants.apiKey, TumblrConstants.apiSecret);

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


    public void login() throws InterruptedException, ExecutionException, IOException {


        if (this.accessToken == null || this.accessTokenSecret == null){
            final OAuth10aService service = new ServiceBuilder(TumblrConstants.apiKey)
                    .apiSecret(TumblrConstants.apiSecret)
                    .callback(TumblrConstants.callback)
                    .build(TumblrApi.instance());
            final Scanner in = new Scanner(System.in);

            System.out.println("Enter the username of the Blog you want to log in");
            this.blogName = in.nextLine();


            System.out.println("=== Tumblr's OAuth Workflow ===");
            System.out.println();

            // Obtain the Request Token
            System.out.println("Fetching the Request Token...");
            final OAuth1RequestToken requestToken = service.getRequestToken();
            System.out.println("Got the Request Token!");
            System.out.println();

            System.out.println("Now go and authorize ScribeJava here:");
            System.out.println(service.getAuthorizationUrl(requestToken));
            System.out.println("And paste the verifier here");
            System.out.print(">>");
            final String oauthVerifier = in.nextLine();
            System.out.println();
            System.out.println(oauthVerifier.length());

            // Trade the Request Token and Verifier for the Access Token
            System.out.println("Trading the Request Token for an Access Token...");
            final OAuth1AccessToken accessToken = service.getAccessToken(requestToken, oauthVerifier);
            System.out.println("Got the Access Token!");
            System.out.println("(The raw response looks like this: " + accessToken.getRawResponse() + "')");
            System.out.println();
            this.accessToken = accessToken.getToken();
            this.accessTokenSecret = accessToken.getTokenSecret();
            this.tumblrClient.setToken(this.accessToken, this.accessTokenSecret);
            loggedIn = true;
        } else {
            System.out.println("already logged in");
        }


    }

    public void loginNewUser(){
        this.accessToken = null;
        this.accessTokenSecret = null;
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


    @Override
    public Token getToken() {
        //only used for oAuth2 SocialMedias
        return null;
    }

    @Override
    public void setToken(Token token) {
        //only used for oAuth2 SocialMedias
    }

    @Override
    public boolean postToSocialNetwork(byte[] media, MediaType mediaType, String keyword) {
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

    @Override
    public boolean subscribeToKeyword(String keyword) {

            tags.add(keyword);
            for (Post post : tumblrClient.tagged(keyword, null)) {
                if (post.getType() == Post.PostType.PHOTO || post.getType() == Post.PostType.VIDEO || post.getType() == Post.PostType.AUDIO)
                    postURLsForKeyword.add(post.getShortUrl());
            }
            printPostURLs();
            return true;
    }

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

    @Override
    public String getApiName() {
        return APINames.TUMBLR.toString();
    }

    @Override
    public List<String> getAllSubscribedKeywords() {
        try {
            return JSONPersistentManager.getInstance().getKeywordListForAPI(APINames.TUMBLR);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }


    /**
     * used to post Audio
     * For Audio the File object is enough,
     * no special "Audio"-Object has to be instanciated from File
     * with a File as input
     */
    public Long postAudio(byte[] media, String keyword){

        File audioFile = null;
        AudioPost audioPost = null;

        try {
            FileOutputStream fileOutputStream = new FileOutputStream("src/main/java/apis/tumblr/medias/audiotestAfterEncode.mp3");
            fileOutputStream.write(media);
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        audioFile = new File("src/main/java/apis/tumblr/medias/audiotestAfterEncode.mp3");
        List<String> tags = new ArrayList<>();
        tags.add(keyword);

        try {
            audioPost = this.tumblrClient.newPost(this.blogName, AudioPost.class);
            audioPost.setData(audioFile);
            audioPost.setTags(tags);
            audioPost.save();
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
            System.out.println("uploaded MP3 successfull");
        }
        return audioPost.getId();

    }

    /**
     * used to post Photos and GIFs
     * For Photos and GIFs a "Photo"-Object needs to be created
     * with a File as input
     */
    public Long postPhoto(byte[] media, String keyword){

        File photoFile = new File("src/main/java/apis/tumblr/medias/photoTest.png");
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
