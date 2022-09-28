package com.babas.controllers;

import com.babas.models.Sale;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import jakarta.persistence.LockModeType;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Root;

import java.util.Date;
import java.util.Vector;

public class Sales extends Babas {
    private static Root<Sale> root;
    private static CriteriaQuery<Sale> criteria;

    public static Sale get(Integer id) {
        return session.find(Sale.class, id, LockModeType.NONE);
    }

    public static Vector<Sale> getTodos(){
        criteria = builder.createQuery(Sale.class);
        criteria.select(criteria.from(Sale.class));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }

    public static Vector<Sale> getByRangeOfDate(Date start,Date end){
        criteria = builder.createQuery(Sale.class);
        root=criteria.from(Sale.class);
        criteria.select(root).where(
                builder.between(root.get("created"),Utilities.getDateStart(start),Utilities.getDateEnd(end)))
                .orderBy(builder.desc(root.get("id")));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }

    public static Vector<Sale> getBefore(Date end){
        criteria = builder.createQuery(Sale.class);
        root=criteria.from(Sale.class);
        criteria.select(root).where(
                builder.lessThan(root.get("created"),Utilities.getDateLessThan(end)))
                .orderBy(builder.desc(root.get("id")));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }

    public static Vector<Sale> getAfter(Date start){
        criteria = builder.createQuery(Sale.class);
        root=criteria.from(Sale.class);
        criteria.select(root).where(
                builder.greaterThan(root.get("created"),Utilities.getDateGreaterThan(start)))
                .orderBy(builder.desc(root.get("id")));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }
}
