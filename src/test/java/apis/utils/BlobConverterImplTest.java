package apis.utils;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/*
 *
 * @author Mario Teklic
 */

class BlobConverterImplTest {

    @Test
    void downloadToByte() {
        String testLink = "https://external-preview.redd.it/y99BwI48BIQYN2vlyK6nBa2L-Zj--n1xtQM_-8K3wBo.jpg?width=108&amp;crop=smart&amp;auto=webp&amp;s=d82d50a60d160a968561947c47a9e04b8a3106f2";
        byte[] bytes = BlobConverterImpl.downloadToByte(BaseUtil.decodeUrl(testLink));
        assertTrue(bytes.length > 0);
    }
}
