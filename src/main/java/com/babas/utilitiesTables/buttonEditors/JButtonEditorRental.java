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
                    changeRental(rental);
                    break;
                case "end":
                    rental.refresh();
                    if(rental.isActive()==0){
                        if(Babas.boxSession.getId()!=null){
                            TabFinishRental tabFinishRental=new TabFinishRental(rental);
                            if(Utilities.getTabbedPane().indexOfTab(tabFinishRental.getTabPane().getTitle())==-1){
                                Utilities.getTabbedPane().addTab(tabFinishRental.getTabPane().getTitle(),tabFinishRental.getTabPane());
                            }
                            Utilities.getTabbedPane().setSelectedIndex(Utilities.getTabbedPane().indexOfTab(tabFinishRental.getTabPane().getTitle()));
                        }else{
                            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","Debe aperturar caja");
                        }
                    }else if(rental.isActive()==1){
                        Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","El alquiler ya fue completado");
                    }else{
                        Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","El alquiler está cancelado");
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
                    rental.refresh();
                    if(rental.isActive()!=2){
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
                                            rental.setStatusSunat(ApiClient.cancelComprobante(ApiClient.getCancelComprobanteOfRental(rental)));
                                            if(rental.isStatusSunat()==null){
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
                    }else{
                        Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","El alquiler ya está cancelado");
                    }
                    break;
            }
            Utilities.getTabbedPane().updateTab();
            Utilities.updateDialog();
        }
    }
    private void changeRental(Rental rental){
        String messageError=null;
        if(Babas.company.isValidToken()){
            rental.refresh();
            if(rental.isActive()==1){
                if(Babas.boxSession.getId()!=null){
                    if(rental.getTypeVoucher().equals("77")){
                        DChangeVoucher dChangeVoucher=new DChangeVoucher(rental.getClient());
                        int option = JOptionPane.showOptionDialog(Utilities.getJFrame(), dChangeVoucher.getContentPane(), "Cambio de comprobante", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, new Object[]{"Confirmar", "Cancelar"}, "Confirmar");
                        if (option == JOptionPane.OK_OPTION) {
                            rental.refresh();
                            if(rental.getTypeVoucher().equals("77")){
                                if(rental.isActive()==1){
                                    Rental rental1=new Rental();
                                    rental1.setClient(dChangeVoucher.getClient());
                                    rental1.setTypeVoucher(dChangeVoucher.getTypeVoucher());
                                    if (rental1.isValidClient()) {
                                        rental.setActive(2);
                                        Movement movement=new Movement();
                                        movement.setAmount(-rental.getTotalCurrent());
                                        movement.setEntrance(false);
                                        movement.setBoxSesion(Babas.boxSession);
                                        movement.setDescription("Cambio de comprobante, Alquiler: "+rental.getSerie()+"-"+rental.getCorrelativo());
                                        movement.getBoxSesion().getMovements().add(0,movement);
                                        movement.getBoxSesion().calculateTotals();
                                        movement.save();
                                        if(Babas.company.isValidToken()){
                                            rental.setStatusSunat(ApiClient.cancelComprobante(ApiClient.getCancelComprobanteOfRental(rental)));
                                        }
                                        rental.save();

                                        rental1.setUser(Babas.user);
                                        rental1.setObservation(rental.getObservation());
                                        rental1.setBoxSession(Babas.boxSession);
                                        rental1.setCash(rental.isCash());
                                        rental1.setDiscount(rental.getDiscount());
                                        rental1.setWarranty(rental.getWarranty());
                                        rental1.setPenalty(rental.getPenalty());
                                        rental1.setBranch(rental.getBranch());
                                        rental.getDetailRentals().forEach(detailRental -> {
                                            DetailRental detailRental1=new DetailRental();
                                            detailRental1.setRental(rental1);
                                            detailRental1.setQuantity(detailRental.getQuantity());
                                            detailRental1.setProduct(detailRental.getProduct());
                                            detailRental1.setPrice(detailRental.getPrice());
                                            detailRental1.setNamePresentation(detailRental.getNamePresentation());
                                            detailRental1.setQuantityPresentation(detailRental.getQuantityPresentation());
                                            rental1.getDetailRentals().add(detailRental1);
                                        });
                                        rental1.calculateTotals();
                                        rental1.create();
                                        rental1.save();
                                        if(Babas.company.isValidToken()){
                                            if(Rentals.getOnWait().isEmpty() && Sales.getOnWait().isEmpty()){
                                                rental1.setStatusSunat(ApiClient.sendComprobante(ApiClient.getComprobanteOfRental(rental1),false));
                                            }else{
                                                rental1.setStatusSunat(false);
                                            }
                                        }
                                        rental1.save();
                                        Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER,"ÉXITO","Cambios guardados");
                                    }else{
                                        rental.setTypeVoucher("77");
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
