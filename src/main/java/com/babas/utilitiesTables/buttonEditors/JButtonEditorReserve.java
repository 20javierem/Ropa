package com.babas.utilitiesTables.buttonEditors;

import com.babas.models.Movement;
import com.babas.models.Rental;
import com.babas.models.Reserve;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.babas.utilities.UtilitiesReports;
import com.babas.utilitiesTables.tablesModels.RentalAbstractModel;
import com.babas.utilitiesTables.tablesModels.ReserveAbstractModel;
import com.babas.views.frames.FPrincipal;
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
    private String type;
    public JButtonEditorReserve(String type) {
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
            Reserve reserve=((ReserveAbstractModel) table.getModel()).getList().get(table.convertRowIndexToModel(table.getSelectedRow()));
            switch (type){
                case "detail":
                    if(Babas.boxSession.getId()!=null){
                        if(reserve.isActive()==0){
                            TabNewRental tabNewRental=new TabNewRental(new Rental(reserve));
                            if(Utilities.getTabbedPane().indexOfTab(tabNewRental.getTabPane().getTitle())==-1){
                                Utilities.getTabbedPane().addTab(tabNewRental.getTabPane().getTitle(),tabNewRental.getTabPane());
                            }
                            Utilities.getTabbedPane().setSelectedIndex(Utilities.getTabbedPane().indexOfTab(tabNewRental.getTabPane().getTitle()));
                        }else if(reserve.isActive()==1){
                            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","La reserva ya fue completada");
                        }else{
                            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","La reserva está cancelada");
                        }
                    }else{
                        Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","Debe aperturar caja");
                    }
                    break;
                case "ticket":
                    int index = JOptionPane.showOptionDialog(Utilities.getJFrame(), "Seleccione el formato a ver", "Ver ticket", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"A4", "Ticket", "Cancelar"}, "A4");
                    if (index == 0) {
                        UtilitiesReports.generateTicketReserve(true, reserve, false);
                    } else if (index == 1) {
                        UtilitiesReports.generateTicketReserve(false, reserve, false);
                    }
                    break;
                default:
                    if(Babas.boxSession.getId()!=null){
                        boolean si=JOptionPane.showConfirmDialog(Utilities.getJFrame(),"¿Está seguro?, esta acción no se puede deshacer","Cancelar reserva",JOptionPane.YES_NO_OPTION)==0;
                        if(si){
                            reserve.refresh();
                            if(reserve.isActive()!=2){
                                if(reserve.isActive()==0){
                                    reserve.setActive(2);
                                    reserve.save();
                                    Movement movement=new Movement();
                                    movement.setAmount(-reserve.getAdvance());
                                    movement.setEntrance(false);
                                    movement.setBoxSesion(Babas.boxSession);
                                    movement.setDescription("Reserva cancelada NRO: "+reserve.getNumberReserve());
                                    movement.save();
                                    movement.getBoxSesion().getMovements().add(movement);
                                    movement.getBoxSesion().calculateTotals();
                                    FPrincipal.reservesActives.remove(reserve);
                                    Utilities.getLblIzquierda().setText("Reserva cancelada Nro. " + reserve.getNumberReserve() + " : " + Utilities.formatoFechaHora.format(reserve.getUpdated()));
                                    Utilities.getLblDerecha().setText("Monto caja: " + Utilities.moneda.format(Babas.boxSession.getAmountToDelivered()));
                                    Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER,"ÉXITO","Reserva cancelada");
                                }else{
                                    Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","No puede cancelar una reserva completada");
                                }
                            }else{
                                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","La reserva ya está cancelada");
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
