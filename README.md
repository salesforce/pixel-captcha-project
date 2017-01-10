# Pixel CAPTCHA Project
A Unicode based visual CAPTCHA scheme that leverages the 64K Unicode code points from the Basic Multilingual Plane (plane 0) to construct the CAPTCHAs that can be solved with 2 to 4 mouse clicks.

It is written in Java and tested to work on JDK 8.0. You will also need Maven build system. This project has two demo applications; a sample web application the the second is a Swing based UI. You can play with either and provide your feedback.

## Sample CAPTCHAS
Let's see a couple of sample CAPTCHA's.

### Sample Horizontal CAPTCHA
<br/>
![Alt text](https://github.com/gursev/misc/blob/master/sample-pixel-captcha/horizontal_captcha.png)

### Sample Vertical CAPTCHA
<br/>
![Alt text](https://github.com/gursev/misc/blob/master/sample-pixel-captcha/vertical_captcha.png)

## Getting Your Feet Wet
### Demo Web Application
Clone the repo, cd to the code directory, compile and run... Profit!!
* git clone git@github.com:salesforce/pixel-captcha-project.git
* cd pixel-captcha-project
* mvn clean install
* mvn -pl demo-webapp jetty:run

On your browser: Go to http://localhost:8080/

### Demo Swing Application
com.salesforce.pixelcaptcha.gui.ConsumerJFrame inside pixel-captcha project is a GUI application that you can play with.

## Solving a CAPTCHA
* For each character in blue color (top to bottom, or left to right), find the corresponding black character
* Click on the black character
* Submit the solution to the server
* You can also reset the solution by clicking on 'Reset Clicks' button
* You can attempt solving CAPTCHA only once

## Validating your Solution
* Once you are done clicking on the all the black characters corresponding to the blue ones, submit the solution using the 'Submit Solution' button
* The validation result will be displayed in the bottom right box

## Configuring your CAPTCHA Generator
* Pick a challenge count. This is the number of blue characters
* Pick a response count. This is the number of black characters
* Pick the orientation (you can choose horizontal for desktops, vertical for tablets, mobiles etc...)
* Pick the unicode code point range you would like to use (Try it.. it gets interesting)
* If the 'Ordered Clicks' is set to true, the CAPTCHA solution will need to honor the order of blue characters. 
  * For example
    * The first black character you click on should be the first blue character (topmost or leftmost based on the CAPTCHA orientation)
    * The second black character you click on should be the second blue character (second from top or from left based on the CAPTCHA orientation) etc... 
  * Basically, you will need to click on black characters as per the order of the blue characters.
* If the 'Ordered Clicks' is set to false, the CAPTCHA solution does not need to honor the order of blue characters. Relatively insecure! But you have an option.
  * You can click on black characters in any order you like

## Project Structure
* The demo-webapp project provides an example implementation on how to consume the CAPTCHA library
* CAPTCHA Code is in the pixel-captcha directory.

##Dependencies
The following are the current dependencies for the project. Some of them were used to reduce the development effort. The dependencies changed be changed or removed based on the community feedback.

### pixel-captcha: The main library that you will consume
* com.google.guava:guava:jar:18.0
* com.google.code.gson:gson:jar:2.8.0

### demo-webapp: The demo web application
* org.springframework:spring-core:jar:4.2.1.RELEASE
* org.springframework:spring-context:jar:4.2.1.RELEASE
* org.springframework:spring-web:jar:4.2.1.RELEASE
* org.springframework:spring-webmvc:jar:4.2.1.RELEASE
* org.springframework:spring-aop:jar:4.2.1.RELEASE
* jstl:jstl:jar:1.2
* com.salesforce.pixelcaptcha:pixel-captcha:jar:1.0-BETA
* com.google.code.gson:gson:jar:2.8.0
* javax.servlet:javax.servlet-api:jar:4.0.0-b01

## Whitepaper

Last but not the least. To learn more about the CAPTCHA scheme, consider reading the whitepaper hosted here https://github.com/gursev/whitepapers/blob/master/PixelCAPTCHA_Whitepaper.pdf

