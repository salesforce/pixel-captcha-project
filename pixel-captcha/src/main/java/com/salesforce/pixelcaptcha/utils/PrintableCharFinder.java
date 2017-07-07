/*
 * Copyright (c) 2017, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license.
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */
package com.salesforce.pixelcaptcha.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import com.google.common.base.CharMatcher;

import java.util.Scanner;
import java.util.logging.Logger;

/**
 * @author Gursev Singh Kalra @ Salesforce.com
 *         This class initializes its contents from serif_printable.txt inside resources.
 *         It creates a boolean array of size 65536 and stores true or false inside each index.
 *         A true at an index means that the character is printable and will leave a visible impression on the CAPTCHA
 *         A true at an index means that the character is not printable and will not leave a visible impression on the CAPTCHA
 */
public class PrintableCharFinder {

    private static final Logger LOGGER = Logger.getLogger( PrintableCharFinder.class.getName() );
    private static int CAPTCHA_SIZE = 100;
    private static int SIZE = 65536;
    private static int WHITE = -1;
    private static String suffix = "_printable.txt";
    private static String directory = "printable_chars";
    private static String separator = "/";
    private static Font font = new Font(Font.SERIF, Font.PLAIN, CAPTCHA_SIZE / 4);

    //Stores the exact code points that are printable. It is not being used at the moment.
    private int[] printableGlyphsInt = new int[SIZE];

    // The boolean array contains a true/false for each location in an array so querying is simple
    // An index for which a printable character is available has the value set to true, everything else is false.
    private boolean[] printableGlyphsBoolean = new boolean[SIZE];
    private static PrintableCharFinder instance;

    private PrintableCharFinder() {
        loadPrintableCharsFromResourceFile(font);
    }

    public static synchronized PrintableCharFinder getInstance() {
        if (instance == null) {
            instance = new PrintableCharFinder();
        }
        return instance;
    }

    public boolean isPrintableChar(int codePoint) {
        if (codePoint < 0 || codePoint >= SIZE)
            throw new IllegalArgumentException("codePoint parameter cannot be greater than " + SIZE + " or less than " + 0);

        return printableGlyphsBoolean[codePoint];
    }


    private void loadPrintableCharsFromResourceFile(Font font) {
        int[] cPs = new int[SIZE];
        int index = 0;

        // The serif_printable.txt file currently has code points for all the printable characters for Serif font.
        // It appears to work for the CAPTCHA at this time even when code points for other Fonts are provided.
        // This WILL change when physical and fancy fonts are used.
        // TODO: In future, I may try to use Font specific printable characters.
        /*
        The split is needed because different JDK versions return different values for getFontName. For example, some versions
        return Serif, others return Serif.plain. This was causing issues when trying to load the serif_printable.txt resource
        between the different JDK versions.
         */
        String fontName;
        String resourcePath;
        Scanner sc;

        if(System.getenv("codePointList") != null) {
            resourcePath = System.getenv("codePointList");
            try {
                LOGGER.info(">>> Using file " + resourcePath + " to load unicode code points");
                sc = new Scanner(new File(resourcePath));
            } catch (FileNotFoundException ex) {
                throw new RuntimeException("File not found or invalid " + System.getProperty("codePointList"));
            }

        } else {
            LOGGER.info(">>> Using unicode code points from inbuilt resource file");
            fontName = font.getFontName().toLowerCase().split("\\.")[0];
            resourcePath = directory + separator + fontName + suffix;
            sc = new Scanner(PrintableCharFinder.class.getClassLoader().getResourceAsStream(resourcePath));

        }
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] ints = line.split(",");
            for (String i : ints) {
                cPs[index] = Integer.parseInt(i.trim());
                printableGlyphsBoolean[cPs[index]] = true;
                index++;
            }
        }
        printableGlyphsInt = Arrays.copyOf(cPs, index);
    }

    public static void main(String... args) throws FileNotFoundException, InterruptedException {
        LOGGER.info("Computing unicode code points for which your system has valid glyphs using " + font.getFontName() + " font");

        int min = 0;
        int max = 65535;
        int value = 65535;
        if(args.length != 0)
            value = Integer.parseInt(args[0]);

        if(value < 0 || value > 65535) {
            LOGGER.warning("Using default maximum unicode value of 65535 now!");
        } else {
            max = value;
        }
        int[] printables = computePrintableChars(font, min, max);
        int breakAt = 20;
        StringBuilder sb = new StringBuilder();

        int i;
        for (i = 0; i < printables.length - 1; i++) {
            if (i % breakAt == 0 && i != 0) {
                sb.append(printables[i] + "\n");

            } else {
                sb.append(printables[i] + ", ");
            }
        }
        if(printables.length > 0)
            sb.append(printables[i]);

        PrintWriter pw = new PrintWriter(System.out);
        pw.write(sb.toString());
        pw.flush();
        pw.close();
        Thread.sleep(1000);

        displayPrintableStats(printables);

    }

    private static void displayPrintableStats(int[] printables) {
        int upto_255 = 0;
        int upto_4095 = 0;
        int upto_65535 = 0;

        for(int i = 0; i < printables.length; i++) {
            if(printables[i] >= 1 && printables[i] <=255)
                upto_255++;
            if(printables[i] >= 1 && printables[i] <=4095)
                upto_4095++;
            if(printables[i] >= 1 && printables[i] <=65535)
                upto_65535++;
        }
        System.err.println("\n\nHere are the stats for number of characters available to be drawn on the CAPTCHA");
        System.err.println("+ Number of printable characters available between 0 to 255 unicode character range is " + upto_255);
        System.err.println("+ Number of printable characters available between 0 to 4095 unicode character range is " + upto_4095);
        System.err.println("+ Number of printable characters available between 0 to 65535 unicode character range is " + upto_65535 + "\n");

    }

    private static void printAvailableFonts() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fontFamilies = ge.getAvailableFontFamilyNames();
    }

    private static void rawDisplayable(int max) {
        int dispCount = 0;
        for (int i = 0; i < max; i++) {
            if (font.canDisplay(i)) {
                dispCount++;
            }
        }
    }

    private static void computeWhitespaces(int max) {
        int gWhite = 0;
        int jWhite = 0;
        boolean found = false;
        for (int i = 0; i < max; i++) {
            if (Character.isWhitespace(i)) {
                System.out.println("j => " + i);
                jWhite++;
                found = true;
            }

            if (CharMatcher.WHITESPACE.matches((char) i)) {
                System.out.println("g => " + i);
                gWhite++;
                found = true;
            }

            if (found) {
                System.out.println("============");
                found = false;
            }

        }

    }

    private static int[] computePrintableChars(Font font, int minCodePoint, int maxCodePoint) {
        if (minCodePoint >= maxCodePoint || minCodePoint < 0 || maxCodePoint <= 0) {
            throw new IllegalArgumentException("Illegal parameters to computePrintableChars");
        }

        if (font == null)
            throw new IllegalArgumentException("Font cannot be null");

        int captchaSize = CAPTCHA_SIZE;
        int[] printable = new int[maxCodePoint - minCodePoint + 1];
        int printableCount = 0;
        BufferedImage bi = new BufferedImage(captchaSize, captchaSize, BufferedImage.TYPE_BYTE_BINARY);
        Graphics2D g2d;
        g2d = bi.createGraphics();

        for (int i = minCodePoint; i <= maxCodePoint; i++) {
            if (!font.canDisplay(i) || Character.isWhitespace(i)) {
                continue;
            }

            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, captchaSize, captchaSize);
            StringBuilder sb;
            g2d.setFont(font);
            g2d.setColor(Color.BLACK);
            sb = new StringBuilder();
            sb.append(Character.toChars(i));
            g2d.drawString(sb.toString(), captchaSize / 2, captchaSize / 2);
            int nonWhiteCount = getBlackPixelCount(bi);
            if (nonWhiteCount > 0) {
                printable[printableCount++] = i;
            }
        }

        printable = Arrays.copyOf(printable, printableCount);
        return printable;
    }

    private static int[] getPrintableChars(int max) {
        int min = 0x1F601;
        min = 0;

        int captchaSize = 100;
        int[] printable = new int[max - min + 1];
        int[] nonPrintable = new int[max - min + 1];
        int printableCount = 0;
        int nonPrintableCount = 0;
        String homeDir = System.getProperty("user.home");
        homeDir = homeDir + "/printable-test/";
        BufferedImage bi = new BufferedImage(captchaSize, captchaSize, BufferedImage.TYPE_BYTE_BINARY);
        Font fontHere = new Font(Font.DIALOG_INPUT, Font.BOLD, captchaSize / 4);
        Graphics2D g2d;
        g2d = bi.createGraphics();
        StringBuilder sb;
        for (int i = min; i < max; i++) {
            if (!fontHere.canDisplay(i)) {
                nonPrintable[nonPrintableCount++] = i;
                System.out.println("[-] " + i + "is non-printable");
                continue;
            }
            sb = new StringBuilder();
            sb.append(Character.toChars(i - min));
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, captchaSize, captchaSize);
            g2d.setColor(Color.BLACK);
            g2d.setFont(fontHere);
            g2d.drawString(sb.toString(), captchaSize / 2, captchaSize / 2);
            int nonWhiteCount = getBlackPixelCount(bi);
            if (nonWhiteCount > 0) {
                printable[printableCount++] = i;
            } else {
//                nonPrintable[nonPrintableCount++] = i;
            }
            File outputFile = new File(homeDir + i + ".png");
            try {
                ImageIO.write(bi, "png", outputFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println(printableCount);

        printable = Arrays.copyOf(printable, printableCount - 1);
        return printable;
    }

    private static void prettyPrint(int[] array, int limit) {
        System.out.println("{");
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i] + ", ");
            if (i % limit == 0)
                System.out.println();
        }
        System.out.println("\n}");

    }

    private static int getBlackPixelCount(BufferedImage bi) {
        int count = 0;
        for (int i = 0; i < bi.getWidth(); i++) {
            for (int j = 0; j < bi.getHeight(); j++) {
                long pixel = bi.getRGB(i, j);
                if (pixel != WHITE) {
                    count++;
                }
            }
            if (count > 2)
                break;
        }
        return count;
    }
}
