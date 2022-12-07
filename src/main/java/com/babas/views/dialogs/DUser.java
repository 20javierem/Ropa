package com.babas.views.dialogs;

import com.babas.controllers.Permissions;
import com.babas.controllers.Sexs;
import com.babas.controllers.Users;
import com.babas.custom.CustomPasswordField;
import com.babas.models.Branch;
import com.babas.models.Permission;
import com.babas.models.Sex;
import com.babas.models.User;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.UtilitiesTables;
import com.babas.utilitiesTables.tablesCellRendered.BranchCellRendered;
import com.babas.utilitiesTables.tablesModels.BranchAbstractModel;
import com.babas.validators.ProgramValidator;
import com.babas.views.frames.FPrincipal;
import com.formdev.flatlaf.extras.components.FlatTable;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.moreno.Notify;
import com.toedter.calendar.JDateChooser;
import jakarta.validation.ConstraintViolation;
import org.jdesktop.swingx.JXHyperlink;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Set;

public class DUser extends JDialog {
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
    private JCheckBox ckAceptTransfer;
    private JLabel activoLabel;
    private JCheckBox ckManageClients;
    private JCheckBox ckNewQuotation;
    private JCheckBox ckRecordQuotations;
    private User user;
    private boolean update;
    private BranchAbstractModel modelBranchs;
    private BranchAbstractModel modelBranchsUsers;
    private boolean fprincipal;

    public DUser(boolean fprincipal, User user) {
        super(Utilities.getJFrame(), "Registrar usuario", true);
        this.user = user;
        this.fprincipal = fprincipal;
        $$$setupUI$$$();
        update = user.getId() != null;
        init();
        btnHecho.addActionListener(e -> onHecho());
        btnRemoveBranch.addActionListener(e -> removeBranch());
        btnAddBranch.addActionListener(e -> addBranch());
        btnSave.addActionListener(e -> onSave());
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onHecho();
            }
        });
        contentPane.registerKeyboardAction(e -> onHecho(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        btnNewSex.addActionListener(e -> loadNewSex());
        rbGroup.addActionListener(e -> verifyPermises());
        rbPropies.addActionListener(e -> verifyPermises());
    }

    private void verifyPermises() {
        scrooll.setVisible(rbPropies.isSelected());
        cbbGroupsPermitions.setEnabled(rbGroup.isSelected());
    }

    private void loadNewSex() {
        DSex dSex = new DSex(new Sex());
        dSex.setVisible(true);
        if (cbbSex.getItemCount() > 0) {
            cbbSex.setSelectedItem(0);
        }
    }

    private void addBranch() {
        if (tableBranchs.getSelectedRow() != -1) {
            Branch branch = modelBranchs.getList().get(tableBranchs.convertRowIndexToModel(tableBranchs.getSelectedRow()));
            user.getBranchs().add(branch);
            branch.getUsers().add(user);
            modelBranchs.getList().remove(branch);
            modelBranchs.fireTableDataChanged();
            modelBranchsUsers.fireTableDataChanged();
        } else {
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER, "ERROR", "Debe seleccionar una sucursal");
        }
    }

    private void removeBranch() {
        if (tableBranchsUser.getSelectedRow() != -1) {
            Branch branch = modelBranchsUsers.getList().get(tableBranchsUser.convertRowIndexToModel(tableBranchsUser.getSelectedRow()));
            user.getBranchs().remove(branch);
            branch.getUsers().remove(user);
            modelBranchs.getList().add(branch);
            modelBranchs.fireTableDataChanged();
            modelBranchsUsers.fireTableDataChanged();
        } else {
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER, "ERROR", "Debe seleccionar una sucursal");
        }
    }

    private void init() {
        setContentPane(contentPane);
        getRootPane().setDefaultButton(btnSave);
        loadCombos();
        scrooll.getVerticalScrollBar().setUnitIncrement(16);
        if (fprincipal) {
            tabbedPane.removeTabAt(tabbedPane.indexOfTab("Sucursales"));
            tabbedPane.removeTabAt(tabbedPane.indexOfTab("Permisos"));
            ckActive.setVisible(false);
            activoLabel.setVisible(false);
        }
        if (update) {
            txtNameUser.setEnabled(false);
            setTitle("Actualizar Usuario");
            btnSave.setText("Guardar");
            btnHecho.setText("Cancelar");
            if (user.getId() == 1) {
                if (tabbedPane.indexOfTab("Permisos") != -1) {
                    tabbedPane.remove(tabbedPane.indexOfTab("Permisos"));
                }
            }
        }
        loadTables();
        load();
        pack();
        setResizable(false);
        setLocationRelativeTo(Utilities.getJFrame());
    }

    private void loadCombos() {
        if (FPrincipal.sexs.isEmpty()) {
            FPrincipal.sexs = Sexs.getActives();
        }
        cbbSex.setModel(new DefaultComboBoxModel(FPrincipal.sexs));
        cbbSex.setRenderer(new Sex.ListCellRenderer());
        cbbSex.setSelectedIndex(-1);
        cbbGroupsPermitions.setModel(new DefaultComboBoxModel(FPrincipal.groupPermnitions));
        cbbGroupsPermitions.setRenderer(new Permission.ListCellRenderer());
        cbbSex.setSelectedIndex(-1);
    }

    private void loadTables() {
        modelBranchs = new BranchAbstractModel(new ArrayList<>(FPrincipal.branchs));
        tableBranchs.setModel(modelBranchs);
        BranchCellRendered.setCellRenderer(tableBranchs, null);
        UtilitiesTables.headerNegrita(tableBranchs);
        tableBranchs.removeColumn(tableBranchs.getColumn("EMAIL"));
        tableBranchs.removeColumn(tableBranchs.getColumn("DIRECCIÓN"));
        tableBranchs.removeColumn(tableBranchs.getColumn("ESTADO"));
        tableBranchs.removeColumn(tableBranchs.getColumn("USUARIOS"));
        tableBranchs.removeColumn(tableBranchs.getColumn(""));
        tableBranchs.removeColumn(tableBranchs.getColumn(""));
    }

    private void load() {
        txtFirstName.setText(user.getFirstName());
        txtLastName.setText(user.getLastName());
        txtPhone.setText(user.getPhone());
        jdateBirthday.setDate(user.getBirthday());
        txtNameUser.setText(user.getUserName());
        pswPasword1.setText(user.getUserPassword());
        pswPasword2.setText(user.getUserPassword());
        cbbSex.setSelectedItem(user.getSex());
        ckActive.setSelected(user.isStaff());
        modelBranchsUsers = new BranchAbstractModel(user.getBranchs());
        tableBranchsUser.setModel(modelBranchsUsers);
        BranchCellRendered.setCellRenderer(tableBranchsUser, null);
        UtilitiesTables.headerNegrita(tableBranchsUser);
        tableBranchsUser.removeColumn(tableBranchsUser.getColumn("EMAIL"));
        tableBranchsUser.removeColumn(tableBranchsUser.getColumn("DIRECCIÓN"));
        tableBranchsUser.removeColumn(tableBranchsUser.getColumn("ESTADO"));
        tableBranchsUser.removeColumn(tableBranchsUser.getColumn("USUARIOS"));
        tableBranchsUser.removeColumn(tableBranchsUser.getColumn(""));
        tableBranchsUser.removeColumn(tableBranchsUser.getColumn(""));
        loadPermitions();
        user.getBranchs().forEach(branch -> modelBranchs.getList().remove(branch));
        modelBranchs.fireTableDataChanged();
    }

    private void loadPermitions() {
        rbGroup.setSelected(user.isGroupDefault());
        verifyPermises();
        if (user.isGroupDefault()) {
            cbbGroupsPermitions.setSelectedItem(user.getPermissionGroup());
        }
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
        ckAceptTransfer.setSelected(user.getPermitions().isAceptTransfer());
        ckNewQuotation.setSelected(user.getPermitions().isNewQuotation());
        ckRecordQuotations.setSelected(user.getPermitions().isRecordQuotations());
        ckManageClients.setSelected(user.getPermitions().isManageClients());
    }

    private void savePermitions() {
        if (rbGroup.isSelected()) {
            Permission permission = (Permission) cbbGroupsPermitions.getSelectedItem();
            if (permission != null) {
                user.setGroupDefault(true);
                user.setPermissionGroup(permission);
                user.getPermissionGroup().getUsers().remove(user);
                user.getPermissionGroup().getUsers().add(user);
            } else {
                user.setGroupDefault(false);
            }
        } else {
            user.setGroupDefault(false);
        }
        if (!user.isGroupDefault()) {
            if (user.getPermissionGroup() != null) {
                user.getPermissionGroup().getUsers().remove(user);
                user.setPermissionGroup(null);
            }
            user.getPermitions().setNewSale(ckNewSale.isSelected());
            user.getPermitions().setShowCatalogue(ckCatalogue.isSelected());
            user.getPermitions().setRecordSales(ckRecordSales.isSelected());
            user.getPermitions().setNewRental(ckNewRental.isSelected());
            user.getPermitions().setRentalsActives(ckRentalsActives.isSelected());
            user.getPermitions().setRecordRentals(ckRecordRentals.isSelected());
            user.getPermitions().setNewReserve(ckNewReserve.isSelected());
            user.getPermitions().setReservesActives(ckReservesActives.isSelected());
            user.getPermitions().setRecordReserves(ckRecordReserves.isSelected());
            user.getPermitions().setNewTransfer(ckNewTransfer.isSelected());
            user.getPermitions().setRecordTransfers(ckRecordTransfers.isSelected());
            user.getPermitions().setRecordBoxes(ckRecordBoxes.isSelected());
            user.getPermitions().setManageProducts(ckManageProducts.isSelected());
            user.getPermitions().setManageUsers(ckManageUsers.isSelected());
            user.getPermitions().setManageBranchs(ckManageBranchs.isSelected());
            user.getPermitions().setManageCompany(ckManageCompany.isSelected());
            user.getPermitions().setAceptTransfer(ckAceptTransfer.isSelected());
            user.getPermitions().setManageClients(ckManageClients.isSelected());
            user.getPermitions().setNewQuotation(ckNewQuotation.isSelected());
            user.getPermitions().setRecordQuotations(ckRecordQuotations.isSelected());
        }
    }

    private void onSave() {
        user.setFirstName(txtFirstName.getText().trim());
        user.setLastName(txtLastName.getText().trim());
        user.setPhone(txtPhone.getText().trim());
        user.setActive(ckActive.isSelected());
        user.setUserName(txtNameUser.getText().trim());
        user.setBirthday(jdateBirthday.getDate());
        user.setSex((Sex) cbbSex.getSelectedItem());
        user.setStaff(ckActive.isSelected());
        savePermitions();
        if (Arrays.equals(pswPasword2.getPassword(), pswPasword1.getPassword())) {
            user.setUserPassword(new String(pswPasword1.getPassword()));
        } else {
            user.setUserPassword(null);
        }
        Set<ConstraintViolation<Object>> constraintViolationSet = ProgramValidator.loadViolations(user);
        if (constraintViolationSet.isEmpty()) {
            if (update) {
                user.save();
                Utilities.updateDialog();
                Utilities.getTabbedPane().updateTab();
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER, "ÉXITO", "Usuario actualizado");
                onHecho();
            } else {
                if (Users.getByUserName(user.getUserName()) == null) {
                    user.save();
                    FPrincipal.users.add(user);
                    Utilities.updateDialog();
                    if (Utilities.getTabbedPane() != null) {
                        Utilities.getTabbedPane().updateTab();
                    } else {
                        onHecho();
                    }
                    user = new User();
                    load();
                    if (Utilities.getJFrame() != null&&Utilities.getJFrame() instanceof FPrincipal) {
                        Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER, "ÉXITO", "Usuario registrado");
                    }
                } else {
                    Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER, "ERROR", "Nombre de usuario ya registrado");
                }
            }
        } else {
            ProgramValidator.mostrarErrores(constraintViolationSet);
        }
    }

    private void onHecho() {
        if (update) {
            user.refresh();
        }
        dispose();
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        jdateBirthday = new JDateChooser();
        jdateBirthday.setDateFormatString(Utilities.getFormatoFecha());
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), 10, 10));
        contentPane.setPreferredSize(new Dimension(550, 448));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        tabbedPane = new JTabbedPane();
        panel1.add(tabbedPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(10, 3, new Insets(10, 10, 10, 10), 5, 5));
        tabbedPane.addTab("Datos", panel2);
        final JLabel label1 = new JLabel();
        label1.setText("Nombres:");
        panel2.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel2.add(spacer1, new GridConstraints(9, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Apellidos:");
        panel2.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtFirstName = new JTextField();
        panel2.add(txtFirstName, new GridConstraints(0, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        txtLastName = new JTextField();
        panel2.add(txtLastName, new GridConstraints(1, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Sexo:");
        panel2.add(label3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbbSex = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        cbbSex.setModel(defaultComboBoxModel1);
        panel2.add(cbbSex, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("F. Nacimiento:");
        panel2.add(label4, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panel2.add(jdateBirthday, new GridConstraints(3, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("N. Usuario:");
        panel2.add(label5, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("Contraseña:");
        panel2.add(label6, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtNameUser = new JTextField();
        panel2.add(txtNameUser, new GridConstraints(5, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        pswPasword1 = new CustomPasswordField();
        panel2.add(pswPasword1, new GridConstraints(6, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("Contraseña:");
        panel2.add(label7, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        pswPasword2 = new CustomPasswordField();
        panel2.add(pswPasword2, new GridConstraints(7, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label8 = new JLabel();
        label8.setText("Nro. Celular:");
        panel2.add(label8, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtPhone = new JTextField();
        panel2.add(txtPhone, new GridConstraints(4, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        activoLabel = new JLabel();
        activoLabel.setText("Activo:");
        panel2.add(activoLabel, new GridConstraints(8, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ckActive = new JCheckBox();
        ckActive.setText("");
        panel2.add(ckActive, new GridConstraints(8, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnNewSex = new JXHyperlink();
        btnNewSex.setText("[+]");
        panel2.add(btnNewSex, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, 1, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(2, 3, new Insets(5, 5, 5, 5), 0, 10));
        tabbedPane.addTab("Sucursales", panel3);
        final JScrollPane scrollPane1 = new JScrollPane();
        panel3.add(scrollPane1, new GridConstraints(0, 0, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        scrollPane1.setBorder(BorderFactory.createTitledBorder(null, "Sucursales", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, null, null));
        tableBranchs = new FlatTable();
        scrollPane1.setViewportView(tableBranchs);
        final JScrollPane scrollPane2 = new JScrollPane();
        panel3.add(scrollPane2, new GridConstraints(0, 2, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        scrollPane2.setBorder(BorderFactory.createTitledBorder(null, "Sucursales del usuario", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, null, null));
        tableBranchsUser = new FlatTable();
        scrollPane2.setViewportView(tableBranchsUser);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(2, 1, new Insets(2, 2, 2, 2), -1, 1));
        panel3.add(panel4, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnRemoveBranch = new JButton();
        btnRemoveBranch.setIcon(new ImageIcon(getClass().getResource("/com/babas/icons/x32/flechaLeft.png")));
        panel4.add(btnRemoveBranch, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(42, 35), new Dimension(42, 35), new Dimension(42, 35), 0, false));
        btnAddBranch = new JButton();
        btnAddBranch.setIcon(new ImageIcon(getClass().getResource("/com/babas/icons/x32/flechaRigth.png")));
        panel4.add(btnAddBranch, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(42, 35), new Dimension(42, 35), new Dimension(42, 35), 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(4, 3, new Insets(5, 5, 5, 5), 5, 5));
        tabbedPane.addTab("Permisos", panel5);
        final JLabel label9 = new JLabel();
        label9.setText("De un grupo:");
        panel5.add(label9, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label10 = new JLabel();
        label10.setText("Propios:");
        panel5.add(label10, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        rbGroup = new JRadioButton();
        rbGroup.setText("");
        panel5.add(rbGroup, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        rbPropies = new JRadioButton();
        rbPropies.setSelected(true);
        rbPropies.setText("");
        panel5.add(rbPropies, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbbGroupsPermitions = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel2 = new DefaultComboBoxModel();
        cbbGroupsPermitions.setModel(defaultComboBoxModel2);
        panel5.add(cbbGroupsPermitions, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        scrooll = new JScrollPane();
        panel5.add(scrooll, new GridConstraints(2, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(32, 3, new Insets(0, 0, 0, 0), -1, -1));
        scrooll.setViewportView(panel6);
        final JLabel label11 = new JLabel();
        label11.setText("Nueva venta:");
        panel6.add(label11, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel6.add(spacer2, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel6.add(spacer3, new GridConstraints(31, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        ckNewSale = new JCheckBox();
        ckNewSale.setText("");
        panel6.add(ckNewSale, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label12 = new JLabel();
        label12.setText("Catálogo:");
        panel6.add(label12, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label13 = new JLabel();
        label13.setText("Historial de ventas:");
        panel6.add(label13, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ckCatalogue = new JCheckBox();
        ckCatalogue.setText("");
        panel6.add(ckCatalogue, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ckRecordSales = new JCheckBox();
        ckRecordSales.setText("");
        panel6.add(ckRecordSales, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JSeparator separator1 = new JSeparator();
        panel6.add(separator1, new GridConstraints(4, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(-1, 5), null, 0, false));
        final JLabel label14 = new JLabel();
        Font label14Font = this.$$$getFont$$$(null, Font.BOLD, -1, label14.getFont());
        if (label14Font != null) label14.setFont(label14Font);
        label14.setText("Ventas");
        panel6.add(label14, new GridConstraints(0, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label15 = new JLabel();
        Font label15Font = this.$$$getFont$$$(null, Font.BOLD, -1, label15.getFont());
        if (label15Font != null) label15.setFont(label15Font);
        label15.setText("Alquileres");
        panel6.add(label15, new GridConstraints(5, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label16 = new JLabel();
        label16.setText("Nuevo alquiler:");
        panel6.add(label16, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label17 = new JLabel();
        label17.setText("Alquileres activos:");
        panel6.add(label17, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label18 = new JLabel();
        label18.setText("Historial de alquileres:");
        panel6.add(label18, new GridConstraints(8, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ckNewRental = new JCheckBox();
        ckNewRental.setText("");
        panel6.add(ckNewRental, new GridConstraints(6, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ckRentalsActives = new JCheckBox();
        ckRentalsActives.setText("");
        panel6.add(ckRentalsActives, new GridConstraints(7, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ckRecordRentals = new JCheckBox();
        ckRecordRentals.setText("");
        panel6.add(ckRecordRentals, new GridConstraints(8, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JSeparator separator2 = new JSeparator();
        panel6.add(separator2, new GridConstraints(9, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(-1, 5), null, 0, false));
        final JLabel label19 = new JLabel();
        Font label19Font = this.$$$getFont$$$(null, Font.BOLD, -1, label19.getFont());
        if (label19Font != null) label19.setFont(label19Font);
        label19.setText("Reservas");
        panel6.add(label19, new GridConstraints(14, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label20 = new JLabel();
        label20.setText("Nueva reserva:");
        panel6.add(label20, new GridConstraints(15, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label21 = new JLabel();
        label21.setText("Reservas activas:");
        panel6.add(label21, new GridConstraints(16, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label22 = new JLabel();
        label22.setText("Historial de reservas:");
        panel6.add(label22, new GridConstraints(17, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ckNewReserve = new JCheckBox();
        ckNewReserve.setText("");
        panel6.add(ckNewReserve, new GridConstraints(15, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ckReservesActives = new JCheckBox();
        ckReservesActives.setText("");
        panel6.add(ckReservesActives, new GridConstraints(16, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ckRecordReserves = new JCheckBox();
        ckRecordReserves.setText("");
        panel6.add(ckRecordReserves, new GridConstraints(17, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JSeparator separator3 = new JSeparator();
        panel6.add(separator3, new GridConstraints(18, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(-1, 5), null, 0, false));
        final JLabel label23 = new JLabel();
        Font label23Font = this.$$$getFont$$$(null, Font.BOLD, -1, label23.getFont());
        if (label23Font != null) label23.setFont(label23Font);
        label23.setText("Traslados");
        panel6.add(label23, new GridConstraints(19, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label24 = new JLabel();
        label24.setText("Nuevo traslado:");
        panel6.add(label24, new GridConstraints(20, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ckNewTransfer = new JCheckBox();
        ckNewTransfer.setText("");
        panel6.add(ckNewTransfer, new GridConstraints(20, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label25 = new JLabel();
        label25.setText("Historial de traslados:");
        panel6.add(label25, new GridConstraints(21, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label26 = new JLabel();
        Font label26Font = this.$$$getFont$$$(null, Font.BOLD, -1, label26.getFont());
        if (label26Font != null) label26.setFont(label26Font);
        label26.setText("Otros");
        panel6.add(label26, new GridConstraints(24, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ckRecordTransfers = new JCheckBox();
        ckRecordTransfers.setText("");
        panel6.add(ckRecordTransfers, new GridConstraints(21, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label27 = new JLabel();
        label27.setText("Historial de cajas:");
        panel6.add(label27, new GridConstraints(25, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label28 = new JLabel();
        label28.setText("Gestionar productos:");
        panel6.add(label28, new GridConstraints(26, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label29 = new JLabel();
        label29.setText("Gestionar usuarios:");
        panel6.add(label29, new GridConstraints(27, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label30 = new JLabel();
        label30.setText("Gestionar sucursales:");
        panel6.add(label30, new GridConstraints(29, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label31 = new JLabel();
        label31.setText("Gestionar empresa:");
        panel6.add(label31, new GridConstraints(30, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ckRecordBoxes = new JCheckBox();
        ckRecordBoxes.setText("");
        panel6.add(ckRecordBoxes, new GridConstraints(25, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ckManageProducts = new JCheckBox();
        ckManageProducts.setText("");
        panel6.add(ckManageProducts, new GridConstraints(26, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ckManageUsers = new JCheckBox();
        ckManageUsers.setText("");
        panel6.add(ckManageUsers, new GridConstraints(27, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ckManageBranchs = new JCheckBox();
        ckManageBranchs.setText("");
        panel6.add(ckManageBranchs, new GridConstraints(29, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ckManageCompany = new JCheckBox();
        ckManageCompany.setText("");
        panel6.add(ckManageCompany, new GridConstraints(30, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JSeparator separator4 = new JSeparator();
        panel6.add(separator4, new GridConstraints(23, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(-1, 5), null, 0, false));
        final JLabel label32 = new JLabel();
        label32.setText("Aceptar traslados:");
        panel6.add(label32, new GridConstraints(22, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ckAceptTransfer = new JCheckBox();
        ckAceptTransfer.setText("");
        panel6.add(ckAceptTransfer, new GridConstraints(22, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label33 = new JLabel();
        label33.setText("Gestionar clientes:");
        panel6.add(label33, new GridConstraints(28, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ckManageClients = new JCheckBox();
        ckManageClients.setText("");
        panel6.add(ckManageClients, new GridConstraints(28, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label34 = new JLabel();
        Font label34Font = this.$$$getFont$$$(null, Font.BOLD, -1, label34.getFont());
        if (label34Font != null) label34.setFont(label34Font);
        label34.setText("Cotizaciones");
        panel6.add(label34, new GridConstraints(10, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JSeparator separator5 = new JSeparator();
        panel6.add(separator5, new GridConstraints(13, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(-1, 5), null, 0, false));
        final JLabel label35 = new JLabel();
        label35.setText("Nueva cotización:");
        panel6.add(label35, new GridConstraints(11, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label36 = new JLabel();
        label36.setText("Historial de cotizaciones:");
        panel6.add(label36, new GridConstraints(12, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ckNewQuotation = new JCheckBox();
        ckNewQuotation.setText("");
        panel6.add(ckNewQuotation, new GridConstraints(11, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ckRecordQuotations = new JCheckBox();
        ckRecordQuotations.setText("");
        panel6.add(ckRecordQuotations, new GridConstraints(12, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        panel5.add(spacer4, new GridConstraints(3, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel7, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_SOUTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnHecho = new JButton();
        btnHecho.setText("Hecho");
        panel7.add(btnHecho, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        panel7.add(spacer5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        btnSave = new JButton();
        btnSave.setText("Registrar");
        panel7.add(btnSave, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ButtonGroup buttonGroup;
        buttonGroup = new ButtonGroup();
        buttonGroup.add(rbGroup);
        buttonGroup.add(rbPropies);
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

}
