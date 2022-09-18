package com.babas.views.dialogs;

import com.babas.models.Branch;
import com.babas.models.Color;
import com.babas.models.Transfer;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.babas.validators.ProgramValidator;
import com.babas.views.frames.FPrincipal;
import com.babas.views.tabs.TabNewTraslade;
import com.babas.views.tabs.TabRecordTransfers;
import com.formdev.flatlaf.ui.FlatRootPaneUI;
import com.moreno.Notify;
import jakarta.validation.ConstraintViolation;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;
import java.util.Vector;

public class DTraslade extends JDialog {
    private JPanel contentPane;
    private JButton btnHecho;
    private JButton btnSave;
    private JComboBox cbbTipoTransfer;
    private JComboBox cbbBranchSource;
    private JComboBox cbbBranchDestiny;
    private JComboBox cbbBranch;
    private JPanel paneTraslade;
    private JPanel paneEntry;
    private Transfer transfer;
    private Branch oldBranch;
    private boolean edit;

    public DTraslade(Transfer transfer,boolean edit) {
        super(Utilities.getJFrame(), "Nuevo Traslado", true);
        this.transfer = transfer;
        this.edit=edit;
        init();
        if(!edit){
            loadTab();
        }
        cbbTipoTransfer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filter();
            }
        });
        cbbBranchSource.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filerBranchDestiny();
            }
        });
        btnHecho.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onHecho();
            }
        });
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSave();
            }
        });
    }
    private void filerBranchDestiny(){
        if(oldBranch==null){
            oldBranch=(Branch) cbbBranchSource.getSelectedItem();
        }
        if(!transfer.getDetailTransfers().isEmpty()){
            boolean si=JOptionPane.showConfirmDialog(Utilities.getJFrame(),"Esta acción quitará todos los productos agregados","Cambiar origen",JOptionPane.YES_NO_OPTION)==0;
            if(si){
                transfer.getDetailTransfers().clear();
                Utilities.getTabbedPane().updateTab();
                filterComboDestiny();
            }else{
                cbbBranchSource.setSelectedItem(oldBranch);
            }
        }else{
            filterComboDestiny();
        }
    }
    private void filterComboDestiny(){
        cbbBranchDestiny.setModel(new DefaultComboBoxModel(new Vector(FPrincipal.branchs)));
        cbbBranchDestiny.setRenderer(new Branch.ListCellRenderer());
        cbbBranchDestiny.removeItem(cbbBranchSource.getSelectedItem());
        oldBranch= (Branch) cbbBranchSource.getSelectedItem();
    }
    private void filter(){
        switch (cbbTipoTransfer.getSelectedIndex()){
            case 0:
                paneEntry.setVisible(false);
                paneTraslade.setVisible(false);
                pack();
                break;
            case 1:
                paneEntry.setVisible(true);
                paneTraslade.setVisible(false);
                pack();
                break;
            case 2:
                paneEntry.setVisible(false);
                paneTraslade.setVisible(true);
                pack();
                break;
        }
    }
    private void onSave(){
        if(paneTraslade.isVisible()){
            transfer.setSource((Branch) cbbBranchSource.getSelectedItem());
            transfer.setDestiny((Branch) cbbBranchDestiny.getSelectedItem());
        }else if(paneEntry.isVisible()){
            transfer.setSource((Branch) cbbBranch.getSelectedItem());
            transfer.setDestiny((Branch) cbbBranch.getSelectedItem());
        }
        if(transfer.getSource()!=null){
            TabNewTraslade tabNewTraslade=new TabNewTraslade(transfer);
            if(Utilities.getTabbedPane().indexOfTab(tabNewTraslade.getTabPane().getTitle())==-1){
                Utilities.getTabbedPane().addTab(tabNewTraslade.getTabPane().getTitle(), tabNewTraslade.getTabPane().getIcon(), tabNewTraslade.getTabPane());
            }
            Utilities.getTabbedPane().setSelectedIndex(Utilities.getTabbedPane().indexOfTab(tabNewTraslade.getTabPane().getTitle()));
            dispose();
        }else{
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","Debe ingresar origen y destino");
        }
    }

    private void load(){
        if(transfer.getSource().getId().equals(transfer.getDestiny().getId())){
            cbbTipoTransfer.setSelectedIndex(1);
            paneTraslade.setVisible(false);
            paneEntry.setVisible(true);
            cbbBranch.setSelectedItem(transfer.getSource());
        }else{
            cbbTipoTransfer.setSelectedIndex(2);
            paneTraslade.setVisible(true);
            paneEntry.setVisible(false);
            oldBranch=transfer.getSource();
            cbbBranchSource.setSelectedItem(transfer.getSource());
            cbbBranchDestiny.setSelectedItem(transfer.getDestiny());
        }
    }

    private void onHecho(){
        if(transfer.getId()!=null){
            transfer.refresh();
        }
        dispose();
    }
    private void init() {
        setContentPane(contentPane);
        getRootPane().setDefaultButton(btnSave);
        loadCombos();
        filter();
        if(edit){
            load();
            filterComboDestiny();
            pack();
        }
        setLocationRelativeTo(getOwner());
    }

    private void loadTab(){
        if(Utilities.getTabbedPane().indexOfTab("Nuevo traslado")!=-1){
            System.out.println("entra");
            Utilities.getTabbedPane().setSelectedIndex(Utilities.getTabbedPane().indexOfTab("Nuevo traslado"));
            dispose();
        }
    }
    private void loadCombos(){
        cbbBranch.setModel(new DefaultComboBoxModel(new Vector(Babas.user.getBranchs())));
        cbbBranch.setRenderer(new Branch.ListCellRenderer());
        cbbBranchSource.setModel(new DefaultComboBoxModel(new Vector(Babas.user.getBranchs())));
        cbbBranchSource.setRenderer(new Branch.ListCellRenderer());
        cbbBranchDestiny.setModel(new DefaultComboBoxModel(new Vector(FPrincipal.branchs)));
        cbbBranchDestiny.setRenderer(new Branch.ListCellRenderer());
        cbbBranch.setSelectedIndex(-1);
        cbbBranchSource.setSelectedIndex(-1);
        cbbBranchDestiny.setSelectedIndex(-1);
    }

}
