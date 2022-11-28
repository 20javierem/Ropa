package com.babas.views.frames;

import com.babas.App;
import com.babas.controllers.Branchs;
import com.babas.controllers.Companys;
import com.babas.controllers.Users;
import com.babas.custom.CustomPasswordField;
import com.babas.custom.JPanelGradiente;
import com.babas.custom.CSwitchButton;
import com.babas.models.BoxSession;
import com.babas.models.Branch;
import com.babas.models.Company;
import com.babas.models.User;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.babas.views.dialogs.DBranch;
import com.babas.views.dialogs.DCompany;
import com.babas.views.dialogs.DUser;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.extras.components.FlatTextField;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.*;
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
    private JLabel lbl;
    private JLabel lblConection;
    private JButton btnTryConection;
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
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onHecho();
            }
        });
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onHecho();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        btnInitSession.addActionListener(e -> start());
        lbl.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ckRememberUser.setSelected(!ckRememberUser.isSelected(), true);
            }
        });
        btnTryConection.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tryConnection();
            }
        });
    }

    public void onHecho() {
        Babas.close();
        dispose();
    }

    public void tryConnection() {
        btnTryConection.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        if (Babas.session == null || !Babas.session.isConnected()) {
            tryConnect();
            Babas.initialize();
            tryConnect();
            if (Babas.session == null || !Babas.session.isConnected()) {
                lblConection.setText("Sin conexión");
            }
        } else {
            lblConection.setText("Conexión establecida");
        }
        btnTryConection.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        check();
    }

    private void check() {
        if (Babas.session != null && Babas.session.isConnected()) {
            Babas.company = Companys.get(1);
            if (Babas.company == null) {
                Babas.company = new Company();
                DCompany dCompany = new DCompany();
                dCompany.setVisible(true);
            } else {
                if (Babas.company.getLogo() != null) {
                    Utilities.downloadLogo(Babas.company.getLogo());
                }
            }
            if (Babas.company.getId() != null) {
                if (Branchs.getTodos().isEmpty()) {
                    DBranch dBranch = new DBranch(new Branch(), true);
                    dBranch.setVisible(true);
                }
            }
            if (Babas.company.getId() != null && !Branchs.getTodos().isEmpty()) {
                if (Users.getTodos().isEmpty()) {
                    User user = new User();
                    user.getBranchs().add(Branchs.get(1));
                    user.getPermitions().setNewSale(true);
                    user.getPermitions().setShowCatalogue(true);
                    user.getPermitions().setRecordSales(true);
                    user.getPermitions().setNewRental(true);
                    user.getPermitions().setRentalsActives(true);
                    user.getPermitions().setRecordRentals(true);
                    user.getPermitions().setNewReserve(true);
                    user.getPermitions().setReservesActives(true);
                    user.getPermitions().setRecordReserves(true);
                    user.getPermitions().setNewTransfer(true);
                    user.getPermitions().setRecordTransfers(true);
                    user.getPermitions().setRecordBoxes(true);
                    user.getPermitions().setManageProducts(true);
                    user.getPermitions().setManageUsers(true);
                    user.getPermitions().setManageBranchs(true);
                    user.getPermitions().setManageCompany(true);
                    user.getPermitions().setAceptTransfer(true);
                    user.getPermitions().setManageClients(true);
                    user.getPermitions().setNewQuotation(true);
                    user.getPermitions().setRecordQuotations(true);
                    DUser dUser = new DUser(true, user);
                    dUser.setVisible(true);
                }
            }
            if (Users.getTodos().isEmpty()) {
                onHecho();
            }
        }
    }

    public void tryConnect() {
        lblConection.setText("Conectando...");
        if (Babas.session != null && Babas.session.isConnected()) {
            lblConection.setText("Conexión establecida");
            if (Babas.company != null) {
                Babas.company.refresh();
                if (Utilities.iconCompanyx255x220 != null) {
                    lblLogo.setIcon(Utilities.iconCompanyx255x220);
                } else if (Babas.company.getLogo() != null) {
                    Image logo = Utilities.getImage(Babas.company.getLogo(), true);
                    if (logo != null) {
                        int width = logo.getWidth(this);
                        int height = logo.getHeight(this);
                        if (width > 255 || height > 200) {
                            double percen = Math.min(255.00 / width, 200.00 / height);
                            width = (int) (percen * width);
                            height = (int) (percen * height);
                        }
                        Utilities.iconCompanyx255x220 = new ImageIcon(logo.getScaledInstance(width, height, Image.SCALE_SMOOTH));
                        if (width > 420 || height > 420) {
                            double percen = Math.min(420.00 / width, 420.00 / height);
                            width = (int) (percen * width);
                            height = (int) (percen * height);
                        }
                        Utilities.iconCompanyx420x420 = new ImageIcon(logo.getScaledInstance(width, height, Image.SCALE_SMOOTH));
                        lblLogo.setIcon(Utilities.iconCompanyx255x220);
                    }
                }
            }
        }
        fieldUser.setEnabled(Babas.session != null && Babas.session.isConnected());
        fieldPasword.setEnabled(Babas.session != null && Babas.session.isConnected());
        btnInitSession.setEnabled(Babas.session != null && Babas.session.isConnected());
        repaint();
    }

    private void init() {
        setContentPane(contentPane);
        setTitle("Software-Tienda");
        Utilities.setJFrame(this);
        getRootPane().setDefaultButton(btnInitSession);
        loadUserSaved();
        setResizable(false);
        pack();
        lbl.setCursor(new Cursor(Cursor.HAND_CURSOR));
        ckRememberUser.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnInitSession.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnInitSession.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblError.setVisible(false);
        btnTryConection.setIcon(new FlatSVGIcon(App.class.getResource("icons/svg/buildLoadChanges.svg")));
        setLocationRelativeTo(null);
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
        contentPane.setLayout(new GridLayoutManager(4, 1, new Insets(10, 10, 10, 10), -1, -1));
        panelDatos = new JPanel();
        panelDatos.setLayout(new GridLayoutManager(6, 1, new Insets(0, 0, 0, 0), 5, 5));
        panelDatos.setBackground(new Color(-12828863));
        panelDatos.setOpaque(false);
        contentPane.add(panelDatos, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnInitSession = new JButton();
        Font btnInitSessionFont = this.$$$getFont$$$(null, -1, 14, btnInitSession.getFont());
        if (btnInitSessionFont != null) btnInitSession.setFont(btnInitSessionFont);
        btnInitSession.setText("Ingresar");
        panelDatos.add(btnInitSession, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(-1, 32), null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), 0, -1));
        panel1.setOpaque(false);
        panelDatos.add(panel1, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        ckRememberUser = new CSwitchButton();
        panel1.add(ckRememberUser, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 5, 0), -1, -1));
        panel2.setOpaque(false);
        panel1.add(panel2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        lbl = new JLabel();
        lbl.setForeground(new Color(-16777216));
        lbl.setText("Recordar usuario");
        panel2.add(lbl, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        fieldUser = new FlatTextField();
        Font fieldUserFont = this.$$$getFont$$$(null, -1, 17, fieldUser.getFont());
        if (fieldUserFont != null) fieldUser.setFont(fieldUserFont);
        fieldUser.setPlaceholderText("Usuario");
        panelDatos.add(fieldUser, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(185, -1), null, 0, false));
        lblError = new JLabel();
        lblError.setForeground(new Color(-976373));
        lblError.setHorizontalTextPosition(11);
        lblError.setText("Credenciales incorrectas");
        panelDatos.add(lblError, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panelDatos.add(spacer2, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        fieldPasword = new CustomPasswordField();
        Font fieldPaswordFont = this.$$$getFont$$$(null, -1, 17, fieldPasword.getFont());
        if (fieldPaswordFont != null) fieldPasword.setFont(fieldPaswordFont);
        fieldPasword.setPlaceholderText("Contraseña");
        panelDatos.add(fieldPasword, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(185, -1), null, 0, false));
        lblLogo = new JLabel();
        lblLogo.setHorizontalAlignment(0);
        lblLogo.setIcon(new ImageIcon(getClass().getResource("/com/babas/images/lojoJmoreno (1).png")));
        lblLogo.setText("");
        contentPane.add(lblLogo, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(255, 200), new Dimension(255, 200), new Dimension(255, 200), 0, false));
        final Spacer spacer3 = new Spacer();
        contentPane.add(spacer3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JToolBar toolBar1 = new JToolBar();
        toolBar1.setOpaque(false);
        contentPane.add(toolBar1, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(-1, 20), null, 0, false));
        lblConection = new JLabel();
        lblConection.setForeground(new Color(-16777216));
        lblConection.setHorizontalTextPosition(11);
        lblConection.setText("Sin conexión...");
        toolBar1.add(lblConection);
        final Spacer spacer4 = new Spacer();
        toolBar1.add(spacer4);
        btnTryConection = new JButton();
        btnTryConection.setMaximumSize(new Dimension(38, 38));
        btnTryConection.setMinimumSize(new Dimension(38, 38));
        btnTryConection.setPreferredSize(new Dimension(38, 38));
        btnTryConection.setText("");
        btnTryConection.setToolTipText("Conectar a la base de datos");
        toolBar1.add(btnTryConection);
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
