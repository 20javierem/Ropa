package com.babas.controllers;

import com.babas.models.Category;
import com.babas.models.Company;
import com.babas.utilities.Babas;

public class Companys extends Babas {

    public static Company get(Integer id) {
        return session.find(Company.class, id);
    }
}
