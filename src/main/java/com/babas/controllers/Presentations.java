package com.babas.controllers;

import com.babas.models.Presentation;
import com.babas.models.Product;
import com.babas.utilities.Babas;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class Presentations extends Babas {
    private static Root<Product> root;
    private static CriteriaQuery<Product> criteria;

    public static Presentation get(Integer id) {
        return session.find(Presentation.class, id);
    }
}
