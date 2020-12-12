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
package apis;

import apis.Tweet;
import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.auth.OAuthAuthorization;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.media.ImageUpload;
import twitter4j.media.ImageUploadFactory;

import javax.net.ssl.HttpsURLConnection;
import java.awt.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class App {
    private static BufferedReader br;

    public static void main(String[] args) {

        //Download Image
        FileOutputStream out = null;
        try {
            out = new FileOutputStream("C:\\Users\\User\\Documents\\GitHub\\ProjektStudiumSteganography\\src\\main\\java\\Image.jpg");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        try {
            Tweet tweet = new Tweet();
            out.write(tweet.downloadTweet("https://pbs.twimg.com/media/EoUrHVMXUAkmo52?format=jpg&name=medium"));
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Download Image




//Connecting with Twitter

        // consumer Key: QZB3bBTKcWeNAEjFTV66qZntn
        // consumer Key Secret: ezz3RDGGVFKRrjM2bSi9ayoQ6mqfZWN6yboCUIhjCKeGkfTIEh

        String UserAccessToken = null;
        String UserAccessSecret = null;

        try{
        Twitter twitter = new TwitterFactory().getInstance();


        //Set Your Application Consumer Key & Consumer Key Secret
        twitter.setOAuthConsumer("QZB3bBTKcWeNAEjFTV66qZntn", "ezz3RDGGVFKRrjM2bSi9ayoQ6mqfZWN6yboCUIhjCKeGkfTIEh");
        try{
        RequestToken requestToken = twitter.getOAuthRequestToken();
        System.out.println("Got request token.");
        System.out.println("Request token: " + requestToken.getToken());
        System.out.println("Request token secret: " + requestToken.getTokenSecret());


        AccessToken accessToken = null;

        br = new BufferedReader(new InputStreamReader(System.in));
        while (null == accessToken) {
            System.out.println("Open the following URL and grant access to your account:");
            System.out.println(requestToken.getAuthorizationURL());
            System.out.print("Enter the PIN(if available) and hit enter after you granted access.[PIN]:");
            String pin = br.readLine();
            try {
                if (pin.length() > 0) {
                    accessToken = twitter.getOAuthAccessToken(requestToken, pin);
                } else {
                    accessToken = twitter.getOAuthAccessToken(requestToken);
                }
            } catch (TwitterException te) {
                if (401 == te.getStatusCode()) {
                    System.out.println("Unable to get the access token.");
                } else {
                    te.printStackTrace();
                }
            }
        }
        System.out.println("Got access token.");
        UserAccessToken = accessToken.getToken();
        UserAccessSecret = accessToken.getTokenSecret();
        System.out.println("Access token: " + UserAccessToken);
        System.out.println("Access token secret: " + UserAccessSecret);


    } catch (IllegalStateException ie) {
        // access token is already available, or consumer key/secret is not set.
        if (!twitter.getAuthorization().isEnabled()) {
            System.out.println("OAuth consumer key/secret is not set.");
            System.exit(-1);
        }
    }

//Connecting with Twitter





//Fetching User Information from Twitter

                System.out.println("Connecting.... ");
                System.out.println("Twitter Connected To : ");


            User user = twitter.showUser(twitter.getId());
            if (user.getStatus() != null) {
                System.out.println("@" + user.getScreenName());
            } else {
                // the user is protected
                System.out.println("@" + user.getScreenName());
            }

//Fetching User Information from Twitter




//Searching a tweet


            System.out.println("Searching...");
        try {
            Query query = new Query("#goStegaSaiyajin");
            QueryResult result;
            result = twitter.search(query);
            List<Status> tweets = result.getTweets();
            for (Status tweet : tweets) {
                System.out.println("@" + tweet.getUser().getScreenName() + " - " + tweet.getText());
            }

            //System.exit(0);
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to search tweets: " + te.getMessage());
            System.exit(-1);
        }


//Searching a tweet


//Posting a Tweet

            System.out.println("Now Try to upload Image");

            String ImagePath = "C:\\Users\\User\\Documents\\GitHub\\ProjektStudiumSteganography\\src\\main\\java\\Image\\lowResolution.jpg";
            br = new BufferedReader(new InputStreamReader(System.in));
            while (null == ImagePath) {
                System.out.println("Please Type Image Path: ");
                String path = br.readLine();
                if (path.length() > 0) {
                    ImagePath = path;
                } else {
                    path = "";
                    System.exit(-1);
                }
            }


            // type your message(status)
            String Message = null;
            br = new BufferedReader(new InputStreamReader(System.in));
            while (null == Message) {
                System.out.println("Please type your message: ");
                String msg = br.readLine();
                if (msg.length() > 0) {
                    Message = msg;
                } else {
                    Message = "";
                    System.exit(-1);
                }
            }

            try {

                Configuration conf = new ConfigurationBuilder()
                        .setOAuthConsumerKey("QZB3bBTKcWeNAEjFTV66qZntn")
                        .setOAuthConsumerSecret("ezz3RDGGVFKRrjM2bSi9ayoQ6mqfZWN6yboCUIhjCKeGkfTIEh")
                        .setOAuthAccessToken(UserAccessToken)
                        .setOAuthAccessTokenSecret(UserAccessSecret).build();


                OAuthAuthorization auth = new OAuthAuthorization(conf);
                ImageUpload upload = new ImageUploadFactory(conf).getInstance(auth);

                String url;
                if (ImagePath.length()>0) {
                    url = upload.upload(new File(ImagePath), Message);
                } else {
                    url = upload.upload(Message,null);
                }
                System.out.println("Successfully uploaded image to Twitpic at " + url);
                System.exit(0);
            } catch (TwitterException te) {
                te.printStackTrace();
                System.out.println("Failed to upload the image: " + te.getMessage());
                System.exit(-1);
            }

            System.exit(0);


        } catch (TwitterException te) {
            //te.printStackTrace();
            System.out.println("Failed to get timeline: " + te.getMessage());
            System.exit(-1);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println("Failed to read the system input.");
            System.exit(-1);
        }

//Posting a Tweet


    }


}
