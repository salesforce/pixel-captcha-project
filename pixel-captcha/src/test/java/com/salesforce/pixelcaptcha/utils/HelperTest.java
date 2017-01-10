/*
 * Copyright (c) 2017, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license.
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */
package com.salesforce.pixelcaptcha.utils;

import com.salesforce.pixelcaptcha.core.GlobalConstants;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Gursev Singh Kalra @ Salesforce.com
 */
public class HelperTest {

    @Test(expected = IllegalArgumentException.class)
    public void testShuffleIntArrayWithNullArray() throws Exception {
        Helper.shuffleIntArray(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testShuffleIntArrayWithEmptyArray() throws Exception {
        Helper.shuffleIntArray(new int[]{});
    }

    @Test
    public void testConvertIntegerListToIntArray() throws Exception {
        int[] a = {1, 2, 3};
        List<Integer> l = new ArrayList<>();
        for (int temp : a) {
            l.add(new Integer(temp));
        }
        assertArrayEquals(a, Helper.convertIntegerListToIntArray(l));

    }

    @Test
    public void testValidateCodePointValues() throws Exception {
        int[] cpv = {-1, 2, 3};
        try {
            Helper.validateCodePointValues(cpv);
            Helper.validateCodePointValues(new int[]{0, GlobalConstants.MAX_ALLOWED_CODE_POINT + 1});
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "Code point must be between " + GlobalConstants.MIN_ALLOWED_CODE_POINT + " and " + GlobalConstants.MAX_ALLOWED_CODE_POINT);
        }

    }

    @Test
    public void testConstructCodePointArrayFomMinMaxValuesOneToTen() throws Exception {
        int min = 1;
        int max = 10;
        int[] array = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        assertTrue(Arrays.equals(Helper.constructCodePointArrayFomMinMaxValues(min, max), array));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructCodePointArrayFomMinMaxValuesNegativeMinThrowsException() throws Exception {
        int min = -1;
        int max = 10;
        Helper.constructCodePointArrayFomMinMaxValues(min, max);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructCodePointArrayFomMinMaxValuesMaxLessThanMin() throws Exception {
        int min = -1;
        int max = 10;
        assertNotNull(Helper.constructCodePointArrayFomMinMaxValues(10, -1));
    }


    @Test
    public void testConvertStringToIntOrDefault() throws Exception {
        assertEquals(1, Helper.convertStringToIntOrDefault("1", 1));
        assertNotEquals(1, Helper.convertStringToIntOrDefault("1.1", 2));
        assertEquals(2, Helper.convertStringToIntOrDefault("1.1", 2));
    }

    @Test
    public void testConvertCodePointsToSortedIntArrayNoDuplicates() throws Exception {
        int[] expected = {1, 2, 3};
        int[] actual;
        assertArrayEquals(expected, Helper.convertCodePointsToSortedIntArrayNoDuplicates(null, "1-3"));
        assertArrayEquals(expected, Helper.convertCodePointsToSortedIntArrayNoDuplicates("1-3", "4-9"));
        expected = new int[]{1, 2, 3, 4, 5};
        assertArrayEquals(expected, Helper.convertCodePointsToSortedIntArrayNoDuplicates("1-3, 3-5", "4-9"));
        assertArrayEquals(expected, Helper.convertCodePointsToSortedIntArrayNoDuplicates(", 1-3, 3-5, 3", "4-9"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConvertCodePointsToSortedIntArrayNoDuplicatesBothNull() throws Exception {
        Helper.convertCodePointsToSortedIntArrayNoDuplicates(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConvertCodePointsToSortedIntArrayNoDuplicatesFirstBlankSecondNull() throws Exception {
        Helper.convertCodePointsToSortedIntArrayNoDuplicates("", "");
    }

}