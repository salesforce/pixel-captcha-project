/*
 * Copyright (c) 2017, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license.
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */

package com.salesforce.pixelcaptcha.demo.unused;

import com.google.gson.Gson;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Gursev Singh Kalra @ Salesforce.com
 */
public class TestCaptchaGenerator {
    private static SecureRandom random = new SecureRandom();
    private static String getRandomImageAsString(){

        BufferedImage bi = new BufferedImage(
                300, 300, BufferedImage.TYPE_USHORT_555_RGB
        );
        Graphics2D g2d = bi.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(
                0, 0, 300, 300
        );
        bi.createGraphics();
        g2d.setColor(Color.BLACK);
        g2d.drawString("DD", 100, 100);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bi, "png", baos);
        } catch (IOException e) {
            System.out.println(e);
        }
        String encoded = Base64.getEncoder().encodeToString(baos.toByteArray());
//        System.out.println(encoded);

        return encoded;
    }

    public static String getRandomCaptcha() {
        Map<String, String> m = new HashMap<>();
        m.put("id", new BigInteger(100, random).toString(32) );
        m.put("width", Integer.toString(300));
        m.put("height", Integer.toString(300));
        m.put("image", "data:image/png;base64,"+getRandomImageAsString());
        Gson gson = new Gson();
        return gson.toJson(m);
    }

    public static void main(String[] args) {
        System.out.println(getRandomCaptcha());
    }


}
