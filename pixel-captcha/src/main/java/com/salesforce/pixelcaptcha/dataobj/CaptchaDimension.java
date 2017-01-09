/*
 * Copyright (c) 2017, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license.
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */

package com.salesforce.pixelcaptcha.dataobj;

public class CaptchaDimension {
	private int width;
	private int height;
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public CaptchaDimension(int width, int height) {
		if(width <= 0 || height <= 0)
			throw new IllegalArgumentException("Both height and width must be greater than zero");
		this.width = width;
		this.height = height;
	}
	
}
