package com.babas.views.dialogs;

import com.babas.models.Color;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.UtilitiesTables;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorColor;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorSex;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorSize;
import com.babas.utilitiesTables.tablesCellRendered.ColorCellRendered;
import com.babas.utilitiesTables.tablesModels.ColorAbstractModel;
import com.babas.utilitiesTables.tablesModels.SexAbstractModel;
import com.babas.views.frames.FPrincipal;
import com.formdev.flatlaf.extras.components.FlatButton;
import com.formdev.flatlaf.extras.components.FlatTable;

import javax.swing.*;
import java.awt.event.*;

public class DAllSexs extends JDialog{
    private JPanel contentPane;
    private FlatTable table;
    private FlatButton btnNew;
    private FlatButton btnHecho;
    private SexAbstractModel model;

    public DAllSexs(){
        super(Utilities.getJFrame(),"Todos los Géneros",true);
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
        loadTable();
        pack();
        setResizable(false);
        setLocationRelativeTo(getOwner());
        Utilities.setActionsdOfDialog(e -> UtilitiesTables.actualizarTabla(table));
    }
    private void loadTable(){
        model=new SexAbstractModel(FPrincipal.sexs);
        table.setModel(model);
        UtilitiesTables.headerNegrita(table);
        ColorCellRendered.setCellRenderer(table);
        table.getColumnModel().getColumn(model.getColumnCount() - 1).setCellEditor(new JButtonEditorSex(false));
        table.getColumnModel().getColumn(model.getColumnCount() - 2).setCellEditor(new JButtonEditorSex(true));
    }
    private void loadNew(){
        DColor dColor=new DColor(new Color());
        dColor.setVisible(true);
    }
    private void onHecho(){
        dispose();
    }
}
