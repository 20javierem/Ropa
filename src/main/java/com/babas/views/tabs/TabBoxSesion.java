package com.babas.views.tabs;

import com.babas.custom.TabPane;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.UtilitiesTables;
import com.babas.utilitiesTables.tablesCellRendered.SaleCellRendered;
import com.babas.utilitiesTables.tablesCellRendered.TransferCellRendered;
import com.babas.utilitiesTables.tablesModels.SaleAbstractModel;
import com.formdev.flatlaf.extras.components.FlatTable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TabBoxSesion {
    private TabPane tabPane;
    private JButton btnCloseBoxSesion;
    private FlatTable tableSales;
    private JLabel lblTotalSales;
    private JLabel lblTotalReserves;
    private JLabel lblTotalRentals;
    private JLabel lblTotalMovements;
    private JLabel lblTotalToDelivered;
    private JLabel lblAmountInitial;
    private JPanel paneCloseBoxSession;
    private JButton btnNewMovement;
    private JPanel paneNewMomevent;
    private SaleAbstractModel saleAbstractModel;

    public TabBoxSesion(){
        init();
    }
    private void loadTables(){
        saleAbstractModel=new SaleAbstractModel(Babas.boxSession.getSales());
        tableSales.setModel(saleAbstractModel);
        UtilitiesTables.headerNegrita(tableSales);
        SaleCellRendered.setCellRenderer(tableSales);
        tableSales.removeColumn(tableSales.getColumn(""));
        tableSales.removeColumn(tableSales.getColumn(""));
    }
    private void init(){
        tabPane.setTitle("Caja");
        tabPane.getActions().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saleAbstractModel.fireTableDataChanged();
                loadTotals();
            }
        });
        loadTables();
        loadTotals();
    }

    private void loadTotals(){
        Babas.boxSession.calculateTotals();
        lblAmountInitial.setText("Monto inicial: "+Utilities.moneda.format(Babas.boxSession.getAmountInitial()));
        lblTotalSales.setText("Total ventas: "+ Utilities.moneda.format(Babas.boxSession.getTotalSales()));
        lblTotalReserves.setText("Total reservas: "+ Utilities.moneda.format(Babas.boxSession.getTotalReserves()));
        lblTotalRentals.setText("Total alquileres: "+ Utilities.moneda.format(Babas.boxSession.getTotalRentals()));
        lblTotalMovements.setText("Total movimientos: "+ Utilities.moneda.format(Babas.boxSession.getTotalMovements()));
        lblTotalToDelivered.setText("Total a entregar: "+Utilities.moneda.format(Babas.boxSession.getAmountToDelivered()));
    }
    public TabPane getTabPane() {
        return tabPane;
    }
}
