package com.babas.views.tabs;

import com.babas.custom.TabPane;
import com.babas.models.*;
import com.babas.utilitiesTables.UtilitiesTables;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorProduct;
import com.babas.utilitiesTables.tablesCellRendered.ProductCellRendered;
import com.babas.utilitiesTables.tablesModels.ProductAbstractModel;
import com.babas.views.frames.FPrincipal;
import com.formdev.flatlaf.extras.components.FlatTable;
import com.formdev.flatlaf.extras.components.FlatTextField;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TabCatalogue {
    private TabPane tabPane;
    private FlatTable table;
    private FlatTextField txtSearch;
    private JButton btnClearFilters;
    private JComboBox cbbBrand;
    private JComboBox cbbSex;
    private JComboBox cbbCategory;
    private JComboBox cbbSize;
    private JComboBox cbbColor;
    private ProductAbstractModel model;
    private Map<Integer, String> listaFiltros = new HashMap<Integer, String>();
    private TableRowSorter<ProductAbstractModel> modeloOrdenado;
    private List<RowFilter<ProductAbstractModel, String>> filtros = new ArrayList<>();
    private RowFilter filtroand;

    public TabCatalogue(){
        init();
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filter();
            }
        });

        cbbBrand.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filter();
            }
        });
        cbbSex.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filter();
            }
        });
        cbbCategory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filter();
            }
        });
        cbbSize.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filter();
            }
        });
        cbbColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
    }
    private void clearFilters(){
        txtSearch.setText(null);
        cbbSex.setSelectedIndex(0);
        cbbCategory.setSelectedIndex(0);
        cbbBrand.setSelectedIndex(0);
        cbbSize.setSelectedIndex(0);
        cbbColor.setSelectedIndex(0);
        filter();
    }
    private void init(){
        tabPane.setTitle("Catálogo");
        loadCombos();
        loadTable();
        getTabPane().getActions().addActionListener(e -> model.fireTableDataChanged());
    }
    private void loadCombos(){
        cbbBrand.setModel(new DefaultComboBoxModel(FPrincipal.brandsWithAll));
        cbbBrand.setRenderer(new Brand.ListCellRenderer());
        cbbCategory.setModel(new DefaultComboBoxModel(FPrincipal.categoriesWithAll));
        cbbCategory.setRenderer(new Category.ListCellRenderer());
        cbbColor.setModel(new DefaultComboBoxModel(FPrincipal.colorsWithAll));
        cbbColor.setRenderer(new Color.ListCellRenderer());
        cbbSex.setModel(new DefaultComboBoxModel(FPrincipal.sexsWithAll));
        cbbSex.setRenderer(new Sex.ListCellRenderer());
        cbbSize.setModel(new DefaultComboBoxModel(FPrincipal.sizesWithAll));
        cbbSize.setRenderer(new Size.ListCellRenderer());
    }
    private void loadTable(){
        model=new ProductAbstractModel(FPrincipal.products);
        table.setModel(model);
        UtilitiesTables.headerNegrita(table);
        ProductCellRendered.setCellRenderer(table,listaFiltros);
        table.removeColumn(table.getColumnModel().getColumn(table.getColumnCount()-1));
        table.removeColumn(table.getColumnModel().getColumn(table.getColumnCount()-1));
        table.getColumnModel().getColumn(table.getColumnCount() - 1).setCellEditor(new JButtonEditorProduct("images"));
        modeloOrdenado = new TableRowSorter<>(model);
        table.setRowSorter(modeloOrdenado);
    }
    private void filter(){
        filtros.clear();
        String busqueda = txtSearch.getText().trim();
        filtros.add(RowFilter.regexFilter("(?i)" +busqueda,0,1,2,3,4,5,6,7,8));
        listaFiltros.put(0, busqueda);
        listaFiltros.put(1, busqueda);
        listaFiltros.put(2, busqueda);
        listaFiltros.put(3, busqueda);
        listaFiltros.put(4, busqueda);
        listaFiltros.put(5, busqueda);
        listaFiltros.put(6, busqueda);
        listaFiltros.put(7, busqueda);
        listaFiltros.put(8, busqueda);
        if (((Sex) cbbSex.getSelectedItem()).getId() != null) {
            Sex sex = (Sex) cbbSex.getSelectedItem();
            filtros.add(RowFilter.regexFilter(sex.getName(), 2));
        }
        if (((Category) cbbCategory.getSelectedItem()).getId() != null) {
            Category category = (Category) cbbCategory.getSelectedItem();
            filtros.add(RowFilter.regexFilter(category.getName(), 3));
        }
        if (((Brand) cbbBrand.getSelectedItem()).getId() != null) {
            Brand brand = (Brand) cbbBrand.getSelectedItem();
            filtros.add(RowFilter.regexFilter(brand.getName(), 4));
        }
        if (((Size) cbbSize.getSelectedItem()).getId() != null) {
            Size seccion = (Size) cbbSize.getSelectedItem();
            filtros.add(RowFilter.regexFilter(seccion.getName(), 6));
        }
        if (((Color) cbbColor.getSelectedItem()).getId() != null) {
            Color color = (Color) cbbColor.getSelectedItem();
            filtros.add(RowFilter.regexFilter(color.getName(), 7));
        }
        filtroand = RowFilter.andFilter(filtros);
        modeloOrdenado.setRowFilter(filtroand);
    }
    public TabPane getTabPane(){
        return tabPane;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
