package com.babas.views.dialogs;

import com.babas.models.Color;
import com.babas.utilities.Utilities;
import com.babas.validators.ProgramValidator;
import com.babas.views.frames.FPrincipal;
import com.moreno.Notify;
import com.moreno.Principal;
import jakarta.validation.ConstraintViolation;

import javax.swing.*;
import java.awt.event.*;
import java.util.Date;
import java.util.Set;

public class DColor extends JDialog{
    private JPanel contentPane;
    private JButton btnHecho;
    private JTextField txtName;
    private JButton btnSave;
    private Color color;
    private boolean update;

    public DColor(Color color){
        super(Utilities.getJFrame(),"Nuevo color",true);
        this.color=color;
        update=color.getId()!=null;
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
            setTitle("Actualizar color");
            btnSave.setText("Guardar");
            btnHecho.setText("Cancelar");
            load();
        }
        pack();
        setResizable(false);
        setLocationRelativeTo(Utilities.getJFrame());
    }
    private void onSave(){
        color.setName(txtName.getText());
        color.setUpdated(new Date());
        Set<ConstraintViolation<Object>> constraintViolationSet= ProgramValidator.loadViolations(color);
        if(constraintViolationSet.isEmpty()){
            color.save();
            if(!update){
                FPrincipal.colors.add(color);
                Utilities.updateDialog();
                Utilities.getTabbedPane().updateTab();
                color=new Color();
                clear();
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER,"ÉXITO","Color registrado");
            }else{
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER,"ÉXITO","Color actualizado");
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
        txtName.setText(color.getName());
    }
    private void onHecho(){
        if(update){
            color.refresh();
        }
        dispose();
    }
}
