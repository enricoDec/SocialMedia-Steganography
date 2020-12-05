package apis.reddit;

import apis.imgur.ImgurUtil;
import apis.models.PostEntry;
import org.junit.jupiter.api.Test;
import org.mockito.internal.util.StringUtil;
import org.mockito.internal.util.io.IOUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/*
 *
 * @author Mario Teklic
 */

class RedditUtilTest {

    @Test
    void getUrl() {
    }

    @Test
    void getTimestamp() {
    }

    @Test
    void getPosts() throws FileNotFoundException {
        ImgurUtil imgurUtil = new ImgurUtil();
        FileInputStream fis = new FileInputStream("src/test/resources/redditResponse.txt");
        String content = StringUtil.join(IOUtil.readLines(fis)).trim();
        System.out.println(content);
        System.out.println("--------------------------------------");

        List<PostEntry> list = imgurUtil.getPosts(content);

        list.stream().forEach(postEntry -> System.out.println(postEntry));
    }

    @Test
    void hasNullObjects() {
    }

    @Test
    void isImageUploadAllowed() {
    }
}
