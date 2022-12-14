package com.babas.models;

import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.GenericGenerator;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(name = "user_Tbl")
public class User extends Babas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Nombres")
    private String firstName;
    @NotBlank(message = "Apellidos")
    private String lastName;
    private String phone;
    @NotBlank(message = "Nombre de usuario")
    private String userName;
    @NotNull(message = "Contraseña")
    private String userPassword;
    private Date created;
    private Date updated;
    private Date lastLogin;
    private boolean active=true;
    private boolean staff=true;
    @ManyToMany
    @JoinTable(
            name = "user_branch_tbl",
            joinColumns = {@JoinColumn(name = "fk_user")},
            inverseJoinColumns = {@JoinColumn(name = "fk_branch")}
    )
    private List<Branch> branchs=new ArrayList<>();
    @OneToMany
    private List<Sale> sales=new ArrayList<>();
    @ManyToOne
    private Permission permission=new Permission();
    @ManyToOne
    private Permission permissionGroup;
    private boolean groupDefault=false;
    private Integer idFact=0;

    public Integer getIdFact() {
        return idFact;
    }

    public void setIdFact(Integer idFact) {
        this.idFact = idFact;
    }


    public boolean isGroupDefault() {
        return groupDefault;
    }

    public void setGroupDefault(boolean groupDefault) {
        this.groupDefault = groupDefault;
    }

    public Permission getPermissionGroup() {
        return permissionGroup;
    }

    public void setPermissionGroup(Permission permissionGroup) {
        this.permissionGroup = permissionGroup;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = Utilities.nameOwn(firstName);
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = Utilities.nameOwn(lastName);
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return Utilities.desencriptar(userPassword);
    }

    public void setUserPassword(String userPassword) {
        if(userPassword==null){
            this.userPassword = userPassword;
        }else{
            this.userPassword = Utilities.encriptar(userPassword);
        }

    }

    public Date getCreated() {
        return created;
    }

    public Date getUpdated() {
        return updated;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<Branch> getBranchs() {
        return branchs;
    }

    public List<Sale> getSales() {
        return sales;
    }

    public boolean isStaff() {
        return staff;
    }

    public void setStaff(boolean staff) {
        this.staff = staff;
    }

    public Permission getPermitions() {
        return permission;
    }

    public static class ListCellRenderer extends DefaultListCellRenderer {
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            if (value instanceof User) {
                value = ((User) value).getFirstName()+", "+((User) value).getLastName();
            }else{
                value="--SELECCIONE--";
            }
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            return this;
        }
    }

    @Override
    public void save() {
        if(created==null){
            created=new Date();
        }
        if(permission!=null){
            permission.save();
        }
        updated=new Date();
        super.save();
    }
}
