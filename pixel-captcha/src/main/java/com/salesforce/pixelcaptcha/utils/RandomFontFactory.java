/*
 * Copyright (c) 2017, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license.
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */
package com.salesforce.pixelcaptcha.utils;

import java.awt.*;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.common.base.Optional;

public class RandomFontFactory {

	/*
	Font size
	 */
	public static final int MIN_SUPPORTED_FONT_SIZE = 10;
	public static final int MAX_SUPPORTED_FONT_SIZE = 2000;
	public static final int DEFAULT_MIN_FONT_SIZE = 30;
	public static final int DEFAULT_MAX_FONT_SIZE = 45;

	private static final SecureRandom rand;
	static {
		rand = new SecureRandom();
	}
	
	public static final List<String> FONTS_SUPPORTED = (List<String>) Collections.unmodifiableList(
			Arrays.asList(
					Font.SANS_SERIF, 
					Font.SERIF,
					Font.DIALOG,
					Font.DIALOG_INPUT, 
					Font.MONOSPACED
					)
			); 
	
	private Optional<String> fontName;
	private Optional<Boolean> bold;
	private Optional<Boolean> italic;
    // Font and rotation can be chosen to be between two values
    private Optional<Integer> minFontSize;
    private Optional<Integer> maxFontSize;


	public static class Builder {
		/*
		 *
		 * These instance variables if set will take a particular value, otherwise they will be chosen at random.
		 * 
		 * For example, if fontName is Optional.absent(), a random Font will be assigned for for each call. 
		 * If a name is chosen, the particular font is used instead. 
		 *
		 * bold and italic work in a similar fashion. If any of these is set to Optional.absent(),
		 * their value is a randomly chosen random when a font is returned. If a particular value is provided, 
		 * that value is set for all generated Fonts. 
		 */
		  
		private Optional<String> fontName;
		private Optional<Boolean> bold;
		private Optional<Boolean> italic;
		private Optional<Integer> minFontSize;
		private Optional<Integer> maxFontSize;


		public Builder() {
			// If not explicitly initialized later, gets a random value of one allowed Font name from ALLOWED_FONTS
			fontName = Optional.absent(); 
			
			// If not initialized to true or false, get random value of true or false.
			bold = Optional.absent(); 
			italic = Optional.absent();      
			
			minFontSize = Optional.absent(); 
			maxFontSize = Optional.absent();

		}
		
		public Builder minFontSize(int minFontSize) {
			if(minFontSize < MIN_SUPPORTED_FONT_SIZE) 
				throw new IllegalArgumentException("Font size cannot be less than " + MIN_SUPPORTED_FONT_SIZE);
			this.minFontSize = Optional.of(minFontSize);
			return this;
		}

		public Builder maxFontSize(int maxFontSize) {
			if(maxFontSize > MAX_SUPPORTED_FONT_SIZE)
				throw new IllegalArgumentException("Maximum font size cannot be greater than " + MAX_SUPPORTED_FONT_SIZE);
			this.maxFontSize = Optional.of(maxFontSize);
			return this;
		}

		public Builder fontName(String fontName) {
			validateFontName(fontName);
			this.fontName = Optional.of(fontName);
			return this;
		}
		
		private void validateFontName(String fontName){
			if(fontName == null) {
				throw new NullPointerException("Font name cannot be null");
			}
			
			for(String allowed: FONTS_SUPPORTED) {
				if(allowed.equals(fontName))
					return;
			}
			
			throw new IllegalArgumentException("Illegal Font name received");
		}
		
		
		public Builder bold(boolean bold) {
			this.bold = Optional.of(bold);
			return this;
		}
		
		public Builder italic(boolean italic) {
			this.italic = Optional.of(italic); 
			return this;
		}		


		private void setDefaultFontSizeIfNotPresent() {
			if(!this.minFontSize.isPresent())
				this.minFontSize = Optional.of(DEFAULT_MIN_FONT_SIZE);

			if(!this.maxFontSize.isPresent())
				this.maxFontSize = Optional.of(DEFAULT_MAX_FONT_SIZE);
		}


		private void setDefaultsIfNotPresent() {
			setDefaultFontSizeIfNotPresent();
		}
		
		private void validateFontSizes() {
			if(this.minFontSize.get() > this.maxFontSize.get()) {
				throw new IllegalArgumentException("Maximum font size cannot be less than minimum font size");
			}				
		}

		private void validateValues() {
			validateFontSizes();
		}
		
		public RandomFontFactory build() {
			setDefaultsIfNotPresent();
			validateValues();
			return new RandomFontFactory(this);
		}
	}
	
	private RandomFontFactory(Builder builder) {
		this.fontName = builder.fontName;
		this.bold = builder.bold;
		this.italic = builder.italic;
		
		this.minFontSize = builder.minFontSize;
		this.maxFontSize = builder.maxFontSize;
	}


    private int getFontStyle() {
        int fontStyle = 0;
        int temp;

        if(this.bold.isPresent()) {
            if(this.bold.get())
                fontStyle |= Font.BOLD;
        } else {
            temp = rand.nextBoolean() ? Font.BOLD : 0;
            fontStyle |= temp;
        }

        if(this.italic.isPresent()) {
            if(this.italic.get())
                fontStyle |= Font.ITALIC;
        } else {
            temp = rand.nextBoolean() ? Font.ITALIC : 0;
            fontStyle |= temp;
        }

        return fontStyle;

    }


    public Font getRandomFont() {
		Font randomFont;
		int fontSize;
		String fontName;
        int fontStyle;
		
		if(this.fontName.isPresent()) {
			fontName = this.fontName.get(); 
		} else {
			fontName = FONTS_SUPPORTED.get(rand.nextInt(FONTS_SUPPORTED.size()));
		}

		// 1 is added to make sure that 0 is not passed to the random number generator and appropriate random values
        // are generated
		fontSize = this.minFontSize.get() + rand.nextInt(this.maxFontSize.get() - this.minFontSize.get() + 1);
		fontStyle = getFontStyle();

		randomFont = new Font(fontName, fontStyle, fontSize);
		return randomFont;
	}
}
