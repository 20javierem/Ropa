package com.babas.views.tabs;

import com.babas.App;
import com.babas.custom.TabPane;
import com.babas.models.Transfer;
import com.babas.utilitiesTables.UtilitiesTables;
import com.babas.utilitiesTables.tablesCellRendered.DetailTransferCellRendered;
import com.babas.utilitiesTables.tablesModels.DetailTransferAbstractModel;
import com.babas.views.dialogs.DTraslade;
import com.formdev.flatlaf.extras.components.FlatTable;
import com.formdev.flatlaf.extras.components.FlatTextArea;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class TabNewTraslade {
    private TabPane tabPane;
    private JButton btnAddProducts;
    private FlatTable tabla;
    private JLabel lblTotalPesos;
    private JButton btnConfirm;
    private JLabel lblLogo;
    private FlatTextArea txtDescription;
    private JButton btnEdit;
    private JLabel lblSource;
    private JLabel lblDestiny;
    private JLabel lblTypeTraslade;
    private JPanel paneTraslade;
    private JPanel paneEntry;
    private JLabel lblBranch;
    private Transfer transfer;
    private DetailTransferAbstractModel model;

    public TabNewTraslade(Transfer transfer){
        this.transfer=transfer;
        initComponents();
        btnEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadEditTransfer();
            }
        });
    }
    private void loadEditTransfer(){
        DTraslade dTraslade=new DTraslade(transfer,true);
        dTraslade.setVisible(true);
        load();
    }
    private void initComponents(){
        tabPane.setTitle("Nuevo traslado");
        ImageIcon logo=new ImageIcon(new ImageIcon(App.class.getResource("Images/lojoJmoreno (1).png")).getImage().getScaledInstance(255, 220, Image.SCALE_SMOOTH));
        lblLogo.setIcon(logo);
        load();
        loadTable();
    }
    private void load(){
        if(Objects.equals(transfer.getSource().getId(), transfer.getDestiny().getId())){
            paneTraslade.setVisible(false);
            paneEntry.setVisible(true);
            lblTypeTraslade.setText("ENTRADA");
            lblBranch.setText(transfer.getSource().getName());
        }else{
            lblTypeTraslade.setText("TRASLADO");
            lblSource.setText(transfer.getSource().getName());
            lblDestiny.setText(transfer.getDestiny().getName());
            paneTraslade.setVisible(true);
            paneEntry.setVisible(false);
        }
        txtDescription.setText(transfer.getDescription());
    }

    public void loadTable(){
        model=new DetailTransferAbstractModel(transfer.getDetailTransfers());
        tabla.setModel(model);
        DetailTransferCellRendered.setCellRenderer(tabla);
        UtilitiesTables.headerNegrita(tabla);
    }

    public TabPane getTabPane(){
        return tabPane;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
