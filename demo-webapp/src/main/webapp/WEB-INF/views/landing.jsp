<html>
<head>
    <style>
        input[type=button] {
            -moz-box-shadow: 2px 3px 0px 0px #899599;
            -webkit-box-shadow: 2px 3px 0px 0px #899599;
            box-shadow: 2px 3px 0px 0px #899599;
            background:-webkit-gradient(linear, left top, left bottom, color-stop(0.05, #ededed), color-stop(1, #bab1ba));
            background:-moz-linear-gradient(top, #ededed 5%, #bab1ba 100%);
            background:-webkit-linear-gradient(top, #ededed 5%, #bab1ba 100%);
            background:-o-linear-gradient(top, #ededed 5%, #bab1ba 100%);
            background:-ms-linear-gradient(top, #ededed 5%, #bab1ba 100%);
            background:linear-gradient(to bottom, #ededed 5%, #bab1ba 100%);
            background-color:#ededed;
            -moz-border-radius:42px;
            -webkit-border-radius:42px;
            border-radius:42px;
            border:1px solid #d6bcd6;
            display:inline-block;
            cursor:pointer;
            color:#000000;
            font-family:Arial;
            font-size:14px;
            font-weight:bold;
            padding:2px 10px;
            text-decoration:none;
            text-shadow:0px 0px 0px #e1e2ed;
        }

        select {
            width: 100px;
        }

        td[class=configuration] {
            color: blue;
        }

        td[class=configurationbold] {
            color: blue;
            font-weight: bold;
        }

        td[class=success] {
            color: green;
        }

        td[class=successbold] {
            color: green;
            font-weight: bold;
        }

        td[class=failure] {
            color: red;
        }

        td[class=failurebold] {
            color: red;
            font-weight: bold;
        }

        /* https://www.w3schools.com/css/css_tooltip.asp */
        .tooltip {
            position: relative;
            display: inline-block;
            border-bottom: 1px dotted black;
        }

        .tooltip .tooltiptext {
            visibility: hidden;
            width: 250px;
            background-color: darkgray;
            text-align: left;
            padding: 5px 5px;
            border-radius: 10px;
            position: absolute;
            right: -250px;
            z-index: 1;
        }

        .tooltip:hover .tooltiptext {
            visibility: visible;
        }

    </style>
</head>
<body>
<p>
    <b>To solve a CAPTCHA</b>
<ul>
    <li>Click on all the black characters similar to the blue ones and hit submit</li>
    <li>To clear your selection, click on 'Clear Clicks' (before you submit)</li>
    <li>You can submit solution for a CAPTCHA only once</li>
</ul>
</p>
<form id="pixelcaptcha_form">
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
                            <input type="button" name="Reset" value="Clear Clicks" id="reset_solution_button"/>
                        </td>
                    </tr>
                    <tr>
                        <td>&nbsp;</td>
                    </tr>
                    <tr>
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
                            <b> CAPTCHA Configuration Options</b>
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
                            <span class="tooltiptext">Number of resposne characters</span>
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
                    <tr>
                        <td align="center" colspan="2">
                            <input type="button" value="Set Configuration" id="set_captcha_config"/>
                        </td>
                    </tr>
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
    </table>

</form>
<script src="demo_app.js"></script>
</body>
</html>
