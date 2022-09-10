package com.babas.controllers;

import com.babas.models.Product;
import com.babas.models.Size;
import com.babas.utilities.Babas;
import jakarta.persistence.LockModeType;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.Vector;

public class Sizes extends Babas {
    private static Root<Size> root;
    private static CriteriaQuery<Size> criteria;

    public static Size get(Integer id) {
        return session.find(Size.class, id, LockModeType.NONE);
    }

    public static Vector<Size> getTodos(){
        criteria = builder.createQuery(Size.class);
        criteria.select(criteria.from(Size.class));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }

    public static Vector<Size> getActives(){
        criteria = builder.createQuery(Size.class);
        root=criteria.from(Size.class);
        criteria.select(root).where(builder.isTrue(root.get("active")));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }
}
