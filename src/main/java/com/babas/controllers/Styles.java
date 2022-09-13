package com.babas.controllers;

import com.babas.models.Category;
import com.babas.models.Style;
import com.babas.utilities.Babas;
import jakarta.persistence.LockModeType;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.Vector;

public class Styles extends Babas {
    private static Root<Style> root;
    private static CriteriaQuery<Style> criteria;

    public static Style get(Integer id) {
        return session.find(Style.class, id, LockModeType.NONE);
    }

    public static Vector<Style> getTodos(){
        criteria = builder.createQuery(Style.class);
        criteria.select(criteria.from(Style.class));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }

    public static Vector<Style> search(String search){
        criteria = builder.createQuery(Style.class);
        root=criteria.from(Style.class);
        criteria.select(root).where(builder.like(root.get("name"),"%"+search+"%"));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }
}
