package com.babas.views.tabs;

import com.babas.controllers.Transfers;
import com.babas.custom.TabPane;
import com.babas.models.*;
import com.babas.models.Color;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.babas.utilities.UtilitiesReports;
import com.babas.utilitiesTables.UtilitiesTables;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorTransfer;
import com.babas.utilitiesTables.tablesCellRendered.TransferCellRendered;
import com.babas.utilitiesTables.tablesModels.ProductAbstractModel;
import com.babas.utilitiesTables.tablesModels.TransferAbstractModel;
import com.babas.views.frames.FPrincipal;
import com.formdev.flatlaf.extras.components.FlatTable;
import com.formdev.flatlaf.extras.components.FlatTextField;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.moreno.Notify;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.TableRowSorter;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class TabRecordTransfers {
    private TabPane tabPane;
    private JPanel paneEntreFecha;
    private JDateChooser fechaInicio;
    private JDateChooser fechaFin;
    private JPanel paneHastaFecha;
    private JDateChooser fechaHasta;
    private JPanel paneDesdeFecha;
    private JDateChooser fechaDesde;
    private JButton btnSearch;
    private JComboBox cbbDate;
    private JComboBox cbbBranch;
    private FlatTable table;
    private JComboBox cbbState;
    private JButton btnClearFilters;
    private JButton btnGenerateReport;
    private TransferAbstractModel model;
    private Map<Integer, String> listaFiltros = new HashMap<Integer, String>();
    private TableRowSorter<TransferAbstractModel> modeloOrdenado;
    private List<RowFilter<TransferAbstractModel, String>> filtros = new ArrayList<>();
    private RowFilter filtroand;
    private List<Transfer> transfers;
    private Date start, end;

    public TabRecordTransfers() {
        $$$setupUI$$$();
        init();
        cbbDate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterByType();
            }
        });
        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getTransfers();
            }
        });
        cbbBranch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        cbbBranch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filter();
            }
        });
        cbbState.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filter();
            }
        });
        btnClearFilters.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFilters();
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
        List<Transfer> transfers = new ArrayList<>();
        for (int i = 0; i < table.getRowCount(); i++) {
            transfers.add(model.getList().get(table.convertRowIndexToModel(i)));
        }
        if (!transfers.isEmpty()) {
            Date start1 = start;
            Date end1 = end;
            if (start1 == null) {
                start1 = transfers.get(0).getCreated();
            }
            if (end1 == null) {
                end1 = transfers.get(transfers.size() - 1).getCreated();
            }
            btnGenerateReport.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            UtilitiesReports.generateReportTransfers(transfers, start1, end1);
            btnGenerateReport.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        } else {
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.INFO, Notify.Location.TOP_CENTER, "MENSAJE", "No se encontraron transferencias");
        }
    }

    private void clearFilters() {
        cbbBranch.setSelectedIndex(0);
        cbbState.setSelectedIndex(0);
        filter();
    }

    private void filterByType() {
        switch (cbbDate.getSelectedIndex()) {
            case 0:
                paneEntreFecha.setVisible(false);
                paneDesdeFecha.setVisible(false);
                paneHastaFecha.setVisible(false);
                break;
            case 1:
                paneEntreFecha.setVisible(true);
                paneDesdeFecha.setVisible(false);
                paneHastaFecha.setVisible(false);
                break;
            case 2:
                paneEntreFecha.setVisible(false);
                paneDesdeFecha.setVisible(true);
                paneHastaFecha.setVisible(false);
                break;
            case 3:
                paneEntreFecha.setVisible(false);
                paneDesdeFecha.setVisible(false);
                paneHastaFecha.setVisible(true);
                break;
        }
    }

    private void init() {
        tabPane.setTitle("Historial de traslados");
        tabPane.getActions().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.fireTableDataChanged();
                filter();
            }
        });
        loadTable();
        loadCombos();
        filter();
    }

    private void loadCombos() {
        cbbBranch.setModel(new DefaultComboBoxModel(FPrincipal.branchesWithAll));
        cbbBranch.setRenderer(new Branch.ListCellRenderer());
    }

    private void getTransfers() {
        btnSearch.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        start = null;
        end = null;
        if (paneEntreFecha.isVisible()) {
            if (fechaInicio.getDate() != null) {
                start = fechaInicio.getDate();
            }
            if (fechaFin.getDate() != null) {
                end = fechaFin.getDate();
            }
        }
        if (paneDesdeFecha.isVisible()) {
            if (fechaDesde.getDate() != null) {
                start = fechaDesde.getDate();
            }
        }
        if (paneHastaFecha.isVisible()) {
            if (fechaHasta.getDate() != null) {
                end = fechaHasta.getDate();
            }
        }
        if (start != null && end != null) {
            transfers.clear();
            for (Branch branch : Babas.user.getBranchs()) {
                Transfers.getByRangeOfDate(branch, start, end).forEach(transfer -> {
                    if (!transfers.contains(transfer)) {
                        transfers.add(transfer);
                    }
                });
            }
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.INFO, Notify.Location.TOP_CENTER, "MENSAJE", "Transferencias cargadas");
            model.fireTableDataChanged();
        } else if (start != null) {
            transfers.clear();
            for (Branch branch : Babas.user.getBranchs()) {
                Transfers.getAfter(branch, start).forEach(transfer -> {
                    if (!transfers.contains(transfer)) {
                        transfers.add(transfer);
                    }
                });
            }
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.INFO, Notify.Location.TOP_CENTER, "MENSAJE", "Transferencias cargadas");
            model.fireTableDataChanged();
        } else if (end != null) {
            transfers.clear();
            for (Branch branch : Babas.user.getBranchs()) {
                Transfers.getBefore(branch, end).forEach(transfer -> {
                    if (!transfers.contains(transfer)) {
                        transfers.add(transfer);
                    }
                });
            }
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.INFO, Notify.Location.TOP_CENTER, "MENSAJE", "Transferencias cargadas");
            model.fireTableDataChanged();
        } else {
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.INFO, Notify.Location.TOP_CENTER, "ERROR", "Debe seleccionar un rango de fechas");
        }
        btnSearch.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        filter();
    }

    private void filter() {
        filtros.clear();
        if (((Branch) cbbBranch.getSelectedItem()).getId() != null) {
            Branch branch = (Branch) cbbBranch.getSelectedItem();
            filtros.add(RowFilter.regexFilter(branch.getName(), 3, 4));
        }
        if (cbbState.getSelectedIndex() != 0) {
            filtros.add(RowFilter.regexFilter(String.valueOf(cbbState.getSelectedItem()), 6));
        }
        filtroand = RowFilter.andFilter(filtros);
        modeloOrdenado.setRowFilter(filtroand);
        if (model.getList().size() == table.getRowCount()) {
            Utilities.getLblCentro().setText("Registros: " + model.getList().size());
        } else {
            Utilities.getLblCentro().setText("Registros filtrados: " + table.getRowCount());
        }
    }

    public TabPane getTabPane() {
        return tabPane;
    }

    private void loadTable() {
        transfers = new ArrayList<>();
        for (Branch branch : Babas.user.getBranchs()) {
            Transfers.getAfter(branch, new Date()).forEach(transfer -> {
                if (!transfers.contains(transfer)) {
                    transfers.add(transfer);
                }
            });
        }
        model = new TransferAbstractModel(transfers);
        table.setModel(model);
        UtilitiesTables.headerNegrita(table);
        TransferCellRendered.setCellRenderer(table);
        table.getColumnModel().getColumn(table.getColumnCount() - 1).setCellEditor(new JButtonEditorTransfer(false));
        table.getColumnModel().getColumn(table.getColumnCount() - 2).setCellEditor(new JButtonEditorTransfer(true));
        modeloOrdenado = new TableRowSorter<>(model);
        table.setRowSorter(modeloOrdenado);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        fechaInicio = new JDateChooser(new Date());
        fechaFin = new JDateChooser(new Date());
        fechaDesde = new JDateChooser(new Date());
        fechaHasta = new JDateChooser(new Date());
        fechaInicio.setDateFormatString(Utilities.getFormatoFecha());
        fechaFin.setDateFormatString(Utilities.getFormatoFecha());
        fechaDesde.setDateFormatString(Utilities.getFormatoFecha());
        fechaHasta.setDateFormatString(Utilities.getFormatoFecha());
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
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabPane = new TabPane();
        tabPane.setLayout(new GridLayoutManager(3, 1, new Insets(10, 10, 10, 10), 5, 5));
        panel1.add(tabPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 5, 0), 10, -1));
        tabPane.add(panel2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel2.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        table = new FlatTable();
        scrollPane1.setViewportView(table);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 7, new Insets(0, 0, 0, 0), -1, -1));
        tabPane.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnClearFilters = new JButton();
        btnClearFilters.setText("Limpiar filtros");
        panel3.add(btnClearFilters, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$(null, Font.BOLD, -1, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setText("Sucursal:");
        panel3.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbbBranch = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        defaultComboBoxModel1.addElement("TODAS");
        defaultComboBoxModel1.addElement("LIMA");
        defaultComboBoxModel1.addElement("CUSCO");
        cbbBranch.setModel(defaultComboBoxModel1);
        panel3.add(cbbBranch, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        Font label2Font = this.$$$getFont$$$(null, Font.BOLD, -1, label2.getFont());
        if (label2Font != null) label2.setFont(label2Font);
        label2.setText("Estado:");
        panel3.add(label2, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbbState = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel2 = new DefaultComboBoxModel();
        defaultComboBoxModel2.addElement("TODAS");
        defaultComboBoxModel2.addElement("EN ESPERA");
        defaultComboBoxModel2.addElement("COMPLETADO");
        defaultComboBoxModel2.addElement("CANCELADO");
        cbbState.setModel(defaultComboBoxModel2);
        panel3.add(cbbState, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnGenerateReport = new JButton();
        btnGenerateReport.setIcon(new ImageIcon(getClass().getResource("/com/babas/icons/x32/pdf.png")));
        btnGenerateReport.setText("Generar reporte");
        panel3.add(btnGenerateReport, new GridConstraints(0, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel3.add(spacer1, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 7, new Insets(0, 0, 0, 0), -1, -1));
        tabPane.add(panel4, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_SOUTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel4.add(spacer2, new GridConstraints(0, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        Font label3Font = this.$$$getFont$$$(null, Font.BOLD, -1, label3.getFont());
        if (label3Font != null) label3.setFont(label3Font);
        label3.setText("Fecha:");
        panel4.add(label3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbbDate = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel3 = new DefaultComboBoxModel();
        defaultComboBoxModel3.addElement("NINGUNO");
        defaultComboBoxModel3.addElement("ENTRE");
        defaultComboBoxModel3.addElement("DESDE");
        defaultComboBoxModel3.addElement("HASTA");
        cbbDate.setModel(defaultComboBoxModel3);
        panel4.add(cbbDate, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        paneEntreFecha = new JPanel();
        paneEntreFecha.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        paneEntreFecha.setVisible(false);
        panel4.add(paneEntreFecha, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        paneEntreFecha.add(fechaFin, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(150, -1), null, 0, false));
        paneEntreFecha.add(fechaInicio, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(150, -1), null, 0, false));
        paneDesdeFecha = new JPanel();
        paneDesdeFecha.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        paneDesdeFecha.setVisible(false);
        panel4.add(paneDesdeFecha, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        paneDesdeFecha.add(fechaDesde, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(150, -1), null, 0, false));
        paneHastaFecha = new JPanel();
        paneHastaFecha.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        paneHastaFecha.setVisible(false);
        panel4.add(paneHastaFecha, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        paneHastaFecha.add(fechaHasta, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(150, -1), null, 0, false));
        btnSearch = new JButton();
        btnSearch.setText("Buscar");
        panel4.add(btnSearch, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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
