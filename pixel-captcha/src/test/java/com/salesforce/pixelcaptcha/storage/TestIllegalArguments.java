/*
 * Copyright (c) 2017, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license.
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */
package com.salesforce.pixelcaptcha.storage;

import com.salesforce.pixelcaptcha.storage.impl.PixelCaptchaSolutionStore;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Gursev Singh Kalra @ Salesforce.com.
 */
@RunWith(JUnitParamsRunner.class)
public class TestIllegalArguments {

    private static final Object[] getInvalidValues() {
        return new Object[]{
                new Object[]{PixelCaptchaSolutionStore.MIN_SIZE - 1, PixelCaptchaSolutionStore.MAX_TIMEOUT},
                new Object[]{0, PixelCaptchaSolutionStore.MAX_TIMEOUT},
                new Object[]{-1, PixelCaptchaSolutionStore.MAX_TIMEOUT},
                new Object[]{10, PixelCaptchaSolutionStore.MAX_TIMEOUT + 1}
        };
    }

    @Test(expected = IllegalArgumentException.class)
    @Parameters(method = "getInvalidValues")
    public void testInvalidValuesForSizeAndTimeoutThrowExceptions(int size, int timeOut) {
        new PixelCaptchaSolutionStore(size, timeOut);
    }


}
