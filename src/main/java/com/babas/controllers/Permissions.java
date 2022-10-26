package com.babas.controllers;

import com.babas.models.Permission;
import com.babas.utilities.Babas;
import jakarta.persistence.LockModeType;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.Vector;

public class Permissions extends Babas {
    private static Root<Permission> root;
    private static CriteriaQuery<Permission> criteria;

    public static Permission get(Integer id) {
        return session.find(Permission.class, id, LockModeType.NONE);
    }

    public static Vector<Permission> getGroups(){
        criteria = builder.createQuery(Permission.class);
        root=criteria.from(Permission.class);
        criteria.select(root).where(builder.isTrue(root.get("isgroup")));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }
    public static Vector<Permission> getPermissionToDelete(){
        criteria = builder.createQuery(Permission.class);
        root=criteria.from(Permission.class);
        criteria.select(root).where(builder.and(builder.isFalse(root.get("isgroup")),builder.isEmpty(root.get("users"))));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }
}
