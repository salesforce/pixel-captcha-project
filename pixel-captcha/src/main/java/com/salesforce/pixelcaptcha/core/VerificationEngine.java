/*
 * Copyright (c) 2017, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license.
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */

package com.salesforce.pixelcaptcha.core;

import com.salesforce.pixelcaptcha.interfaces.ValidationResult;
import com.salesforce.pixelcaptcha.dataobj.CaptchaSolution;
import com.salesforce.pixelcaptcha.dataobj.PixelCaptchaValidationResult;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Gursev Singh Kalra @ Salesforce.com
 *         This stateless class is responsible for validating the CAPTCHA solution.
 */
public class VerificationEngine {
    public static final int MATCH = 0;
    public static final int SIZE_MISMATCH = 1;
    public static final int THRESHOLD_EXCEEDED = 2;

    private static final String INVALID_SIZE = "Challenge and response size is different";
    private static VerificationEngine INSTANCE = null;

    public static synchronized VerificationEngine getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new VerificationEngine();
        }
        return INSTANCE;
    }

    private VerificationEngine() {
        //Stateless
    }

    /**
     * This method performs CAPTCHA validation validation
     *
     * @param pixelCaptchaSolution The solution for a particular CAPTCHA
     * @param response             The response provided by the user
     * @return A ValidationResult object
     */
    public ValidationResult verifySolution(CaptchaSolution pixelCaptchaSolution, List<Point> response) {
        if (pixelCaptchaSolution == null || response == null)
            throw new IllegalArgumentException("One of the arguments to verifySolution is null");

        if (pixelCaptchaSolution.getPoints().size() != response.size()) {
            return new PixelCaptchaValidationResult(false, SIZE_MISMATCH, INVALID_SIZE);
        }

        ValidationResult result;
        if (pixelCaptchaSolution.isOrdered()) {
            result = verifySolutionOrdered(pixelCaptchaSolution, response);
        } else {
            result = verifySolutionUnordered(pixelCaptchaSolution, response);
        }
        return result;
    }

    private ValidationResult verifySolutionOrdered(CaptchaSolution pixelCaptchaSolution, List<Point> response) {
        int totalDistance = 0;
        for (int i = 0; i < response.size(); i++) {
            int distance = (int) response.get(i).distance(pixelCaptchaSolution.getPoints().get(i));
            totalDistance += distance;
        }

        if (totalDistance <= pixelCaptchaSolution.getMaxDeviation()) {
            return new PixelCaptchaValidationResult(true, MATCH, "Maximum permissible deviation = " + pixelCaptchaSolution.getMaxDeviation() + ", actual value = " + totalDistance);
        } else {
            return new PixelCaptchaValidationResult(false, THRESHOLD_EXCEEDED, "Maximum permissible deviation = " + pixelCaptchaSolution.getMaxDeviation() + ", actual value = " + totalDistance);
        }

    }

    private ValidationResult verifySolutionUnordered(CaptchaSolution pixelCaptchaSolution, List<Point> response) {
        // Not changing the values inside pixelCaptchaSolution.
        List<Point> tempSolutionPoints = new ArrayList<>();
        for (Point p : pixelCaptchaSolution.getPoints())
            tempSolutionPoints.add(p);

        int totalDistance = 0;
        for (Point responsePoint : response) {
            int minDistance = Integer.MAX_VALUE;
            int minDistanceIndex = Integer.MAX_VALUE;
//            int minDistance = 100000;
//            int minDistanceIndex = 1000000;
            for (int i = 0; i < tempSolutionPoints.size(); i++) {
                int distance = (int) responsePoint.distance(tempSolutionPoints.get(i));
                if (distance < minDistance) {
                    minDistance = distance;
                    minDistanceIndex = i;
                }
            }
            //Removing to ensure that same response point cannot be used several times for minimum distance calculation
            tempSolutionPoints.remove(minDistanceIndex);
            totalDistance += minDistance;
        }

        if (totalDistance <= pixelCaptchaSolution.getMaxDeviation()) {
            return new PixelCaptchaValidationResult(true, MATCH, "Maximum permissible deviation = " + String.format("%.2f", pixelCaptchaSolution.getMaxDeviation()) + ", actual value = " + totalDistance);
        } else {
            return new PixelCaptchaValidationResult(false, THRESHOLD_EXCEEDED, "Maximum permissible deviation = " + String.format("%.2f", pixelCaptchaSolution.getMaxDeviation()) + ", actual value = " + totalDistance);
        }
    }
}
