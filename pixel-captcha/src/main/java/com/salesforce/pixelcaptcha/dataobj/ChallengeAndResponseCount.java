/*
 * Copyright (c) 2017, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license.
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */
package com.salesforce.pixelcaptcha.dataobj;

/**
 * Stores the number of challenges and responses in a CAPTCHA
 *
 * @author Gursev Singh Kalra @ Salesforce.com
 */
public enum ChallengeAndResponseCount {
    TWO_TEN(2, 10), TWO_ELEVEN(2, 11), TWO_TWELVE(2, 12),
    THREE_TEN(3, 10), THREE_ELEVEN(3, 11), THREE_TWELVE(3, 12),
    FOUR_TEN(4, 10), FOUR_ELEVEN(4, 11), FOUR_TWELVE(4, 12);

    private int challengeCount;
    private int responseCount;

    private ChallengeAndResponseCount(int challengeCount, int responseCount) {
        this.challengeCount = challengeCount;
        this.responseCount = responseCount;
    }

    public int challengeCount() {
        return challengeCount;
    }

    public int responseCount() {
        return responseCount;
    }
}
