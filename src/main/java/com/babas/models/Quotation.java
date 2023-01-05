package com.babas.models;

import com.babas.controllers.Stocks;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.babas.utilities.UtilitiesReports;
import com.babas.views.frames.FPrincipal;
import com.moreno.Notify;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(name = "quotation_tbl")
public class Quotation extends Babas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(mappedBy = "quotation")
    @NotEmpty(message = "Productos")
    private List<DetailQuotation> detailQuotations=new ArrayList<>();
    @NotNull(message = "Válido hasta")
    private Date ended;
    private Date created;
    @ManyToOne
    @NotNull(message = "Sucursal")
    private Branch branch;
    @ManyToOne
    private Client client;
    private Double total=0.0;
    private Double discount=0.0;
    private Double totalCurrent=0.0;
    @ManyToOne
    @NotNull(message ="Usuario")
    private User user;
    private String observation;
    private Long numberQuotation;

    public void calculateTotals(){
        total=0.0;
        detailQuotations.forEach(detailSale -> {
            total+=detailSale.getSubtotal();
        });
        totalCurrent=total-discount;

    }

    public Long getId() {
        return id;
    }

    public List<DetailQuotation> getDetailQuotations() {
        return detailQuotations;
    }

    public void setDetailQuotations(List<DetailQuotation> detailQuotations) {
        this.detailQuotations = detailQuotations;
    }

    public Date getEnded() {
        return ended;
    }

    public void setEnded(Date ended) {
        this.ended = ended;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getTotalCurrent() {
        return totalCurrent;
    }

    public void setTotalCurrent(Double totalCurrent) {
        this.totalCurrent = totalCurrent;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public Long getNumberQuotation() {
        return numberQuotation;
    }

    public void loadCompleteQuotation(){
        if (Babas.boxSession.getId() != null) {
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
                        sale.setClient(getClient());
                        sale.setDiscount(getDiscount());
                        sale.setObservation(getObservation());
                        sale.setUser(Babas.user);
                        sale.setBoxSession(Babas.boxSession);
                        getDetailQuotations().forEach(detailQuotation -> {
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
                        rental.setClient(getClient());
                        rental.setDiscount(getDiscount());
                        rental.setObservation(getObservation());
                        rental.setUser(Babas.user);
                        rental.setBoxSession(Babas.boxSession);
                        getDetailQuotations().forEach(detailQuotation -> {
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
                        reserve.setClient(getClient());
                        reserve.setObservation(getObservation());
                        reserve.setUser(Babas.user);
                        reserve.setBoxSession(Babas.boxSession);
                        getDetailQuotations().forEach(detailQuotation -> {
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
        }else{
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER, "ERROR", "Debe abrir caja para comenzar");
        }
    }

    @Override
    public void save() {
        if(created==null){
            created=new Date();
        }
        super.save();
        numberQuotation=1000+id;
        super.save();
        getDetailQuotations().forEach(Babas::save);
    }

    public void showTicket() {
        int index=JOptionPane.showOptionDialog(Utilities.getJFrame(),"Seleccione el formato a ver","Ver ticket",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null,new Object[]{"A4", "Ticket","Cancelar"}, "A4");
        if(index==0){
            UtilitiesReports.generateTicketQuotation(true,this,false);
        }else if(index==1){
            UtilitiesReports.generateTicketQuotation(false,this,false);
        }
    }
}
