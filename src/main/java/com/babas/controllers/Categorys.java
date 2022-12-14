package com.babas.controllers;

import com.babas.models.Category;
import com.babas.models.Product;
import com.babas.utilities.Babas;
import jakarta.persistence.LockModeType;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.Vector;

public class Categorys extends Babas {
    private static Root<Category> root;
    private static CriteriaQuery<Category> criteria;

    public static Category get(Integer id) {
        return session.find(Category.class, id);
    }
    public static Category get(String name) {
        criteria = builder.createQuery(Category.class);
        root = criteria.from(Category.class);
        criteria.select(root).
                where(builder.equal(root.get("name"), name));
        return session.createQuery(criteria).getSingleResultOrNull();
    }
    public static Vector<Category> getTodos(){
        criteria = builder.createQuery(Category.class);
        criteria.select(criteria.from(Category.class));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }

    public static Vector<Category> getActives(){
        criteria = builder.createQuery(Category.class);
        root=criteria.from(Category.class);
        criteria.select(root).where(builder.isTrue(root.get("active"))).orderBy(builder.asc(root.get("name")));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }
}
