package com.babas.views.dialogs;

import com.babas.models.Brand;
import com.babas.models.Category;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.UtilitiesTables;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorBrand;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorCategory;
import com.babas.utilitiesTables.tablesCellRendered.ColorCellRendered;
import com.babas.utilitiesTables.tablesModels.BrandAbstractModel;
import com.babas.utilitiesTables.tablesModels.CategoryAbstractModel;
import com.babas.views.frames.FPrincipal;
import com.formdev.flatlaf.extras.components.FlatButton;
import com.formdev.flatlaf.extras.components.FlatTable;

import javax.swing.*;
import java.awt.event.*;

public class DAllBrands extends JDialog{
    private JPanel contentPane;
    private FlatTable table;
    private FlatButton btnNew;
    private FlatButton btnHecho;
    private BrandAbstractModel model;
    private ActionListener actionListener;

    public DAllBrands(){
        super(Utilities.getJFrame(),"Todas las marcas",true);
        init();
        btnNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadNew();
            }
        });
        btnHecho.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onHecho();
            }
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onHecho();
            }
        });
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onHecho();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void init(){
        setContentPane(contentPane);
        getRootPane().setDefaultButton(btnHecho);
        actionListener= e -> model.fireTableDataChanged();
        Utilities.getActionsOfDialog().addActionListener(actionListener);
        loadTable();
        pack();
        setResizable(false);
        setLocationRelativeTo(getOwner());
    }
    private void loadTable(){
        model=new BrandAbstractModel(FPrincipal.brands);
        table.setModel(model);
        UtilitiesTables.headerNegrita(table);
        ColorCellRendered.setCellRenderer(table);
        table.getColumnModel().getColumn(model.getColumnCount() - 1).setCellEditor(new JButtonEditorBrand(false));
        table.getColumnModel().getColumn(model.getColumnCount() - 2).setCellEditor(new JButtonEditorBrand(true));
    }
    private void loadNew(){
        DBrand dBrand=new DBrand(new Brand());
        dBrand.setVisible(true);
    }
    private void onHecho(){
        Utilities.getActionsOfDialog().removeActionListener(actionListener);
        dispose();
    }
}
