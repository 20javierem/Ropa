package com.babas.views.dialogs;

import com.babas.models.Color;
import com.babas.models.Sex;
import com.babas.utilities.Utilities;
import com.babas.validators.ProgramValidator;
import com.babas.views.frames.FPrincipal;
import com.moreno.Notify;
import jakarta.validation.ConstraintViolation;

import javax.swing.*;
import java.awt.event.*;
import java.util.Date;
import java.util.Set;

public class DSex extends JDialog{
    private JTextField txtName;
    private JButton btnHecho;
    private JButton btnSave;
    private JPanel contentPane;
    private Sex sex;
    private boolean update;

    public DSex(Sex sex){
        super(Utilities.getJFrame(),"Nuevo Género",true);
        this.sex=sex;
        update=sex.getId()!=null;
        init();
        btnHecho.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onHecho();
            }
        });
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSave();
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
        if(update){
            setTitle("Actualizar Género");
            btnSave.setText("Guardar");
            btnHecho.setText("Cancelar");
            load();
        }
        pack();
        setResizable(false);
        setLocationRelativeTo(Utilities.getJFrame());
    }
    private void onSave(){
        sex.setName(txtName.getText());
        Set<ConstraintViolation<Object>> constraintViolationSet= ProgramValidator.loadViolations(sex);
        if(constraintViolationSet.isEmpty()){
            sex.save();
            if(!update){
                FPrincipal.sexs.add(sex);
                Utilities.updateDialog();
                Utilities.getTabbedPane().updateTab();
                sex=new Sex();
                clear();
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER,"ÉXITO","Género registrado");
            }else{
                Utilities.updateDialog();
                Utilities.getTabbedPane().updateTab();
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER,"ÉXITO","Género actualizado");
                onHecho();
            }

        }else{
            ProgramValidator.mostrarErrores(constraintViolationSet);
        }
    }
    private void clear(){
        txtName.setText(null);
    }
    private void load(){
        txtName.setText(sex.getName());
    }
    private void onHecho(){
        if(update){
            sex.refresh();
        }
        dispose();
    }
}
