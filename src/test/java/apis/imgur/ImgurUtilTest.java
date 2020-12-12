package apis.imgur;

import apis.models.MyDate;
import apis.models.PostEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.JSONPersistentManager;
import persistence.PersistenceDummy;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/*
 *
 * @author Mario Teklic
 */

class ImgurUtilTest {

    public List<PostEntry> getSingleDownloadablePicAsList() throws MalformedURLException {
        List<PostEntry> list = new ArrayList<>();
        list.add(new PostEntry("https://i.imgur.com/0F374vh.jpeg", new MyDate(new Date(10000)), ".png"));
        return list;
    }

    @BeforeEach
    public void init() {
        JSONPersistentManager.getInstance().setJsonPersistentHelper(new PersistenceDummy());
    }

    @Test
    void getPosts() {
        ImgurUtil imgurUtil = new ImgurUtil();

        List<PostEntry> list = imgurUtil.getPosts(content);

        assertEquals(1, list.size());
        assertEquals("https://i.imgur.com/Nyxc7Xe.png", list.get(0).getUrl());
        assertTrue(16069830000L == list.get(0).getDate().getTime());
    }

            /*Mockito.verify(util, Mockito.times(1)).getKeywordList(ArgumentMatchers.any(APINames.class), ArgumentMatchers.anyString());
        Mockito.verify(util, Mockito.times(1)).getPosts(ArgumentMatchers.anyString());
        Mockito.verify(util, Mockito.times(1)).elimateOldPostEntries(ArgumentMatchers.any(MyDate.class), ArgumentMatchers.anyList());
        Mockito.verify(util, Mockito.times(1)).getLatestStoredTimestamp(ArgumentMatchers.any(APINames.class));
        Mockito.verify(util, Mockito.times(1)).setLatestPostTimestamp(ArgumentMatchers.any(APINames.class), ArgumentMatchers.any(MyDate.class));
        Mockito.verify(util, Mockito.times(1)).getKeywordList(ArgumentMatchers.any(APINames.class), ArgumentMatchers.anyString());
    */

    String content = "{\"data\":[{\"id\":\"N9iCop8\",\"title\":\"Test vs Marsup 1\",\"description\":null,\"datetime\":1607170824,\"cover\":\"hOnDXz1\",\"cover_width\":480,\"cover_height\":854,\"account_url\":\"pierreluc17127001\",\"account_id\":141727559,\"privacy\":\"hidden\",\"layout\":\"blog\",\"views\":1,\"link\":\"https://imgur.com/a/N9iCop8\",\"ups\":1,\"downs\":0,\"points\":1,\"score\":1,\"is_album\":true,\"vote\":null,\"favorite\":false,\"nsfw\":false,\"section\":\"\",\"comment_count\":0,\"favorite_count\":0,\"topic\":\"No Topic\",\"topic_id\":29,\"images_count\":1,\"in_gallery\":true,\"is_ad\":false,\"tags\":[],\"ad_type\":0,\"ad_url\":\"\",\"in_most_viral\":false,\"include_album_ads\":false,\"images\":[{\"id\":\"hOnDXz1\",\"title\":null,\"description\":null,\"datetime\":1607170797,\"type\":\"video/mp4\",\"animated\":true,\"width\":480,\"height\":854,\"size\":10363948,\"views\":58,\"bandwidth\":601108984,\"vote\":null,\"favorite\":false,\"nsfw\":null,\"section\":null,\"account_url\":null,\"account_id\":null,\"is_ad\":false,\"in_most_viral\":false,\"has_sound\":false,\"tags\":[],\"ad_type\":0,\"ad_url\":\"\",\"edited\":\"0\",\"in_gallery\":false,\"link\":\"https://i.imgur.com/hOnDXz1.mp4\",\"mp4_size\":10363948,\"mp4\":\"https://i.imgur.com/hOnDXz1.mp4\",\"gifv\":\"https://i.imgur.com/hOnDXz1.gifv\",\"hls\":\"https://i.imgur.com/hOnDXz1.m3u8\",\"processing\":{\"status\":\"completed\"},\"comment_count\":null,\"favorite_count\":null,\"ups\":null,\"downs\":null,\"points\":null,\"score\":null}],\"ad_config\":{\"safeFlags\":[\"gallery\",\"in_gallery\",\"album\"],\"highRiskFlags\":[],\"unsafeFlags\":[\"under_10\",\"sixth_mod_unsafe\"],\"wallUnsafeFlags\":[],\"showsAds\":false}},{\"id\":\"xsjs2R7\",\"title\":\"Test\",\"description\":null,\"datetime\":1606983053,\"cover\":\"Nyxc7Xe\",\"cover_width\":1919,\"cover_height\":1043,\"account_url\":\"bartek460\",\"account_id\":140413755,\"privacy\":\"hidden\",\"layout\":\"blog\",\"views\":69,\"link\":\"https://imgur.com/a/xsjs2R7\",\"ups\":1,\"downs\":5,\"points\":-4,\"score\":-3,\"is_album\":true,\"vote\":null,\"favorite\":false,\"nsfw\":false,\"section\":\"\",\"comment_count\":0,\"favorite_count\":0,\"topic\":\"No Topic\",\"topic_id\":29,\"images_count\":1,\"in_gallery\":true,\"is_ad\":false,\"tags\":[],\"ad_type\":0,\"ad_url\":\"\",\"in_most_viral\":false,\"include_album_ads\":false,\"images\":[{\"id\":\"Nyxc7Xe\",\"title\":null,\"description\":null,\"datetime\":1606983025,\"type\":\"image/png\",\"animated\":false,\"width\":1919,\"height\":1043,\"size\":118656,\"views\":129,\"bandwidth\":15306624,\"vote\":null,\"favorite\":false,\"nsfw\":null,\"section\":null,\"account_url\":null,\"account_id\":null,\"is_ad\":false,\"in_most_viral\":false,\"has_sound\":false,\"tags\":[],\"ad_type\":0,\"ad_url\":\"\",\"edited\":\"0\",\"in_gallery\":false,\"link\":\"https://i.imgur.com/Nyxc7Xe.png\",\"comment_count\":null,\"favorite_count\":null,\"ups\":null,\"downs\":null,\"points\":null,\"score\":null}],\"ad_config\":{\"safeFlags\":[\"album\",\"in_gallery\"],\"highRiskFlags\":[],\"unsafeFlags\":[\"sixth_mod_unsafe\",\"under_10\"],\"wallUnsafeFlags\":[],\"showsAds\":false}},{\"id\":\"pKT5FI7\",\"title\":\"test\",\"description\":null,\"datetime\":1606973120,\"cover\":\"XBjnje2\",\"cover_width\":480,\"cover_height\":480,\"account_url\":\"UsersubPoetLaureate\",\"account_id\":40160803,\"privacy\":\"hidden\",\"layout\":\"blog\",\"views\":77390,\"link\":\"https://imgur.com/a/pKT5FI7\",\"ups\":858,\"downs\":124,\"points\":734,\"score\":772,\"is_album\":true,\"vote\":null,\"favorite\":false,\"nsfw\":false,\"section\":\"\",\"comment_count\":100,\"favorite_count\":100,\"topic\":\"No Topic\",\"topic_id\":29,\"images_count\":1,\"in_gallery\":true,\"is_ad\":false,\"tags\":[],\"ad_type\":0,\"ad_url\":\"\",\"in_most_viral\":true,\"include_album_ads\":false,\"images\":[{\"id\":\"XBjnje2\",\"title\":null,\"description\":null,\"datetime\":1606973118,\"type\":\"video/mp4\",\"animated\":true,\"width\":480,\"height\":480,\"size\":338715,\"views\":452004,\"bandwidth\":153100534860,\"vote\":null,\"favorite\":false,\"nsfw\":null,\"section\":null,\"account_url\":null,\"account_id\":null,\"is_ad\":false,\"in_most_viral\":false,\"has_sound\":false,\"tags\":[],\"ad_type\":0,\"ad_url\":\"\",\"edited\":\"0\",\"in_gallery\":false,\"link\":\"https://i.imgur.com/XBjnje2.mp4\",\"mp4_size\":338715,\"mp4\":\"https://i.imgur.com/XBjnje2.mp4\",\"gifv\":\"https://i.imgur.com/XBjnje2.gifv\",\"hls\":\"https://i.imgur.com/XBjnje2.m3u8\",\"processing\":{\"status\":\"completed\"},\"comment_count\":null,\"favorite_count\":null,\"ups\":null,\"downs\":null,\"points\":null,\"score\":null}],\"ad_config\":{\"safeFlags\":[\"sixth_mod_safe\",\"gallery\",\"in_gallery\",\"album\"],\"highRiskFlags\":[],\"unsafeFlags\":[],\"wallUnsafeFlags\":[],\"showsAds\":true}}],\"success\":true,\"status\":200}";
}
