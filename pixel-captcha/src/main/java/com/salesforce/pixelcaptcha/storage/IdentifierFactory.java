/*
 * Copyright (c) 2017, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license.
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */

package com.salesforce.pixelcaptcha.storage;

import java.util.UUID;

/**
 * @author Gursev Singh Kalra @ Salesforce.com
 */
public class IdentifierFactory {
    public static String getInstance() {
        return UUID.randomUUID().toString();
    }
}
