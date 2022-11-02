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
            if (Utilities.iconCompanyx255x220 != null) {
                lblLogo.setIcon(Utilities.iconCompanyx255x220);
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

}
