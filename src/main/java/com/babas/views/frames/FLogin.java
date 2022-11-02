package com.babas.views.frames;

import com.babas.App;
import com.babas.controllers.Users;
import com.babas.custom.CustomPasswordField;
import com.babas.custom.JPanelGradiente;
import com.babas.custom.ToggleButton;
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
    private ToggleButton ckRememberUser;
    private FlatTextField fieldUser;
    private JLabel lblLogo;
    private CustomPasswordField fieldPasword;
    private JLabel lblError;
    private FPrincipal fPrincipal;

    public FLogin(FPrincipal fPrincipal) {
        this.fPrincipal = fPrincipal;
        fieldUser.setEnabled(false);
        init();
        btnInitSession.addActionListener(e -> start());
    }

    public FLogin() {
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
        } else if (!Babas.company.getLogo().isBlank()) {
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

}
