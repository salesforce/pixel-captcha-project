/*
 * Copyright (c) 2017, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license.
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */
package com.salesforce.pixelcaptcha.utils;

import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import com.salesforce.pixelcaptcha.core.GlobalConstants;

public class RandomPointFactory {

	public static final int DEFAULT_SPACING = 10;
	public static final int MAX_SPACING = 100;
	private static final SecureRandom rand = new SecureRandom();
	
	 /*
	  * I plotted the string "AWMICZ" with these different fonts (SERIF, SANS_SERIF, MONOSPACE and DIALOG) with font size of 150 
	  * The maximum width was 150 pixels with SERIF for the character M
	  * The maximum height was 110 pixels with multiple fonts for the character M
	  * 
	  * The below derived values are used to convert Font size to pixels (as multipliers with the Font size).
	  * A BUFFER value of will be added to introduce better spacing, and will be used in Future, in needed.
	  * */
	private static final double BUFFER = 0.0; 
	private static final double H_FONT_MULTIPLIER = GlobalConstants.H_FONT_MULTIPLIER + BUFFER;
	private static final double V_FONT_MULTIPLIER = GlobalConstants.V_FONT_MULTIPLIER + BUFFER;

	private final int xSpacing;
	private final int ySpacing;
	
	public RandomPointFactory() {
		this(DEFAULT_SPACING, DEFAULT_SPACING);
	}
	
	/**
	 * xSpacing and ySpacing fields store the maximum allowed buffer in X and Y coordinates while
	 * generating random coordinates for the next character to be placed.
	 * Having thought out values for these buffers allows better placed random points 
	 * For example, if a pixel instance is provided, the x and y coordinates will be calculated as:
	 * newX = x +- (a number between 0 and xSpacing)
	 * newY = y +- (a number between 0 and ySpacing)
	 */ 
	public RandomPointFactory(int xSpacing, int ySpacing) {
		if(xSpacing < 0 || ySpacing < 0 || xSpacing > MAX_SPACING || ySpacing > MAX_SPACING) {
			throw new IllegalArgumentException("Illegal xSpacing or ySpacing value");
		}
		this.xSpacing = xSpacing;
		this.ySpacing = ySpacing;
	}


    /**
	 * This method identifies a random Point inside the top section of vertical challenge region.
	 * @param challengeRectangle The rectangle in which the first challenge point is to be identified
	 * @param challengeCount Total number of challenges for the CAPTCHA
	 * @return A Point object that has coordinates where the first challenge character is to be drawn when challenges are drawn vertically
     */
    public static Point getFirstVChallengePointFromTopInRegion(Rectangle challengeRectangle, int challengeCount) {
		// extract a smaller region towards the top of the challenge rectangle
        Rectangle region = new Rectangle(challengeRectangle.x, challengeRectangle.y, challengeRectangle.width, challengeRectangle.height/challengeCount);
		return getRandomPoint(region);
    }

    /**
	 * Given a rectangle, a reference point inside the rectangle and reference font size, it computes coordinates for next  <br/>
	 * vertical challenge character to be drawn.
	 * @param rectangle rectangle in which the next challenge point is to be identified
	 * @param referencePoint the reference point (within the rectangle). There are no checks to ensure that the referencePoint is inside the rectangle
	 * @param referenceFont font for the reference point. Primarily used for size information.
	 * @param nextFont the font for the new character. Primarily used for size information.
     * @return Returns a point
     */
	public Point getVChallengePointWrtPercentageSizeOfReferenceFont(Rectangle rectangle, Point referencePoint, Font referenceFont, Font nextFont) {
//        System.out.println("Inside getVChallengePointWrtPercentageSizeOfReferenceFont => " + referencePoint);
        if(referencePoint.getX() < 0 || referencePoint.getY() < 0) {
            throw new IllegalArgumentException("Either X or Y coordinate is negative ");
        }

        // make sure that the next challenge coordinates do not drift to more than CHALLENGE_DELTA_PERCENTAGE% of the font size
        int xVariation = (GlobalConstants.CHALLENGE_DELTA_PERCENTAGE * referenceFont.getSize())/100;

		// Variation on x-axis is okay to go either positive or negative
        int xOffset = rand.nextInt(xVariation + 1) * (rand.nextBoolean()? 1: -1);

		// Variation on y-axis is additive from the reference point
        int yOffset = rand.nextInt((int)(1.5 * ySpacing) + 1);

		// Calculate newX and newY
        int newX = (int)referencePoint.getX() + xOffset;
        int newY = (int)referencePoint.getY() + nextFont.getSize() + yOffset;

//        System.out.println("Prior to adjustment => " + newX + ", " + newY);
//        System.out.println("Rectangle Prior to adjustment => " + rectangle);

        // Keep newX inside the Rectangle
        if(newX > rectangle.x + rectangle.width) {
            newX -= (2 * Math.abs(xOffset));
        } else if(newX < rectangle.x) {
            newX += (2 * Math.abs(xOffset));
        }

		// X adjustment did not work, bring X to middle
		if(newX > rectangle.x + rectangle.width || newX < rectangle.x)
			newX = (rectangle.x + rectangle.width)/2;

        //Ensure that Y coordinate does not go beyond the rectangle
        if(newY >= rectangle.y + rectangle.height) {
            newY = rectangle.y + rectangle.height - 1;
        }
//        System.out.println("Returning => " + newX + ", " + newY);

        return new Point(newX, newY);
    }


    public static Point getFirstHChallengePointFromLeftInRegion(Rectangle challengeRectangle, int challengeCount) {
        Rectangle region = new Rectangle(challengeRectangle.x, challengeRectangle.y, challengeRectangle.width/challengeCount, challengeRectangle.height);
        return getRandomPoint(region);
    }

	/**
	 * Given a rectangle, and a reference point and reference font size, it computes coordinates for next  <br/>
	 * horizontal challenge character to be drawn on.
	 * @param rectangle rectangle in which the next horizontal challenge text's next point is to be identified
	 * @param referencePoint the reference point (within the rectangle). There are no checks to ensure that the curentPoint is inside the rectangle
	 * @param referenceFont font for the reference point. Primarily used for size information.
	 * @param nextFont the font for the new character. Not used in this method, but used to provide symmetry with getVChallengePointWrtPercentageSizeOfReferenceFont.
     * @return
     */
    public Point getHChallengePointWrtPercentageSizeOfReferenceFont(Rectangle rectangle, Point referencePoint, Font referenceFont, Font nextFont) {
        if(referencePoint.getX() < 0 || referencePoint.getY() < 0) {
            throw new IllegalArgumentException("Either X or Y coordinate is negative");
        }

        // make sure that the next challenge coordinates do not drift more than 10% of the font size
        int yVariation = (GlobalConstants.CHALLENGE_DELTA_PERCENTAGE * referenceFont.getSize())/100;

        int xOffset = rand.nextInt(xSpacing + 1) * (rand.nextBoolean()? 1: -1);
        int yOffset = rand.nextInt(yVariation + 1) * (rand.nextBoolean()? 1: -1);

        int newX = (int)referencePoint.getX() + xOffset + (int) (referenceFont.getSize() * H_FONT_MULTIPLIER);
        int newY = (int)referencePoint.getY() + yOffset;

        // Keep inside the challenge Rectangle
        if(newY > rectangle.y + rectangle.height) {
            newY -= (2 * Math.abs(yOffset));
        } else if(newY < rectangle.y) {
            newY += (2 * Math.abs(yOffset));
        }

		// X adjustment did not work, bring X to middle
		if(newX > rectangle.x + rectangle.width || newX < rectangle.x)
			newX = rectangle.x + rectangle.width;

		if(newY > rectangle.y + rectangle.height)
			newY = rectangle.y + rectangle.height;

		return new Point(newX, newY);
    }

	/*
	The following two methods are to get a random point in a rectangle.
	These are independent of the Horizontal or Vertical CAPTCHA orientation.
	 */
	
	/**
	 * Returns a random x,y coordinate inside a rectangle with all coordinates including the border.
	 * This will be primarily used to obtain Pixels inside the solution area
	 * 
	 * This method throws IllegalArgumentException in the following case:
	 * 1. When either X or Y coordinate is negative
	 * 2. When either width or height is negative or equal to zero.
	 * @param rect
	 * @return
	 */
	public static Point getRandomPoint(Rectangle rect) { // is used
		Point p = getRandomPointWithDistance(rect, new ArrayList<Point>(), 0);
        return p;
	}
	
	/**
	 * Returns a random point within a rectangle and at a minimum distance from a reference point within the rectangle.
	 * The generated point is at a minimum distance from all the points inside the reference List<Point>.
	 * @param rectangle The rectangle in which the point is to be generated.
	 * @param reference The reference point
	 * @param distance Minimum distance between the reference and the new point
	 * @return Random point in the rectangle.
	 */
	public static Point getRandomPointWithDistance(Rectangle rectangle, List<Point> reference, int distance) {
		int i = 0;
		int max = 100000;
		if(rectangle == null)
			throw new NullPointerException("rectangle parameter cannot be null");
		
		if(rectangle.getX() < 0 || rectangle.getY() < 0) {
			throw new IllegalArgumentException("X or Y coordinates cannot be negative");
		}
		
		if(rectangle.getWidth() <= 0 || rectangle.getHeight() <= 0) {
			throw new IllegalArgumentException("Width or height cannot be zero or negative");
		}
		
		int x;
		int y;
		Point temp = null;
		while(temp == null) {
			if(i >= Integer.MAX_VALUE/2048 )
				throw new RuntimeException("Cannot find random point in " + Integer.toString(max) + " attempts");
			x = (int)(rectangle.getX() + rand.nextInt(rectangle.width));
			y = (int)(rectangle.getY() + rand.nextInt(rectangle.height));
			temp = new Point(x,y);

    		for(Point p: reference) {

    			if(p.distance(temp) < distance) {
					i++;
					temp = null;
					break;
				} 
    		}
		}
		return temp;
	}

	/*
	UNUSED CODE BEGINS
	 */
		/*
	METHODS FOR VERTICAL CAPTCHA
	In the vertical CAPTCHA, challenge is written in a Horizontal fashion. The methods, getFirstHChallengePoint and
	getHChallengePoint are for Horizontal challenge points generation for the vertical CAPTHCA.
	 */

	/*
	METHODS FOR HORIZONTAL CAPTCHA
	In the horizontal CAPTCHA, challenge is written in a Vertical Fashion. The methods, getFirstVChallengePointFromTop and
	getVChallengePoint are for Vertical points generation for the horizontal CAPTHCA.
	 */
	private static Point getFirstVChallengePointFromTop(Rectangle rectangle, int challengeCount, Font font) {
		if(rectangle == null || challengeCount == 0 || font == null)
			throw new NullPointerException("Null paramter not accepted");

		int fontSize = font.getSize();

		int width =  rectangle.width;
		int height = rectangle.height;

		int yOffset = ((height - fontSize * challengeCount)/2) + rand.nextInt(fontSize); // Optionally add another fontSize
		// The X coordinate has to be between (0.35 * fontSize) upto (width - fontSize)
		System.out.println(width + ", " + fontSize);
		System.out.println(rectangle);
		int xOffset = (int) 0.35 * fontSize + rand.nextInt(width - fontSize+1);

		return new Point((int)(rectangle.getX() + xOffset), (int)(rectangle.getY() + yOffset));
	}


	/**
	 * METHOD FOR HORIZONTAL CAPTCHA
	 * @param currentPoint - The current Point with respect to which new random point is to be generated
	 * @param currentFont - The current Font with respect to which new random point is to be generated
	 * @param nextFont - The next font for which the random point is being generated.
	 * @return The next Point where the next character is to be placed. The new
	 */
	private Point getVChallengePoint(Point currentPoint, Font currentFont, Font nextFont) {
		if(currentPoint.getX() < 0 || currentPoint.getY() < 0) {
			throw new IllegalArgumentException("Either X or Y coordinate is negative");
		}

		int xOffset = rand.nextInt(xSpacing + 1) * (rand.nextBoolean()? 1: -1);
		//TODO: review the yOffset calculation
		int yOffset = rand.nextInt(ySpacing + 1) + (int) 1.5 * ySpacing;// * (r.nextBoolean()? 1: -1); //Might consider including this negative aspect later sometime.

		int newX = (int)currentPoint.getX() + xOffset;
		int newY = (int)currentPoint.getY() + yOffset + (int)(nextFont.getSize() * V_FONT_MULTIPLIER) ;

		/* At times when successive vertical coordinate generation attempts return values to the left
		 of the currentPoint, the next point's X coordinate was turning out to be negative.
		 */

		if(newX < 0)
			newX = (int)currentPoint.getX();
		return new Point(newX, newY);
	}

	/**
	 * METHOD FOR VERTICAL CAPTCHA
	 * @param currentPoint - the Point to be used as reference while generating coordinates. The new point will have X and Y coordinates with random positive increment.
	 * @param currentFont - This is the Font at the pixel for which next challenge coordinate is being asked for.
	 * It throws an exception if either X or Y is negative.
	 * Rather, this function attempts to return coordinates that are closer and may result in munged or overlapped challenge text.
	 * @return new Point object to be used for plotting challenge text
	 */
	private Point getHChallengePoint(Point currentPoint, Font currentFont, Font nextFont) {
		if(currentPoint.getX() < 0 || currentPoint.getY() < 0) {
			throw new IllegalArgumentException("Either X or Y coordinate is negative");
		}

		int xOffset = rand.nextInt(xSpacing + 1) * (rand.nextBoolean()? 1: -1);
		int yOffset = rand.nextInt(ySpacing + 1) * (rand.nextBoolean()? 1: -1);

		int newX = (int)currentPoint.getX() + xOffset + (int) (currentFont.getSize() * H_FONT_MULTIPLIER);
		int newY = (int)currentPoint.getY() + yOffset;
		return new Point(newX, newY);
	}

	/**
	 * METHOD FOR VERTICAL CAPTCHA
	 * @param challengeDimensions -
	 * @param challengeAndResponseCount
	 * @param font
	 * @return
	 */
	private static Point getFirstHChallengePoint(Rectangle rectangle, int challengeCount, Font font) {
		if(rectangle == null || challengeCount == 0 || font == null)
			throw new NullPointerException("Null parameter not accepted");

		int fontSize = font.getSize();
		if(fontSize == 0)
			throw new IllegalArgumentException("Font size cannot be zero");

		int width =  (int) rectangle.getWidth();
		int height = (int) rectangle.getHeight();

		int xOffset = (int)(((width - fontSize * challengeCount)/2.0) + rand.nextInt(fontSize));
		// The X coordinate has to be between (0.35 * fontSize) upto (width - fontSize)
		int yOffset = fontSize + rand.nextInt(height - fontSize);

		return new Point((int)(rectangle.getX() + xOffset), (int)(rectangle.getY() + yOffset));
	}

	/*
	UNUSED CODE ENDS
	*/



}
