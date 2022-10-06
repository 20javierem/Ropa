package com.babas.utilitiesTables.buttonEditors;

import com.babas.models.Rental;
import com.babas.models.Reserve;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.tablesModels.RentalAbstractModel;
import com.babas.utilitiesTables.tablesModels.ReserveAbstractModel;
import com.babas.views.tabs.TabFinishRental;
import com.babas.views.tabs.TabNewRental;
import com.moreno.Notify;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JButtonEditorReserve extends AbstractCellEditor implements TableCellEditor, ActionListener {
    private JButtonAction button;

    public JButtonEditorReserve() {
        button=new JButtonAction("x16/mostrarContraseña.png");
        iniciarComponentes();
    }

    private void iniciarComponentes() {
        button.setActionCommand("edit");
        button.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JTable table = (JTable)button.getParent();
        if(table.getSelectedRow()!=-1){
            if(Babas.boxSession.getId()!=null){
                Reserve reserve=((ReserveAbstractModel) table.getModel()).getList().get(table.convertRowIndexToModel(table.getSelectedRow()));
                TabNewRental tabNewRental=new TabNewRental(new Rental(reserve));
                if(Utilities.getTabbedPane().indexOfTab(tabNewRental.getTabPane().getTitle())==-1){
                    Utilities.getTabbedPane().addTab(tabNewRental.getTabPane().getTitle(),tabNewRental.getTabPane());
                }
                Utilities.getTabbedPane().setSelectedIndex(Utilities.getTabbedPane().indexOfTab(tabNewRental.getTabPane().getTitle()));
                fireEditingStopped();
                Utilities.getTabbedPane().updateTab();
            }else{
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","Debe aperturar caja");
            }
        }
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return button;
    }
}