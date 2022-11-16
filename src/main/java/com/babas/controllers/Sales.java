package com.babas.controllers;

import com.babas.models.Branch;
import com.babas.models.Sale;
import com.babas.models.Transfer;
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

    public static Sale getByNumber(Long numberSale){
        criteria = builder.createQuery(Sale.class);
        root=criteria.from(Sale.class);
        criteria.select(root).where(
                builder.equal(root.get("numberSale"),numberSale));
        return session.createQuery(criteria).getSingleResultOrNull();
    }

    public static Vector<Sale> getTodos(){
        criteria = builder.createQuery(Sale.class);
        criteria.select(criteria.from(Sale.class));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }

    public static Vector<Sale> getByRangeOfDate(Branch branch, Date start, Date end){
        criteria = builder.createQuery(Sale.class);
        root=criteria.from(Sale.class);
        criteria.select(root).where(builder.and(
                        builder.between(root.get("created"),Utilities.getDateStart(start),Utilities.getDateEnd(end)),
                        builder.equal(root.get("branch"),branch)))
                .orderBy(builder.desc(root.get("id")));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }

    public static Vector<Sale> getBefore(Branch branch,Date end){
        criteria = builder.createQuery(Sale.class);
        root=criteria.from(Sale.class);
        criteria.select(root).where(builder.and(
                builder.lessThan(root.get("created"),Utilities.getDateLessThan(end))),
                        builder.equal(root.get("branch"),branch))
                .orderBy(builder.desc(root.get("id")));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }

    public static Vector<Sale> getAfter(Branch branch,Date start){
        criteria = builder.createQuery(Sale.class);
        root=criteria.from(Sale.class);
        criteria.select(root).where(builder.and(
                builder.greaterThan(root.get("created"),Utilities.getDateGreaterThan(start))),
                        builder.equal(root.get("branch"),branch))
                .orderBy(builder.desc(root.get("id")));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }
}
