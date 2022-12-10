package com.babas.utilitiesTables.buttonEditors;

import com.babas.App;
import com.babas.controllers.Rentals;
import com.babas.controllers.Sales;
import com.babas.models.Movement;
import com.babas.models.Rental;
import com.babas.models.Reserve;
import com.babas.models.Sale;
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
                button= new JButtonAction(new FlatSVGIcon(App.class.getResource("icons/svg/changeVoucher.svg")));
                break;
            case "end":
                button= new JButtonAction(new FlatSVGIcon(App.class.getResource("icons/svg/check.svg")));
                break;
            case "ticket":
                button=new JButtonAction(new FlatSVGIcon(App.class.getResource("icons/svg/show.svg")));
                break;
            default:
                button=new JButtonAction(new FlatSVGIcon(App.class.getResource("icons/svg/error.svg")));
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
                    if(Babas.company.isValidToken()){
                        if(rental.isActive()==1){
                            if(rental.getTypeVoucher().equals("77")){
                                if (Sales.getOnWait().isEmpty() && Rentals.getOnWait().isEmpty()) {
                                    DChangeVoucher dChangeVoucher=new DChangeVoucher(rental.getClient());
                                    int option = JOptionPane.showOptionDialog(Utilities.getJFrame(), dChangeVoucher.getContentPane(), "Cambio de comprobante", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, new Object[]{"Confirmar", "Cancelar"}, "Confirmar");
                                    if (option == JOptionPane.OK_OPTION) {
                                        rental.refresh();
                                        if(rental.isActive()==1){
                                            if(rental.getTypeVoucher().equals("77")){
                                                rental.setClient(dChangeVoucher.getClient());
                                                rental.setTypeVoucher(dChangeVoucher.getTypeVoucher());
                                                if (rental.isValidClient(rental.getTypeVoucher())) {
                                                    rental.setTypeVoucher("77");
                                                    rental.setStatusSunat(ApiClient.cancelComprobante(ApiClient.getCancelComprobanteOfRental(rental)));
                                                    if(rental.isStatusSunat()){
                                                        rental.setTypeVoucher(dChangeVoucher.getTypeVoucher());
                                                        rental.create();
                                                        rental.save();
                                                        rental.setStatusSunat(ApiClient.sendComprobante(ApiClient.getComprobanteOfRental(rental),true));
                                                        rental.save();
                                                    }
                                                }else{
                                                    rental.setTypeVoucher("77");
                                                    Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER, "ERROR", "El cliente no es válido para el tipo de comprobante");
                                                }
                                            }else{
                                                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","El documento no puede cambiarse");
                                            }
                                        }else{
                                            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","El alquiler debe estar en estado completado");
                                        }
                                    }
                                } else {
                                    Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","Primero de enviar todos los comprobantes pendientes");
                                }
                            }else{
                                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","El documento no puede cambiarse");
                            }
                        }else{
                            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","El alquiler debe estar en estado completado");
                        }

                    }
                    break;
                case "end":
                    if(Babas.boxSession.getId()!=null){
                        rental.refresh();
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
                    int index=JOptionPane.showOptionDialog(Utilities.getJFrame(),"Seleccione el formato a ver","Ver ticket",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null,new Object[]{"A4", "Ticket","Cancelar"}, "A4");
                    if(rental.getCorrelativo()!=null){
                        if(index==0){
                            UtilitiesReports.generateComprobanteOfRental(true,rental,false);
                        }else if(index==1){
                            UtilitiesReports.generateComprobanteOfRental(false,rental,false);
                        }
                    }else{
                        if(index==0){
                            UtilitiesReports.generateTicketRental(true,rental,false);
                        }else if(index==1){
                            UtilitiesReports.generateTicketRental(false,rental,false);
                        }
                    }
                    break;
                default:
                    if(Babas.boxSession.getId()!=null){
                        boolean si=JOptionPane.showConfirmDialog(Utilities.getJFrame(),"¿Está seguro?, esta acción no se puede deshacer","Cancelar alquiler",JOptionPane.YES_NO_OPTION)==0;
                        if(si){
                            rental.refresh();
                            boolean toSunat=rental.isActive()!=0;
                            if(rental.isActive()!=2){
                                rental.setActive(2);
                                rental.updateStocks();
                                Movement movement=new Movement();
                                movement.setAmount(-rental.getTotalCurrent());
                                movement.setEntrance(false);
                                movement.setBoxSesion(Babas.boxSession);
                                movement.setDescription("Alquiler cancelado NRO: "+rental.getId());
                                movement.save();
                                movement.getBoxSesion().getMovements().add(0,movement);
                                movement.getBoxSesion().calculateTotals();
                                FPrincipal.rentalsActives.remove(rental);
                                Utilities.getLblIzquierda().setText("Alquiler cancelado Nro. " + rental.getId() + " : " + Utilities.formatoFechaHora.format(rental.getUpdated()));
                                Utilities.getLblDerecha().setText("Monto caja: " + Utilities.moneda.format(Babas.boxSession.getAmountToDelivered()));
                                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER,"ÉXITO","Alquiler cancelada");
                                if(toSunat){
                                    if(Babas.company.isValidToken()){
                                        if(Rentals.getOnWait().isEmpty()&& Sales.getOnWait().isEmpty()){
                                            rental.setStatusSunat(ApiClient.cancelComprobante(ApiClient.getCancelComprobanteOfRental(rental)));
                                        }else{
                                            rental.setStatusSunat(false);
                                        }
                                    }
                                }
                                rental.save();
                            }else{
                                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","El alquiler ya está cancelado");
                            }
                        }
                    }else{
                        Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","Debe aperturar caja");
                    }
                    break;
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
