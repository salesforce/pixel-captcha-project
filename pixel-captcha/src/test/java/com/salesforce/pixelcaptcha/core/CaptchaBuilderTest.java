/*
 * Copyright (c) 2017, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license.
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */
package com.salesforce.pixelcaptcha.core;

import com.salesforce.pixelcaptcha.dataobj.CaptchaDimension;
import com.salesforce.pixelcaptcha.dataobj.CaptchaMetadata;
import com.salesforce.pixelcaptcha.dataobj.PointProperty;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Gursev Singh Kalra @ Salesforce.com
 */
public class CaptchaBuilderTest {
    CaptchaMetadata captchaMetadata;

    public List<PointProperty> getPointProperties() {
        List<PointProperty> list = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            PointProperty pp = mock(PointProperty.class);
            when(pp.getFont()).thenReturn(new Font(Font.SANS_SERIF, Font.BOLD, i + 10));
            when(pp.getStringToWrite()).thenReturn(Integer.toString(i));
            when(pp.getPoint()).thenReturn(new Point(i * 10, i * 10));
            when(pp.getColor()).thenReturn(Color.BLACK);
            list.add(pp);
        }
        return list;
    }

    @Before
    public void setUp() throws Exception {
        List<PointProperty> list = getPointProperties();
        captchaMetadata = mock(CaptchaMetadata.class);

        //Takes care of getBufferedImage
        when(captchaMetadata.getCaptchaDimension()).thenReturn(new CaptchaDimension(200, 100));
        when(captchaMetadata.getImageType()).thenReturn(GlobalConstants.DEFAULT_IMAGE_TYPE);

        when(captchaMetadata.getBackgroundColor()).thenReturn(Color.WHITE);
        when(captchaMetadata.getChallenge()).thenReturn(list);

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testBuildImageWithCollapsedChallengeHorizontal() throws Exception {
        BufferedImage bi = CaptchaBuilder.buildImageWithCollapsedChallenge(captchaMetadata);
        assertNotNull(bi);
        assertEquals(200, bi.getWidth());
        assertEquals(100, bi.getHeight());
    }
}