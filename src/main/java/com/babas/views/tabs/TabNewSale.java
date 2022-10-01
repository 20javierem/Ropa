package com.babas.views.tabs;

import com.babas.App;
import com.babas.controllers.Clients;
import com.babas.custom.TabPane;
import com.babas.models.Client;
import com.babas.models.Sale;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.UtilitiesTables;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorDetailSale;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorDetailSale2;
import com.babas.utilitiesTables.tablesCellRendered.DetailSaleCellRendered;
import com.babas.utilitiesTables.tablesModels.DetailSaleAbstractModel;
import com.babas.validators.ProgramValidator;
import com.babas.views.dialogs.DaddProductToSale;
import com.formdev.flatlaf.extras.components.FlatSpinner;
import com.formdev.flatlaf.extras.components.FlatTable;
import com.formdev.flatlaf.extras.components.FlatTextField;
import com.moreno.Notify;
import jakarta.validation.ConstraintViolation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Set;

public class TabNewSale {
    private TabPane tabPane;
    private JButton btnAddProducts;
    private JLabel lblSubTotal;
    private JLabel lblTotal;
    private JLabel lblLogo;
    private FlatTextField txtDocument;
    private FlatTextField txtNameClient;
    private FlatTextField txtPhone;
    private FlatTextField txtMail;
    private FlatTable table;
    private JButton btnSaleWithCash;
    private JButton btnSaleWithTrasnfer;
    private FlatSpinner spinnerDiscount;
    private JLabel lblDiscount;
    private Sale sale;
    private DetailSaleAbstractModel model;

    public TabNewSale() {
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
        btnSaleWithTrasnfer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSave(false);
            }
        });
        ((JSpinner.NumberEditor)spinnerDiscount.getEditor()).getTextField().addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        JTextField textField = (JTextField) e.getSource();
                        textField.selectAll();
                    }
                });
            }

            @Override
            public void focusLost(FocusEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        sale.setDiscount(-(Double) spinnerDiscount.getValue());
                        loadTotals();
                    }
                });
            }
        });
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
            sale.setBranch(Babas.boxSession.getBox().getBranch());
        }else{
            sale.setBranch(null);
        }
        if(sale.getBranch()!=null){
            DaddProductToSale daddProductToSale=new DaddProductToSale(sale);
            daddProductToSale.setVisible(true);
        }else{
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","Debe abrir caja para comenzar");
        }
    }
    private void init(){
        tabPane.setTitle("Nueva venta");
        sale=new Sale();
        loadTable();
        tabPane.getActions().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.fireTableDataChanged();
                sale.calculateTotal();
                loadTotals();
            }
        });
        ImageIcon logo=new ImageIcon(new ImageIcon(App.class.getResource("images/lojoJmoreno (1).png")).getImage().getScaledInstance(255, 220, Image.SCALE_SMOOTH));
        lblLogo.setIcon(logo);
    }

    private void loadTotals(){
        if(Babas.boxSession.getId()==null){
            sale.getDetailSales().clear();
            sale.setBranch(null);
        }
        lblSubTotal.setText(Utilities.moneda.format(sale.getTotal()));
        lblDiscount.setText(Utilities.moneda.format(sale.getDiscount()));
        lblTotal.setText(Utilities.moneda.format(sale.getTotalCurrent()));
    }

    private void loadTable(){
        model=new DetailSaleAbstractModel(sale.getDetailSales());
        table.setModel(model);
        DetailSaleCellRendered.setCellRenderer(table);
        UtilitiesTables.headerNegrita(table);
        table.getColumnModel().getColumn(model.getColumnCount() - 1).setCellEditor(new JButtonEditorDetailSale2());
        table.getColumnModel().getColumn(model.getColumnCount() - 3).setCellEditor(new JButtonEditorDetailSale());
        table.getColumnModel().getColumn(model.getColumnCount() - 4).setCellEditor(new JButtonEditorDetailSale());

    }

    private void onSave(boolean isCash){
        if(Babas.boxSession.getId()!=null){
            if(getClient()){
                sale.setBranch(Babas.boxSession.getBox().getBranch());
                sale.setCash(isCash);
                sale.setBoxSession(Babas.boxSession);
                sale.setUser(Babas.user);
                Set<ConstraintViolation<Object>> constraintViolationSet= ProgramValidator.loadViolations(sale);
                if(constraintViolationSet.isEmpty()){
                    boolean si=JOptionPane.showConfirmDialog(Utilities.getJFrame(),"¿Está seguro?","Comfirmar venta",JOptionPane.YES_NO_OPTION)==0;
                    if(si){
                        sale.save();
                        Babas.boxSession.getSales().add(0,sale);
                        Babas.boxSession.calculateTotals();
                        sale=new Sale();
                        clear();
                        Utilities.getTabbedPane().updateTab();
                        Utilities.getLblDerecha().setText("Monto caja: "+Utilities.moneda.format(Babas.boxSession.getAmountToDelivered()));
                        Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER,"ÉXITO","Venta registrada");
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
            Client client=Clients.getByDNI(txtDocument.getText().trim());
            if(client==null){
                client=new Client();
                client.setDni(txtDocument.getText().trim());
            }
            client.setNames(txtNameClient.getText().trim());
            client.setMail(txtMail.getText().trim());
            client.setPhone(txtPhone.getText().trim());
            client.save();
            sale.setClient(client);
            return true;
        }
        return txtDocument.getText().trim().isBlank() && txtNameClient.getText().isBlank();
    }
    public TabPane getTabPane(){
        return tabPane;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        spinnerDiscount=new FlatSpinner();
        spinnerDiscount.setModel(new SpinnerNumberModel(0.00, 0.00, 100000.00, 0.50));
        spinnerDiscount.setEditor(Utilities.getEditorPrice(spinnerDiscount));
    }
}
