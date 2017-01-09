# Pixel CAPTCHA Project
A Unicode based visual CAPTCHA scheme that leverages the 64K Unicode code points from the Basic Multilingual Plane (plane 0) to construct the CAPTCHAs that can be solved with 2 to 4 mouse clicks

Clone the repo, cd to the code directory and proceed to the following.

**To Run the CAPTCHA demo webapp**
* mvn clean install
* mvn -pl demo-webapp jetty:run

On your browser: Go to http://localhost:8080

**To solve a CAPTCHA**
* For each character in blue color (top to bottom, or left to right), find the corresponding black character
* Click on the black character
* Submit the solution to the server
* You can also reset the solution by clicking on 'Reset Clicks' button
* You can attempt solving CAPTCHA only once

**Validating a CAPTCHA**
* Once you are done clicking on the all the black characters corresponding to the blue ones, submit the solution using the 'Submit Solution' button
* The validation result will be displayed in the bottom right box

**To configure the CAPTCHA Generator**
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

**Project Structure**
* The demo-webapp project provides an example implementation on how to consume the CAPTCHA library
* CAPTCHA Code is in the pixel-captcha directory.
