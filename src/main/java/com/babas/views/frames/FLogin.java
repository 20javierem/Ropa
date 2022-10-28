package com.babas.views.frames;

import com.babas.App;
import com.babas.controllers.Users;
import com.babas.custom.CustomPasswordField;
import com.babas.custom.JPanelGradiente;
import com.babas.models.BoxSession;
import com.babas.models.User;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.formdev.flatlaf.extras.components.FlatTextField;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

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
    private JCheckBox ckRememberUser;
    private FlatTextField fieldUser;
    private JLabel lblLogo;
    private CustomPasswordField fieldPasword;
    private JLabel lblError;
    private JButton btnShowPasword;
    private FPrincipal fPrincipal;

    public FLogin(FPrincipal fPrincipal) {
        this.fPrincipal = fPrincipal;
        $$$setupUI$$$();
        fieldUser.setEnabled(false);
        initComponents();
        btnInitSession.addActionListener(e -> start());
    }

    public FLogin() {
        $$$setupUI$$$();
        initComponents();
        btnInitSession.addActionListener(e -> start());
    }

    private void initComponents() {
        setContentPane(contentPane);
        setTitle("Login");
        Utilities.setJFrame(this);
        getRootPane().setDefaultButton(btnInitSession);
        loadUserSaved();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        if (!Babas.company.getLogo().isBlank()) {
            Image logo = Utilities.getImage(Babas.company.getLogo());
            if (logo != null) {
                Utilities.iconCompany = new ImageIcon(logo.getScaledInstance(215, 215, Image.SCALE_SMOOTH));
                lblLogo.setIcon(Utilities.iconCompany);
            }
        }
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
                if (user.getUserName().equals(userName) && user.getUserPassword().equals(userPassword)) {
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
        panelDatos.setLayout(new GridLayoutManager(4, 1, new Insets(2, 2, 2, 2), 5, 5));
        panelDatos.setBackground(new Color(-12828863));
        panelDatos.setOpaque(false);
        contentPane.add(panelDatos, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnInitSession = new JButton();
        Font btnInitSessionFont = this.$$$getFont$$$(null, -1, 14, btnInitSession.getFont());
        if (btnInitSessionFont != null) btnInitSession.setFont(btnInitSessionFont);
        btnInitSession.setText("Ingresar");
        panelDatos.add(btnInitSession, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(-1, 32), null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.setOpaque(false);
        panelDatos.add(panel1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(-1, 50), null, 0, false));
        lblError = new JLabel();
        Font lblErrorFont = this.$$$getFont$$$(null, -1, 12, lblError.getFont());
        if (lblErrorFont != null) lblError.setFont(lblErrorFont);
        lblError.setForeground(new Color(-976373));
        lblError.setText("Credenciales incorrectas");
        lblError.setVisible(false);
        panel1.add(lblError, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ckRememberUser = new JCheckBox();
        ckRememberUser.setHorizontalAlignment(2);
        ckRememberUser.setHorizontalTextPosition(4);
        ckRememberUser.setIconTextGap(5);
        ckRememberUser.setInheritsPopupMenu(false);
        ckRememberUser.setMargin(new Insets(2, 2, 2, 2));
        ckRememberUser.setOpaque(false);
        ckRememberUser.setText("Recordar usuario");
        ckRememberUser.setVerticalAlignment(0);
        panel1.add(ckRememberUser, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldUser = new FlatTextField();
        Font fieldUserFont = this.$$$getFont$$$(null, -1, 17, fieldUser.getFont());
        if (fieldUserFont != null) fieldUser.setFont(fieldUserFont);
        fieldUser.setPlaceholderText("Usuario");
        panelDatos.add(fieldUser, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(165, -1), null, 0, false));
        fieldPasword = new CustomPasswordField();
        Font fieldPaswordFont = this.$$$getFont$$$(null, -1, 17, fieldPasword.getFont());
        if (fieldPaswordFont != null) fieldPasword.setFont(fieldPaswordFont);
        fieldPasword.setPlaceholderText(" Contraseña");
        panelDatos.add(fieldPasword, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(165, -1), null, 0, false));
        lblLogo = new JLabel();
        lblLogo.setHorizontalAlignment(0);
        lblLogo.setIcon(new ImageIcon(getClass().getResource("/com/babas/images/lojoJmoreno (1).png")));
        lblLogo.setText("");
        lblLogo.setVerticalAlignment(0);
        lblLogo.setVerticalTextPosition(0);
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
