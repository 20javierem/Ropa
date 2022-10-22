package com.babas.views.dialogs;

import com.babas.models.Transfer;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.UtilitiesTables;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorProduct;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorTransfer;
import com.babas.utilitiesTables.tablesCellRendered.TransferCellRendered;
import com.babas.utilitiesTables.tablesModels.TransferAbstractModel;
import com.formdev.flatlaf.extras.components.FlatTable;

import javax.swing.*;
import java.awt.event.*;
import java.util.List;

public class DTransfersOnWait extends JDialog {
    private JPanel contentPane;
    private FlatTable table;
    private JButton btnHecho;
    private TransferAbstractModel model;
    private List<Transfer> transfers;

    public DTransfersOnWait(List<Transfer> transfers){
        super(Utilities.getJFrame(),"Transferencias en espera",true);
        this.transfers=transfers;
        init();
        btnHecho.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void init(){
        setContentPane(contentPane);
        loadTable();
        Utilities.getActionsOfDialog().addActionListener(e -> dispose());
        setResizable(false);
        pack();
        setLocationRelativeTo(getOwner());
    }

    private void loadTable(){
        model=new TransferAbstractModel(transfers);
        table.setModel(model);
        table.removeColumn(table.getColumn("ESTADO"));
        table.removeColumn(table.getColumn("DESCRIPCIÓN"));
        table.removeColumn(table.getColumn("CREADO"));
        table.removeColumn(table.getColumn("ORIGEN"));
        table.removeColumn(table.getColumn("ACTUALIZADO"));
        table.removeColumn(table.getColumnModel().getColumn(table.getColumnCount() - 1));
        table.getColumnModel().getColumn(table.getColumnCount() - 1).setCellEditor(new JButtonEditorTransfer(true));
        UtilitiesTables.headerNegrita(table);
        TransferCellRendered.setCellRenderer(table);
    }
}
