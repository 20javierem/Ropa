package com.babas.views.tabs;

import com.babas.custom.TabPane;
import com.babas.models.Movement;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.UtilitiesTables;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorSale;
import com.babas.utilitiesTables.tablesCellRendered.ReserveCellRendered;
import com.babas.utilitiesTables.tablesCellRendered.SaleCellRendered;
import com.babas.utilitiesTables.tablesModels.MovementAbstractModel;
import com.babas.utilitiesTables.tablesModels.RentalAbstractModel;
import com.babas.utilitiesTables.tablesModels.ReserveAbstractModel;
import com.babas.utilitiesTables.tablesModels.SaleAbstractModel;
import com.babas.views.dialogs.DMovement;
import com.formdev.flatlaf.extras.components.FlatTabbedPane;
import com.formdev.flatlaf.extras.components.FlatTable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    private FlatTabbedPane flatTabbedPane;
    private SaleAbstractModel saleAbstractModel;
    private MovementAbstractModel movementAbstractModel;
    private RentalAbstractModel rentalAbstractModel;
    private ReserveAbstractModel reserveAbstractModel;

    public TabBoxSesion(){
        init();
        btnNewMovement.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadNewMovement();
            }
        });
    }
    private void loadNewMovement(){
        DMovement dMovement=new DMovement(new Movement());
        dMovement.setVisible(true);
    }
    private void loadTables(){
        saleAbstractModel=new SaleAbstractModel(Babas.boxSession.getSales());
        tableSales.setModel(saleAbstractModel);
        UtilitiesTables.headerNegrita(tableSales);
        SaleCellRendered.setCellRenderer(tableSales,null);
        tableSales.removeColumn(tableSales.getColumnModel().getColumn(tableSales.getColumnCount()-1));
        tableSales.getColumnModel().getColumn(tableSales.getColumnCount() - 1).setCellEditor(new JButtonEditorSale(true));

        movementAbstractModel=new MovementAbstractModel(Babas.boxSession.getMovements());
        tableMovements.setModel(movementAbstractModel);
        UtilitiesTables.headerNegrita(tableMovements);
        SaleCellRendered.setCellRenderer(tableMovements,null);

        rentalAbstractModel=new RentalAbstractModel(Babas.boxSession.getRentals());
        tableRentals.setModel(rentalAbstractModel);
        UtilitiesTables.headerNegrita(tableRentals);
        SaleCellRendered.setCellRenderer(tableRentals,null);
        tableRentals.removeColumn(tableRentals.getColumn("MULTA"));
        tableRentals.removeColumn(tableRentals.getColumn("TOTAL-ACTUAL"));

        reserveAbstractModel=new ReserveAbstractModel(Babas.boxSession.getReserves());
        tableReserves.setModel(reserveAbstractModel);
        UtilitiesTables.headerNegrita(tableReserves);
        ReserveCellRendered.setCellRenderer(tableReserves,null);
    }
    private void init(){
        tabPane.setTitle("Caja");
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
        loadTables();
        loadTotals();
    }

    private void loadTotals(){
        lblTotalSales.setText(Utilities.moneda.format(Babas.boxSession.getTotalSales()));
        lblTotalSales2.setText(Utilities.moneda.format(Babas.boxSession.getTotalSales()));
        lblSalesTransfer.setText(Utilities.moneda.format(Babas.boxSession.getTotalSalesTransfer()));
        lblSalesCash.setText(Utilities.moneda.format(Babas.boxSession.getTotalSalesCash()));
        lblTotalSalesTransfer.setText(Utilities.moneda.format(Babas.boxSession.getTotalSalesTransfer()));
        lblTotalSalesCash.setText(Utilities.moneda.format(Babas.boxSession.getTotalSalesCash()));
        lblSalesTransfer2.setText("Total transferencias: "+Utilities.moneda.format(Babas.boxSession.getTotalSalesTransfer()));
        lblSalesCash2.setText("Total efectivo: "+Utilities.moneda.format(Babas.boxSession.getTotalSalesCash()));
        lblTotalSales3.setText("Total: "+Utilities.moneda.format(Babas.boxSession.getTotalSales()));
        lblTotalRentals.setText(Utilities.moneda.format(Babas.boxSession.getTotalRentals()));
        lblTotalReserves.setText(Utilities.moneda.format(Babas.boxSession.getTotalReserves()));
        lblTotalReserves2.setText(Utilities.moneda.format(Babas.boxSession.getTotalReserves()));
        lblMovements.setText(Utilities.moneda.format(Babas.boxSession.getTotalMovements()));
        lblMovements2.setText(Utilities.moneda.format(Babas.boxSession.getTotalMovements()));
        lblMovements3.setText(Utilities.moneda.format(Babas.boxSession.getTotalMovements()));
        lblMovements4.setText("Total movimientos: "+Utilities.moneda.format(Babas.boxSession.getTotalMovements()));
        lblIngresos.setText(Utilities.moneda.format(Babas.boxSession.getTotalIngresos()));
        lblIngresos2.setText("Total ingresos: "+Utilities.moneda.format(Babas.boxSession.getTotalIngresos()));
        lblEgresos.setText(Utilities.moneda.format(Babas.boxSession.getTotalRetiros()));
        lblEgresos2.setText("Total retiros: "+Utilities.moneda.format(Babas.boxSession.getTotalRetiros()));
        lblAmountInitial.setText(Utilities.moneda.format(Babas.boxSession.getAmountInitial()));
        lblAmountInitial2.setText(Utilities.moneda.format(Babas.boxSession.getAmountInitial()));
        lblTotalRentals3.setText("Total: "+Utilities.moneda.format(Babas.boxSession.getTotalRentals()));
        lblRentalsTransfer.setText(Utilities.moneda.format(Babas.boxSession.getTotalRentalsTransfer()));
        lblTotalRentalsTransfer.setText(Utilities.moneda.format(Babas.boxSession.getTotalRentalsTransfer()));
        lblRentalsCash2.setText("Total efectivo: "+Utilities.moneda.format(Babas.boxSession.getTotalRentalsCash()));
        lblRentalsTransfer2.setText("Total transferencia: "+Utilities.moneda.format(Babas.boxSession.getTotalRentalsTransfer()));
        lblRentalsCash.setText(Utilities.moneda.format(Babas.boxSession.getTotalRentalsCash()));
        lblTotalRentalsCash.setText(Utilities.moneda.format(Babas.boxSession.getTotalRentalsCash()));
        lblTotalRentals2.setText(Utilities.moneda.format(Babas.boxSession.getTotalRentals()));
        lblTotalTransfers.setText(Utilities.moneda.format(Babas.boxSession.getTotalTransfers()));
        lblTotalCash.setText(Utilities.moneda.format(Babas.boxSession.getTotalCash()));
        lblTotalCurrent.setText(Utilities.moneda.format(Babas.boxSession.getAmountTotal()));
        lblReservesTransfer.setText(Utilities.moneda.format(Babas.boxSession.getTotalReservesTransfer()));
        lblTotalReservesTransfer.setText(Utilities.moneda.format(Babas.boxSession.getTotalReservesTransfer()));
        lblTotalReservesCash.setText(Utilities.moneda.format(Babas.boxSession.getTotalReservesCash()));
        lblReservesTransfer2.setText("Total transferencias: "+Utilities.moneda.format(Babas.boxSession.getTotalReservesTransfer()));
        lblReservesCash2.setText("Total efectivo: "+Utilities.moneda.format(Babas.boxSession.getTotalReservesCash()));
        lblReservesCash.setText(Utilities.moneda.format(Babas.boxSession.getTotalReservesCash()));
    }
    public TabPane getTabPane() {
        return tabPane;
    }

}
