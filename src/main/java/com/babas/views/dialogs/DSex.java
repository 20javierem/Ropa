package com.babas.views.dialogs;

import com.babas.models.Sex;
import com.babas.utilities.Utilities;

import javax.swing.*;

public class DSex extends JDialog{
    private JTextField txtName;
    private JButton btnHecho;
    private JButton btnSave;
    private JPanel contentPane;
    private Sex sex;
    private boolean update;

    public DSex(Sex sex){
        super(Utilities.getJFrame(),"Nuevo sexo",true);
        this.sex=sex;
        update=sex.getId()!=null;
        init();
    }

    private void init(){
        setContentPane(contentPane);
        getRootPane().setDefaultButton(btnSave);
        if(update){
            setTitle("Actualizar sexo");
            load();
        }
    }

    private void load(){
        txtName.setText(sex.getName());
    }
}
