/*
 * Copyright (c) 2017, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license.
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */

package com.salesforce.pixelcaptcha.demo;

import java.awt.*;

/**
 * @author Gursev Singh Kalra @ Salesforce.com.
 */
public class ClientSolutionDO {
    private String pixelcaptchaId;
    private SolutionCoordinates solutionCoordinates;

    class SolutionCoordinates {
        private Point p0;
        private Point p1;
        private Point p2;
        private Point p3;

        public Point getP0() {
            return p0;
        }

        public void setP0(Point p0) {
            this.p0 = p0;
        }

        public Point getP1() {
            return p1;
        }

        public void setP1(Point p1) {
            this.p1 = p1;
        }

        public Point getP2() {
            return p2;
        }

        public void setP2(Point p2) {
            this.p2 = p2;
        }

        public Point getP3() {
            return p3;
        }

        public void setP3(Point p3) {
            this.p3 = p3;
        }
    }

    public String getPixelcaptchaId() {
        return pixelcaptchaId;
    }

    public void setPixelcaptchaId(String pixelcaptchaId) {
        this.pixelcaptchaId = pixelcaptchaId;
    }

    public SolutionCoordinates getSolutionCoordinates() {
        return solutionCoordinates;
    }

    public void setSolutionCoordinates(SolutionCoordinates solutionCoordinates) {
        this.solutionCoordinates = solutionCoordinates;
    }
}
