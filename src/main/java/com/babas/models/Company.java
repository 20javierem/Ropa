package com.babas.models;

import com.babas.utilities.Babas;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.GenericGenerator;

@Entity(name = "company_tbl")
public class Company extends Babas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "RUC")
    private String ruc;
    @NotBlank(message = "NOMBRE DE LA EMPRESA")
    private String businessName;
    @NotBlank(message = "NOMBRE COMERCIAL")
    private String tradeName;
    @NotBlank(message = "DIRECCIÓN PRINCIPAL")
    private String directionPrincipal;
    private String logo;
    private String details="";
    private Integer idFact=0;
    private Integer idProduct=0;
    private String token;
    private String webSite;
    private boolean validToken=false;
    private String tokenConsults;

    public Integer getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(Integer idProduct) {
        this.idProduct = idProduct;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getIdFact() {
        return idFact;
    }

    public void setIdFact(Integer idFact) {
        this.idFact = idFact;
    }

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
        this.businessName = businessName.toUpperCase();
    }

    public String getTradeName() {
        return tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName.toUpperCase();
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

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public boolean isValidToken() {
        return validToken;
    }

    public void setValidToken(boolean validToken) {
        this.validToken = validToken;
    }

    public String getTokenConsults() {
        return tokenConsults;
    }

    public void setTokenConsults(String tokenConsults) {
        this.tokenConsults = tokenConsults;
    }
}
