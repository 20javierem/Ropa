package com.babas.views.dialogs;

import com.babas.controllers.BoxSessions;
import com.babas.models.Box;
import com.babas.models.BoxSession;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.babas.validators.ProgramValidator;
import com.babas.views.frames.FPrincipal;
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
    private JComboBox cbbUser;
    private BoxSession boxSession;
    private boolean update;

    public DBoxSesion(){
        super(Utilities.getJFrame(),"Apertura de caja",true);
        boxSession =Babas.boxSession;
        update= boxSession.getId()!=null;
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
            boxSession.setUser(Babas.user);
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
        cbbBox.addItem(boxSession.getBox().getName()+" / "+ boxSession.getBox().getBranch().getName());
        spinnerAmountInitial.setValue(boxSession.getAmountInitial());
        spinnerAmountToDeliver.setValue(boxSession.getAmountToDelivered());
        spinnerAmountDelivered.setValue(boxSession.getAmountDelivered());
        lblDateStart.setText(Utilities.formatoFechaHora.format(boxSession.getCreated()));
        spinnerAmountInitial.setEnabled(false);
    }

    private void onSave(){
        if(!update){
            boxSession.setAmountInitial((Double) spinnerAmountInitial.getValue());
            boxSession.setUser(Babas.user);
            boxSession.setBox((Box) cbbBox.getSelectedItem());
        }else{
            boxSession.setAmountDelivered((Double) spinnerAmountDelivered.getValue());
            boxSession.setUpdated(new Date());
        }
        Set<ConstraintViolation<Object>> constraintViolationSet= ProgramValidator.loadViolations(boxSession);
        if(constraintViolationSet.isEmpty()){
            boolean flag=true;
            if(!update){
                BoxSession boxSession1= BoxSessions.getByBox(boxSession.getBox());
                if(boxSession1!=null){
                    if(!Objects.equals(boxSession1.getUser().getId(), boxSession.getUser().getId())){
                        flag=JOptionPane.showConfirmDialog(Utilities.getJFrame(),"Ya se abrió esta caja, ¿Desea cerrar la caja "+boxSession.getBox().getName()+"?","Caja abierta",JOptionPane.YES_NO_OPTION)==0;
                    }else{
                        flag=JOptionPane.showConfirmDialog(Utilities.getJFrame(),"Tiene una sesion abierta en esta caja, desea continuar la sessión","Sesión abierta",JOptionPane.YES_NO_OPTION)==0;
                        if(flag){
                            Babas.boxSession=boxSession1;
                            Babas.boxSession.calculateTotals();
                            flag=false;
                            Utilities.getLblDerecha().setText("Monto caja: "+Utilities.moneda.format(boxSession1.getAmountToDelivered()));
                            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER,"ÉXITO","Caja recuperada");
                            onHecho();
                        }else{
                            boxSession1.setAmountDelivered(boxSession1.getAmountToDelivered());
                            boxSession1.setUpdated(new Date());
                            boxSession1.save();
                            flag=true;
                        }
                    }
                }
            }
            if(flag){
                boxSession.save();
                if(update){
                    Utilities.getLblDerecha().setText("Monto caja: "+Utilities.moneda.format(0.00));
                    Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER,"ÉXITO","Caja cerrada");
                    Babas.boxSession =new BoxSession();
                }else{
                    Utilities.getLblDerecha().setText("Monto caja: "+Utilities.moneda.format(boxSession.getAmountToDelivered()));
                    Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER,"ÉXITO","Caja aperturada");
                }
                onHecho();
            }
        }else{
            ProgramValidator.mostrarErrores(constraintViolationSet);
        }
    }

    private void onHecho(){
        if(update){
            boxSession.refresh();
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
