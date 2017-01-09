/*
 * Copyright (c) 2017, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license.
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */

package com.salesforce.pixelcaptcha.utils.impl;

import java.awt.Font;
import java.security.SecureRandom;
import java.util.Arrays;

import com.salesforce.pixelcaptcha.utils.Helper;
import com.salesforce.pixelcaptcha.utils.RandomStringFactory;

/**
 * This class generates Random Unicode Strings.
 */
public class RandomUnicodeStringFactoryImpl implements RandomStringFactory {
	
	public static final int MAX_LENGTH = 100;
	public static final int ONE = 1;
	private int[] printableCodePoints;
	private static final SecureRandom rand;
	static {
		rand = new SecureRandom();
	}

	/**
	 * Initialize the class with a Code Point array. This class assumes that all the characters passed to it are printable
     * for the Font for which particular this RandomUStringFactory instance is being created.
     * If that is not the case, this class will produce wrong output. The input array should be sorted.
	 * This class makes no guarantee to check the contents of printableCodePointArray. Bad input will result in bad output.
	 * @param printableCodePointArray
	 */
	public RandomUnicodeStringFactoryImpl(int[] printableCodePointArray) {
		if(printableCodePointArray == null || printableCodePointArray.length == 0)
			throw new IllegalArgumentException("PrintableCodePointArray is either null or of zero length");
		Helper.validateCodePointValues(printableCodePointArray);
		this.printableCodePoints = Arrays.copyOf(printableCodePointArray, printableCodePointArray.length);
	}

    /**
     * This method returns a random string as with length equal to the length parameter.
     * @param font Font for which the string is to be generated. The font parameter is currently not being used. Provide any font
     * @param length Length of the returned string. Must be greater than 0 and less than MAX_LENGTH
     * @return
     */
	@Override
	public String getString(Font font, int length) {
		throw new UnsupportedOperationException("getString(Font font, int length)  method is not implemented yet");
    }

    @Override
	public String getOneCharString(Font font) {
		return getString(font, ONE);
	}

	@Override
	public String getString(int length) {
		StringBuilder sb = new StringBuilder();
		if(length <= 0 || length > MAX_LENGTH)
			throw new IllegalArgumentException("String length must be greater than 0 and less that or equal to " + MAX_LENGTH);

		for(int i = 0; i < length; i++)
			sb.append(Character.toChars(printableCodePoints[rand.nextInt(printableCodePoints.length)]));

		return sb.toString();

	}

	@Override
	public String getOneCharString() {
		return getString(ONE);
	}
}