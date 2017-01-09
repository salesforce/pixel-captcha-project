/*
 * Copyright (c) 2017, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license.
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */
package com.salesforce.pixelcaptcha.utils;

import org.junit.Test;

import java.awt.*;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Gursev Singh Kalra @ Salesforce.com
 */
public class RandomFontFactoryTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testUnmodifiableFONTS() throws Exception {
        List<String> s = RandomFontFactory.FONTS_SUPPORTED;
        s.add(Font.SANS_SERIF);
    }

    @Test
    public void testGetRandomFont() throws Exception {

    }

    @Test(expected = IllegalArgumentException.class)
    public void testEnforceMinFontSize() throws Exception {
        RandomFontFactory rff = new RandomFontFactory.Builder()
                .minFontSize(RandomFontFactory.MIN_SUPPORTED_FONT_SIZE - 1) //Using larger minimum font size for challenge characters for better clarity and usability
                .maxFontSize(RandomFontFactory.MIN_SUPPORTED_FONT_SIZE + 1)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEnforceMaxFontSize() throws Exception {
        RandomFontFactory rff = new RandomFontFactory.Builder()
                .minFontSize(RandomFontFactory.MIN_SUPPORTED_FONT_SIZE + 1) //Using larger minimum font size for challenge characters for better clarity and usability
                .maxFontSize(RandomFontFactory.MAX_SUPPORTED_FONT_SIZE + 1)
                .build();
    }

    @Test
    public void testEnforceCheckFontSize() throws Exception {
        RandomFontFactory rff = new RandomFontFactory.Builder()
                .minFontSize(RandomFontFactory.MIN_SUPPORTED_FONT_SIZE) //Using larger minimum font size for challenge characters for better clarity and usability
                .maxFontSize(RandomFontFactory.MAX_SUPPORTED_FONT_SIZE/2)
                .build();
        for(int i =0; i < 50; i++) {
            assertTrue(rff.getRandomFont().getSize() >= RandomFontFactory.MIN_SUPPORTED_FONT_SIZE);
            assertTrue(rff.getRandomFont().getSize() <= RandomFontFactory.MAX_SUPPORTED_FONT_SIZE / 2);
        }
    }

    @Test
    public void testRandomFontIsNotNull() throws Exception {
        RandomFontFactory rff = new RandomFontFactory.Builder()
                .minFontSize(RandomFontFactory.MIN_SUPPORTED_FONT_SIZE) //Using larger minimum font size for challenge characters for better clarity and usability
                .maxFontSize(RandomFontFactory.MAX_SUPPORTED_FONT_SIZE/2)
                .build();
        assertNotNull(rff.getRandomFont());
    }






}