package apis.imgur;

import apis.SocialMedia;
import apis.models.APINames;
import apis.models.MyDate;
import apis.models.PostEntry;
import apis.models.Token;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
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

class ImgurTest {

    public List<PostEntry> getSingleDownloadablePicAsList() throws MalformedURLException {
        List<PostEntry> list = new ArrayList<>();
        list.add(new PostEntry("https://i.imgur.com/0F374vh.jpeg", new MyDate(new Date(10000)), ".png"));
        return list;
    }

    @BeforeEach
    public void init(){
        JSONPersistentManager.getInstance().setJsonPersistentHelper(new PersistenceDummy());
    }

    @Test
    void postToSocialNetwork() {
    }

    @Test
    void uploadPicture() {
    }

    @Test
    void listen() {
    }

    @Test
    void subscribeToKeyword() {
    }

    @Test
    void getRecentMediaForKeyword() throws MalformedURLException {
        Imgur imgur = new Imgur();

        ImgurSubscriptionDeamon deamon = Mockito.mock(ImgurSubscriptionDeamon.class);
        Mockito.when(deamon.getRecentMediaForSubscribedKeywords(ArgumentMatchers.anyString()))
                .thenReturn(getSingleDownloadablePicAsList());

        ImgurUtil util = Mockito.mock(ImgurUtil.class);
        deamon.injectImgurUtil(util);
        imgur.injectSubscriptionDeamon(deamon);

        List<byte[]> bytes = imgur.getRecentMediaForKeyword("test");

        assertNotNull(bytes);
        assertTrue(bytes.size() == 1);
        assertTrue(bytes.get(0).length > 0);

        //verify getrecentmedia deamon
        Mockito.verify(deamon, Mockito.times(1)).getRecentMediaForSubscribedKeywords(ArgumentMatchers.anyString());
        Mockito.verify(deamon, Mockito.times(1)).getRecentMedia(ArgumentMatchers.anyString());

        /*Mockito.verify(util, Mockito.times(1)).getKeywordList(ArgumentMatchers.any(APINames.class), ArgumentMatchers.anyString());
        Mockito.verify(util, Mockito.times(1)).getPosts(ArgumentMatchers.anyString());
        Mockito.verify(util, Mockito.times(1)).elimateOldPostEntries(ArgumentMatchers.any(MyDate.class), ArgumentMatchers.anyList());
        Mockito.verify(util, Mockito.times(1)).getLatestStoredTimestamp(ArgumentMatchers.any(APINames.class));
        Mockito.verify(util, Mockito.times(1)).setLatestPostTimestamp(ArgumentMatchers.any(APINames.class), ArgumentMatchers.any(MyDate.class));
        Mockito.verify(util, Mockito.times(1)).getKeywordList(ArgumentMatchers.any(APINames.class), ArgumentMatchers.anyString());
    */
    }


    @Test
    void setGetToken() {
        SocialMedia imgur =new Imgur();
        Token token = new Token("thisisatoken", 100);
        imgur.setToken(token);
        assertTrue(token.getToken().equals(imgur.getToken().getToken()));
        assertTrue(token.getTime() == imgur.getToken().getTime());
    }
}
