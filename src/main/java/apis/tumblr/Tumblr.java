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
import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.exceptions.JumblrException;
import com.tumblr.jumblr.types.AudioPost;
import com.tumblr.jumblr.types.Photo;
import com.tumblr.jumblr.types.PhotoPost;
import com.tumblr.jumblr.types.VideoPost;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Tumblr extends SocialMedia {

    JumblrClient tumblrClient;
    String blogName;
    MediaType mediaType;




    File audioFile = new File("src/main/resources/audiotest.mp3");
    AudioPost audioPost;

    List<String> tags = new ArrayList<>();

    public Tumblr(){
        tumblrClient = new JumblrClient(TumblrConstants.apiKey, TumblrConstants.apiSecret);
        tumblrClient.setToken("sk4dqsFfEScw9NzORZ5x9s7DasAsfhHlAZYhe2nbAsWmBF6SwU", "3cCMy7v1fkOMuSqJUe1nriAzg78nsgtnBHRje0sxSwPbDQCRmm");
    }

    @Override
    public Token getToken() {
        return null;
    }

    @Override
    public void setToken(Token token) {

    }

    @Override
    public boolean postToSocialNetwork(byte[] media, MediaType mediaType, String keyword) {
        Long postId = null;
        switch(mediaType){
            case MP3:
                postId = this.postAudio(media, keyword);
            //case MP4:
              //  postId = this.postVideo(media, keyword);
              //  break;
            case GIF:
                postId = this.postGif(media, keyword);
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
        return false;
    }

    @Override
    public List<byte[]> getRecentMediaForKeyword(String keyword) {
        return null;
    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public String getApiName() {
        return APINames.TUMBLR.toString();
    }

    @Override
    public List<String> getAllSubscribedKeywords() {
        return null;
    }

    @Override
    public void setBlogName(String blogName){
        this.blogName = blogName;
    }

    /**
     * used to post Audio
     * For Audio the File object is enough,
     * no special "Audio"-Object has to be instanciated from File
     * with a File as input
     */
    public Long postAudio(byte[] media, String keyword){

        List<String> tags = new ArrayList<>();
        tags.add(keyword);

        try {
            this.audioPost = this.tumblrClient.newPost(this.blogName, AudioPost.class);
            this.audioPost.setData(this.audioFile);
            this.audioPost.setTags(tags);
            this.audioPost.save();
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
        return this.audioPost.getId();

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
            System.out.println("upload successfull");
        }
        return photoPost.getId();
    }

    /**
     * used to post Photos and GIFs
     * For Photos and GIFs a "Photo"-Object needs to be created
     * with a File as input
     */
    public Long postGif(byte[] media, String keyword){

        String pathString = "src/main/java/apis/tumblr/medias/gifPostTest.gif";
        Path gifPath = Paths.get(pathString);

        try {
            Files.write(gifPath, media);
        } catch (IOException e) {
            e.printStackTrace();
        }
        File gifFile = new File("src/main/java/apis/tumblr/medias/gifPostTest.gif");
        Photo photo = new Photo(gifFile);
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
            System.out.println("(" + ex.getResponseCode() + ") " + ex.getMessage() + "to post gif");
        }
        if(photoPost.getId() != null){
            System.out.println("upload successfull");
        }
        return photoPost.getId();


    }




    public Long postPhotoAndGif(byte[] media, String keyword, File file){

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(media);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Photo photo = new Photo(file);
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
            System.out.println("upload successfull");
        }
        return photoPost.getId();

    }

    /**
     * used to post Videos
     * For Videos the File object is enough,
     * no special "Video"-Object has to be instanciated from File
     * with a File as input
     */
    /*public Long postVideo(byte[] media, String keyword){

        File videoFile = new File("src/main/resources/test.mp4");
        VideoPost videoPost = null;

        List<String> tags = new ArrayList<>();
        tags.add(keyword);

        try {
            videoPost = this.tumblrClient.newPost(this.blogName, VideoPost.class);
            videoPost.setData(videoFile);
            videoPost.setTags(tags);
            videoPost.save();
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
        return videoPost.getId();

    }*/
}
