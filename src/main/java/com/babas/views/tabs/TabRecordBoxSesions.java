package com.babas.views.tabs;

import com.babas.controllers.BoxSessions;
import com.babas.controllers.Sales;
import com.babas.custom.TabPane;
import com.babas.models.BoxSession;
import com.babas.models.Branch;
import com.babas.models.Sale;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.UtilitiesTables;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorBoxSession;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorSale;
import com.babas.utilitiesTables.tablesCellRendered.BoxSessionCellRendered;
import com.babas.utilitiesTables.tablesCellRendered.SaleCellRendered;
import com.babas.utilitiesTables.tablesModels.BoxSesionAbstractModel;
import com.babas.utilitiesTables.tablesModels.SaleAbstractModel;
import com.babas.views.frames.FPrincipal;
import com.formdev.flatlaf.extras.components.FlatTable;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TabRecordBoxSesions {
    private TabPane tabPane;
    private JDateChooser fechaInicio;
    private JDateChooser fechaFin;
    private JDateChooser fechaDesde;
    private JPanel paneEntreFecha;
    private JPanel paneDesdeFecha;
    private JButton btnSearch;
    private FlatTable table;
    private JComboBox cbbBranch;
    private JComboBox cbbDate;
    private List<BoxSession> sales;
    private BoxSesionAbstractModel model;
    private TableRowSorter<BoxSesionAbstractModel> modeloOrdenado;
    private List<RowFilter<BoxSesionAbstractModel, String>> filtros = new ArrayList<>();
    private RowFilter filtroand;

    public TabRecordBoxSesions() {
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
                getSales(true);
            }
        });
        cbbBranch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filter();
            }
        });
    }

    private void init() {
        tabPane.setTitle("Historial de cajas");
        tabPane.getActions().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getSales(false);
                filter();
            }
        });
        loadTable();
        loadCombos();
    }

    private void filterByType() {
        switch (cbbDate.getSelectedIndex()) {
            case 0:
                paneEntreFecha.setVisible(false);
                paneDesdeFecha.setVisible(false);
                break;
            case 1:
                paneEntreFecha.setVisible(true);
                paneDesdeFecha.setVisible(false);
                break;
            case 2:
                paneEntreFecha.setVisible(false);
                paneDesdeFecha.setVisible(true);
                break;
        }
    }

    private void loadCombos() {
        cbbBranch.setModel(new DefaultComboBoxModel(FPrincipal.branchesWithAll));
        cbbBranch.setRenderer(new Branch.ListCellRenderer());
    }

    private void loadTable() {
        sales = new ArrayList<>();
        for (Branch branch : Babas.user.getBranchs()) {
            sales.addAll(BoxSessions.getAfter(branch, new Date()));
        }
        model = new BoxSesionAbstractModel(sales);
        table.setModel(model);
        UtilitiesTables.headerNegrita(table);
        BoxSessionCellRendered.setCellRenderer(table, null);
        table.getColumnModel().getColumn(table.getColumnCount() - 1).setCellEditor(new JButtonEditorBoxSession());
        modeloOrdenado = new TableRowSorter<>(model);
        table.setRowSorter(modeloOrdenado);
    }

    private void filter() {
        filtros.clear();
        if (((Branch) cbbBranch.getSelectedItem()).getId() != null) {
            Branch branch = (Branch) cbbBranch.getSelectedItem();
            filtros.add(RowFilter.regexFilter(branch.getName(), 1));
        }
        filtroand = RowFilter.andFilter(filtros);
        modeloOrdenado.setRowFilter(filtroand);
        if (model.getList().size() == table.getRowCount()) {
            Utilities.getLblCentro().setText("Registros: " + model.getList().size());
        } else {
            Utilities.getLblCentro().setText("Registros filtrados: " + table.getRowCount());
        }
    }

    private void getSales(boolean show) {
        btnSearch.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        Date start = null;
        Date end = null;
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

        if (start != null && end != null) {
            sales.clear();
            for (Branch branch : Babas.user.getBranchs()) {
                sales.addAll(BoxSessions.getByRangeOfDate(branch, start, end));
            }
            if (show) {
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.INFO, Notify.Location.TOP_CENTER, "MENSAJE", "Cajas cargadas");
            }
            model.fireTableDataChanged();
        } else if (start != null) {
            sales.clear();
            for (Branch branch : Babas.user.getBranchs()) {
                sales.addAll(BoxSessions.getAfter(branch, start));
            }
            if (show) {
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.INFO, Notify.Location.TOP_CENTER, "MENSAJE", "Cajas cargadas");
            }
            model.fireTableDataChanged();
        } else {
            if (show) {
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.INFO, Notify.Location.TOP_CENTER, "ERROR", "Debe seleccionar un rango de fechas");
            }
        }
        btnSearch.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    public TabPane getTabPane() {
        return tabPane;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        fechaInicio = new JDateChooser(new Date());
        fechaFin = new JDateChooser(new Date());
        fechaDesde = new JDateChooser(new Date());
        fechaInicio.setDateFormatString(Utilities.getFormatoFecha());
        fechaFin.setDateFormatString(Utilities.getFormatoFecha());
        fechaDesde.setDateFormatString(Utilities.getFormatoFecha());
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
        tabPane.setLayout(new GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), 5, 5));
        panel1.add(tabPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), 10, -1));
        tabPane.add(panel2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel2.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        table = new FlatTable();
        scrollPane1.setViewportView(table);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 8, new Insets(0, 0, 0, 0), -1, -1));
        tabPane.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$(null, Font.BOLD, -1, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setText("Sucursal:");
        panel3.add(label1, new GridConstraints(0, 6, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbbBranch = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        defaultComboBoxModel1.addElement("TODAS");
        defaultComboBoxModel1.addElement("LIMA");
        defaultComboBoxModel1.addElement("CUSCO");
        cbbBranch.setModel(defaultComboBoxModel1);
        panel3.add(cbbBranch, new GridConstraints(0, 7, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        Font label2Font = this.$$$getFont$$$(null, Font.BOLD, -1, label2.getFont());
        if (label2Font != null) label2.setFont(label2Font);
        label2.setText("Fecha:");
        panel3.add(label2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbbDate = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel2 = new DefaultComboBoxModel();
        defaultComboBoxModel2.addElement("NINGUNO");
        defaultComboBoxModel2.addElement("ENTRE");
        defaultComboBoxModel2.addElement("DESDE");
        cbbDate.setModel(defaultComboBoxModel2);
        cbbDate.setSelectedIndex(2);
        panel3.add(cbbDate, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        paneEntreFecha = new JPanel();
        paneEntreFecha.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        paneEntreFecha.setVisible(false);
        panel3.add(paneEntreFecha, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        paneEntreFecha.add(fechaFin, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(150, -1), null, 0, false));
        paneEntreFecha.add(fechaInicio, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(150, -1), null, 0, false));
        btnSearch = new JButton();
        btnSearch.setText("Buscar");
        panel3.add(btnSearch, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        paneDesdeFecha = new JPanel();
        paneDesdeFecha.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(paneDesdeFecha, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        paneDesdeFecha.add(fechaDesde, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(150, -1), null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel3.add(spacer1, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
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