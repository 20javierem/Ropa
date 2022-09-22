package com.babas.controllers;

import com.babas.models.Dimention;
import com.babas.utilities.Babas;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.Vector;

public class Dimentions extends Babas {
    private static Root<Dimention> root;
    private static CriteriaQuery<Dimention> criteria;

    public static Dimention get(Integer id) {
        return session.find(Dimention.class, id);
    }

    public static Vector<Dimention> getTodos(){
        criteria = builder.createQuery(Dimention.class);
        criteria.select(criteria.from(Dimention.class));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }

    public static Vector<Dimention> getActives(){
        criteria = builder.createQuery(Dimention.class);
        root=criteria.from(Dimention.class);
        criteria.select(root).where(builder.isTrue(root.get("active")));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }
}
