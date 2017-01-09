/*
 * Copyright (c) 2017, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license.
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */
package com.salesforce.pixelcaptcha.core;

import java.awt.*;
import java.util.*;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.salesforce.pixelcaptcha.interfaces.ValidationResult;
import com.salesforce.pixelcaptcha.interfaces.Captcha;
import com.salesforce.pixelcaptcha.interfaces.CaptchaProvider;

/**
 * @author Gursev Singh Kalra @ Salesforce.com
 */
public class PixelCaptchaProvider implements CaptchaProvider {
	
	PixelCaptchaController pcc;

	/**
	 * SERVICE_NAME is the name of the this CAPTCHA implementation. 
	 * PCVS stands for Pixel CAPTCHA with Vertical Split between challenge and solution
	 */
	private static final String PROVIDER_NAME = "PXL";
	private static final String PROVIDER_VERSION = "1.0";
	
	static {
		
	}

	PixelCaptchaProvider() {
		pcc = null;

	}
	/**
	 * Accepts Properties as a configuration parameter
	 * @param properties
	 */
	public PixelCaptchaProvider(Properties properties) {
		this.init(properties);
	}

	@Override
	public void init(Properties properties) {
		if(properties == null) {
			throw new IllegalArgumentException("Properties cannot be null");
		}
		pcc = new PixelCaptchaController();
		pcc.initProperties(properties);
	}
	
	@Override
	public Captcha getCaptcha() {
		if(pcc == null)
			throw new IllegalStateException("Captcha Provider is not initialized.");
		return pcc.getCaptcha();
	}

	/**
	 * Accepts CAPTCHA identifier, the response and returns status if the validation succeeded or not
	 * @param captchaIdentifier - Unique identifier of the CAPTCHA provider
	 * @param response - The response string sent by the client. Important to convert the response from JSON to List<Point>
     * @return
     */
	@Override
	public ValidationResult verify(String captchaIdentifier, String response) {
		if(pcc == null)
			throw new IllegalStateException("Captcha Provider is not initialized.");
		Map<String,Point> map = new Gson().fromJson(response, new TypeToken<HashMap<String, Point>>(){}.getType());
		List<Point> l = new ArrayList<>(map.values());
		return pcc.verifyCaptcha(captchaIdentifier, l);
	}

	/**
	 * @return UUID identifier for a particular controller instance.
     */
	@Override
	public String getIdentifier() {
		return pcc.getIdentifier();
	}


	@Override
	public String getName() {
		return PROVIDER_NAME;
	}


	@Override
	public String getVersion() {
		return PROVIDER_VERSION;
	}

}
