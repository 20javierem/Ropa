package com.babas.views.tabs;

import com.babas.App;
import com.babas.controllers.Clients;
import com.babas.custom.TabPane;
import com.babas.models.Client;
import com.babas.models.Rental;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.UtilitiesTables;
import com.babas.utilitiesTables.tablesCellRendered.DetailRentalCellRendered;
import com.babas.utilitiesTables.tablesModels.DetailRentalAbstractModel;
import com.babas.validators.ProgramValidator;
import com.babas.views.dialogs.DaddProductToRental;
import com.formdev.flatlaf.extras.components.FlatSpinner;
import com.formdev.flatlaf.extras.components.FlatTable;
import com.formdev.flatlaf.extras.components.FlatTextField;
import com.moreno.Notify;
import com.toedter.calendar.JDateChooser;
import jakarta.validation.ConstraintViolation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Calendar;
import java.util.Set;

public class TabNewRental {
    private TabPane tabPane;
    private JButton btnAddProducts;
    private FlatTable table;
    private FlatTextField txtDocument;
    private FlatTextField txtNameClient;
    private FlatTextField txtPhone;
    private FlatTextField txtMail;
    private FlatSpinner spinnerDiscount;
    private JLabel lblSubTotal;
    private JLabel lblTotal;
    private JLabel lblDiscount;
    private JButton btnSaleWithTrasnfer;
    private JButton btnSaleWithCash;
    private JLabel lblLogo;
    private JDateChooser jDateFinish;
    private FlatSpinner spinnerWarranty;
    private JLabel lblTotalCurrent;
    private JLabel lblWarranty;
    private Rental rental;
    private DetailRentalAbstractModel model;

    public TabNewRental(){
        init();
        btnAddProducts.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadAddProducts();
            }
        });
        txtDocument.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.VK_ENTER){
                    searchClient();
                }
            }
        });
        btnSaleWithCash.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSave(true);
            }
        });
        btnSaleWithTrasnfer.addActionListener(e -> onSave(false));
        ((JSpinner.NumberEditor)spinnerDiscount.getEditor()).getTextField().addFocusListener(new FocusAdapter() {
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
                    rental.setDiscount((Double) spinnerDiscount.getValue());
                    loadTotals();
                });
            }
        });
        ((JSpinner.NumberEditor) spinnerWarranty.getEditor()).getTextField().addFocusListener(new FocusAdapter() {
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
                    rental.setWarranty((Double) spinnerWarranty.getValue());
                    loadTotals();
                });
            }
        });
    }

    private void init(){
        tabPane.setTitle("Nuevo alquiler");
        rental=new Rental();
        loadTable();
        tabPane.getActions().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.fireTableDataChanged();
                rental.calculateTotal();
                loadTotals();
            }
        });
        ImageIcon logo=new ImageIcon(new ImageIcon(App.class.getResource("images/lojoJmoreno (1).png")).getImage().getScaledInstance(255, 220, Image.SCALE_SMOOTH));
        lblLogo.setIcon(logo);
    }
    private void searchClient(){
        Client client= Clients.getByDNI(txtDocument.getText().trim());
        if(client!=null){
            txtNameClient.setText(client.getNames());
            txtPhone.setText(client.getPhone());
            txtMail.setText(client.getMail());
        }
    }
    private void loadAddProducts(){
        if(Babas.boxSession.getId()!=null){
            rental.setBranch(Babas.boxSession.getBox().getBranch());
        }else{
            rental.setBranch(null);
        }
        if(rental.getBranch()!=null){
            DaddProductToRental daddProductToRental =new DaddProductToRental(rental);
            daddProductToRental.setVisible(true);
        }else{
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","Debe abrir caja para comenzar");
        }
    }
    private void loadTable(){
        model=new DetailRentalAbstractModel(rental.getDetailRentals());
        table.setModel(model);
        DetailRentalCellRendered.setCellRenderer(table);
        UtilitiesTables.headerNegrita(table);
//        table.getColumnModel().getColumn(model.getColumnCount() - 1).setCellEditor(new JButtonEditorDetailSale2());
//        table.getColumnModel().getColumn(model.getColumnCount() - 3).setCellEditor(new JButtonEditorDetailSale());
//        table.getColumnModel().getColumn(model.getColumnCount() - 4).setCellEditor(new JButtonEditorDetailSale());
    }

    private void loadTotals(){
        if(Babas.boxSession.getId()==null){
            rental.getDetailRentals().clear();
            rental.setBranch(null);
        }
        rental.calculateTotal();
        lblSubTotal.setText(Utilities.moneda.format(rental.getTotal()));
        lblWarranty.setText(Utilities.moneda.format(rental.getWarranty()));
        lblTotal.setText(Utilities.moneda.format(rental.getTotalCurrent()+rental.getDiscount()));
        lblDiscount.setText(Utilities.moneda.format(rental.getDiscount()));
        lblTotalCurrent.setText(Utilities.moneda.format(rental.getTotalCurrent()));
    }
    private void onSave(boolean isCash){
        if(Babas.boxSession.getId()!=null){
            if(getClient()){
                rental.setBranch(Babas.boxSession.getBox().getBranch());
                rental.setCash(isCash);
                rental.setBoxSession(Babas.boxSession);
                rental.setUser(Babas.user);
                rental.setEnded(jDateFinish.getDate());
                Set<ConstraintViolation<Object>> constraintViolationSet= ProgramValidator.loadViolations(rental);
                if(constraintViolationSet.isEmpty()){
                    boolean si=JOptionPane.showConfirmDialog(Utilities.getJFrame(),"¿Está seguro?","Comfirmar Alquiler",JOptionPane.YES_NO_OPTION)==0;
                    if(si){
                        rental.save();
                        Babas.boxSession.getRentals().add(0,rental);
                        Babas.boxSession.calculateTotals();
                        rental=new Rental();
                        clear();
                        Utilities.getTabbedPane().updateTab();
                        Utilities.getLblDerecha().setText("Monto caja: "+Utilities.moneda.format(Babas.boxSession.getAmountToDelivered()));
                        Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER,"ÉXITO","Alquiler registrado");
                    }
                }else{
                    ProgramValidator.mostrarErrores(constraintViolationSet);
                }
            }else{
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","Debe introducir NOMBRE y DNI");
            }
        }else{
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","Debe aperturar caja");
        }
    }
    private void clear(){
        txtMail.setText(null);
        txtDocument.setText(null);
        txtPhone.setText(null);
        txtNameClient.setText(null);
        loadTable();
        loadTotals();
    }
    private boolean getClient(){
        if(!txtDocument.getText().isBlank()&&!txtNameClient.getText().isBlank()){
            Client client= Clients.getByDNI(txtDocument.getText().trim());
            if(client==null){
                client=new Client();
                client.setDni(txtDocument.getText().trim());
            }
            client.setNames(txtNameClient.getText().trim());
            client.setMail(txtMail.getText().trim());
            client.setPhone(txtPhone.getText().trim());
            client.save();
            rental.setClient(client);
            return true;
        }
        return false;
    }
    public TabPane getTabPane() {
        return tabPane;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        spinnerDiscount=new FlatSpinner();
        spinnerDiscount.setModel(new SpinnerNumberModel(0.00, 0.00, 100000.00, 0.50));
        spinnerDiscount.setEditor(Utilities.getEditorPrice(spinnerDiscount));
        spinnerWarranty =new FlatSpinner();
        spinnerWarranty.setModel(new SpinnerNumberModel(0.00, 0.00, 100000.00, 0.50));
        spinnerWarranty.setEditor(Utilities.getEditorPrice(spinnerWarranty));
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.DATE,1);
        jDateFinish=new JDateChooser(calendar.getTime());
        jDateFinish.setMinSelectableDate(calendar.getTime());
        jDateFinish.setDateFormatString(Utilities.getFormatoFecha());
    }
}
