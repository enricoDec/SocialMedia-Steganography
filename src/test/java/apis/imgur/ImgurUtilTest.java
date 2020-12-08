package apis.imgur;

import apis.models.APINames;
import apis.models.MyDate;
import apis.models.PostEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.util.StringUtil;
import org.mockito.internal.util.io.IOUtil;
import persistence.JSONPersistentManager;
import persistence.PersistenceDummy;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
    void getPosts() throws FileNotFoundException {
        ImgurUtil imgurUtil = new ImgurUtil();
        FileInputStream fis = new FileInputStream("src/test/resources/imgurResponse.txt");

        String content = StringUtil.join(IOUtil.readLines(fis)).trim();
        System.out.println(content);
        System.out.println("--------------------------------------");

        List<PostEntry> list = imgurUtil.getPosts(content);

        list.stream().forEach(postEntry -> System.out.println(postEntry));
    }
}
