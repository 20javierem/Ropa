package com.babas.views.dialogs;

import com.babas.custom.CustomPasswordField;
import com.babas.models.Branch;
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

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
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
    private User user;
    private boolean update;
    private BranchAbstractModel modelBranchs;
    private BranchAbstractModel modelBranchsUsers;

    public DUser(User user){
        super(Utilities.getJFrame(),true);
        this.user=user;
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
    }

    private void addBranch(){
        if(tableBranchs.getSelectedRow()!=-1){
            Branch branch=modelBranchs.getList().get(tableBranchs.convertRowIndexToModel(tableBranchs.getSelectedRow()));
            user.getBranchs().add(branch);
            branch.getUsers().add(user);
            modelBranchs.getList().remove(branch);
            UtilitiesTables.actualizarTabla(tableBranchs);
            UtilitiesTables.actualizarTabla(tableBranchsUser);
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
            UtilitiesTables.actualizarTabla(tableBranchs);
            UtilitiesTables.actualizarTabla(tableBranchsUser);
        }else{
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","Debe seleccionar una sucursal");
        }
    }
    private void init(){
        setContentPane(contentPane);
        getRootPane().setDefaultButton(btnSave);
        loadCombos();
        if(update){
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
        cbbSex.setSelectedItem(user.getSex());
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

        user.getBranchs().forEach( branch -> modelBranchs.getList().remove(branch));
        UtilitiesTables.actualizarTabla(tableBranchs);
    }

    private void onSave(){
        user.setFirstName(txtFirstName.getText().trim());
        user.setLastName(txtLastName.getText().trim());
        user.setPhone(txtPhone.getText().trim());
        user.setActive(ckActive.isSelected());
        user.setUserName(txtNameUser.getText().trim());
        user.setBirthday(jdateBirthday.getDate());
        user.setSex((Sex) cbbSex.getSelectedItem());
        user.setUserPassword(new String(pswPasword1.getPassword()));
        Set<ConstraintViolation<Object>> constraintViolationSet= ProgramValidator.loadViolations(user);
        if(constraintViolationSet.isEmpty()){
            user.save();
            if(!update){
                FPrincipal.users.add(user);
                Utilities.updateDialog();
                Utilities.getTabbedPane().updateTab();
                user=new User();
                load();
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER,"ÉXITO","Usuario registrado");
            }else{
                Utilities.updateDialog();
                Utilities.getTabbedPane().updateTab();
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER,"ÉXITO","Usuario actualizado");
                onHecho();
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
