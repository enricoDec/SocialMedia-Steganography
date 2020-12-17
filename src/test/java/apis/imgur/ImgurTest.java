package apis.imgur;

import apis.MediaType;
import apis.SocialMedia;
import apis.imgur.models.ImgurPostResponse;
import apis.models.MyDate;
import apis.models.PostEntry;
import apis.models.Token;
import apis.utils.BaseUtil;
import apis.utils.BlobConverterImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import persistence.JSONPersistentManager;
import persistence.PersistenceDummy;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;

import static apis.models.APINames.IMGUR;
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
    public void init() {
        JSONPersistentManager.getInstance().setJsonPersistentHelper(new PersistenceDummy());
        JSONPersistentManager.getInstance().setLastTimeCheckedForAPI(IMGUR, 0);
    }

    @Test
    void postToSocialNetworkShouldReceiveErrorCode() {
        Imgur imgur = new Imgur();
        byte[] file = BlobConverterImpl.downloadToByte("https://compress-or-die.com/public/understanding-png/assets/lena-dirty-transparency-corrected-cv.png");
        imgur.setToken(new Token("dummy", 123));
        assertFalse(imgur.postToSocialNetwork(file, MediaType.PNG, "test"));
    }

    @Test
    void uploadPicture() {
        Imgur imgur = new Imgur();
        byte[] file = BlobConverterImpl.downloadToByte("https://compress-or-die.com/public/understanding-png/assets/lena-dirty-transparency-corrected-cv.png");
        imgur.setToken(new Token("dummy", 123));
        ImgurPostResponse response = Imgur.uploadPicture(file, "test");
        assertNotNull(response);
        assertNotNull(response.getData());
        assertNotNull(response.getData().getLink());
        assertFalse(BaseUtil.hasErrorCode(response.status));
    }

    @Test
    void subscribeAndChangeSchedulerPeriod() throws Exception {
        Imgur imgur = new Imgur();
        String newKeyword = String.valueOf(System.currentTimeMillis());

        try{
            imgur.subscribeToKeyword(newKeyword);
            imgur.changeSchedulerPeriod(1);
            Thread.sleep(2000);
            assertTrue(imgur.isSchedulerRunning());
            Thread.sleep(2000);
            assertTrue(imgur.isSchedulerRunning());
        }catch (Exception e){
            //Should not throw an exception. If thrown -> test failed.
            assertTrue(false);
        }
    }

    @Test
    void subscribeToKeyword() throws Exception {
        Imgur imgur = new Imgur();
        String newKeyword = String.valueOf(System.currentTimeMillis());

        List<String> before = JSONPersistentManager.getInstance().getKeywordListForAPI(IMGUR);

        assertTrue(!before.contains(newKeyword));

        try {
            imgur.subscribeToKeyword(newKeyword);
            //Exception must be thrown, otherwise test should fail
            assertTrue(false);
        } catch (RejectedExecutionException e) {
            //Will be thrown by deamon scheduled executor. This is tested in "subscribeAndListen()"
            assertTrue(true);
        }

        List<String> after = JSONPersistentManager.getInstance().getKeywordListForAPI(IMGUR);

        assertTrue(after.contains(newKeyword));
    }

    @Test
    void getRecentMediaForKeyword() {
        Imgur imgur = new Imgur();

        ImgurSubscriptionDeamon deamon = Mockito.mock(ImgurSubscriptionDeamon.class, Mockito.withSettings().verboseLogging());

        ImgurUtil util = Mockito.mock(ImgurUtil.class);
        deamon.injectImgurUtil(util);
        imgur.injectSubscriptionDeamon(deamon);

        List<byte[]> bytes = imgur.getRecentMediaForKeyword("test");

        assertNotNull(bytes);

        //verify getrecentmedia deamon
        Mockito.verify(deamon, Mockito.times(1)).getRecentMediaForSubscribedKeywords(ArgumentMatchers.anyString());
    }


    @Test
    void setGetToken() {
        SocialMedia imgur = new Imgur();
        Token token = new Token("thisisatoken", 100);
        imgur.setToken(token);
        assertTrue(token.getToken().equals(imgur.getToken().getToken()));
        assertTrue(token.getTime() == imgur.getToken().getTime());
    }
}
