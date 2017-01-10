/*
 * Copyright (c) 2017, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license.
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */

package com.salesforce.pixelcaptcha.core;

import com.salesforce.pixelcaptcha.dataobj.CaptchaDimension;
import com.salesforce.pixelcaptcha.dataobj.ChallengeAndResponseCount;
import org.junit.Test;

import java.awt.*;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * @author Gursev Singh Kalra @ Salesforce.com.
 */
public class MasterConfigTest {
    private static final int H_WIDTH = 400;
    private static final int H_HEIGHT = 300;

    private static final int V_WIDTH = 300;
    private static final int V_HEIGHT = 400;
    CaptchaDimension cd;

    private CaptchaDimension getHorizontalDimension() {
        return new CaptchaDimension(H_WIDTH, H_HEIGHT);
    }

    private CaptchaDimension getVerticalDimension() {
        return new CaptchaDimension(V_WIDTH, V_HEIGHT);
    }

    private int[] getPrintableCodePoints() {
        return new int[]{33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50,
                51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80,
                81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100};
    }

    private MasterConfig getCaptchaMasterConfig(boolean isHorizontal) {
        CaptchaDimension captchaDimension;
        if (isHorizontal)
            captchaDimension = getHorizontalDimension();
        else
            captchaDimension = getVerticalDimension();

        int[] printableCodePoints = getPrintableCodePoints();
        boolean ordered = false;
        MasterConfig masterconfig = new MasterConfig(captchaDimension,
                printableCodePoints,
                ChallengeAndResponseCount.FOUR_TWELVE,
                ordered
        );
        return masterconfig;

    }

    @Test
    public void testPrintableCodePointsAreNotChanged() {
        boolean isHorizontal = true;
        MasterConfig masterconfig = getCaptchaMasterConfig(isHorizontal);
        Arrays.equals(getPrintableCodePoints(), masterconfig.getPrintableCodePoints());
    }


    @Test
    public void testHorizontalCapthaChallengeAndResponseRectanglesDoNotOverlapAndAreInsideTheCaptchaArea() {
        boolean isHorizontal = true;
        MasterConfig masterconfig = getCaptchaMasterConfig(isHorizontal);
        Rectangle challengeRectangle = masterconfig.getChallengeRectangle();
        Rectangle responseRectangle = masterconfig.getResponseRectangle();

        assertTrue(masterconfig.isHorizontalCaptcha());

        // Assert that the challenge and response rectangle do not intersect
        assertFalse(challengeRectangle.intersects(responseRectangle));

        //Make sure that challenge rectangle ends before response rectangle starts
        assertTrue(challengeRectangle.getX() + challengeRectangle.getWidth() < responseRectangle.getX());

        // Assert that the response rectangle's X coordinate is within the area
        assertTrue(responseRectangle.getX() + responseRectangle.width < masterconfig.getCaptchaDimensions().getWidth());

        // Assert that the challenge and response areas is vertically within the CAPTCHA dimensions
        assertTrue(challengeRectangle.getY() + challengeRectangle.getHeight() < masterconfig.getCaptchaDimensions().getHeight());
        assertTrue(responseRectangle.getY() + responseRectangle.getHeight() < masterconfig.getCaptchaDimensions().getHeight());

    }

    @Test
    public void testVerticalCapthaChallengeAndResponseRectanglesDoNotOverlapAndAreInsideTheCaptchaArea() {
        boolean isHorizontal = false;
        MasterConfig masterconfig = getCaptchaMasterConfig(isHorizontal);
        Rectangle challengeRectangle = masterconfig.getChallengeRectangle();
        Rectangle responseRectangle = masterconfig.getResponseRectangle();

        assertFalse(masterconfig.isHorizontalCaptcha());
        // Assert that the challenge and response rectangles do not intersect
        assertFalse(challengeRectangle.intersects(responseRectangle));

        //Make sure that challenge rectangle ends before response rectangle starts
        assertTrue(challengeRectangle.getY() + challengeRectangle.getHeight() < responseRectangle.getY());

        // Assert that the response rectangle's Y coordinate is within the area
        assertTrue(responseRectangle.getY() + responseRectangle.height < masterconfig.getCaptchaDimensions().getHeight());

        // Assert that the challenge and response areas is horizontally within the CAPTCHA dimensions
        assertTrue(challengeRectangle.getX() + challengeRectangle.getWidth() < masterconfig.getCaptchaDimensions().getWidth());
        assertTrue(responseRectangle.getX() + responseRectangle.getWidth() < masterconfig.getCaptchaDimensions().getWidth());

    }

    @Test
    public void testAMinMaxFontSizesAreAsExpectedForHorizontalCaptcha() throws Exception {
        boolean isHorizontal = true;
        MasterConfig masterconfig = getCaptchaMasterConfig(isHorizontal);
        int minFontSize = H_HEIGHT / MasterConfig.MIN_FONT_SIZE_DIVISOR;
        int maxFontSize = minFontSize * MasterConfig.MAX_FONT_SIZE_MULTIPLIER;
        assertEquals(minFontSize, masterconfig.getMinFontSize());
        assertEquals(maxFontSize, masterconfig.getMaxFontSize());

    }

    @Test
    public void testAMinMaxFontSizesAreAsExpectedForVerticalCaptcha() throws Exception {
        boolean isHorizontal = false;
        MasterConfig masterconfig = getCaptchaMasterConfig(isHorizontal);
        int minFontSize = V_WIDTH / MasterConfig.MIN_FONT_SIZE_DIVISOR;
        int maxFontSize = minFontSize * MasterConfig.MAX_FONT_SIZE_MULTIPLIER;
        assertEquals(minFontSize, masterconfig.getMinFontSize());
        assertEquals(maxFontSize, masterconfig.getMaxFontSize());

    }
}