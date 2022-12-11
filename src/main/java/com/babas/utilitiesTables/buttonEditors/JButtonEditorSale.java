package com.babas.utilitiesTables.buttonEditors;

import com.babas.App;
import com.babas.controllers.Rentals;
import com.babas.controllers.Sales;
import com.babas.models.Movement;
import com.babas.models.Sale;
import com.babas.modelsFacture.ApiClient;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.babas.utilities.UtilitiesReports;
import com.babas.utilitiesTables.tablesModels.SaleAbstractModel;
import com.babas.views.dialogs.DChangeVoucher;
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
            case "change":
                button= new JButtonAction(new FlatSVGIcon(App.class.getResource("icons/svg/changeVoucher.svg")));
                break;
            case "show":
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
            Sale sale=((SaleAbstractModel) table.getModel()).getList().get(table.convertRowIndexToModel(table.getSelectedRow()));
            switch (type){
                case "change":
                    if(Babas.company.isValidToken()){
                        sale.refresh();
                        if(sale.isActive()){
                            if(sale.getTypeVoucher().equals("77")){
                                if (Sales.getOnWait().isEmpty() && Rentals.getOnWait().isEmpty()) {
                                    DChangeVoucher dChangeVoucher=new DChangeVoucher(sale.getClient());
                                    int option = JOptionPane.showOptionDialog(Utilities.getJFrame(), dChangeVoucher.getContentPane(), "Cambio de comprobante", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, new Object[]{"Confirmar", "Cancelar"}, "Confirmar");
                                    if (option == JOptionPane.OK_OPTION) {
                                        sale.refresh();
                                        if(sale.getTypeVoucher().equals("77")){
                                            if(sale.isActive()){
                                                sale.setClient(dChangeVoucher.getClient());
                                                sale.setTypeVoucher(dChangeVoucher.getTypeVoucher());
                                                if (sale.isValidClient(sale.getTypeVoucher())) {
                                                    sale.setTypeVoucher("77");
                                                    sale.setStatusSunat(ApiClient.cancelComprobante(ApiClient.getCancelComprobanteOfSale(sale)));
                                                    if(sale.isStatusSunat()){
                                                        sale.setTypeVoucher(dChangeVoucher.getTypeVoucher());
                                                        sale.create();
                                                        sale.save();
                                                        sale.setStatusSunat(ApiClient.sendComprobante(ApiClient.getComprobanteOfSale(sale),true));
                                                        sale.save();
                                                    }
                                                }else{
                                                    sale.setTypeVoucher("77");
                                                    Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER, "ERROR", "El cliente no es válido para el tipo de comprobante");
                                                }
                                            }else{
                                                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","La venta está cancelada");
                                            }
                                        }else{
                                            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","El documento no puede cambiarse");
                                        }
                                    }
                                } else {
                                    Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","Primero de enviar todos los comprobantes pendientes");
                                }
                            }else{
                                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","El documento no puede cambiarse");
                            }
                        }else{
                            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","La venta está cancelada");
                        }
                    }
                    break;
                case "show":
                    int index=JOptionPane.showOptionDialog(Utilities.getJFrame(),"Seleccione el formato a ver","Ver ticket",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null,new Object[]{"A4", "Ticket","Cancelar"}, "A4");
                    if(index==0){
                        UtilitiesReports.generateComprobanteOfSale(true,sale,false);
                    }else if(index==1){
                        UtilitiesReports.generateComprobanteOfSale(false,sale,false);
                    }
                    break;
                default:
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
                                if(Babas.company.isValidToken()){
                                    if(Rentals.getOnWait().isEmpty() && Sales.getOnWait().isEmpty()){
                                        sale.setStatusSunat(ApiClient.cancelComprobante(ApiClient.getCancelComprobanteOfSale(sale)));
                                    }else{
                                        sale.setStatusSunat(false);
                                    }
                                }
                                sale.save();
                            }else{
                                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","La venta ya está cancelada");
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

    private void changeSale(Sale sale,String type){
        sale.setActive(false);
        Movement movement=new Movement();
        movement.setAmount(-sale.getTotalCurrent());
        movement.setEntrance(false);
        movement.setBoxSesion(Babas.boxSession);
        movement.setDescription("Venta cancelada NRO: "+sale.getId());
        movement.getBoxSesion().getMovements().add(0,movement);
        movement.getBoxSesion().calculateTotals();
        movement.save();
        if(Babas.company.isValidToken()){
            if(Rentals.getOnWait().isEmpty() && Sales.getOnWait().isEmpty()){
                sale.setStatusSunat(ApiClient.cancelComprobante(ApiClient.getCancelComprobanteOfSale(sale)));
            }else{
                sale.setStatusSunat(false);
            }
        }
        sale.save();

        Sale sale1=new Sale();
        sale1.setTypeVoucher(type);
        sale1.setClient(sale.getClient());
        sale1.setUser(Babas.user);
        sale1.setObservation(sale.getObservation());
        sale1.setBoxSession(Babas.boxSession);
        sale1.setCash(sale.isCash());
        sale1.setDiscount(sale.getDiscount());
        sale1.setBranch(sale.getBranch());
        sale1.getDetailSales().addAll(sale.getDetailSales());
        sale1.calculateTotal();
        sale1.create();
        sale1.save();
        if(Babas.company.isValidToken()){
            if(Rentals.getOnWait().isEmpty() && Sales.getOnWait().isEmpty()){
                sale1.setStatusSunat(ApiClient.cancelComprobante(ApiClient.getCancelComprobanteOfSale(sale1)));
            }else{
                sale1.setStatusSunat(false);
            }
        }
        sale1.save();
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
