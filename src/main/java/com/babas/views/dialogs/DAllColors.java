package com.babas.views.dialogs;

import com.babas.models.Color;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.UtilitiesTables;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorColor;
import com.babas.utilitiesTables.tablesCellRendered.ColorCellRendered;
import com.babas.utilitiesTables.tablesModels.ColorAbstractModel;
import com.babas.views.frames.FPrincipal;
import com.formdev.flatlaf.extras.components.FlatButton;
import com.formdev.flatlaf.extras.components.FlatTable;

import javax.swing.*;
import java.awt.event.*;

public class DAllColors extends JDialog{
    private JPanel contentPane;
    private FlatTable table;
    private FlatButton btnNewColor;
    private FlatButton btnHecho;
    private ColorAbstractModel model;

    public DAllColors(){
        super(Utilities.getJFrame(),"Todos los colores",true);
        init();
        btnNewColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadNewColor();
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
        setLocationRelativeTo(Utilities.getJFrame());
        Utilities.setActionsdOfDialog(e -> UtilitiesTables.actualizarTabla(table));
    }
    private void loadTable(){
        model=new ColorAbstractModel(FPrincipal.colors);
        table.setModel(model);
        UtilitiesTables.headerNegrita(table);
        ColorCellRendered.setCellRenderer(table);
        table.getColumnModel().getColumn(model.getColumnCount() - 1).setCellEditor(new JButtonEditorColor(false));
        table.getColumnModel().getColumn(model.getColumnCount() - 2).setCellEditor(new JButtonEditorColor(true));
    }
    private void loadNewColor(){
        DColor dColor=new DColor(new Color());
        dColor.setVisible(true);
    }
    private void onHecho(){
        dispose();
    }
}
