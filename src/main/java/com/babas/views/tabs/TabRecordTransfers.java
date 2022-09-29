package com.babas.views.tabs;

import com.babas.controllers.Transfers;
import com.babas.custom.TabPane;
import com.babas.models.*;
import com.babas.models.Color;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.UtilitiesTables;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorTransfer;
import com.babas.utilitiesTables.tablesCellRendered.TransferCellRendered;
import com.babas.utilitiesTables.tablesModels.ProductAbstractModel;
import com.babas.utilitiesTables.tablesModels.TransferAbstractModel;
import com.babas.views.frames.FPrincipal;
import com.formdev.flatlaf.extras.components.FlatTable;
import com.formdev.flatlaf.extras.components.FlatTextField;
import com.moreno.Notify;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class TabRecordTransfers {
    private TabPane tabPane;
    private JPanel paneEntreFecha;
    private JDateChooser fechaInicio;
    private JDateChooser fechaFin;
    private JPanel paneHastaFecha;
    private JDateChooser fechaHasta;
    private JPanel paneDesdeFecha;
    private JDateChooser fechaDesde;
    private JButton btnSearch;
    private JComboBox cbbDate;
    private JComboBox cbbBranch;
    private FlatTable table;
    private JComboBox cbbState;
    private JButton btnClearFilters;
    private TransferAbstractModel model;
    private Map<Integer, String> listaFiltros = new HashMap<Integer, String>();
    private TableRowSorter<TransferAbstractModel> modeloOrdenado;
    private List<RowFilter<TransferAbstractModel, String>> filtros = new ArrayList<>();
    private RowFilter filtroand;
    private List<Transfer> transfers;

    public TabRecordTransfers(){
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
                getTransfers();
            }
        });
        cbbBranch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

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
    private void init(){
        tabPane.setTitle("Historial de traslados");
        tabPane.getActions().addActionListener(e -> model.fireTableDataChanged());
        loadTable();
        loadCombos();
    }
    private void loadCombos(){
        cbbBranch.setModel(new DefaultComboBoxModel(FPrincipal.branchesWithAll));
        cbbBranch.setRenderer(new Branch.ListCellRenderer());
    }
    private void getTransfers(){
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
            transfers.clear();
            for (Branch branch : Babas.user.getBranchs()) {
                Transfers.getByRangeOfDate(branch,start,end).forEach(transfer -> {
                    if(!transfers.contains(transfer)){
                        transfers.add(transfer);
                    }
                });
            }
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.INFO, Notify.Location.TOP_CENTER,"MENSAJE","Transferencias cargadas");
            model.fireTableDataChanged();
        }else if(start!=null){
            transfers.clear();
            for (Branch branch : Babas.user.getBranchs()) {
                Transfers.getAfter(branch,start).forEach(transfer -> {
                    if(!transfers.contains(transfer)){
                        transfers.add(transfer);
                    }
                });
            }
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.INFO, Notify.Location.TOP_CENTER,"MENSAJE","Transferencias cargadas");
            model.fireTableDataChanged();
        }else if(end!=null){
            transfers.clear();
            for (Branch branch : Babas.user.getBranchs()) {
                Transfers.getBefore(branch,end).forEach(transfer -> {
                    if(!transfers.contains(transfer)){
                        transfers.add(transfer);
                    }
                });
            }
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.INFO, Notify.Location.TOP_CENTER,"MENSAJE","Transferencias cargadas");
            model.fireTableDataChanged();
        }else{
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.INFO, Notify.Location.TOP_CENTER,"ERROR","Debe seleccionar un rango de fechas");
        }
        btnSearch.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    private void filter(){
        filtros.clear();
        if (((Branch) cbbBranch.getSelectedItem()).getId() != null) {
            Branch branch = (Branch) cbbBranch.getSelectedItem();
            filtros.add(RowFilter.regexFilter(branch.getName(), 3,4));
        }
        if (cbbState.getSelectedIndex()!=0) {
            filtros.add(RowFilter.regexFilter(String.valueOf(cbbState.getSelectedItem()), 6));
        }
        filtroand = RowFilter.andFilter(filtros);
        modeloOrdenado.setRowFilter(filtroand);
    }

    public TabPane getTabPane(){
        return tabPane;
    }

    private void loadTable() {
        transfers=new ArrayList<>();
        for (Branch branch : Babas.user.getBranchs()) {
            Transfers.getAfter(branch,new Date()).forEach(transfer -> {
                if(!transfers.contains(transfer)){
                    transfers.add(transfer);
                }
            });
        }
        model = new TransferAbstractModel(transfers);
        table.setModel(model);
        UtilitiesTables.headerNegrita(table);
        TransferCellRendered.setCellRenderer(table);
        table.getColumnModel().getColumn(table.getColumnCount() - 1).setCellEditor(new JButtonEditorTransfer(false));
        table.getColumnModel().getColumn(table.getColumnCount() - 2).setCellEditor(new JButtonEditorTransfer(true));
        modeloOrdenado = new TableRowSorter<>(model);
        table.setRowSorter(modeloOrdenado);
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
