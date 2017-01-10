/*
 * Copyright (c) 2017, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license.
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */

package com.salesforce.pixelcaptcha.dataobj;

import java.awt.Point;
import java.util.List;

/**
 * Implementation of this interface are used to store CAPTCHA solutions
 *
 * @author Gursev Singh Kalra @ Salesforce.com
 */
public interface CaptchaSolution {

    /*
     If true, the validation routine must ensure that the points are traversed and validated in order.
     Which means that that the first point from the user provided solution will be matched only against the
     first point in the stored solution. If false, each point from the solution is checked against all points
      to find the minimum distance.
     */
    public boolean isOrdered();

    /*
    Add a new point to the CAPTCHA
     */
    public void appendPoint(Point point);

    /*
    Return all the CAPTCHA solution points.
     */
    public List<Point> getPoints();

    /* The deviation is the maximum allowed distance between the points and the
     * solution. This can be separately calculated for each CAPTCHA.
     */
    public double getMaxDeviation();
}
