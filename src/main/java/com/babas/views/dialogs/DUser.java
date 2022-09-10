package com.babas.views.dialogs;

import com.babas.custom.CustomPasswordField;
import com.babas.models.User;
import com.babas.utilities.Utilities;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DUser extends JDialog{
    private JPanel contentPane;
    private JButton btnHecho;
    private JButton btnSave;
    private JTabbedPane tabbedPane;
    private JTextField txtName;
    private JComboBox cbbSex;
    private JDateChooser jdateBirthday;
    private JTextField txtLastName;
    private JTextField txtNameUser;
    private CustomPasswordField pswPasword1;
    private CustomPasswordField pswPasword2;
    private JButton btnRemoveBranch;
    private JTable tableBranchs;
    private JTable tableBranchsUser;
    private JButton btnAddBranch;
    private JTextField txtCelular;
    private User user;
    private boolean update=false;

    public DUser(User user){
        super(Utilities.getJFrame(),true);
        this.user=user;
        initialize();
        btnHecho.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onHecho();
            }
        });
    }
    private void initialize(){
        setTitle("Nuevo usuario");
        setContentPane(contentPane);
        pack();
        setLocationRelativeTo(Utilities.getJFrame());
        getRootPane().setDefaultButton(btnSave);
        setResizable(false);
        loadUser();
    }

    private void loadUser(){
        txtName.setText(user.getFirstName());
        txtLastName.setText(user.getLastName());
        txtCelular.setText(user.getPhone());
    }

    private void onHecho(){
        if(update){
            user.refresh();
        }
        dispose();
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        jdateBirthday =new JDateChooser();
        jdateBirthday.setDateFormatString(Utilities.getFormatoFecha());
    }
}
