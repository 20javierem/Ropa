package com.babas.views.tabs;

import com.babas.custom.TabPane;
import com.babas.models.*;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.babas.utilities.UtilitiesReports;
import com.babas.utilitiesTables.UtilitiesTables;
import com.babas.utilitiesTables.buttonEditors.JButtonAction;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorRental;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorReserve;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorSale;
import com.babas.utilitiesTables.tablesCellRendered.RentalCellRendered;
import com.babas.utilitiesTables.tablesCellRendered.ReserveCellRendered;
import com.babas.utilitiesTables.tablesCellRendered.SaleCellRendered;
import com.babas.utilitiesTables.tablesModels.MovementAbstractModel;
import com.babas.utilitiesTables.tablesModels.RentalAbstractModel;
import com.babas.utilitiesTables.tablesModels.ReserveAbstractModel;
import com.babas.utilitiesTables.tablesModels.SaleAbstractModel;
import com.babas.views.dialogs.DBoxSesion;
import com.babas.views.dialogs.DMovement;
import com.formdev.flatlaf.extras.components.FlatTabbedPane;
import com.formdev.flatlaf.extras.components.FlatTable;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Locale;

public class TabBoxSesion {
    private TabPane tabPane;
    private JButton btnCloseBoxSesion;
    private FlatTable tableSales;
    private FlatTable tableMovements;
    private JLabel lblTotalMovements;
    private JLabel lblTotalToDelivered;
    private JLabel lblAmountInitial;
    private JPanel paneCloseBoxSession;
    private JButton btnNewMovement;
    private JPanel paneNewMomevent;
    private FlatTable tableRentals;
    private FlatTable tableReserves;
    private JLabel lblSalesTransfer;
    private JLabel lblSalesCash;
    private JLabel lblReservesTransfer;
    private JLabel lblReservesCash;
    private JLabel lblRentalsCash;
    private JLabel lblRentalsTransfer;
    private JLabel lblIngresos;
    private JLabel lblEgresos;
    private JLabel lblTotalSales;
    private JLabel lblTotalReserves;
    private JLabel lblTotalRentals;
    private JLabel lblMovements;
    private JLabel lblAmountInitial2;
    private JLabel lblTotalSalesCash;
    private JLabel lblTotalSalesTransfer;
    private JLabel lblTotalRentalsTransfer;
    private JLabel lblTotalReservesTransfer;
    private JLabel lblTotalRentalsCash;
    private JLabel lblTotalReservesCash;
    private JLabel lblMovements2;
    private JLabel lblTotalSales2;
    private JLabel lblTotalRentals2;
    private JLabel lblTotalReserves2;
    private JLabel lblMovements3;
    private JLabel lblTotalSales3;
    private JLabel lblSalesTransfer2;
    private JLabel lblSalesCash2;
    private JLabel lblRentalsTransfer2;
    private JLabel lblRentalsCash2;
    private JLabel lblReservesTransfer2;
    private JLabel lblReservesCash2;
    private JLabel lblTotalRentals3;
    private JLabel lblTotalReserves3;
    private JLabel lblMovements4;
    private JLabel lblIngresos2;
    private JLabel lblEgresos2;
    private JLabel lblTotalCurrent;
    private JLabel lblTotalTransfers;
    private JLabel lblTotalCash;
    private JButton btnGenerateReport;
    private FlatTabbedPane flatTabbedPane;
    private SaleAbstractModel saleAbstractModel;
    private MovementAbstractModel movementAbstractModel;
    private RentalAbstractModel rentalAbstractModel;
    private ReserveAbstractModel reserveAbstractModel;
    private BoxSession boxSession;

    public TabBoxSesion(BoxSession boxSession){
        this.boxSession=boxSession;
        init();
        btnNewMovement.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadNewMovement();
            }
        });
        btnCloseBoxSesion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeBoxSession();
            }
        });
        btnGenerateReport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateReport();
            }
        });
    }
    private void generateReport(){
        btnGenerateReport.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        UtilitiesReports.generateReportBoxSesssion(boxSession);
        btnGenerateReport.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
    private void closeBoxSession(){
        DBoxSesion dBoxSesion=new DBoxSesion();
        dBoxSesion.setVisible(true);
    }
    private void loadNewMovement(){
        DMovement dMovement=new DMovement(new Movement());
        dMovement.setVisible(true);
    }
    private void loadTables(){
        saleAbstractModel=new SaleAbstractModel(boxSession.getSales());
        tableSales.setModel(saleAbstractModel);
        UtilitiesTables.headerNegrita(tableSales);
        SaleCellRendered.setCellRenderer(tableSales,null);
        tableSales.getColumnModel().getColumn(tableSales.getColumnCount() - 2).setCellEditor(new JButtonEditorSale("show"));
        tableSales.removeColumn(tableSales.getColumnModel().getColumn(tableSales.getColumnCount()-1));
        tableSales.removeColumn(tableSales.getColumnModel().getColumn(tableSales.getColumnCount()-2));
        tableSales.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                if (me.getClickCount() == 2) {
                    if (tableSales.getSelectedRow() != -1) {
                        Sale Sale = saleAbstractModel.getList().get(tableSales.convertRowIndexToModel(tableSales.getSelectedRow()));
                        Sale.showTicket();
                    }
                }
            }
        });

        rentalAbstractModel=new RentalAbstractModel(boxSession.getRentals());
        tableRentals.setModel(rentalAbstractModel);
        UtilitiesTables.headerNegrita(tableRentals);
        RentalCellRendered.setCellRenderer(tableRentals,null);
        tableRentals.getColumnModel().getColumn(tableRentals.getColumnCount() - 2).setCellEditor(new JButtonEditorRental("ticket"));
        tableRentals.removeColumn(tableRentals.getColumnModel().getColumn(tableRentals.getColumnCount()-1));
        tableRentals.removeColumn(tableRentals.getColumnModel().getColumn(tableRentals.getColumnCount()-2));
        tableRentals.removeColumn(tableRentals.getColumn(""));
        tableRentals.removeColumn(tableRentals.getColumn("MULTA"));
        tableRentals.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                if (me.getClickCount() == 2) {
                    if (tableRentals.getSelectedRow() != -1) {
                        Rental rental = rentalAbstractModel.getList().get(tableRentals.convertRowIndexToModel(tableRentals.getSelectedRow()));
                        rental.showTicket();
                    }
                }
            }
        });

        reserveAbstractModel=new ReserveAbstractModel(boxSession.getReserves());
        tableReserves.setModel(reserveAbstractModel);
        UtilitiesTables.headerNegrita(tableReserves);
        ReserveCellRendered.setCellRenderer(tableReserves,null);
        tableReserves.getColumnModel().getColumn(tableReserves.getColumnCount() - 2).setCellEditor(new JButtonEditorReserve("ticket"));
        tableReserves.removeColumn(tableReserves.getColumnModel().getColumn(tableReserves.getColumnCount()-1));
        tableReserves.removeColumn(tableReserves.getColumn(""));
        tableReserves.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                if (me.getClickCount() == 2) {
                    if (tableReserves.getSelectedRow() != -1) {
                        Reserve reserve = reserveAbstractModel.getList().get(tableReserves.convertRowIndexToModel(tableReserves.getSelectedRow()));
                        reserve.showTicket();
                    }
                }
            }
        });

        movementAbstractModel=new MovementAbstractModel(boxSession.getMovements());
        tableMovements.setModel(movementAbstractModel);
        UtilitiesTables.headerNegrita(tableMovements);
        SaleCellRendered.setCellRenderer(tableMovements,null);
    }
    private void init(){
        if(boxSession.getId().equals(Babas.boxSession.getId())){
            tabPane.setTitle("Caja actual");
            tabPane.getActions().addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    saleAbstractModel.fireTableDataChanged();
                    movementAbstractModel.fireTableDataChanged();
                    rentalAbstractModel.fireTableDataChanged();
                    reserveAbstractModel.fireTableDataChanged();
                    loadTotals();
                }
            });
        }else{
            tabPane.setTitle(boxSession.getBox().getName()+": "+Utilities.formatoFechaHora.format(boxSession.getCreated())+" a "+Utilities.formatoFechaHora.format(boxSession.getUpdated()));
            btnCloseBoxSesion.setVisible(false);
            btnNewMovement.setVisible(false);
        }
        loadTables();
        loadTotals();
    }

    private void loadTotals(){
        lblTotalSales.setText(Utilities.moneda.format(boxSession.getTotalSales()));
        lblTotalSales2.setText(Utilities.moneda.format(boxSession.getTotalSales()));
        lblSalesTransfer.setText(Utilities.moneda.format(boxSession.getTotalSalesTransfer()));
        lblSalesCash.setText(Utilities.moneda.format(boxSession.getTotalSalesCash()));
        lblTotalSalesTransfer.setText(Utilities.moneda.format(boxSession.getTotalSalesTransfer()));
        lblTotalSalesCash.setText(Utilities.moneda.format(boxSession.getTotalSalesCash()));
        lblSalesTransfer2.setText("Total transferencias: "+Utilities.moneda.format(boxSession.getTotalSalesTransfer()));
        lblSalesCash2.setText("Total efectivo: "+Utilities.moneda.format(boxSession.getTotalSalesCash()));
        lblTotalSales3.setText("Total: "+Utilities.moneda.format(boxSession.getTotalSales()));
        lblTotalRentals.setText(Utilities.moneda.format(boxSession.getTotalRentals()));
        lblTotalReserves.setText(Utilities.moneda.format(boxSession.getTotalReserves()));
        lblTotalReserves2.setText(Utilities.moneda.format(boxSession.getTotalReserves()));
        lblMovements.setText(Utilities.moneda.format(boxSession.getTotalMovements()));
        lblMovements2.setText(Utilities.moneda.format(boxSession.getTotalMovements()));
        lblMovements3.setText(Utilities.moneda.format(boxSession.getTotalMovements()));
        lblMovements4.setText("Total movimientos: "+Utilities.moneda.format(boxSession.getTotalMovements()));
        lblIngresos.setText(Utilities.moneda.format(boxSession.getTotalIngresos()));
        lblIngresos2.setText("Total ingresos: "+Utilities.moneda.format(boxSession.getTotalIngresos()));
        lblEgresos.setText(Utilities.moneda.format(boxSession.getTotalRetiros()));
        lblEgresos2.setText("Total retiros: "+Utilities.moneda.format(boxSession.getTotalRetiros()));
        lblAmountInitial.setText(Utilities.moneda.format(boxSession.getAmountInitial()));
        lblAmountInitial2.setText(Utilities.moneda.format(boxSession.getAmountInitial()));
        lblTotalRentals3.setText("Total: "+Utilities.moneda.format(boxSession.getTotalRentals()));
        lblRentalsTransfer.setText(Utilities.moneda.format(boxSession.getTotalRentalsTransfer()));
        lblTotalRentalsTransfer.setText(Utilities.moneda.format(boxSession.getTotalRentalsTransfer()));
        lblRentalsCash2.setText("Total efectivo: "+Utilities.moneda.format(boxSession.getTotalRentalsCash()));
        lblRentalsTransfer2.setText("Total transferencia: "+Utilities.moneda.format(boxSession.getTotalRentalsTransfer()));
        lblRentalsCash.setText(Utilities.moneda.format(boxSession.getTotalRentalsCash()));
        lblTotalRentalsCash.setText(Utilities.moneda.format(boxSession.getTotalRentalsCash()));
        lblTotalRentals2.setText(Utilities.moneda.format(boxSession.getTotalRentals()));
        lblTotalTransfers.setText(Utilities.moneda.format(boxSession.getTotalTransfers()));
        lblTotalCash.setText(Utilities.moneda.format(boxSession.getTotalCash()));
        lblTotalCurrent.setText(Utilities.moneda.format(boxSession.getAmountTotal()));
        lblReservesTransfer.setText(Utilities.moneda.format(boxSession.getTotalReservesTransfer()));
        lblTotalReservesTransfer.setText(Utilities.moneda.format(boxSession.getTotalReservesTransfer()));
        lblTotalReservesCash.setText(Utilities.moneda.format(boxSession.getTotalReservesCash()));
        lblReservesTransfer2.setText("Total transferencias: "+Utilities.moneda.format(boxSession.getTotalReservesTransfer()));
        lblReservesCash2.setText("Total efectivo: "+Utilities.moneda.format(boxSession.getTotalReservesCash()));
        lblReservesCash.setText(Utilities.moneda.format(boxSession.getTotalReservesCash()));
        Utilities.getLblCentro().setText("Caja actual");
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
        tabPane.setLayout(new GridLayoutManager(2, 2, new Insets(5, 10, 10, 10), 5, 5));
        panel1.add(tabPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        paneCloseBoxSession = new JPanel();
        paneCloseBoxSession.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        tabPane.add(paneCloseBoxSession, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        paneCloseBoxSession.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        btnCloseBoxSesion = new JButton();
        btnCloseBoxSesion.setText("Cerrar caja");
        paneCloseBoxSession.add(btnCloseBoxSesion, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabPane.add(panel2, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final FlatTabbedPane flatTabbedPane1 = new FlatTabbedPane();
        flatTabbedPane1.setTabAreaAlignment(FlatTabbedPane.TabAreaAlignment.center);
        panel2.add(flatTabbedPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        flatTabbedPane1.addTab("Ventas", panel3);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), 20, -1));
        panel3.add(panel4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_SOUTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel4.add(spacer2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        lblSalesCash2 = new JLabel();
        Font lblSalesCash2Font = this.$$$getFont$$$(null, Font.BOLD, -1, lblSalesCash2.getFont());
        if (lblSalesCash2Font != null) lblSalesCash2.setFont(lblSalesCash2Font);
        lblSalesCash2.setText("Total ventas efectivo: S/0.0");
        panel4.add(lblSalesCash2, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblTotalSales3 = new JLabel();
        Font lblTotalSales3Font = this.$$$getFont$$$(null, Font.BOLD, -1, lblTotalSales3.getFont());
        if (lblTotalSales3Font != null) lblTotalSales3.setFont(lblTotalSales3Font);
        lblTotalSales3.setText("Total ventas: S/0.0");
        panel4.add(lblTotalSales3, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblSalesTransfer2 = new JLabel();
        Font lblSalesTransfer2Font = this.$$$getFont$$$(null, Font.BOLD, -1, lblSalesTransfer2.getFont());
        if (lblSalesTransfer2Font != null) lblSalesTransfer2.setFont(lblSalesTransfer2Font);
        lblSalesTransfer2.setText("Total ventas transferencia: S/0.0");
        panel4.add(lblSalesTransfer2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(panel5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel5.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        tableSales = new FlatTable();
        tableSales.setRowHeight(25);
        scrollPane1.setViewportView(tableSales);
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        flatTabbedPane1.addTab("Alquileres", panel6);
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), 20, -1));
        panel6.add(panel7, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_SOUTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel7.add(spacer3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        lblRentalsCash2 = new JLabel();
        Font lblRentalsCash2Font = this.$$$getFont$$$(null, Font.BOLD, -1, lblRentalsCash2.getFont());
        if (lblRentalsCash2Font != null) lblRentalsCash2.setFont(lblRentalsCash2Font);
        lblRentalsCash2.setText("Total alquileres efectivo: S/0.0");
        panel7.add(lblRentalsCash2, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblRentalsTransfer2 = new JLabel();
        Font lblRentalsTransfer2Font = this.$$$getFont$$$(null, Font.BOLD, -1, lblRentalsTransfer2.getFont());
        if (lblRentalsTransfer2Font != null) lblRentalsTransfer2.setFont(lblRentalsTransfer2Font);
        lblRentalsTransfer2.setText("Total alquileres transferencia: S/0.0");
        panel7.add(lblRentalsTransfer2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblTotalRentals3 = new JLabel();
        Font lblTotalRentals3Font = this.$$$getFont$$$(null, Font.BOLD, -1, lblTotalRentals3.getFont());
        if (lblTotalRentals3Font != null) lblTotalRentals3.setFont(lblTotalRentals3Font);
        lblTotalRentals3.setText("Total alquileres: S/0.0");
        panel7.add(lblTotalRentals3, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel6.add(panel8, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JScrollPane scrollPane2 = new JScrollPane();
        panel8.add(scrollPane2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        tableRentals = new FlatTable();
        tableRentals.setRowHeight(25);
        scrollPane2.setViewportView(tableRentals);
        final JPanel panel9 = new JPanel();
        panel9.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        flatTabbedPane1.addTab("Reservas", panel9);
        final JPanel panel10 = new JPanel();
        panel10.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), 20, -1));
        panel9.add(panel10, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_SOUTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        lblReservesCash2 = new JLabel();
        Font lblReservesCash2Font = this.$$$getFont$$$(null, Font.BOLD, -1, lblReservesCash2.getFont());
        if (lblReservesCash2Font != null) lblReservesCash2.setFont(lblReservesCash2Font);
        lblReservesCash2.setText("Total reservas efectivo: S/0.0");
        panel10.add(lblReservesCash2, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        panel10.add(spacer4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        lblReservesTransfer2 = new JLabel();
        Font lblReservesTransfer2Font = this.$$$getFont$$$(null, Font.BOLD, -1, lblReservesTransfer2.getFont());
        if (lblReservesTransfer2Font != null) lblReservesTransfer2.setFont(lblReservesTransfer2Font);
        lblReservesTransfer2.setText("Total reservas transferencia: S/0.0");
        panel10.add(lblReservesTransfer2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblTotalReserves3 = new JLabel();
        Font lblTotalReserves3Font = this.$$$getFont$$$(null, Font.BOLD, -1, lblTotalReserves3.getFont());
        if (lblTotalReserves3Font != null) lblTotalReserves3.setFont(lblTotalReserves3Font);
        lblTotalReserves3.setText("Total reservas: S/0.0");
        panel10.add(lblTotalReserves3, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel11 = new JPanel();
        panel11.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel9.add(panel11, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JScrollPane scrollPane3 = new JScrollPane();
        panel11.add(scrollPane3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        tableReserves = new FlatTable();
        tableReserves.setRowHeight(25);
        scrollPane3.setViewportView(tableReserves);
        final JPanel panel12 = new JPanel();
        panel12.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        flatTabbedPane1.addTab("Movimientos", panel12);
        final JPanel panel13 = new JPanel();
        panel13.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), 20, -1));
        panel12.add(panel13, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_SOUTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        lblMovements4 = new JLabel();
        Font lblMovements4Font = this.$$$getFont$$$(null, Font.BOLD, -1, lblMovements4.getFont());
        if (lblMovements4Font != null) lblMovements4.setFont(lblMovements4Font);
        lblMovements4.setText("Total movimientos: S/0.0");
        panel13.add(lblMovements4, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        panel13.add(spacer5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        lblEgresos2 = new JLabel();
        Font lblEgresos2Font = this.$$$getFont$$$(null, Font.BOLD, -1, lblEgresos2.getFont());
        if (lblEgresos2Font != null) lblEgresos2.setFont(lblEgresos2Font);
        lblEgresos2.setText("Total egresos: S/0.0");
        panel13.add(lblEgresos2, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblIngresos2 = new JLabel();
        Font lblIngresos2Font = this.$$$getFont$$$(null, Font.BOLD, -1, lblIngresos2.getFont());
        if (lblIngresos2Font != null) lblIngresos2.setFont(lblIngresos2Font);
        lblIngresos2.setText("Total ingresos: S/0.0");
        panel13.add(lblIngresos2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel14 = new JPanel();
        panel14.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel12.add(panel14, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JScrollPane scrollPane4 = new JScrollPane();
        panel14.add(scrollPane4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        tableMovements = new FlatTable();
        tableMovements.setRowHeight(25);
        scrollPane4.setViewportView(tableMovements);
        final JPanel panel15 = new JPanel();
        panel15.setLayout(new GridLayoutManager(5, 3, new Insets(0, 0, 0, 0), 10, -1));
        flatTabbedPane1.addTab("Resumen", panel15);
        final Spacer spacer6 = new Spacer();
        panel15.add(spacer6, new GridConstraints(1, 0, 3, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer7 = new Spacer();
        panel15.add(spacer7, new GridConstraints(1, 2, 3, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel16 = new JPanel();
        panel16.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        panel15.add(panel16, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel17 = new JPanel();
        panel17.setLayout(new GridLayoutManager(1, 1, new Insets(-2, -2, -2, -2), -1, -1));
        panel16.add(panel17, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel17.setBorder(BorderFactory.createTitledBorder(null, "Ventas", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, null, null));
        final JPanel panel18 = new JPanel();
        panel18.setLayout(new GridLayoutManager(5, 2, new Insets(10, 10, 10, 10), 10, -1));
        panel18.setBackground(new Color(-14680231));
        panel17.add(panel18, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$(null, Font.BOLD, -1, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setForeground(new Color(-16777216));
        label1.setText("Transferencia:");
        panel18.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblSalesTransfer = new JLabel();
        lblSalesTransfer.setForeground(new Color(-16777216));
        lblSalesTransfer.setText("S/0.00");
        panel18.add(lblSalesTransfer, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        Font label2Font = this.$$$getFont$$$(null, Font.BOLD, -1, label2.getFont());
        if (label2Font != null) label2.setFont(label2Font);
        label2.setForeground(new Color(-16777216));
        label2.setText("Efectivo:");
        panel18.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblSalesCash = new JLabel();
        lblSalesCash.setForeground(new Color(-16777216));
        lblSalesCash.setText("S/0.00");
        panel18.add(lblSalesCash, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JSeparator separator1 = new JSeparator();
        panel18.add(separator1, new GridConstraints(3, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        Font label3Font = this.$$$getFont$$$(null, Font.BOLD, -1, label3.getFont());
        if (label3Font != null) label3.setFont(label3Font);
        label3.setForeground(new Color(-16777216));
        label3.setText("Total:");
        panel18.add(label3, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblTotalSales = new JLabel();
        lblTotalSales.setForeground(new Color(-16777216));
        lblTotalSales.setText("S/0.00");
        panel18.add(lblTotalSales, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer8 = new Spacer();
        panel18.add(spacer8, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel19 = new JPanel();
        panel19.setLayout(new GridLayoutManager(1, 1, new Insets(-2, -2, -2, -2), -1, -1));
        panel16.add(panel19, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel19.setBorder(BorderFactory.createTitledBorder(null, "Reservas", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, null, null));
        final JPanel panel20 = new JPanel();
        panel20.setLayout(new GridLayoutManager(5, 2, new Insets(10, 10, 10, 10), 10, -1));
        panel20.setBackground(new Color(-14680231));
        panel19.add(panel20, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        Font label4Font = this.$$$getFont$$$(null, Font.BOLD, -1, label4.getFont());
        if (label4Font != null) label4.setFont(label4Font);
        label4.setForeground(new Color(-16777216));
        label4.setText("Transferencia:");
        panel20.add(label4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblReservesTransfer = new JLabel();
        lblReservesTransfer.setForeground(new Color(-16777216));
        lblReservesTransfer.setText("S/0.00");
        panel20.add(lblReservesTransfer, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        Font label5Font = this.$$$getFont$$$(null, Font.BOLD, -1, label5.getFont());
        if (label5Font != null) label5.setFont(label5Font);
        label5.setForeground(new Color(-16777216));
        label5.setText("Efectivo:");
        panel20.add(label5, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblReservesCash = new JLabel();
        lblReservesCash.setForeground(new Color(-16777216));
        lblReservesCash.setText("S/0.00");
        panel20.add(lblReservesCash, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer9 = new Spacer();
        panel20.add(spacer9, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JSeparator separator2 = new JSeparator();
        panel20.add(separator2, new GridConstraints(3, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        Font label6Font = this.$$$getFont$$$(null, Font.BOLD, -1, label6.getFont());
        if (label6Font != null) label6.setFont(label6Font);
        label6.setForeground(new Color(-16777216));
        label6.setText("Total:");
        panel20.add(label6, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblTotalReserves = new JLabel();
        lblTotalReserves.setForeground(new Color(-16777216));
        lblTotalReserves.setText("S/0.00");
        panel20.add(lblTotalReserves, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel21 = new JPanel();
        panel21.setLayout(new GridLayoutManager(1, 1, new Insets(-2, -2, -2, -2), -1, -1));
        panel16.add(panel21, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel21.setBorder(BorderFactory.createTitledBorder(null, "Movimientos", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, null, null));
        final JPanel panel22 = new JPanel();
        panel22.setLayout(new GridLayoutManager(5, 2, new Insets(10, 10, 10, 10), 10, -1));
        panel22.setBackground(new Color(-14680231));
        panel21.add(panel22, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        Font label7Font = this.$$$getFont$$$(null, Font.BOLD, -1, label7.getFont());
        if (label7Font != null) label7.setFont(label7Font);
        label7.setForeground(new Color(-16777216));
        label7.setText("Ingresos:");
        panel22.add(label7, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblIngresos = new JLabel();
        Font lblIngresosFont = this.$$$getFont$$$(null, -1, -1, lblIngresos.getFont());
        if (lblIngresosFont != null) lblIngresos.setFont(lblIngresosFont);
        lblIngresos.setForeground(new Color(-16777216));
        lblIngresos.setText("S/0.00");
        panel22.add(lblIngresos, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label8 = new JLabel();
        Font label8Font = this.$$$getFont$$$(null, Font.BOLD, -1, label8.getFont());
        if (label8Font != null) label8.setFont(label8Font);
        label8.setForeground(new Color(-16777216));
        label8.setText("Retiros:");
        panel22.add(label8, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblEgresos = new JLabel();
        Font lblEgresosFont = this.$$$getFont$$$(null, -1, -1, lblEgresos.getFont());
        if (lblEgresosFont != null) lblEgresos.setFont(lblEgresosFont);
        lblEgresos.setForeground(new Color(-16777216));
        lblEgresos.setText("S/0.00");
        panel22.add(lblEgresos, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JSeparator separator3 = new JSeparator();
        panel22.add(separator3, new GridConstraints(3, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label9 = new JLabel();
        Font label9Font = this.$$$getFont$$$(null, Font.BOLD, -1, label9.getFont());
        if (label9Font != null) label9.setFont(label9Font);
        label9.setForeground(new Color(-16777216));
        label9.setText("Total:");
        panel22.add(label9, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblMovements = new JLabel();
        Font lblMovementsFont = this.$$$getFont$$$(null, -1, -1, lblMovements.getFont());
        if (lblMovementsFont != null) lblMovements.setFont(lblMovementsFont);
        lblMovements.setForeground(new Color(-16777216));
        lblMovements.setText("S/0.00");
        panel22.add(lblMovements, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer10 = new Spacer();
        panel22.add(spacer10, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel23 = new JPanel();
        panel23.setLayout(new GridLayoutManager(1, 1, new Insets(-2, -2, -2, -2), -1, -1));
        panel16.add(panel23, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel23.setBorder(BorderFactory.createTitledBorder(null, "Alquileres", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, null, null));
        final JPanel panel24 = new JPanel();
        panel24.setLayout(new GridLayoutManager(5, 2, new Insets(10, 10, 10, 10), 10, -1));
        panel24.setBackground(new Color(-14680231));
        panel23.add(panel24, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label10 = new JLabel();
        Font label10Font = this.$$$getFont$$$(null, Font.BOLD, -1, label10.getFont());
        if (label10Font != null) label10.setFont(label10Font);
        label10.setForeground(new Color(-16777216));
        label10.setText("Transferencia:");
        panel24.add(label10, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblRentalsTransfer = new JLabel();
        Font lblRentalsTransferFont = this.$$$getFont$$$(null, -1, -1, lblRentalsTransfer.getFont());
        if (lblRentalsTransferFont != null) lblRentalsTransfer.setFont(lblRentalsTransferFont);
        lblRentalsTransfer.setForeground(new Color(-16777216));
        lblRentalsTransfer.setText("S/0.00");
        panel24.add(lblRentalsTransfer, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label11 = new JLabel();
        Font label11Font = this.$$$getFont$$$(null, Font.BOLD, -1, label11.getFont());
        if (label11Font != null) label11.setFont(label11Font);
        label11.setForeground(new Color(-16777216));
        label11.setText("Efectivo:");
        panel24.add(label11, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblRentalsCash = new JLabel();
        Font lblRentalsCashFont = this.$$$getFont$$$(null, -1, -1, lblRentalsCash.getFont());
        if (lblRentalsCashFont != null) lblRentalsCash.setFont(lblRentalsCashFont);
        lblRentalsCash.setForeground(new Color(-16777216));
        lblRentalsCash.setText("S/0.00");
        panel24.add(lblRentalsCash, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer11 = new Spacer();
        panel24.add(spacer11, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JSeparator separator4 = new JSeparator();
        panel24.add(separator4, new GridConstraints(3, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label12 = new JLabel();
        Font label12Font = this.$$$getFont$$$(null, Font.BOLD, -1, label12.getFont());
        if (label12Font != null) label12.setFont(label12Font);
        label12.setForeground(new Color(-16777216));
        label12.setText("Total:");
        panel24.add(label12, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblTotalRentals = new JLabel();
        Font lblTotalRentalsFont = this.$$$getFont$$$(null, -1, -1, lblTotalRentals.getFont());
        if (lblTotalRentalsFont != null) lblTotalRentals.setFont(lblTotalRentalsFont);
        lblTotalRentals.setForeground(new Color(-16777216));
        lblTotalRentals.setText("S/0.00");
        panel24.add(lblTotalRentals, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel25 = new JPanel();
        panel25.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel15.add(panel25, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel26 = new JPanel();
        panel26.setLayout(new GridLayoutManager(1, 1, new Insets(-2, -2, -2, -2), -1, -1));
        panel25.add(panel26, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel26.setBorder(BorderFactory.createTitledBorder(null, "Total efectivo", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, null, null));
        final JPanel panel27 = new JPanel();
        panel27.setLayout(new GridLayoutManager(8, 2, new Insets(10, 10, 10, 10), 10, -1));
        panel27.setBackground(new Color(-23724));
        panel26.add(panel27, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label13 = new JLabel();
        Font label13Font = this.$$$getFont$$$(null, Font.BOLD, -1, label13.getFont());
        if (label13Font != null) label13.setFont(label13Font);
        label13.setForeground(new Color(-16777216));
        label13.setText("Monto inicial:");
        panel27.add(label13, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblAmountInitial = new JLabel();
        lblAmountInitial.setForeground(new Color(-16777216));
        lblAmountInitial.setText("S/0.00");
        panel27.add(lblAmountInitial, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label14 = new JLabel();
        Font label14Font = this.$$$getFont$$$(null, Font.BOLD, -1, label14.getFont());
        if (label14Font != null) label14.setFont(label14Font);
        label14.setForeground(new Color(-16777216));
        label14.setText("Ventas:");
        panel27.add(label14, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label15 = new JLabel();
        Font label15Font = this.$$$getFont$$$(null, Font.BOLD, -1, label15.getFont());
        if (label15Font != null) label15.setFont(label15Font);
        label15.setForeground(new Color(-16777216));
        label15.setText("Alquileres:");
        panel27.add(label15, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblTotalSalesCash = new JLabel();
        lblTotalSalesCash.setForeground(new Color(-16777216));
        lblTotalSalesCash.setText("S/0.00");
        panel27.add(lblTotalSalesCash, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblTotalRentalsCash = new JLabel();
        lblTotalRentalsCash.setForeground(new Color(-16777216));
        lblTotalRentalsCash.setText("S/0.00");
        panel27.add(lblTotalRentalsCash, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label16 = new JLabel();
        Font label16Font = this.$$$getFont$$$(null, Font.BOLD, -1, label16.getFont());
        if (label16Font != null) label16.setFont(label16Font);
        label16.setForeground(new Color(-16777216));
        label16.setText("Reservas:");
        panel27.add(label16, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblTotalReservesCash = new JLabel();
        lblTotalReservesCash.setForeground(new Color(-16777216));
        lblTotalReservesCash.setText("S/0.00");
        panel27.add(lblTotalReservesCash, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label17 = new JLabel();
        Font label17Font = this.$$$getFont$$$(null, Font.BOLD, -1, label17.getFont());
        if (label17Font != null) label17.setFont(label17Font);
        label17.setForeground(new Color(-16777216));
        label17.setText("Movimientos:");
        panel27.add(label17, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblMovements2 = new JLabel();
        lblMovements2.setForeground(new Color(-16777216));
        lblMovements2.setText("S/0.00");
        panel27.add(lblMovements2, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label18 = new JLabel();
        Font label18Font = this.$$$getFont$$$(null, Font.BOLD, -1, label18.getFont());
        if (label18Font != null) label18.setFont(label18Font);
        label18.setForeground(new Color(-16777216));
        label18.setText("Total:");
        panel27.add(label18, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblTotalCash = new JLabel();
        lblTotalCash.setForeground(new Color(-16777216));
        lblTotalCash.setText("S/0.00");
        panel27.add(lblTotalCash, new GridConstraints(7, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JSeparator separator5 = new JSeparator();
        panel27.add(separator5, new GridConstraints(6, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer12 = new Spacer();
        panel27.add(spacer12, new GridConstraints(5, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel28 = new JPanel();
        panel28.setLayout(new GridLayoutManager(1, 1, new Insets(-2, -2, -2, -2), -1, -1));
        panel25.add(panel28, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel28.setBorder(BorderFactory.createTitledBorder(null, "Total transferencia", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, null, null));
        final JPanel panel29 = new JPanel();
        panel29.setLayout(new GridLayoutManager(6, 2, new Insets(10, 10, 10, 10), 10, -1));
        panel29.setBackground(new Color(-23724));
        panel28.add(panel29, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label19 = new JLabel();
        Font label19Font = this.$$$getFont$$$(null, Font.BOLD, -1, label19.getFont());
        if (label19Font != null) label19.setFont(label19Font);
        label19.setForeground(new Color(-16777216));
        label19.setText("Ventas:");
        panel29.add(label19, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label20 = new JLabel();
        Font label20Font = this.$$$getFont$$$(null, Font.BOLD, -1, label20.getFont());
        if (label20Font != null) label20.setFont(label20Font);
        label20.setForeground(new Color(-16777216));
        label20.setText("Alquileres:");
        panel29.add(label20, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblTotalSalesTransfer = new JLabel();
        lblTotalSalesTransfer.setForeground(new Color(-16777216));
        lblTotalSalesTransfer.setText("S/0.00");
        panel29.add(lblTotalSalesTransfer, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblTotalRentalsTransfer = new JLabel();
        lblTotalRentalsTransfer.setForeground(new Color(-16777216));
        lblTotalRentalsTransfer.setText("S/0.00");
        panel29.add(lblTotalRentalsTransfer, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label21 = new JLabel();
        Font label21Font = this.$$$getFont$$$(null, Font.BOLD, -1, label21.getFont());
        if (label21Font != null) label21.setFont(label21Font);
        label21.setForeground(new Color(-16777216));
        label21.setText("Reservas:");
        panel29.add(label21, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblTotalReservesTransfer = new JLabel();
        lblTotalReservesTransfer.setForeground(new Color(-16777216));
        lblTotalReservesTransfer.setText("S/0.00");
        panel29.add(lblTotalReservesTransfer, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label22 = new JLabel();
        Font label22Font = this.$$$getFont$$$(null, Font.BOLD, -1, label22.getFont());
        if (label22Font != null) label22.setFont(label22Font);
        label22.setForeground(new Color(-16777216));
        label22.setText("Total:");
        panel29.add(label22, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblTotalTransfers = new JLabel();
        lblTotalTransfers.setForeground(new Color(-16777216));
        lblTotalTransfers.setText("S/0.00");
        panel29.add(lblTotalTransfers, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JSeparator separator6 = new JSeparator();
        panel29.add(separator6, new GridConstraints(4, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer13 = new Spacer();
        panel29.add(spacer13, new GridConstraints(3, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel30 = new JPanel();
        panel30.setLayout(new GridLayoutManager(1, 1, new Insets(-2, -2, -2, -2), -1, -1));
        panel25.add(panel30, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel30.setBorder(BorderFactory.createTitledBorder(null, "Total", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, null, null));
        final JPanel panel31 = new JPanel();
        panel31.setLayout(new GridLayoutManager(8, 2, new Insets(10, 10, 10, 10), 10, -1));
        panel31.setBackground(new Color(-23724));
        panel30.add(panel31, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label23 = new JLabel();
        Font label23Font = this.$$$getFont$$$(null, Font.BOLD, -1, label23.getFont());
        if (label23Font != null) label23.setFont(label23Font);
        label23.setForeground(new Color(-16777216));
        label23.setText("Monto inicial:");
        panel31.add(label23, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblAmountInitial2 = new JLabel();
        lblAmountInitial2.setForeground(new Color(-16777216));
        lblAmountInitial2.setText("S/0.00");
        panel31.add(lblAmountInitial2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label24 = new JLabel();
        Font label24Font = this.$$$getFont$$$(null, Font.BOLD, -1, label24.getFont());
        if (label24Font != null) label24.setFont(label24Font);
        label24.setForeground(new Color(-16777216));
        label24.setText("Ventas:");
        panel31.add(label24, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label25 = new JLabel();
        Font label25Font = this.$$$getFont$$$(null, Font.BOLD, -1, label25.getFont());
        if (label25Font != null) label25.setFont(label25Font);
        label25.setForeground(new Color(-16777216));
        label25.setText("Alquileres:");
        panel31.add(label25, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblTotalSales2 = new JLabel();
        lblTotalSales2.setForeground(new Color(-16777216));
        lblTotalSales2.setText("S/0.00");
        panel31.add(lblTotalSales2, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblTotalRentals2 = new JLabel();
        lblTotalRentals2.setForeground(new Color(-16777216));
        lblTotalRentals2.setText("S/0.00");
        panel31.add(lblTotalRentals2, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label26 = new JLabel();
        Font label26Font = this.$$$getFont$$$(null, Font.BOLD, -1, label26.getFont());
        if (label26Font != null) label26.setFont(label26Font);
        label26.setForeground(new Color(-16777216));
        label26.setText("Reservas:");
        panel31.add(label26, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblTotalReserves2 = new JLabel();
        lblTotalReserves2.setForeground(new Color(-16777216));
        lblTotalReserves2.setText("S/0.00");
        panel31.add(lblTotalReserves2, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label27 = new JLabel();
        Font label27Font = this.$$$getFont$$$(null, Font.BOLD, -1, label27.getFont());
        if (label27Font != null) label27.setFont(label27Font);
        label27.setForeground(new Color(-16777216));
        label27.setText("Movimientos:");
        panel31.add(label27, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblMovements3 = new JLabel();
        lblMovements3.setForeground(new Color(-16777216));
        lblMovements3.setText("S/0.00");
        panel31.add(lblMovements3, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label28 = new JLabel();
        Font label28Font = this.$$$getFont$$$(null, Font.BOLD, -1, label28.getFont());
        if (label28Font != null) label28.setFont(label28Font);
        label28.setForeground(new Color(-16777216));
        label28.setText("Total:");
        panel31.add(label28, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblTotalCurrent = new JLabel();
        lblTotalCurrent.setForeground(new Color(-16777216));
        lblTotalCurrent.setText("S/0.00");
        panel31.add(lblTotalCurrent, new GridConstraints(7, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JSeparator separator7 = new JSeparator();
        panel31.add(separator7, new GridConstraints(6, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer14 = new Spacer();
        panel31.add(spacer14, new GridConstraints(5, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer15 = new Spacer();
        panel15.add(spacer15, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer16 = new Spacer();
        panel15.add(spacer16, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        btnGenerateReport = new JButton();
        btnGenerateReport.setIcon(new ImageIcon(getClass().getResource("/com/babas/icons/x32/pdf.png")));
        btnGenerateReport.setText("Generar reporte");
        panel15.add(btnGenerateReport, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        paneNewMomevent = new JPanel();
        paneNewMomevent.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        tabPane.add(paneNewMomevent, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnNewMovement = new JButton();
        btnNewMovement.setText("Nuevo Retiro/Ingreso");
        paneNewMomevent.add(btnNewMovement, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer17 = new Spacer();
        paneNewMomevent.add(spacer17, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        flatTabbedPane1.setLeadingComponent(paneNewMomevent);
        flatTabbedPane1.setTrailingComponent(paneCloseBoxSession);
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
