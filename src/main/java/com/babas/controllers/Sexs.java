package com.babas.controllers;

import com.babas.models.Color;
import com.babas.models.Sex;
import com.babas.utilities.Babas;
import jakarta.persistence.LockModeType;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.Vector;

public class Sexs extends Babas {
    private static Root<Sex> root;
    private static CriteriaQuery<Sex> criteria;

    public static Sex get(Integer id) {
        return session.find(Sex.class, id, LockModeType.NONE);
    }

    public static Vector<Sex> getTodos(){
        criteria = builder.createQuery(Sex.class);
        criteria.select(criteria.from(Sex.class));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }

    public static Vector<Sex> getActives(){
        criteria = builder.createQuery(Sex.class);
        root=criteria.from(Sex.class);
        criteria.select(root).where(builder.isTrue(root.get("active")));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }
}
