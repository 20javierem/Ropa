package com.babas.models;

import com.babas.controllers.Rentals;
import com.babas.controllers.Sales;
import com.babas.modelsFacture.ApiClient;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.babas.utilities.UtilitiesReports;
import com.babas.views.dialogs.DChangeVoucher;
import com.moreno.Notify;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.GenericGenerator;

import javax.swing.*;
import java.util.*;

@Entity(name = "sale_tbl")
public class Sale extends Babas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(mappedBy = "sale")
    @NotEmpty(message = "Productos")
    private List<DetailSale> detailSales = new ArrayList<>();
    @ManyToOne
    @NotNull(message = "Usuario")
    private User user;
    @ManyToOne
    private Client client;
    @ManyToOne
    @NotNull(message = "Caja")
    private BoxSession boxSession;
    private Double total = 0.0;
    private Double discount = 0.0;
    private Double totalCurrent = 0.0;
    private boolean cash;
    private Date created;
    private Date updated;
    @ManyToOne
    @NotNull
    private Branch branch;
    @OneToOne
    private Reserve reserve;
    private String observation;
    private String serie;
    private Long correlativo;
    private String typeVoucher;
    private Boolean statusSunat = true;
    private boolean active = true;

    public Sale() {

    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public Long getId() {
        return id;
    }

    public List<DetailSale> getDetailSales() {
        return detailSales;
    }

    public Double getTotal() {
        return total;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
        calculateTotal();
    }

    public Boolean isStatusSunat() {
        return statusSunat;
    }

    public void setStatusSunat(Boolean statusSunat) {
        this.statusSunat = statusSunat;
    }

    public String getSerie() {
        return serie;
    }

    public String getTypeVoucher() {
        return typeVoucher;
    }

    public String getTypeStringVoucher(){
        switch (getTypeVoucher()) {
            case "77":
                return "NOTA";
            case "03":
                return "BOLETA";
            default:
                return "FACTURA";
        }
    }
    public void setTypeVoucher(String typeVoucher) {
        this.typeVoucher = typeVoucher;
    }

    public Double getTotalCurrent() {
        return totalCurrent;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Date getCreated() {
        return created;
    }

    public Date getUpdated() {
        return updated;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public BoxSession getBoxSession() {
        return boxSession;
    }

    public void setBoxSession(BoxSession boxSession) {
        this.boxSession = boxSession;
    }

    public boolean isCash() {
        return cash;
    }

    public void setCash(boolean cash) {
        this.cash = cash;
    }

    public Long getCorrelativo() {
        return correlativo;
    }

    public Reserve getReserve() {
        return reserve;
    }

    public void setReserve(Reserve reserve) {
        this.reserve = reserve;
    }

    public void calculateTotal(){
        total=0.0;
        detailSales.forEach(detailSale -> {
            total+=detailSale.getSubtotal();
        });
        totalCurrent=total-discount;
        if(reserve!=null){
            total=totalCurrent-reserve.getAdvance();
        }
        total=Math.round(total*100.0)/100.0;
        totalCurrent=Math.round(totalCurrent*100.0)/100.0;
    }
    public boolean isValidClient(){
        if(client!=null){
            if ("01".equals(typeVoucher)) {
                return client.getDni().length() == 11;
            }
            return true;
        }else{
            return typeVoucher.equals("77")||typeVoucher.equals("03");
        }
    }
    public String getStringUpdated(){
        return Utilities.formatoFechaHora.format(updated);
    }
    public String getStringBranch(){
        return branch.getName();
    }
    public String getStringStade(){
        return active?"REALIZADO":"CANCELADO";
    }
    public String getStringSunat(){
        return !statusSunat&&!active?"FORZADO":statusSunat?"CONFIRMADO":"PENDIENTE";
    }
    public String getStringSubtotal(){
        return Utilities.moneda.format(total);
    }
    public String getStringDiscount(){
        return Utilities.moneda.format(discount);
    }
    public String getStringTotal(){
        return Utilities.moneda.format(totalCurrent);
    }
    public String getStringType(){
        return cash?"EFECTIVO":"TRANSFERENCIA";
    }
    public String getStringClient(){
        return client!=null?client.getNames():"";
    }
    public String getClientDni( ){
        return client!=null?client.getDni():"";
    }
    public int getClientType(){
        return client!=null?client.getTypeDocument():1;
    }
    public String getClientAdress(){
        return client!=null?client.getAddres():"";
    }
    public String getStringTypeDocument() {
        switch (getTypeVoucher()) {
            case "77":
                return "NOTA DE VENTA ELECTRÓNICA";
            case "03":
                return "BOLETA DE VENTA ELECTRÓNICA";
            default:
                return "FACTURA DE VENTA ELECTRÓNICA";
        }
    }
    public String getDetailTicket(){
        switch (getTypeVoucher()) {
            case "77":
                return "Representacion Impresa de la Nota de Venta Electrónica";
            case "03":
                return "Representacion Impresa de la Boleta de Venta Electrónica";
            default:
                return "Representacion Impresa de la Factura de Venta Electrónica";
        }
    }
    public String getContentQR(){
        return Babas.company.getRuc()+"|"+typeVoucher+"|"+getSerie()+"|"+getCorrelativo()+"|0.0|"+getTotalCurrent()+"|"+Utilities.formatoFecha.format(new Date())+"|"+getClientType()+"|"+getClientDni();
    }
    public void create(){
        created=new Date();
        branch.refresh();
        switch (typeVoucher){
            case "01":
                correlativo=branch.getCorrelativoFactura();
                serie=branch.getSerieFactura();
                branch.setCorrelativoFactura(branch.getCorrelativoFactura()+1);
                break;
            case "03":
                correlativo=branch.getCorrelativoBoleta();
                serie=branch.getSerieBoleta();
                branch.setCorrelativoBoleta(branch.getCorrelativoBoleta()+1);
                break;
            default:
                correlativo=branch.getCorrelativoNotaVenta();
                serie=branch.getSerieNotaVenta();
                branch.setCorrelativoNotaVenta(branch.getCorrelativoNotaVenta()+1);
                break;
        }
        branch.save();
        save();
    }

    public void updateStocks(){
        getDetailSales().forEach(DetailSale::updateStocks);
    }
    public void saveDetails(){
        getDetailSales().forEach(Babas::save);
    }
    public void showTicket(){int index=JOptionPane.showOptionDialog(Utilities.getJFrame(),"Seleccione el formato a ver","Ver ticket",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null,new Object[]{"A4", "Ticket","Cancelar"}, "A4");
        if(index==0){
            UtilitiesReports.generateComprobanteOfSale(true,this,false);
        }else if(index==1){
            UtilitiesReports.generateComprobanteOfSale(false,this,false);
        }
    }
    public void cancelSale(){
        refresh();
        if(isActive()&&isStatusSunat()){
            if(Babas.boxSession.getId()!=null){
                int response=JOptionPane.showOptionDialog(Utilities.getJFrame(),"¿Está seguro?, esta acción no se puede deshacer","Cancelar venta",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null,new Object[]{"Si","Forzar","Cancelar"},"Si");
                if(response==0||response==1){
                    boolean cancel=true;
                    if(response==1){
                        cancel=JOptionPane.showOptionDialog(Utilities.getJFrame(),"¿Está seguro?, forzar cancelación","Cancelar venta",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,new Object[]{"Si","Cancelar"},"Si")==0;
                    }
                    if(cancel){
                        refresh();
                        if(isActive()){
                            Babas.company.refresh();
                            if(response==0&&Babas.company.isValidToken()){
                                cancel=ApiClient.cancelComprobante(ApiClient.getCancelComprobanteOfSale(this));
                                setStatusSunat(cancel);
                            }else{
                                setStatusSunat(false);
                            }
                            if(cancel){
                                setActive(false);
                                updateStocks();
                                Movement movement=new Movement();
                                movement.setAmount(-getTotalCurrent());
                                movement.setEntrance(false);
                                movement.setBoxSesion(Babas.boxSession);
                                movement.setDescription("Venta cancelada: "+getSerie()+"-"+getCorrelativo());
                                movement.getBoxSesion().getMovements().add(0,movement);
                                movement.getBoxSesion().calculateTotals();
                                movement.save();
                                Utilities.getLblIzquierda().setText("Venta cancelada: " + getSerie()+"-"+getCorrelativo() + " : " + Utilities.formatoFechaHora.format(getUpdated()));
                                Utilities.getLblDerecha().setText("Monto caja: " + Utilities.moneda.format(Babas.boxSession.getAmountToDelivered()));
                                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER,"ÉXITO","Venta cancelada");
                                save();
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
    }
    public void changeSale(){
        String messageError=null;
        Babas.company.refresh();
        if(Babas.company.isValidToken()){
            refresh();
            if(isActive()){
                if(Babas.boxSession.getId()!=null){
                    if(getTypeVoucher().equals("77")){
                        DChangeVoucher dChangeVoucher=new DChangeVoucher(getClient());
                        int option = JOptionPane.showOptionDialog(Utilities.getJFrame(), dChangeVoucher.getContentPane(), "Cambio de comprobante", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, new Object[]{"Confirmar", "Cancelar"}, "Confirmar");
                        if (option == JOptionPane.OK_OPTION) {
                            refresh();
                            if(getTypeVoucher().equals("77")){
                                if(isActive()){
                                    Sale sale1=new Sale();
                                    sale1.setClient(dChangeVoucher.getClient());
                                    sale1.setTypeVoucher(dChangeVoucher.getTypeVoucher());
                                    if (sale1.isValidClient()&& ApiClient.cancelComprobante(ApiClient.getCancelComprobanteOfSale(this))) {
                                        setActive(false);
                                        Movement movement=new Movement();
                                        movement.setAmount(-getTotalCurrent());
                                        movement.setEntrance(false);
                                        movement.setBoxSesion(Babas.boxSession);
                                        movement.setDescription("Cambio de comprobante, Venta: "+getSerie()+"-"+getCorrelativo());
                                        Babas.boxSession.getMovements().add(0,movement);
                                        Babas.boxSession.calculateTotals();
                                        movement.save();
                                        save();

                                        sale1.setUser(Babas.user);
                                        sale1.setObservation(getObservation());
                                        sale1.setBoxSession(Babas.boxSession);
                                        sale1.setCash(isCash());
                                        sale1.setDiscount(getDiscount());
                                        sale1.setBranch(getBranch());
                                        getDetailSales().forEach(detailSale -> {
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
                                        setTypeVoucher("77");
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
    public void save() {
        updated=new Date();
        super.save();
    }

}
