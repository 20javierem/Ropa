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

@Entity(name = "client_tbl")
public class Client extends Babas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date created;
    private Date updated;
    @NotBlank(message = "Dni")
    private String dni;
    @NotBlank(message = "Cliente")
    private String names;
    private String addres;
    private String phone;
    private Integer typeDocument=0;

    public Date getCreated() {
        return created;
    }

    public Date getUpdated() {
        return updated;
    }

    public Long getId() {
        return id;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = Utilities.nameOwn(names.toLowerCase());
    }

    public String getAddres() {
        return addres;
    }

    public void setAddres(String addres) {
        char[] arr = addres.toCharArray();
        arr[0] = Character.toUpperCase(arr[0]);
        this.addres = new String(arr);;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getTypeDocument() {
        return typeDocument;
    }

    public void setTypeDocument(Integer typeDocument) {
        this.typeDocument = typeDocument;
    }

    @Override
    public void save() {
        if (dni.length() == 8) {
            typeDocument=1;
        } else if (dni.length() == 11) {
            typeDocument=6;
        }
        if(created==null){
            created=new Date();
        }
        updated=new Date();
        super.save();
    }
}
