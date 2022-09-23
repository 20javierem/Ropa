package com.babas.utilitiesTables.buttonEditors;

import com.babas.controllers.Stocks;
import com.babas.models.Category;
import com.babas.models.Stock;
import com.babas.models.Transfer;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.tablesModels.CategoryAbstractModel;
import com.babas.utilitiesTables.tablesModels.TransferAbstractModel;
import com.babas.views.dialogs.DCategory;
import com.babas.views.frames.FPrincipal;
import com.babas.views.tabs.TabNewTraslade;
import com.moreno.Notify;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

public class JButtonEditorTransfer extends AbstractCellEditor implements TableCellEditor, ActionListener {
    private JButtonAction button;
    private boolean show;

    public JButtonEditorTransfer(boolean show) {
        this.show=show;
        if(show){
            button=new JButtonAction("x16/mostrarContraseña.png");
        }else{
            button=new JButtonAction("x16/remove.png");
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
            Transfer transfer=((TransferAbstractModel) table.getModel()).getList().get(table.convertRowIndexToModel(table.getSelectedRow()));
            if(show){
                TabNewTraslade tabNewTraslade=new TabNewTraslade(transfer);
                if(Utilities.getTabbedPane().indexOfTab(tabNewTraslade.getTabPane().getTitle())==-1){
                    Utilities.getTabbedPane().addTab(tabNewTraslade.getTabPane().getTitle(),tabNewTraslade.getTabPane());
                }
                Utilities.getTabbedPane().setSelectedIndex(Utilities.getTabbedPane().indexOfTab(tabNewTraslade.getTabPane().getTitle()));
            }else{
                if(transfer.getState()!=2){
                    boolean si=JOptionPane.showConfirmDialog(Utilities.getJFrame(),"¿Está seguro?, esta acción no se puede deshacer","Cancelar transferencia",JOptionPane.YES_NO_OPTION)==0;
                    if(si){
                        transfer.refresh();
                        transfer.getDetailTransfers().forEach( detailTransfer -> detailTransfer.getProduct().getStocks().forEach(Babas::refresh));
                        if(transfer.getState()!=2){
                            transfer.setState(2);
                            transfer.setUpdated(new Date());
                            transfer.save();
                            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER,"ÉXITO","Transferencia cancelada");
                        }else{
                            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER,"ÉXITO","La transferencia ya fue cancelada por otro usuario");
                        }

                    }
                }else{
                    Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","La transferencia ya está cancelada");
                }
            }
            fireEditingStopped();
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
