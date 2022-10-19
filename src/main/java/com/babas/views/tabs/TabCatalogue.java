package com.babas.views.tabs;

import com.babas.App;
import com.babas.custom.TabPane;
import com.babas.models.*;
import com.babas.models.Color;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.UtilitiesTables;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorProduct;
import com.babas.utilitiesTables.tablesCellRendered.ProductCellRendered;
import com.babas.utilitiesTables.tablesModels.ProductAbstractModel;
import com.babas.views.ModelProduct;
import com.babas.views.frames.FPrincipal;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.extras.components.FlatTable;
import com.formdev.flatlaf.extras.components.FlatTextField;
import org.jdesktop.swingx.WrapLayout;

import javax.swing.*;
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
    private JScrollPane scrollPane;
    private JButton btnPrevius;
    private JButton btnNext;
    private JPanel panelProducts;
    private ProductAbstractModel model;
    private Map<Integer, String> listaFiltros = new HashMap<Integer, String>();
    private List<RowFilter<ProductAbstractModel, String>> filtros = new ArrayList<>();
    private int position=0;
    private List<Product> productsFilters;
    private Product product;
    private String search="";

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
    private void init() {
        tabPane.setTitle("Catálogo");
        panelProducts.setLayout(new WrapLayout(WrapLayout.LEFT,12,12));
        scrollPane.getVerticalScrollBar().setUnitIncrement(35);
        loadCombos();
        loadTable();
        reloadCards();
        getTabPane().getActions().addActionListener(e -> model.fireTableDataChanged());
        btnPrevius.setIcon(new FlatSVGIcon(App.class.getResource("icons/svg/arrowCollapse.svg")));
        btnNext.setIcon(new FlatSVGIcon(App.class.getResource("icons/svg/arrowExpand.svg")));
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

    private void loadTableFilter(){
        productsFilters=new ArrayList<>(new ArrayList<>(FPrincipal.products));
        if(product.getStyle().getCategory()!=null){
            productsFilters.removeIf(product1 -> !product1.getStyle().getCategory().getId().equals(product.getStyle().getCategory().getId()));
        }
        if(product.getSize()!=null){
            productsFilters.removeIf(product1 -> !product1.getSize().getId().equals(product.getSize().getId()));
        }
        if(product.getColor()!=null){
            productsFilters.removeIf(product1 -> !product1.getColor().getId().equals(product.getColor().getId()));
        }
        if(product.getSex()!=null){
            productsFilters.removeIf(product1 -> !product1.getSex().getId().equals(product.getSex().getId()));
        }
        if(product.getBrand()!=null){
            productsFilters.removeIf(product1 -> !product1.getBrand().getId().equals(product.getBrand().getId()));
        }
        if(!search.isBlank()){
            productsFilters.removeIf(product1 -> {
                if(product1.getBarcode().toString().toLowerCase().contains(search.toLowerCase())||
                        product1.getStyle().getName().toLowerCase().contains(search.toLowerCase())||
                        product1.getSex().getName().toLowerCase().contains(search.toLowerCase())||
                        product1.getStyle().getCategory().getName().toLowerCase().contains(search.toLowerCase())||
                        product1.getBrand().getName().toLowerCase().contains(search.toLowerCase())||
                        Utilities.moneda.format(product1.getPresentationDefault().getPriceDefault().getPrice()).toLowerCase().contains(search.toLowerCase())||
                        product1.getSize().getName().toLowerCase().contains(search.toLowerCase())||
                        product1.getColor().getName().toLowerCase().contains(search.toLowerCase())||
                        product1.getStockTotal().toString().contains(search.toLowerCase())
                ){
                    return false;
                }else{
                    return true;
                }
            });
        }
        reloadTable();
        reloadCards();
    }
    private void loadTable(){
        model=new ProductAbstractModel(new ArrayList<>());
        filter();
        table.setModel(model);
        table.removeColumn(table.getColumnModel().getColumn(table.getColumnCount()-1));
        table.removeColumn(table.getColumnModel().getColumn(table.getColumnCount()-1));
        table.getColumnModel().getColumn(table.getColumnCount() - 1).setCellEditor(new JButtonEditorProduct("images"));
        UtilitiesTables.headerNegrita(table);
        ProductCellRendered.setCellRenderer(table,listaFiltros);
    }
    private void reloadCards(){
        panelProducts.removeAll();
        for(Product product: model.getList()){
            ModelProduct modelProduct =new ModelProduct(product);
            panelProducts.add(modelProduct.getContentPane());
        }
        scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMinimum());
        panelProducts.repaint();
        panelProducts.revalidate();
    }
    private void reloadTable(){
        model.setList(getProductsRows());
        model.fireTableDataChanged();
    }

    private List<Product> getProductsRows(){
        if(productsFilters.size()>102){
            if(btnPrevius.getActionListeners().length>0){
                btnPrevius.removeActionListener(btnPrevius.getActionListeners()[0]);
            }
            if(btnNext.getActionListeners().length>0){
                btnNext.removeActionListener(btnNext.getActionListeners()[0]);
            }
            btnNext.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(position<productsFilters.size()-1){
                        if(position+102<=productsFilters.size()-1){
                            position=position+102;
                        }
                    }
                    reloadTable();
                    reloadCards();
                }
            });
            btnPrevius.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(position>0){
                        position=position-102;
                    }
                    reloadTable();
                    reloadCards();
                }
            });
            if((position+102)>productsFilters.size()){
                return productsFilters.subList(position,productsFilters.size()-1);
            }else{
                return productsFilters.subList(position,position+102);
            }
        }else{
            position=0;
            return productsFilters;
        }
    }

    private void filter(){
        product=new Product();
        product.setStyle(new Style());
        search = txtSearch.getText().trim();
        filtros.add(RowFilter.regexFilter("(?i)" + search,0,1,2,3,4,5,6,7,8));
        listaFiltros.put(0, search);
        listaFiltros.put(1, search);
        listaFiltros.put(2, search);
        listaFiltros.put(3, search);
        listaFiltros.put(4, search);
        listaFiltros.put(5, search);
        listaFiltros.put(6, search);
        listaFiltros.put(7, search);
        listaFiltros.put(8, search);

        if (((Sex) cbbSex.getSelectedItem()).getId() != null) {
            Sex sex = (Sex) cbbSex.getSelectedItem();
            product.setSex(sex);
        }
        if (((Category) cbbCategory.getSelectedItem()).getId() != null) {
            Category category = (Category) cbbCategory.getSelectedItem();
            product.getStyle().setCategory(category);
        }
        if (((Brand) cbbBrand.getSelectedItem()).getId() != null) {
            Brand brand = (Brand) cbbBrand.getSelectedItem();
            product.setBrand(brand);
        }
        if (((Size) cbbSize.getSelectedItem()).getId() != null) {
            Size size = (Size) cbbSize.getSelectedItem();
            product.setSize(size);
        }
        if (((Color) cbbColor.getSelectedItem()).getId() != null) {
            Color color = (Color) cbbColor.getSelectedItem();
            product.setColor(color);
        }
        loadTableFilter();
    }
    public TabPane getTabPane(){
        return tabPane;
    }


}
