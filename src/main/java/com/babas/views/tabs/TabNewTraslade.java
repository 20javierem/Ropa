package com.babas.views.tabs;

import com.babas.App;
import com.babas.custom.TabPane;
import com.babas.models.Branch;
import com.babas.models.Brand;
import com.babas.models.Transfer;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.babas.utilities.UtilitiesReports;
import com.babas.utilitiesTables.UtilitiesTables;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorBrand;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorDetailTransfer;
import com.babas.utilitiesTables.tablesCellRendered.DetailTransferCellRendered;
import com.babas.utilitiesTables.tablesModels.DetailTransferAbstractModel;
import com.babas.validators.ProgramValidator;
import com.babas.views.dialogs.DAddProductToTransfer;
import com.babas.views.frames.FPrincipal;
import com.formdev.flatlaf.extras.components.FlatTable;
import com.formdev.flatlaf.extras.components.FlatTextArea;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.moreno.Notify;
import jakarta.validation.ConstraintViolation;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

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
    private JButton btnRechase;
    private JButton btnGenerateReport;
    private Transfer transfer;
    private DetailTransferAbstractModel model;
    private Branch olBranchSource;
    private int oldIndexType = 0;
    private boolean update;

    public TabNewTraslade(Transfer transfer) {
        this.transfer = transfer;
        update = transfer.getId() != null;
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
        btnConfirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSave(true);
            }
        });
        btnRechase.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSave(false);
            }
        });
        btnGenerateReport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateReport();
            }
        });
    }

    private void generateReport() {
        btnGenerateReport.setCursor(new Cursor(Cursor.HAND_CURSOR));
        UtilitiesReports.generateReportTransfer(transfer);
        btnGenerateReport.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    private void loadAddProducts() {
        DAddProductToTransfer dAddProductToTransfer;
        switch (cbbTypeTransfer.getSelectedIndex()) {
            case 0:
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.INFO, Notify.Location.TOP_CENTER, "MENSAJE", "Debe seleccionar el tipo de transferencia");
                break;
            case 1:
                dAddProductToTransfer = new DAddProductToTransfer(transfer, null);
                dAddProductToTransfer.setVisible(true);
                break;
            case 2:
                if (cbbBranchSource.getSelectedIndex() != -1) {
                    dAddProductToTransfer = new DAddProductToTransfer(transfer, (Branch) cbbBranchSource.getSelectedItem());
                    dAddProductToTransfer.setVisible(true);
                } else {
                    Notify.sendNotify(Utilities.getJFrame(), Notify.Type.INFO, Notify.Location.TOP_CENTER, "MENSAJE", "Debe seleccionar el origen");
                }
                break;
        }
    }

    private void filterBySource() {
        if (olBranchSource == null) {
            olBranchSource = (Branch) cbbBranchSource.getSelectedItem();
        }
        if (!transfer.getDetailTransfers().isEmpty()) {
            boolean si = JOptionPane.showConfirmDialog(Utilities.getJFrame(), "Esta acción quitará todos los productos agregados", "Cambiar origen", JOptionPane.YES_NO_OPTION) == 0;
            if (si) {
                transfer.getDetailTransfers().clear();
                filterSource();
                Utilities.getTabbedPane().updateTab();
            } else {
                cbbBranchSource.setSelectedItem(olBranchSource);
            }
        } else {
            filterSource();
        }
    }

    private void filterSource() {
        olBranchSource = (Branch) cbbBranchSource.getSelectedItem();
        cbbBranchDestiny.setModel(new DefaultComboBoxModel(new Vector(FPrincipal.branchs)));
        cbbBranchDestiny.setRenderer(new Branch.ListCellRenderer());
        cbbBranchDestiny.removeItem(olBranchSource);
    }

    private void filterByType() {
        if (!transfer.getDetailTransfers().isEmpty()) {
            boolean si = JOptionPane.showConfirmDialog(Utilities.getJFrame(), "Esta acción quitará todos los productos agregados", "Cambiar tipo de tralado", JOptionPane.YES_NO_OPTION) == 0;
            if (si) {
                transfer.getDetailTransfers().clear();
                filterType();
                Utilities.getTabbedPane().updateTab();
            } else {
                cbbTypeTransfer.setSelectedIndex(oldIndexType);
            }
        } else {
            filterType();
        }
    }

    private void filterType() {
        oldIndexType = cbbTypeTransfer.getSelectedIndex();
        switch (cbbTypeTransfer.getSelectedIndex()) {
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

    private void init() {
        tabPane.setTitle("Nuevo traslado");
        if (!Babas.company.getLogo().isBlank()) {
            if (Utilities.iconCompany != null) {
                lblLogo.setIcon(Utilities.iconCompany);
            }
        }
        loadCombos();
        paneTraslade.setVisible(false);
        paneEntry.setVisible(false);
        loadTable();
        if (update) {
            tabPane.setTitle("Traslado " + transfer.getId());
            load();
            btnConfirm.setText("Confirmar");
            table.removeColumn(table.getColumn(""));
            if (transfer.getState() == 1) {
                btnRechase.setVisible(false);
                btnConfirm.setVisible(false);
            }
        } else {
            btnRechase.setVisible(false);
        }
        if (transfer.getId() != null) {
            btnGenerateReport.setVisible(true);
        }
        getTabPane().getActions().addActionListener(e -> model.fireTableDataChanged());
    }

    private void loadCombos() {
        cbbBranch.setModel(new DefaultComboBoxModel(new Vector(Babas.user.getBranchs())));
        cbbBranch.setRenderer(new Branch.ListCellRenderer());
        cbbBranchSource.setModel(new DefaultComboBoxModel(new Vector(Babas.user.getBranchs())));
        cbbBranchSource.setRenderer(new Branch.ListCellRenderer());
        cbbBranchDestiny.setModel(new DefaultComboBoxModel(new Vector(FPrincipal.branchs)));
        cbbBranchDestiny.setRenderer(new Branch.ListCellRenderer());
    }

    private void load() {
        cbbTypeTransfer.setEnabled(false);
        if (Objects.equals(transfer.getSource().getId(), transfer.getDestiny().getId())) {
            paneTraslade.setVisible(false);
            paneEntry.setVisible(true);
            cbbBranch.setEnabled(false);
            cbbTypeTransfer.setSelectedIndex(1);
        } else {
            cbbTypeTransfer.setSelectedIndex(2);
            cbbBranchSource.setSelectedItem(transfer.getSource());
            cbbBranchDestiny.setSelectedItem(transfer.getDestiny());
            cbbBranchSource.setEnabled(false);
            cbbBranchDestiny.setEnabled(false);
            paneTraslade.setVisible(true);
            paneEntry.setVisible(false);
        }
        txtDescription.setText(transfer.getDescription());
        txtDescription.setEnabled(false);
    }

    public void loadTable() {
        model = new DetailTransferAbstractModel(transfer.getDetailTransfers());
        table.setModel(model);
        DetailTransferCellRendered.setCellRenderer(table);
        UtilitiesTables.headerNegrita(table);
        table.getColumnModel().getColumn(model.getColumnCount() - 1).setCellEditor(new JButtonEditorDetailTransfer());
    }

    public void onSave(boolean acept) {
        if (!update) {
            if (paneTraslade.isVisible()) {
                transfer.setSource((Branch) cbbBranchSource.getSelectedItem());
                transfer.setDestiny((Branch) cbbBranchDestiny.getSelectedItem());
                transfer.setState(0);
            } else if (paneEntry.isVisible()) {
                transfer.setSource((Branch) cbbBranch.getSelectedItem());
                transfer.setDestiny((Branch) cbbBranch.getSelectedItem());
                transfer.setState(1);
            }
            transfer.setDescription(txtDescription.getText().trim());
            transfer.calculateTotalProuctsTransfers();
            Set<ConstraintViolation<Object>> constraintViolationSet = ProgramValidator.loadViolations(transfer);
            if (constraintViolationSet.isEmpty()) {
                boolean si = JOptionPane.showConfirmDialog(Utilities.getJFrame(), "¿Está seguro?", "Crear transferencia", JOptionPane.YES_NO_OPTION) == 0;
                if (si) {
                    transfer.save();
                    Utilities.getTabbedPane().updateTab();
                    ((FPrincipal) Utilities.getJFrame()).loadTransferOnWait();
                    transfer = new Transfer();
                    clear();
                    Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER, "ÉXITO", "Transferencia registrada");
                }

            } else {
                ProgramValidator.mostrarErrores(constraintViolationSet);
            }
        } else {
            if (acept) {
                boolean si = JOptionPane.showConfirmDialog(Utilities.getJFrame(), "¿Está seguro?", "Comfirmar transferencia", JOptionPane.YES_NO_OPTION) == 0;
                if (si) {
                    transfer.refresh();
                    if (transfer.getState() == 0) {
                        transfer.setState(1);
                        transfer.setUpdated(new Date());
                        transfer.save();
                        Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER, "ÉXITO", "Transferencia aceptada");
                    } else {
                        if (transfer.getState() == 1) {
                            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER, "MENSAJE", "La transferencia ya fue aceptada por otro usuario");
                        } else {
                            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER, "MENSAJE", "La transferencia ya fue rechazada por otro usuario");
                        }
                    }
                    btnRechase.setVisible(false);
                    btnConfirm.setVisible(false);
                    ((FPrincipal) Utilities.getJFrame()).loadTransferOnWait();
                }
            } else {
                boolean si = JOptionPane.showConfirmDialog(Utilities.getJFrame(), "¿Está seguro?", "Rechazar transferencia", JOptionPane.YES_NO_OPTION) == 0;
                if (si) {
                    if (transfer.getState() != 0) {
                        transfer.setState(2);
                        transfer.setUpdated(new Date());
                        transfer.save();
                        Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER, "ÉXITO", "Transferencia rechazada");
                    } else {
                        if (transfer.getState() == 1) {
                            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER, "MENSAJE", "La transferencia ya fue aceptada por otro usuario");
                        } else {
                            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER, "MENSAJE", "La transferencia ya fue rechazada por otro usuario");
                        }
                    }
                    btnRechase.setVisible(false);
                    btnConfirm.setVisible(false);
                    ((FPrincipal) Utilities.getJFrame()).loadTransferOnWait();
                }
            }
        }

    }

    private void clear() {
        cbbTypeTransfer.setSelectedIndex(0);
        txtDescription.setText(null);
        filterByType();
        loadTable();
    }

    public TabPane getTabPane() {
        return tabPane;
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabPane = new TabPane();
        tabPane.setLayout(new GridLayoutManager(3, 2, new Insets(10, 10, 10, 10), 5, 5));
        panel1.add(tabPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        tabPane.add(panel2, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_SOUTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel2.add(spacer1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        btnAddProducts = new JButton();
        btnAddProducts.setText("Añadir Productos");
        panel2.add(btnAddProducts, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabPane.add(panel3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel3.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        table = new FlatTable();
        scrollPane1.setViewportView(table);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 1, new Insets(0, 5, 0, 0), 10, -1));
        tabPane.add(panel4, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 6, new Insets(0, 0, 0, 0), -1, -1));
        panel4.add(panel5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$(null, Font.BOLD, -1, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setText("Tipo de transferencia:");
        panel5.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel5.add(spacer2, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        cbbTypeTransfer = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        defaultComboBoxModel1.addElement("--SELECCIONE--");
        defaultComboBoxModel1.addElement("RECEPCIÓN");
        defaultComboBoxModel1.addElement("ENVÍO");
        cbbTypeTransfer.setModel(defaultComboBoxModel1);
        panel5.add(cbbTypeTransfer, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        paneEntry = new JPanel();
        paneEntry.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), 5, -1));
        panel5.add(paneEntry, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        Font label2Font = this.$$$getFont$$$(null, Font.BOLD, -1, label2.getFont());
        if (label2Font != null) label2.setFont(label2Font);
        label2.setText("Sucursal:");
        paneEntry.add(label2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbbBranch = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel2 = new DefaultComboBoxModel();
        defaultComboBoxModel2.addElement("--SELECCIONE--");
        cbbBranch.setModel(defaultComboBoxModel2);
        paneEntry.add(cbbBranch, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        paneTraslade = new JPanel();
        paneTraslade.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), 5, -1));
        panel5.add(paneTraslade, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        Font label3Font = this.$$$getFont$$$(null, Font.BOLD, -1, label3.getFont());
        if (label3Font != null) label3.setFont(label3Font);
        label3.setText("Origen:");
        paneTraslade.add(label3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbbBranchSource = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel3 = new DefaultComboBoxModel();
        defaultComboBoxModel3.addElement("--SELECCIONE--");
        cbbBranchSource.setModel(defaultComboBoxModel3);
        paneTraslade.add(cbbBranchSource, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        Font label4Font = this.$$$getFont$$$(null, Font.BOLD, -1, label4.getFont());
        if (label4Font != null) label4.setFont(label4Font);
        label4.setText("Destino:");
        paneTraslade.add(label4, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbbBranchDestiny = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel4 = new DefaultComboBoxModel();
        defaultComboBoxModel4.addElement("--SELECCIONE--");
        cbbBranchDestiny.setModel(defaultComboBoxModel4);
        paneTraslade.add(cbbBranchDestiny, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnGenerateReport = new JButton();
        btnGenerateReport.setIcon(new ImageIcon(getClass().getResource("/com/babas/icons/x32/pdf.png")));
        btnGenerateReport.setText("Generar reporte");
        btnGenerateReport.setVisible(false);
        panel5.add(btnGenerateReport, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabPane.add(panel6, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        lblLogo = new JLabel();
        lblLogo.setIcon(new ImageIcon(getClass().getResource("/com/babas/images/lojoJmoreno (1).png")));
        lblLogo.setText("");
        panel6.add(lblLogo, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(255, 220), new Dimension(255, 220), new Dimension(255, 220), 0, false));
        final JLabel label5 = new JLabel();
        Font label5Font = this.$$$getFont$$$(null, -1, 16, label5.getFont());
        if (label5Font != null) label5.setFont(label5Font);
        label5.setText("Descripción:");
        panel6.add(label5, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane2 = new JScrollPane();
        panel6.add(scrollPane2, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(-1, 80), new Dimension(-1, 80), new Dimension(-1, 80), 0, false));
        txtDescription = new FlatTextArea();
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);
        scrollPane2.setViewportView(txtDescription);
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel6.add(panel7, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel7.add(spacer3, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        btnConfirm = new JButton();
        btnConfirm.setText("Registrar");
        panel7.add(btnConfirm, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnRechase = new JButton();
        btnRechase.setText("Rechazar");
        panel7.add(btnRechase, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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

}
