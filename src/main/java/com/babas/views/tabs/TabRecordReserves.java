package com.babas.views.tabs;

import com.babas.controllers.Reserves;
import com.babas.custom.TabPane;
import com.babas.models.Branch;
import com.babas.models.Rental;
import com.babas.models.Reserve;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.babas.utilities.UtilitiesReports;
import com.babas.utilitiesTables.UtilitiesTables;
import com.babas.utilitiesTables.buttonEditors.JButtonAction;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorReserve;
import com.babas.utilitiesTables.tablesCellRendered.ReserveCellRendered;
import com.babas.utilitiesTables.tablesModels.ReserveAbstractModel;
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
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class TabRecordReserves {
    private TabPane tabPane;
    private JComboBox cbbType;
    private JDateChooser fechaInicio;
    private JDateChooser fechaFin;
    private JDateChooser fechaDesde;
    private JLabel lblTotalEfectivo;
    private JLabel lblTotalTransferencias;
    private JPanel paneEntreFecha;
    private JPanel paneDesdeFecha;
    private JButton btnSearch;
    private FlatTable table;
    private JComboBox cbbBranch;
    private JComboBox cbbDate;
    private JComboBox cbbState;
    private JButton btnClearFilters;
    private JButton btnGenerateReport;
    private FlatTextField txtSearch;
    private List<Reserve> reserves;
    private ReserveAbstractModel model;
    private TableRowSorter<ReserveAbstractModel> modeloOrdenado;
    private Map<Integer, String> listaFiltros = new HashMap<Integer, String>();
    private List<RowFilter<ReserveAbstractModel, String>> filtros = new ArrayList<>();
    private RowFilter filtroand;
    private Date start, end;
    private Double totalTransfer, totalCash;

    public TabRecordReserves() {
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
        btnGenerateReport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateReport();
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
        cbbType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filter();
            }
        });
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filter();
            }
        });
        ((JButton) txtSearch.getComponent(0)).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtSearch.setText(null);
                filter();
            }
        });
        btnClearFilters.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFilters();
            }
        });
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                if (me.getClickCount() == 2) {
                    loadOptions();
                }
            }
        });
    }

    private void loadOptions() {
        Reserve reserve = model.getList().get(table.convertRowIndexToModel(table.getSelectedRow()));
        reserve.showTicket();
    }

    private void clearFilters() {
        txtSearch.setText(null);
        cbbBranch.setSelectedIndex(0);
        cbbType.setSelectedIndex(0);
        cbbState.setSelectedIndex(0);
        filter();
    }

    private void init() {
        tabPane.setTitle("Historial de reservas");
        tabPane.getActions().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getSales(false);
                filter();
            }
        });
        loadTable();
        loadCombos();
        filter();
    }

    private void generateReport() {
        List<Reserve> reserves = new ArrayList<>();
        for (int i = 0; i < table.getRowCount(); i++) {
            reserves.add(model.getList().get(table.convertRowIndexToModel(i)));
        }
        if (!reserves.isEmpty()) {
            Date start1 = start;
            Date end1 = end;
            if (start1 == null) {
                start1 = reserves.get(0).getUpdated();
            }
            if (end1 == null) {
                end1 = reserves.get(reserves.size() - 1).getUpdated();
            }
            btnGenerateReport.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            UtilitiesReports.generateReportReserves(reserves, start1, end1, totalCash, totalTransfer);
            btnGenerateReport.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        } else {
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.INFO, Notify.Location.TOP_CENTER, "MENSAJE", "No se encontraron reservas");
        }
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
        reserves = new ArrayList<>();
        reserves.addAll(Reserves.getLast30());
        model = new ReserveAbstractModel(reserves);
        table.setModel(model);
        UtilitiesTables.headerNegrita(table);
        ReserveCellRendered.setCellRenderer(table, listaFiltros);
        table.removeColumn(table.getColumn(""));
        table.removeColumn(table.getColumnModel().getColumn(table.getColumnCount() - 1));
        table.getColumnModel().getColumn(table.getColumnCount() - 1).setCellEditor(new JButtonEditorReserve("ticket"));
        modeloOrdenado = new TableRowSorter<>(model);
        table.setRowSorter(modeloOrdenado);
    }

    private void filter() {
        filtros.clear();
        String busqueda = txtSearch.getText().trim();
        filtros.add(RowFilter.regexFilter("(?i)" + busqueda, 0, 1, 2, 3, 4, 5, 6, 7, 8));
        listaFiltros.put(0, busqueda);
        listaFiltros.put(1, busqueda);
        listaFiltros.put(2, busqueda);
        listaFiltros.put(3, busqueda);
        listaFiltros.put(4, busqueda);
        listaFiltros.put(5, busqueda);
        listaFiltros.put(6, busqueda);
        listaFiltros.put(7, busqueda);
        listaFiltros.put(8, busqueda);
        if (((Branch) cbbBranch.getSelectedItem()).getId() != null) {
            Branch branch = (Branch) cbbBranch.getSelectedItem();
            filtros.add(RowFilter.regexFilter(branch.getName(), 2));
        }
        if (cbbType.getSelectedIndex() != 0) {
            filtros.add(RowFilter.regexFilter(String.valueOf(cbbType.getSelectedItem()), 4));
        }
        if (cbbState.getSelectedIndex() != 0) {
            filtros.add(RowFilter.regexFilter(String.valueOf(cbbState.getSelectedItem()), 5));
        }
        filtroand = RowFilter.andFilter(filtros);
        modeloOrdenado.setRowFilter(filtroand);
        loadTotals();
        if (model.getList().size() == table.getRowCount()) {
            Utilities.getLblCentro().setText("Registros: " + model.getList().size());
        } else {
            Utilities.getLblCentro().setText("Registros filtrados: " + table.getRowCount());
        }
    }

    private void loadTotals() {
        totalCash = 0.0;
        totalTransfer = 0.0;
        for (int i = 0; i < table.getRowCount(); i++) {
            Reserve reserve = model.getList().get(table.convertRowIndexToModel(i));
            if (reserve.isCash()) {
                totalCash += reserve.getAdvance();
            } else {
                totalTransfer += reserve.getAdvance();
            }
        }
        lblTotalEfectivo.setText("Total efectivo: " + Utilities.moneda.format(totalCash));
        lblTotalTransferencias.setText("Total transferencias: " + Utilities.moneda.format(totalTransfer));
    }

    private void getSales(boolean show) {
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
        if (start != null && end != null) {
            reserves.clear();
            reserves.addAll(Reserves.getByRangeOfDate(start, end));
            if (show) {
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.INFO, Notify.Location.TOP_CENTER, "MENSAJE", "Reservas cargadas");
            }
            model.fireTableDataChanged();
        } else if (start != null) {
            reserves.clear();
            reserves.addAll(Reserves.getAfter(start));
            if (show) {
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.INFO, Notify.Location.TOP_CENTER, "MENSAJE", "Reservas cargadas");
            }
            model.fireTableDataChanged();
        } else {
            if (show) {
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.INFO, Notify.Location.TOP_CENTER, "ERROR", "Debe seleccionar un rango de fechas");
            }
        }
        btnSearch.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        filter();
    }

    public TabPane getTabPane() {
        return tabPane;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        fechaInicio = new JDateChooser(new Date());
        fechaFin = new JDateChooser(new Date());
        fechaDesde = new JDateChooser();
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
        tabPane.setLayout(new GridLayoutManager(3, 1, new Insets(10, 10, 10, 10), 5, 5));
        panel1.add(tabPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), 10, -1));
        tabPane.add(panel2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel2.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        table = new FlatTable();
        scrollPane1.setViewportView(table);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 9, new Insets(0, 10, 0, 10), 10, -1));
        tabPane.add(panel3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_SOUTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblTotalTransferencias = new JLabel();
        Font lblTotalTransferenciasFont = this.$$$getFont$$$(null, Font.BOLD, -1, lblTotalTransferencias.getFont());
        if (lblTotalTransferenciasFont != null) lblTotalTransferencias.setFont(lblTotalTransferenciasFont);
        lblTotalTransferencias.setText("Total Transferencias: S/0.0");
        panel3.add(lblTotalTransferencias, new GridConstraints(0, 8, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel3.add(spacer1, new GridConstraints(0, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        lblTotalEfectivo = new JLabel();
        Font lblTotalEfectivoFont = this.$$$getFont$$$(null, Font.BOLD, -1, lblTotalEfectivo.getFont());
        if (lblTotalEfectivoFont != null) lblTotalEfectivo.setFont(lblTotalEfectivoFont);
        lblTotalEfectivo.setText("Total Efectivo: S/0.0");
        panel3.add(lblTotalEfectivo, new GridConstraints(0, 7, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$(null, Font.BOLD, -1, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setText("Fecha:");
        panel3.add(label1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbbDate = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        defaultComboBoxModel1.addElement("NINGUNO");
        defaultComboBoxModel1.addElement("ENTRE");
        defaultComboBoxModel1.addElement("DESDE");
        cbbDate.setModel(defaultComboBoxModel1);
        cbbDate.setSelectedIndex(2);
        panel3.add(cbbDate, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        paneEntreFecha = new JPanel();
        paneEntreFecha.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        paneEntreFecha.setVisible(false);
        panel3.add(paneEntreFecha, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        paneEntreFecha.add(fechaFin, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(150, -1), null, 0, false));
        paneEntreFecha.add(fechaInicio, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(150, -1), null, 0, false));
        paneDesdeFecha = new JPanel();
        paneDesdeFecha.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(paneDesdeFecha, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        paneDesdeFecha.add(fechaDesde, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(150, -1), null, 0, false));
        btnSearch = new JButton();
        btnSearch.setText("Buscar");
        panel3.add(btnSearch, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnGenerateReport = new JButton();
        btnGenerateReport.setIcon(new ImageIcon(getClass().getResource("/com/babas/icons/x32/pdf.png")));
        btnGenerateReport.setText("Generar reporte");
        panel3.add(btnGenerateReport, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 8, new Insets(0, 0, 0, 0), -1, -1));
        tabPane.add(panel4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnClearFilters = new JButton();
        btnClearFilters.setText("Limpiar filtros");
        panel4.add(btnClearFilters, new GridConstraints(0, 7, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        Font label2Font = this.$$$getFont$$$(null, Font.BOLD, -1, label2.getFont());
        if (label2Font != null) label2.setFont(label2Font);
        label2.setText("Sucursal:");
        panel4.add(label2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbbBranch = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel2 = new DefaultComboBoxModel();
        defaultComboBoxModel2.addElement("TODAS");
        defaultComboBoxModel2.addElement("LIMA");
        defaultComboBoxModel2.addElement("CUSCO");
        cbbBranch.setModel(defaultComboBoxModel2);
        panel4.add(cbbBranch, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(90, -1), new Dimension(250, -1), new Dimension(250, -1), 0, false));
        final JLabel label3 = new JLabel();
        Font label3Font = this.$$$getFont$$$(null, Font.BOLD, -1, label3.getFont());
        if (label3Font != null) label3.setFont(label3Font);
        label3.setText("Estado:");
        panel4.add(label3, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbbState = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel3 = new DefaultComboBoxModel();
        defaultComboBoxModel3.addElement("TODAS");
        defaultComboBoxModel3.addElement("REALIZADA");
        defaultComboBoxModel3.addElement("COMPLETADA");
        defaultComboBoxModel3.addElement("CANCELADA");
        cbbState.setModel(defaultComboBoxModel3);
        panel4.add(cbbState, new GridConstraints(0, 6, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        Font label4Font = this.$$$getFont$$$(null, Font.BOLD, -1, label4.getFont());
        if (label4Font != null) label4.setFont(label4Font);
        label4.setText("Tipo/pago:");
        panel4.add(label4, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbbType = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel4 = new DefaultComboBoxModel();
        defaultComboBoxModel4.addElement("TODAS");
        defaultComboBoxModel4.addElement("EFECTIVO");
        defaultComboBoxModel4.addElement("TRANSFERENCIA");
        cbbType.setModel(defaultComboBoxModel4);
        panel4.add(cbbType, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtSearch = new FlatTextField();
        txtSearch.setPlaceholderText("Busqueda...");
        txtSearch.setShowClearButton(true);
        txtSearch.setText("");
        panel4.add(txtSearch, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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