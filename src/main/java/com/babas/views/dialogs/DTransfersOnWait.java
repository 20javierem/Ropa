package com.babas.views.dialogs;

import com.babas.models.Transfer;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.UtilitiesTables;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorProduct;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorTransfer;
import com.babas.utilitiesTables.tablesCellRendered.TransferCellRendered;
import com.babas.utilitiesTables.tablesModels.TransferAbstractModel;
import com.formdev.flatlaf.extras.components.FlatTable;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class DTransfersOnWait extends JDialog {
    private JPanel contentPane;
    private FlatTable table;
    private JButton btnHecho;
    private TransferAbstractModel model;
    private List<Transfer> transfers;

    public DTransfersOnWait(List<Transfer> transfers) {
        super(Utilities.getJFrame(), "Transferencias en espera", true);
        this.transfers = transfers;
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

    private void init() {
        setContentPane(contentPane);
        loadTable();
        Utilities.getActionsOfDialog().addActionListener(e -> dispose());
        setResizable(false);
        pack();
        setLocationRelativeTo(getOwner());
    }

    private void loadTable() {
        model = new TransferAbstractModel(transfers);
        table.setModel(model);
        table.removeColumn(table.getColumn("NRO."));
        table.removeColumn(table.getColumn("ESTADO"));
        table.removeColumn(table.getColumn("DESCRIPCI??N"));
        table.removeColumn(table.getColumn("CREADO"));
        table.removeColumn(table.getColumn("ORIGEN"));
        table.removeColumn(table.getColumn("ACTUALIZADO"));
        table.removeColumn(table.getColumnModel().getColumn(table.getColumnCount() - 1));
        table.getColumnModel().getColumn(table.getColumnCount() - 1).setCellEditor(new JButtonEditorTransfer(true));
        UtilitiesTables.headerNegrita(table);
        TransferCellRendered.setCellRenderer(table);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel1.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        table = new FlatTable();
        scrollPane1.setViewportView(table);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_SOUTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnHecho = new JButton();
        btnHecho.setText("Hecho");
        panel2.add(btnHecho, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel2.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }
}
