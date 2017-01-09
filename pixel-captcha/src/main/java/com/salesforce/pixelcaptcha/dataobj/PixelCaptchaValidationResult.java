/*
 * Copyright (c) 2017, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license.
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */
package com.salesforce.pixelcaptcha.dataobj;

import com.google.gson.Gson;
import com.salesforce.pixelcaptcha.interfaces.ValidationResult;

/**
 * @author Gursev Singh Kalra @ Salesforce.com
 *
 */
public class PixelCaptchaValidationResult implements ValidationResult {
	private boolean positive;
	private int responseCode;
	private String responseDetails;


	public PixelCaptchaValidationResult(boolean positive, int responseCode, String responseDetails) {
		this.positive = positive;
		this.responseCode = responseCode;
		this.responseDetails = responseDetails;
	}

	@Override
	public boolean isPositive() {
		return this.positive;
	}

	@Override
	public int getResponseCode() {
		return this.responseCode;
	}

	@Override
	public String getResponseDetails() {
		return this.responseDetails;
	}

    @Override
    public String toString() {
		Gson gson = new Gson();
		return gson.toJson(this);
    }
}
