/*
 * Copyright (c) 2017, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license.
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */

package com.salesforce.pixelcaptcha.demo;

import com.google.gson.Gson;
import com.salesforce.pixelcaptcha.interfaces.Captcha;
import com.salesforce.pixelcaptcha.interfaces.ValidationResult;
import com.salesforce.pixelcaptcha.core.PixelCaptchaProvider;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author Gursev Singh Kalra @ Salesforce.com
 */

@Controller
@Scope(value = "session")
public class LandingController {
    private final String ERROR = "{\"status\":\"error\", \"message\": \"An error occurred. Please check the configuration parameters\"}";
    private PixelCaptchaProvider pcp;
    private Properties p;

    /* Default Configuration on application startup */
    @PostConstruct
    public void init() {
        p = new Properties();
        p.setProperty("captchaWidth", "400");
        p.setProperty("captchaHeight", "300");
        p.setProperty("responseCount", "10");
        p.setProperty("challengeCount", "2");
        p.setProperty("codePoints", "0-255");
        pcp = new PixelCaptchaProvider(p);
    }

    private String convertBufferedImageToPngBase64(BufferedImage bi) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bi, "png", baos);
        } catch (IOException e) {
            System.out.println(e);
        }
        String encoded = Base64.getEncoder().encodeToString(baos.toByteArray());
        return encoded;
    }

    private String buildCaptchaJSON(Captcha c) {
        Map<String, String> m = new HashMap<>();
        m.put("id", c.getIdentifier());
        m.put("width", Integer.toString(c.getImage().getWidth()));
        m.put("height", Integer.toString(c.getImage().getHeight()));
        m.put("image", "data:image/png;base64," + convertBufferedImageToPngBase64(c.getImage()));
        Gson gson = new Gson();
        return gson.toJson(m);
    }

    @RequestMapping(method = RequestMethod.GET, value = {"/", "/landing"})
    public String landing() {
        return "landing";
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = {"/getCaptcha"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getCaptcha() {
        Captcha c = pcp.getCaptcha();
        return buildCaptchaJSON(c);
    }

    @ResponseBody
    @RequestMapping(
            method = RequestMethod.GET,
            value = {"/getConfig"},
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public String getConfig() {
        Map<String, Object> m = new HashMap<>();
        Map<String, String> pMap = new HashMap<>();
        for (final String name : p.stringPropertyNames())
            pMap.put(name, p.getProperty(name));
        m.put("configuration", pMap);
        Gson gson = new Gson();
        return gson.toJson(m).toString();
    }


    /*
     *
     * Example JSON {"orientation": "horizontal", "codePoints": "0-65535", "challengeCount": "2", "responseCount": "12", "ordered": "false" }
     * @param config
     * @return
     */
    @ResponseBody
    @RequestMapping(
            method = RequestMethod.POST,
            value = {"/setConfig"},
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public String setConfig(@RequestBody CaptchaConfig config) {

        switch (config.getCodePoints()) {
            case "0-255":
            case "0-4095":
            case "0-65535":
                break;
            default:
                return ERROR;
        }

        if (!(config.getChallengeCount().equals("2") ||
                config.getChallengeCount().equals("3") ||
                config.getChallengeCount().equals("4"))) {
            return ERROR;
        }

        if (!(config.getResponseCount().equals("10") ||
                config.getResponseCount().equals("11") ||
                config.getResponseCount().equals("12"))) {
            return ERROR;
        }


        if (!(config.getOrientation().equals("horizontal") || config.getOrientation().equals("vertical"))) {
            return ERROR;
        }

        if (!(config.getOrdered().equals("true") || config.getOrdered().equals("false"))) {
            return ERROR;
        }

        p = new Properties();
        if (config.getOrientation().equals("horizontal")) {
            p.setProperty("captchaWidth", "400");
            p.setProperty("captchaHeight", "300");
        } else {
            p.setProperty("captchaWidth", "300");
            p.setProperty("captchaHeight", "400");
        }

        p.setProperty("codePoints", config.getCodePoints());
        p.setProperty("challengeCount", config.getChallengeCount());
        p.setProperty("order", config.getOrdered());
        p.setProperty("responseCount", config.getResponseCount());
        pcp.init(p);

        Map<String, Object> m = new HashMap<>();
        Map<String, String> pMap = new HashMap<>();
        for (final String name : p.stringPropertyNames())
            pMap.put(name, p.getProperty(name));
        m.put("status", "success");
        m.put("configuration", pMap);
        Gson gson = new Gson();
        return gson.toJson(m).toString();
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST,
            value = {"/verifySolution"},
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public String verifySolution(@RequestBody ClientSolutionDO clientSolutionDO) {
        Gson gson = new Gson();
        String solutionAsString = gson.toJson(clientSolutionDO.getSolutionCoordinates()).toString();
        ValidationResult vr = pcp.verify(clientSolutionDO.getPixelcaptchaId(), solutionAsString);
        Map<String, Object> m = new HashMap<>();
        if (vr.isPositive()) {
            m.put("status", "success");
        } else {
            m.put("status", "failure");
        }
        m.put("details", vr);
        return new Gson().toJson(m);
    }
}
