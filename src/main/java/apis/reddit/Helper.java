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

package apis.reddit;

import apis.MediaType;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Helper {

    public static File download(String url) {
        File file = new File("_dl.jpg");
        try{
            System.out.println("URL:" + url);
            ReadableByteChannel readableByteChannel = Channels.newChannel(new URL(url).openStream());
            FileOutputStream fis = new FileOutputStream(file);
            FileChannel fileChannel = fis.getChannel();
            fileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
            return file;
        }catch (Exception e){
            e.printStackTrace();
            System.err.println("An error occured.");
        }
        return null;
    }

    public static List<String> getDownloadLinks(String responseString){
        Map json = new Gson().fromJson(responseString, Map.class);

        LinkedTreeMap obj = (LinkedTreeMap) json.get("data");
        ArrayList obj2 = (ArrayList) obj.get("children");
        List<LinkedTreeMap> ltm = new ArrayList<>();
        for(Object os : obj2){
            ltm.add((LinkedTreeMap) os);
        }

        List<LinkedTreeMap> ltm2 = new ArrayList<>();
        for(LinkedTreeMap item : ltm){
            ltm2.add((LinkedTreeMap) item.get("data"));
        }

        List<LinkedTreeMap> ltm3 = new ArrayList<>();
        for(LinkedTreeMap s : ltm2){
            if(s.containsKey("preview")){
                ltm3.add((LinkedTreeMap) s.get("preview"));
            }
        }

        ArrayList ltm4 = new ArrayList<>();
        for(LinkedTreeMap a : ltm3){
            if(a.containsKey("images")){
                ltm4.add(a.get("images"));
            }
        }

        ArrayList ltm5 = new ArrayList<>();
        for(Object q : ltm4){
            ArrayList qa = (ArrayList) q;
            ltm5.add(qa.get(0));
        }

        List<LinkedTreeMap> ltm6 = new ArrayList<>();
        for(Object r : ltm5){
            LinkedTreeMap re = (LinkedTreeMap) r;
            if(re.containsKey("source")){
                ltm6.add((LinkedTreeMap) re.get("source"));
            }
        }

        List<String> ltm7 = new ArrayList<>();
        for(Object r : ltm6){
            LinkedTreeMap re = (LinkedTreeMap) r;
            if(re.containsKey("url")){
                String temp = (String) re.get("url");
                temp = temp.replace("amp;s", "s");
                ltm7.add(temp);
            }
        }
        return ltm7;
    }

    public static MediaType getMediaType(String url){
        if(url.contains(MediaType.BMP.name().toLowerCase())){
            return MediaType.BMP;
        }else if(url.contains(MediaType.GIF.name().toLowerCase())){
            return MediaType.GIF;
        }else if(url.contains(MediaType.JPEG.name().toLowerCase())){
            return MediaType.JPEG;
        }else if(url.contains(MediaType.PNG.name().toLowerCase())){
            return MediaType.PNG;
        }else if(url.contains(MediaType.TIFF.name().toLowerCase())){
            return MediaType.TIFF;
        }else if(url.contains(MediaType.JPG.name().toLowerCase())){
            return MediaType.JPG;
        }
        return null;
    }
}
