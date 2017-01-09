/*
 * Copyright (c) 2017, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license.
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */

package com.salesforce.pixelcaptcha.core;

import java.awt.Rectangle;

import com.salesforce.pixelcaptcha.dataobj.CaptchaDimension;
import com.salesforce.pixelcaptcha.dataobj.ChallengeAndResponseCount;

public class MasterConfig {
	private CaptchaDimension captchaDimensions;
	private int[] printableCodePoints; // initializing only with printable code points
	private ChallengeAndResponseCount challengeAndResponseCount;
	private Rectangle challengeRect;
	private Rectangle responseRect;
	private int imageType;

	
	private int minFontSize;
	private int maxFontSize;
	private int avgFontSize;
	
	public static int MIN_FONT_SIZE_DIVISOR = 10;
	public static int MAX_FONT_SIZE_MULTIPLIER = 2;
	private static double CHALLENGE_WIDTH_MULTIPLIER = 0.75;
	private static double CHALLENGE_HEIGHT_MULTIPLIER = 1.0;
	private static double MIN_RATIO = 0.5;
	private static double MAX_RATIO = 2.0;


	private boolean isHorizontalCaptcha;
	private boolean ordered;

	
	// min font size, max font size, challenge font generator, response font generator
	// challenge coordinates, response coordinates
	// solution store
	
	public CaptchaDimension getCaptchaDimensions() {
		return this.captchaDimensions;
	}
	

	public MasterConfig(CaptchaDimension captchaDimensions,
			int[] printableCodePoints,
			ChallengeAndResponseCount challengeAndResponseCount,
			boolean ordered) {
		super();

		if(captchaDimensions == null || printableCodePoints == null || challengeAndResponseCount == null)
			throw new NullPointerException("One of captchaDimensions, printableCodePoints or challengeResponseCount is null");

		if(printableCodePoints.length <= GlobalConstants.MIN_PRINTABLE_CHARS)
			throw new IllegalArgumentException("Code points count cannot less than or equal to" + GlobalConstants.MIN_PRINTABLE_CHARS);

		double ratio = (double)(captchaDimensions.getHeight())/captchaDimensions.getWidth();
		if(ratio < MIN_RATIO || ratio > MAX_RATIO)
			throw new IllegalArgumentException("Either width or height cannot be twice the other");

		this.captchaDimensions = captchaDimensions;
		this.printableCodePoints = printableCodePoints;
		this.challengeAndResponseCount = challengeAndResponseCount;
		this.imageType = GlobalConstants.DEFAULT_IMAGE_TYPE;

		initFontSizes();
		if(this.captchaDimensions.getWidth() >= this.captchaDimensions.getHeight()) {
			//This is the case for horizontal CAPTCHA where the horizontal dimension is larger than vertical
			this.isHorizontalCaptcha = true;
			computeChallengeRectForHorCap();
			computeResponseRectForHorCap();
		} else {
			//This is the case for vertical CAPTCHA where the vertical dimension is larger than vertical
			this.isHorizontalCaptcha = false;
			computeChallengeRectForVertCap();
			computeResponseRectForVertCap();
		}

		this.ordered = ordered;
	}
	
	private void initFontSizes() {
		this.minFontSize = this.captchaDimensions.getHeight() < this.captchaDimensions.getWidth() ?
				this.captchaDimensions.getHeight()/MIN_FONT_SIZE_DIVISOR :
				this.captchaDimensions.getWidth()/MIN_FONT_SIZE_DIVISOR;

		this.maxFontSize = this.minFontSize * MAX_FONT_SIZE_MULTIPLIER;
		this.avgFontSize = (this.minFontSize + this.maxFontSize)/2;
	}

	/**
	 * Used for horizontal CAPTCHA
	 */
	private void computeChallengeRectForHorCap() {
		int startX = (int) (0.25 * this.maxFontSize);
//		int startY = (int) (this.maxFontSize + 0.25 * this.maxFontSize);
        int startY = (int) (this.maxFontSize);
		int width = (int) (CHALLENGE_WIDTH_MULTIPLIER * this.maxFontSize);
		int height = (int)(this.captchaDimensions.getHeight() - startY - 0.25 * this.maxFontSize);
		this.challengeRect = new Rectangle(startX, startY, width, height);
//        System.out.println(this.challengeRect);
	}

	/**
	 * Used for horizontal CAPTCHA
	 */
	private void computeResponseRectForHorCap() {
//		int startX = (int) (this.challengeRect.getWidth() + 0.25 * this.maxFontSize);
        int startX = (int) (2 * this.maxFontSize + 0.25 * this.maxFontSize);
//		int startY = (int) (this.maxFontSize + 0.25 * this.maxFontSize); // Changed on Feb 2
        int startY = (int) (this.maxFontSize);
		int width = this.captchaDimensions.getWidth() - startX - this.maxFontSize;
		int height = (int) (this.captchaDimensions.getHeight() - startY - 0.25 * this.maxFontSize);
		this.responseRect = new Rectangle(startX, startY, width, height);
	}

	/**
	 * Used for vertical CAPTCHA
	 */
	private void computeChallengeRectForVertCap() {
		int startX = (int) (0.25 * this.maxFontSize);
//		int startY = (int) (this.maxFontSize + 0.25 * this.maxFontSize);
        int startY = (int) (this.maxFontSize); // Changed on Feb 2
		int width = this.captchaDimensions.getWidth() - startX - this.maxFontSize;
		int height = (int) (this.maxFontSize * CHALLENGE_HEIGHT_MULTIPLIER);
		this.challengeRect = new Rectangle(startX, startY, width, height);
//		System.out.println(this.challengeRect);
	}

	/**
	 * Used for vertical CAPTCHA
	 */
	private void computeResponseRectForVertCap() {
		int startX = (int) (0.25 * this.maxFontSize);
		int startY = (int) (this.challengeRect.getHeight() + this.challengeRect.getY() + 1.0 * this.maxFontSize); // changed from 1.25 to 1.10 on Feb 3
		int width = this.captchaDimensions.getWidth() - startX - this.maxFontSize;
		int height = (int) (this.captchaDimensions.getHeight() - startY - 0.75 * this.maxFontSize);
		this.responseRect = new Rectangle(startX, startY, width, height);
	}

	public int[] getPrintableCodePoints() {
		return this.printableCodePoints;
	}
	
	public ChallengeAndResponseCount getChallengeResponseCount() {
		return challengeAndResponseCount;
	}
	
	public int getImageType() {
		return this.imageType;
	}
	
	public int getMinFontSize() {
		return this.minFontSize;
	}
	
	public int getMaxFontSize() {
		return this.maxFontSize;
	}
	
	public int getAvgFontSize() {
		return this.avgFontSize;
	}
	
	public Rectangle getChallengeRectangle() {
		return this.challengeRect;
	}
	
	public Rectangle getResponseRectangle() {
		return this.responseRect;
	}

	public boolean isHorizontalCaptcha() {
		return this.isHorizontalCaptcha;
	}

	public boolean isOrdered() {
		return this.ordered;
	}
}
