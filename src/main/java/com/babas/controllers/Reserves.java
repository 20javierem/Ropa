package com.babas.controllers;

import com.babas.models.Branch;
import com.babas.models.Rental;
import com.babas.models.Reserve;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import jakarta.persistence.LockModeType;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.Date;
import java.util.Vector;

public class Reserves extends Babas {
    private static Root<Reserve> root;
    private static CriteriaQuery<Reserve> criteria;

    public static Reserve get(Integer id) {
        return session.find(Reserve.class, id);
    }

    public static Reserve getByNumber(Long numberSale){
        criteria = builder.createQuery(Reserve.class);
        root=criteria.from(Reserve.class);
        criteria.select(root).where(
                builder.equal(root.get("numberReserve"),numberSale));
        return session.createQuery(criteria).getSingleResultOrNull();
    }

    public static Vector<Reserve> getTodos(){
        criteria = builder.createQuery(Reserve.class);
        criteria.select(criteria.from(Reserve.class));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }

    public static Vector<Reserve> getByRangeOfDate( Date start, Date end){
        criteria = builder.createQuery(Reserve.class);
        root=criteria.from(Reserve.class);
        criteria.select(root).where(
                        builder.between(root.get("created"),Utilities.getDateStart(start),Utilities.getDateEnd(end)))
                .orderBy(builder.desc(root.get("id")));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }

    public static Vector<Reserve> getBefore(Date end){
        criteria = builder.createQuery(Reserve.class);
        root=criteria.from(Reserve.class);
        criteria.select(root).where(
                builder.lessThan(root.get("created"),Utilities.getDateLessThan(end)))
                .orderBy(builder.desc(root.get("id")));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }

    public static Vector<Reserve> getAfter(Date start){
        criteria = builder.createQuery(Reserve.class);
        root=criteria.from(Reserve.class);
        criteria.select(root).where(
                builder.greaterThan(root.get("created"),Utilities.getDateGreaterThan(start)))
                .orderBy(builder.desc(root.get("id")));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }

    public static Vector<Reserve> getActives(){
        criteria=builder.createQuery(Reserve.class);
        root=criteria.from(Reserve.class);
        criteria.select(root).where(
                        builder.equal(root.get("active"),0))
                .orderBy(builder.desc(root.get("id")));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }

}
