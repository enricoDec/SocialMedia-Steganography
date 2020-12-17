package steganography.audio;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class BitByteConverterTest {

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                  Byte to Bits
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    public void byteToBitsTest_PositiveMin() {
        byte[] expected = new byte[] {0, 0, 0, 0, 0, 0, 0, 1};

        byte b = 1;
        byte[] actual = BitByteConverter.byteToBits(b);

        Assertions.assertTrue(Arrays.equals(expected, actual));
    }

    @Test
    public void byteToBitsTest_Positive() {
        byte[] expected = new byte[] {0, 0, 0, 0, 0, 1, 0, 0};

        byte b = 4;
        byte[] actual = BitByteConverter.byteToBits(b);

        Assertions.assertTrue(Arrays.equals(expected, actual));
    }

    @Test
    public void byteToBitsTest_PositiveMax() {
        byte[] expected = new byte[] {0, 1, 1, 1, 1, 1, 1, 1};

        byte b = 127;
        byte[] actual = BitByteConverter.byteToBits(b);

        Assertions.assertTrue(Arrays.equals(expected, actual));
    }

    @Test
    public void byteToBitsTest_0() {
        byte[] expected = new byte[] {0, 0, 0, 0, 0, 0, 0, 0};

        byte b = 0;
        byte[] actual = BitByteConverter.byteToBits(b);

        Assertions.assertTrue(Arrays.equals(expected, actual));
    }

    @Test
    public void byteToBitsTest_NegativeMin() {
        byte[] expected = new byte[] {1, 1, 1, 1, 1, 1, 1, 1};

        byte b = -1;
        byte[] actual = BitByteConverter.byteToBits(b);

        Assertions.assertTrue(Arrays.equals(expected, actual));
    }

    @Test
    public void byteToBitsTest_Negative() {
        byte[] expected = new byte[] {1, 0, 0, 0, 0, 1, 0, 0};

        byte b = -124;
        byte[] actual = BitByteConverter.byteToBits(b);

        Assertions.assertTrue(Arrays.equals(expected, actual));
    }

    @Test
    public void byteToBitsTest_NegativeMax() {
        byte[] expected = new byte[] {1, 0, 0, 0, 0, 0, 0, 0};

        byte b = -128;
        byte[] actual = BitByteConverter.byteToBits(b);

        Assertions.assertTrue(Arrays.equals(expected, actual));
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                            Byte array to Bits
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    public void byteToBitsTest_SingleByte_PositiveMin() {
        byte[][] expected = new byte[][] {
                new byte[] {0, 0, 0, 0, 0, 0, 0, 1}
        };

        byte[] b = new byte[] {1};
        byte[][] actual = BitByteConverter.byteToBits(b);

        if (expected.length != actual.length)
            Assertions.fail("Arrays have different lengths.");

        for (int i = 0; i < actual.length; i++) {
            Assertions.assertTrue(Arrays.equals(expected[i], actual[i]));
        }
    }

    @Test
    public void byteToBitsTest_SingleByte_Positive() {
        byte[][] expected = new byte[][] {
                new byte[] {0, 0, 0, 0, 0, 1, 0, 0}
        };

        byte[] b = new byte[] {4};
        byte[][] actual = BitByteConverter.byteToBits(b);

        if (expected.length != actual.length)
            Assertions.fail("Arrays have different lengths.");

        for (int i = 0; i < actual.length; i++) {
            Assertions.assertTrue(Arrays.equals(expected[i], actual[i]));
        }
    }

    @Test
    public void byteToBitsTest_SingleByte_PositiveMax() {
        byte[][] expected = new byte[][] {
                new byte[] {0, 1, 1, 1, 1, 1, 1, 1}
        };

        byte[] b = new byte[] {127};
        byte[][] actual = BitByteConverter.byteToBits(b);

        if (expected.length != actual.length)
            Assertions.fail("Arrays have different lengths.");

        for (int i = 0; i < actual.length; i++) {
            Assertions.assertTrue(Arrays.equals(expected[i], actual[i]));
        }
    }

    @Test
    public void byteToBitsTest_SingleByte_0() {
        byte[][] expected = new byte[][] {
                new byte[] {0, 0, 0, 0, 0, 0, 0, 0}
        };

        byte[] b = new byte[] {0};
        byte[][] actual = BitByteConverter.byteToBits(b);

        if (expected.length != actual.length)
            Assertions.fail("Arrays have different lengths.");

        for (int i = 0; i < actual.length; i++) {
            Assertions.assertTrue(Arrays.equals(expected[i], actual[i]));
        }
    }

    @Test
    public void byteToBitsTest_SingleByte_NegativeMin() {
        byte[][] expected = new byte[][] {
                new byte[] {1, 1, 1, 1, 1, 1, 1, 1}
        };

        byte[] b = new byte[] {-1};
        byte[][] actual = BitByteConverter.byteToBits(b);

        if (expected.length != actual.length)
            Assertions.fail("Arrays have different lengths.");

        for (int i = 0; i < actual.length; i++) {
            Assertions.assertTrue(Arrays.equals(expected[i], actual[i]));
        }
    }

    @Test
    public void byteToBitsTest_SingleByte_Negative() {
        byte[][] expected = new byte[][] {
                new byte[] {1, 0, 0, 0, 0, 1, 0, 0}
        };

        byte[] b = new byte[] {-124};
        byte[][] actual = BitByteConverter.byteToBits(b);

        if (expected.length != actual.length)
            Assertions.fail("Arrays have different lengths.");

        for (int i = 0; i < actual.length; i++) {
            Assertions.assertTrue(Arrays.equals(expected[i], actual[i]));
        }
    }

    @Test
    public void byteToBitsTest_SingleByte_NegativeMax() {
        byte[][] expected = new byte[][] {
                new byte[] {1, 0, 0, 0, 0, 0, 0, 0}
        };

        byte[] b = new byte[] {-128};
        byte[][] actual = BitByteConverter.byteToBits(b);

        if (expected.length != actual.length)
            Assertions.fail("Arrays have different lengths.");

        for (int i = 0; i < actual.length; i++) {
            Assertions.assertTrue(Arrays.equals(expected[i], actual[i]));
        }
    }

    @Test
    public void byteToBitsTest_MultipleBytes() {
        byte[][] expected = new byte[][] {
                new byte[] {1, 0, 0, 0, 0, 0, 0, 0},    // -128
                new byte[] {1, 0, 0, 0, 0, 1, 0, 0},    // -124
                new byte[] {1, 1, 1, 1, 1, 1, 1, 1},    // -1
                new byte[] {0, 0, 0, 0, 0, 0, 0, 0},    // 0
                new byte[] {0, 0, 0, 0, 0, 0, 0, 1},    // 1
                new byte[] {0, 0, 0, 0, 0, 1, 0, 0},    // 4
                new byte[] {0, 1, 1, 1, 1, 1, 1, 1}     // 127
        };

        byte[] b = new byte[] {-128, -124, -1, 0, 1, 4, 127};
        byte[][] actual = BitByteConverter.byteToBits(b);

        if (expected.length != actual.length)
            Assertions.fail("Arrays have different lengths.");

        for (int i = 0; i < actual.length; i++) {
            Assertions.assertTrue(Arrays.equals(expected[i], actual[i]));
        }
    }

    @Test
    public void byteToBitsTest_null() {
        Assertions.assertThrows(NullPointerException.class, () ->
                BitByteConverter.byteToBits(null)
        );
    }

    @Test
    public void byteToBitsTest_emptyArray1() {
        byte[][] expected = new byte[][] {
                new byte[] {0, 0, 0, 0, 0, 0, 0, 0}
        };

        byte[] b = new byte[] {};
        byte[][] actual = BitByteConverter.byteToBits(b);

        if (expected.length != actual.length)
            Assertions.fail("Arrays have different lengths.");

        for (int i = 0; i < actual.length; i++) {
            Assertions.assertTrue(Arrays.equals(expected[i], actual[i]));
        }
    }

    @Test
    public void byteToBitsTest_emptyArray2() {
        byte[][] expected = new byte[][] {
                new byte[] {0, 0, 0, 0, 0, 0, 0, 0}
        };

        byte[] b = new byte[1];
        byte[][] actual = BitByteConverter.byteToBits(b);

        if (expected.length != actual.length)
            Assertions.fail("Arrays have different lengths.");

        for (int i = 0; i < actual.length; i++) {
            Assertions.assertTrue(Arrays.equals(expected[i], actual[i]));
        }
    }

    @Test
    public void byteToBitsTest_emptyArray3() {
        byte[][] expected = new byte[][] {
                new byte[] {0, 0, 0, 0, 0, 0, 0, 0},
                new byte[] {0, 0, 0, 0, 0, 0, 0, 0},
                new byte[] {0, 0, 0, 0, 0, 0, 0, 0},
                new byte[] {0, 0, 0, 0, 0, 0, 0, 0},
                new byte[] {0, 0, 0, 0, 0, 0, 0, 0},
                new byte[] {0, 0, 0, 0, 0, 0, 0, 0},
                new byte[] {0, 0, 0, 0, 0, 0, 0, 0},
                new byte[] {0, 0, 0, 0, 0, 0, 0, 0},
                new byte[] {0, 0, 0, 0, 0, 0, 0, 0},
                new byte[] {0, 0, 0, 0, 0, 0, 0, 0}
        };

        byte[] b = new byte[10];
        byte[][] actual = BitByteConverter.byteToBits(b);

        if (expected.length != actual.length)
            Assertions.fail("Arrays have different lengths.");

        for (int i = 0; i < actual.length; i++) {
            Assertions.assertTrue(Arrays.equals(expected[i], actual[i]));
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                            Bit array to Byte
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    public void bitsToByte_PositiveMin() {
        byte expected = 1;

        byte[] b = new byte[] {0, 0, 0, 0, 0, 0, 0, 1};
        byte actual = BitByteConverter.bitsToByte(b);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void bitsToByte_Positive() {
        byte expected = 4;

        byte[] b = new byte[] {0, 0, 0, 0, 0, 1, 0, 0};
        byte actual = BitByteConverter.bitsToByte(b);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void bitsToByte_PositiveMax() {
        byte expected = 127;

        byte[] b = new byte[] {0, 1, 1, 1, 1, 1, 1, 1};
        byte actual = BitByteConverter.bitsToByte(b);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void bitsToByte_0() {
        byte expected = 0;

        byte[] b = new byte[] {0, 0, 0, 0, 0, 0, 0, 0};
        byte actual = BitByteConverter.bitsToByte(b);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void bitsToByte_NegativeMin() {
        byte expected = -1;

        byte[] b = new byte[] {1, 1, 1, 1, 1, 1, 1, 1};
        byte actual = BitByteConverter.bitsToByte(b);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void bitsToByte_Negative() {
        byte expected = -124;

        byte[] b = new byte[] {1, 0, 0, 0, 0, 1, 0, 0};
        byte actual = BitByteConverter.bitsToByte(b);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void bitsToByte_NegativeMax() {
        byte expected = -128;

        byte[] b = new byte[] {1, 0, 0, 0, 0, 0, 0, 0};
        byte actual = BitByteConverter.bitsToByte(b);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void bitsToByte_EmptyArray1_ExpectIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                BitByteConverter.bitsToByte(new byte[] {})
        );
    }

    @Test
    public void bitsToByte_EmptyArray2_ExpectIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                BitByteConverter.bitsToByte(new byte[1])
        );
    }

    @Test
    public void bitsToByte_EmptyArray3_ExpectIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                BitByteConverter.bitsToByte(new byte[10])
        );
    }

    @Test
    public void bitsToByte_EmptyArrayLengthEight_ExpectZero() {
        Assertions.assertEquals(0, BitByteConverter.bitsToByte(new byte[8]));
    }

    @Test
    public void bitsToByte_ArrayHasWrongLength_ExpectIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                BitByteConverter.bitsToByte(new byte[] {0, 1})
        );
    }

    @Test
    public void bitsToByte_ArrayDoesNotContainBits_ExpectIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                BitByteConverter.bitsToByte(new byte[] {0, 1, 0, 0, 1, 1, 0, 14})
        );
    }

    @Test
    public void bitsToByte_ArrayIsNull_ExpectNullPointerException() {
        Assertions.assertThrows(NullPointerException.class, () ->
                BitByteConverter.bitsToByte(null)
        );
    }
}
