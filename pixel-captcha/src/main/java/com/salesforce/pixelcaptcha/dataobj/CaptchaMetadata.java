/*
 * Copyright (c) 2017, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license.
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */

package com.salesforce.pixelcaptcha.dataobj;

import com.salesforce.pixelcaptcha.core.GlobalConstants;

import java.awt.*;
import java.util.List;

/**
 * This class contains all data about a CAPTCHA. It contains: <br/>
 * <ol>
 * <li>Challenge and response characters along with properties of each character</li>
 * <li>CAPTCHA dimensions</li>
 * <li>Background color</li>
 * <li>Image Type</li>
 * <li>Solution for a CAPTCHA instance</li>
 * </ol>
 */
public class CaptchaMetadata {
    private List<PointProperty> challenge;
    private List<PointProperty> solutionOptions;
    private CaptchaSolution solution;
    private CaptchaDimension captchaDimension;
    private int imageType;
    private Color backgroundColor;

    public CaptchaMetadata(List<PointProperty> challenge,
                           List<PointProperty> solutionOptions,
                           CaptchaSolution solution,
                           CaptchaDimension captchaDimension) {
        super();


        if (challenge == null || solutionOptions == null || solution == null || captchaDimension == null)
            throw new NullPointerException("challenge, solutionOptions, solution or captchaDimension cannot be null");

        if (challenge.size() < GlobalConstants.MIN_CHALLENGE_COUNT ||
                solutionOptions.size() < GlobalConstants.MIN_RESPONSE_COUNT ||
                challenge.size() > GlobalConstants.MAX_CHALLENGE_COUNT ||
                solutionOptions.size() > GlobalConstants.MAX_RESPONSE_COUNT
                )
            throw new IllegalArgumentException("Either challenge count or solutionOptions count is illegal");

        this.challenge = challenge;
        this.solutionOptions = solutionOptions;
        this.solution = solution;
        this.captchaDimension = captchaDimension;

//		 For now, imageType will always be set to GlobalConstants.DEFAULT_IMAGE_TYPE
//		 if(imageType < 0)
//			throw  new IllegalArgumentException("imageType cannot be less than 0");
        this.imageType = GlobalConstants.DEFAULT_IMAGE_TYPE;
        this.backgroundColor = Color.WHITE;
    }

    public List<PointProperty> getChallenge() {
        return this.challenge;
    }

    public List<PointProperty> getSolutionOptions() {
        return this.solutionOptions;
    }

    public CaptchaSolution getSolution() {
        return this.solution;
    }

    public CaptchaDimension getCaptchaDimension() {
        return this.captchaDimension;
    }

    public int getImageType() {
        return this.imageType;
    }


    public Color getBackgroundColor() {
        return this.backgroundColor;
    }
}
