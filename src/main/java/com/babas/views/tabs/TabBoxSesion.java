package com.babas.views.tabs;

import com.babas.custom.TabPane;
import com.babas.models.Movement;
import com.babas.models.Sale;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.UtilitiesTables;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorSale;
import com.babas.utilitiesTables.tablesCellRendered.SaleCellRendered;
import com.babas.utilitiesTables.tablesCellRendered.TransferCellRendered;
import com.babas.utilitiesTables.tablesModels.MovementAbstractModel;
import com.babas.utilitiesTables.tablesModels.RentalAbstractModel;
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
    private JLabel lblTotalSales;
    private JLabel lblTotalReserves;
    private JLabel lblTotalRentals;
    private JLabel lblTotalMovements;
    private JLabel lblTotalToDelivered;
    private JLabel lblAmountInitial;
    private JPanel paneCloseBoxSession;
    private JButton btnNewMovement;
    private JPanel paneNewMomevent;
    private FlatTable tableRentals;
    private FlatTabbedPane flatTabbedPane;
    private SaleAbstractModel saleAbstractModel;
    private MovementAbstractModel movementAbstractModel;
    private RentalAbstractModel rentalAbstractModel;

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
        SaleCellRendered.setCellRenderer(tableSales);
        tableSales.getColumnModel().getColumn(saleAbstractModel.getColumnCount() - 1).setCellEditor(new JButtonEditorSale(false));
        tableSales.getColumnModel().getColumn(saleAbstractModel.getColumnCount() - 2).setCellEditor(new JButtonEditorSale(true));

        movementAbstractModel=new MovementAbstractModel(Babas.boxSession.getMovements());
        tableMovements.setModel(movementAbstractModel);
        UtilitiesTables.headerNegrita(tableMovements);
        SaleCellRendered.setCellRenderer(tableMovements);

        rentalAbstractModel=new RentalAbstractModel(Babas.boxSession.getRentals());
        tableRentals.setModel(rentalAbstractModel);
        UtilitiesTables.headerNegrita(tableRentals);
        SaleCellRendered.setCellRenderer(tableRentals);
    }
    private void init(){
        tabPane.setTitle("Caja");
        tabPane.getActions().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saleAbstractModel.fireTableDataChanged();
                movementAbstractModel.fireTableDataChanged();
                rentalAbstractModel.fireTableDataChanged();
                loadTotals();
            }
        });
        loadTables();
        loadTotals();
    }

    private void loadTotals(){
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
