/*
 * Copyright (c) 2017, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license.
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */

package com.salesforce.pixelcaptcha.demo;

/**
 * @author Gursev Singh Kalra @ Salesforce.com
 */
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class PixelCaptchaDemoAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    public PixelCaptchaDemoAppInitializer() {
        // TODO Auto-generated constructor stub
    }

    //	@Configuration class’s returned getRootConfigClasses() will be used to configure the application
    // context created by ContextLoaderListener.
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class <?>[] {RootConfig.class};
    }

    // The @Configuration classes returned from getServletConfigClasses() will define beans
    // for DispatcherServlet’s application context.
    // loads application context with beans defined in WebConfig configuration class
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[] {WebConfig.class};
    }

    // Returns the paths to which the DispatcherServlet will map to
    // This will be applications default servlet and will handle all requests coming into the application

    @Override
    protected String[] getServletMappings() {
        return new String[] {"/"};
    }

}

