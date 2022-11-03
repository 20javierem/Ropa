package com.babas.controllers;

import com.babas.models.Brand;
import com.babas.models.Color;
import com.babas.utilities.Babas;
import jakarta.persistence.LockModeType;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.Vector;

public class Brands extends Babas {
    private static Root<Brand> root;
    private static CriteriaQuery<Brand> criteria;

    public static Brand get(Integer id) {
        return session.find(Brand.class, id);
    }
    public static Brand get(String name) {
        criteria = builder.createQuery(Brand.class);
        root = criteria.from(Brand.class);
        criteria.select(root).
                where(builder.equal(root.get("name"), name));
        return session.createQuery(criteria).getSingleResultOrNull();
    }
    public static Vector<Brand> getTodos(){
        criteria = builder.createQuery(Brand.class);
        criteria.select(criteria.from(Brand.class));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }

    public static Vector<Brand> getActives(){
        criteria = builder.createQuery(Brand.class);
        root=criteria.from(Brand.class);
        criteria.select(root).where(builder.isTrue(root.get("active")));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }
}
