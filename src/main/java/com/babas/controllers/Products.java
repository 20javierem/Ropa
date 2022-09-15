package com.babas.controllers;

import com.babas.models.Product;
import com.babas.utilities.Babas;
import jakarta.persistence.LockModeType;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.Vector;

public class Products extends Babas {
    private static Root<Product> root;
    private static CriteriaQuery<Product> criteria;

    public static Product get(Integer id) {
        return session.find(Product.class, id);
    }

    public static Vector<Product> getTodos(){
        criteria = builder.createQuery(Product.class);
        criteria.select(criteria.from(Product.class));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }

    public static Vector<Product> getActives(){
        criteria = builder.createQuery(Product.class);
        root=criteria.from(Product.class);
        criteria.select(root).where(builder.isTrue(root.get("active")));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }
}
