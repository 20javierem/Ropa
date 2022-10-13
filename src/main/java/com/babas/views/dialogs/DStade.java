package com.babas.views.dialogs;

import com.babas.models.Stade;
import com.babas.utilities.Utilities;
import com.babas.validators.ProgramValidator;
import com.babas.views.frames.FPrincipal;
import com.moreno.Notify;
import jakarta.validation.ConstraintViolation;

import javax.swing.*;
import java.awt.event.*;
import java.util.Set;

public class DStade extends JDialog{
    private JPanel contentPane;
    private JButton btnHecho;
    private JTextField txtName;
    private JButton btnSave;
    private Stade stade;
    private boolean update;

    public DStade(Stade stade){
        super(Utilities.getJFrame(),"Nuevo estado",true);
        this.stade=stade;
        update=stade.getId()!=null;
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
            setTitle("Actualizar estado");
            btnSave.setText("Guardar");
            btnHecho.setText("Cancelar");
            load();
        }
        pack();
        setResizable(false);
        setLocationRelativeTo(Utilities.getJFrame());
    }
    private void onSave(){
        stade.setName(txtName.getText());
        Set<ConstraintViolation<Object>> constraintViolationSet= ProgramValidator.loadViolations(stade);
        if(constraintViolationSet.isEmpty()){
            stade.save();
            if(!update){
                FPrincipal.stades.add(stade);
                FPrincipal.stadesWithAll.add(stade);
                Utilities.updateDialog();
                Utilities.getTabbedPane().updateTab();
                stade=new Stade();
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
        txtName.setText(stade.getName());
    }
    private void onHecho(){
        if(update){
            stade.refresh();
        }
        dispose();
    }
}