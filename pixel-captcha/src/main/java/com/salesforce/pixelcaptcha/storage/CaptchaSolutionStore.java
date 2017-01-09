/*
 * Copyright (c) 2017, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license.
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */
package com.salesforce.pixelcaptcha.storage;

import com.google.common.base.Optional;
import com.salesforce.pixelcaptcha.dataobj.CaptchaSolution;

/**
 * Implementations of this interface actually store the response coordinates to the CAPTCHA.
 * @author Gursev Singh Kalra @ Salesforce.com
 *
 */
public interface CaptchaSolutionStore {
	String storeCaptchaSolution(CaptchaSolution solution);
	Optional<CaptchaSolution> getCaptchaSolution(String identifier);
	long getSize();
}
