package com.babas.utilitiesTables.buttonEditors;

import com.babas.App;
import com.babas.controllers.Rentals;
import com.babas.controllers.Sales;
import com.babas.models.*;
import com.babas.modelsFacture.ApiClient;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.babas.utilities.UtilitiesReports;
import com.babas.utilitiesTables.tablesModels.RentalAbstractModel;
import com.babas.utilitiesTables.tablesModels.ReserveAbstractModel;
import com.babas.utilitiesTables.tablesModels.SaleAbstractModel;
import com.babas.views.dialogs.DChangeVoucher;
import com.babas.views.frames.FPrincipal;
import com.babas.views.tabs.TabFinishRental;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.moreno.Notify;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JButtonEditorRental extends AbstractCellEditor implements TableCellEditor, ActionListener {
    private JButtonAction button;
    private String type;

    public JButtonEditorRental(String type) {
        this.type=type;
        switch (type){
            case "change":
                button= new JButtonAction("change");
                break;
            case "end":
                button= new JButtonAction("check");
                break;
            case "ticket":
                button=new JButtonAction("show");
                break;
            default:
                button=new JButtonAction("error");
                break;
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
            Rental rental=((RentalAbstractModel) table.getModel()).getList().get(table.convertRowIndexToModel(table.getSelectedRow()));
            switch (type){
                case "change":
                    rental.changeRental();
                    break;
                case "end":
                    rental.endRental();
                    break;
                case "ticket":
                    rental.showTicket();
                    break;
                default:
                    rental.cancelRental();
                    break;
            }
            rental.refresh();
            Utilities.getTabbedPane().updateTab();
            Utilities.updateDialog();
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
