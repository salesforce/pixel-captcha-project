/*
 * Copyright (c) 2017, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license.
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */
package com.salesforce.pixelcaptcha.storage;

import static org.junit.Assert.*;

import com.salesforce.pixelcaptcha.dataobj.CaptchaSolution;
import com.salesforce.pixelcaptcha.storage.impl.PixelCaptchaSolutionStore;
import junitparams.JUnitParamsRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.common.base.Optional;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;

@RunWith(JUnitParamsRunner.class)
public class GeneralTests {


	int testSize = 10;
	int testTimeout = 15;

	@Test
	public void testGetInstanceWorkingWithValidValues() {
		CaptchaSolutionStore pcss ;
		pcss = PixelCaptchaSolutionStore.getInstance(testSize, testTimeout);
		assertNotNull(pcss);
		pcss = PixelCaptchaSolutionStore.getInstance();
		assertNotNull(pcss);
	}


	@Test
	public void testMaximumSizeOfStorage() {
		CaptchaSolutionStore pcss ;
		int size = 10;
		List<String> identifiers = new ArrayList<>();
		pcss = PixelCaptchaSolutionStore.getInstance(size, PixelCaptchaSolutionStore.DEFAULT_TIMEOUT);
		for(int i = 0; i < size/2; i++) {
			identifiers.add(pcss.storeCaptchaSolution(mock(CaptchaSolution.class)));
		}
		assertNotEquals(Optional.absent(), pcss.getCaptchaSolution(identifiers.get(0)));
		
		for(int i = size/2; i < size*2; i++) 
			identifiers.add(pcss.storeCaptchaSolution(mock(CaptchaSolution.class)));
		assertNotEquals(Optional.absent(), pcss.getCaptchaSolution(identifiers.get(size*2-1)));
	}


	/**
	 * This test tries to store more data than to initialized storage.
	 */
	@Test
	public void testStorageSize() {
		CaptchaSolutionStore pcss ;
		int size = 10;
		pcss = PixelCaptchaSolutionStore.getInstance(size, PixelCaptchaSolutionStore.DEFAULT_TIMEOUT);
		for(int i = 0; i < size *2; i++)
			pcss.storeCaptchaSolution(mock(CaptchaSolution.class));
		assertEquals(size, pcss.getSize());
	}
}
