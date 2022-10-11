package com.babas.views.dialogs;

import com.babas.models.Movement;
import com.babas.models.Price;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.babas.validators.ProgramValidator;
import com.formdev.flatlaf.extras.components.FlatSpinner;
import com.moreno.Notify;
import jakarta.validation.ConstraintViolation;

import javax.swing.*;
import java.awt.event.*;
import java.util.Set;

public class DMovement extends JDialog{
    private JPanel contentPane;
    private JButton btnHecho;
    private JRadioButton ckEntrance;
    private JTextField txtDescription;
    private FlatSpinner spinnerAmount;
    private JButton btnSave;
    private JRadioButton ckExit;
    private Movement movement;
    private boolean updated;
    private int pX,pY;

    public DMovement(Movement movement){
        super(Utilities.getJFrame(),"Nuevo movimiento",true);
        this.movement=movement;
        updated=movement.getId()!=null;
        init();
        contentPane.addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent me) {
                pX=me.getX();
                pY=me.getY();
            }
        });
        contentPane.addMouseMotionListener(new MouseAdapter(){
            public void mouseDragged(MouseEvent me) {
                setLocation(getLocation().x+me.getX()-pX,getLocation().y+me.getY()-pY);
            }
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
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
        setUndecorated(true);
        setContentPane(contentPane);
        getRootPane().setDefaultButton(btnSave);
        setResizable(false);
        if(updated){
            load();
        }
        pack();
        setLocationRelativeTo(getOwner());
    }
    private void load(){
        txtDescription.setText(movement.getDescription());
        if(movement.isEntrance()){
            ckEntrance.setSelected(true);
        }else{
            ckExit.setSelected(true);
        }
        spinnerAmount.setValue(movement.getAmount());
    }
    private void onSave(){
        if(Babas.boxSession.getId()!=null){
            movement.setDescription(txtDescription.getText().trim());
            movement.setEntrance(ckEntrance.isSelected());
            movement.setBoxSesion(Babas.boxSession);
            if(ckEntrance.isSelected()){
                movement.setAmount((Double) spinnerAmount.getValue());
            }else{
                movement.setAmount(-(Double) spinnerAmount.getValue());
            }
            Set<ConstraintViolation<Object>> constraintViolationSet= ProgramValidator.loadViolations(movement);
            if(constraintViolationSet.isEmpty()){
                movement.save();
                if(!updated){
                    movement.getBoxSesion().getMovements().add(movement);
                }
                movement.getBoxSesion().calculateTotals();
                Utilities.getTabbedPane().updateTab();
            }else{
                ProgramValidator.mostrarErrores(constraintViolationSet);
            }
        }else{
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","Debe aperturar caja");
        }

    }
    private void onHecho(){
        if(updated){
            movement.refresh();
        }
        dispose();
    }
    private void createUIComponents() {
        // TODO: place custom component creation code here
        spinnerAmount=new FlatSpinner();
        spinnerAmount.setModel(new SpinnerNumberModel(1.00, 0.01, 100000.00, 0.50));
        spinnerAmount.setEditor(Utilities.getEditorPrice(spinnerAmount));
    }
}
