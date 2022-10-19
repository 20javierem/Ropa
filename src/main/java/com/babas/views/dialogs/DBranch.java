package com.babas.views.dialogs;

import com.babas.models.Box;
import com.babas.models.Branch;
import com.babas.models.User;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.UtilitiesTables;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorBox;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorBrand;
import com.babas.utilitiesTables.tablesCellRendered.ColorCellRendered;
import com.babas.utilitiesTables.tablesCellRendered.UserCellRendered;
import com.babas.utilitiesTables.tablesModels.BoxAbstractModel;
import com.babas.utilitiesTables.tablesModels.UserAbstractModel;
import com.babas.validators.ProgramValidator;
import com.babas.views.frames.FPrincipal;
import com.formdev.flatlaf.extras.components.FlatButton;
import com.formdev.flatlaf.extras.components.FlatTable;
import com.formdev.flatlaf.extras.components.FlatTextField;
import com.moreno.Notify;
import jakarta.validation.ConstraintViolation;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Set;

public class DBranch extends JDialog{
    private JPanel contentPane;
    private FlatButton btnHecho;
    private JTabbedPane tabbedPane1;
    private FlatButton btnSave;
    private FlatTextField txtName;
    private JButton btnRemoveUser;
    private JButton btnAddUser;
    private FlatTable tableUsers;
    private FlatTable tableUserBranch;
    private FlatTextField txtDirection;
    private FlatTextField txtEmail;
    private FlatTextField txtPhone;
    private FlatTable tableBoxs;
    private JButton btnNewBox;
    private Branch branch;
    private boolean update;
    private UserAbstractModel modelUsersBranchs;
    private UserAbstractModel modelUsers;
    private BoxAbstractModel modelBoxs;
    private ActionListener actionListener;

    public DBranch(Branch branch){
        super(Utilities.getJFrame(),"Nueva sucursal",true);
        this.branch=branch;
        update=branch.getId()!=null;
        init();
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
        btnRemoveUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeUser();
            }
        });
        btnAddUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addUser();
            }
        });
        btnNewBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadNewBox();
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
    }
    private void loadNewBox(){
        DBox dBox=new DBox(new Box(branch));
        dBox.setVisible(true);
    }
    private void addUser(){
        if(tableUsers.getSelectedRow()!=-1){
            User user=modelUsers.getList().get(tableUsers.convertRowIndexToModel(tableUsers.getSelectedRow()));
            branch.getUsers().add(user);
            user.getBranchs().add(branch);
            modelUsers.getList().remove(user);
            modelUsers.fireTableDataChanged();
            modelUsersBranchs.fireTableDataChanged();
        }else{
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","Debe seleccionar un usuario");
        }
    }

    private void removeUser(){
        if(tableUserBranch.getSelectedRow()!=-1){
            User user=modelUsersBranchs.getList().get(tableUserBranch.convertRowIndexToModel(tableUserBranch.getSelectedRow()));
            user.getBranchs().remove(branch);
            branch.getUsers().remove(user);
            modelUsers.getList().add(user);
            modelUsers.fireTableDataChanged();
            modelUsersBranchs.fireTableDataChanged();
        }else{
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","Debe seleccionar un usuario");
        }
    }
    private void init(){
        setContentPane(contentPane);
        getRootPane().setDefaultButton(btnSave);
        actionListener= e -> modelBoxs.fireTableDataChanged();
        Utilities.getActionsOfDialog().addActionListener(actionListener);
        if(update){
            setTitle("Actualizar Usuario");
            btnSave.setText("Guardar");
            btnHecho.setText("Cancelar");
        }
        load();
        pack();
        setResizable(false);
        setLocationRelativeTo(Utilities.getJFrame());
    }

    private void load(){
        txtName.setText(branch.getName());
        txtDirection.setText(branch.getDirection());
        txtEmail.setText(branch.getEmail());
        txtPhone.setText(branch.getPhone());
        modelUsersBranchs=new UserAbstractModel(branch.getUsers());
        tableUserBranch.setModel(modelUsersBranchs);
        UserCellRendered.setCellRenderer(tableUserBranch);
        UtilitiesTables.headerNegrita(tableUserBranch);
        tableUserBranch.removeColumn(tableUserBranch.getColumn("CELULAR"));
        tableUserBranch.removeColumn(tableUserBranch.getColumn("ULTIMA SESIÓN"));
        tableUserBranch.removeColumn(tableUserBranch.getColumn("ESTADO"));
        tableUserBranch.removeColumn(tableUserBranch.getColumn("SUCURSALES"));
        tableUserBranch.removeColumn(tableUserBranch.getColumn(""));
        tableUserBranch.removeColumn(tableUserBranch.getColumn(""));
        loadTables();
        branch.getUsers().forEach(user -> modelUsers.getList().remove(user));
        modelUsers.fireTableDataChanged();
    }

    private void loadTables(){
        modelUsers=new UserAbstractModel(new ArrayList<>(FPrincipal.users));
        tableUsers.setModel(modelUsers);
        UserCellRendered.setCellRenderer(tableUsers);
        UtilitiesTables.headerNegrita(tableUsers);
        tableUsers.removeColumn(tableUsers.getColumn("CELULAR"));
        tableUsers.removeColumn(tableUsers.getColumn("ULTIMA SESIÓN"));
        tableUsers.removeColumn(tableUsers.getColumn("ESTADO"));
        tableUsers.removeColumn(tableUsers.getColumn("SUCURSALES"));
        tableUsers.removeColumn(tableUsers.getColumn(""));
        tableUsers.removeColumn(tableUsers.getColumn(""));
        modelBoxs=new BoxAbstractModel(branch.getBoxs());
        tableBoxs.setModel(modelBoxs);

        ColorCellRendered.setCellRenderer(tableBoxs);
        UtilitiesTables.headerNegrita(tableBoxs);
        tableBoxs.getColumnModel().getColumn(modelBoxs.getColumnCount() - 1).setCellEditor(new JButtonEditorBox(false));
        tableBoxs.getColumnModel().getColumn(modelBoxs.getColumnCount() - 2).setCellEditor(new JButtonEditorBox(true));
        tableBoxs.removeColumn(tableBoxs.getColumn("SUCURSAL"));
    }

    private void onSave(){
        branch.setPhone(txtPhone.getText().trim());
        branch.setDirection(txtDirection.getText().trim());
        branch.setEmail(txtEmail.getText().trim());
        branch.setName(txtName.getText().trim());
        branch.setCompany(Babas.company);
        Set<ConstraintViolation<Object>> constraintViolationSet= ProgramValidator.loadViolations(branch);
        if(constraintViolationSet.isEmpty()){
            branch.save();
            if(!update){
                FPrincipal.branchs.add(branch);
                Utilities.updateDialog();
                if(Utilities.getTabbedPane()!=null){
                    Utilities.getTabbedPane().updateTab();
                }else{
                    onHecho();
                }
                branch=new Branch();
                load();
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER,"ÉXITO","Color registrado");
            }else{
                Utilities.updateDialog();
                Utilities.getTabbedPane().updateTab();
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER,"ÉXITO","Color actualizado");
                onHecho();
            }

        }else{
            ProgramValidator.mostrarErrores(constraintViolationSet);
        }
    }

    private void onHecho(){
        if(update){
            branch.refresh();
        }
        Utilities.getActionsOfDialog().removeActionListener(actionListener);
        dispose();
    }
}
