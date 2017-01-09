package com.salesforce.pixelcaptcha.utils.impl;

import com.salesforce.pixelcaptcha.utils.RandomStringFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Gursev Singh Kalra @ Salesforce.com
 */
public class RandomUnicodeStringFactoryImplTest {
    RandomStringFactory rsf;

    @Before
    public void setUp() throws Exception {
        rsf = new RandomUnicodeStringFactoryImpl(new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
                21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50,
                51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80,
                81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100});

    }

    @After
    public void tearDown() throws Exception {

    }


    @Test(expected = IllegalArgumentException.class)
    public void testWithNullArray() throws Exception {
        new RandomUnicodeStringFactoryImpl(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithZeroLengthArray() throws Exception {
        new RandomUnicodeStringFactoryImpl(new int[]{});

    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetStringMoreThanMaxLengthThrowsExceptionNoFont() throws Exception {
        rsf.getString(RandomUnicodeStringFactoryImpl.MAX_LENGTH + 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetStringZeroLengthThrowsExceptionNoFont() throws Exception {
        rsf.getString(0);
    }

    @Test
    public void testGetStringNoFont() throws Exception {
        assertNotNull(rsf.getString(100));
        assertNotNull(rsf.getString(1));

    }

    @Test
    public void testCodePointsWithinMinMax() throws Exception {
        String s = rsf.getString(RandomUnicodeStringFactoryImpl.MAX_LENGTH);
        for(int i = 0; i < RandomUnicodeStringFactoryImpl.MAX_LENGTH; i++) {
            assertTrue(s.charAt(i) >= 1 && s.charAt(i) <= 100);
        }
    }

    @Test
    public void testGetOneCharStringNoArgs() throws Exception {
        assertNotNull(rsf.getOneCharString());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetOneCharStringFontArgument() throws Exception {
        rsf.getOneCharString(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetStringFontArgument() throws Exception {
        rsf.getString(null, 10);
    }
}