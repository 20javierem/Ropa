package com.babas.views.dialogs;

import com.babas.utilities.Utilities;
import com.formdev.flatlaf.extras.components.FlatButton;
import com.formdev.flatlaf.extras.components.FlatComboBox;
import com.formdev.flatlaf.extras.components.FlatTextField;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DCompany extends JDialog {
    private JPanel contentPane;
    private FlatButton btnHecho;
    private FlatButton btnSave;
    private JTabbedPane tabbedPane1;
    private FlatTextField txtBusinessName;
    private JTextArea txtSlogan;
    private FlatTextField txtUserSunat;
    private FlatComboBox cbbCurrency;
    private FlatTextField txtRuc;
    private FlatTextField txtTradeName;
    private FlatTextField txtFiscalAdress;
    private FlatTextField txtLogo;
    private FlatTextField txtWebSide;
    private FlatTextField txtPasswordSunat;
    private FlatTextField txtCertificate;
    private FlatTextField txtPsswordCertificate;
    private FlatComboBox cbbRegime;
    private FlatComboBox cbbTypeDocument;

    public DCompany() {
        super(Utilities.getJFrame(), "Nueva empresa", true);
        init();
    }

    private void init() {
        setContentPane(contentPane);
        pack();
        setLocationRelativeTo(getOwner());
        loadButtonLogo();
    }
    private void loadButtonLogo(){
        FlatButton btnShowCrop=new FlatButton();
        btnShowCrop.setText("Logo");
        btnShowCrop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DCrop dCrop=new DCrop(null);
                dCrop.setVisible(true);
            }
        });
        txtLogo.setTrailingComponent(btnShowCrop);
    }
}
