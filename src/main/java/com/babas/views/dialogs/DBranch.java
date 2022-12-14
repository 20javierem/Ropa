package com.babas.views.dialogs;

import com.babas.models.Box;
import com.babas.models.Branch;
import com.babas.models.User;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.UtilitiesTables;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorBox;
import com.babas.utilitiesTables.tablesCellRendered.ColorCellRendered;
import com.babas.utilitiesTables.tablesCellRendered.UserCellRendered;
import com.babas.utilitiesTables.tablesModels.BoxAbstractModel;
import com.babas.utilitiesTables.tablesModels.UserAbstractModel;
import com.babas.validators.ProgramValidator;
import com.babas.views.frames.FPrincipal;
import com.formdev.flatlaf.extras.components.FlatButton;
import com.formdev.flatlaf.extras.components.FlatSpinner;
import com.formdev.flatlaf.extras.components.FlatTable;
import com.formdev.flatlaf.extras.components.FlatTextField;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.moreno.Notify;
import jakarta.validation.ConstraintViolation;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Set;

public class DBranch extends JDialog {
    private JPanel contentPane;
    private FlatButton btnHecho;
    private JTabbedPane tabbedPane;
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
    private FlatSpinner spinnerNotaVenta;
    private FlatSpinner spinnerBoleta;
    private FlatSpinner spinnerFactura;
    private FlatTextField txtSerieNotaVenta;
    private FlatTextField txtSerieBoleta;
    private FlatTextField txtSerieFactura;
    private FlatTextField txtUbigeo;
    private Branch branch;
    private boolean update;
    private UserAbstractModel modelUsersBranchs;
    private UserAbstractModel modelUsers;
    private BoxAbstractModel modelBoxs;
    private ActionListener actionListener;
    private boolean fprincipal;

    public DBranch(Branch branch, boolean fprincipal) {
        super(Utilities.getJFrame(), "Nueva sucursal", true);
        this.branch = branch;
        this.fprincipal = fprincipal;
        $$$setupUI$$$();
        update = branch.getId() != null;
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

    private void loadNewBox() {
        DBox dBox = new DBox(new Box(branch));
        dBox.setVisible(true);
    }

    private void addUser() {
        if (tableUsers.getSelectedRow() != -1) {
            User user = modelUsers.getList().get(tableUsers.convertRowIndexToModel(tableUsers.getSelectedRow()));
            branch.getUsers().add(user);
            user.getBranchs().add(branch);
            modelUsers.getList().remove(user);
            modelUsers.fireTableDataChanged();
            modelUsersBranchs.fireTableDataChanged();
        } else {
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER, "ERROR", "Debe seleccionar un usuario");
        }
    }

    private void removeUser() {
        if (tableUserBranch.getSelectedRow() != -1) {
            User user = modelUsersBranchs.getList().get(tableUserBranch.convertRowIndexToModel(tableUserBranch.getSelectedRow()));
            user.getBranchs().remove(branch);
            branch.getUsers().remove(user);
            modelUsers.getList().add(user);
            modelUsers.fireTableDataChanged();
            modelUsersBranchs.fireTableDataChanged();
        } else {
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER, "ERROR", "Debe seleccionar un usuario");
        }
    }

    private void init() {
        setContentPane(contentPane);
        getRootPane().setDefaultButton(btnSave);
        actionListener = e -> modelBoxs.fireTableDataChanged();
        Utilities.getActionsOfDialog().addActionListener(actionListener);
        if (fprincipal) {
            tabbedPane.removeTabAt(tabbedPane.indexOfTab("Usuarios"));
            tabbedPane.removeTabAt(tabbedPane.indexOfTab("Cajas"));
            tabbedPane.removeTabAt(tabbedPane.indexOfTab("Facturador"));
        }
        if (update) {
            setTitle("Actualizar Sucursal");
            btnSave.setText("Guardar");
            btnHecho.setText("Cancelar");
        }
        load();
        pack();
        setResizable(false);
        setLocationRelativeTo(Utilities.getJFrame());
    }

    private void load() {
        spinnerNotaVenta.setValue(branch.getCorrelativoNotaVenta());
        spinnerBoleta.setValue(branch.getCorrelativoBoleta());
        spinnerFactura.setValue(branch.getCorrelativoFactura());
        txtSerieNotaVenta.setText(branch.getSerieNotaVenta());
        txtSerieBoleta.setText(branch.getSerieBoleta());
        txtSerieFactura.setText(branch.getSerieFactura());
        txtName.setText(branch.getName());
        txtDirection.setText(branch.getDirection());
        txtEmail.setText(branch.getEmail());
        txtPhone.setText(branch.getPhone());
        txtUbigeo.setText(branch.getUbigeo());
        modelUsersBranchs = new UserAbstractModel(branch.getUsers());
        tableUserBranch.setModel(modelUsersBranchs);
        UserCellRendered.setCellRenderer(tableUserBranch, null);
        UtilitiesTables.headerNegrita(tableUserBranch);
        tableUserBranch.removeColumn(tableUserBranch.getColumn("CELULAR"));
        tableUserBranch.removeColumn(tableUserBranch.getColumn("ULTIMA SESI??N"));
        tableUserBranch.removeColumn(tableUserBranch.getColumn("ESTADO"));
        tableUserBranch.removeColumn(tableUserBranch.getColumn("SUCURSALES"));
        tableUserBranch.removeColumn(tableUserBranch.getColumn(""));
        tableUserBranch.removeColumn(tableUserBranch.getColumn(""));
        loadTables();
        branch.getUsers().forEach(user -> modelUsers.getList().remove(user));
        modelUsers.fireTableDataChanged();
    }

    private void loadTables() {
        modelUsers = new UserAbstractModel(new ArrayList<>(FPrincipal.users));
        tableUsers.setModel(modelUsers);
        UserCellRendered.setCellRenderer(tableUsers, null);
        UtilitiesTables.headerNegrita(tableUsers);
        tableUsers.removeColumn(tableUsers.getColumn("CELULAR"));
        tableUsers.removeColumn(tableUsers.getColumn("ULTIMA SESI??N"));
        tableUsers.removeColumn(tableUsers.getColumn("ESTADO"));
        tableUsers.removeColumn(tableUsers.getColumn("SUCURSALES"));
        tableUsers.removeColumn(tableUsers.getColumn(""));
        tableUsers.removeColumn(tableUsers.getColumn(""));
        modelBoxs = new BoxAbstractModel(branch.getBoxs());
        tableBoxs.setModel(modelBoxs);

        ColorCellRendered.setCellRenderer(tableBoxs);
        UtilitiesTables.headerNegrita(tableBoxs);
        tableBoxs.getColumnModel().getColumn(modelBoxs.getColumnCount() - 1).setCellEditor(new JButtonEditorBox(false));
        tableBoxs.getColumnModel().getColumn(modelBoxs.getColumnCount() - 2).setCellEditor(new JButtonEditorBox(true));
        tableBoxs.removeColumn(tableBoxs.getColumn("SUCURSAL"));
    }

    private void onSave() {
        branch.setCorrelativoNotaVenta(Long.valueOf(String.valueOf(spinnerNotaVenta.getValue())));
        branch.setCorrelativoBoleta(Long.valueOf(String.valueOf(spinnerBoleta.getValue())));
        branch.setCorrelativoFactura(Long.valueOf(String.valueOf(spinnerFactura.getValue())));
        branch.setSerieBoleta(txtSerieBoleta.getText().trim());
        branch.setSerieNotaVenta(txtSerieNotaVenta.getText().trim());
        branch.setSerieFactura(txtSerieFactura.getText().trim());
        branch.setPhone(txtPhone.getText().trim());
        branch.setDirection(txtDirection.getText().trim());
        branch.setEmail(txtEmail.getText().trim());
        branch.setName(txtName.getText().trim());
        branch.setUbigeo(txtUbigeo.getText().trim());
        branch.setCompany(Babas.company);
        Set<ConstraintViolation<Object>> constraintViolationSet = ProgramValidator.loadViolations(branch);
        if (constraintViolationSet.isEmpty()) {
            branch.save();
            if (!update) {
                FPrincipal.branchs.add(branch);
                if (Utilities.getTabbedPane() != null) {
                    Utilities.getTabbedPane().updateTab();
                } else {
                    onHecho();
                }
                branch = new Branch();
                load();
                Utilities.updateDialog();
                if (Utilities.getJFrame() != null && Utilities.getJFrame() instanceof FPrincipal) {
                    Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER, "??XITO", "Sucursal registrada");
                }
            } else {
                Utilities.updateDialog();
                Utilities.getTabbedPane().updateTab();
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER, "??XITO", "Sucursal actualizada");
                onHecho();
            }
        } else {
            ProgramValidator.mostrarErrores(constraintViolationSet);
        }
    }

    private void onHecho() {
        if (update) {
            branch.refresh();
        }
        Utilities.getActionsOfDialog().removeActionListener(actionListener);
        dispose();
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
        contentPane.setLayout(new GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), -1, 10));
        contentPane.setPreferredSize(new Dimension(550, 448));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        tabbedPane = new JTabbedPane();
        panel1.add(tabbedPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(7, 4, new Insets(10, 10, 10, 10), 5, 5));
        tabbedPane.addTab("Sucursal", panel2);
        final JLabel label1 = new JLabel();
        label1.setText("Nombre:");
        panel2.add(label1, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel2.add(spacer1, new GridConstraints(6, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Direcci??n:");
        panel2.add(label2, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtName = new FlatTextField();
        panel2.add(txtName, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(210, -1), null, 0, false));
        txtDirection = new FlatTextField();
        panel2.add(txtDirection, new GridConstraints(5, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(210, -1), null, 0, false));
        txtEmail = new FlatTextField();
        panel2.add(txtEmail, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(210, -1), null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Email:");
        panel2.add(label3, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Celular:");
        panel2.add(label4, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtPhone = new FlatTextField();
        panel2.add(txtPhone, new GridConstraints(4, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(210, -1), null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel2.add(spacer2, new GridConstraints(0, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel2.add(spacer3, new GridConstraints(0, 0, 7, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        panel2.add(spacer4, new GridConstraints(0, 3, 7, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Ubigeo:");
        panel2.add(label5, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtUbigeo = new FlatTextField();
        panel2.add(txtUbigeo, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(210, -1), null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(2, 3, new Insets(5, 5, 5, 5), 0, 10));
        tabbedPane.addTab("Usuarios", panel3);
        final JScrollPane scrollPane1 = new JScrollPane();
        panel3.add(scrollPane1, new GridConstraints(0, 0, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        scrollPane1.setBorder(BorderFactory.createTitledBorder(null, "Usuarios", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, null, null));
        tableUsers = new FlatTable();
        scrollPane1.setViewportView(tableUsers);
        final JScrollPane scrollPane2 = new JScrollPane();
        panel3.add(scrollPane2, new GridConstraints(0, 2, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        scrollPane2.setBorder(BorderFactory.createTitledBorder(null, "Usuarios de la sucursal", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, null, null));
        tableUserBranch = new FlatTable();
        scrollPane2.setViewportView(tableUserBranch);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(2, 1, new Insets(2, 2, 2, 2), -1, 1));
        panel3.add(panel4, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnAddUser = new JButton();
        btnAddUser.setIcon(new ImageIcon(getClass().getResource("/com/babas/icons/x32/flechaRigth.png")));
        panel4.add(btnAddUser, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(42, 35), new Dimension(42, 35), new Dimension(42, 35), 0, false));
        btnRemoveUser = new JButton();
        btnRemoveUser.setIcon(new ImageIcon(getClass().getResource("/com/babas/icons/x32/flechaLeft.png")));
        panel4.add(btnRemoveUser, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(42, 35), new Dimension(42, 35), new Dimension(42, 35), 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(2, 2, new Insets(5, 5, 5, 5), 0, 10));
        tabbedPane.addTab("Cajas", panel5);
        final JScrollPane scrollPane3 = new JScrollPane();
        panel5.add(scrollPane3, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        tableBoxs = new FlatTable();
        scrollPane3.setViewportView(tableBoxs);
        btnNewBox = new JButton();
        btnNewBox.setText("Nueva");
        panel5.add(btnNewBox, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        panel5.add(spacer5, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(5, 4, new Insets(10, 10, 10, 10), 5, 5));
        tabbedPane.addTab("Facturador", panel6);
        final Spacer spacer6 = new Spacer();
        panel6.add(spacer6, new GridConstraints(4, 0, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JComboBox comboBox1 = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        defaultComboBoxModel1.addElement("Serie Nota de venta");
        comboBox1.setModel(defaultComboBoxModel1);
        panel6.add(comboBox1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JComboBox comboBox2 = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel2 = new DefaultComboBoxModel();
        defaultComboBoxModel2.addElement("Serie Boleta");
        comboBox2.setModel(defaultComboBoxModel2);
        panel6.add(comboBox2, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JComboBox comboBox3 = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel3 = new DefaultComboBoxModel();
        defaultComboBoxModel3.addElement("Serie Factura");
        comboBox3.setModel(defaultComboBoxModel3);
        panel6.add(comboBox3, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtSerieNotaVenta = new FlatTextField();
        panel6.add(txtSerieNotaVenta, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(65, -1), null, 0, false));
        txtSerieBoleta = new FlatTextField();
        panel6.add(txtSerieBoleta, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(65, -1), null, 0, false));
        txtSerieFactura = new FlatTextField();
        panel6.add(txtSerieFactura, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(65, -1), null, 0, false));
        final JComboBox comboBox4 = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel4 = new DefaultComboBoxModel();
        defaultComboBoxModel4.addElement("Correlativo");
        comboBox4.setModel(defaultComboBoxModel4);
        panel6.add(comboBox4, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panel6.add(spinnerNotaVenta, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(150, -1), new Dimension(150, -1), 0, false));
        panel6.add(spinnerBoleta, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(150, -1), new Dimension(150, -1), 0, false));
        panel6.add(spinnerFactura, new GridConstraints(3, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(150, -1), new Dimension(150, -1), 0, false));
        final JComboBox comboBox5 = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel5 = new DefaultComboBoxModel();
        defaultComboBoxModel5.addElement("Correlativo");
        comboBox5.setModel(defaultComboBoxModel5);
        panel6.add(comboBox5, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JComboBox comboBox6 = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel6 = new DefaultComboBoxModel();
        defaultComboBoxModel6.addElement("Correlativo");
        comboBox6.setModel(defaultComboBoxModel6);
        panel6.add(comboBox6, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer7 = new Spacer();
        panel6.add(spacer7, new GridConstraints(0, 0, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel7, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_SOUTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnHecho = new FlatButton();
        btnHecho.setText("Hecho");
        panel7.add(btnHecho, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer8 = new Spacer();
        panel7.add(spacer8, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        btnSave = new FlatButton();
        btnSave.setText("Registrar");
        panel7.add(btnSave, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        spinnerNotaVenta = new FlatSpinner();
        spinnerNotaVenta.setModel(new SpinnerNumberModel(0, 0, 100000, 1));
        spinnerNotaVenta.setEditor(Utilities.getEditorPrice(spinnerNotaVenta));
        spinnerBoleta = new FlatSpinner();
        spinnerBoleta.setModel(new SpinnerNumberModel(0, 0, 100000, 1));
        spinnerBoleta.setEditor(Utilities.getEditorPrice(spinnerBoleta));
        spinnerFactura = new FlatSpinner();
        spinnerFactura.setModel(new SpinnerNumberModel(0, 0, 100000, 1));
        spinnerFactura.setEditor(Utilities.getEditorPrice(spinnerFactura));
    }
}
