package com.babas.models;

import com.babas.controllers.Rentals;
import com.babas.controllers.Sales;
import com.babas.modelsFacture.ApiClient;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.babas.utilities.UtilitiesReports;
import com.babas.views.dialogs.DChangeVoucher;
import com.babas.views.frames.FPrincipal;
import com.babas.views.tabs.TabFinishRental;
import com.moreno.Notify;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.GenericGenerator;

import javax.swing.*;
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
        return !statusSunat&&active==2&&serie!=null?"FORZADO":statusSunat?"CONFIRMADO":active==2?"NO ENVIADO":"PENDIENTE";
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
    public void endRental(){
        refresh();
        if(isActive()==0){
            if(Babas.boxSession.getId()!=null){
                TabFinishRental tabFinishRental=new TabFinishRental(this);
                if(Utilities.getTabbedPane().indexOfTab(tabFinishRental.getTabPane().getTitle())==-1){
                    Utilities.getTabbedPane().addTab(tabFinishRental.getTabPane().getTitle(),tabFinishRental.getTabPane());
                }
                Utilities.getTabbedPane().setSelectedIndex(Utilities.getTabbedPane().indexOfTab(tabFinishRental.getTabPane().getTitle()));
            }else{
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","Debe aperturar caja");
            }
        }else if(isActive()==1){
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","El alquiler ya fue completado");
        }else{
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","El alquiler está cancelado");
        }
    }
    public void showTicket(){
        int index=JOptionPane.showOptionDialog(Utilities.getJFrame(),"Seleccione el formato a ver","Ver ticket",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null,new Object[]{"A4", "Ticket","Cancelar"}, "A4");
        if(getCorrelativo()!=null){
            if(index==0){
                UtilitiesReports.generateComprobanteOfRental(true,this,false);
            }else if(index==1){
                UtilitiesReports.generateComprobanteOfRental(false,this,false);
            }
        }else{
            if(index==0){
                UtilitiesReports.generateTicketRental(true,this,false);
            }else if(index==1){
                UtilitiesReports.generateTicketRental(false,this,false);
            }
        }
    }
    public void cancelRental(){
        String messageError=null;
        refresh();
        if(isActive()!=2){
            if(Babas.boxSession.getId()!=null){
                boolean toSunat=isActive()==1;
                boolean cancel;
                if(toSunat){
                    if(isStatusSunat()){
                        int response=JOptionPane.showOptionDialog(Utilities.getJFrame(),"¿Está seguro?, esta acción no se puede deshacer","Cancelar alquiler",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null,new Object[]{"Si","Forzar","Cancelar"},"Si");
                        if(response==1){
                            cancel=JOptionPane.showOptionDialog(Utilities.getJFrame(),"¿Está seguro?, forzar cancelación","Cancelar alquiler",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,new Object[]{"Si","No"},"Si")==0;
                            toSunat=false;
                        }else{
                            cancel=response==0;
                        }
                    }else{
                        messageError="El alquiler no fué enviado a sunat";
                        cancel=false;
                    }
                }else{
                    cancel=JOptionPane.showOptionDialog(Utilities.getJFrame(),"¿Está seguro?, esta acción no se puede deshacer","Cancelar alquiler",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,new Object[]{"Si","No"},"Si")==0;
                }
                if(cancel){
                    refresh();
                    if(isActive()!=2){
                        Babas.company.refresh();
                        if(toSunat&&Babas.company.isValidToken()){
                            cancel=ApiClient.cancelComprobante(ApiClient.getCancelComprobanteOfRental(this));
                            setStatusSunat(cancel);
                        }else{
                            setStatusSunat(false);
                        }
                        if(cancel){
                            setActive(2);
                            updateStocks();
                            Movement movement=new Movement();
                            movement.setAmount(-getTotalCurrent());
                            movement.setEntrance(false);
                            movement.setBoxSesion(Babas.boxSession);
                            movement.setDescription("Alquiler cancelado: "+getNumberRental());
                            movement.save();
                            movement.getBoxSesion().getMovements().add(0,movement);
                            movement.getBoxSesion().calculateTotals();
                            FPrincipal.rentalsActives.remove(this);
                            Utilities.getLblIzquierda().setText("Alquiler cancelado: " + getSerie()+"-"+getCorrelativo() + " : " + Utilities.formatoFechaHora.format(getUpdated()));
                            Utilities.getLblDerecha().setText("Monto caja: " + Utilities.moneda.format(Babas.boxSession.getAmountToDelivered()));
                            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER,"ÉXITO","Alquiler cancelada");
                            save();
                        }
                    }else{
                        messageError="El alquiler está cancelado";
                    }
                }
            }else{
                messageError="Debe aperturar caja";
            }
        }else{
            messageError="El alquiler está cancelado";
        }
        if(messageError!=null){
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR",messageError);
        }
    }
    public void changeRental(){
        String messageError=null;
        Babas.company.refresh();
        if(Babas.company.isValidToken()){
            refresh();
            if(isStatusSunat()){
                if(isActive()==1){
                    if(Babas.boxSession.getId()!=null){
                        if(getTypeVoucher().equals("77")){
                            DChangeVoucher dChangeVoucher=new DChangeVoucher(getClient());
                            int option = JOptionPane.showOptionDialog(Utilities.getJFrame(), dChangeVoucher.getContentPane(), "Cambio de comprobante", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, new Object[]{"Confirmar", "Cancelar"}, "Confirmar");
                            if (option == JOptionPane.OK_OPTION) {
                                refresh();
                                if(getTypeVoucher().equals("77")){
                                    if(isActive()==1){
                                        Rental rental1=new Rental();
                                        rental1.setClient(dChangeVoucher.getClient());
                                        rental1.setTypeVoucher(dChangeVoucher.getTypeVoucher());
                                        if (rental1.isValidClient()&& ApiClient.cancelComprobante(ApiClient.getCancelComprobanteOfRental(this))) {
                                            setActive(2);
                                            Movement movement=new Movement();
                                            movement.setAmount(-getTotalCurrent());
                                            movement.setEntrance(false);
                                            movement.setBoxSesion(Babas.boxSession);
                                            movement.setDescription("Cambio de comprobante, Alquiler: "+getNumberRental());
                                            Babas.boxSession.getMovements().add(0,movement);
                                            Babas.boxSession.calculateTotals();
                                            movement.save();
                                            save();

                                            rental1.setUser(Babas.user);
                                            rental1.setObservation(getObservation());
                                            rental1.setBoxSession(Babas.boxSession);
                                            rental1.setCash(isCash());
                                            rental1.setDiscount(getDiscount());
                                            rental1.setWarranty(getWarranty());
                                            rental1.setPenalty(getPenalty());
                                            rental1.setBranch(getBranch());
                                            getDetailRentals().forEach(detailRental -> {
                                                DetailRental detailRental1=new DetailRental();
                                                detailRental1.setRental(rental1);
                                                detailRental1.setQuantity(detailRental.getQuantity());
                                                detailRental1.setProduct(detailRental.getProduct());
                                                detailRental1.setPrice(detailRental.getPrice());
                                                detailRental1.setNamePresentation(detailRental.getNamePresentation());
                                                detailRental1.setQuantityPresentation(detailRental.getQuantityPresentation());
                                                rental1.getDetailRentals().add(detailRental1);
                                            });
                                            Babas.boxSession.getRentals().add(rental1);
                                            Babas.boxSession.calculateTotals();
                                            rental1.calculateTotals();
                                            rental1.create();
                                            rental1.save();
                                            rental1.saveDetails();
                                            if(Rentals.getOnWait().isEmpty() && Sales.getOnWait().isEmpty()){
                                                rental1.setStatusSunat(ApiClient.sendComprobante(ApiClient.getComprobanteOfRental(rental1),false));
                                            }else{
                                                rental1.setStatusSunat(false);
                                            }
                                            rental1.save();
                                            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER,"ÉXITO","Cambios guardados");
                                        }else{
                                            refresh();
                                            messageError="El cliente no es válido para el tipo de comprobante";
                                        }
                                    }else{
                                        messageError="El alquiler fué cancelada por otro usuario";
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
                    messageError="El alquiler está cancelado";
                }
            }else{
                messageError="El alquiler no fue enviado a sunat";
            }
        }else{
            messageError="El token no es válido";
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
