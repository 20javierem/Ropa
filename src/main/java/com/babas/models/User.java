package com.babas.models;

import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(name = "user_Tbl")
public class User extends Babas {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "identity")
    private Long id;
    @NotBlank(message = "Nombres")
    private String firstName;
    @NotBlank(message = "Apellidos")
    private String lastName;
    private String phone;
    @NotBlank(message = "Nombre de usuario")
    private String userName;
    @ManyToOne
    @NotNull(message = "Género")
    private Sex sex;
    @NotNull(message = "Contraseña")
    private String userPassword;
    private Date created;
    private Date updated;
    private Date lastLogin;
    private boolean active=true;
    private boolean staff=true;
    private Date birthday;
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
    @NotNull(message = "Permisos")
    private Permission permission=new Permission();

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public List<Branch> getBranchs() {
        return branchs;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
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

    public void setPermitions(Permission permission) {
        this.permission = permission;
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
