/*
 * Copyright (c) 2017, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license.
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */

package com.salesforce.pixelcaptcha.core;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Properties;
import java.util.UUID;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import com.google.common.base.Optional;

import com.salesforce.pixelcaptcha.dataobj.*;
import com.salesforce.pixelcaptcha.interfaces.ValidationResult;
import com.salesforce.pixelcaptcha.interfaces.Captcha;
import com.salesforce.pixelcaptcha.dataobj.impl.PixelCaptcha;
import com.salesforce.pixelcaptcha.storage.CaptchaSolutionStore;
import com.salesforce.pixelcaptcha.storage.impl.PixelCaptchaSolutionStore;
import com.salesforce.pixelcaptcha.utils.Helper;
import com.salesforce.pixelcaptcha.utils.PrintableCharFinder;

import javax.imageio.ImageIO;

public class PixelCaptchaController {

    private final String identifier;
    private MasterConfig masterConfig;
    private CaptchaMetadataFactory captchaMetadataFactory;
    private CaptchaSolutionStore captchaSolutionStore;
    private VerificationEngine verificationEngine;
    private PrintableCharFinder printableCharFinder;

    public PixelCaptchaController() {
        printableCharFinder = PrintableCharFinder.getInstance();
        this.identifier = UUID.randomUUID().toString();
        this.captchaSolutionStore = PixelCaptchaSolutionStore.getInstance();
    }

    public static void main(String... args) {
        iterateAllVertical();
        if (true)
            return;
        Properties p = new Properties();

        p.setProperty("captchaWidth", "400");
        p.setProperty("captchaHeight", "300");

        String[] cCount = {"2", "3", "4"};
        String[] rCount = {"10", "11", "12"};
        String[] ranges = {"0-255", "0-4095", "0-65535"};

        PixelCaptchaController pcc = new PixelCaptchaController();
        pcc.initProperties(p);
        BufferedImage bi;
        for (int i = 0; i < 100; i++) {
            Captcha c = pcc.getCaptcha();
            String home = System.getProperty("user.home");
            File outputFile = new File(home + "/captchasamples/v_saved" + i + ".png");

            try {
                ImageIO.write(c.getImage(), "png", outputFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Done!");
//		BufferedImage bi = CaptchaBuilder.buildImage(captchaMetadataFactory.getCaptchaMetadata());
//		Captcha c = pcc.getCaptcha();
//		BufferedImage bi = c.getImage();


    }

    public static void iterateAllVertical() {
        Properties p = new Properties();
        p.setProperty("captchaWidth", "300");
        p.setProperty("captchaHeight", "400");
        String orientation = "vertical";
        orientation = "horizontal";

        String[] cCount = {"2", "3", "4"};
        cCount = new String[]{"4"};
        String[] rCount = {"10", "11", "12"};
        rCount = new String[]{"12"};
        String[] ranges = {"0-255", "0-4095", "0-65535"};
        ranges = new String[]{"0-155"};

        PixelCaptchaController pcc = new PixelCaptchaController();
        BufferedImage bi;
        String home = System.getProperty("user.home");
        String date = (new Date()).toString().replaceAll(":", "_").replaceAll(" ", "_");
        String dir = home + "/captchasamples/" + date;
        (new File(dir)).mkdir();


        int totalCount = 0;
        long timeNow = System.currentTimeMillis();
        for (String range : ranges) {
            for (String chal : cCount) {
                for (String resp : rCount) {
                    p.setProperty("responseCount", resp);
                    p.setProperty("challengeCount", chal);
                    p.setProperty("codePoints", range);
                    pcc.initProperties(p);
                    for (int i = 0; i < 40; i++) {
                        Captcha c = pcc.getCaptcha();
                        totalCount++;
                        File outputFile = new File(dir + "/" + orientation + "_" + chal + "x" + resp + "__" + range + "__" + i + "____" + c.getIdentifier() + ".png");
                        try {
                            ImageIO.write(c.getImage(), "png", outputFile);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        System.out.println(totalCount);
        System.out.println((System.currentTimeMillis() - timeNow) / 1000);


    }

    public void initProperties(Properties properties) {
        int challengeCount;
        int responseCount;
        int captchaWidth;
        int captchaHeight;
        int[] codePoints;
        int[] printableCodePoints;
        boolean ordered;

        challengeCount = Helper.convertStringToIntOrDefault(properties.getProperty("challengeCount"), GlobalConstants.DEFAULT_CHALLENGE_COUNT);
        responseCount = Helper.convertStringToIntOrDefault(properties.getProperty("responseCount"), GlobalConstants.DEFAULT_RESPONSE_COUNT);

        captchaWidth = Helper.convertStringToIntOrDefault(properties.getProperty("captchaWidth"), GlobalConstants.DEFAULT_WIDTH);
        captchaHeight = Helper.convertStringToIntOrDefault(properties.getProperty("captchaHeight"), GlobalConstants.DEFAULT_HEIGHT);

        codePoints = Helper.convertCodePointsToSortedIntArrayNoDuplicates(properties.getProperty("codePoints"), GlobalConstants.DEFAULT_CHAR_RANGE);
        printableCodePoints = identifyPrintableCodePoints(codePoints);

        if (properties.getProperty("ordered") != null && properties.getProperty("ordered").equals("true")) {
            ordered = true;
        } else {
            ordered = false;
        }


        CaptchaDimension captchaDimension = new CaptchaDimension(captchaWidth, captchaHeight);
        ChallengeAndResponseCount challengeAndResponseCount;

        challengeAndResponseCount = getChallengeResponseCount(challengeCount, responseCount);
        masterConfig = new MasterConfig(captchaDimension, printableCodePoints, challengeAndResponseCount, ordered);
        captchaMetadataFactory = new CaptchaMetadataFactory(masterConfig);
        verificationEngine = VerificationEngine.getInstance();

    }

    private int[] identifyPrintableCodePoints(int[] codePoints) {
        List<Integer> printableCodePoints = new ArrayList<>();
        for (int cp : codePoints) {
            if (printableCharFinder.isPrintableChar(cp)) {
                printableCodePoints.add(cp);
            }
        }
        return Helper.convertIntegerListToIntArray(printableCodePoints);
    }

    private ChallengeAndResponseCount getChallengeResponseCount(int challengeCount, int responseCount) {
        ChallengeAndResponseCount carc;
        switch (challengeCount) {
            case 2:
                switch (responseCount) {
                    case 10:
                        carc = ChallengeAndResponseCount.TWO_TEN;
                        break;
                    case 11:
                        carc = ChallengeAndResponseCount.TWO_ELEVEN;
                        break;
                    case 12:
                        carc = ChallengeAndResponseCount.TWO_TWELVE;
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid challenge response count combination");

                }
                break;
            case 3:
                switch (responseCount) {
                    case 10:
                        carc = ChallengeAndResponseCount.THREE_TEN;
                        break;
                    case 11:
                        carc = ChallengeAndResponseCount.THREE_ELEVEN;
                        break;
                    case 12:
                        carc = ChallengeAndResponseCount.THREE_TWELVE;
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid challenge response count combination");
                }
                break;
            case 4:
                switch (responseCount) {
                    case 10:
                        carc = ChallengeAndResponseCount.FOUR_TEN;
                        break;
                    case 11:
                        carc = ChallengeAndResponseCount.FOUR_ELEVEN;
                        break;
                    case 12:
                        carc = ChallengeAndResponseCount.FOUR_TWELVE;
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid challenge response count combination");
                }
                break;
            default:
                throw new IllegalArgumentException("challengeCount is invalid");
        }
        return carc;

    }


//	private BufferedImage getCaptchaImage() {
//		BufferedImage bi = CaptchaBuilder.buildImage(captchaMetadataFactory.getCaptchaMetadata());
//		return bi;
//	}

    private static void paintDebuggingInformation(BufferedImage bi, CaptchaMetadata captchaMetadata, Rectangle challengeRect, Rectangle responseRect) {
        Graphics2D g2d = bi.createGraphics();
        String dot = ".";
        Font dotFont = new Font(Font.SANS_SERIF, Font.BOLD, 50);

        g2d.setColor(Color.RED);
        g2d.setFont(dotFont);
        for (PointProperty pp : captchaMetadata.getChallenge()) {
            g2d.drawString(
                    dot,
                    (int) pp.getPoint().getX(),
                    (int) pp.getPoint().getY()
            );
        }

        g2d.setColor(Color.GREEN);
        g2d.setFont(dotFont);
        for (PointProperty pp : captchaMetadata.getSolutionOptions()) {
            g2d.drawString(
                    dot,
                    (int) pp.getPoint().getX(),
                    (int) pp.getPoint().getY()
            );
        }

        g2d.setColor(Color.BLUE);
        g2d.setFont(dotFont);
        for (Point p : captchaMetadata.getSolution().getPoints()) {
            g2d.drawString(
                    dot,
                    p.x,
                    p.y
            );
        }

        g2d.setColor(Color.black);
        g2d.draw(challengeRect);
        g2d.draw(responseRect);
    }


    public Captcha getCaptcha() {
        if (masterConfig == null)
            throw new IllegalAccessError("initProperties must be called prior to this method");
        CaptchaMetadata cmd = captchaMetadataFactory.getCaptchaMetadata();
//		BufferedImage bi = CaptchaBuilder.buildImage(cmd);
        BufferedImage bi = CaptchaBuilder.buildImageWithCollapsedChallenge(cmd);
//        paintDebuggingInformation(bi, cmd, masterConfig.getChallengeRectangle(), masterConfig.getResponseRectangle() );
        String id = this.captchaSolutionStore.storeCaptchaSolution(cmd.getSolution());
//        try {
//            debugInfo(cmd, id);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return new PixelCaptcha(bi, id);
    }


    private String createPointString(CaptchaMetadata cmd, String id) {
        StringBuilder sb = new StringBuilder();
        String challenges = getStringFromPP(cmd.getChallenge());
        String responses = getStringFromPP(cmd.getSolutionOptions());
        sb.append("\n\n+++++++++++++++++++++++++++++++++++++++++++++++++++\nID = [");
        sb.append(id);
        sb.append("], ");
        sb.append("\n\tChallenges => ");
        sb.append(challenges);
        sb.append("\n\tResponses => ");
        sb.append(responses);
        return sb.toString();

    }

    private String getStringFromPP(List<PointProperty> input) {
        StringBuilder sb = new StringBuilder();
        for (PointProperty pp : input) {
            Font font = pp.getFont();
            AffineTransform aft = font.getTransform();
            sb.append("\n\t\tPoint = [");
            sb.append(pp.getPoint());
            sb.append("], ");
            int ch = pp.getStringToWrite().charAt(0);
            sb.append("Character Value = [" + String.valueOf(ch) + ", " + (int) ch + ", 0x" + Integer.toHexString(ch) + ", " + Character.isWhitespace(ch) + "], ");
            sb.append("Font Size = ");
            sb.append(font.getSize());
            sb.append(", ");
            sb.append("[ScaleX, ScaleY] = [");
            sb.append(aft.getScaleX());
            sb.append(", ");
            sb.append(aft.getScaleY());
            sb.append("], ");
            sb.append("[ShearX, ShearY] = [");
            sb.append(aft.getShearX());
            sb.append(", ");
            sb.append(aft.getShearY());
            sb.append("], ");
        }
        return sb.toString();
    }

    public ValidationResult verifyCaptcha(String captchaIdentifier, List<Point> solution) {
        Optional<CaptchaSolution> captchaSolutionOptional = captchaSolutionStore.getCaptchaSolution(captchaIdentifier);
        if (captchaSolutionOptional.isPresent()) {
            return verificationEngine.verifySolution(captchaSolutionOptional.get(), solution);
        } else {
            return new PixelCaptchaValidationResult(false, 0, "Solution not found or expired");
        }
    }

    public String getIdentifier() {
        return this.identifier;
    }
}
