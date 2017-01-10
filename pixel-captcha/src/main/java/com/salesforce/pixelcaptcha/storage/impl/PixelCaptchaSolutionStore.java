/*
 * Copyright (c) 2017, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license.
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */

package com.salesforce.pixelcaptcha.storage.impl;

import java.util.concurrent.TimeUnit;

import com.salesforce.pixelcaptcha.dataobj.CaptchaSolution;
import com.google.common.base.Optional;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.salesforce.pixelcaptcha.storage.CaptchaSolutionStore;
import com.salesforce.pixelcaptcha.storage.IdentifierFactory;

public class PixelCaptchaSolutionStore implements CaptchaSolutionStore {

    LoadingCache<String, Optional<CaptchaSolution>> solutionStore;

    public static final int DEFAULT_SIZE = 1000000;
    public static final int DEFAULT_TIMEOUT = 30 * 60;

    public static final int MIN_TIMEOUT = 1;
    public static final int MAX_TIMEOUT = 60 * 60;

    public static final int MIN_SIZE = 1;
    private static PixelCaptchaSolutionStore INSTANCE = null;

    public PixelCaptchaSolutionStore(int size, int timeoutInSecs) {

        if (size <= 0)
            throw new IllegalArgumentException("The cache size cannot be less than " + MIN_SIZE);
        if (timeoutInSecs < MIN_TIMEOUT || timeoutInSecs > MAX_TIMEOUT)
            throw new IllegalArgumentException("The cache entry timeout cannot be less than " + MIN_TIMEOUT + " or greater than " + MAX_TIMEOUT);

        solutionStore = CacheBuilder.newBuilder()
                .maximumSize(size)
                .expireAfterAccess(timeoutInSecs, TimeUnit.SECONDS)
                .build(new CacheLoader<String, Optional<CaptchaSolution>>() {
                    @Override
                    public Optional<CaptchaSolution> load(String key) {
                        // Since there is no local store load data from, the function always returns Optional.absent()
                        // Returning null will cause an exception as per CacheLoader design
                        return Optional.absent();
                    }
                });
    }

    public static PixelCaptchaSolutionStore getInstance() {
        return PixelCaptchaSolutionStore.getInstance(DEFAULT_SIZE, DEFAULT_TIMEOUT);
    }


    /**
     * Only one INSTANCE of the CAPTCHA store.
     *
     * @param size
     * @param timeoutInSecs
     * @return
     */
    public static synchronized PixelCaptchaSolutionStore getInstance(int size, int timeoutInSecs) {
        if (INSTANCE == null) {
            INSTANCE = new PixelCaptchaSolutionStore(size, timeoutInSecs);
        }
        return INSTANCE;
    }


    @Override
    public String storeCaptchaSolution(CaptchaSolution solution) {
        if (solution == null)
            throw new NullPointerException("Either CAPTCHA identifier or solution is null");

        String identifier = IdentifierFactory.getInstance();
        solutionStore.put(identifier, Optional.of(solution));

        return identifier;
    }

    @Override
    public Optional<CaptchaSolution> getCaptchaSolution(String identifier) {
        if (identifier == null)
            throw new NullPointerException("CAPTCHA identifier cannot be null");
        // The CacheLoader used above does not throw any checked exception
        Optional<CaptchaSolution> o = solutionStore.getUnchecked(identifier);

        // The invalidate is performed here for two reasons:
        // 1. When a non existent identifier is queried, the cache is populated with Optional.absent() which needs to be removed.
        // 2. The entries in PixelCaptchaStore are for one time use and are removed on first access.
        // The invalidate call below achieves both the ends and is part of cleanup process.
        solutionStore.invalidate(identifier);

        //Return the Optional that can be absent() or can have a value
        return o;
    }

    @Override
    public long getSize() {
        return solutionStore.size();
    }

}
