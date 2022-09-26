package com.babas.views.tabs;

import com.babas.App;
import com.babas.custom.TabPane;
import com.babas.models.Branch;
import com.babas.models.Sale;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.UtilitiesTables;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorDetailSale;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorDetailSale2;
import com.babas.utilitiesTables.tablesCellRendered.DetailSaleCellRendered;
import com.babas.utilitiesTables.tablesModels.DetailSaleAbstractModel;
import com.babas.views.dialogs.DaddProductToSale;
import com.formdev.flatlaf.extras.components.FlatTable;
import com.formdev.flatlaf.extras.components.FlatTextField;
import com.moreno.Notify;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class TabNewSale {
    private TabPane tabPane;
    private JButton btnAddProducts;
    private JComboBox cbbBranchs;
    private JLabel lblSubTotal;
    private JLabel lblTotal;
    private JButton btnSaleWithCash;
    private JLabel lblLogo;
    private FlatTextField txtDocument;
    private FlatTextField txtRazonSocial;
    private FlatTextField txtPhone;
    private FlatTextField txtMail;
    private FlatTable table;
    private JLabel lblIgv;
    private Sale sale;
    private DetailSaleAbstractModel model;

    public TabNewSale(){
        init();
        btnAddProducts.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadAddProducts();
            }
        });
        cbbBranchs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterBranch();
            }
        });
    }
    private void filterBranch(){
        if(!sale.getDetailSales().isEmpty()){
            cbbBranchs.setSelectedItem(sale.getBranch());
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.INFO, Notify.Location.TOP_CENTER,"ERROR","Debe quitar los productos primero");
        }else{
            sale.setBranch((Branch) cbbBranchs.getSelectedItem());
        }
    }
    private void loadAddProducts(){
        if(sale.getBranch()!=null){
            DaddProductToSale daddProductToSale=new DaddProductToSale(sale);
            daddProductToSale.setVisible(true);
        }else{
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","Debe seleccionar una sucursal");
        }

    }
    private void init(){
        tabPane.setTitle("Nueva venta");
        sale=new Sale();
        loadCombos();
        sale.setBranch((Branch) cbbBranchs.getSelectedItem());
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
        lblSubTotal.setText(Utilities.moneda.format(sale.getTotal()));
        lblTotal.setText(Utilities.moneda.format(sale.getTotal()));
    }
    private void loadCombos(){
        cbbBranchs.setModel(new DefaultComboBoxModel(new Vector(Babas.user.getBranchs())));
        cbbBranchs.setRenderer(new Branch.ListCellRenderer());
    }

    private void loadTable(){
        model=new DetailSaleAbstractModel(sale.getDetailSales());
        table.setModel(model);
        DetailSaleCellRendered.setCellRenderer(table);
        UtilitiesTables.headerNegrita(table);
        table.getColumnModel().getColumn(model.getColumnCount() - 1).setCellEditor(new JButtonEditorDetailSale2());
        table.getColumnModel().getColumn(model.getColumnCount() - 3).setCellEditor(new JButtonEditorDetailSale(sale,false));
        table.getColumnModel().getColumn(model.getColumnCount() - 4).setCellEditor(new JButtonEditorDetailSale(sale,true));

    }

    public TabPane getTabPane(){
        return tabPane;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
