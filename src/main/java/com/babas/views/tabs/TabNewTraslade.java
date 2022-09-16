package com.babas.views.tabs;

import com.babas.App;
import com.babas.custom.TabPane;
import com.formdev.flatlaf.extras.components.FlatTextArea;

import javax.swing.*;
import java.awt.*;

public class TabNewTraslade {
    private TabPane tabPane;
    private JButton btnAddProducts;
    private JTable tabla;
    private JLabel lblTotalPesos;
    private JButton btnConfirm;
    private JLabel lblLogo;
    private JComboBox cbbTipoTransfer;
    private JComboBox cbbBranchSource;
    private JComboBox cbbBranchDestiny;
    private FlatTextArea txtDescription;

    public TabNewTraslade(){
        initComponents();
    }

    private void initComponents(){
        tabPane.setTitle("Nueva traslado");
        ImageIcon logo=new ImageIcon(new ImageIcon(App.class.getResource("Images/lojoJmoreno (1).png")).getImage().getScaledInstance(255, 220, Image.SCALE_SMOOTH));
        lblLogo.setIcon(logo);
    }
    public TabPane getTabPane(){
        return tabPane;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
