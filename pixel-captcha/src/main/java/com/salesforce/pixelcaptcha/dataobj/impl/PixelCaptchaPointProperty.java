/*
 * Copyright (c) 2017, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license.
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */
package com.salesforce.pixelcaptcha.dataobj.impl;

import com.salesforce.pixelcaptcha.dataobj.PointProperty;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;

public class PixelCaptchaPointProperty implements PointProperty {
    private Font font;
    private String stringToWrite;
    private Point point;
    private Color color;

    public PixelCaptchaPointProperty(String stringToWrite,
                                     Point point,
                                     Font font,
                                     Color color) {
        if (stringToWrite == null || point == null || font == null || color == null) {
            throw new NullPointerException("At least one parameter to PixelCaptchaPointProperty constructor is null");
        }
        this.stringToWrite = stringToWrite;
        this.point = point;
        this.font = font;
        this.color = color;
    }

    @Override
    public Font getFont() {
        return font;
    }

    @Override
    public String getStringToWrite() {
        return stringToWrite;
    }

    @Override
    public Point getPoint() {
        return point;
    }

    @Override
    public Color getColor() {
        return color;
    }
}
