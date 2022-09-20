package com.babas.views.tabs;

import com.babas.custom.TabPane;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.UtilitiesTables;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorTransfer;
import com.babas.utilitiesTables.tablesCellRendered.TransferCellRendered;
import com.babas.utilitiesTables.tablesModels.TransferAbstractModel;
import com.babas.views.frames.FPrincipal;
import com.formdev.flatlaf.extras.components.FlatTable;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;

public class TabRecordTransfers {
    private TabPane tabPane;
    private JButton btnExportar;
    private JPanel paneEntreFecha;
    private JDateChooser fechaInicio;
    private JDateChooser fechaFin;
    private JPanel paneHastaFecha;
    private JDateChooser fechaHasta;
    private JPanel paneDesdeFecha;
    private JDateChooser fechaDesde;
    private JButton btnBuscar;
    private JComboBox cbbFecha;
    private JComboBox cbbSucursal;
    private JButton btnLimpiarFiltros;
    private JLabel lblTotalEfectivo;
    private JLabel lblTotalTransferencias;
    private FlatTable table;
    private TransferAbstractModel model;

    public TabRecordTransfers(){
        init();
    }

    private void init(){
        tabPane.setTitle("Historial de traslados");
        loadTable();

    }
    public TabPane getTabPane(){
        return tabPane;
    }

    private void loadTable() {
        model = new TransferAbstractModel(FPrincipal.transfers);
        table.setModel(model);
        UtilitiesTables.headerNegrita(table);
        TransferCellRendered.setCellRenderer(table);
        table.getColumnModel().getColumn(table.getColumnCount() - 1).setCellEditor(new JButtonEditorTransfer());
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        fechaInicio = new JDateChooser();
        fechaFin = new JDateChooser();
        fechaDesde = new JDateChooser();
        fechaHasta = new JDateChooser();
        fechaInicio.setDateFormatString(Utilities.getFormatoFecha());
        fechaFin.setDateFormatString(Utilities.getFormatoFecha());
        fechaDesde.setDateFormatString(Utilities.getFormatoFecha());
        fechaHasta.setDateFormatString(Utilities.getFormatoFecha());
    }
}
