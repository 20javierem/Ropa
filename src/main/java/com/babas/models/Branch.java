package com.babas.models;

import com.babas.utilities.Babas;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(name = "branch_tbl")
public class Branch extends Babas {
    @Id
    private Long id;
    @NotBlank(message = "Nombre")
    private String name;
    private String email;
    @NotBlank(message = "Dirección")
    private String direction;
    @NotBlank(message = "Celular")
    private String phone;
    @NotNull
    private boolean active=true;
    @ManyToMany
    private List<User> users =new ArrayList<>();
    @OneToMany(mappedBy = "source")
    private List<Transfer> transfers_sources =new ArrayList<>();
    @OneToMany(mappedBy = "destiny")
    private List<Transfer> transfers_destinys =new ArrayList<>();
    private Date created=new Date();
    private Date updated;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Date getCreated() {
        return created;
    }

    public Date getUpdated() {
        return updated;
    }

    public List<User> getUsers() {
        return users;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public void save() {
        updated=new Date();
        super.save();
    }
}
