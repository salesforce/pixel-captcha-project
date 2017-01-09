/*
 * Copyright (c) 2017, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license.
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */
package com.salesforce.pixelcaptcha.gui;

import com.salesforce.pixelcaptcha.interfaces.Captcha;
import com.salesforce.pixelcaptcha.interfaces.ValidationResult;
import com.salesforce.pixelcaptcha.core.PixelCaptchaController;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author Gursev Singh Kalra @ Salesforce.com
 */
public class ConsumerJFrame extends javax.swing.JFrame {

    /**
     * Creates new form ConsumerJFrame
     */
    public ConsumerJFrame() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {
        this.setTitle("PixelCAPTCHA PoC");

        settingsPanel = new javax.swing.JPanel();
        sizeSelectionComboBox = new javax.swing.JComboBox();
        sizeLabel = new javax.swing.JLabel();
        challengeResponseCountComboBox = new javax.swing.JComboBox();
        challengeResponseCountJLabel = new javax.swing.JLabel();
        characterRangeLabel = new javax.swing.JLabel();
        codePointValues = new javax.swing.JTextField();
        configureCaptchaButton = new javax.swing.JButton();
        captchaAndButtonsPanel = new javax.swing.JPanel();
        captchaDrawingLabel = new javax.swing.JLabel();
        newCaptchaButton = new javax.swing.JButton();
        verifySolutionButton = new javax.swing.JButton();
        orderedClicks = new java.awt.Checkbox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        settingsPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        sizeSelectionComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"300 x 400", "400 x 300" }));

        sizeLabel.setText("Size");

        challengeResponseCountComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"2 x 10", "2 x 11", "2 x 12", "3 x 10", "3 x 11", "3 x 12", "4 x 10", "4 x 11", "4 x 12"}));

        challengeResponseCountJLabel.setText("Counts");

        characterRangeLabel.setText("Code Point Range(s)/Value(s)");

        codePointValues.setText("0-255");

        configureCaptchaButton.setText("Configure");
        configureCaptchaButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                configureCaptchaProvider(evt);
            }
        });

        orderedClicks.setLabel("Ordered");

        javax.swing.GroupLayout settingsPanelLayout = new javax.swing.GroupLayout(settingsPanel);
        settingsPanel.setLayout(settingsPanelLayout);
        settingsPanelLayout.setHorizontalGroup(
                settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(settingsPanelLayout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addGroup(settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(settingsPanelLayout.createSequentialGroup()
                                                .addGroup(settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(codePointValues, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGroup(settingsPanelLayout.createSequentialGroup()
                                                                .addGroup(settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(sizeLabel)
                                                                        .addComponent(challengeResponseCountJLabel))
                                                                .addGap(12, 12, 12)
                                                                .addGroup(settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(sizeSelectionComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(challengeResponseCountComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                                .addContainerGap(51, Short.MAX_VALUE))
                                        .addGroup(settingsPanelLayout.createSequentialGroup()
                                                .addGroup(settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(orderedClicks, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(configureCaptchaButton)
                                                        .addComponent(characterRangeLabel))
                                                .addGap(0, 0, Short.MAX_VALUE))))
        );
        settingsPanelLayout.setVerticalGroup(
                settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(settingsPanelLayout.createSequentialGroup()
                                .addGap(38, 38, 38)
                                .addGroup(settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(sizeSelectionComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(sizeLabel))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(challengeResponseCountComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(challengeResponseCountJLabel))
                                .addGap(30, 30, 30)
                                .addComponent(characterRangeLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(codePointValues, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(26, 26, 26)
                                .addComponent(orderedClicks, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(28, 28, 28)
                                .addComponent(configureCaptchaButton)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        orderedClicks.getAccessibleContext().setAccessibleName("");
        captchaAndButtonsPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        captchaDrawingLabel.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 0), null));
        captchaDrawingLabel.setPreferredSize(new java.awt.Dimension(400, 400));
        captchaDrawingLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CaptureMouseClickCoordinates(evt);
            }
        });

        javax.swing.GroupLayout captchaPanelLayout = new javax.swing.GroupLayout(captchaDrawingLabel);
        captchaDrawingLabel.setLayout(captchaPanelLayout);
        captchaPanelLayout.setHorizontalGroup(
                captchaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 398, Short.MAX_VALUE)
        );
        captchaPanelLayout.setVerticalGroup(
                captchaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 398, Short.MAX_VALUE)
        );

        newCaptchaButton.setText("New CAPTCHA");
        newCaptchaButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                drawNewCAPTCHA(evt);
            }
        });

        verifySolutionButton.setText("Verify Solution");
        verifySolutionButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                verifyCaptchaSolution(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(captchaAndButtonsPanel);
        captchaAndButtonsPanel.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(23, 23, 23)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(captchaDrawingLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 402, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(42, 42, 42)
                                                .addComponent(newCaptchaButton)
                                                .addGap(49, 49, 49)
                                                .addComponent(verifySolutionButton)))
                                .addContainerGap(59, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(captchaDrawingLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 402, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(verifySolutionButton)
                                        .addComponent(newCaptchaButton))
                                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addComponent(captchaAndButtonsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(40, 40, 40)
                                .addComponent(settingsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(43, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(settingsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(captchaAndButtonsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );

        pack();

        configureCaptchaProvider(null);
        drawNewCAPTCHA(null);
    }// </editor-fold>

    private void CaptureMouseClickCoordinates(java.awt.event.MouseEvent evt) {
        int x = evt.getPoint().x;
        int y = evt.getPoint().y;
        Graphics g2 = captchaDrawingLabel.getGraphics();
        g2.setColor(Color.red);
        g2.setFont(new Font(Font.SERIF, Font.BOLD, 75));
        g2.drawString(".", x, y);

        if(captchaWidth.contains("400")) {
            clickPoints.add(new Point(x, y - 50));
        } else {
            clickPoints.add((new Point(x, y)));
        }

        System.out.println(evt.getPoint());
    }

    private void drawNewCAPTCHA(java.awt.event.MouseEvent evt) {
        currentCaptcha = pixelCaptchaController.getCaptcha();
        BufferedImage bi = currentCaptcha.getImage();
        ImageIcon imageIcon = new ImageIcon(bi);
        captchaDrawingLabel.setIcon(imageIcon);
        clickPoints = new ArrayList<>();
    }

    private void verifyCaptchaSolution(java.awt.event.MouseEvent evt) {
        ValidationResult vr = pixelCaptchaController.verifyCaptcha(currentCaptcha.getIdentifier(), clickPoints);
        System.out.println(vr);
        JOptionPane.showMessageDialog(null, vr);
    }

    private String getCaptchaWidth() {
        return captchaWidth;
    }

    private void initProperties() {
        System.out.println("\"" + challengeResponseCountComboBox.getSelectedItem() + "\"");
        System.out.println("\"" + sizeSelectionComboBox.getSelectedItem() + "\"");
        System.out.println("\"" + codePointValues.getText() + "\"");
        System.out.println("\"" + Boolean.toString(orderedClicks.getState()) + "\"");

        String[] crCount = challengeResponseCountComboBox.getSelectedItem().toString().split("x");
        this.challengeCount = crCount[0].trim();
        this.responseCount = crCount[1].trim();

        String[] dimensions = sizeSelectionComboBox.getSelectedItem().toString().split("x");
        this.captchaWidth =  dimensions[0].trim();
        this.captchaHeight =  dimensions[1].trim();

        String codePoints = codePointValues.getText();
        this.codePoints = codePoints;

    }

    private void configureCaptchaProvider(java.awt.event.MouseEvent evt) {
        initProperties();
        Properties p = new Properties();
        p.setProperty("captchaWidth", this.captchaWidth);
        p.setProperty("captchaHeight", this.captchaHeight);
        p.setProperty("responseCount", this.responseCount);
        p.setProperty("challengeCount", this.challengeCount);
        p.setProperty("codePoints", this.codePoints);
        p.setProperty("ordered", Boolean.toString(this.orderedClicks.getState()));

        pixelCaptchaController = new PixelCaptchaController();
        pixelCaptchaController.initProperties(p);

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ConsumerJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ConsumerJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ConsumerJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ConsumerJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ConsumerJFrame().setVisible(true);
            }
        });
    }

    // Variables declaration
    private javax.swing.JLabel captchaDrawingLabel;
    private javax.swing.JComboBox challengeResponseCountComboBox;
    private javax.swing.JLabel challengeResponseCountJLabel;
    private javax.swing.JLabel characterRangeLabel;
    private javax.swing.JTextField codePointValues;
    private javax.swing.JButton configureCaptchaButton;
    private javax.swing.JComboBox sizeSelectionComboBox;
    private javax.swing.JPanel captchaAndButtonsPanel;
    private javax.swing.JButton newCaptchaButton;
    private javax.swing.JPanel settingsPanel;
    private javax.swing.JLabel sizeLabel;
    private javax.swing.JButton verifySolutionButton;
    private PixelCaptchaController pixelCaptchaController;
    private String captchaWidth;
    private String captchaHeight;
    private String responseCount;
    private String challengeCount;
    private String codePoints;
    private java.awt.Checkbox orderedClicks;

    private Captcha currentCaptcha;
    private List<Point> clickPoints;

    // End of variables declaration                   
}
