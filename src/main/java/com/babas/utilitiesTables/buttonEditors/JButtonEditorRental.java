package com.babas.utilitiesTables.buttonEditors;

import com.babas.models.Movement;
import com.babas.models.Rental;
import com.babas.models.Reserve;
import com.babas.models.Sale;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.babas.utilities.UtilitiesReports;
import com.babas.utilitiesTables.tablesModels.RentalAbstractModel;
import com.babas.utilitiesTables.tablesModels.ReserveAbstractModel;
import com.babas.utilitiesTables.tablesModels.SaleAbstractModel;
import com.babas.views.tabs.TabFinishRental;
import com.moreno.Notify;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JButtonEditorRental extends AbstractCellEditor implements TableCellEditor, ActionListener {
    private JButtonAction button;
    private boolean detail;

    public JButtonEditorRental(boolean detail) {
        this.detail=detail;
        if(detail){
            button=new JButtonAction("x16/mostrarContraseña.png","Finalizar");
        }else{
            button=new JButtonAction("x16/mostrarContraseña.png","Ticket");
        }

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
            fireEditingStopped();
            if(detail){
                if(Babas.boxSession.getId()!=null){
                    Rental rental=((RentalAbstractModel) table.getModel()).getList().get(table.convertRowIndexToModel(table.getSelectedRow()));
                    TabFinishRental tabFinishRental=new TabFinishRental(rental);
                    if(Utilities.getTabbedPane().indexOfTab(tabFinishRental.getTabPane().getTitle())==-1){
                        Utilities.getTabbedPane().addTab(tabFinishRental.getTabPane().getTitle(),tabFinishRental.getTabPane());
                    }
                    Utilities.getTabbedPane().setSelectedIndex(Utilities.getTabbedPane().indexOfTab(tabFinishRental.getTabPane().getTitle()));
                    Utilities.getTabbedPane().updateTab();
                    Utilities.updateDialog();
                }else{
                    Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","Debe aperturar caja");
                }
            }else{
                Rental rental=((RentalAbstractModel) table.getModel()).getList().get(table.convertRowIndexToModel(table.getSelectedRow()));
                if(rental.isActive()){
                    UtilitiesReports.generateTicketRental(rental,false);
                }else{
                    UtilitiesReports.generateTicketRentalFinish(rental,false);
                }

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
