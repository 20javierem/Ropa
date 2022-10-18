package com.babas.views.tabs;

import com.babas.controllers.Rentals;
import com.babas.controllers.Sales;
import com.babas.custom.TabPane;
import com.babas.models.Branch;
import com.babas.models.Rental;
import com.babas.models.Sale;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.UtilitiesTables;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorRental;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorSale;
import com.babas.utilitiesTables.tablesCellRendered.RentalCellRendered;
import com.babas.utilitiesTables.tablesCellRendered.SaleCellRendered;
import com.babas.utilitiesTables.tablesModels.RentalAbstractModel;
import com.babas.utilitiesTables.tablesModels.SaleAbstractModel;
import com.babas.views.frames.FPrincipal;
import com.formdev.flatlaf.extras.components.FlatTable;
import com.moreno.Notify;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class TabRecordRentals {
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
    private JButton btnClearFilters;
    private List<Rental> rentals;
    private RentalAbstractModel model;
    private TableRowSorter<RentalAbstractModel> modeloOrdenado;
    private List<RowFilter<RentalAbstractModel, String>> filtros = new ArrayList<>();
    private RowFilter filtroand;

    public TabRecordRentals(){
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
        btnClearFilters.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFilters();
            }
        });
    }
    private void init(){
        tabPane.setTitle("Historial de alquileres");
        loadTable();
        loadCombos();
    }
    private void clearFilters(){
        cbbState.setSelectedIndex(0);
        cbbType.setSelectedIndex(0);
        cbbBranch.setSelectedIndex(0);
        cbbType.setSelectedIndex(0);
        filter();
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
        rentals=new ArrayList<>();
        for (Branch branch : Babas.user.getBranchs()) {
            rentals.addAll(Rentals.getAfter(branch,new Date()));
        }
        model = new RentalAbstractModel(rentals);
        table.setModel(model);
        UtilitiesTables.headerNegrita(table);
        RentalCellRendered.setCellRenderer(table,null);
        table.removeColumn(table.getColumn(""));
        table.getColumnModel().getColumn(table.getColumnCount() - 1).setCellEditor(new JButtonEditorRental(false));
        modeloOrdenado = new TableRowSorter<>(model);
        table.setRowSorter(modeloOrdenado);
    }
    private void filter(){
        filtros.clear();
        if (((Branch) cbbBranch.getSelectedItem()).getId() != null) {
            Branch branch = (Branch) cbbBranch.getSelectedItem();
            filtros.add(RowFilter.regexFilter(branch.getName(), 2));
        }
        if(cbbType.getSelectedIndex()!=0){
            filtros.add(RowFilter.regexFilter(String.valueOf(cbbType.getSelectedItem()), 4));
        }
        if (cbbState.getSelectedIndex()!=0) {
            filtros.add(RowFilter.regexFilter(String.valueOf(cbbState.getSelectedItem()), 5));
        }
        filtroand = RowFilter.andFilter(filtros);
        modeloOrdenado.setRowFilter(filtroand);
    }
    private void getSales(){
        btnSearch.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        Date start = null;
        Date end = null;
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
            rentals.clear();
            for (Branch branch : Babas.user.getBranchs()) {
                rentals.addAll(Rentals.getByRangeOfDate(branch,start,end));
            }
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.INFO, Notify.Location.TOP_CENTER,"MENSAJE","Ventas cargadas");
            model.fireTableDataChanged();
        }else if(start!=null){
            rentals.clear();
            for (Branch branch : Babas.user.getBranchs()) {
                rentals.addAll(Rentals.getAfter(branch,start));
            }
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.INFO, Notify.Location.TOP_CENTER,"MENSAJE","Ventas cargadas");
            model.fireTableDataChanged();
        }else if(end!=null){
            rentals.clear();
            for (Branch branch : Babas.user.getBranchs()) {
                rentals.addAll(Rentals.getBefore(branch,end));
            }
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.INFO, Notify.Location.TOP_CENTER,"MENSAJE","Ventas cargadas");
            model.fireTableDataChanged();
        }else{
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.INFO, Notify.Location.TOP_CENTER,"ERROR","Debe seleccionar un rango de fechas");
        }
        btnSearch.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
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

