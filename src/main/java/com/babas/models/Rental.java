package com.babas.models;

import com.babas.controllers.Rentals;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.babas.views.frames.FPrincipal;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.GenericGenerator;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Entity(name = "rental_tbl")
public class Rental extends Babas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @NotNull(message = "Caja")
    private BoxSession boxSession;
    @OneToMany(mappedBy = "rental")
    @NotEmpty(message = "Productos")
    private List<DetailRental> detailRentals=new ArrayList<>();
    @ManyToOne
    @NotNull(message = "Cliente")
    private Client client;
    private Date created;
    private Date updated;
    private Date delivery;
    @NotNull(message = "Fecha fin")
    private Date ended;
    @ManyToOne
    private Branch branch;
    private Integer active=0;
    private boolean cash;
    private Double total=0.0;
    private Double discount=0.0;
    private Double totalWithDiscount=0.0;
    private Double totalCurrent=0.0;
    private Double warranty=0.0;
    private Double penalty=0.0;
    @OneToOne
    private Reserve reserve;
    @ManyToOne
    @NotNull(message ="Usuario")
    private User user;
    private String observation;
    private String serie;
    private Long correlativo;
    private String typeVoucher;
    private Boolean statusSunat = false;

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }
    public Rental(){

    }

    public Rental(Reserve reserve){
        this.reserve=reserve;
        for (DetailReserve detailReserve : reserve.getDetailReserves()) {
            DetailRental detailRental=new DetailRental();
            detailRental.setRental(this);
            detailRental.setPrice(detailReserve.getPrice());
            detailRental.setQuantity(detailReserve.getQuantity());
            detailRental.setPresentation(detailReserve.getPresentation());
            detailRental.setProduct(detailReserve.getProduct());
            detailRentals.add(detailRental);
        }
        calculateTotals();
    }
    public Long getId() {
        return id;
    }

    public BoxSession getBoxSession() {
        return boxSession;
    }

    public void setBoxSession(BoxSession boxSession) {
        this.boxSession = boxSession;
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

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public Integer isActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
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
    public String getNumberRental(){
        String number=serie!=null? serie :"A001";
        number+="-";
        number+= correlativo!=null?correlativo:id;
        return number;
    }
    public Double getTotal() {
        return total;
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

    public List<DetailRental> getDetailRentals() {
        return detailRentals;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void calculateTotals(){
        total=0.0;
        detailRentals.forEach(detailSale -> {
            total+=detailSale.getSubtotal();
        });
        total=Math.round(total*100.0)/100.0;
        totalWithDiscount=Math.round((total-discount)*100.0)/100.0;
        totalCurrent=Math.round((totalWithDiscount+warranty)*100.0)/100.0;
        if(reserve!=null){
            totalCurrent=Math.round((totalCurrent-reserve.getAdvance())*100.0)/100.0;
        }
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
    public void setCorrelativo(Long correlativo) {
        this.correlativo = correlativo;
    }

    public String getTypeVoucher() {
        return typeVoucher;
    }

    public void setTypeVoucher(String typeVoucher) {
        this.typeVoucher = typeVoucher;
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

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public Double getTotalWithDiscount() {
        return totalWithDiscount;
    }

    public Double getWarranty() {
        return warranty;
    }

    public void setWarranty(Double warranty) {
        this.warranty = warranty;
    }

    public Date getEnded() {
        return ended;
    }

    public void setEnded(Date ended) {
        this.ended = ended;
    }

    public Double getPenalty() {
        return penalty;
    }

    public void setPenalty(Double penalty) {
        this.penalty = penalty;
    }

    public Date getDelivery() {
        return delivery;
    }

    public void setDelivery(Date delivery) {
        this.delivery = delivery;
    }

    public Reserve getReserve() {
        return reserve;
    }

    public void setReserve(Reserve reserve) {
        this.reserve = reserve;
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

    public String getStringUpdated(){
        return Utilities.formatoFechaHora.format(updated);
    }
    public String getStringBranch(){
        return branch.getName();
    }
    public String getStringStade(){
        return active==0?"EN ALQUILER":active==1?"COMPLETADO":"CANCELADO";
    }
    public String getStringSunat(){
        return !statusSunat&&active==2&&serie!=null?"FORZADO":statusSunat?"CONFIRMADO":"NO ENVIADO";
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
    public String getStringMulta(){
        return Utilities.moneda.format(getPenalty());
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
        branch.save();
        save();
        if(active==0&&reserve!=null){
            reserve.setActive(1);
            reserve.save();
            FPrincipal.reservesActives.remove(reserve);
        }
    }
    public boolean isPosibleCancel(){
        Calendar dateCreated=Calendar.getInstance();
        dateCreated.setTime(created);
        return (dateCreated.get(Calendar.DAY_OF_YEAR)-Calendar.getInstance().get(Calendar.DAY_OF_YEAR))<=7;
    }
    public void updateStocks(){
        getDetailRentals().forEach(DetailRental::updateStocks);
    }
    public void saveDetails(){
        getDetailRentals().forEach(Babas::save);
    }
    public void setNumbersVoucher(){
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
    }
    @Override
    public void save() {
        updated=new Date();
        super.save();
    }
}
