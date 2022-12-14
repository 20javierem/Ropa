package com.babas.models;

import com.babas.controllers.Stocks;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.babas.views.tabs.TabNewTraslade;
import com.moreno.Notify;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.GenericGenerator;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity(name = "transfer_tbl")
public class Transfer extends Babas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "Origen")
    @ManyToOne
    private Branch source;
    @NotNull(message = "Destino")
    @ManyToOne
    private Branch destiny;
    @NotEmpty(message = "Productos")
    @OneToMany(mappedBy = "transfer")
    private List<DetailTransfer> detailTransfers=new ArrayList<>();
    @NotNull(message = "Tipo de transferencia")
    private Integer state=0;
    private Integer productsTransfers=0;
    private Long numberTransfer;
    private String description="";
    private Date created;
    private Date updated;
    @Transient
    private Boolean acept;

    public Boolean isAcept() {
        if(acept==null){
            acept=state==1;
        }
        return acept;
    }

    public Long getId() {
        return id;
    }

    public List<DetailTransfer> getDetailTransfers() {
        return detailTransfers;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getProductsTransfers() {
        return productsTransfers;
    }

    public void calculateTotalProuctsTransfers(){
        productsTransfers=0;
        getDetailTransfers().forEach(detailTransfer -> {
            productsTransfers+=detailTransfer.getQuantity();
        });
    }

    public Branch getSource() {
        return source;
    }

    public void setSource(Branch source) {
        this.source = source;
    }

    public Branch getDestiny() {
        return destiny;
    }

    public void setDestiny(Branch destiny) {
        this.destiny = destiny;
    }

    public String getDescription() {
        return description.isBlank()?"--":description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreated() {
        return created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public String getStringUpdated(){
        return updated==null? Utilities.formatoFechaHora.format(created):Utilities.formatoFechaHora.format(updated);
    }
    public String getStringCreated(){
        return Utilities.formatoFechaHora.format(created);
    }
    public String getStringSource(){
        return Objects.equals(getSource().getId(), getDestiny().getId()) ?"Ingreso":getSource().getName();
    }
    public String getStringStade(){
        return getState()==0?"EN ESPERA":getState()==1?"COMPLETADO":"CANCELADO";
    }
    public String getStringDestiny(){
        return destiny.getName();
    }

    public Long getNumberTransfer() {
        return numberTransfer;
    }

    public void showTicket(){
        TabNewTraslade tabNewTraslade=new TabNewTraslade(this);
        if(Utilities.getTabbedPane().indexOfTab(tabNewTraslade.getTabPane().getTitle())==-1){
            Utilities.getTabbedPane().addTab(tabNewTraslade.getTabPane().getTitle(),tabNewTraslade.getTabPane());
        }
        Utilities.getTabbedPane().setSelectedIndex(Utilities.getTabbedPane().indexOfTab(tabNewTraslade.getTabPane().getTitle()));
    }

    public void cancelTransfer(){
        if(getState()!=2){
            if(getSource().isActive()){
                boolean si= JOptionPane.showConfirmDialog(Utilities.getJFrame(),"??Est?? seguro?, esta acci??n no se puede deshacer","Cancelar transferencia",JOptionPane.YES_NO_OPTION)==0;
                if(si){
                    refresh();
                    if(getState()!=2){
                        setState(2);
                        setUpdated(new Date());
                        save();
                        Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER,"??XITO","Transferencia cancelada");
                    }else{
                        Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER,"??XITO","La transferencia ya fue cancelada por otro usuario");
                    }
                }
            }else{
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","No se encontr?? la sucursal de origen");
            }

        }else{
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","La transferencia ya est?? cancelada");
        }
    }
    @Override
    public void save() {
        if(created==null){
            created=new Date();
        }
        super.save();
        numberTransfer=1000+id;
        super.save();
        getDetailTransfers().forEach(Babas::save);
        getSource().getTransfers_sources().add(Transfer.this);
        getDestiny().getTransfers_destinys().add(Transfer.this);
        getDetailTransfers().forEach( detailTransfer -> detailTransfer.getProduct().getStocks().forEach(Babas::refresh));
        switch (getState()){
            case 0:
                acept=true;
                getDetailTransfers().forEach(detailTransfer -> {
                    Stock stock= Stocks.getStock(getSource(),detailTransfer.getProduct());
                    stock.setOnStock(stock.getOnStock()-detailTransfer.getQuantity());
                    stock.setQuantity(stock.getQuantity()-detailTransfer.getQuantity());
                    stock.save();
                });
                break;
            case 1:
                acept=true;
                getDetailTransfers().forEach(detailTransfer -> {
                    Stock stock= Stocks.getStock(getDestiny(),detailTransfer.getProduct());
                    if(stock==null){
                        stock=new Stock();
                        stock.setProduct(detailTransfer.getProduct());
                        stock.setBranch(getDestiny());
                        getDestiny().getStocks().add(stock);
                        detailTransfer.getProduct().getStocks().add(stock);
                    }
                    stock.setQuantity(stock.getQuantity()+detailTransfer.getQuantity());
                    stock.setOnStock(stock.getOnStock()+detailTransfer.getQuantity());
                    stock.save();
                });
                break;
            case 2:
                if(Objects.equals(getSource().getId(), getDestiny().getId())){
                    getDetailTransfers().forEach(detailTransfer -> {
                        Stock stock= Stocks.getStock(getSource(),detailTransfer.getProduct());
                        stock.refresh();
                        stock.setOnStock(stock.getOnStock()-detailTransfer.getQuantity());
                        stock.setQuantity(stock.getQuantity()-detailTransfer.getQuantity());
                        stock.save();
                    });
                }else{
                    getDetailTransfers().forEach(detailTransfer -> {
                        Stock stock= Stocks.getStock(getSource(),detailTransfer.getProduct());
                        stock.refresh();
                        stock.setOnStock(stock.getOnStock()+detailTransfer.getQuantity());
                        stock.setQuantity(stock.getQuantity()+detailTransfer.getQuantity());
                        stock.save();
                    });
                    if(isAcept()){
                        getDetailTransfers().forEach(detailTransfer -> {
                            Stock stock= Stocks.getStock(getDestiny(),detailTransfer.getProduct());
                            stock.refresh();
                            stock.setOnStock(stock.getOnStock()-detailTransfer.getQuantity());
                            stock.setQuantity(stock.getQuantity()-detailTransfer.getQuantity());
                            stock.save();
                        });
                    }
                }
                break;
        }
    }
}


