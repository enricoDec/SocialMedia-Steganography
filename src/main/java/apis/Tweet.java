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

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.TwitterException;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class Tweet {

    public Tweet(){

    }

    public byte[] downloadTweet(String downloadTweetURL) {
        String fAddress = downloadTweetURL;

        URL url;
        byte[] buf;
        int byteRead;
        try {
            url = new URL(fAddress);


            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            BufferedOutputStream outStream = new BufferedOutputStream(byteArrayOutputStream);


            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            InputStream is = conn.getInputStream();
            buf = new byte[conn.getContentLength()];
            while ((byteRead = is.read(buf)) != -1) {
                outStream.write(buf, 0, byteRead);
            }
            is.close();
            outStream.close();
            conn.disconnect();
            return byteArrayOutputStream.toByteArray();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

}
