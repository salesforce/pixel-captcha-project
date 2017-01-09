/*
 * Copyright (c) 2017, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license.
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */

package com.salesforce.pixelcaptcha.core;

import com.salesforce.pixelcaptcha.dataobj.CaptchaDimension;
import com.salesforce.pixelcaptcha.dataobj.CaptchaMetadata;
import com.salesforce.pixelcaptcha.dataobj.ChallengeAndResponseCount;
import com.salesforce.pixelcaptcha.dataobj.PointProperty;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Gursev Singh Kalra @ Salesforce.com.
 */
public class CaptchaMetadataFactoryTest {
    private int[] printableCodePoints = {33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50,
            51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80,
            81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100};
    private Rectangle challengeRectangle = new Rectangle(30, 40, 50, 240);
    private Rectangle responseRectangle = new Rectangle(30 + 50, 40, 300, 240);
    private int minFontSize = 20;
    private int maxFontSize = 50;
    private CaptchaMetadata captchaMetadata;
    private List<PointProperty> ppList;
    private int averageFontSize;

    CaptchaMetadataFactory captchaMetadataFactory;
    @Before
    public void setUp() throws Exception {
        MasterConfig masterConfig;
        ChallengeAndResponseCount challengeAndResponseCount = ChallengeAndResponseCount.FOUR_TWELVE;
        CaptchaDimension captchaDimensions = new CaptchaDimension(400, 300);
        averageFontSize = (minFontSize + maxFontSize)/2;
        boolean isHorizontal = true;
        boolean isOrdered = false;
        masterConfig = mock(MasterConfig.class);

        // Add to return customPrintableCodePoints
        when(masterConfig.getAvgFontSize()).thenReturn(averageFontSize);
        when(masterConfig.getMinFontSize()).thenReturn(minFontSize);
        when(masterConfig.getMaxFontSize()).thenReturn(maxFontSize);
        when(masterConfig.getChallengeResponseCount()).thenReturn(challengeAndResponseCount);
        when(masterConfig.getChallengeRectangle()).thenReturn(challengeRectangle);
        when(masterConfig.getResponseRectangle()).thenReturn(responseRectangle);
        when(masterConfig.isOrdered()).thenReturn(isOrdered);
        when(masterConfig.isHorizontalCaptcha()).thenReturn(isHorizontal);
        when(masterConfig.getCaptchaDimensions()).thenReturn(captchaDimensions);
        when(masterConfig.getPrintableCodePoints()).thenReturn(printableCodePoints);
        captchaMetadataFactory = new CaptchaMetadataFactory(masterConfig);
        captchaMetadata = captchaMetadataFactory.getCaptchaMetadata();

    }

    @Test
    public void testChallengePointsAreInChallengeRectangle() throws Exception {
        ppList = captchaMetadata.getChallenge();
        for(int i = 0; i < ppList.size(); i++ ) {
            Point p = ppList.get(i).getPoint();
            assertTrue(challengeRectangle.contains(p.x, p.y));
        }
    }

    @Test
    public void testSolutionPointsAreInResponseRectangle() throws Exception {
        ppList = captchaMetadata.getSolutionOptions();
        for(int i = 0; i < ppList.size(); i++ ) {
            Point p = ppList.get(i).getPoint();
            assertTrue(responseRectangle.contains(p.x, p.y));
        }
    }


    @Test
    public void testSolutionFontSizeIsWithinRange() throws Exception {
        ppList = captchaMetadata.getSolutionOptions();
        for(int i = 0; i < ppList.size(); i++ ) {
            Font f = ppList.get(i).getFont();
            assertTrue(f.getSize() >= minFontSize);
            assertTrue(f.getSize() <= maxFontSize);
        }
    }

    @Test
    public void testChallengeFontSizeIsWithinRange() throws Exception {
        ppList = captchaMetadata.getChallenge();
        for(int i = 0; i < ppList.size(); i++ ) {
            Font f = ppList.get(i).getFont();
            assertTrue(f.getSize() >= averageFontSize);
            assertTrue(f.getSize() <= maxFontSize);
        }
    }

    @Test
    public void testSolutionCodePointsAreAsExpected() throws Exception {
        ppList = captchaMetadata.getSolutionOptions();
        for(int i = 0; i < ppList.size(); i++ ) {
            String s = ppList.get(i).getStringToWrite();
            assertTrue(s.length() == 1);
            assertTrue(ArrayUtils.contains(printableCodePoints, s.charAt(0)));
        }
    }

}