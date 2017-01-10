/*
 * Copyright (c) 2017, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license.
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */
package com.salesforce.pixelcaptcha.dataobj;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.geom.AffineTransform;

/**
 * This interface serves as a guideline on the properties associated with individual points in a CAPTCHA.
 * The information that must be associated with each point includes, Font, Text to Write, Coordinates and the Color.
 */
public interface PointProperty {

    //The font for the current point
    public Font getFont();

    // The string is one character in length for the current CAPTCHA.
    // This may change in future.
    public String getStringToWrite();

    //Return the current point for which the properties have values
    public Point getPoint();

    //Return the color for the current point
    public Color getColor();

}
