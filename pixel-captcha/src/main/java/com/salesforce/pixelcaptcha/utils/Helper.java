/*
 * Copyright (c) 2017, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license.
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */

package com.salesforce.pixelcaptcha.utils;

import com.google.common.base.Strings;
import com.salesforce.pixelcaptcha.core.GlobalConstants;

import java.awt.Point;
import java.util.*;

public class Helper {

	private static Random rnd = new Random();

	// http://stackoverflow.com/questions/1519736/random-shuffling-of-an-array
	public static void shuffleIntArray(int[] arr) {
		if(arr == null || arr.length == 0)
			throw new IllegalArgumentException("Array cannot be null or have zero length");
		
		for (int i = arr.length - 1; i > 0; i--) {
			int index = rnd.nextInt(i + 1);
			int a = arr[index];
			arr[index] = arr[i];
			arr[i] = a;
		}
	}
	
	// http://stackoverflow.com/questions/718554/how-to-convert-an-arraylist-containing-integers-to-primitive-int-array
	public static int[] convertIntegerListToIntArray(List<Integer> listOfInts) {
		int[] arrayOfInts = new int[listOfInts.size()];
		Iterator<Integer> iter = listOfInts.iterator();
		for(int i = 0; i< arrayOfInts.length; i++) {
			arrayOfInts[i] = iter.next().intValue();
		}
		return arrayOfInts;
	}

	private static int[] convertIntegerSetToIntArray(SortedSet<Integer> sortedSetInts) {
		int[] arrayOfInts = new int[sortedSetInts.size()];
		Iterator<Integer> iter = sortedSetInts.iterator();
		for(int i = 0; i< arrayOfInts.length; i++) {
			arrayOfInts[i] = iter.next().intValue();
		}
		return arrayOfInts;
	}

	/**
	 * Rotates a point with respect to a center point. This is used for calculating
	 * the new center of a character after it has been rotated.
	 * http://www.gamefromscratch.com/post/2012/11/24/GameDev-math-recipes-Rotating-one-point-around-another-point.aspx 
	 * @param reference - The point at which the text was drawn
	 * @param toRotate - The approximate center of the drawn text
	 * @param degrees - The degrees by whith the Text/Font is rotated
	 * @return
	 */
	public static Point rotatePoint(Point reference, Point toRotate, int degrees) {
		double angle = degrees;
		angle = (angle ) * (Math.PI/180);
		double rotatedX = Math.cos(angle) * (toRotate.x - reference.x) - Math.sin(angle) * (toRotate.y - reference.y) + reference.x;
		double rotatedY = Math.sin(angle) * (toRotate.x - reference.x) + Math.cos(angle) * (toRotate.y - reference.y) + reference.y;
		return new Point((int)rotatedX, (int)rotatedY);
	}


	public static void validateCodePointValues(int[] codePoints) {
		for(int c: codePoints) {
			if(c < GlobalConstants.MIN_ALLOWED_CODE_POINT || c > GlobalConstants.MAX_ALLOWED_CODE_POINT)
				throw new IllegalArgumentException("Code point must be between " + GlobalConstants.MIN_ALLOWED_CODE_POINT + " and " + GlobalConstants.MAX_ALLOWED_CODE_POINT);
		}
	}

	public static int[] constructCodePointArrayFomMinMaxValues(int minCodePoint, int maxCodePoint) {
		if(minCodePoint >= maxCodePoint)
			throw new IllegalArgumentException("minCodePoint must be less than maxCodePoint");

		if(minCodePoint < GlobalConstants.MIN_ALLOWED_CODE_POINT || maxCodePoint > GlobalConstants.MAX_ALLOWED_CODE_POINT)
			throw new IllegalArgumentException("Code points must be between " + GlobalConstants.MIN_ALLOWED_CODE_POINT + " and " + GlobalConstants.MAX_ALLOWED_CODE_POINT);

		int codePointCount = maxCodePoint - minCodePoint + 1;
		int[] allCodePoints = new int[codePointCount];
		for(int i = 0; i < codePointCount; i++) {
			allCodePoints[i] = minCodePoint + i;
		}

		return allCodePoints;
	}

    public static int convertStringToIntOrDefault(String str, int defaultValue) {
        // Return default value of the string is null or empty
        if(Strings.isNullOrEmpty(str))
            return defaultValue;

        int strToInt;
        try {
            strToInt = Integer.parseInt(str);
        } catch(NumberFormatException nfe) {
            strToInt = defaultValue;
        }
        return strToInt;
    }


    public static int[] convertCodePointsToSortedIntArrayNoDuplicates(String line, String range) {
        if(Strings.isNullOrEmpty(line))
            line = range;

        if(Strings.isNullOrEmpty(line))
            throw new IllegalArgumentException("Nothing to work on");

        String[] items = line.split("\\s*,\\s*");
        SortedSet<Integer> treeSetOfInts = new TreeSet<>();

        for(String item : items) {
            if(item.contains("-")) {
                int[] temp = convertDashSeparatedValuesToIntArray(item);
                for(int t: temp) {
                    treeSetOfInts.add(t);
                }
            } else {
                try {
                    treeSetOfInts.add(Integer.parseInt(item));
                } catch (NumberFormatException nfe) {
                    //Ignore this exception
                }
            }
        }

        int[] arrayOfInts = convertIntegerSetToIntArray(treeSetOfInts);
        return arrayOfInts;
    }

    private static int[] convertDashSeparatedValuesToIntArray(String item) {
        String[] endpoints = item.split("-");
        if(endpoints.length != 2)
            throw new IllegalArgumentException("The range parameter is not in a valid format");

        int rangeStart = Integer.parseInt(endpoints[0]);
        int rangeEnd = Integer.parseInt(endpoints[1]);

        int[] range = constructCodePointArrayFomMinMaxValues(rangeStart, rangeEnd);
        return range;
    }


    private static int[] sortedIntArrayFromMinMaxValues(int minCodePoint, int maxCodePoint) {
        if(minCodePoint >= maxCodePoint)
            throw new IllegalArgumentException("minCodePoint must be less than maxCodePoint");
        int codePointCount = maxCodePoint - minCodePoint + 1;
        int[] allCodePoints = new int[codePointCount];
        for(int i = 0; i < codePointCount; i++) {
            allCodePoints[i] = minCodePoint + i;
        }

        return allCodePoints;
    }

    private static int[] getNewSortedIntArray(int[] codePointArray) {
        int[] allCodePoints;
        int codePointCount;
        if(codePointArray == null || codePointArray.length == 0)
            throw new IllegalArgumentException("codePointArray is null or has zero length");
        codePointCount = codePointArray.length;
        allCodePoints = new int[codePointCount];
        System.arraycopy(codePointArray, 0, allCodePoints, 0, codePointCount);
        Arrays.sort(allCodePoints);

        return allCodePoints;
    }


}
