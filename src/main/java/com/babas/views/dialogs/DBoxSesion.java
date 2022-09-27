package com.babas.views.dialogs;

import com.babas.models.Box;
import com.babas.models.BoxSesion;
import com.babas.models.Branch;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.babas.validators.ProgramValidator;
import com.formdev.flatlaf.extras.components.FlatSpinner;
import com.moreno.Notify;
import jakarta.validation.ConstraintViolation;

import javax.swing.*;
import java.awt.event.*;
import java.util.*;

public class DBoxSesion extends JDialog{
    private JPanel contentPane;
    private JButton btnHecho;
    private JButton btnSave;
    private JComboBox cbbBox;
    private FlatSpinner spinnerAmountInitial;
    private FlatSpinner spinnerAmountToDeliver;
    private FlatSpinner spinnerAmountDelivered;
    private JLabel lblDateStart;
    private JLabel lblDateEnd;
    private JComboBox cbbUser;
    private BoxSesion boxSesion;
    private boolean update;

    public DBoxSesion(){
        super(Utilities.getJFrame(),"Apertura de caja",true);
        boxSesion=Babas.boxSesion;
        update=boxSesion.getId()!=null;
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
    }

    private void init(){
        setContentPane(contentPane);
        getRootPane().setDefaultButton(btnSave);
        cbbUser.addItem(Babas.user.getUserName());
        if(update){
            load();
            btnSave.setText("Cerrar caja");
        }else{
            boxSesion.setUser(Babas.user);
            spinnerAmountDelivered.setEnabled(false);
            Vector<Box> boxes=new Vector<>();
            Babas.user.getBranchs().forEach(branch -> {
                boxes.addAll(branch.getBoxs());
            });
            cbbBox.setModel(new DefaultComboBoxModel(boxes));
            cbbBox.setRenderer(new Box.ListCellRenderer());
        }
        pack();
        setLocationRelativeTo(getOwner());
        setResizable(false);
    }

    private void load(){
        cbbBox.addItem(boxSesion.getBox().getName()+" / "+boxSesion.getBox().getBranch().getName());
        spinnerAmountInitial.setValue(boxSesion.getAmountInitial());
        spinnerAmountToDeliver.setValue(boxSesion.getAmountToDelivered());
        spinnerAmountDelivered.setValue(boxSesion.getAmountDelivered());
        lblDateStart.setText(Utilities.formatoFechaHora.format(boxSesion.getCreated()));
        if(boxSesion.getUpdated()!=null){
            lblDateEnd.setText(Utilities.formatoFechaHora.format(boxSesion.getUpdated()));
        }
        spinnerAmountInitial.setEnabled(false);
    }

    private void onSave(){
        if(!update){
            boxSesion.setAmountInitial((Double) spinnerAmountInitial.getValue());
            boxSesion.setUser(Babas.user);
            boxSesion.setBox((Box) cbbBox.getSelectedItem());
        }else{
            boxSesion.setAmountDelivered((Double) spinnerAmountDelivered.getValue());
            boxSesion.setUpdated(new Date());
        }
        Set<ConstraintViolation<Object>> constraintViolationSet= ProgramValidator.loadViolations(boxSesion);
        if(constraintViolationSet.isEmpty()){
            boxSesion.save();
            if(update){
                Utilities.getLblDerecha().setText("Monto caja: "+Utilities.moneda.format(0.00));
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER,"ÉXITO","Caja cerrada");
                Babas.boxSesion=new BoxSesion();
            }else{
                Utilities.getLblDerecha().setText("Monto caja: "+Utilities.moneda.format(boxSesion.getAmountToDelivered()));
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER,"ÉXITO","Caja aperturada");
            }
            onHecho();
        }else{
            ProgramValidator.mostrarErrores(constraintViolationSet);
        }
    }

    private void onHecho(){
        if(update){
            boxSesion.refresh();
        }
        dispose();
    }
    private void createUIComponents() {
        // TODO: place custom component creation code here
        spinnerAmountInitial=new FlatSpinner();
        spinnerAmountInitial.setModel(new SpinnerNumberModel(0.0, 0.0, 100000, 1.0));
        spinnerAmountInitial.setEditor(Utilities.getEditorPrice(spinnerAmountInitial));
        spinnerAmountToDeliver=new FlatSpinner();
        spinnerAmountToDeliver.setModel(new SpinnerNumberModel(0.0, 0.0, 100000, 1.0));
        spinnerAmountToDeliver.setEditor(Utilities.getEditorPrice(spinnerAmountToDeliver));
        spinnerAmountDelivered=new FlatSpinner();
        spinnerAmountDelivered.setModel(new SpinnerNumberModel(0.0, 0.0, 100000, 1.0));
        spinnerAmountDelivered.setEditor(Utilities.getEditorPrice(spinnerAmountDelivered));
    }
}
