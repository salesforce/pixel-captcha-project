<html>
<head>
    <link rel="stylesheet" type="text/css" href="demo_app_css.css">
</head>
<body>
<p>
    <h3 id="howtosolve"></h3>
</p>
<form id="pixelcaptcha_form" class="hide">
    <br/>
    <table>
        <tr>
            <td>
                <table border="0">
                    <input type="hidden" name="pixelcaptcha_id" id="pixelcaptcha_id"/>
                    <tr>
                        <td colspan="3">
                            <canvas id="pixelcaptcha_canvas" style="border:1px solid #000000;"></canvas>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            &nbsp;
                        </td>
                    </tr>
                    <tr>
                        <td align="center">
                            <input type="button" name="New" value="New Captcha" id="new_captcha_button"/>
                        </td>
                        <td align="center">
                            <input type="button" name="Submit" value="Submit Solution"
                                   id="pixelcaptcha_submit_button"></input>
                        </td>
                        <td align="center">
                            <input type="button" name="Reset" value="Clear Selection" id="reset_solution_button"/>
                        </td>
                    </tr>
                    <tr>
                        <td>&nbsp;</td>
                    </tr>
                </table>
            </td>
            <td>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            </td>
            <td>
                <table frame="box" width="400">
                    <tr>
                        <td align="left" colspan="2">
                            <b> Set your CAPTCHA configuration</b>
                        </td>
                    </tr>

                    <tr>
                        <td class="tooltip">
                            Challenge Count
                            <span class="tooltiptext">Number of challenge characters</span>
                        </td>
                        <td>
                            <select id="challengeCount">
                                <option value="2">2</option>
                                <option value="3">3</option>
                                <option value="4">4</option>
                            </select>

                        </td>
                    </tr>
                    <tr>
                        <td class="tooltip">
                            Response Count
                            <span class="tooltiptext">Number of response characters</span>
                        </td>
                        <td>
                            <select id="responseCount">
                                <option value="10">10</option>
                                <option value="11">11</option>
                                <option value="12">12</option>
                            </select>

                        </td>
                    </tr>
                    <tr>
                        <td class="tooltip">
                            CAPTCHA Orientation
                            <span class="tooltiptext">
                                Vertical or Horizontal CAPTCHA
                            </span>
                        </td>
                        <td>
                            <select id="orientation">
                                <option value="horizontal">horizontal</option>
                                <option value="vertical">vertical</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td class="tooltip">
                            Unicode Code Point Range
                            <span class="tooltiptext">
                                Unicode code point range (from plane 0) to pick challenge and response character values from.
                            </span>
                        </td>
                        <td >
                            <select id="codePoints">
                                <option value="0-255">0-255</option>
                                <option value="0-4095">0-4095</option>
                                <option value="0-65535">0-65535</option>
                            </select>

                        </td>
                    </tr>
                    <%--<tr><td>&nbsp;</td></tr>--%>
                    <tr>
                        <td class="tooltip" >Ordered Clicks
                            <span class="tooltiptext">When true, the CAPTCHA solution should follow the order in which the challenge characters appears.
                                For horizontal CAPTCHA, find the topmost challenge character among the response characters and click on it, and so on.
                                For vertical CAPTCHA, find the leftmost challenge character among the response characters and click on it, and so on.
                                </span>
                        </td>
                        <td>
                            <select id="ordered">
                                <option value="false">false</option>
                                <option value="true">true</option>
                            </select>

                        </td>
                    </tr>
                    <tr>
                        <td>
                            &nbsp;
                        </td>
                    </tr>
                    <%--<tr>--%>
                        <%--<td align="center" colspan="2">--%>
                            <%--<input type="button" value="Set Configuration" id="set_captcha_config"/>--%>
                        <%--</td>--%>
                    <%--</tr>--%>
                </table>
                <table>
                    <tr>
                        <td>&nbsp;</td>
                    </tr>
                </table>

                <table frame="box" width="400">
                    <tr>
                        <td id="info_label">
                            <%--<b>Captcha Config</b>--%>
                        </td>
                    </tr>
                    <tr>
                        <td id="info_content"></td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td colspan="5">
                <br/>
                <br/>
                <b>What else?</b>
                <ul>
                    <li>'Clear Selection' lets you clear and reattempt before submitting</li>
                    <li>It's a fun experiment. Interesting CAPTCHA configuration options on the top right</li>
                    <li>Hover over the dotted text for more info. Do try out the different configurations </li>
                    <li>It's open source. Get code from <a href="https://github.com/salesforce/pixel-captcha-project"> here</a></li>
                    <li>Got some time? Read the <a href="https://github.com/gursev/whitepapers/blob/master/PixelCAPTCHA_Whitepaper.pdf">whitepaper</a> </li>
                    <li>Want to do quick reading? I'll soon blog about it</li>
                </ul>
            </td>
        </tr>

    </table>

</form>

<script src="demo_app.js"></script>
</body>
</html>
