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
    private String type;

    public JButtonEditorSale(String type) {
        this.type=type;
        switch (type){
            case "show":
                button=new JButtonAction("x16/mostrarContraseña.png");
                break;
            case "cancel":
                button=new JButtonAction("x16/remove.png");
                break;
            default:
                button=new JButtonAction(new FlatSVGIcon(App.class.getResource("icons/svg/check.svg")),"CONFIRMADO");
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
            switch (type){
                case "show":
                    int index=JOptionPane.showOptionDialog(Utilities.getJFrame(),"Seleccione el formato a ver","Ver ticket",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null,new Object[]{"A4", "Ticket","Cancelar"}, "A4");
                    if(index==0){
                        UtilitiesReports.generateComprobanteOfSale(true,sale,false);
                    }else if(index==1){
                        UtilitiesReports.generateComprobanteOfSale(false,sale,false);
                    }
                    break;
                case "cancel":
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
                    break;
                default:
                    if(!sale.isStatusSunat()){
                        boolean si=JOptionPane.showConfirmDialog(Utilities.getJFrame(),"¿Está seguro?","Confirmar envío a sunat",JOptionPane.YES_NO_OPTION)==0;
                        if(si){
                            if(sale.isActive()){
                                sale.setStatusSunat(ApiClient.sendComprobante(ApiClient.getComprobanteOfSale(sale)));
                                sale.save();
                            }else{
                                sale.setStatusSunat(ApiClient.cancelComprobante(ApiClient.getCancelComprobanteOfSale(sale)));
                                sale.save();
                            }
                        }
                    }
            }
            Utilities.getTabbedPane().updateTab();
            Utilities.updateDialog();
        }
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if(value instanceof Sale){
            Sale sale= (Sale) value;
            if (type.equals("sunat")) {
                if(sale.isStatusSunat()){
                    button.setIcon(new FlatSVGIcon(App.class.getResource("icons/svg/chek.svg")));
                    button.setText("CONFIRMADO");
                }else{
                    button.setIcon(new FlatSVGIcon(App.class.getResource("icons/svg/error.svg")));
                    button.setText("REENVIAR");
                }
            }
        }
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return button;
    }
}
