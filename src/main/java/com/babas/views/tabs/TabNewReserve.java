package com.babas.views.tabs;

import com.babas.App;
import com.babas.controllers.Clients;
import com.babas.custom.TabPane;
import com.babas.models.Client;
import com.babas.models.Rental;
import com.babas.models.Reserve;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.babas.utilities.UtilitiesReports;
import com.babas.utilitiesTables.UtilitiesTables;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorDetailReserve;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorDetailReserve2;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorDetailSale;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorDetailSale2;
import com.babas.utilitiesTables.tablesCellRendered.DetailReserveCellRendered;
import com.babas.utilitiesTables.tablesCellRendered.DetailSaleCellRendered;
import com.babas.utilitiesTables.tablesCellRendered.SaleCellRendered;
import com.babas.utilitiesTables.tablesModels.DetailRentalAbstractModel;
import com.babas.utilitiesTables.tablesModels.DetailReserveAbstractModel;
import com.babas.utilitiesTables.tablesModels.DetailSaleAbstractModel;
import com.babas.validators.ProgramValidator;
import com.babas.views.dialogs.DaddProductToRental;
import com.babas.views.dialogs.DaddProductToReserve;
import com.babas.views.frames.FPrincipal;
import com.formdev.flatlaf.extras.components.FlatSpinner;
import com.formdev.flatlaf.extras.components.FlatTable;
import com.formdev.flatlaf.extras.components.FlatTextField;
import com.moreno.Notify;
import com.toedter.calendar.JDateChooser;
import jakarta.validation.ConstraintViolation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import java.util.Set;

public class TabNewReserve {
    private TabPane tabPane;
    private JButton btnAddProducts;
    private FlatTable table;
    private FlatTextField txtDocument;
    private FlatTextField txtNameClient;
    private FlatTextField txtPhone;
    private FlatTextField txtMail;
    private FlatSpinner spinnerAdvance;
    private JLabel lblSubTotal;
    private JLabel lblTotal;
    private JLabel lblAdvance;
    private JButton btnReserveWithTrasnfer;
    private JLabel lblLogo;
    private JButton btnReserveWithCash;
    private JDateChooser jDateReserve;
    private Reserve reserve;
    private DetailReserveAbstractModel model;

    public TabNewReserve(){
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
        ((JSpinner.NumberEditor) spinnerAdvance.getEditor()).getTextField().addFocusListener(new FocusAdapter() {
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
                    reserve.setAdvance((Double) spinnerAdvance.getValue());
                    loadTotals();
                });
            }
        });
        btnReserveWithCash.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSave(true);
            }
        });
        btnReserveWithTrasnfer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSave(false);
            }
        });
    }

    private void init(){
        reserve=new Reserve();
        tabPane.setTitle("Nueva reserva");
        loadTable();
        tabPane.getActions().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.fireTableDataChanged();
                loadTotals();
            }
        });
        if(!Babas.company.getLogo().isBlank()){
            if(Utilities.iconCompany!=null){
                lblLogo.setIcon(Utilities.iconCompany);
            }
        }
    }

    private void searchClient(){
        Client client= Clients.getByDNI(txtDocument.getText().trim());
        if(client!=null){
            txtNameClient.setText(client.getNames());
            txtPhone.setText(client.getPhone());
            txtMail.setText(client.getMail());
        }
    }
    private void loadTotals(){
        if(Babas.boxSession.getId()==null){
            reserve.getDetailReserves().clear();
            reserve.setBranch(null);
        }
        reserve.calculateTotal();
        lblSubTotal.setText(Utilities.moneda.format(reserve.getTotal()));
        lblAdvance.setText(Utilities.moneda.format(reserve.getAdvance()));
        lblTotal.setText(Utilities.moneda.format(reserve.getToCancel()));
        Utilities.getLblCentro().setText("Nueva reserva");
    }
    private void loadTable(){
        model=new DetailReserveAbstractModel(reserve.getDetailReserves());
        table.setModel(model);
        UtilitiesTables.headerNegrita(table);
        DetailReserveCellRendered.setCellRenderer(table);
        table.getColumnModel().getColumn(model.getColumnCount() - 1).setCellEditor(new JButtonEditorDetailReserve2());
        table.getColumnModel().getColumn(model.getColumnCount() - 3).setCellEditor(new JButtonEditorDetailReserve());
        table.getColumnModel().getColumn(model.getColumnCount() - 4).setCellEditor(new JButtonEditorDetailReserve());
    }
    private void loadAddProducts(){
        if(Babas.boxSession.getId()!=null){
            reserve.setBranch(Babas.boxSession.getBox().getBranch());
        }else{
            reserve.setBranch(null);
        }
        if(reserve.getBranch()!=null){
            DaddProductToReserve daddProductToReserve =new DaddProductToReserve(reserve);
            daddProductToReserve.setVisible(true);
        }else{
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","Debe abrir caja para comenzar");
        }
    }
    public TabPane getTabPane() {
        return tabPane;
    }

    private void onSave(boolean isCash){
        if(Babas.boxSession.getId()!=null){
            reserve.setClient(getClient());
            reserve.setBranch(Babas.boxSession.getBox().getBranch());
            reserve.setCash(isCash);
            reserve.setBoxSession(Babas.boxSession);
            reserve.setUser(Babas.user);
            reserve.setStarted(jDateReserve.getDate());
            Set<ConstraintViolation<Object>> constraintViolationSet= ProgramValidator.loadViolations(reserve);
            if(constraintViolationSet.isEmpty()){
                boolean si=JOptionPane.showConfirmDialog(Utilities.getJFrame(),"¿Está seguro?","Comfirmar Reserva",JOptionPane.YES_NO_OPTION)==0;
                if(si){
                    reserve.save();
                    FPrincipal.reservesActives.add(reserve);
                    Babas.boxSession.getReserves().add(0,reserve);
                    Babas.boxSession.calculateTotals();
                    Utilities.getLblIzquierda().setText("Reserva registrada Nro. "+reserve.getNumberReserve()+" :"+Utilities.formatoFechaHora.format(reserve.getCreated()));
                    Utilities.getLblDerecha().setText("Monto caja: "+Utilities.moneda.format(Babas.boxSession.getAmountToDelivered()));
                    Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER,"ÉXITO","Reserva registrada");
                    if(Utilities.propiedades.getPrintTicketReserve().equals("always")){
                        UtilitiesReports.generateTicketReserve(reserve,true);
                    }else if(Utilities.propiedades.getPrintTicketReserve().equals("question")){
                        si = JOptionPane.showConfirmDialog(Utilities.getJFrame(), "¿Imprimir?", "Ticket de reserva", JOptionPane.YES_NO_OPTION) == 0;
                        if(si){
                            UtilitiesReports.generateTicketReserve(reserve,true);
                        }
                    }
                    reserve=new Reserve();
                    clear();
                    Utilities.getTabbedPane().updateTab();
                }
            }else{
                ProgramValidator.mostrarErrores(constraintViolationSet);
            }
        }else{
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","Debe aperturar caja");
        }
    }
    private Client getClient(){
        Client client = null;
        if(!txtDocument.getText().isBlank()&&!txtNameClient.getText().isBlank()){
            client= Clients.getByDNI(txtDocument.getText().trim());
            if(client==null){
                client=new Client();
                client.setDni(txtDocument.getText().trim());
            }
            client.setNames(txtNameClient.getText().trim());
            client.setMail(txtMail.getText().trim());
            client.setPhone(txtPhone.getText().trim());
            client.save();
        }
        return client;
    }
    private void clear(){
        txtMail.setText(null);
        txtDocument.setText(null);
        txtPhone.setText(null);
        txtNameClient.setText(null);
        spinnerAdvance.setValue(0.0);
        spinnerAdvance.setValue(0.0);
        loadTable();
        loadTotals();
    }
    private void createUIComponents() {
        // TODO: place custom component creation code here
        spinnerAdvance =new FlatSpinner();
        spinnerAdvance.setModel(new SpinnerNumberModel(0.00, 0.00, 100000.00, 0.50));
        spinnerAdvance.setEditor(Utilities.getEditorPrice(spinnerAdvance));
        jDateReserve =new JDateChooser();
        jDateReserve.setMinSelectableDate(new Date());
        jDateReserve.setDateFormatString(Utilities.getFormatoFecha());
    }
}
