package com.babas.views.dialogs;

import com.babas.models.Branch;
import com.babas.utilities.Utilities;
import com.formdev.flatlaf.extras.components.FlatButton;
import com.formdev.flatlaf.extras.components.FlatTable;
import com.formdev.flatlaf.extras.components.FlatTextField;
import org.hibernate.sql.Update;

import javax.swing.*;
import java.awt.event.*;

public class DBranch extends JDialog{
    private JPanel contentPane;
    private FlatButton btnHecho;
    private JTabbedPane tabbedPane1;
    private FlatButton btnSave;
    private FlatTextField txtName;
    private JButton btnRemoveBranch;
    private JButton btnAddBranch;
    private FlatTable tableUsers;
    private FlatTable tableUserBranch;
    private JCheckBox ckActive;
    private FlatTextField txtDirection;
    private FlatTextField txtEmail;
    private FlatTextField txtPhone;
    private Branch branch;
    private boolean update;

    public DBranch(Branch branch){
        super(Utilities.getJFrame(),"Nueva sucursal",true);
        this.branch=branch;
        update=branch.getId()!=null;
        init();
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSave();
            }
        });
        btnHecho.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onHecho();
            }
        });
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
    }

    private void init(){
        setContentPane(contentPane);
        getRootPane().setDefaultButton(btnSave);
        pack();
        setResizable(false);
        setLocationRelativeTo(getOwner());
    }

    private void onSave(){

    }

    private void onHecho(){
        if(update){
            branch.refresh();
        }
        dispose();
    }
}
