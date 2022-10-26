package com.babas.views.dialogs;

import com.babas.controllers.Users;
import com.babas.custom.CustomPasswordField;
import com.babas.models.Branch;
import com.babas.models.Permission;
import com.babas.models.Sex;
import com.babas.models.User;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.UtilitiesTables;
import com.babas.utilitiesTables.tablesCellRendered.BranchCellRendered;
import com.babas.utilitiesTables.tablesModels.BranchAbstractModel;
import com.babas.validators.ProgramValidator;
import com.babas.views.frames.FPrincipal;
import com.formdev.flatlaf.extras.components.FlatTable;
import com.moreno.Notify;
import com.toedter.calendar.JDateChooser;
import jakarta.validation.ConstraintViolation;
import org.jdesktop.swingx.JXHyperlink;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

public class DUser extends JDialog{
    private JPanel contentPane;
    private JButton btnHecho;
    private JButton btnSave;
    private JTabbedPane tabbedPane;
    private JTextField txtFirstName;
    private JComboBox cbbSex;
    private JDateChooser jdateBirthday;
    private JTextField txtLastName;
    private JTextField txtNameUser;
    private CustomPasswordField pswPasword1;
    private CustomPasswordField pswPasword2;
    private JButton btnRemoveBranch;
    private FlatTable tableBranchs;
    private FlatTable tableBranchsUser;
    private JButton btnAddBranch;
    private JTextField txtPhone;
    private JCheckBox ckActive;
    private JXHyperlink btnNewSex;
    private JRadioButton rbGroup;
    private JComboBox cbbGroupsPermitions;
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
    private JRadioButton rbPropies;
    private JScrollPane scrooll;
    private User user;
    private boolean update;
    private BranchAbstractModel modelBranchs;
    private BranchAbstractModel modelBranchsUsers;
    private boolean fprincipal;

    public DUser(boolean fprincipal,User user){
        super(Utilities.getJFrame(),"Registrar usuario",true);
        this.user=user;
        this.fprincipal=fprincipal;
        update=user.getId()!=null;
        init();
        btnHecho.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onHecho();
            }
        });
        btnRemoveBranch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeBranch();
            }
        });
        btnAddBranch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addBranch();
            }
        });
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSave();
            }
        });
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
        btnNewSex.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadNewSex();
            }
        });
        rbGroup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verifyPermises();
            }
        });
        rbPropies.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verifyPermises();
            }
        });
    }
    private void verifyPermises(){
        scrooll.setVisible(rbPropies.isSelected());
        cbbGroupsPermitions.setEnabled(rbGroup.isSelected());
    }
    private void loadNewSex(){
        DSex dSex=new DSex(new Sex());
        dSex.setVisible(true);
    }
    private void addBranch(){
        if(tableBranchs.getSelectedRow()!=-1){
            Branch branch=modelBranchs.getList().get(tableBranchs.convertRowIndexToModel(tableBranchs.getSelectedRow()));
            user.getBranchs().add(branch);
            branch.getUsers().add(user);
            modelBranchs.getList().remove(branch);
            modelBranchs.fireTableDataChanged();
            modelBranchsUsers.fireTableDataChanged();
        }else{
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","Debe seleccionar una sucursal");
        }
    }

    private void removeBranch(){
        if(tableBranchsUser.getSelectedRow()!=-1){
            Branch branch=modelBranchsUsers.getList().get(tableBranchsUser.convertRowIndexToModel(tableBranchsUser.getSelectedRow()));
            user.getBranchs().remove(branch);
            branch.getUsers().remove(user);
            modelBranchs.getList().add(branch);
            modelBranchs.fireTableDataChanged();
            modelBranchsUsers.fireTableDataChanged();
        }else{
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","Debe seleccionar una sucursal");
        }
    }
    private void init(){
        setContentPane(contentPane);
        getRootPane().setDefaultButton(btnSave);
        loadCombos();
        scrooll.getVerticalScrollBar().setUnitIncrement(16);
        if(fprincipal){
            tabbedPane.removeTabAt(tabbedPane.indexOfTab("Sucursales"));
        }
        if(update){
            txtNameUser.setEnabled(false);
            setTitle("Actualizar Usuario");
            btnSave.setText("Guardar");
            btnHecho.setText("Cancelar");
        }
        loadTables();
        load();
        pack();
        setResizable(false);
        setLocationRelativeTo(Utilities.getJFrame());
    }

    private void loadCombos(){
        cbbSex.setModel(new DefaultComboBoxModel(FPrincipal.sexs));
        cbbSex.setRenderer(new Sex.ListCellRenderer());
        cbbSex.setSelectedIndex(-1);
        cbbGroupsPermitions.setModel(new DefaultComboBoxModel(FPrincipal.groupPermnitions));
        cbbGroupsPermitions.setRenderer(new Permission.ListCellRenderer());
        cbbSex.setSelectedIndex(-1);
    }

    private void loadTables(){
        modelBranchs=new BranchAbstractModel(new ArrayList<>(FPrincipal.branchs));
        tableBranchs.setModel(modelBranchs);
        BranchCellRendered.setCellRenderer(tableBranchs);
        UtilitiesTables.headerNegrita(tableBranchs);
        tableBranchs.removeColumn(tableBranchs.getColumn("EMAIL"));
        tableBranchs.removeColumn(tableBranchs.getColumn("DIRECCIÓN"));
        tableBranchs.removeColumn(tableBranchs.getColumn("ESTADO"));
        tableBranchs.removeColumn(tableBranchs.getColumn("USUARIOS"));
        tableBranchs.removeColumn(tableBranchs.getColumn(""));
        tableBranchs.removeColumn(tableBranchs.getColumn(""));
    }

    private void load(){
        txtFirstName.setText(user.getFirstName());
        txtLastName.setText(user.getLastName());
        txtPhone.setText(user.getPhone());
        jdateBirthday.setDate(user.getBirthday());
        txtNameUser.setText(user.getUserName());
        pswPasword1.setText(user.getUserPassword());
        pswPasword2.setText(user.getUserPassword());
        cbbSex.setSelectedItem(user.getSex());
        ckActive.setSelected(user.isStaff());
        modelBranchsUsers=new BranchAbstractModel(user.getBranchs());
        tableBranchsUser.setModel(modelBranchsUsers);
        BranchCellRendered.setCellRenderer(tableBranchsUser);
        UtilitiesTables.headerNegrita(tableBranchsUser);
        tableBranchsUser.removeColumn(tableBranchsUser.getColumn("EMAIL"));
        tableBranchsUser.removeColumn(tableBranchsUser.getColumn("DIRECCIÓN"));
        tableBranchsUser.removeColumn(tableBranchsUser.getColumn("ESTADO"));
        tableBranchsUser.removeColumn(tableBranchsUser.getColumn("USUARIOS"));
        tableBranchsUser.removeColumn(tableBranchsUser.getColumn(""));
        tableBranchsUser.removeColumn(tableBranchsUser.getColumn(""));
        loadPermitions();
        user.getBranchs().forEach( branch -> modelBranchs.getList().remove(branch));
        modelBranchs.fireTableDataChanged();
    }
    private void loadPermitions(){
        rbPropies.setSelected(!user.getPermitions().isGroup());
        verifyPermises();
        if(user.getPermitions().isGroup()){
            cbbGroupsPermitions.setSelectedItem(user.getPermitions());
        }else{
            ckNewSale.setSelected(user.getPermitions().isNewSale());
            ckCatalogue.setSelected(user.getPermitions().isShowCatalogue());
            ckRecordSales.setSelected(user.getPermitions().isRecordSales());
            ckNewRental.setSelected(user.getPermitions().isNewRental());
            ckRentalsActives.setSelected(user.getPermitions().isRentalsActives());
            ckRecordRentals.setSelected(user.getPermitions().isRecordRentals());
            ckNewReserve.setSelected(user.getPermitions().isNewReserve());
            ckReservesActives.setSelected(user.getPermitions().isReservesActives());
            ckRecordReserves.setSelected(user.getPermitions().isRecordReserves());
            ckNewTransfer.setSelected(user.getPermitions().isNewTransfer());
            ckRecordTransfers.setSelected(user.getPermitions().isRecordTransfers());
            ckRecordBoxes.setSelected(user.getPermitions().isRecordBoxes());
            ckManageProducts.setSelected(user.getPermitions().isManageProducts());
            ckManageUsers.setSelected(user.getPermitions().isManageUsers());
            ckManageBranchs.setSelected(user.getPermitions().isManageBranchs());
            ckManageCompany.setSelected(user.getPermitions().isManageCompany());
        }
    }
    private void savePermitions(){
        if(user.getPermitions()!=null){
            if(user.getPermitions().isGroup()){
                user.getPermitions().getUsers().remove(user);
            }
        }

        if(rbGroup.isSelected()){
            user.setPermitions((Permission) cbbGroupsPermitions.getSelectedItem());
        }else{
            Permission permission =new Permission();
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
            user.setPermitions(permission);
        }
    }
    private void onSave(){
        user.setFirstName(txtFirstName.getText().trim());
        user.setLastName(txtLastName.getText().trim());
        user.setPhone(txtPhone.getText().trim());
        user.setActive(ckActive.isSelected());
        user.setUserName(txtNameUser.getText().trim());
        user.setBirthday(jdateBirthday.getDate());
        user.setSex((Sex) cbbSex.getSelectedItem());
        user.setStaff(ckActive.isSelected());
        savePermitions();
        if(Arrays.equals(pswPasword2.getPassword(), pswPasword1.getPassword())){
            user.setUserPassword(new String(pswPasword1.getPassword()));
        }else{
            user.setUserPassword(null);
        }
        Set<ConstraintViolation<Object>> constraintViolationSet= ProgramValidator.loadViolations(user);
        if(constraintViolationSet.isEmpty()){
            if(update){
                user.save();
                user.getPermitions().getUsers().add(user);
                Utilities.updateDialog();
                Utilities.getTabbedPane().updateTab();
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER,"ÉXITO","Usuario actualizado");
                onHecho();
            }else{
                if(Users.getByUserName(user.getUserName())==null){
                    user.save();
                    FPrincipal.users.add(user);
                    Utilities.updateDialog();
                    if(Utilities.getTabbedPane()!=null){
                        Utilities.getTabbedPane().updateTab();
                    }else{
                        onHecho();
                    }
                    user=new User();
                    load();
                    Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER,"ÉXITO","Usuario registrado");
                }else{
                    Notify.sendNotify(Utilities.getJFrame(),Notify.Type.WARNING,Notify.Location.TOP_CENTER,"ERROR","Nombre de usuario ya registrado");
                }
            }
        }else{
            ProgramValidator.mostrarErrores(constraintViolationSet);
        }
    }

    private void onHecho(){
        if(update){
            user.refresh();
        }
        dispose();
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        jdateBirthday =new JDateChooser();
        jdateBirthday.setDateFormatString(Utilities.getFormatoFecha());
    }
}
