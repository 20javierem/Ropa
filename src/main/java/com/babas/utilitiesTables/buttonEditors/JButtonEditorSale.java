package com.babas.utilitiesTables.buttonEditors;

import com.babas.App;
import com.babas.models.Movement;
import com.babas.models.Sale;
import com.babas.modelsFacture.ApiClient;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.babas.utilities.UtilitiesReports;
import com.babas.utilitiesTables.tablesModels.SaleAbstractModel;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.moreno.Notify;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
            fireEditingStopped();
            Sale sale=((SaleAbstractModel) table.getModel()).getList().get(table.convertRowIndexToModel(table.getSelectedRow()));
            if(show){
                int index=JOptionPane.showOptionDialog(Utilities.getJFrame(),"Seleccione el formato a ver","Ver ticket",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null,new Object[]{"A4", "Ticket","Cancelar"}, "A4");
                if(index==0){
                    UtilitiesReports.generateComprobanteOfSale(true,sale,false);
                }else if(index==1){
                    UtilitiesReports.generateComprobanteOfSale(false,sale,false);
                }
            }else{
                if(Babas.boxSession.getId()!=null){
                    boolean si=JOptionPane.showConfirmDialog(Utilities.getJFrame(),"¿Está seguro?, esta acción no se puede deshacer","Cancelar venta",JOptionPane.YES_NO_OPTION)==0;
                    if(si){
                        sale.refresh();
                        if(sale.isActive()){
                            sale.setActive(false);
                            sale.updateStocks();
                            Movement movement=new Movement();
                            movement.setAmount(-sale.getTotalCurrent());
                            movement.setEntrance(false);
                            movement.setBoxSesion(Babas.boxSession);
                            movement.setDescription("Venta cancelada NRO: "+sale.getId());
                            movement.getBoxSesion().getMovements().add(0,movement);
                            movement.getBoxSesion().calculateTotals();
                            movement.save();
                            Utilities.getLblIzquierda().setText("Venta cancelada Nro. " + sale.getId() + " : " + Utilities.formatoFechaHora.format(sale.getUpdated()));
                            Utilities.getLblDerecha().setText("Monto caja: " + Utilities.moneda.format(Babas.boxSession.getAmountToDelivered()));
                            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER,"ÉXITO","Venta cancelada");
                            sale.setStatusSunat(ApiClient.cancelComprobante(ApiClient.getCancelComprobanteOfSale(sale)));
                            sale.save();
                        }else{
                            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","La venta ya está cancelada");
                        }
                    }
                }else{
                    Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","Debe aperturar caja");
                }
            }
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
