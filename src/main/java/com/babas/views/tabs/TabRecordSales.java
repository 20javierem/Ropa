package com.babas.views.tabs;

import com.babas.controllers.Sales;
import com.babas.custom.TabPane;
import com.babas.models.Branch;
import com.babas.models.Sale;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.babas.utilities.UtilitiesReports;
import com.babas.utilitiesTables.UtilitiesTables;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorSale;
import com.babas.utilitiesTables.tablesCellRendered.SaleCellRendered;
import com.babas.utilitiesTables.tablesModels.SaleAbstractModel;
import com.babas.views.frames.FPrincipal;
import com.formdev.flatlaf.extras.components.FlatTable;
import com.moreno.Notify;
import com.thoughtworks.qdox.model.expression.Not;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class TabRecordSales {
    private TabPane tabPane;
    private JComboBox cbbType;
    private JDateChooser fechaInicio;
    private JDateChooser fechaFin;
    private JDateChooser fechaHasta;
    private JDateChooser fechaDesde;
    private JLabel lblTotalEfectivo;
    private JLabel lblTotalTransferencias;
    private JPanel paneEntreFecha;
    private JPanel paneHastaFecha;
    private JPanel paneDesdeFecha;
    private JButton btnSearch;
    private FlatTable table;
    private JComboBox cbbBranch;
    private JComboBox cbbDate;
    private JComboBox cbbState;
    private JButton btnGenerateReport;
    private JButton btnClearFilters;
    private List<Sale> sales;
    private SaleAbstractModel model;
    private TableRowSorter<SaleAbstractModel> modeloOrdenado;
    private List<RowFilter<SaleAbstractModel, String>> filtros = new ArrayList<>();
    private RowFilter filtroand;
    private Double totalTransfer,totalCash;
    private Date start,end;

    public TabRecordSales(){
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
                getSales();
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
        btnGenerateReport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateReport();
            }
        });
        btnClearFilters.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFilters();
            }
        });
    }
    private void clearFilters(){
        cbbBranch.setSelectedIndex(0);
        cbbType.setSelectedIndex(0);
        cbbState.setSelectedIndex(0);
        filter();
    }
    private void init(){
        tabPane.setTitle("Historial de ventas");
        loadTable();
        loadCombos();
        loadTotals();
    }

    private void generateReport(){
        List<Sale> sales=new ArrayList<>();
        for (int i = 0; i < table.getRowCount(); i++) {
            sales.add(model.getList().get(table.convertRowIndexToModel(i)));
        }
        if(!sales.isEmpty()){
            Date start1=start;
            Date end1=end;
            if(start1==null){
                start1=sales.get(0).getUpdated();
            }
            if(end1==null){
                end1=sales.get(sales.size()-1).getUpdated();
            }
            btnGenerateReport.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            UtilitiesReports.generateReportSales(sales,start1,end1,totalCash,totalTransfer);
            btnGenerateReport.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }else{
            Notify.sendNotify(Utilities.getJFrame(),Notify.Type.INFO,Notify.Location.TOP_CENTER,"MENSAJE","No se encontraron ventas");
        }
    }

    private void loadTotals(){
        totalCash=0.0;
        totalTransfer=0.0;
        for (int i = 0; i < table.getRowCount(); i++) {
            Sale sale = model.getList().get(table.convertRowIndexToModel(i));
            if(sale.isCash()){
                totalCash+=sale.getTotalCurrent();
            }else{
                totalTransfer+=sale.getTotalCurrent();
            }
        }
        lblTotalEfectivo.setText("Total efectivo: "+Utilities.moneda.format(totalCash));
        lblTotalTransferencias.setText("Total transferencias: "+Utilities.moneda.format(totalTransfer));
    }
    private void filterByType(){
        switch (cbbDate.getSelectedIndex()) {
            case 0:
                paneEntreFecha.setVisible(false);
                paneDesdeFecha.setVisible(false);
                paneHastaFecha.setVisible(false);
                break;
            case 1:
                paneEntreFecha.setVisible(true);
                paneDesdeFecha.setVisible(false);
                paneHastaFecha.setVisible(false);
                break;
            case 2:
                paneEntreFecha.setVisible(false);
                paneDesdeFecha.setVisible(true);
                paneHastaFecha.setVisible(false);
                break;
            case 3:
                paneEntreFecha.setVisible(false);
                paneDesdeFecha.setVisible(false);
                paneHastaFecha.setVisible(true);
                break;
        }
    }
    private void loadCombos(){
        cbbBranch.setModel(new DefaultComboBoxModel(FPrincipal.branchesWithAll));
        cbbBranch.setRenderer(new Branch.ListCellRenderer());
    }
    private void loadTable() {
        sales=new ArrayList<>();
        for (Branch branch : Babas.user.getBranchs()) {
            sales.addAll(Sales.getAfter(branch,new Date()));
        }
        model = new SaleAbstractModel(sales);
        table.setModel(model);
        UtilitiesTables.headerNegrita(table);
        SaleCellRendered.setCellRenderer(table,null);
        table.getColumnModel().getColumn(table.getColumnCount() - 1).setCellEditor(new JButtonEditorSale(false));
        table.getColumnModel().getColumn(table.getColumnCount() - 2).setCellEditor(new JButtonEditorSale(true));
        modeloOrdenado = new TableRowSorter<>(model);
        table.setRowSorter(modeloOrdenado);
    }
    private void filter(){
        filtros.clear();
        if (((Branch) cbbBranch.getSelectedItem()).getId() != null) {
            Branch branch = (Branch) cbbBranch.getSelectedItem();
            filtros.add(RowFilter.regexFilter(branch.getName(), 3));
        }
        if(cbbType.getSelectedIndex()!=0){
            filtros.add(RowFilter.regexFilter(String.valueOf(cbbType.getSelectedItem()), 5));
        }
        if (cbbState.getSelectedIndex()!=0) {
            filtros.add(RowFilter.regexFilter(String.valueOf(cbbState.getSelectedItem()), 6));
        }
        filtroand = RowFilter.andFilter(filtros);
        modeloOrdenado.setRowFilter(filtroand);
        loadTotals();
    }
    private void getSales(){
        btnSearch.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        if(paneEntreFecha.isVisible()){
            if(fechaInicio.getDate()!=null){
                start=fechaInicio.getDate();
            }
            if(fechaFin.getDate()!=null){
                end=fechaFin.getDate();
            }
        }
        if(paneDesdeFecha.isVisible()){
            if(fechaDesde.getDate()!=null){
                start=fechaDesde.getDate();
            }
        }
        if(paneHastaFecha.isVisible()){
            if(fechaHasta.getDate()!=null){
                end=fechaHasta.getDate();
            }
        }
        if(start!=null&&end!=null){
            sales.clear();
            for (Branch branch : Babas.user.getBranchs()) {
                sales.addAll(Sales.getByRangeOfDate(branch,start,end));
            }
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.INFO, Notify.Location.TOP_CENTER,"MENSAJE","Ventas cargadas");
            model.fireTableDataChanged();
        }else if(start!=null){
            sales.clear();
            for (Branch branch : Babas.user.getBranchs()) {
                sales.addAll(Sales.getAfter(branch,start));
            }
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.INFO, Notify.Location.TOP_CENTER,"MENSAJE","Ventas cargadas");
            model.fireTableDataChanged();
        }else if(end!=null){
            sales.clear();
            for (Branch branch : Babas.user.getBranchs()) {
                sales.addAll(Sales.getBefore(branch,end));
            }
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.INFO, Notify.Location.TOP_CENTER,"MENSAJE","Ventas cargadas");
            model.fireTableDataChanged();
        }else{
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.INFO, Notify.Location.TOP_CENTER,"ERROR","Debe seleccionar un rango de fechas");
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
        fechaDesde = new JDateChooser(new Date());
        fechaHasta = new JDateChooser(new Date());
        fechaInicio.setDateFormatString(Utilities.getFormatoFecha());
        fechaFin.setDateFormatString(Utilities.getFormatoFecha());
        fechaDesde.setDateFormatString(Utilities.getFormatoFecha());
        fechaHasta.setDateFormatString(Utilities.getFormatoFecha());
    }
}