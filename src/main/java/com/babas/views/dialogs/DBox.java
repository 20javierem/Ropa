package com.babas.views.dialogs;

import com.babas.models.Box;
import com.babas.models.Branch;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.babas.validators.ProgramValidator;
import com.formdev.flatlaf.extras.components.FlatSpinner;
import com.moreno.Notify;
import jakarta.validation.ConstraintViolation;

import javax.swing.*;
import java.awt.event.*;
import java.util.Date;
import java.util.Set;
import java.util.Vector;

public class DBox extends JDialog{
    private JPanel contentPane;
    private JButton btnHecho;
    private JButton btnSave;
    private JComboBox cbbBranch;
    private FlatSpinner spinnerAmountInitial;
    private FlatSpinner spinnerAmountToDeliver;
    private FlatSpinner spinnerAmountDelivered;
    private JLabel lblDateStart;
    private JLabel lblDateEnd;
    private JComboBox cbbUser;
    private Box box;
    private boolean update;

    public DBox(){
        super(Utilities.getJFrame(),"Apertura de caja",true);
        box= Babas.box;
        update=box.getId()!=null;
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
        if(box.getUpdated()!=null){
            box=new Box();
            update=false;
        }
        cbbUser.addItem(Babas.user.getUserName());
        if(box.getId()!=null){
            load();
            btnSave.setText("Cerrar caja");
        }else{
            spinnerAmountDelivered.setEnabled(false);
            cbbBranch.setModel(new DefaultComboBoxModel(new Vector(Babas.user.getBranchs())));
            cbbBranch.setRenderer(new Branch.ListCellRenderer());
        }
        pack();
        setLocationRelativeTo(getOwner());
        setResizable(false);
    }

    private void load(){
        cbbBranch.addItem(box.getBranch().getName());
        spinnerAmountInitial.setValue(box.getStartingAmount());
        spinnerAmountToDeliver.setValue(box.getAmountToDeliver());
        spinnerAmountDelivered.setValue(box.getAmountDelivered());
        lblDateStart.setText(Utilities.formatoFechaHora.format(box.getCreated()));
        if(box.getUpdated()!=null){
            lblDateEnd.setText(Utilities.formatoFechaHora.format(box.getUpdated()));
        }
        spinnerAmountInitial.setEnabled(false);
    }

    private void onSave(){
        if(!update){
            box.setStartingAmount((Double) spinnerAmountInitial.getValue());
            box.setUser(Babas.user);
            box.setBranch((Branch) cbbBranch.getSelectedItem());
        }else{
            box.setAmountDelivered((Double) spinnerAmountDelivered.getValue());
            box.setUpdated(new Date());
        }
        Set<ConstraintViolation<Object>> constraintViolationSet= ProgramValidator.loadViolations(box);
        if(constraintViolationSet.isEmpty()){
            box.save();
            if(update){
                Utilities.getLblDerecha().setText("Monto caja: "+Utilities.moneda.format(0.00));
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER,"ÉXITO","Caja cerrada");
            }else{
                Utilities.getLblDerecha().setText("Monto caja: "+Utilities.moneda.format(box.getAmountToDeliver()));
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER,"ÉXITO","Caja aperturada");
            }
            onHecho();
        }else{
            ProgramValidator.mostrarErrores(constraintViolationSet);
        }
    }

    private void onHecho(){
        if(update){
            box.refresh();
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
