/*
 * Copyright (c) 2017, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license.
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */

package com.salesforce.pixelcaptcha.misc;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author Gursev Singh Kalra @ Salesforce.com.
 */
public class FontRotationTestWithSeparateAft {
    public static void main(String... args) {
        AffineTransform aft;
        String toWrite = "ABCD";
        int size = 300;
        Font regularFont = getRegularFont(size);
        aft = getAftRotationFirst();
        writeImageWithFont(regularFont, aft, size, toWrite, "rotationFirst");
        aft = getAftRotationLast();
        writeImageWithFont(regularFont, aft, size, toWrite, "rotationLast");


    }

    private static void writeImageWithFont(Font font, AffineTransform aft, int size, String toWrite, String name) {
        String homeDir = System.getProperty("user.home") + "/temp";
        BufferedImage bi = new BufferedImage(size, size, BufferedImage.TYPE_BYTE_BINARY);
        Graphics2D g2d;
        g2d = bi.createGraphics();
        StringBuilder sb;
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, size, size);
        g2d.setColor(Color.BLACK);
        g2d.setFont(font);
        g2d.setTransform(aft);
        g2d.drawString(toWrite, size / 2, size / 2);
        File outputFile = new File(homeDir + "/" + name + ".png");
        try {
            ImageIO.write(bi, "png", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private static Font getRegularFont(int size) {
        return new Font(Font.SANS_SERIF, Font.PLAIN, size / 3);
    }

    private static AffineTransform getAftRotationFirst() {
        AffineTransform aft = new AffineTransform();
        aft.shear(0.5, 0.5);
        aft.scale(1.5, 1.5);
        aft.rotate(Math.toRadians(45));
        return aft;
    }

    private static AffineTransform getAftRotationLast() {
        AffineTransform aft = new AffineTransform();
        aft.rotate(Math.toRadians(45));
        aft.shear(0.5, 0.5);
        aft.scale(1.5, 1.5);
        return aft;
    }

    private static AffineTransform getAft() {
        AffineTransform aft = new AffineTransform();
        aft.rotate(Math.toRadians(45));
        aft.shear(0.5, 0.5);
        aft.scale(1.5, 1.5);
        return aft;
    }


}
