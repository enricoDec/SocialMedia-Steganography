/*
 * Copyright (c) 2020
 * Contributed by Selina Wernike
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

package steganography.image.encoders;

import steganography.util.ByteHex;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Selina Wernike
 * These Class is mainly used to decode and change a GIF color table.
 * It's based on the official GIF source
 */
public class GIFTableDecoder {
    byte[] header = {ByteHex.hexToByte("47"),ByteHex.hexToByte("49"), ByteHex.hexToByte("46"),
                        ByteHex.hexToByte("38"), ByteHex.hexToByte("39"), ByteHex.hexToByte("61")};

    /**
     * This Method extracts the color table of a gif and transforms it into an int-array.
     * Each entry contains an Integer representing Alpha, red, green, blue channel with 8 bit each
     * @param gif Not decoded byte-Array of a gif
     * @return {int[]} colorTable The
     */
    public int[] saveColorTable(byte[] gif) {
        if (gif.length <= header.length) {
            throw new IllegalArgumentException("Data is not a gif89a");
        }
        //check for gif Header
        for (int i = 0; i < header.length; i++) {
            if (header[i] != gif[i]) {
                throw new IllegalArgumentException("Data is not a gif89a");
            }
        }
        int[] colorTable = null;
        //check if globalcolorTable exists
        System.out.println(ByteHex.byteToHex(gif[10]));
        if((gif[10] & 0x80) == 0) {
            System.out.println("No globalcolorTable");
        }  else {
            System.out.println("GlobalColorTabel exists");
            System.out.println(ByteHex.byteToHex(gif[9]));
            int length =  (gif[10] & 0x7);
             colorTable = globalColorTable(gif,length);
        }
        return colorTable;
    }

    /**
     * Writes color Values from gif color Table into an array
     * @param gif An gif that has a color table
     * @param length Length of the color Table
     * @return int[] table with ARGB values
     */
    private int[] globalColorTable(byte[] gif, int length) {
        int i = 13;
        int[] table = new int[(int) Math.pow(2,length + 1)];
        for (int j = 0; j < table.length;j++) {
            int color = 0xFF;
            color = (color << 8) | gif[i];
            i++;
            color = (color << 8) | gif[i];
            i++;
            color = (color << 8) | gif[i];
            i++;
            table[j] = color;
        }
        return table;
    }

    /**
     * Splits the colorTable into Color Couples which have similar color Values. Alpha is
     * allways 00xF.
     * @param colorTable A color Table containing ARGB values
     * @return ColorCoupel[] Hash Map with a color as key and similar Colors as List values
     */
    public Map<Integer,List<Integer>> getColorCouples(int[] colorTable) {
        if(colorTable.length <= Math.pow(2,8)) {
        Map<Integer, List<Integer>> colorCouples = new HashMap<>();
        for (int i = 0; i < colorTable.length;i++) {
            List<Integer> couples = new ArrayList<>();
            boolean pixelIsOne = PixelBit.pixelIsOne(colorTable[i]);
            for (int j = 0; j < colorTable.length; j++) {
                if (i != j && colorTable[i] != colorTable[j]) {
                    int redI = getRed(colorTable[i]);
                    int redJ = getRed(colorTable[j]);
                    if (Math.abs(redI - redJ) <= 8 ) {
                        int greenI = getGreen(colorTable[i]);
                        if (Math.abs(greenI - getGreen(colorTable[j])) <= 8) {
                            if (Math.abs(getBlue(colorTable[i]) - getBlue(colorTable[j])) <= 8) {
                                if (pixelIsOne != PixelBit.pixelIsOne(colorTable[j])) {
                                    couples.add(colorTable[j]);
                                }
                            }
                        }
                    }

                }
            }
            if (couples.size() > 0) {
                colorCouples.put(colorTable[i],couples);
            }
        }
        return colorCouples;
        }
        throw new IllegalArgumentException("Array is not a color Table");
    }

        private int getRed(int color) {
        return (color >> 16) & 0xFF;
    }

    private int getGreen(int color) {
        return (color >> 8) & 0xFF;
    }

    private int getBlue(int color) {
        return  color & 0xFF;
    }
}
