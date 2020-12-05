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

package steganography.image;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import steganography.image.GIFTableDecoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GIFTableDecoderTest {
    GIFTableDecoder decoder;
    byte[] gif;

    @BeforeEach
    public void before() {
        decoder = new GIFTableDecoder();

    }

/*
    @Test
    public void saveColorTable_FullTabel_intArray() {
        try {
            FileInputStream in = new FileInputStream("src/main/resources/gifFrames/25.gif");
            gif = in.readAllBytes();
            in.close();
            int[] table = decoder.saveColorTable(gif);
            Assertions.assertEquals(256,table.length);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void saveColorTable_SmallTable_intArray() {
        try {
            FileInputStream in = new FileInputStream("src/main/resources/gifFrames/1.gif");
            gif = in.readAllBytes();
            in.close();
            int[] table = decoder.saveColorTable(gif);
            Assertions.assertTrue(table.length > 0 && table.length < 256);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int[] table = decoder.saveColorTable(gif);
        Assertions.assertTrue(table.length > 0 && table.length < 256);
    }
    */


    @Test
    public void saveColorTable_NullInput_NullPointerException() {
        Assertions.assertThrows(NullPointerException.class, () -> {decoder.saveColorTable(null);});
    }

    @Test
    public void saveColorTable_NonGIFInput_IllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {decoder.saveColorTable(new byte[] {2,5,3,6,3});});
    }

    @Test
    public void getColorCouples_CorrectInput_HashMap() {
        int[] table = new int[] {2184637,2184636,2119100,0,1};
        Map<Integer, List<Integer>> map = decoder.getColorCouples(table);
        Assertions.assertEquals(5,map.size());
    }

    @Test
    public void getColorCouples_NoCouples_Empty() {
        int[] table = new int[] {1, 5464345, 49, 2435, 3};
        Map<Integer, List<Integer>> map = decoder.getColorCouples(table);
        Assertions.assertEquals(0,map.size());
    }
}
