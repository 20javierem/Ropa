package com.babas.models;

import com.babas.utilities.Babas;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity(name = "company_tbl")
public class Company extends Babas {
    @Id
    @GeneratedValue(generator = "increment")
    private Long id;
    private String ruc;
    private String businessName;
    private String tradeName;
    private String directionPrincipal;
    private String logo;
    private String slogan;
    private String city;

    public Long getId() {
        return id;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getTradeName() {
        return tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }

    public String getDirectionPrincipal() {
        return directionPrincipal;
    }

    public void setDirectionPrincipal(String directionPrincipal) {
        this.directionPrincipal = directionPrincipal;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
