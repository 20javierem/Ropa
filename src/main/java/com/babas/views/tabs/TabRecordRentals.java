package com.babas.views.tabs;

import com.babas.App;
import com.babas.controllers.Rentals;
import com.babas.controllers.Sales;
import com.babas.custom.TabPane;
import com.babas.models.Branch;
import com.babas.models.Quotation;
import com.babas.models.Rental;
import com.babas.models.Sale;
import com.babas.modelsFacture.ApiClient;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.babas.utilities.UtilitiesReports;
import com.babas.utilitiesTables.UtilitiesTables;
import com.babas.utilitiesTables.buttonEditors.JButtonAction;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorRental;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorReserve;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorSale;
import com.babas.utilitiesTables.tablesCellRendered.RentalCellRendered;
import com.babas.utilitiesTables.tablesCellRendered.SaleCellRendered;
import com.babas.utilitiesTables.tablesModels.RentalAbstractModel;
import com.babas.utilitiesTables.tablesModels.SaleAbstractModel;
import com.babas.views.frames.FPrincipal;
import com.formdev.flatlaf.extras.FlatSVGIcon;
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

public class TabRecordRentals {
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
    private JButton btnSendPedings;
    private List<Rental> rentals;
    private RentalAbstractModel model;
    private TableRowSorter<RentalAbstractModel> modeloOrdenado;
    private Map<Integer, String> listaFiltros = new HashMap<Integer, String>();
    private List<RowFilter<RentalAbstractModel, String>> filtros = new ArrayList<>();
    private RowFilter filtroand;
    private Date start, end;
    private Double totalTransfer, totalCash;

    public TabRecordRentals() {
        $$$setupUI$$$();
        init();
        cbbDate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterByType();
            }
        });
        btnGenerateReport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateReport();
            }
        });
        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getRentals(true);
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
        btnSendPedings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendOnWaitSunat();
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
        if (table.getSelectedRow() != -1) {
            Rental rental = model.getList().get(table.convertRowIndexToModel(table.getSelectedRow()));
            JPanel jPanel = new JPanel();
            jPanel.add(new JLabel("Seleccione una opción"));
            JButton btnChange = new JButton("Cambiar", JButtonAction.iconChange);
            btnChange.addActionListener(e -> {
                rental.changeRental();
                JDialog jDialog = (JDialog) btnChange.getParent().getParent().getParent().getParent().getParent().getParent();
                jDialog.dispose();
            });
            JButton btnTicket = new JButton("Ver ticket", JButtonAction.iconShow);
            btnTicket.addActionListener(e -> {
                rental.showTicket();
                JDialog jDialog = (JDialog) btnChange.getParent().getParent().getParent().getParent().getParent().getParent();
                jDialog.dispose();
            });
            JButton btnCancel = new JButton("Cancelar", JButtonAction.iconError);
            btnCancel.addActionListener(e -> {
                rental.cancelRental();
                JDialog jDialog = (JDialog) btnChange.getParent().getParent().getParent().getParent().getParent().getParent();
                jDialog.dispose();
            });
            JOptionPane.showOptionDialog(Utilities.getJFrame(), jPanel, "Opciones", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, new JButton[]{btnChange, btnTicket, btnCancel}, btnChange);
        }
    }

    private void generateReport() {
        List<Rental> rentals = new ArrayList<>();
        for (int i = 0; i < table.getRowCount(); i++) {
            rentals.add(model.getList().get(table.convertRowIndexToModel(i)));
        }
        if (!rentals.isEmpty()) {
            Date start1 = start;
            Date end1 = end;
            if (start1 == null) {
                start1 = rentals.get(0).getUpdated();
            }
            if (end1 == null) {
                end1 = rentals.get(rentals.size() - 1).getUpdated();
            }
            btnGenerateReport.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            UtilitiesReports.generateReportRentals(rentals, start1, end1, totalCash, totalTransfer);
            btnGenerateReport.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        } else {
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.INFO, Notify.Location.TOP_CENTER, "MENSAJE", "No se encontraron alquileres");
        }
    }

    private void init() {
        tabPane.setTitle("Historial de alquileres");
        tabPane.getActions().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getRentals(false);
                filter();
            }
        });
        loadTable();
        loadCombos();
        filter();
        btnSendPedings.setIcon(new FlatSVGIcon(App.class.getResource("icons/svg/upload.svg")));
    }

    private void clearFilters() {
        txtSearch.setText(null);
        cbbState.setSelectedIndex(0);
        cbbType.setSelectedIndex(0);
        cbbBranch.setSelectedIndex(0);
        cbbType.setSelectedIndex(0);
        filter();
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
        rentals = new ArrayList<>();
        rentals.addAll(Rentals.getLast30());
        model = new RentalAbstractModel(rentals);
        table.setModel(model);
        UtilitiesTables.headerNegrita(table);
        RentalCellRendered.setCellRenderer(table, listaFiltros);
        table.removeColumn(table.getColumnModel().getColumn(table.getColumnCount() - 3));
        table.getColumnModel().getColumn(table.getColumnCount() - 1).setCellEditor(new JButtonEditorRental("cancel"));
        table.getColumnModel().getColumn(table.getColumnCount() - 2).setCellEditor(new JButtonEditorRental("ticket"));
        table.getColumnModel().getColumn(table.getColumnCount() - 3).setCellEditor(new JButtonEditorRental("change"));
        modeloOrdenado = new TableRowSorter<>(model);
        table.setRowSorter(modeloOrdenado);
        table.removeColumn(table.getColumn("RESUMEN"));
    }

    private void filter() {
        filtros.clear();
        String busqueda = txtSearch.getText().trim();
        filtros.add(RowFilter.regexFilter("(?i)" + busqueda, 0, 1, 2, 3, 4, 5, 6, 7, 9, 10, 11, 12, 13));
        listaFiltros.put(0, busqueda);
        listaFiltros.put(1, busqueda);
        listaFiltros.put(2, busqueda);
        listaFiltros.put(3, busqueda);
        listaFiltros.put(4, busqueda);
        listaFiltros.put(5, busqueda);
        listaFiltros.put(6, busqueda);
        listaFiltros.put(7, busqueda);
        listaFiltros.put(8, busqueda);
        listaFiltros.put(9, busqueda);
        listaFiltros.put(10, busqueda);
        listaFiltros.put(11, busqueda);
        listaFiltros.put(12, busqueda);
        if (((Branch) cbbBranch.getSelectedItem()).getId() != null) {
            Branch branch = (Branch) cbbBranch.getSelectedItem();
            filtros.add(RowFilter.regexFilter(branch.getName(), 2));
        }
        if (cbbType.getSelectedIndex() != 0) {
            filtros.add(RowFilter.regexFilter(String.valueOf(cbbType.getSelectedItem()), 4));
        }
        if (cbbState.getSelectedIndex() != 0) {
            filtros.add(RowFilter.regexFilter(String.valueOf(cbbState.getSelectedItem()), 12));
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
            Rental rental = model.getList().get(table.convertRowIndexToModel(i));
            if (rental.isCash()) {
                totalCash += rental.getTotalWithDiscount();
            } else {
                totalTransfer += rental.getTotalWithDiscount();
            }
        }
        lblTotalEfectivo.setText("Total efectivo: " + Utilities.moneda.format(totalCash));
        lblTotalTransferencias.setText("Total transferencias: " + Utilities.moneda.format(totalTransfer));
    }


    private void sendOnWaitSunat() {
        btnSendPedings.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        if (!Sales.getOnWait().isEmpty() || !Rentals.getOnWait().isEmpty()) {
            Long correlativeNota = null;
            Long correlativeBoleta = null;
            Long correlativeFactura = null;
            Sale firstSaleNota = Sales.getFirstOnWait("77");
            Sale firstSaleBoleta = Sales.getFirstOnWait("03");
            Sale firstSaleFactura = Sales.getFirstOnWait("01");
            Rental firstRentalNota = Rentals.getFirstOnWait("77");
            Rental firstRentalBoleta = Rentals.getFirstOnWait("03");
            Rental firstRentalFactura = Rentals.getFirstOnWait("01");

            if (firstSaleNota != null) {
                correlativeNota = firstSaleNota.getCorrelativo();
                if (firstRentalNota != null && firstRentalNota.getCorrelativo() < correlativeNota) {
                    correlativeNota = firstRentalNota.getCorrelativo();
                }
            } else if (firstRentalNota != null) {
                correlativeNota = firstRentalNota.getCorrelativo();
            }

            if (firstSaleBoleta != null) {
                correlativeBoleta = firstSaleBoleta.getCorrelativo();
                if (firstRentalBoleta != null && firstRentalBoleta.getCorrelativo() < correlativeBoleta) {
                    correlativeBoleta = firstRentalBoleta.getCorrelativo();
                }
            } else if (firstRentalBoleta != null) {
                correlativeBoleta = firstRentalBoleta.getCorrelativo();
            }

            if (firstSaleFactura != null) {
                correlativeFactura = firstSaleFactura.getCorrelativo();
                if (firstRentalFactura != null && firstRentalFactura.getCorrelativo() < correlativeFactura) {
                    correlativeFactura = firstRentalFactura.getCorrelativo();
                }
            } else if (firstRentalFactura != null) {
                correlativeFactura = firstRentalFactura.getCorrelativo();
            }

            if (correlativeNota != null) {
                boolean flag = true;
                while (flag) {
                    Sale sale = Sales.getByCorrelativoAndType(correlativeNota, "77");
                    Rental rental = Rentals.getByCorrelativoAndType(correlativeNota, "77");
                    if (sale != null) {
                        sale.refresh();
                        if (!sale.isStatusSunat()) {
                            sale.setStatusSunat(ApiClient.sendComprobante(ApiClient.getComprobanteOfSale(sale), true));
                        }
                        sale.save();
                        flag = sale.isStatusSunat();
                    } else if (rental != null) {
                        rental.refresh();
                        if (!rental.isStatusSunat()) {
                            rental.setStatusSunat(ApiClient.sendComprobante(ApiClient.getComprobanteOfRental(rental), true));
                        }
                        rental.save();
                        flag = rental.isStatusSunat();
                    } else {
                        flag = false;
                    }
                    correlativeNota++;
                }
            }

            if (correlativeBoleta != null) {
                boolean flag = true;
                while (flag) {
                    Sale sale = Sales.getByCorrelativoAndType(correlativeBoleta, "03");
                    Rental rental = Rentals.getByCorrelativoAndType(correlativeBoleta, "03");
                    if (sale != null) {
                        sale.refresh();
                        if (!sale.isStatusSunat()) {
                            sale.setStatusSunat(ApiClient.sendComprobante(ApiClient.getComprobanteOfSale(sale), true));
                        }
                        sale.save();
                        flag = sale.isStatusSunat();
                    } else if (rental != null) {
                        rental.refresh();
                        if (!rental.isStatusSunat()) {
                            rental.setStatusSunat(ApiClient.sendComprobante(ApiClient.getComprobanteOfRental(rental), true));
                        }
                        rental.save();
                        flag = rental.isStatusSunat();
                    } else {
                        flag = false;
                    }
                    correlativeBoleta++;
                }
            }

            if (correlativeFactura != null) {
                boolean flag = true;
                while (flag) {
                    Sale sale = Sales.getByCorrelativoAndType(correlativeFactura, "01");
                    Rental rental = Rentals.getByCorrelativoAndType(correlativeFactura, "01");
                    if (sale != null) {
                        sale.refresh();
                        if (!sale.isStatusSunat()) {
                            if (sale.isValidClient()) {
                                sale.setStatusSunat(ApiClient.sendComprobante(ApiClient.getComprobanteOfSale(sale), true));
                            } else {
                                flag = false;
                                continue;
                            }
                        }
                        sale.save();
                        flag = sale.isStatusSunat();
                    } else if (rental != null) {
                        rental.refresh();
                        if (!rental.isStatusSunat()) {
                            if (rental.isValidClient()) {
                                rental.setStatusSunat(ApiClient.sendComprobante(ApiClient.getComprobanteOfRental(rental), true));
                            } else {
                                flag = false;
                                continue;
                            }
                        }
                        rental.save();
                        flag = rental.isStatusSunat();
                    } else {
                        flag = false;
                    }
                    correlativeFactura++;
                }
            }
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER, "ÉXITO", "Comprobantes enviados");
        } else {
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.INFO, Notify.Location.TOP_CENTER, "MENSAJE", "No se encontraron comprobantes pendientes");
        }
        btnSendPedings.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    private void getRentals(boolean show) {
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
            rentals.clear();
            rentals.addAll(Rentals.getByRangeOfDate(start, end));
            if (show) {
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.INFO, Notify.Location.TOP_CENTER, "MENSAJE", "Alquileres cargados");
            }
            model.fireTableDataChanged();
        } else if (start != null) {
            rentals.clear();
            rentals.addAll(Rentals.getAfter(start));
            if (show) {
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.INFO, Notify.Location.TOP_CENTER, "MENSAJE", "Alquileres cargados");
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
        panel4.setLayout(new GridLayoutManager(1, 9, new Insets(0, 0, 0, 0), -1, -1));
        tabPane.add(panel4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnClearFilters = new JButton();
        btnClearFilters.setText("Limpiar filtros");
        panel4.add(btnClearFilters, new GridConstraints(0, 7, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        Font label2Font = this.$$$getFont$$$(null, Font.BOLD, -1, label2.getFont());
        if (label2Font != null) label2.setFont(label2Font);
        label2.setText("Sucursal:");
        panel4.add(label2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(53, 16), null, 0, false));
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
        defaultComboBoxModel3.addElement("COMPLETADO");
        defaultComboBoxModel3.addElement("CANCELADO");
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
        btnSendPedings = new JButton();
        btnSendPedings.setText("Enviar pendientes");
        panel4.add(btnSendPedings, new GridConstraints(0, 8, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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

