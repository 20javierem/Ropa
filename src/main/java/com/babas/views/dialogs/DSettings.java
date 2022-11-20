package com.babas.views.dialogs;

import com.babas.utilities.Utilities;
import com.babas.views.frames.FPrincipal;
import com.formdev.flatlaf.extras.components.FlatComboBox;
import com.formdev.flatlaf.extras.components.FlatTextField;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.moreno.Notify;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.*;
import java.util.Locale;

public class DSettings extends JDialog {
    private JTabbedPane tabbedPane1;
    private JButton hechoButton;
    private JButton btnSave;
    private JPanel contentPane;
    private FlatComboBox cbbThemes;
    private FlatComboBox cbbFontSize;
    private JButton btnApply;
    private JRadioButton rbSaleAlways;
    private JRadioButton rbReserveAlways;
    private JRadioButton rbRentalAlways;
    private JRadioButton rbSaleNever;
    private JRadioButton rbReserveNever;
    private JRadioButton rbRentalNever;
    private JRadioButton rbSaleQuestion;
    private JRadioButton rbReserveQuestion;
    private JRadioButton rbRentalQuestion;
    private FlatTextField txtServerIp;
    private FlatTextField txtNameUserServer;
    private FlatTextField txtNameUserPassword;
    private JRadioButton rbLocalImages;
    private JRadioButton rbServerImages;
    private FPrincipal fPrincipal;

    public DSettings(FPrincipal fPrincipal) {
        super(Utilities.getJFrame(), "Configuraciones", true);
        this.fPrincipal = fPrincipal;
        initComponents();
        cbbThemes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeTheme();
            }
        });
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                save();
            }
        });
        hechoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        btnApply.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                apply();
            }
        });
        ((JTextField) cbbFontSize.getEditor().getEditorComponent()).getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                verify();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                verify();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                verify();
            }
        });
        rbLocalImages.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verifyImages(!rbLocalImages.isSelected());
            }
        });
        rbServerImages.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verifyImages(rbServerImages.isSelected());
            }
        });
    }

    private void verifyImages(boolean server) {
        txtServerIp.setVisible(server);
        txtNameUserPassword.setVisible(server);
        txtNameUserServer.setVisible(server);
    }

    private void verify() {
        String newSize = ((JTextField) cbbFontSize.getEditor().getEditorComponent()).getText();
        btnApply.setEnabled(!String.valueOf(Utilities.propiedades.getFont().getSize()).equals(newSize));
    }

    private void apply() {
        Utilities.propiedades.setFont(String.valueOf(cbbFontSize.getSelectedItem()));
        Utilities.propiedades.save();
        changeTheme();
        Utilities.updateUI(true);
        Utilities.updateComponents(Utilities.getJFrame().getRootPane());
        Utilities.updateComponents(getRootPane());
        Utilities.updateUI(false);
        pack();
    }

    private void changeTheme() {
        Utilities.propiedades.setTema(String.valueOf(cbbThemes.getSelectedItem()));
        Utilities.propiedades.save();
        Utilities.loadTheme();
        Utilities.updateUI(true);
        Utilities.updateComponents(Utilities.getJFrame().getRootPane());
        Utilities.updateComponents(getRootPane());
        Utilities.updateUI(false);
        ((JTextField) cbbFontSize.getEditor().getEditorComponent()).getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                verify();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                verify();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                verify();
            }
        });
    }

    private void save() {
        if (rbSaleAlways.isSelected()) {
            Utilities.propiedades.setPrintTicketSale("always");
        } else if (rbSaleNever.isSelected()) {
            Utilities.propiedades.setPrintTicketSale("never");
        } else {
            Utilities.propiedades.setPrintTicketSale("question");
        }

        if (rbReserveAlways.isSelected()) {
            Utilities.propiedades.setPrintTicketReserve("always");
        } else if (rbReserveNever.isSelected()) {
            Utilities.propiedades.setPrintTicketReserve("never");
        } else {
            Utilities.propiedades.setPrintTicketReserve("question");
        }

        if (rbRentalAlways.isSelected()) {
            Utilities.propiedades.setPrintTicketRental("always");
        } else if (rbRentalNever.isSelected()) {
            Utilities.propiedades.setPrintTicketRental("never");
        } else {
            Utilities.propiedades.setPrintTicketRental("question");
        }

        if (rbLocalImages.isSelected()) {
            Utilities.propiedades.setLocalImages("local");
        } else {
            Utilities.propiedades.setLocalImages("server");
            Utilities.propiedades.setServerUrl(txtServerIp.getText().trim());
            Utilities.propiedades.setServerName(txtNameUserServer.getText().trim());
            Utilities.propiedades.setServerPassword(txtNameUserPassword.getText().trim());
        }

        Utilities.propiedades.save();
        onDispose();
        Notify.sendNotify(Utilities.getJFrame(), Notify.Type.INFO, Notify.Location.TOP_CENTER, "ÉXITO", "Cambios guardados");
    }

    private void onDispose() {
        dispose();
    }

    private void onCancel() {
        Utilities.loadTheme();
        Utilities.updateUI(true);
        Utilities.updateComponents(Utilities.getJFrame().getRootPane());
        Utilities.updateComponents(getRootPane());
        Utilities.updateUI(false);
        onDispose();
    }

    private void initComponents() {
        setContentPane(contentPane);
        pack();
        getRootPane().setDefaultButton(btnSave);
        loadSetings();
        setLocationRelativeTo(null);
    }

    private void loadSetings() {
        cbbThemes.setSelectedItem(Utilities.propiedades.getTema());
        cbbFontSize.setSelectedItem(Utilities.propiedades.getFont().getSize());
        txtServerIp.setText(Utilities.propiedades.getServerUrl());
        txtNameUserServer.setText(Utilities.propiedades.getServerName());
        txtNameUserPassword.setText(Utilities.propiedades.getPasswordServer());

        if (Utilities.propiedades.getLocalImages().equals("local")) {
            verifyImages(false);
            rbLocalImages.setSelected(true);
        } else {
            rbServerImages.setSelected(true);
        }

        if (Utilities.propiedades.getPrintTicketSale().equals("always")) {
            rbSaleAlways.setSelected(true);
        } else if (Utilities.propiedades.getPrintTicketSale().equals("never")) {
            rbSaleNever.setSelected(true);
        } else {
            rbSaleQuestion.setSelected(true);
        }

        if (Utilities.propiedades.getPrintTicketReserve().equals("always")) {
            rbReserveAlways.setSelected(true);
        } else if (Utilities.propiedades.getPrintTicketReserve().equals("never")) {
            rbReserveNever.setSelected(true);
        } else {
            rbReserveQuestion.setSelected(true);
        }

        if (Utilities.propiedades.getPrintTicketRental().equals("always")) {
            rbRentalAlways.setSelected(true);
        } else if (Utilities.propiedades.getPrintTicketRental().equals("never")) {
            rbRentalNever.setSelected(true);
        } else {
            rbRentalQuestion.setSelected(true);
        }
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), 10, 10));
        contentPane.setPreferredSize(new Dimension(500, 500));
        tabbedPane1 = new JTabbedPane();
        contentPane.add(tabbedPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(3, 1, new Insets(10, 10, 10, 10), 5, 5));
        tabbedPane1.addTab("Apariencia", panel1);
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$(null, Font.BOLD, -1, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setText("Tema:");
        panel2.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel2.add(spacer2, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        cbbThemes = new FlatComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        defaultComboBoxModel1.addElement("Claro");
        defaultComboBoxModel1.addElement("Oscuro");
        defaultComboBoxModel1.addElement("Ligth");
        defaultComboBoxModel1.addElement("Darcula");
        defaultComboBoxModel1.addElement("Arc");
        defaultComboBoxModel1.addElement("Arc - Orange");
        defaultComboBoxModel1.addElement("Arc Dark");
        defaultComboBoxModel1.addElement("Arc Dark - Orange");
        defaultComboBoxModel1.addElement("Carbon");
        defaultComboBoxModel1.addElement("Cobalt 2");
        defaultComboBoxModel1.addElement("Cyan light");
        defaultComboBoxModel1.addElement("Dark Flat");
        defaultComboBoxModel1.addElement("Dark purple");
        defaultComboBoxModel1.addElement("Dracula");
        defaultComboBoxModel1.addElement("Gradianto Dark Fuchsia");
        defaultComboBoxModel1.addElement("Gradianto Deep Ocean");
        defaultComboBoxModel1.addElement("Gradianto Midnight Blue");
        defaultComboBoxModel1.addElement("Gradianto Nature Green");
        defaultComboBoxModel1.addElement("Gray");
        defaultComboBoxModel1.addElement("Gruvbox Dark Hard");
        defaultComboBoxModel1.addElement("Gruvbox Dark Medium");
        defaultComboBoxModel1.addElement("Gruvbox Dark Soft");
        defaultComboBoxModel1.addElement("Hiberbee Dark");
        defaultComboBoxModel1.addElement("High contrast");
        defaultComboBoxModel1.addElement("Light Flat");
        defaultComboBoxModel1.addElement("Material Design Dark");
        defaultComboBoxModel1.addElement("Monocai");
        defaultComboBoxModel1.addElement("Monokai Pro");
        defaultComboBoxModel1.addElement("Nord");
        defaultComboBoxModel1.addElement("One Dark");
        defaultComboBoxModel1.addElement("Solarized Dark");
        defaultComboBoxModel1.addElement("Solarized Light");
        defaultComboBoxModel1.addElement("Spacegray");
        defaultComboBoxModel1.addElement("Vuesion");
        defaultComboBoxModel1.addElement("Xcode-Dark");
        defaultComboBoxModel1.addElement("Arc Dark (Material)");
        defaultComboBoxModel1.addElement("Arc Dark Contrast (Material)");
        defaultComboBoxModel1.addElement("Atom One Dark (Material)");
        defaultComboBoxModel1.addElement("Atom One Dark Contrast (Material)");
        defaultComboBoxModel1.addElement("Atom One Light (Material)");
        defaultComboBoxModel1.addElement("Atom One Light Contrast (Material)");
        defaultComboBoxModel1.addElement("Dracula (Material)");
        defaultComboBoxModel1.addElement("Dracula Contrast (Material)");
        defaultComboBoxModel1.addElement("GitHub (Material)");
        defaultComboBoxModel1.addElement("GitHub Contrast (Material)");
        defaultComboBoxModel1.addElement("GitHub Dark (Material)");
        defaultComboBoxModel1.addElement("GitHub Dark Contrast (Material)");
        defaultComboBoxModel1.addElement("Light Owl (Material)");
        defaultComboBoxModel1.addElement("Light Owl Contrast (Material)");
        defaultComboBoxModel1.addElement("Material Darker (Material)");
        defaultComboBoxModel1.addElement("Material Darker Contrast (Material)");
        defaultComboBoxModel1.addElement("Material Deep Ocean (Material)");
        defaultComboBoxModel1.addElement("Material Deep Ocean Contrast (Material)");
        defaultComboBoxModel1.addElement("Material Lighter (Material)");
        defaultComboBoxModel1.addElement("Material Lighter Contrast (Material)");
        defaultComboBoxModel1.addElement("Material Oceanic (Material)");
        defaultComboBoxModel1.addElement("Material Oceanic Contrast (Material)");
        defaultComboBoxModel1.addElement("Material Palenight (Material)");
        defaultComboBoxModel1.addElement("Material Palenight Contrast (Material)");
        defaultComboBoxModel1.addElement("Monokai Pro (Material)");
        defaultComboBoxModel1.addElement("Monokai Pro Contrast (Material)");
        defaultComboBoxModel1.addElement("Moonlight (Material)");
        defaultComboBoxModel1.addElement("Moonlight Contrast (Material)");
        defaultComboBoxModel1.addElement("Night Owl (Material)");
        defaultComboBoxModel1.addElement("Night Owl Contrast (Material)");
        defaultComboBoxModel1.addElement("Solarized Dark (Material)");
        defaultComboBoxModel1.addElement("Solarized Dark Contrast (Material)");
        defaultComboBoxModel1.addElement("Solarized Light (Material)");
        defaultComboBoxModel1.addElement("Solarized Light Contrast (Material)");
        cbbThemes.setModel(defaultComboBoxModel1);
        panel2.add(cbbThemes, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        Font label2Font = this.$$$getFont$$$(null, Font.BOLD, -1, label2.getFont());
        if (label2Font != null) label2.setFont(label2Font);
        label2.setText("Tamaño de fuente:");
        panel3.add(label2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbbFontSize = new FlatComboBox();
        cbbFontSize.setEditable(true);
        final DefaultComboBoxModel defaultComboBoxModel2 = new DefaultComboBoxModel();
        defaultComboBoxModel2.addElement("8");
        defaultComboBoxModel2.addElement("9");
        defaultComboBoxModel2.addElement("10");
        defaultComboBoxModel2.addElement("11");
        defaultComboBoxModel2.addElement("12");
        defaultComboBoxModel2.addElement("14");
        defaultComboBoxModel2.addElement("16");
        defaultComboBoxModel2.addElement("18");
        defaultComboBoxModel2.addElement("20");
        defaultComboBoxModel2.addElement("22");
        defaultComboBoxModel2.addElement("24");
        defaultComboBoxModel2.addElement("26");
        defaultComboBoxModel2.addElement("28");
        defaultComboBoxModel2.addElement("36");
        defaultComboBoxModel2.addElement("48");
        defaultComboBoxModel2.addElement("72");
        cbbFontSize.setModel(defaultComboBoxModel2);
        panel3.add(cbbFontSize, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel3.add(spacer3, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(7, 2, new Insets(10, 10, 10, 10), 5, 5));
        tabbedPane1.addTab("Sistema", panel4);
        final JLabel label3 = new JLabel();
        Font label3Font = this.$$$getFont$$$(null, Font.BOLD, -1, label3.getFont());
        if (label3Font != null) label3.setFont(label3Font);
        label3.setText("Imprimir:");
        panel4.add(label3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        panel4.add(spacer4, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 5, new Insets(0, 0, 0, 0), -1, -1));
        panel4.add(panel5, new GridConstraints(3, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        Font label4Font = this.$$$getFont$$$(null, Font.BOLD, -1, label4.getFont());
        if (label4Font != null) label4.setFont(label4Font);
        label4.setText("Ticket de alquiler:");
        panel5.add(label4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        panel5.add(spacer5, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        rbRentalAlways = new JRadioButton();
        rbRentalAlways.setText("Siempre");
        panel5.add(rbRentalAlways, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        rbRentalNever = new JRadioButton();
        rbRentalNever.setText("Nunca");
        panel5.add(rbRentalNever, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        rbRentalQuestion = new JRadioButton();
        rbRentalQuestion.setText("Preguntar");
        panel5.add(rbRentalQuestion, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(1, 5, new Insets(0, 0, 0, 0), -1, -1));
        panel4.add(panel6, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        Font label5Font = this.$$$getFont$$$(null, Font.BOLD, -1, label5.getFont());
        if (label5Font != null) label5.setFont(label5Font);
        label5.setText("Ticket de venta:");
        panel6.add(label5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer6 = new Spacer();
        panel6.add(spacer6, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        rbSaleAlways = new JRadioButton();
        rbSaleAlways.setText("Siempre");
        panel6.add(rbSaleAlways, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        rbSaleNever = new JRadioButton();
        rbSaleNever.setText("Nunca");
        panel6.add(rbSaleNever, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        rbSaleQuestion = new JRadioButton();
        rbSaleQuestion.setText("Preguntar");
        panel6.add(rbSaleQuestion, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridLayoutManager(1, 5, new Insets(0, 0, 0, 0), -1, -1));
        panel4.add(panel7, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        Font label6Font = this.$$$getFont$$$(null, Font.BOLD, -1, label6.getFont());
        if (label6Font != null) label6.setFont(label6Font);
        label6.setText("Ticket de reserva:");
        panel7.add(label6, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer7 = new Spacer();
        panel7.add(spacer7, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        rbReserveAlways = new JRadioButton();
        rbReserveAlways.setText("Siempre");
        panel7.add(rbReserveAlways, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        rbReserveNever = new JRadioButton();
        rbReserveNever.setText("Nunca");
        panel7.add(rbReserveNever, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        rbReserveQuestion = new JRadioButton();
        rbReserveQuestion.setText("Preguntar");
        panel7.add(rbReserveQuestion, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer8 = new Spacer();
        panel4.add(spacer8, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        Font label7Font = this.$$$getFont$$$(null, Font.BOLD, -1, label7.getFont());
        if (label7Font != null) label7.setFont(label7Font);
        label7.setText("Imagenes:");
        panel4.add(label7, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new GridLayoutManager(4, 4, new Insets(0, 0, 0, 0), -1, -1));
        panel4.add(panel8, new GridConstraints(5, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer9 = new Spacer();
        panel8.add(spacer9, new GridConstraints(0, 2, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        rbLocalImages = new JRadioButton();
        rbLocalImages.setText("Local");
        panel8.add(rbLocalImages, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        rbServerImages = new JRadioButton();
        rbServerImages.setText("Servidor");
        panel8.add(rbServerImages, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtServerIp = new FlatTextField();
        txtServerIp.setPlaceholderText("Dirección ip");
        panel8.add(txtServerIp, new GridConstraints(1, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer10 = new Spacer();
        panel8.add(spacer10, new GridConstraints(1, 3, 3, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        txtNameUserServer = new FlatTextField();
        txtNameUserServer.setPlaceholderText("Nombre de usuario");
        panel8.add(txtNameUserServer, new GridConstraints(2, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        txtNameUserPassword = new FlatTextField();
        txtNameUserPassword.setPlaceholderText("Contraseña");
        panel8.add(txtNameUserPassword, new GridConstraints(3, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel9 = new JPanel();
        panel9.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel9, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_SOUTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        hechoButton = new JButton();
        hechoButton.setText("Cancel");
        panel9.add(hechoButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer11 = new Spacer();
        panel9.add(spacer11, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        btnSave = new JButton();
        btnSave.setText("OK");
        panel9.add(btnSave, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnApply = new JButton();
        btnApply.setEnabled(false);
        btnApply.setText("Aplicar");
        panel9.add(btnApply, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ButtonGroup buttonGroup;
        buttonGroup = new ButtonGroup();
        buttonGroup.add(rbSaleAlways);
        buttonGroup.add(rbSaleNever);
        buttonGroup.add(rbSaleQuestion);
        buttonGroup = new ButtonGroup();
        buttonGroup.add(rbReserveAlways);
        buttonGroup.add(rbReserveNever);
        buttonGroup.add(rbReserveQuestion);
        buttonGroup = new ButtonGroup();
        buttonGroup.add(rbRentalAlways);
        buttonGroup.add(rbRentalNever);
        buttonGroup.add(rbRentalQuestion);
        buttonGroup = new ButtonGroup();
        buttonGroup.add(rbLocalImages);
        buttonGroup.add(rbServerImages);
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
