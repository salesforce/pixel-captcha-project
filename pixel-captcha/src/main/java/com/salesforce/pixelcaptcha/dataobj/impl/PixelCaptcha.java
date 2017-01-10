/*
 * Copyright (c) 2017, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license.
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */
package com.salesforce.pixelcaptcha.dataobj.impl;

import java.awt.image.BufferedImage;

import com.salesforce.pixelcaptcha.interfaces.Captcha;


public class PixelCaptcha implements Captcha {

    private BufferedImage image;
    private String identifier;


    public PixelCaptcha(BufferedImage image, String identifier) {
        super();
        this.image = image;
        this.identifier = identifier;
    }

    @Override
    public BufferedImage getImage() {
        return image;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

}
