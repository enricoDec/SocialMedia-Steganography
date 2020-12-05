package apis.imgur;

import apis.models.PostEntry;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
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

class ImgurUtilTest {

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
