/*
 * Copyright (c) 2017, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license.
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */
package com.salesforce.pixelcaptcha.dataobj;

import com.google.gson.Gson;
import com.salesforce.pixelcaptcha.interfaces.ValidationResult;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Gursev Singh Kalra @ Salesforce.com
 */
public class PixelCaptchaValidationResultTest {
    @Test
    public void testToStringWorksAsExpectedWithNullResponseDetails() {
        ValidationResult vr = new PixelCaptchaValidationResult(true, 1, null);
        Gson gson = new Gson();
        ValidationResult vr2 = gson.fromJson(vr.toString(), PixelCaptchaValidationResult.class);
        assertEquals(vr.getResponseCode(), vr2.getResponseCode());
        assertEquals(vr.isPositive(), vr2.isPositive());
        assertEquals(vr.getResponseDetails(), vr2.getResponseDetails());
    }


}