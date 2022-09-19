package com.babas.views.tabs;

import com.babas.custom.TabPane;
import com.babas.models.*;
import com.babas.views.frames.FPrincipal;
import com.formdev.flatlaf.extras.components.FlatTable;
import com.formdev.flatlaf.extras.components.FlatTextField;

import javax.swing.*;

public class TabCatalogue {
    private TabPane tabPane;
    private FlatTable table;
    private FlatTextField flatTextField;
    private JButton btnClearFilters;
    private JComboBox cbbBrand;
    private JComboBox cbbBranch;
    private JComboBox cbbSex;
    private JComboBox cbbCategory;
    private JComboBox cbbSize;
    private JComboBox cbbColor;

    public TabCatalogue(){
        init();
    }

    private void init(){
        tabPane.setTitle("Catálogo");
        loadCombos();
    }
    private void loadCombos(){
        cbbBranch.setModel(new DefaultComboBoxModel(FPrincipal.branchesWithAll));
        cbbBranch.setRenderer(new Branch.ListCellRenderer());
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
    public TabPane getTabPane(){
        return tabPane;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
