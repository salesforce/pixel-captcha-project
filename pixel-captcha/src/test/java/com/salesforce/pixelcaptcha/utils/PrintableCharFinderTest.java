/*
 * Copyright (c) 2017, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license.
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */
package com.salesforce.pixelcaptcha.utils;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Gursev Singh Kalra @ Salesforce.com .
 */
public class PrintableCharFinderTest {

    PrintableCharFinder pcf ;
    @Before
    public void setUp() throws Exception {
        pcf = PrintableCharFinder.getInstance();

    }


    @Test
    public void testGetInstance() throws Exception {
        PrintableCharFinder a = PrintableCharFinder.getInstance();
        PrintableCharFinder b = PrintableCharFinder.getInstance();
        assertEquals(a, b);
    }

    @Test
    public void testIsPrintableChar() throws Exception {
        for(int i = 0; i < 30; i++) {
            assertFalse(pcf.isPrintableChar(i));
        }

        for(int i = 33; i< 40; i++) {
            assertTrue(pcf.isPrintableChar(i));
        }
    }

}