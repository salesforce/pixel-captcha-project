<html>
<body>
<p>
    <b>To solve the CAPTCHA</b>
<ul>
    <li>Click on the black characters similar to the blue ones and hit submit</li>
    <li>To clear your selection, click on reset (before you submit)</li>
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
                            <input type="button" name="Reset" value="Reset Clicks" id="reset_solution_button"/>
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
                <table frame="box" width="300">
                    <tr>
                        <td>
                            Challenge Count
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
                        <td>
                            Response Count
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
                        <td>
                            Orientation
                        </td>
                        <td>
                            <select id="orientation">
                                <option value="horizontal">horizontal</option>
                                <option value="vertical">vertical</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            Unicode Code Points
                        </td>
                        <td>
                            <select id="codePoints">
                                <option value="0-255">0-255</option>
                                <option value="0-4095">0-4095</option>
                                <option value="0-65535">0-65535</option>
                            </select>

                        </td>
                    </tr>
                    <%--<tr><td>&nbsp;</td></tr>--%>
                    <tr>
                        <td>
                            Ordered Clicks
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
                            <input type="button" value="Configure" id="set_captcha_config"/>
                        </td>
                    </tr>
                </table>
                <table>
                    <tr>
                        <td>&nbsp;</td>
                    </tr>
                </table>

                <table frame="box" width="300">
                    <tr>
                        <td id="info_label"> Captcha Config</td>
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
