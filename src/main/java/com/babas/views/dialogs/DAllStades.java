package com.babas.views.dialogs;

import com.babas.models.Size;
import com.babas.models.Stade;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.UtilitiesTables;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorSize;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorStade;
import com.babas.utilitiesTables.tablesCellRendered.ColorCellRendered;
import com.babas.utilitiesTables.tablesModels.SizeAbstractModel;
import com.babas.utilitiesTables.tablesModels.StadeAbstractModel;
import com.babas.views.frames.FPrincipal;
import com.formdev.flatlaf.extras.components.FlatButton;
import com.formdev.flatlaf.extras.components.FlatTable;

import javax.swing.*;
import java.awt.event.*;

public class DAllStades extends JDialog{
    private JPanel contentPane;
    private FlatTable table;
    private FlatButton btnNew;
    private FlatButton btnHecho;
    private StadeAbstractModel model;
    private ActionListener actionListener;

    public DAllStades(){
        super(Utilities.getJFrame(),"Todos los estados",true);
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
        model=new StadeAbstractModel(FPrincipal.stades);
        table.setModel(model);
        UtilitiesTables.headerNegrita(table);
        ColorCellRendered.setCellRenderer(table);
        table.getColumnModel().getColumn(model.getColumnCount() - 1).setCellEditor(new JButtonEditorStade(false));
        table.getColumnModel().getColumn(model.getColumnCount() - 2).setCellEditor(new JButtonEditorStade(true));
    }
    private void loadNew(){
        DStade dStade=new DStade(new Stade());
        dStade.setVisible(true);
    }
    private void onHecho(){
        Utilities.getActionsOfDialog().removeActionListener(actionListener);
        dispose();
    }
}
