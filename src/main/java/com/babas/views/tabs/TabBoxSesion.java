package com.babas.views.tabs;

import com.babas.custom.TabPane;
import com.babas.models.BoxSession;
import com.babas.models.Movement;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.UtilitiesTables;
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
        tableSales.removeColumn(tableSales.getColumnModel().getColumn(tableSales.getColumnCount()-1));
        tableSales.getColumnModel().getColumn(tableSales.getColumnCount() - 1).setCellEditor(new JButtonEditorSale(true));

        movementAbstractModel=new MovementAbstractModel(boxSession.getMovements());
        tableMovements.setModel(movementAbstractModel);
        UtilitiesTables.headerNegrita(tableMovements);
        SaleCellRendered.setCellRenderer(tableMovements,null);

        rentalAbstractModel=new RentalAbstractModel(boxSession.getRentals());
        tableRentals.setModel(rentalAbstractModel);
        UtilitiesTables.headerNegrita(tableRentals);
        RentalCellRendered.setCellRenderer(tableRentals,null);
        tableRentals.removeColumn(tableRentals.getColumn(""));
        tableRentals.removeColumn(tableRentals.getColumn("MULTA"));
        tableRentals.removeColumn(tableRentals.getColumn("TOTAL-ACTUAL"));
        tableRentals.getColumnModel().getColumn(tableRentals.getColumnCount() - 1).setCellEditor(new JButtonEditorRental(false));

        reserveAbstractModel=new ReserveAbstractModel(boxSession.getReserves());
        tableReserves.setModel(reserveAbstractModel);
        UtilitiesTables.headerNegrita(tableReserves);
        ReserveCellRendered.setCellRenderer(tableReserves,null);
        tableReserves.removeColumn(tableReserves.getColumn(""));
        tableReserves.getColumnModel().getColumn(tableReserves.getColumnCount() - 1).setCellEditor(new JButtonEditorReserve(false));
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
    }
    public TabPane getTabPane() {
        return tabPane;
    }

}
