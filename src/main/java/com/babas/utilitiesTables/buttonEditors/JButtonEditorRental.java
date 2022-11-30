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
import com.babas.views.frames.FPrincipal;
import com.babas.views.tabs.TabFinishRental;
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
            case "detail":
                button=new JButtonAction("x16/checkbox.png");
                break;
            case "ticket":
                button=new JButtonAction("x16/mostrarContraseña.png");
                break;
            default:
                button=new JButtonAction("x16/remove.png");
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
                case "detail":
                    if(Babas.boxSession.getId()!=null){
                        if(rental.isActive()==0){
                            TabFinishRental tabFinishRental=new TabFinishRental(rental);
                            if(Utilities.getTabbedPane().indexOfTab(tabFinishRental.getTabPane().getTitle())==-1){
                                Utilities.getTabbedPane().addTab(tabFinishRental.getTabPane().getTitle(),tabFinishRental.getTabPane());
                            }
                            Utilities.getTabbedPane().setSelectedIndex(Utilities.getTabbedPane().indexOfTab(tabFinishRental.getTabPane().getTitle()));
                        }else if(rental.isActive()==1){
                            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","El alquiler ya fue completado");
                        }else{
                            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","El alquiler está cancelado");
                        }
                    }else{
                        Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","Debe aperturar caja");
                    }
                    break;
                case "ticket":
                    if(rental.isActive()==0){
                        int index=JOptionPane.showOptionDialog(Utilities.getJFrame(),"Seleccione el formato a ver","Ver ticket",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null,new Object[]{"A4", "Ticket","Cancelar"}, "A4");
                        if(index==0){
                            UtilitiesReports.generateTicketRental(true,rental,false);
                        }else if(index==1){
                            UtilitiesReports.generateTicketRental(false,rental,false);
                        }
                    }else{
                        int index = JOptionPane.showOptionDialog(Utilities.getJFrame(), "Seleccione el formato a ver", "Ver ticket", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"A4", "Ticket", "Cancelar"}, "A4");
                        if (index == 0) {
                            UtilitiesReports.generateTicketRentalFinish(true,rental,false);
                        } else if (index == 1) {
                            UtilitiesReports.generateTicketRentalFinish(false,rental,false);
                        }
                    }
                    break;
                default:
                    if(Babas.boxSession.getId()!=null){
                        boolean si=JOptionPane.showConfirmDialog(Utilities.getJFrame(),"¿Está seguro?, esta acción no se puede deshacer","Cancelar alquiler",JOptionPane.YES_NO_OPTION)==0;
                        if(si){
                            rental.refresh();
                            if(rental.isActive()!=2){
                                rental.setActive(2);
                                rental.save();
                                Movement movement=new Movement();
                                movement.setAmount(-rental.getTotalCurrent());
                                movement.setEntrance(false);
                                movement.setBoxSesion(Babas.boxSession);
                                movement.setDescription("Alquiler cancelado NRO: "+rental.getCorrelativo());
                                movement.save();
                                movement.getBoxSesion().getMovements().add(0,movement);
                                movement.getBoxSesion().calculateTotals();
                                FPrincipal.rentalsActives.remove(rental);
                                Utilities.getLblIzquierda().setText("Alquiler cancelado Nro. " + rental.getCorrelativo() + " : " + Utilities.formatoFechaHora.format(rental.getUpdated()));
                                Utilities.getLblDerecha().setText("Monto caja: " + Utilities.moneda.format(Babas.boxSession.getAmountToDelivered()));
                                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER,"ÉXITO","Venta cancelada");
                            }else{
                                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","El alquiler ya está cancelado");
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
