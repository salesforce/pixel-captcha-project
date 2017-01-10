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
import java.util.Arrays;

/**
 * @author Gursev Singh Kalra @ Salesforce.com
 */
public class FontRotationTest {
    public static void main(String... args) {
        String toWrite = "A";
        int size = 300;
        Font font = getRegularFont(size);
        writeImageWithFont(font, size, toWrite, "regular");
        font = getRotatedFont(size);
        writeImageWithFont(font, size, toWrite, "regularNoPointForRotation");

        for (int rotation = 0; rotation < 150; rotation += 20) {
            for (int pt = 0; pt < size; pt += 20) {
                Point p = new Point(pt, 0);
                font = getRotatedFontAtPoint(size, p, rotation);
                writeImageWithFont(font, size, toWrite, "rotation_" + rotation + "point_" + p);
            }
        }

    }

    private static void writeImageWithFont(Font font, int size, String toWrite, String name) {
        String homeDir = System.getProperty("user.home") + "/temp";
        BufferedImage bi = new BufferedImage(size, size, BufferedImage.TYPE_BYTE_BINARY);
        Graphics2D g2d;
        g2d = bi.createGraphics();
        StringBuilder sb;
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, size, size);
        g2d.setColor(Color.BLACK);
        g2d.setFont(font);
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

    private static Font getRotatedFont(int size) {
        Font font = new Font(Font.SANS_SERIF, Font.PLAIN, size / 3);
        AffineTransform aft = new AffineTransform();
        aft.rotate(Math.toRadians(20));
        return font.deriveFont(aft);
    }

    private static Font getRotatedFontAtPoint(int size, Point point, int rotation) {
        Font font = new Font(Font.SANS_SERIF, Font.PLAIN, size / 3);
        AffineTransform aft = new AffineTransform();
        aft.rotate(Math.toRadians(rotation), point.getX(), point.getY());
//        aft.rotate(20, 0, 0);
        return font.deriveFont(aft);
    }

}
