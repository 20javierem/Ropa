package com.babas.views.tabs;

import com.babas.custom.TabPane;
import com.babas.models.Branch;
import com.babas.models.Rental;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.UtilitiesTables;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorRental;
import com.babas.utilitiesTables.tablesCellRendered.SaleCellRendered;
import com.babas.utilitiesTables.tablesModels.RentalAbstractModel;
import com.babas.views.frames.FPrincipal;
import com.formdev.flatlaf.extras.components.FlatTable;
import com.formdev.flatlaf.extras.components.FlatTextField;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TabRentalsActives {
    private TabPane tabPane;
    private FlatTable table;
    private JLabel lblTotalTransferencias;
    private JLabel lblTotalEfectivo;
    private JComboBox cbbBranch;
    private JComboBox cbbType;
    private FlatTextField txtSearch;
    private JButton btnClearFilters;
    private List<Rental> rentals;
    private RentalAbstractModel model;
    private TableRowSorter<RentalAbstractModel> modeloOrdenado;
    private Map<Integer, String> listaFiltros = new HashMap<Integer, String>();
    private List<RowFilter<RentalAbstractModel, String>> filtros = new ArrayList<>();
    private RowFilter filtroand;

    public TabRentalsActives(){
        init();
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filter();
            }
        });
        ((JButton)txtSearch.getComponent(0)).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtSearch.setText(null);
                filter();
            }
        });
        btnClearFilters.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFilters();
            }
        });
        cbbBranch.addActionListener(new ActionListener() {
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
    }
    
    private void init(){
        tabPane.setTitle("Alquileres activos");
        tabPane.getActions().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.fireTableDataChanged();
                filter();
            }
        });
        loadCombos();
        loadTables();
    }
    private void loadCombos(){
        cbbBranch.setModel(new DefaultComboBoxModel(FPrincipal.branchesWithAll));
        cbbBranch.setRenderer(new Branch.ListCellRenderer());
    }
    private void loadTables(){
        model=new RentalAbstractModel(FPrincipal.rentalsActives);
        table.setModel(model);
        UtilitiesTables.headerNegrita(table);
        SaleCellRendered.setCellRenderer(table,listaFiltros);
        table.getColumnModel().getColumn(table.getColumnCount() - 1).setCellEditor(new JButtonEditorRental());
        modeloOrdenado = new TableRowSorter<>(model);
        table.setRowSorter(modeloOrdenado);
    }

    private void clearFilters(){
        txtSearch.setText(null);
        cbbType.setSelectedIndex(0);
        cbbBranch.setSelectedIndex(0);
        cbbType.setSelectedIndex(0);
        filter();
    }

    private void filter(){
        filtros.clear();
        String busqueda = txtSearch.getText().trim();
        filtros.add(RowFilter.regexFilter("(?i)" +busqueda,0,1,2,3,4,5,6,7));
        listaFiltros.put(0, busqueda);
        listaFiltros.put(1, busqueda);
        listaFiltros.put(2, busqueda);
        listaFiltros.put(3, busqueda);
        listaFiltros.put(4, busqueda);
        listaFiltros.put(5, busqueda);
        listaFiltros.put(6, busqueda);
        listaFiltros.put(7, busqueda);
        if (((Branch) cbbBranch.getSelectedItem()).getId() != null) {
            Branch branch = (Branch) cbbBranch.getSelectedItem();
            filtros.add(RowFilter.regexFilter(branch.getName(), 2));
        }
        if(cbbType.getSelectedIndex()!=0){
            filtros.add(RowFilter.regexFilter(String.valueOf(cbbType.getSelectedItem()), 4));
        }
        filtroand = RowFilter.andFilter(filtros);
        modeloOrdenado.setRowFilter(filtroand);
    }

    public TabPane getTabPane() {
        return tabPane;
    }
}
