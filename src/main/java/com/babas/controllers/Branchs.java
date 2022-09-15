package com.babas.controllers;

import com.babas.models.Branch;
import com.babas.models.Brand;
import com.babas.utilities.Babas;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.Vector;

public class Branchs extends Babas {
    private static Root<Branch> root;
    private static CriteriaQuery<Branch> criteria;

    public static Branch get(Integer id) {
        return session.find(Branch.class, id);
    }

    public static Vector<Branch> getTodos(){
        criteria = builder.createQuery(Branch.class);
        criteria.select(criteria.from(Branch.class));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }

    public static Vector<Branch> getActives(){
        criteria = builder.createQuery(Branch.class);
        root=criteria.from(Branch.class);
        criteria.select(root).where(builder.isTrue(root.get("active")));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }
}
