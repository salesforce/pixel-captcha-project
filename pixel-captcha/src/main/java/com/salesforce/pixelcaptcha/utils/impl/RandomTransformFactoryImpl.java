/*
 * Copyright (c) 2017, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license.
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */

package com.salesforce.pixelcaptcha.utils.impl;

import com.google.common.base.Optional;
import com.salesforce.pixelcaptcha.utils.RandomTransformFactory;

import java.awt.geom.AffineTransform;
import java.security.SecureRandom;

public class RandomTransformFactoryImpl implements RandomTransformFactory {

    /*
    Shear
     */
    public static final double MIN_SUPPORTED_SHEAR = 0.0;
    public static final double MAX_SUPPORTED_SHEAR = 1.0;
    public static final double DEFAULT_MIN_SHEAR = 0.0;
    public static final double DEFAULT_MAX_SHEAR = 0.2;

    /*
    Rotation
     */
    public static final int MIN_SUPPORTED_ROTATION = 0;
    public static final int MAX_SUPPORTED_ROTATION = 90;
    public static final int DEFAULT_MIN_ROTATION = 0;
    public static final int DEFAULT_MAX_ROTATION = 45;

    /*
    Scale along X and Y co ordinates
     */
    public static final double DEFAULT_SCALE_X = 1.0;
    public static final double DEFAULT_SCALE_Y = 1.0;

    private static final SecureRandom rand;

    static {
        rand = new SecureRandom();
    }

    private Optional<Integer> minRotation;
    private Optional<Integer> maxRotation;

    private Optional<Double> minShearX;
    private Optional<Double> maxShearX;
    private Optional<Double> minShearY;
    private Optional<Double> maxShearY;

    private Optional<Double> minScaleX;
    private Optional<Double> minScaleY;
    private Optional<Double> maxScaleX;
    private Optional<Double> maxScaleY;


    public static class Builder {
        /*
		 * These instance variables if set will take a particular value, otherwise they will be chosen at random.
		 *
		 * For example, if fontName is Optional.absent(), a random Font will be assigned for for each call.
		 * If a name is chosen, the particular font is used instead.
		 *
		 * bold and italic work in a similar fashion. If any of these is set to Optional.absent(),
		 * their value is a randomly chosen random when a font is returned. If a particular value is provided,
		 * that value is set for all generated Fonts.
		 */

        private Optional<Integer> minRotation;
        private Optional<Integer> maxRotation;

        // Shear is always chosen to be random with only an upper bound for both X and Y shears
        // TODO: Review this in future.
        private Optional<Double> minShearX;
        private Optional<Double> maxShearX;

        private Optional<Double> minShearY;
        private Optional<Double> maxShearY;

        private Optional<Double> minScaleX;
        private Optional<Double> minScaleY;
        private Optional<Double> maxScaleX;
        private Optional<Double> maxScaleY;

        public Builder() {

            minRotation = Optional.absent();
            maxRotation = Optional.absent();

            minShearX = Optional.absent();
            maxShearX = Optional.absent();
            minShearY = Optional.absent();
            maxShearY = Optional.absent();

            minScaleX = Optional.absent();
            maxScaleX = Optional.absent();
            minScaleY = Optional.absent();
            maxScaleY = Optional.absent();

        }

        public Builder minRotation(int minRotation) {
            if (minRotation < MIN_SUPPORTED_ROTATION)
                throw new IllegalArgumentException("Minimum rotation cannot be less than " + Integer.toString(MIN_SUPPORTED_ROTATION));
            this.minRotation = Optional.of(minRotation);
            return this;
        }

        public Builder maxRotation(int maxRotation) {
            if (maxRotation > MAX_SUPPORTED_ROTATION)
                throw new IllegalArgumentException("Maximum rotation cannot be greater than " + Integer.toString(MAX_SUPPORTED_ROTATION));
            this.maxRotation = Optional.of(maxRotation);
            return this;
        }

        public Builder minShearX(double minShearX) {
            if (minShearX < MIN_SUPPORTED_SHEAR)
                throw new IllegalArgumentException("Shear value cannot be less than " + MIN_SUPPORTED_SHEAR);
            this.maxShearX = Optional.of(minShearX);
            return this;
        }

        public Builder maxShearX(double maxShearX) {
            if (maxShearX > MAX_SUPPORTED_SHEAR)
                throw new IllegalArgumentException("Shear value cannot be greater than " + MAX_SUPPORTED_SHEAR);
            this.maxShearX = Optional.of(maxShearX);
            return this;
        }

        public Builder minShearY(double minShearY) {
            if (minShearY < MIN_SUPPORTED_SHEAR)
                throw new IllegalArgumentException("Shear value cannot be less than " + MIN_SUPPORTED_SHEAR);
            this.maxShearY = Optional.of(minShearY);
            return this;
        }

        public Builder maxShearY(double maxShearY) {
            if (maxShearY > MAX_SUPPORTED_SHEAR)
                throw new IllegalArgumentException("Shear value cannot be greater than " + MAX_SUPPORTED_SHEAR);
            this.maxShearY = Optional.of(maxShearY);
            return this;
        }

        public Builder minScaleX(double minScaleX) {
            this.minScaleX = Optional.of(minScaleX);
            return this;
        }

        public Builder maxScaleX(double maxScaleX) {
            this.maxScaleX = Optional.of(maxScaleX);
            return this;
        }


        public Builder minScaleY(double minScaleY) {
            this.minScaleY = Optional.of(minScaleY);
            return this;
        }

        public Builder maxScaleY(double maxScaleY) {
            this.maxScaleY = Optional.of(maxScaleY);
            return this;
        }

        private void setDefaultShearsIfNotPresent() {
            if (!this.minShearX.isPresent())
                this.minShearX = Optional.of(DEFAULT_MIN_SHEAR);

            if (!this.maxShearX.isPresent())
                this.maxShearX = Optional.of(DEFAULT_MAX_SHEAR);

            if (!this.minShearY.isPresent())
                this.minShearY = Optional.of(DEFAULT_MIN_SHEAR);

            if (!this.maxShearY.isPresent())
                this.maxShearY = Optional.of(DEFAULT_MAX_SHEAR);


        }

        private void setDefaultScalesIfNotPresent() {
            if (!this.minScaleX.isPresent())
                this.minScaleX = Optional.of(DEFAULT_SCALE_X);

            if (!this.maxScaleX.isPresent())
                this.maxScaleX = Optional.of(DEFAULT_SCALE_X);

            if (!this.minScaleY.isPresent())
                this.minScaleY = Optional.of(DEFAULT_SCALE_Y);

            if (!this.maxScaleY.isPresent())
                this.maxScaleY = Optional.of(DEFAULT_SCALE_Y);

        }

        private void setDefaultRotationIfNotPresent() {
            if (!this.minRotation.isPresent())
                this.minRotation = Optional.of(DEFAULT_MIN_ROTATION);

            if (!this.maxRotation.isPresent())
                this.maxRotation = Optional.of(DEFAULT_MAX_ROTATION);
        }

        private void setDefaultsIfNotPresent() {
            setDefaultShearsIfNotPresent();
            setDefaultRotationIfNotPresent();
            setDefaultScalesIfNotPresent();
        }

        private void validateShears() {
            if (this.minShearX.get() > this.maxShearX.get())
                throw new IllegalArgumentException("Maximum shearX value cannot be less that minimum shearX value");

            if (this.minShearY.get() > this.maxShearY.get())
                throw new IllegalArgumentException("Maximum shearY value cannot be less that minimum shearY value");
        }

        private void validateScales() {
            if (this.minScaleX.get() > this.maxScaleX.get())
                throw new IllegalArgumentException("Maximum scaleX value cannot be less that minimum scaleX value");

            if (this.minScaleY.get() > this.maxScaleY.get())
                throw new IllegalArgumentException("Maximum scaleY value cannot be less that minimum scaleY value");
        }


        private void validateRotations() {
            if (this.minRotation.get() > this.maxRotation.get()) {
                throw new IllegalArgumentException("Maximum rotation cannot be less than minimum rotation");
            }
        }

        private void validateValues() {
            validateShears();
            validateRotations();
            validateScales();
        }

        public RandomTransformFactoryImpl build() {
            setDefaultsIfNotPresent();
            validateValues();
            return new RandomTransformFactoryImpl(this);
        }
    }

    private RandomTransformFactoryImpl(Builder builder) {
        this.minRotation = builder.minRotation;
        this.maxRotation = builder.maxRotation;

        this.minShearX = builder.minShearX;
        this.maxShearX = builder.maxShearX;
        this.minShearY = builder.minShearY;
        this.maxShearY = builder.maxShearY;

        this.minScaleX = builder.minScaleX;
        this.maxScaleX = builder.maxScaleX;
        this.minScaleY = builder.minScaleY;
        this.maxScaleY = builder.maxScaleY;


    }

    @Override
    public AffineTransform getRandomTransform() {
        int rotation;
        AffineTransform aft = new AffineTransform();

        double shearX;
        double shearY;
        double minShearX = this.minShearX.get();
        double maxShearX = this.maxShearX.get();
        double minShearY = this.minShearY.get();
        double maxShearY = this.maxShearY.get();

        shearX = (minShearX + rand.nextDouble() * (maxShearX - minShearX)) * (rand.nextBoolean() ? -1 : 1);
        shearY = (minShearY + rand.nextDouble() * (maxShearY - minShearY)) * (rand.nextBoolean() ? -1 : 1);

        double scaleX;
        double scaleY;
        double minScaleX = this.minScaleX.get();
        double maxScaleX = this.maxScaleX.get();
        double minScaleY = this.minScaleY.get();
        double maxScaleY = this.maxScaleY.get();

        scaleX = minScaleX + rand.nextDouble() * (maxScaleX - minScaleX);
        scaleY = minScaleY + rand.nextDouble() * (maxScaleY - minScaleY);

        // 1 is added to make sure that 0 is not passed to the random number generator
        rotation = (this.minRotation.get() + rand.nextInt(this.maxRotation.get() - this.minRotation.get() + 1)) *
                (rand.nextBoolean() ? -1 : 1);

//        TODO: review the impact of shear and revisit.
//        TODO: Shear is disabled for now!!
        aft.shear(shearX, shearY);
        aft.rotate(Math.toRadians(rotation));
        aft.scale(scaleX, scaleY);

        return aft;
    }
}
