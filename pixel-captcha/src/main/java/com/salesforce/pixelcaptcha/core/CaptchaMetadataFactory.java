/*
 * Copyright (c) 2017, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license.
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */

package com.salesforce.pixelcaptcha.core;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.*;

import com.salesforce.pixelcaptcha.dataobj.CaptchaDimension;
import com.salesforce.pixelcaptcha.dataobj.CaptchaSolution;
import com.salesforce.pixelcaptcha.dataobj.PointProperty;
import com.salesforce.pixelcaptcha.dataobj.CaptchaMetadata;
import com.salesforce.pixelcaptcha.dataobj.impl.PixelCaptchaSolution;
import com.salesforce.pixelcaptcha.dataobj.impl.PixelCaptchaPointProperty;
import com.salesforce.pixelcaptcha.utils.RandomFontFactory;
import com.salesforce.pixelcaptcha.utils.RandomStringFactory;
import com.salesforce.pixelcaptcha.utils.impl.RandomTransformFactoryImpl;
import com.salesforce.pixelcaptcha.utils.impl.RandomUnicodeStringFactoryImpl;
import com.salesforce.pixelcaptcha.utils.Helper;
import com.salesforce.pixelcaptcha.utils.RandomPointFactory;

/**
 * @author Gursev Singh Kalra @ Salesforce.com
 *         This class contains most of the math required for generating the metadata for a CAPTCHA instance.
 */
public class CaptchaMetadataFactory {
    private static final Color responseColor = Color.BLACK;
    private static final Color challengeColor = Color.BLUE;
    private RandomFontFactory challengeFontFactory;
    private RandomTransformFactoryImpl challengeTransformFactory;
    private static int BUFFER = 10;
    private static double STD_DEVIATION_RATIO = 1.5;

    private RandomFontFactory responseFontFactory;
    private RandomTransformFactoryImpl responseTransformFactory;

    private RandomStringFactory randomStringFactory;
    private int challengeCount;
    private int responseCount;
    private Rectangle challengeRect;
    private Rectangle responseAndNoiseRect;
    private int averageFontSize;
    private boolean ordered;
    private boolean isHorizontalCaptcha;
    private CaptchaDimension captchaDimensions;
    private MasterConfig masterConfig;


    public CaptchaMetadataFactory(MasterConfig masterConfig) {
        if (masterConfig == null)
            throw new NullPointerException("Master Config cannot be null");

        this.masterConfig = masterConfig;

        this.challengeFontFactory = new RandomFontFactory.Builder()
//                .minFontSize(masterConfig.getMinFontSize())
                .minFontSize(masterConfig.getAvgFontSize()) //Using larger minimum font size for challenge characters for better clarity and usability
                .maxFontSize(masterConfig.getMaxFontSize())
                .build();

        this.responseFontFactory = new RandomFontFactory.Builder()
                .minFontSize(masterConfig.getMinFontSize())
                .maxFontSize(masterConfig.getMaxFontSize())
                .build();

        // With default shear values
        this.challengeTransformFactory = new RandomTransformFactoryImpl.Builder()
                .minScaleX(0.8)
                .maxScaleX(1.2)
                .minScaleY(0.8)
                .maxScaleY(1.2)
                .minRotation(0)
                .maxRotation(40)
                .build();

        this.responseTransformFactory = new RandomTransformFactoryImpl.Builder()
                .minScaleX(0.8)
                .maxScaleX(1.2)
                .minScaleY(0.8)
                .maxScaleY(1.2)
                .minRotation(0)
                .maxRotation(60)
                .minShearY(0)
                .maxShearY(0.4)
                .minShearX(0.0)
                .maxShearX(0.4)
                .build();


        this.randomStringFactory = new RandomUnicodeStringFactoryImpl(masterConfig.getPrintableCodePoints());


        /*
        These fields are copied over from MasterConfig to increase readability of this class.
        Merge this and MasterConfig class in future?
         */
        this.challengeCount = masterConfig.getChallengeResponseCount().challengeCount();
        this.responseCount = masterConfig.getChallengeResponseCount().responseCount();
        this.challengeRect = masterConfig.getChallengeRectangle();
        this.responseAndNoiseRect = masterConfig.getResponseRectangle();
        this.averageFontSize = masterConfig.getAvgFontSize();
        this.ordered = masterConfig.isOrdered();
        this.isHorizontalCaptcha = masterConfig.isHorizontalCaptcha();
        this.captchaDimensions = masterConfig.getCaptchaDimensions();

    }


    public CaptchaMetadata getCaptchaMetadata() {
        // This holds the sum of distance of all clicks from the center of the response characters.
        double maxDeviation = 0;

        CaptchaMetadata captchaMetadata = null;
        List<PointProperty> responsePP = new ArrayList<>();
        List<PointProperty> challengePP = new ArrayList<>();
        List<Point> solution = new ArrayList<>();

        // Start building response area
        // The points variable contains all points created so far. This is used to calculate appropriate distance
        // for the new point. For each new point, the RandomPointFactory.getRandomPointWithDistance call makes sure that
        // the new point is at least averageFontSize from all the existing points in the responseAndNoise
        // The current code does not consider the possibility of duplicate string being present.
        List<Point> points = new ArrayList<>();
        Set<String> stringSet = new HashSet<>();

        for (int i = 0; i < this.responseCount; i++) {
            AffineTransform aft = responseTransformFactory.getRandomTransform();
            Font font = responseFontFactory.getRandomFont();
            font = font.deriveFont(aft);

            // Make sure that characters do not repeat
            String string;
            boolean exists;
            do {
                string = randomStringFactory.getOneCharString();
                exists = stringSet.contains(string);
            } while (exists);

            stringSet.add(string);
            // Maintain average font size difference between two points for usability
            Point point = RandomPointFactory.getRandomPointWithDistance(responseAndNoiseRect, points, averageFontSize);
            points.add(point);

            responsePP.add(new PixelCaptchaPointProperty(string, point, font, this.responseColor));
        }


        //Start building challenge area and the solution
        RandomPointFactory rpf = new RandomPointFactory(BUFFER, BUFFER);
        AffineTransform aft;

        // The responseChooser will be used to choose the random characters from responsePP while constructing the
        // challenge array.
        int[] randomResponseChooser = new int[responsePP.size()];
        for (int i = 0; i < randomResponseChooser.length; i++)
            randomResponseChooser[i] = i;
        Helper.shuffleIntArray(randomResponseChooser); // randomize the array

        // Only one commonFont for the entire challenge
        // Pick the first element
        Font commonFont = challengeFontFactory.getRandomFont();
        Font newFont;

		/*
        Horizontal CAPTCHA has vertical challenge points, whereas Vertical CAPTCHA has horizontal challenge points.
		 */
        Point point;
        if (this.isHorizontalCaptcha) {
            //The challenge is vertical for horizontal CAPTCHA.
            point = RandomPointFactory.getFirstVChallengePointFromTopInRegion(challengeRect, challengeCount);
        } else {
            //The challenge is horizontal for Vertical CAPTCHA.
            point = RandomPointFactory.getFirstHChallengePointFromLeftInRegion(challengeRect, challengeCount);
        }

        PointProperty pp = responsePP.get(randomResponseChooser[0]); // Pick the first value from the random array
        aft = challengeTransformFactory.getRandomTransform();
        newFont = commonFont.deriveFont(aft);
        challengePP.add(new PixelCaptchaPointProperty(pp.getStringToWrite(), point, newFont, this.challengeColor));

		/*
		 It is important to note that the solution point coordinates are different
		 than the coordinates where the actual challenge will be drawn.
		 For example, in the ascii art below, if the challenge coordinate is at C, the solution coordinate
		 will be somewhere in the middle of the text and around S. This is what the following line tries to achieve.
		 The code also takes into account any rotation for the particular commonFont.
		     +-------------+
		    +			  +
		   +	  S		 +
		  +             +
		 C-------------+

		 */
        solution.add(computeSolutionFromChallenge(pp)); // also considers the rotation


        maxDeviation += pp.getFont().getSize();
        // Now pick the rest of the elements
        for (int i = 1; i < this.challengeCount; i++) {
            pp = responsePP.get(randomResponseChooser[i]);
            maxDeviation += pp.getFont().getSize();
            // Note that the same commonFont is used for all challenges. This may be to change for better security? Need to analyze.
            // Possible add random rotation for exact same properties
            if (this.isHorizontalCaptcha) {
//                The challenge is vertical for horizontal CAPTCHA.
                point = rpf.getVChallengePointWrtPercentageSizeOfReferenceFont(this.challengeRect, point, commonFont, commonFont);
            } else {
                //The challenge is horizontal for Vertical CAPTCHA.
                point = rpf.getHChallengePointWrtPercentageSizeOfReferenceFont(this.challengeRect, point, commonFont, commonFont);
            }

//            newFont = challengeFontFactory.randomlyRotateFont(point, commonFont);
            aft = challengeTransformFactory.getRandomTransform();
            newFont = commonFont.deriveFont(aft);

            challengePP.add(new PixelCaptchaPointProperty(pp.getStringToWrite(), point, newFont, this.challengeColor));
            solution.add(computeSolutionFromChallenge(pp));
        }

        maxDeviation = maxDeviation / STD_DEVIATION_RATIO;
        CaptchaSolution pCaptchaSolution = new PixelCaptchaSolution(solution, maxDeviation, this.ordered);
        captchaMetadata = new CaptchaMetadata(challengePP, responsePP, pCaptchaSolution, this.captchaDimensions);

        return captchaMetadata;
    }


    /**
     * This method calculates the center position of the response character which may be rotated
     * during the random font generation process. Obtaining an approximate central position
     * of the font allows me to position the CAPTCHA solution towards the center of the
     * character at its current rotation and makes the verification simpler and more accurate during the
     * user clicks.
     *
     * @param pp
     * @return
     */
    private Point computeSolutionFromChallenge(PointProperty pp) {
        Point rotatedPoint;
        int fontSize = pp.getFont().getSize();
        Point referencePoint = pp.getPoint();
        double charHeight = fontSize * GlobalConstants.V_FONT_MULTIPLIER;
        double charWidth = fontSize;
        // Since the character's first position is upright, and entire character is to the right of the reference point,
        // The regular mid character point is going to be:
        // 1. x coordinate = referenceX + charWidth/2
        // 2. y coordinate = referenceY - charHeight/2
        Point midPoint = new Point((int) (referencePoint.getX() + charWidth / 2), (int) (referencePoint.getY() - charHeight / 2));
        // There is no method to retrieve rotation from an AffineTransform.
        // https://groups.google.com/forum/#!topic/uw.cs.cs349/gpaYRPQggvc
        // What works it the following:
        // Math.atan2(aft.getShearY(), aft.getScaleY());
        // aft is the AffineTransform
        AffineTransform aft = pp.getFont().getTransform();
        double rotation = Math.toDegrees(Math.atan2(aft.getShearY(), aft.getScaleY()));
        rotatedPoint = Helper.rotatePoint(referencePoint, midPoint, (int) rotation);
        return rotatedPoint;
    }

}
