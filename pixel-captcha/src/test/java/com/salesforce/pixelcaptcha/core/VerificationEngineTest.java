/*
 * Copyright (c) 2017, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license.
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */
package com.salesforce.pixelcaptcha.core;

import com.salesforce.pixelcaptcha.interfaces.ValidationResult;
import com.salesforce.pixelcaptcha.dataobj.CaptchaSolution;
import com.salesforce.pixelcaptcha.dataobj.impl.PixelCaptchaSolution;
import junit.framework.TestCase;
import org.junit.Before;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class VerificationEngineTest extends TestCase {

    VerificationEngine ve;
    List<Point> twoChallenges;
    private int maxDeviation = 100;


    private List<Point> getTwoChallenges() {
        List<Point> twoPointList = new ArrayList<Point>();
        twoPointList.add(new Point(100, 100));
        twoPointList.add(new Point(200, 200));
        return twoPointList;
    }

    private List<Point> getTwoResponsesYDelta(List<Point> reference, int delta) {
        List<Point> twoPointList = new ArrayList<Point>();
        for (Point p : reference) {
            twoPointList.add(new Point(p.x, p.y + delta));
        }
        return twoPointList;
    }

    private List<Point> getTwoResponsesXDelta(List<Point> reference, int delta) {
        List<Point> twoPointList = new ArrayList<Point>();
        for (Point p : reference) {
            twoPointList.add(new Point(p.x + delta, p.y));
        }
        return twoPointList;
    }


    @Before
    public void setUp() throws Exception {
        ve = VerificationEngine.getInstance();
        twoChallenges = getTwoChallenges();
    }

    public void testValidateSolutionWithTwoChallengesOrdered() throws Exception {
        int deviation = maxDeviation / 4;
        CaptchaSolution cs = mock(PixelCaptchaSolution.class);
        when(cs.getMaxDeviation()).thenReturn((double) maxDeviation);
        when(cs.isOrdered()).thenReturn(true);
        when(cs.getPoints()).thenReturn(twoChallenges);

        List<Point> deviated = getTwoResponsesXDelta(twoChallenges, deviation);
        ValidationResult result = ve.verifySolution(cs, deviated);
        assertEquals(true, result.isPositive());

        deviated = getTwoResponsesXDelta(twoChallenges, deviation);
        Point p = deviated.get(0);
        deviated.remove(0);
        deviated.add(p); // Add to end
        result = ve.verifySolution(cs, deviated);
        assertEquals(false, result.isPositive());

        deviated = getTwoResponsesXDelta(twoChallenges, deviation * 2);
        result = ve.verifySolution(cs, deviated);
        assertEquals(true, result.isPositive());

        // More distance
        deviated = getTwoResponsesXDelta(twoChallenges, (int) (deviation * 3));
        result = ve.verifySolution(cs, deviated);
        assertEquals(false, result.isPositive());

        deviated = getTwoResponsesYDelta(twoChallenges, deviation * 2);
        result = ve.verifySolution(cs, deviated);
        assertEquals(true, result.isPositive());

        // More distance
        deviated = getTwoResponsesYDelta(twoChallenges, (int) (deviation * 3));
        result = ve.verifySolution(cs, deviated);
        assertEquals(false, result.isPositive());

    }

    public void testValidateSolutionWithTwoChallengesUnOrdered() throws Exception {
        int deviation = maxDeviation / 4;
        CaptchaSolution cs = mock(PixelCaptchaSolution.class);
        when(cs.getMaxDeviation()).thenReturn((double) maxDeviation);
        when(cs.isOrdered()).thenReturn(false);
        when(cs.getPoints()).thenReturn(twoChallenges);

        List<Point> deviated;
        ValidationResult result;

        deviated = getTwoResponsesXDelta(twoChallenges, deviation);
        Point p = deviated.get(0);
        deviated.remove(0);
        deviated.add(p); // Add to end
        result = ve.verifySolution(cs, deviated);
        assertEquals(true, result.isPositive());
    }

    public void testValidateSolutionWithTwoChallengesUnOrderedNegativePoints() throws Exception {
        CaptchaSolution cs = mock(PixelCaptchaSolution.class);
        when(cs.getMaxDeviation()).thenReturn((double) maxDeviation);
        when(cs.isOrdered()).thenReturn(false);
        when(cs.getPoints()).thenReturn(twoChallenges);

        List<Point> negatives = new ArrayList<>();
        ValidationResult result;
        negatives.add(new Point(-1, -1));
        negatives.add(new Point(-1, 200));

        result = ve.verifySolution(cs, negatives);
        assertEquals(false, result.isPositive());
    }

    public void testValidateSolutionWithDifferentLengths() throws Exception {
        int deviation = maxDeviation / 4;
        CaptchaSolution cs = mock(PixelCaptchaSolution.class);
        when(cs.getMaxDeviation()).thenReturn((double) maxDeviation);
        when(cs.isOrdered()).thenReturn(true);
        when(cs.getPoints()).thenReturn(twoChallenges);

        List<Point> deviated = getTwoResponsesXDelta(twoChallenges, deviation);
        deviated.add(new Point(1, 1));
        ValidationResult result = ve.verifySolution(cs, deviated);
        assertEquals(false, result.isPositive());

        deviated = getTwoResponsesXDelta(twoChallenges, deviation * 2);
        deviated.remove(0);
        assertEquals(false, result.isPositive());
    }


}