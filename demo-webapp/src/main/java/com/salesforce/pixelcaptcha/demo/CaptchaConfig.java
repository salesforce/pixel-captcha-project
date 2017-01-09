/*
 * Copyright (c) 2017, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license.
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */

package com.salesforce.pixelcaptcha.demo;

/**
 * @author Gursev Singh Kalra @ Salesforce.com
 */
public class CaptchaConfig {
    private String orientation;
    private String challengeCount;
    private String responseCount;
    private String codePoints;
    private String ordered;

    public String getOrdered() {
        return ordered;
    }

    public void setOrdered(String ordered) {
        this.ordered = ordered;
    }

    public String getChallengeCount() {
        return challengeCount;
    }

    public void setChallengeCount(String challengeCount) {
        this.challengeCount = challengeCount;
    }

    public String getCodePoints() {
        return codePoints;
    }

    public void setCodePoints(String codePoints) {
        this.codePoints = codePoints;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public String getResponseCount() {
        return responseCount;
    }

    public void setResponseCount(String responseCount) {
        this.responseCount = responseCount;
    }
}
