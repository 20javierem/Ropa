package com.babas.models;

import com.babas.utilities.Babas;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "permission_tbl")
public class Permission extends Babas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nameGroup;
    private boolean isgroup=false;

    private boolean newSale=false;
    private boolean showCatalogue=false;
    private boolean recordSales=false;
    private boolean newRental=false;
    private boolean rentalsActives=false;
    private boolean recordRentals=false;
    private boolean newReserve=false;
    private boolean reservesActives=false;
    private boolean recordReserves=false;
    private boolean newTransfer=false;
    private boolean recordTransfers=false;
    private boolean recordBoxes=false;
    private boolean manageProducts=false;
    private boolean manageUsers=false;
    private boolean manageBranchs=false;
    private boolean manageCompany=false;
    private boolean aceptTransfer=false;
    private boolean newQuotation=false;
    private boolean recordQuotations=false;
    private boolean manageClients=false;

    public Permission(){

    }
    public Permission(boolean all) {
        newSale = true;
        showCatalogue = true;
        recordSales = true;
        newRental = true;
        rentalsActives = true;
        recordRentals = true;
        newReserve = true;
        reservesActives = true;
        recordReserves = true;
        newTransfer = true;
        recordTransfers = true;
        recordBoxes = true;
        manageProducts = true;
        manageUsers = true;
        manageBranchs = true;
        manageCompany = true;
        aceptTransfer = true;
        newQuotation=true;
        recordQuotations=true;
        manageClients=true;
    }

    @OneToMany(mappedBy = "permission")
    private List<User> users=new ArrayList<>();

    public Long getId() {
        return id;
    }

    public String getNameGroup() {
        return nameGroup;
    }

    public void setNameGroup(String nameGroup) {
        this.nameGroup = nameGroup;
    }

    public boolean isNewSale() {
        return newSale;
    }

    public void setNewSale(boolean newSale) {
        this.newSale = newSale;
    }

    public boolean isShowCatalogue() {
        return showCatalogue;
    }

    public void setShowCatalogue(boolean showCatalogue) {
        this.showCatalogue = showCatalogue;
    }

    public boolean isRecordSales() {
        return recordSales;
    }

    public void setRecordSales(boolean recordSales) {
        this.recordSales = recordSales;
    }

    public boolean isNewRental() {
        return newRental;
    }

    public void setNewRental(boolean newRental) {
        this.newRental = newRental;
    }

    public boolean isRentalsActives() {
        return rentalsActives;
    }

    public void setRentalsActives(boolean rentalsActives) {
        this.rentalsActives = rentalsActives;
    }

    public boolean isRecordRentals() {
        return recordRentals;
    }

    public void setRecordRentals(boolean recordRentals) {
        this.recordRentals = recordRentals;
    }

    public boolean isNewReserve() {
        return newReserve;
    }

    public void setNewReserve(boolean newReserve) {
        this.newReserve = newReserve;
    }

    public boolean isRecordReserves() {
        return recordReserves;
    }

    public void setRecordReserves(boolean recordReserves) {
        this.recordReserves = recordReserves;
    }

    public boolean isReservesActives() {
        return reservesActives;
    }

    public void setReservesActives(boolean reservesActives) {
        this.reservesActives = reservesActives;
    }

    public boolean isNewTransfer() {
        return newTransfer;
    }

    public void setNewTransfer(boolean newTransfer) {
        this.newTransfer = newTransfer;
    }

    public boolean isRecordTransfers() {
        return recordTransfers;
    }

    public void setRecordTransfers(boolean recordTransfers) {
        this.recordTransfers = recordTransfers;
    }

    public boolean isRecordBoxes() {
        return recordBoxes;
    }

    public void setRecordBoxes(boolean recordBoxes) {
        this.recordBoxes = recordBoxes;
    }

    public boolean isManageProducts() {
        return manageProducts;
    }

    public void setManageProducts(boolean manageProducts) {
        this.manageProducts = manageProducts;
    }

    public boolean isManageUsers() {
        return manageUsers;
    }

    public void setManageUsers(boolean manageUsers) {
        this.manageUsers = manageUsers;
    }

    public boolean isManageBranchs() {
        return manageBranchs;
    }

    public void setManageBranchs(boolean manageBranchs) {
        this.manageBranchs = manageBranchs;
    }

    public boolean isManageCompany() {
        return manageCompany;
    }

    public void setManageCompany(boolean manageCompany) {
        this.manageCompany = manageCompany;
    }

    public List<User> getUsers() {
        return users;
    }

    public boolean isGroup() {
        return isgroup;
    }

    public void setGroup(boolean isgroup) {
        this.isgroup = isgroup;
    }


    public boolean isAceptTransfer() {
        return aceptTransfer;
    }

    public void setAceptTransfer(boolean aceptTransfer) {
        this.aceptTransfer = aceptTransfer;
    }

    public boolean isNewQuotation() {
        return newQuotation;
    }

    public void setNewQuotation(boolean newQuotation) {
        this.newQuotation = newQuotation;
    }

    public boolean isRecordQuotations() {
        return recordQuotations;
    }

    public void setRecordQuotations(boolean recordQuotations) {
        this.recordQuotations = recordQuotations;
    }

    public boolean isManageClients() {
        return manageClients;
    }

    public void setManageClients(boolean manageClients) {
        this.manageClients = manageClients;
    }

    public static class ListCellRenderer extends DefaultListCellRenderer {
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            if (value instanceof Permission) {
                value = ((Permission) value).getNameGroup();
            }else{
                value="--SELECCIONE--";
            }
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            return this;
        }
    }
}
