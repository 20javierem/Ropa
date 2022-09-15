package com.babas.models;

import com.babas.utilities.Babas;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Entity(name = "user_Tbl")
public class User extends Babas {

    @Id
    @GeneratedValue(generator = "increment")
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
    @NotBlank(message = "Contraseña")
    private String userPassword;
    private Date created=new Date();
    private Date updated;
    private Date lastLogin;
    private boolean active=true;
    private Date birthday;
    @ManyToMany
    private List<Branch> branchs=new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
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

    @Override
    public void save() {
        updated=new Date();
        super.save();
    }
}
