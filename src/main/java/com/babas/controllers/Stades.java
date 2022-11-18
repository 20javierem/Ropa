package com.babas.controllers;

import com.babas.models.Brand;
import com.babas.models.Color;
import com.babas.models.Stade;
import com.babas.utilities.Babas;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.Vector;

public class Stades extends Babas {
    private static Root<Stade> root;
    private static CriteriaQuery<Stade> criteria;

    public static Stade get(Integer id) {
        return session.find(Stade.class, id);
    }
    public static Stade getByName(String name) {
        criteria = builder.createQuery(Stade.class);
        root = criteria.from(Stade.class);
        criteria.select(root).
                where(builder.equal(root.get("name"), name));
        return session.createQuery(criteria).getSingleResultOrNull();
    }
    public static Vector<Stade> getTodos(){
        criteria = builder.createQuery(Stade.class);
        criteria.select(criteria.from(Stade.class));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }

    public static Vector<Stade> getActives(){
        criteria = builder.createQuery(Stade.class);
        root=criteria.from(Stade.class);
        criteria.select(root).where(builder.isTrue(root.get("active")));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }
}
