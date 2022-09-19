package com.babas.views.tabs;

import com.babas.App;
import com.babas.custom.TabPane;
import com.babas.models.Branch;
import com.babas.models.Transfer;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.UtilitiesTables;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorBrand;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorDetailTransfer;
import com.babas.utilitiesTables.tablesCellRendered.DetailTransferCellRendered;
import com.babas.utilitiesTables.tablesModels.DetailTransferAbstractModel;
import com.babas.views.dialogs.DAddProductToTransfer;
import com.babas.views.frames.FPrincipal;
import com.formdev.flatlaf.extras.components.FlatTable;
import com.formdev.flatlaf.extras.components.FlatTextArea;
import com.moreno.Notify;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;
import java.util.Vector;

public class TabNewTraslade {
    private TabPane tabPane;
    private JButton btnAddProducts;
    private FlatTable table;
    private JButton btnConfirm;
    private JLabel lblLogo;
    private FlatTextArea txtDescription;
    private JPanel paneTraslade;
    private JPanel paneEntry;
    private JComboBox cbbTypeTransfer;
    private JComboBox cbbBranch;
    private JComboBox cbbBranchSource;
    private JComboBox cbbBranchDestiny;
    private Transfer transfer;
    private DetailTransferAbstractModel model;
    private Branch olBranchSource;
    private int oldIndexType=0;
    private boolean update;

    public TabNewTraslade(Transfer transfer){
        this.transfer=transfer;
        update=transfer.getId()!=null;
        init();
        cbbTypeTransfer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterByType();
            }
        });
        cbbBranchSource.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterBySource();
            }
        });
        btnAddProducts.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadAddProducts();
            }
        });
    }

    private void loadAddProducts(){
        DAddProductToTransfer dAddProductToTransfer;
        switch (cbbTypeTransfer.getSelectedIndex()){
            case 0:
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.INFO, Notify.Location.TOP_CENTER,"MENSAJE","Debe seleccionar el tipo de transferencia");
                break;
            case 1:
                dAddProductToTransfer=new DAddProductToTransfer(transfer,null);
                dAddProductToTransfer.setVisible(true);
                break;
            case 2:
                if(cbbBranchSource.getSelectedIndex()!=-1){
                    dAddProductToTransfer = new DAddProductToTransfer(transfer, (Branch) cbbBranchSource.getSelectedItem());
                    dAddProductToTransfer.setVisible(true);
                }else{
                    Notify.sendNotify(Utilities.getJFrame(), Notify.Type.INFO, Notify.Location.TOP_CENTER,"MENSAJE","Debe seleccionar el origen");
                }
                break;
        }
    }
    private void filterBySource(){
        if(olBranchSource==null){
            olBranchSource= (Branch) cbbBranchSource.getSelectedItem();
        }
        if(!transfer.getDetailTransfers().isEmpty()){
            boolean si=JOptionPane.showConfirmDialog(Utilities.getJFrame(),"Esta acción quitará todos los productos agregados","Cambiar origen",JOptionPane.YES_NO_OPTION)==0;
            if(si){
                transfer.getDetailTransfers().clear();
                filterSource();
                Utilities.getTabbedPane().updateTab();
            }else{
                cbbBranchSource.setSelectedItem(olBranchSource);
            }
        }else{
            filterSource();
        }
    }
    private void filterSource(){
        olBranchSource= (Branch) cbbBranchSource.getSelectedItem();
        cbbBranchDestiny.setModel(new DefaultComboBoxModel(new Vector(FPrincipal.branchs)));
        cbbBranchDestiny.setRenderer(new Branch.ListCellRenderer());
        cbbBranchDestiny.removeItem(olBranchSource);
    }
    private void filterByType(){
        if(!transfer.getDetailTransfers().isEmpty()){
            boolean si=JOptionPane.showConfirmDialog(Utilities.getJFrame(),"Esta acción quitará todos los productos agregados","Cambiar tipo de tralado",JOptionPane.YES_NO_OPTION)==0;
            if(si){
                transfer.getDetailTransfers().clear();
                filterType();
                Utilities.getTabbedPane().updateTab();
            }else{
                cbbTypeTransfer.setSelectedIndex(oldIndexType);
            }
        }else{
            filterType();
        }
    }

    private void filterType(){
        oldIndexType=cbbTypeTransfer.getSelectedIndex();
        switch (cbbTypeTransfer.getSelectedIndex()){
            case 0:
                paneEntry.setVisible(false);
                paneTraslade.setVisible(false);
                break;
            case 1:
                paneEntry.setVisible(true);
                paneTraslade.setVisible(false);
                break;
            case 2:
                paneEntry.setVisible(false);
                paneTraslade.setVisible(true);
                filterSource();
                break;
        }
    }
    private void init(){
        tabPane.setTitle("Nuevo traslado");
        ImageIcon logo=new ImageIcon(new ImageIcon(App.class.getResource("Images/lojoJmoreno (1).png")).getImage().getScaledInstance(255, 220, Image.SCALE_SMOOTH));
        lblLogo.setIcon(logo);
        loadCombos();
        paneTraslade.setVisible(false);
        paneEntry.setVisible(false);
        if(update){
            load();
        }
        loadTable();
        getTabPane().getActions().addActionListener(e -> model.fireTableDataChanged());
    }

    private void loadCombos(){
        cbbBranch.setModel(new DefaultComboBoxModel(new Vector(Babas.user.getBranchs())));
        cbbBranch.setRenderer(new Branch.ListCellRenderer());
        cbbBranchSource.setModel(new DefaultComboBoxModel(new Vector(Babas.user.getBranchs())));
        cbbBranchSource.setRenderer(new Branch.ListCellRenderer());
        cbbBranchDestiny.setModel(new DefaultComboBoxModel(new Vector(FPrincipal.branchs)));
        cbbBranchDestiny.setRenderer(new Branch.ListCellRenderer());
    }

    private void load(){
        if(Objects.equals(transfer.getSource().getId(), transfer.getDestiny().getId())){
            paneTraslade.setVisible(false);
            paneEntry.setVisible(true);
            cbbTypeTransfer.setSelectedIndex(1);
        }else{
            cbbTypeTransfer.setSelectedIndex(2);
            cbbBranchSource.setSelectedItem(transfer.getSource());
            cbbBranchDestiny.setSelectedItem(transfer.getDestiny());
            paneTraslade.setVisible(true);
            paneEntry.setVisible(false);
        }
        txtDescription.setText(transfer.getDescription());
    }

    public void loadTable(){
        model=new DetailTransferAbstractModel(transfer.getDetailTransfers());
        table.setModel(model);
        DetailTransferCellRendered.setCellRenderer(table);
        UtilitiesTables.headerNegrita(table);
        table.getColumnModel().getColumn(model.getColumnCount() - 1).setCellEditor(new JButtonEditorDetailTransfer());
    }

    public TabPane getTabPane(){
        return tabPane;
    }

}
