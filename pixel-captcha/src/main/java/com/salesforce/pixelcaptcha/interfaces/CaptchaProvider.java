/*
 * Copyright (c) 2017, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license.
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */

package com.salesforce.pixelcaptcha.interfaces;

import java.util.Properties;

/**
 * This is the Interface that CAPTCHA provider will implement.
 * In addition to these methods, it is the CAPTCHA Provider implementations must ensure the following:
 *
 * @author Gursev Singh Kalra @ Salesforce.com
 */
public interface CaptchaProvider {

    /**
     * Call this method with either a null object or an object of CaptchaProviderConfig class containing configuration for the specific CAPTCHA provider.
     * This will be the first method the CaptchaService will call for each CAPTCHA provider and pass on the configuration information.
     *
     * @param config
     */
    public void init(Properties config);

    /**
     * Name of the CAPTCHA provider. This name will be used while selecting a CAPTCHA provider
     * from the collection of CAPTCHA providers loaded by the ServiceLoader
     *
     * @return
     */
    public String getName();

    /**
     * Return the unique identifier for the particular CAPTCHA Provider instance. This allows same provider to
     * have multiple instances.
     *
     * @return CAPTCHA provider's name
     */
    public String getIdentifier();

    /**
     * @return CAPTCHA provider's version information as a String. <br/>
     * <br/>
     * The version information can be optionally used to select a CAPTCHA provider <br/>
     * from the collection of CAPTCHA providers loaded by the ServiceLoader
     */
    public String getVersion();

    /**
     * Return the CAPTCHA Object
     *
     * @return
     */
    public Captcha getCaptcha();

    /**
     * Verifies a CAPTCHA response
     *
     * @param captchaIdentifier - Unique identifier of the CAPTCHA provider
     * @param response          - The response string sent by the client
     * @return
     */
    public ValidationResult verify(String captchaIdentifier, String response);
}
