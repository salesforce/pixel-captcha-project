/*
 * Copyright (c) 2017, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license.
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */
package com.salesforce.pixelcaptcha.dataobj.impl;

import com.salesforce.pixelcaptcha.dataobj.CaptchaSolution;

import java.awt.Point;
import java.util.List;

/**
 * This class stores the solution for a CAPTCHA.
 * @author Gursev Singh Kalra @ Salesforce.com
 *
 */
public class PixelCaptchaSolution implements CaptchaSolution {
	/**
	 * The solution parameter stores the solution for the CAPTCHA. It needs to be mutable.
	 */
	private List<Point> solution;
	private double maxDeviation;
	
	/**
	 * The ordered parameters indicates if the solution Points verification should be performed in ordered fashion or not
	 * It defaults to false
	 */
	private boolean ordered;

	public PixelCaptchaSolution(List<Point> solution, double maxDeviation) {
		this(solution, maxDeviation, false);
	}

	public PixelCaptchaSolution(List<Point> solution, double maxDeviation, boolean ordered) {
		if(solution == null)
			throw new NullPointerException("Solution cannot be null");
		this.solution = solution;
		this.maxDeviation = maxDeviation;
		this.ordered = ordered;
	}

	@Override
	public void appendPoint(Point point) {
		if(point == null)
			throw new NullPointerException("point parameter cannot be null");
		this.solution.add(point);
	}

	@Override
	public List<Point> getPoints() {
		return this.solution;
	}

	@Override
	public boolean isOrdered() {
		return this.ordered;
	}

	@Override
	public double getMaxDeviation() {
		return this.maxDeviation;
	}

}
