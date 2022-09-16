package com.babas.views.tabs;

import com.babas.App;
import com.babas.custom.TabPane;
import com.formdev.flatlaf.extras.components.FlatTable;
import com.formdev.flatlaf.extras.components.FlatTextField;

import javax.swing.*;
import java.awt.*;

public class TabCatalogue {
    private TabPane tabPane;
    private FlatTable table;
    private FlatTextField flatTextField;
    private JButton btnClearFilters;
    private JComboBox comboBox1;
    public TabCatalogue(){
        initComponents();
    }

    private void initComponents(){
        tabPane.setTitle("Catálogo");
    }
    public TabPane getTabPane(){
        return tabPane;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
