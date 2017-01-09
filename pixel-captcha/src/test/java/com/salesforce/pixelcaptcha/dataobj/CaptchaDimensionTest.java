/*
 * Copyright (c) 2017, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license.
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */

package com.salesforce.pixelcaptcha.dataobj;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertNotNull;
/**
 * @author Gursev Singh Kalra @ Salesforce.com
 */

@RunWith(JUnitParamsRunner.class)
public class CaptchaDimensionTest {

    private static final Object[] getInvalidValues() {
        return new Object[] {
                new Object[] {0, 0},
                new Object[] {10, -1},
                new Object[] {-1, 10}
        };
    }

    @Test(expected = IllegalArgumentException.class)
    @Parameters(method = "getInvalidValues")
    public void testInvalidValuesForSizeAndTimeoutThrowExceptions(int x, int y) {
        new CaptchaDimension(x, y);
    }

    @Test
    public void testNegativeValues() {
        assertNotNull(new CaptchaDimension(10, 10));
    }
}
