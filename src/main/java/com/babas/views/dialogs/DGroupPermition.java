package com.babas.views.dialogs;

import com.babas.models.Permission;
import com.babas.utilities.Utilities;
import com.babas.views.frames.FPrincipal;
import com.moreno.Notify;

import javax.swing.*;
import java.awt.event.*;

public class DGroupPermition extends JDialog {
    private JPanel contentPane;
    private JButton btnHecho;
    private JTextField txtName;
    private JButton btnSave;
    private JCheckBox ckNewSale;
    private JCheckBox ckCatalogue;
    private JCheckBox ckRecordSales;
    private JCheckBox ckNewRental;
    private JCheckBox ckRentalsActives;
    private JCheckBox ckRecordRentals;
    private JCheckBox ckNewReserve;
    private JCheckBox ckReservesActives;
    private JCheckBox ckRecordReserves;
    private JCheckBox ckNewTransfer;
    private JCheckBox ckRecordTransfers;
    private JCheckBox ckRecordBoxes;
    private JCheckBox ckManageProducts;
    private JCheckBox ckManageUsers;
    private JCheckBox ckManageBranchs;
    private JCheckBox ckManageCompany;
    private JScrollPane scrool;
    private Permission permission;
    private boolean update;

    public DGroupPermition(Permission permission){
        super(Utilities.getJFrame(),"Nuevo grupo",true);
        this.permission = permission;
        update= permission.getId()!=null;
        init();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onHecho();
            }
        });
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onHecho();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSave();
            }
        });
        btnHecho.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onHecho();
            }
        });
    }

    private void init(){
        setContentPane(contentPane);
        if(update){
            load();
            btnSave.setText("Guardar");
            btnHecho.setText("Cancelar");
        }
        scrool.getVerticalScrollBar().setUnitIncrement(16);
        pack();
        setResizable(false);
        setLocationRelativeTo(getOwner());
    }

    private void load(){
        txtName.setText(permission.getNameGroup());
        ckNewSale.setSelected(permission.isNewSale());
        ckCatalogue.setSelected(permission.isShowCatalogue());
        ckRecordSales.setSelected(permission.isRecordSales());
        ckNewRental.setSelected(permission.isNewRental());
        ckRentalsActives.setSelected(permission.isRentalsActives());
        ckRecordRentals.setSelected(permission.isRecordRentals());
        ckNewReserve.setSelected(permission.isNewReserve());
        ckReservesActives.setSelected(permission.isReservesActives());
        ckRecordReserves.setSelected(permission.isRecordReserves());
        ckNewTransfer.setSelected(permission.isNewTransfer());
        ckRecordTransfers.setSelected(permission.isRecordTransfers());
        ckRecordBoxes.setSelected(permission.isRecordBoxes());
        ckManageProducts.setSelected(permission.isManageProducts());
        ckManageUsers.setSelected(permission.isManageUsers());
        ckManageBranchs.setSelected(permission.isManageBranchs());
        ckManageCompany.setSelected(permission.isManageCompany());
    }
    private void onSave(){
        permission.setGroup(true);
        permission.setNewSale(ckNewSale.isSelected());
        permission.setShowCatalogue(ckCatalogue.isSelected());
        permission.setRecordSales(ckRecordSales.isSelected());
        permission.setNewRental(ckNewRental.isSelected());
        permission.setRentalsActives(ckRentalsActives.isSelected());
        permission.setRecordRentals(ckRecordRentals.isSelected());
        permission.setNewReserve(ckNewReserve.isSelected());
        permission.setReservesActives(ckReservesActives.isSelected());
        permission.setRecordReserves(ckRecordReserves.isSelected());
        permission.setNewTransfer(ckNewTransfer.isSelected());
        permission.setRecordTransfers(ckRecordTransfers.isSelected());
        permission.setRecordBoxes(ckRecordBoxes.isSelected());
        permission.setManageProducts(ckManageProducts.isSelected());
        permission.setManageUsers(ckManageUsers.isSelected());
        permission.setManageBranchs(ckManageBranchs.isSelected());
        permission.setManageCompany(ckManageCompany.isSelected());
        permission.setNameGroup(txtName.getText().trim());
        if(!permission.getNameGroup().isBlank()){
            permission.save();
            if(!update){
                FPrincipal.groupPermnitions.add(permission);
                Utilities.updateDialog();
                Utilities.getTabbedPane().updateTab();
                permission =new Permission();
                load();
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER,"ÉXITO","Grupo registrado");
            }else{
                Utilities.updateDialog();
                Utilities.getTabbedPane().updateTab();
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ÉXITO","Grupo guardados");
                onHecho();
            }
        }else{
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","Verifique el campo: Nombre");
        }


    }
    private void onHecho(){
        if(update){
            permission.refresh();
        }
        dispose();
    }
}
