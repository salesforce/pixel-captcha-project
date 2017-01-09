/*
 * Copyright (c) 2017, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license.
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */

package com.salesforce.pixelcaptcha.core;

import com.salesforce.pixelcaptcha.dataobj.CaptchaMetadata;
import com.salesforce.pixelcaptcha.dataobj.PointProperty;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.security.SecureRandom;
import java.util.List;

/**
 * Stateless class. Given a CaptchaMetadata object, the purpose of this class is to paint a CAPTCHA image and returned
 * it as a BufferedImage
 * @author Gursev Singh Kalra @ Salesforce.com
 */
public class CaptchaBuilder {
    private static long WHITE = -1;
    private static SecureRandom sr = new SecureRandom();
    private static int MAX_OVERLAP_PERCENTAGE = 15; // Percentage overlap

    /**
     * NOT USED
     * The calls to this method will have everything in order and no exceptions are expected.
     * No null checks are performed either.
     *
     * This method shall never be invoked directly from outside the class with CaptchaMetadata object instance.
     * @param captchaMetadata
     * @return
     */
    private static BufferedImage buildImage(CaptchaMetadata captchaMetadata) {


        BufferedImage bi = new BufferedImage(
                captchaMetadata.getCaptchaDimension().getWidth(),
                captchaMetadata.getCaptchaDimension().getHeight(),
                captchaMetadata.getImageType()
        );

        Graphics2D g2d = bi.createGraphics();
        g2d.setColor(captchaMetadata.getBackgroundColor());
        g2d.fillRect(
                0,
                0,
                captchaMetadata.getCaptchaDimension().getWidth(),
                captchaMetadata.getCaptchaDimension().getHeight()
        );

        g2d.setColor(Color.black);

        for(PointProperty pp : captchaMetadata.getChallenge()) {
            drawStringAtPoint(g2d,
                    pp.getColor(),
                    pp.getFont(),
                    pp.getStringToWrite(),
                    pp.getPoint().x,
                    pp.getPoint().y);
        }

        for(PointProperty pp : captchaMetadata.getSolutionOptions()) {
            drawStringAtPoint(g2d,
                    pp.getColor(),
                    pp.getFont(),
                    pp.getStringToWrite(),
                    pp.getPoint().x,
                    pp.getPoint().y);
        }

        return bi;
    }

    /**
     * Paints a CAPTCHA as per the CaptchaMetadata object supplied.
     * @param captchaMetadata CaptchaMetadata object for the CAPTCHA.
     * @return BufferedImage with the new CAPTCHA
     */

    public static BufferedImage buildImageWithCollapsedChallenge(CaptchaMetadata captchaMetadata) {
        if(captchaMetadata == null)
            throw new IllegalArgumentException("CaptchaMetadata object cannot be null");

        if(captchaMetadata.getCaptchaDimension().getWidth() >= captchaMetadata.getCaptchaDimension().getHeight()) {
            return buildHorizontalCAPTCHA(captchaMetadata);
        } else {
            return buildVerticalCAPTCHA(captchaMetadata);
        }
    }

    private static BufferedImage getBufferedImage(CaptchaMetadata captchaMetadata) {
        return new BufferedImage(
                captchaMetadata.getCaptchaDimension().getWidth(),
                captchaMetadata.getCaptchaDimension().getHeight(),
                captchaMetadata.getImageType()
        );
    }

    private static void drawSolution(BufferedImage bi, CaptchaMetadata captchaMetadata) {
        Graphics2D g2d = bi.createGraphics();
        for (PointProperty pp : captchaMetadata.getSolutionOptions()) {
            drawStringAtPoint(g2d,
                    pp.getColor(),
                    pp.getFont(),
                    pp.getStringToWrite(),
                    pp.getPoint().x,
                    pp.getPoint().y);
        }
    }

    private static int scanForNextHorizontalChallengeXCoordinateForVerticalCaptcha(BufferedImage bi, int startX, int referenceFontSize) {
//        System.out.println("[+] Entering scanForNextHorizontalChallengeXCoordinateForVerticalCaptcha");
        int nextX = startX;
        int scanWidth = (int)(referenceFontSize * 1.5);
        int maxOverlap = (referenceFontSize * MAX_OVERLAP_PERCENTAGE)/100;
        int heightToScan = (bi.getWidth()/5) * 2; /*This is twice the maximum font size*/


        for(int xMovement = 0; xMovement < scanWidth ; xMovement++) {
            for(int y = 0; y <  heightToScan; y++) {
                if(startX + xMovement >= bi.getWidth())
                    break;
//                System.out.println(bi.getWidth() + ", " + bi.getHeight() + ", => " + (startX + xMovement) + ", " + y);
                long pixel = bi.getRGB(startX + xMovement, y);
                if(pixel != WHITE) {
                    nextX = startX + xMovement;
                    break;
                }
            }
        }

        // Now I have the y coordinate by which the text potentially ends. Time to get the next random coordinate for the overlap
        nextX -= sr.nextInt(maxOverlap);
        return nextX;
    }

    private static void drawCollapsedChallengeForVerticalCAPTCHA(BufferedImage bi, CaptchaMetadata captchaMetadata) {
        List<PointProperty> challengePointProperties = captchaMetadata.getChallenge();
        int challengeCount = challengePointProperties.size();
        PointProperty pp = challengePointProperties.get(0);

        Graphics2D g2d = bi.createGraphics();
        drawStringAtPoint(g2d, pp.getColor(), pp.getFont(), pp.getStringToWrite(), pp.getPoint().x, pp.getPoint().y);

        Point tempPoint = pp.getPoint();

        for(int i = 1; i < challengeCount ; i++) {
            int tempX = scanForNextHorizontalChallengeXCoordinateForVerticalCaptcha(bi, tempPoint.x, pp.getFont().getSize());
            tempPoint = new Point(tempX, pp.getPoint().y); // change X, keep the same Y
            pp = challengePointProperties.get(i);
            drawStringAtPoint(g2d, pp.getColor(), pp.getFont(), pp.getStringToWrite(), tempPoint.x, tempPoint.y);
        }

    }


    private static void drawStringAtPoint(Graphics2D g2d, Color color, Font font, String string, int x, int y) {
        g2d.setColor(color);
        g2d.setFont(font);
        g2d.drawString(string, x, y);
    }

    private static int scanForNextVerticalChallengeYCoordinateForHorizontalCaptcha(BufferedImage bi, int startY, int referenceFontSize) {
//        System.out.println("[+] Entering scanForNextVerticalChallengeYCoordinateForHorizontalCaptcha with startY = " + startY);
        int nextY = startY;
        int scanHeight = (int)(referenceFontSize * 1.5);
        int maxOverlap = (referenceFontSize * MAX_OVERLAP_PERCENTAGE)/100;
        int widthToScan = (bi.getWidth()/5) * 2; /*This is twice the maximum font size*/

        for(int yMovement = 0; yMovement < scanHeight ; yMovement++) {
            for(int x = 0; x <  widthToScan; x++) {
                if(startY - yMovement < 0)
                    break;
//                System.out.println(bi.getWidth() + ", " + bi.getHeight() + ", => " +x + ", " + (startY - yMovement));
                long pixel = bi.getRGB(x, startY - yMovement);
                if(pixel != WHITE) {
                    nextY = startY - yMovement;
                    break;
                }
            }
        }

        // Now I have the y coordinate by which the text possibly ends. Time to get the next random coordinate for the overlap
        nextY += sr.nextInt(maxOverlap);
        return nextY;
    }

    private static void drawCollapsedChallengeForHorizontalCAPTCHA(BufferedImage bi, CaptchaMetadata captchaMetadata) {
//        List<PointProperty> pointPropertyList = captchaMetadata.getChallenge();
        List<PointProperty> challengePointProperties = captchaMetadata.getChallenge();
        int challengeCount = challengePointProperties.size();
        PointProperty pp = challengePointProperties.get(challengeCount - 1);

        Graphics2D g2d = bi.createGraphics();
        drawStringAtPoint(g2d, pp.getColor(), pp.getFont(), pp.getStringToWrite(), pp.getPoint().x, pp.getPoint().y);

        Point tempPoint = pp.getPoint();

        for(int i = 2; i <= challengeCount ; i++) {
            int tempY = scanForNextVerticalChallengeYCoordinateForHorizontalCaptcha(bi, tempPoint.y, pp.getFont().getSize());
            tempPoint = new Point(pp.getPoint().x, tempY); // keep the same X, change the Y
            pp = challengePointProperties.get(challengeCount - i);
            drawStringAtPoint(g2d, pp.getColor(), pp.getFont(), pp.getStringToWrite(), tempPoint.x, tempPoint.y);
        }
    }

    private static void paintCaptchaBackground(BufferedImage bi, CaptchaMetadata captchaMetadata) {
        Graphics2D g2d = bi.createGraphics();
        g2d.setColor(captchaMetadata.getBackgroundColor());
        g2d.fillRect(
                0,
                0,
                captchaMetadata.getCaptchaDimension().getWidth(),
                captchaMetadata.getCaptchaDimension().getHeight()
        );
    }

    private static BufferedImage buildCAPTCHA(CaptchaMetadata captchaMetadata, boolean isHorizontal) {
        BufferedImage bi = getBufferedImage(captchaMetadata);
        paintCaptchaBackground(bi, captchaMetadata);

        if(isHorizontal) {
            drawCollapsedChallengeForHorizontalCAPTCHA(bi, captchaMetadata);
        } else {
            drawCollapsedChallengeForVerticalCAPTCHA(bi, captchaMetadata);
        }

        drawSolution(bi, captchaMetadata);
        return bi;

    }



    private static BufferedImage buildHorizontalCAPTCHA(CaptchaMetadata captchaMetadata) {
        boolean isHorizontal = true;
        return buildCAPTCHA(captchaMetadata, isHorizontal);
    }

    private static BufferedImage buildVerticalCAPTCHA(CaptchaMetadata captchaMetadata) {
        boolean isHorizontal = false;
        return buildCAPTCHA(captchaMetadata, isHorizontal);
    }

}
