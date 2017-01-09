/*
 * Copyright (c) 2017, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license.
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */
package com.salesforce.pixelcaptcha.storage;

import com.google.common.base.Optional;
import com.salesforce.pixelcaptcha.dataobj.CaptchaSolution;
import com.salesforce.pixelcaptcha.storage.impl.PixelCaptchaSolutionStore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * @author Gursev Singh Kalra @ Salesforce.com
 */
public class TestTimeout {
    @Test
    public void testStorageTimeOut() throws InterruptedException {
        CaptchaSolutionStore pcss ;
        int size = 10;
        pcss = new PixelCaptchaSolutionStore(size, 1);
        String identifier = pcss.storeCaptchaSolution(mock(CaptchaSolution.class));
        for(int i = 0; i < size/2; i++)
            pcss.storeCaptchaSolution(mock(CaptchaSolution.class));
        Thread.sleep(2 * 1000);
        assertEquals(Optional.absent(), pcss.getCaptchaSolution(identifier));
    }
}
