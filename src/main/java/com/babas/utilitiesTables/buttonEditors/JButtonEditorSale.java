package com.babas.utilitiesTables.buttonEditors;

import com.babas.models.Movement;
import com.babas.models.Sale;
import com.babas.models.Transfer;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.babas.utilities.UtilitiesReports;
import com.babas.utilitiesTables.tablesModels.SaleAbstractModel;
import com.babas.utilitiesTables.tablesModels.TransferAbstractModel;
import com.babas.views.tabs.TabNewSale;
import com.babas.views.tabs.TabNewTraslade;
import com.moreno.Notify;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

public class JButtonEditorSale extends AbstractCellEditor implements TableCellEditor, ActionListener {
    private JButtonAction button;
    private boolean show;

    public JButtonEditorSale(boolean show) {
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
            Sale sale=((SaleAbstractModel) table.getModel()).getList().get(table.convertRowIndexToModel(table.getSelectedRow()));
            if(show){
                UtilitiesReports.generateTicketSale(sale);
            }else{
                if(Babas.boxSession.getId()!=null){
                    if(sale.isActive()){
                        boolean si=JOptionPane.showConfirmDialog(Utilities.getJFrame(),"¿Está seguro?, esta acción no se puede deshacer","Cancelar venta",JOptionPane.YES_NO_OPTION)==0;
                        if(si){
                            sale.refresh();
                            if(sale.isActive()){
                                sale.setActive(false);
                                sale.save();
                                Movement movement=new Movement();
                                movement.setAmount(sale.getTotalCurrent());
                                movement.setEntrance(false);
                                movement.setBoxSesion(Babas.boxSession);
                                movement.setDescription("VENTA CANCELADA NRO: "+sale.getNumberSale());
                                movement.save();
                                movement.getBoxSesion().getMovements().add(movement);
                                movement.getBoxSesion().calculateTotals();
                                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER,"ÉXITO","Venta cancelada");
                            }else{
                                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.INFO, Notify.Location.TOP_CENTER,"MENSAJE","La venta ya fué cancelada por otro usuario");
                            }
                        }
                    }else{
                        Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","La venta ya está cancelada");
                    }
                }else{
                    Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","Debe aperturar caja");
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
