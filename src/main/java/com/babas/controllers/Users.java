package com.babas.controllers;

import com.babas.models.Branch;
import com.babas.models.User;
import com.babas.utilities.Babas;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.Vector;

public class Users extends Babas {
    private static Root<User> root;
    private static CriteriaQuery<User> criteria;

    public static User get(Integer id) {
        return session.find(User.class, id);
    }

    public static Vector<User> getTodos(){
        criteria = builder.createQuery(User.class);
        criteria.select(criteria.from(User.class));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }

    public static Vector<User> getActives(){
        criteria = builder.createQuery(User.class);
        root=criteria.from(User.class);
        criteria.select(root).where(builder.isTrue(root.get("active")));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }

    public static User getByUserName(String userName){
        criteria = builder.createQuery(User.class);
        root=criteria.from(User.class);
        criteria.select(root).where(builder.equal(root.get("userName"), userName));
        return session.createQuery(criteria).uniqueResult();
    }
}
