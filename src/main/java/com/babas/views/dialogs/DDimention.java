package com.babas.views.dialogs;

import com.babas.models.Color;
import com.babas.models.Dimention;
import com.babas.utilities.Utilities;
import com.babas.validators.ProgramValidator;
import com.babas.views.frames.FPrincipal;
import com.moreno.Notify;
import jakarta.validation.ConstraintViolation;

import javax.swing.*;
import java.awt.event.*;
import java.util.Set;

public class DDimention extends JDialog{
    private JPanel contentPane;
    private JButton btnHecho;
    private JTextField txtName;
    private JButton btnSave;
    private Dimention dimention;
    private boolean update;

    public DDimention(Dimention dimention){
        super(Utilities.getJFrame(),"Nueva dimensión",true);
        this.dimention=dimention;
        update=dimention.getId()!=null;
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
    }

    private void init(){
        setContentPane(contentPane);
        getRootPane().setDefaultButton(btnSave);
        if(update){
            setTitle("Actualizar dimensión");
            btnSave.setText("Guardar");
            btnHecho.setText("Cancelar");
            load();
        }
        pack();
        setResizable(false);
        setLocationRelativeTo(Utilities.getJFrame());
    }
    private void onSave(){
        dimention.setName(txtName.getText());
        Set<ConstraintViolation<Object>> constraintViolationSet= ProgramValidator.loadViolations(dimention);
        if(constraintViolationSet.isEmpty()){
            dimention.save();
            if(!update){
                FPrincipal.dimentions.add(dimention);
                Utilities.updateDialog();
                Utilities.getTabbedPane().updateTab();
                dimention=new Dimention();
                clear();
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER,"ÉXITO","Dimensión registrado");
            }else{
                Utilities.updateDialog();
                Utilities.getTabbedPane().updateTab();
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER,"ÉXITO","Dimensión actualizado");
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
        txtName.setText(dimention.getName());
    }
    private void onHecho(){
        if(update){
            dimention.refresh();
        }
        dispose();
    }
}