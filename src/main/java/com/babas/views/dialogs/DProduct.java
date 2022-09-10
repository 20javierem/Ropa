package com.babas.views.dialogs;

import com.babas.models.Product;
import com.babas.utilities.Utilities;
import com.formdev.flatlaf.extras.components.FlatTable;

import javax.swing.*;

public class DProduct extends JDialog{
    private JPanel contentPane;
    private JTabbedPane tabbedPane1;
    private JButton btnHecho;
    private JButton btnSave;
    private JComboBox cbbStyle;
    private JComboBox cbbCategory;
    private JButton btnNewCategory;
    private JButton btnNewSize;
    private JButton btnNewColor;
    private JComboBox cbbSize;
    private JComboBox cbbColor;
    private FlatTable table;
    private JButton btnNewPresentation;
    private Product product;
    private boolean update;

    public DProduct(Product product){
        super(Utilities.getJFrame(),"Nuevo producto",true);
        this.product=product;
        update=product.getId()!=null;
        init();
    }

    private void init(){
        setContentPane(contentPane);
        getRootPane().setDefaultButton(btnSave);

    }
}
