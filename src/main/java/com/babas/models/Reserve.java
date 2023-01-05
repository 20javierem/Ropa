package com.babas.models;

import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.babas.utilities.UtilitiesReports;
import com.babas.views.frames.FPrincipal;
import com.babas.views.tabs.TabNewRental;
import com.moreno.Notify;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.GenericGenerator;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(name = "reserve_tbl")
public class Reserve extends Babas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @NotNull(message = "Cliente")
    private Client client;
    @ManyToOne
    @NotNull(message = "Usuario")
    private User user;
    @ManyToOne
    @NotNull(message = "Caja")
    private BoxSession boxSession;
    @ManyToOne
    @NotNull
    private Branch branch;
    private Date created;
    private Date updated;
    @NotNull(message = "Fecha de entrega")
    private Date started;
    @OneToMany(mappedBy = "reserve")
    @NotEmpty(message = "Productos")
    private List<DetailReserve> detailReserves=new ArrayList<>();
    private Double total=0.0;
    private Double advance=0.0;
    private Double toCancel=0.0;
    private Long numberReserve;
    private Integer active=0;
    private boolean cash;
    private String observation;

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isCash() {
        return cash;
    }

    public void setCash(boolean cash) {
        this.cash = cash;
    }

    public Long getId() {
        return id;
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

    public List<DetailReserve> getDetailReserves() {
        return detailReserves;
    }

    public Double getTotal() {
        return total;
    }

    public BoxSession getBoxSession() {
        return boxSession;
    }

    public void setBoxSession(BoxSession boxSession) {
        this.boxSession = boxSession;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public Double getAdvance() {
        return advance;
    }

    public void setAdvance(Double advance) {
        this.advance = advance;
    }

    public Double getToCancel() {
        return toCancel;
    }
    public void calculateTotal(){
        total=0.0;
        detailReserves.forEach(detailSale -> {
            total+=detailSale.getSubtotal();
        });
        total=Math.round(total*100.0)/100.0;
        toCancel=Math.round((total-advance)*100.0)/100.0;
    }

    public Date getStarted() {
        return started;
    }

    public void setStarted(Date started) {
        this.started = started;
    }

    public Integer isActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public Long getNumberReserve() {
        return numberReserve;
    }

    public String getStringUpdated(){
        return Utilities.formatoFechaHora.format(updated);
    }
    public String getStringBranch(){
        return branch.getName();
    }
    public String getStringClient(){
        return client==null?"--":client.getNames();
    }
    public String getStringStade(){
        return active==0?"REALIZADA":active==1?"COMPLETADA":"CANCELADA";
    }
    public String getStringSubtotal(){
        return Utilities.moneda.format(total);
    }
    public String getStringType(){
        return cash?"EFECTIVO":"TRANSFERENCIA";
    }
    public String getStringAdvance(){return Utilities.moneda.format(advance);}
    public String getStringToCancel(){return Utilities.moneda.format(toCancel);}

    public void cancelReserve(){
        if(Babas.boxSession.getId()!=null){
            refresh();
            if(isActive()!=2){
                boolean si=JOptionPane.showConfirmDialog(Utilities.getJFrame(),"¿Está seguro?, esta acción no se puede deshacer","Cancelar reserva",JOptionPane.YES_NO_OPTION)==0;
                if(si){
                    refresh();
                    if(isActive()!=2){
                        if(isActive()==0){
                            setActive(2);
                            save();
                            Movement movement=new Movement();
                            movement.setAmount(-getAdvance());
                            movement.setEntrance(false);
                            movement.setBoxSesion(Babas.boxSession);
                            movement.setDescription("Reserva cancelada NRO: "+getNumberReserve());
                            movement.save();
                            movement.getBoxSesion().getMovements().add(0,movement);
                            movement.getBoxSesion().calculateTotals();
                            FPrincipal.reservesActives.remove(this);
                            Utilities.getLblIzquierda().setText("Reserva cancelada Nro. " + getNumberReserve() + " : " + Utilities.formatoFechaHora.format(getUpdated()));
                            Utilities.getLblDerecha().setText("Monto caja: " + Utilities.moneda.format(Babas.boxSession.getAmountToDelivered()));
                            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER,"ÉXITO","Reserva cancelada");
                        }else{
                            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","No puede cancelar una reserva completada");
                        }
                    }else{
                        Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","La reserva ya está cancelada");
                    }
                }
            }else{
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","La reserva ya está cancelada");
            }
        }else{
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","Debe aperturar caja");
        }
    }
    public void completeReserve(){
        if(Babas.boxSession.getId()!=null){
            if(isActive()==0){
                TabNewRental tabNewRental=new TabNewRental(new Rental(this));
                if(Utilities.getTabbedPane().indexOfTab(tabNewRental.getTabPane().getTitle())==-1){
                    Utilities.getTabbedPane().addTab(tabNewRental.getTabPane().getTitle(),tabNewRental.getTabPane());
                }
                Utilities.getTabbedPane().setSelectedIndex(Utilities.getTabbedPane().indexOfTab(tabNewRental.getTabPane().getTitle()));
            }else if(isActive()==1){
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","La reserva ya fue completada");
            }else{
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","La reserva está cancelada");
            }
        }else{
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","Debe aperturar caja");
        }
    }
    public void showTicket(){
        int index = JOptionPane.showOptionDialog(Utilities.getJFrame(), "Seleccione el formato a ver", "Ver ticket", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"A4", "Ticket", "Cancelar"}, "A4");
        if (index == 0) {
            UtilitiesReports.generateTicketReserve(true, this, false);
        } else if (index == 1) {
            UtilitiesReports.generateTicketReserve(false, this, false);
        }
    }
    @Override
    public void save() {
        if(created==null){
            created=new Date();
        }
        updated=new Date();
        super.save();
        numberReserve=1000+id;
        super.save();
        getDetailReserves().forEach(Babas::save);
    }
}
