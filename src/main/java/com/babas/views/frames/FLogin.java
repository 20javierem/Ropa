package com.babas.views.frames;

import com.babas.controllers.Users;
import com.babas.custom.CustomPasswordField;
import com.babas.custom.JPanelGradiente;
import com.babas.custom.CSwitchButton;
import com.babas.models.BoxSession;
import com.babas.models.User;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.formdev.flatlaf.extras.components.FlatTextField;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.util.Date;
import java.util.Locale;

public class FLogin extends JFrame {
    private JPanel contentPane;
    private JPanel panelDatos;
    private JButton btnInitSession;
    private CSwitchButton ckRememberUser;
    private FlatTextField fieldUser;
    private JLabel lblLogo;
    private CustomPasswordField fieldPasword;
    private JLabel lblError;
    private FPrincipal fPrincipal;

    public FLogin(FPrincipal fPrincipal) {
        this.fPrincipal = fPrincipal;
        $$$setupUI$$$();
        fieldUser.setEnabled(false);
        init();
        btnInitSession.addActionListener(e -> start());
    }

    public FLogin() {
        $$$setupUI$$$();
        init();
        btnInitSession.addActionListener(e -> start());
    }

    private void init() {
        setContentPane(contentPane);
        setTitle("Login");
        Utilities.setJFrame(this);
        getRootPane().setDefaultButton(btnInitSession);
        loadUserSaved();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        Babas.company.refresh();
        if (Utilities.iconCompanyx255x220 != null) {
            lblLogo.setIcon(Utilities.iconCompanyx255x220);
        } else if (Babas.company.getLogo() != null) {
            if (Utilities.openConection()) {
                Image logo = Utilities.getImage(Babas.company.getLogo());
                if (logo != null) {
                    Utilities.iconCompanyx255x220 = new ImageIcon(logo.getScaledInstance(255, 220, Image.SCALE_SMOOTH));
                    Utilities.iconCompanyx420x420 = new ImageIcon(logo.getScaledInstance(420, 420, Image.SCALE_SMOOTH));
                    lblLogo.setIcon(Utilities.iconCompanyx255x220);
                }
            }
        }
        lblError.setVisible(false);
    }

    private void loadUserSaved() {
        String userName = Utilities.propiedades.getUserName();
        String userPassword = Utilities.propiedades.getUserPassword();
        if (!userName.isBlank()) {
            fieldUser.setText(userName);
            fieldPasword.setText(userPassword);
            ckRememberUser.setSelected(true);
        }
    }

    private void start() {
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        String userName = fieldUser.getText().trim();
        String userPassword = new String(fieldPasword.getPassword());
        if (!userName.isBlank() && !userPassword.isBlank()) {
            User user = Users.getByUserName(userName);
            if (user != null) {
                if (user.getUserPassword().equals(userPassword)) {
                    if (user.isStaff()) {
                        if (user.isActive()) {
                            if (fPrincipal == null) {
                                user.setLastLogin(new Date());
                                user.save();
                                saveUser();
                                Babas.user = user;
                                Babas.boxSession = new BoxSession();
                                fPrincipal = new FPrincipal();
                            } else {
                                Utilities.setJFrame(fPrincipal);
                            }
                            fPrincipal.setVisible(true);
                            dispose();
                        } else {
                            lblError.setText("Usuario desactivado");
                            lblError.setVisible(true);
                        }
                    } else {
                        lblError.setText("Credenciales incorrectas");
                        lblError.setVisible(true);
                    }
                } else {
                    lblError.setText("Credenciales incorrectas");
                    lblError.setVisible(true);
                }
            } else {
                lblError.setText("Credenciales incorrectas");
                lblError.setVisible(true);
            }
        } else {
            lblError.setText("Ingrese Usuario y Contraseña");
            lblError.setVisible(true);
        }
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    private void saveUser() {
        if (ckRememberUser.isSelected()) {
            Utilities.propiedades.setUserName(fieldUser.getText().trim());
            Utilities.propiedades.setUserPassword(new String(fieldPasword.getPassword()));
        } else {
            Utilities.propiedades.setUserName("");
            Utilities.propiedades.setUserPassword("");
        }
        Utilities.propiedades.save();
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        contentPane = new JPanelGradiente(new Color(0xC6FFDD), new Color(0xFBD786), new Color(0xF7797D));
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        contentPane.setLayout(new GridLayoutManager(2, 1, new Insets(10, 10, 40, 10), -1, -1));
        panelDatos = new JPanel();
        panelDatos.setLayout(new GridLayoutManager(5, 1, new Insets(2, 2, 2, 2), 5, 5));
        panelDatos.setBackground(new Color(-12828863));
        panelDatos.setOpaque(false);
        contentPane.add(panelDatos, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnInitSession = new JButton();
        Font btnInitSessionFont = this.$$$getFont$$$(null, -1, 14, btnInitSession.getFont());
        if (btnInitSessionFont != null) btnInitSession.setFont(btnInitSessionFont);
        btnInitSession.setText("Ingresar");
        panelDatos.add(btnInitSession, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(-1, 32), null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), 0, -1));
        panel1.setOpaque(false);
        panelDatos.add(panel1, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 4, 0), -1, -1));
        panel2.setOpaque(false);
        panel1.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setForeground(new Color(-16777216));
        label1.setText("Recordar usuario");
        panel2.add(label1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ckRememberUser = new CSwitchButton();
        panel2.add(ckRememberUser, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        fieldUser = new FlatTextField();
        Font fieldUserFont = this.$$$getFont$$$(null, -1, 17, fieldUser.getFont());
        if (fieldUserFont != null) fieldUser.setFont(fieldUserFont);
        fieldUser.setPlaceholderText("Usuario");
        panelDatos.add(fieldUser, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(165, -1), null, 0, false));
        lblError = new JLabel();
        lblError.setForeground(new Color(-976373));
        lblError.setHorizontalTextPosition(11);
        lblError.setText("Credenciales incorrectas");
        panelDatos.add(lblError, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldPasword = new CustomPasswordField();
        panelDatos.add(fieldPasword, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(165, -1), null, 0, false));
        lblLogo = new JLabel();
        lblLogo.setHorizontalAlignment(0);
        lblLogo.setIcon(new ImageIcon(getClass().getResource("/com/babas/images/lojoJmoreno (1).png")));
        lblLogo.setText("");
        contentPane.add(lblLogo, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(255, 220), new Dimension(255, 220), new Dimension(255, 220), 0, false));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

}
