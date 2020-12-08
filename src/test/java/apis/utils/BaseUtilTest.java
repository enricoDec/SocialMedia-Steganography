package apis.utils;

import apis.models.MyDate;
import apis.models.PostEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.JSONPersistentManager;
import persistence.PersistenceDummy;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static apis.models.APINames.IMGUR;
import static org.junit.jupiter.api.Assertions.*;

/*
 *
 * @author Mario Teklic
 */

class BaseUtilTest {

    @BeforeEach
    public void setPersistence(){
        JSONPersistentManager.getInstance().setJsonPersistentHelper(new PersistenceDummy());
    }

    public List<PostEntry> getUnsortedPostEntryList() {
        List<PostEntry> list = new ArrayList<>();
        list.add(new PostEntry("url4", new MyDate(new Date(40000)), ".png"));
        list.add(new PostEntry("url2", new MyDate(new Date(20000)), ".png"));
        list.add(new PostEntry("url1", new MyDate(new Date(10000)), ".png"));
        list.add(new PostEntry("url3", new MyDate(new Date(30000)), ".png"));
        return list;
    }

    @Test
    void sortPostEntries() {
        List<PostEntry> unsorted = getUnsortedPostEntryList();
        BaseUtil.sortPostEntries(unsorted);

        int l = 10000;
        for(int i = 0; i < unsorted.size(); i++){
            assertEquals(l, unsorted.get(i).getDate().getTime());
            l += 10000;
        }

        assertTrue(unsorted.size() > 0);
        assertFalse(unsorted.isEmpty());
    }

    @Test
    void setGetLatestPostTimestamp() {
        BaseUtil baseUtil = new BaseUtil();
        long ts = System.currentTimeMillis();
        baseUtil.setLatestPostTimestamp(IMGUR, new MyDate(new Date(ts)));

        MyDate tsRestored = baseUtil.getLatestStoredTimestamp(IMGUR);
        MyDate tsConverted = new MyDate(new Date(ts));

        assertNotNull(tsRestored);
        assertNotNull(tsRestored.getTime());
        assertEquals(tsConverted.getTime(), tsRestored.getTime());
    }


    @Test
    void setGetKeywordList() throws Exception {
        List<String> kw = new ArrayList<>();
        kw.add("nature");
        kw.add("random");
        kw.add("keyword");

        for(String s : kw){
            JSONPersistentManager.getInstance().addKeywordForAPI(IMGUR, s);
        }

        List<String> list = JSONPersistentManager.getInstance().getKeywordListForAPI(IMGUR);
        list.stream().forEach(s -> assertTrue(kw.contains(s)));

        assertTrue(list.size() == 3);
        assertTrue(list.get(0).equals(kw.get(0)));
        assertTrue(list.get(1).equals(kw.get(1)));
        assertTrue(list.get(2).equals(kw.get(2)));

    }

    @Test
    void hasErrorCode() {
        assertTrue(BaseUtil.hasErrorCode(400));
        assertTrue(BaseUtil.hasErrorCode(404));
        assertTrue(BaseUtil.hasErrorCode(402));
        assertFalse(BaseUtil.hasErrorCode(200));
        assertFalse(BaseUtil.hasErrorCode(202));
        assertFalse(BaseUtil.hasErrorCode(204));
    }

    @Test
    void elimateOldPostEntries() {
        BaseUtil util = new BaseUtil();
        List<PostEntry> list = getUnsortedPostEntryList();
        BaseUtil.sortPostEntries(list);

        list = util.elimateOldPostEntries(new MyDate(new Date(30000)), list);

        assertTrue(list.size() < 4);
        assertTrue(list.size() == 1);
        assertFalse(list.get(0).getDate().getTime() == 30000);
        assertTrue(list.get(0).getDate().getTime() == 40000);
    }

    @Test
    void decodeUrl() {
        String testLink = "https://external-preview.redd.it/y99BwI48BIQYN2vlyK6nBa2L-Zj--n1xtQM_-8K3wBo.jpg?width=108&amp;crop=smart&amp;auto=webp&amp;s=d82d50a60d160a968561947c47a9e04b8a3106f2";
        assertTrue(testLink.contains("amp;"));
        testLink = BaseUtil.decodeUrl(testLink);
        assertFalse(testLink.contains("amp;"));
    }
}
