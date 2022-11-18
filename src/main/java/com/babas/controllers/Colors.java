package com.babas.controllers;

import com.babas.models.Color;
import com.babas.models.Product;
import com.babas.models.Size;
import com.babas.utilities.Babas;
import jakarta.persistence.LockModeType;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.Vector;

public class Colors extends Babas {
    private static Root<Color> root;
    private static CriteriaQuery<Color> criteria;

    public static Color get(Integer id) {
        return session.find(Color.class, id);
    }
    public static Color getByName(String name) {
        criteria = builder.createQuery(Color.class);
        root = criteria.from(Color.class);
        criteria.select(root).
                where(builder.equal(root.get("name"), name));
        return session.createQuery(criteria).getSingleResultOrNull();
    }
    public static Vector<Color> getTodos(){
        criteria = builder.createQuery(Color.class);
        criteria.select(criteria.from(Color.class));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }

    public static Vector<Color> getActives(){
        criteria = builder.createQuery(Color.class);
        root=criteria.from(Color.class);
        criteria.select(root).where(builder.isTrue(root.get("active"))).orderBy(builder.asc(root.get("name")));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }
}
