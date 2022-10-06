package com.babas.views.tabs;

import com.babas.App;
import com.babas.custom.TabPane;
import com.babas.utilities.Utilities;
import com.formdev.flatlaf.extras.components.FlatSpinner;
import com.formdev.flatlaf.extras.components.FlatTable;
import com.formdev.flatlaf.extras.components.FlatTextField;

import javax.swing.*;
import java.awt.*;

public class TabNewReserve {
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

    public TabNewReserve(){
        init();
    }
    private void init(){
        tabPane.setTitle("Nueva reserva");
        ImageIcon logo=new ImageIcon(new ImageIcon(App.class.getResource("images/lojoJmoreno (1).png")).getImage().getScaledInstance(255, 220, Image.SCALE_SMOOTH));
        lblLogo.setIcon(logo);
    }

    public TabPane getTabPane() {
        return tabPane;
    }
    private void createUIComponents() {
        // TODO: place custom component creation code here
        spinnerDiscount=new FlatSpinner();
        spinnerDiscount.setModel(new SpinnerNumberModel(0.00, 0.00, 100000.00, 0.50));
        spinnerDiscount.setEditor(Utilities.getEditorPrice(spinnerDiscount));
    }
}