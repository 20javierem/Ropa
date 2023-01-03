package com.babas.utilitiesTables.buttonEditors;

import com.babas.App;
import com.babas.controllers.Rentals;
import com.babas.controllers.Sales;
import com.babas.models.DetailSale;
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
                button= new JButtonAction("change");
                break;
            case "show":
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
            Sale sale=((SaleAbstractModel) table.getModel()).getList().get(table.convertRowIndexToModel(table.getSelectedRow()));
            switch (type){
                case "change":
                    changeSale(sale);
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
                    sale.refresh();
                    if(sale.isActive()&&sale.isStatusSunat()){
                        if(Babas.boxSession.getId()!=null){
                            int response=JOptionPane.showOptionDialog(Utilities.getJFrame(),"¿Está seguro?, esta acción no se puede deshacer","Cancelar venta",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null,new Object[]{"Si","Forzar","Cancelar"},"Si");
                            if(response==0||response==1){
                                boolean cancel=true;
                                if(response==1){
                                    cancel=JOptionPane.showOptionDialog(Utilities.getJFrame(),"¿Está seguro?, forzar cancelación","Cancelar venta",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,new Object[]{"Si","Cancelar"},"Si")==0;
                                }
                                if(cancel){
                                    sale.refresh();
                                    if(sale.isActive()){
                                        Babas.company.refresh();
                                        if(response==0&&Babas.company.isValidToken()){
                                            cancel=ApiClient.cancelComprobante(ApiClient.getCancelComprobanteOfSale(sale));
                                            sale.setStatusSunat(cancel);
                                        }else{
                                            sale.setStatusSunat(false);
                                        }
                                        if(cancel){
                                            sale.setActive(false);
                                            sale.updateStocks();
                                            Movement movement=new Movement();
                                            movement.setAmount(-sale.getTotalCurrent());
                                            movement.setEntrance(false);
                                            movement.setBoxSesion(Babas.boxSession);
                                            movement.setDescription("Venta cancelada: "+sale.getSerie()+"-"+sale.getCorrelativo());
                                            movement.getBoxSesion().getMovements().add(0,movement);
                                            movement.getBoxSesion().calculateTotals();
                                            movement.save();
                                            Utilities.getLblIzquierda().setText("Venta cancelada: " + sale.getSerie()+"-"+sale.getCorrelativo() + " : " + Utilities.formatoFechaHora.format(sale.getUpdated()));
                                            Utilities.getLblDerecha().setText("Monto caja: " + Utilities.moneda.format(Babas.boxSession.getAmountToDelivered()));
                                            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER,"ÉXITO","Venta cancelada");
                                            sale.save();
                                        }
                                    }else{
                                        Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","La venta ya está cancelada");
                                    }
                                }
                            }
                        }else{
                            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","Debe aperturar caja");
                        }
                    }else{
                        Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","La venta no fué enviada o está cancelada");
                    }
                    break;
            }
            sale.refresh();
            Utilities.getTabbedPane().updateTab();
            Utilities.updateDialog();
        }
    }

    private void changeSale(Sale sale){
        String messageError=null;
        Babas.company.refresh();
        if(Babas.company.isValidToken()){
            sale.refresh();
            if(sale.isActive()){
                if(Babas.boxSession.getId()!=null){
                    if(sale.getTypeVoucher().equals("77")){
                        DChangeVoucher dChangeVoucher=new DChangeVoucher(sale.getClient());
                        int option = JOptionPane.showOptionDialog(Utilities.getJFrame(), dChangeVoucher.getContentPane(), "Cambio de comprobante", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, new Object[]{"Confirmar", "Cancelar"}, "Confirmar");
                        if (option == JOptionPane.OK_OPTION) {
                            sale.refresh();
                            if(sale.getTypeVoucher().equals("77")){
                                if(sale.isActive()){
                                    Sale sale1=new Sale();
                                    sale1.setClient(dChangeVoucher.getClient());
                                    sale1.setTypeVoucher(dChangeVoucher.getTypeVoucher());
                                    if (sale1.isValidClient()&&ApiClient.cancelComprobante(ApiClient.getCancelComprobanteOfSale(sale))) {
                                        sale.setActive(false);
                                        Movement movement=new Movement();
                                        movement.setAmount(-sale.getTotalCurrent());
                                        movement.setEntrance(false);
                                        movement.setBoxSesion(Babas.boxSession);
                                        movement.setDescription("Cambio de comprobante, Venta: "+sale.getSerie()+"-"+sale.getCorrelativo());
                                        Babas.boxSession.getMovements().add(0,movement);
                                        Babas.boxSession.calculateTotals();
                                        movement.save();
                                        sale.save();

                                        sale1.setUser(Babas.user);
                                        sale1.setObservation(sale.getObservation());
                                        sale1.setBoxSession(Babas.boxSession);
                                        sale1.setCash(sale.isCash());
                                        sale1.setDiscount(sale.getDiscount());
                                        sale1.setBranch(sale.getBranch());
                                        sale.getDetailSales().forEach(detailSale -> {
                                            DetailSale detailSale1=new DetailSale();
                                            detailSale1.setSale(sale1);
                                            detailSale1.setQuantity(detailSale.getQuantity());
                                            detailSale1.setProduct(detailSale.getProduct());
                                            detailSale1.setPrice(detailSale.getPrice());
                                            detailSale1.setNamePresentation(detailSale.getNamePresentation());
                                            detailSale1.setQuantityPresentation(detailSale.getQuantityPresentation());
                                            sale1.getDetailSales().add(detailSale1);
                                        });
                                        Babas.boxSession.getSales().add(0,sale1);
                                        Babas.boxSession.calculateTotals();
                                        sale1.calculateTotal();
                                        sale1.create();
                                        sale1.save();
                                        sale1.saveDetails();
                                        if(Rentals.getOnWait().isEmpty() && Sales.getOnWait().isEmpty()){
                                            sale1.setStatusSunat(ApiClient.sendComprobante(ApiClient.getComprobanteOfSale(sale1),false));
                                        }else{
                                            sale1.setStatusSunat(false);
                                        }
                                        sale1.save();
                                        Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER,"ÉXITO","Cambios guardados");
                                    }else{
                                        sale.setTypeVoucher("77");
                                        messageError="El cliente no es válido para el tipo de comprobante";
                                    }
                                }else{
                                    messageError="La venta fué cancelada por otro usuario";
                                }
                            }else{
                                messageError="El documento no puede cambiarse";
                            }
                        }
                    }else{
                        messageError="El documento no puede cambiarse";
                    }
                }else{
                    messageError="Debe aperturar caja";
                }

            }else{
                messageError="La venta está cancelada";
            }
        }
        if(messageError!=null){
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR",messageError);
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
