package com.babas.utilitiesTables.buttonEditors;

import com.babas.App;
import com.babas.controllers.Stocks;
import com.babas.models.*;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.babas.utilities.UtilitiesReports;
import com.babas.utilitiesTables.tablesModels.QuotationAbstractModel;
import com.babas.utilitiesTables.tablesModels.SaleAbstractModel;
import com.babas.views.frames.FPrincipal;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.moreno.Notify;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JButtonEditorQuotation extends AbstractCellEditor implements TableCellEditor, ActionListener {
    private JButtonAction button;
    private boolean complete;

    public JButtonEditorQuotation(boolean complete) {
        this.complete=complete;
        if(complete){
            button= new JButtonAction("check");
        }else{
            button=new JButtonAction("show");
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
            Quotation quotation=((QuotationAbstractModel) table.getModel()).getList().get(table.convertRowIndexToModel(table.getSelectedRow()));
            if(complete){
                if (Babas.boxSession.getId() != null) {
                    loadCompleteQuotation(quotation);
                }else{
                    Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER, "ERROR", "Debe abrir caja para comenzar");
                }
            }else{
                int index=JOptionPane.showOptionDialog(Utilities.getJFrame(),"Seleccione el formato a ver","Ver ticket",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null,new Object[]{"A4", "Ticket","Cancelar"}, "A4");
                if(index==0){
                    UtilitiesReports.generateTicketQuotation(true,quotation,false);
                }else if(index==1){
                    UtilitiesReports.generateTicketQuotation(false,quotation,false);
                }
            }
            Utilities.getTabbedPane().updateTab();
            Utilities.updateDialog();
        }
    }
    private void loadCompleteQuotation(Quotation quotation){
        JComboBox comboBox = new JComboBox();
        comboBox.addItem("VENTA");
        comboBox.addItem("ALQUILER");
        comboBox.addItem("RESERVA");
        int option = JOptionPane.showOptionDialog(Utilities.getJFrame(), comboBox, "Completar", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Aceptar", "Cancelar"}, "Aceptar");
        if (option == JOptionPane.OK_OPTION) {
            switch (comboBox.getSelectedItem().toString()){
                case "VENTA":
                    Sale sale=new Sale();
                    sale.setBranch(Babas.boxSession.getBox().getBranch());
                    sale.setClient(quotation.getClient());
                    sale.setDiscount(quotation.getDiscount());
                    sale.setObservation(quotation.getObservation());
                    sale.setUser(Babas.user);
                    sale.setBoxSession(Babas.boxSession);
                    quotation.getDetailQuotations().forEach(detailQuotation -> {
                        Stock stock= Stocks.getStock(sale.getBranch(),detailQuotation.getProduct());
                        if(stock!=null){
                            DetailSale detailSale=new DetailSale();
                            detailSale.setSale(sale);
                            detailSale.setPrice(detailQuotation.getPrice());
                            detailSale.setQuantityPresentation(detailQuotation.getQuantityPresentation());
                            detailSale.setQuantity(detailQuotation.getQuantity());
                            detailSale.setProduct(detailQuotation.getProduct());
                            detailSale.setNamePresentation(detailQuotation.getNamePresentation());
                            sale.getDetailSales().add(detailSale);
                        }else{
                            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"MENSAJE","No se encontró el producto: "+detailQuotation.getProduct().getStyle().getName()+" en la sucursal.");
                        }
                    });
                    sale.calculateTotal();
                    FPrincipal fPrincipal= (FPrincipal) Utilities.getJFrame();
                    fPrincipal.getMenuSales().loadNewSale(true,sale);
                    break;
                case "ALQUILER":
                    Rental rental=new Rental();
                    rental.setBranch(Babas.boxSession.getBox().getBranch());
                    rental.setClient(quotation.getClient());
                    rental.setDiscount(quotation.getDiscount());
                    rental.setObservation(quotation.getObservation());
                    rental.setUser(Babas.user);
                    rental.setBoxSession(Babas.boxSession);
                    quotation.getDetailQuotations().forEach(detailQuotation -> {
                        Stock stock= Stocks.getStock(rental.getBranch(),detailQuotation.getProduct());
                        if(stock!=null){
                            DetailRental detailRental=new DetailRental();
                            detailRental.setRental(rental);
                            detailRental.setPrice(detailQuotation.getPrice());
                            detailRental.setQuantityPresentation(detailQuotation.getQuantityPresentation());
                            detailRental.setQuantity(detailQuotation.getQuantity());
                            detailRental.setProduct(detailQuotation.getProduct());
                            detailRental.setNamePresentation(detailQuotation.getNamePresentation());
                            rental.getDetailRentals().add(detailRental);
                        }else{
                            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"MENSAJE","No se encontró el producto: "+detailQuotation.getProduct().getStyle().getName()+" en la sucursal.");
                        }
                    });
                    rental.calculateTotals();
                    fPrincipal = (FPrincipal) Utilities.getJFrame();
                    fPrincipal.getMenuRentals().loadNewRental(rental);
                    break;
                case "RESERVA":
                    Reserve reserve=new Reserve();
                    reserve.setBranch(Babas.boxSession.getBox().getBranch());
                    reserve.setClient(quotation.getClient());
                    reserve.setObservation(quotation.getObservation());
                    reserve.setUser(Babas.user);
                    reserve.setBoxSession(Babas.boxSession);
                    quotation.getDetailQuotations().forEach(detailQuotation -> {
                        Stock stock= Stocks.getStock(reserve.getBranch(),detailQuotation.getProduct());
                        if(stock!=null){
                            DetailReserve detailReserve=new DetailReserve();
                            detailReserve.setReserve(reserve);
                            detailReserve.setPrice(detailQuotation.getPrice());
                            detailReserve.setQuantityPresentation(detailQuotation.getQuantityPresentation());
                            detailReserve.setQuantity(detailQuotation.getQuantity());
                            detailReserve.setProduct(detailQuotation.getProduct());
                            detailReserve.setNamePresentation(detailQuotation.getNamePresentation());
                            reserve.getDetailReserves().add(detailReserve);
                        }else{
                            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"MENSAJE","No se encontró el producto: "+detailQuotation.getProduct().getStyle().getName()+" en la sucursal.");
                        }
                    });
                    reserve.calculateTotal();
                    fPrincipal = (FPrincipal) Utilities.getJFrame();
                    fPrincipal.getMenuReserves().loadNewReserve(reserve);
                    break;
            }
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
