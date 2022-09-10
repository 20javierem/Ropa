package com.babas.views.tabs;

import com.babas.custom.TabPane;
import com.babas.utilities.Utilities;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;

public class TabRecordSales {
    private TabPane tabPane;
    private JComboBox cbbTipo;
    private JComboBox cbbVendedor;
    private JPanel paneEntre;
    private JTextField txtMinimo;
    private JTextField txtMaximo;
    private JPanel paneMenorque;
    private JTextField txtMayor;
    private JPanel paneMayorque;
    private JTextField txtMenor;
    private JComboBox cbbPrecio;
    private JButton btnExportar;
    private JPanel paneEntreFecha;
    private JDateChooser fechaInicio;
    private JDateChooser fechaFin;
    private JPanel paneHastaFecha;
    private JDateChooser fechaHasta;
    private JPanel paneDesdeFecha;
    private JDateChooser fechaDesde;
    private JButton btnBuscar;
    private JButton btnLimpiarFiltros;
    private JComboBox cbbFecha;
    private JComboBox cbbSucursal;
    private JTable tabla;
    private JLabel lblTotalEfectivo;
    private JLabel lblTotalTransferencias;


    public TabRecordSales(){
        initComponents();
    }
    private void initComponents(){
        tabPane.setTitle("Historial de ventas");
    }
    public TabPane getTabPane() {
        return tabPane;
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
