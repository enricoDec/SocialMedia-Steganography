package apis.imgur;

import apis.models.MyDate;
import apis.models.PostEntry;
import apis.utils.BaseUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import persistence.JSONPersistentManager;
import persistence.PersistenceDummy;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static apis.models.APINames.IMGUR;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.*;

/*
 *
 * @author Mario Teklic
 */

class ImgurSubscriptionDeamonTest {

    public List<PostEntry> getUnsortedPostEntryList() throws MalformedURLException {
        List<PostEntry> list = new ArrayList<>();
        list.add(new PostEntry("url4", new MyDate(new Date(10000)), ".png"));
        list.add(new PostEntry("url2", new MyDate(new Date(20000)), ".png"));
        list.add(new PostEntry("url5", new MyDate(new Date(50000)), ".png"));
        list.add(new PostEntry("url1", new MyDate(new Date(10000)), ".png"));
        list.add(new PostEntry("url3", new MyDate(new Date(30000)), ".png"));
        return list;
    }

    @BeforeEach
    public void mockitoObjects(){
        //baseUtil = Mockito.mock(BaseUtil.class);
        //Mockito.when(baseUtil.getLatestStoredTimestamp(APINames.IMGUR)).thenReturn(new MyDate(new Date(100)));
        JSONPersistentManager.getInstance().setJsonPersistentHelper(new PersistenceDummy());
    }

    @Test
    void getRecentMediaForSubscribedKeywordsListShouldBeEmpty() throws MalformedURLException {
        ImgurSubscriptionDeamon deamon = Mockito.mock(ImgurSubscriptionDeamon.class);

        Mockito.when(deamon.getRecentMedia("")).thenReturn(getUnsortedPostEntryList());
        List<PostEntry> list = deamon.getRecentMediaForSubscribedKeywords("test");
       // p.println(list.size());

        List<PostEntry> sortedByExtern = getUnsortedPostEntryList();
        BaseUtil.sortPostEntries(sortedByExtern);

        //p.println(list.size());
        assertTrue(list.isEmpty());
        for(int i = 0; i < sortedByExtern.size(); i++){
            assertEquals(sortedByExtern.get(i).getDate(), list.get(i).getDate());
        }
    }

    @Test
    void getRecentMediaForSubscribedKeywordsListShouldBeNotEmpty() throws MalformedURLException {
        ImgurUtil imgurUtil = Mockito.mock(ImgurUtil.class);
        Mockito.when(imgurUtil.elimateOldPostEntries(any(MyDate.class), anyList()))
                .thenAnswer(invocationOnMock ->
                        imgurUtil.elimateOldPostEntries(new MyDate(new Date(0)), invocationOnMock.getArgument(1)));

        ImgurSubscriptionDeamon deamon = new ImgurSubscriptionDeamon();

        List<PostEntry> list = deamon.getRecentMediaForSubscribedKeywords("test");

        List<PostEntry> sortedByExtern = getUnsortedPostEntryList();
        BaseUtil.sortPostEntries(sortedByExtern);

        assertTrue(!list.isEmpty());
        for(int i = 0; i < sortedByExtern.size(); i++){
            assertEquals(sortedByExtern.get(i).getDate(), list.get(i).getDate());
        }
    }

    @Test
    void isNewPostAvailableGetLatestPostEntries() {
        JSONPersistentManager.getInstance().setLastTimeCheckedForAPI(IMGUR, 0);

        ImgurSubscriptionDeamon deamon = new ImgurSubscriptionDeamon();

        assertEquals(false, deamon.isNewPostAvailable());
        assertEquals(null, deamon.getLatestPostEntries());

        deamon.getRecentMediaForSubscribedKeywords("nature");
        assertEquals(true, deamon.isNewPostAvailable());
        assertNotNull(deamon.getLatestPostEntries());
        assertTrue(deamon.getLatestPostEntries().size() > 0);
    }
}
