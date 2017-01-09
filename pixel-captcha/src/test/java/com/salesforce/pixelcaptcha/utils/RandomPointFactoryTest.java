/*
 * Copyright (c) 2017, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license.
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */
package com.salesforce.pixelcaptcha.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.*;

import static org.junit.Assert.*;

/**
 * @author Gursev Singh Kalra @ Salesforce.com
 */
public class RandomPointFactoryTest {
    RandomPointFactory rpf;
    Rectangle testRectangle;
    int MAX = 100;
    @Before
    public void setUp() throws Exception {
        rpf = new RandomPointFactory();
        testRectangle = new Rectangle(0, 0, MAX, MAX);
    }

    @After
    public void tearDown() throws Exception {

    }

    private void checkThatFirstVChallengePointIsInExpectedRegion(int challengeCount) {
        Point p;
        for(int i = 0; i < 10; i++) {
            p = rpf.getFirstVChallengePointFromTopInRegion(testRectangle, challengeCount);
            assertTrue(p.x >= testRectangle.x);
            assertTrue(p.y >= testRectangle.y);
            assertTrue(p.x <= testRectangle.x + testRectangle.width);
            assertTrue(p.y <= testRectangle.y + testRectangle.height / challengeCount);
        }
    }

    @Test
    public void testGetFirstVChallengePointFromTopInRegionThreeChallenges() throws Exception {
        checkThatFirstVChallengePointIsInExpectedRegion(3);
        checkThatFirstVChallengePointIsInExpectedRegion(4);
        checkThatFirstVChallengePointIsInExpectedRegion(5);
    }

    private Font getNewFont(int fontSize) {
        return new Font(Font.SERIF, Font.PLAIN, fontSize);
    }

    private boolean isPointInRectangle(Rectangle rectangle, Point point) {
        boolean result = true;
        // Not using rectangle.contains(point) because it returns false when the point is at the boundary

        if(point.x < rectangle.x || point.x > rectangle.x + rectangle.width )
            result = false;

        if(point.y < rectangle.y || point.y > rectangle.y + rectangle.height )
            result = false;
        return result;

    }

    private boolean isPointInsideRectangleAndNextYGreaterThanReferenceY(Rectangle rectangle, Point referencePoint, Point nextPoint) {

        if(!isPointInRectangle(rectangle, nextPoint))
            return false;

        // new y value must be greater than or equal to the reference value
        if(nextPoint.y < referencePoint.y)
            return false;

        return true;
    }

    /**
     * The way to test getVChallengePointWrtPercentageSizeOfReferenceFont appears the following:
     * 1. Get random Point within a rectangle
     * 2. Choose two incremental Fonts for new and reference point
     * 3. Get a new VChallenge point
     * 4. Do the following comparison for the new font: x is always within the rectangle and <br/>
     * new y is always greater than or equal to reference y, plus y is within the rectangle
     * @throws Exception
     */
    @Test
    public void testGetVChallengePointWrtPercentageSizeOfReferenceFont() throws Exception {
        Point temp;
        Font referenceFont;
        Font nextFont;
        Point referencePoint;
        // Get 1000 random points in the rectangle, and for each of those, create two fonts, get value of next point and check if it follows the conditions.
        for(int i = 0; i < 1000; i++) {
            referencePoint = rpf.getRandomPoint(testRectangle);
            for (int reference = 10; reference < MAX / 2; reference += 5) {
                for (int next = 10; next < MAX / 2; next += 5) {
                    referenceFont = getNewFont(reference);
                    nextFont = getNewFont(next);
                    temp = rpf.getVChallengePointWrtPercentageSizeOfReferenceFont(testRectangle, referencePoint, referenceFont, nextFont);
                    assertTrue(isPointInsideRectangleAndNextYGreaterThanReferenceY(testRectangle, referencePoint, temp));
                }
            }
        }

    }

    private boolean isPointInsideRectangleAndNextXGreaterThanReferenceX(Rectangle rectangle, Point referencePoint, Point nextPoint) {
        if(!isPointInRectangle(rectangle, nextPoint))
            return false;

        // new y value must be greater than or equal to the reference value
        if(nextPoint.x < referencePoint.x)
            return false;

        return true;

    }

    @Test
    public void testGetHChallengePointWrtPercentageSizeOfReferenceFont() throws Exception {
        Point temp;
        Font referenceFont;
        Font nextFont;
        Point referencePoint;
        // Get 1000 random points in the rectangle, and for each of those, create two fonts, get value of next point and check if it follows the conditions.
        for(int i = 0; i < 1000; i++) {
            referencePoint = rpf.getRandomPoint(testRectangle);
            for (int reference = 10; reference < MAX / 2; reference += 5) {
                for (int next = 10; next < MAX / 2; next += 5) {
                    referenceFont = getNewFont(reference);
                    nextFont = getNewFont(next);
                    temp = rpf.getHChallengePointWrtPercentageSizeOfReferenceFont(testRectangle, referencePoint, referenceFont, nextFont);
                    assertTrue(isPointInsideRectangleAndNextXGreaterThanReferenceX(testRectangle, referencePoint, temp));
                }
            }
        }

    }


    @Test
    public void testGetRandomPoint() throws Exception {
        Point p;
        for(int i = 0; i< 100; i++) {
            p = rpf.getRandomPoint(testRectangle);
            assertTrue(p.getX() <= MAX);
            assertTrue(p.getY() <= MAX);
        }
    }

    @Test
    public void testGetRandomPointWithDistance() throws Exception {
        java.util.List<Point> lp = new ArrayList<>();
        // This should work because the when the list is empty, the method must function like a getRandomPoint and return a point.
        assertNotNull(rpf.getRandomPointWithDistance(testRectangle, lp, MAX * 2));
    }

    @Test(expected = RuntimeException.class)
    public void testGetRandomPointWithDistanceLargeDistance() throws Exception {
        java.util.List<Point> lp = new ArrayList<>();
        lp.add(rpf.getRandomPoint(testRectangle));

        // It should not be able to get any point at distance greater than Pythagoras. Using MAX * 2 for simplicity.
        rpf.getRandomPointWithDistance(testRectangle, lp, MAX * 2);

    }

    // As each point is at distance of sqrt(2) from each other, <br/>
    // this test essentially gets all the possible points in the rectangle
    @Test
    public void testGetRandomPointWithDistanceShortDistance9999Points() throws Exception {
        java.util.List<Point> lp = new ArrayList<>();
        testRectangle = new Rectangle(0, 0, 10, 10);
        Point p;
        for(int i = 0; i < 10 * 10 -1 ; i++) {
            p = rpf.getRandomPointWithDistance(testRectangle, lp, 1);
            lp.add(p);
        }
    }

    // As each point is at distance of sqrt(2) from each other, <br/>
    // this test essentially gets all the possible points in the rectangle and then plus 1. This must fail
    @Test(expected = RuntimeException.class)
    public void testGetRandomPointWithDistanceShortDistance10001Points() throws Exception {
        java.util.List<Point> lp = new ArrayList<>();
        testRectangle = new Rectangle(0, 0, 10, 10);
        Point p;
        for(int i = 0; i < 10 * 10 + 1 ; i++) {
            p = rpf.getRandomPointWithDistance(testRectangle, lp, 1);
            lp.add(p);
        }
    }
}