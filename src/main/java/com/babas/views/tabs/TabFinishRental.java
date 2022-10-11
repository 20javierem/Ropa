package com.babas.views.tabs;

import com.babas.App;
import com.babas.custom.TabPane;
import com.babas.models.Movement;
import com.babas.models.Rental;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.UtilitiesTables;
import com.babas.utilitiesTables.tablesCellRendered.DetailRentalCellRendered;
import com.babas.utilitiesTables.tablesModels.DetailRentalAbstractModel;
import com.babas.validators.ProgramValidator;
import com.babas.views.frames.FPrincipal;
import com.formdev.flatlaf.extras.components.FlatSpinner;
import com.formdev.flatlaf.extras.components.FlatTable;
import com.formdev.flatlaf.extras.components.FlatTextField;
import com.moreno.Notify;
import com.thoughtworks.qdox.model.expression.Not;
import com.toedter.calendar.JDateChooser;
import jakarta.validation.ConstraintViolation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Date;
import java.util.Set;

public class TabFinishRental {
    private TabPane tabPane;
    private FlatTable table;
    private FlatTextField txtDocument;
    private FlatTextField txtNameClient;
    private FlatTextField txtPhone;
    private FlatTextField txtMail;
    private JDateChooser jDateFinish;
    private FlatSpinner spinnerPenalty;
    private JLabel lblSubTotal;
    private JLabel lblDiscount;
    private JLabel lblWarranty;
    private JLabel lblTotal;
    private JLabel lblTotalCurrent;
    private JButton btnFinishRental;
    private JLabel lblLogo;
    private JLabel lblPenalty;
    private JLabel lblTotalWithPenalty;
    private JLabel lblWarranty2;
    private Rental rental;
    private DetailRentalAbstractModel model;

    public TabFinishRental(Rental rental){
        this.rental=rental;
        init();
        ((JSpinner.NumberEditor) spinnerPenalty.getEditor()).getTextField().addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                SwingUtilities.invokeLater(() -> {
                    JTextField textField = (JTextField) e.getSource();
                    textField.selectAll();
                });
            }
            @Override
            public void focusLost(FocusEvent e) {
                SwingUtilities.invokeLater(() -> {
                    rental.setPenalty((Double) spinnerPenalty.getValue());
                    loadTotals();
                });
            }
        });
        btnFinishRental.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSave();
            }
        });
    }
    private void init(){
        tabPane.setTitle("Finalización alquiler Nro. "+rental.getNumberRental());
        if(!Babas.company.getLogo().isBlank()){
            ImageIcon logo=new ImageIcon(new ImageIcon(Utilities.getImage(Babas.company.getLogo())).getImage().getScaledInstance(255, 220, Image.SCALE_SMOOTH));
            lblLogo.setIcon(logo);
        }else{
            ImageIcon logo=new ImageIcon(new ImageIcon(App.class.getResource("images/logo.jpeg")).getImage().getScaledInstance(255, 220, Image.SCALE_SMOOTH));
            lblLogo.setIcon(logo);
        }
        load();
        loadTotals();
    }
    private void onSave(){
        if(Babas.boxSession.getId()!=null){
            if(jDateFinish.getDate()!=null){
                boolean si=JOptionPane.showConfirmDialog(Utilities.getJFrame(),"¿Está seguro?","Comfirmar Alquiler",JOptionPane.YES_NO_OPTION)==0;
                if(si){
                    rental.refresh();
                    if(rental.isActive()){
                        rental.setPenalty((Double) spinnerPenalty.getValue());
                        rental.calculateTotals();
                        rental.setDelivery(jDateFinish.getDate());
                        rental.setActive(false);
                        rental.save();
                        Movement movement=new Movement();
                        if(rental.getPenalty()>rental.getWarranty()){
                            movement.setEntrance(true);
                            movement.setAmount(rental.getPenalty()-rental.getWarranty());
                        }else{
                            movement.setEntrance(false);
                            movement.setAmount(-(rental.getWarranty()-rental.getPenalty()));
                        }
                        movement.setBoxSesion(Babas.boxSession);
                        movement.setDescription("ALQUILER FINALIZADO NRO: "+rental.getNumberRental());
                        movement.save();
                        movement.getBoxSesion().getMovements().add(0,movement);
                        movement.getBoxSesion().calculateTotals();
                        Utilities.getLblDerecha().setText("Monto caja: "+Utilities.moneda.format(Babas.boxSession.getAmountToDelivered()));
                        Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER,"ÉXITO","Alquiler finalizado");
                    }else{
                        Notify.sendNotify(Utilities.getJFrame(), Notify.Type.INFO, Notify.Location.TOP_CENTER,"MENSAJE","El alquiler ya fue finalizado por otro usuario");
                    }
                    FPrincipal.rentalsActives.remove(rental);
                    btnFinishRental.setVisible(false);
                    jDateFinish.setEnabled(false);
                    spinnerPenalty.setEnabled(false);
                }
            }else{
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","Debe introducir fecha de entrega");
            }
        }else{
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","Debe aperturar caja");
        }
    }
    private void load(){
        model=new DetailRentalAbstractModel(rental.getDetailRentals());
        table.setModel(model);
        DetailRentalCellRendered.setCellRenderer(table);
        UtilitiesTables.headerNegrita(table);
        txtDocument.setText(rental.getClient().getDni());
        txtNameClient.setText(rental.getClient().getNames());
        txtPhone.setText(rental.getClient().getPhone());
        txtMail.setText(rental.getClient().getMail());
        table.removeColumn(table.getColumn(""));
        if(!rental.isActive()){
            spinnerPenalty.setValue(rental.getPenalty());
            jDateFinish.setDate(rental.getDelivery());
            btnFinishRental.setVisible(false);
            jDateFinish.setEnabled(false);
            spinnerPenalty.setEnabled(false);
        }
    }

    private void loadTotals(){
        rental.calculateTotals();

        lblTotal.setText(Utilities.moneda.format(rental.getTotal()));
        lblWarranty.setText(Utilities.moneda.format(rental.getWarranty()));
        lblSubTotal.setText(Utilities.moneda.format(rental.getTotal()));
        lblDiscount.setText(Utilities.moneda.format(rental.getDiscount()));
        lblTotalCurrent.setText(Utilities.moneda.format(rental.getTotalCurrent()));

        lblWarranty2.setText(Utilities.moneda.format(rental.getWarranty()));
        lblPenalty.setText(Utilities.moneda.format(rental.getPenalty()));
        lblTotalWithPenalty.setText(Utilities.moneda.format(rental.getWarranty()-rental.getPenalty()));
    }

    public TabPane getTabPane() {
        return tabPane;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        spinnerPenalty=new FlatSpinner();
        spinnerPenalty.setModel(new SpinnerNumberModel(0.00, 0.00, 100000.00, 0.50));
        spinnerPenalty.setEditor(Utilities.getEditorPrice(spinnerPenalty));
        jDateFinish=new JDateChooser(new Date());
        jDateFinish.setDateFormatString(Utilities.getFormatoFecha());
    }
}
