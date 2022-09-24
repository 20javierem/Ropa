package com.babas.views.tabs;

import com.babas.App;
import com.babas.custom.TabPane;
import com.babas.models.Branch;
import com.babas.models.Sale;
import com.babas.utilities.Babas;
import com.formdev.flatlaf.extras.components.FlatLabel;
import com.formdev.flatlaf.extras.components.FlatTextField;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class TabNewSale {
    private TabPane tabPane;
    private JButton btnAddProducts;
    private JTable tabla;
    private JComboBox cbbBranchs;
    private JComboBox cbbTipoClient;
    private JLabel lblTotalUSD;
    private JLabel lblTotalPesos;
    private JButton btnSaleWithCash;
    private JButton btnSaleWithTransfer;
    private JComboBox cbbVendedor;
    private JLabel lblLogo;
    private FlatTextField txtDocument;
    private FlatTextField txtRazonSocial;
    private Sale sale;

    public TabNewSale(){
        init();
    }

    private void init(){
        tabPane.setTitle("Nueva venta");
        ImageIcon logo=new ImageIcon(new ImageIcon(App.class.getResource("images/lojoJmoreno (1).png")).getImage().getScaledInstance(255, 220, Image.SCALE_SMOOTH));
        lblLogo.setIcon(logo);
        loadCombos();
    }
    private void loadCombos(){
        cbbBranchs.setModel(new DefaultComboBoxModel(new Vector(Babas.user.getBranchs())));
        cbbBranchs.setRenderer(new Branch.ListCellRenderer());
    }
    public TabPane getTabPane(){
        return tabPane;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
