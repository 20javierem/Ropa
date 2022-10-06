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
        return session.find(Reserve.class, id, LockModeType.NONE);
    }

    public static Vector<Reserve> getTodos(){
        criteria = builder.createQuery(Reserve.class);
        criteria.select(criteria.from(Reserve.class));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }

    public static Vector<Reserve> getByRangeOfDate(Branch branch, Date start, Date end){
        criteria = builder.createQuery(Reserve.class);
        root=criteria.from(Reserve.class);
        criteria.select(root).where(builder.and(
                        builder.between(root.get("created"),Utilities.getDateStart(start),Utilities.getDateEnd(end)),
                        builder.equal(root.get("branch"),branch)))
                .orderBy(builder.desc(root.get("id")));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }

    public static Vector<Reserve> getBefore(Branch branch,Date end){
        criteria = builder.createQuery(Reserve.class);
        root=criteria.from(Reserve.class);
        criteria.select(root).where(builder.and(
                builder.lessThan(root.get("created"),Utilities.getDateLessThan(end))),
                        builder.equal(root.get("branch"),branch))
                .orderBy(builder.desc(root.get("id")));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }

    public static Vector<Reserve> getAfter(Branch branch,Date start){
        criteria = builder.createQuery(Reserve.class);
        root=criteria.from(Reserve.class);
        criteria.select(root).where(builder.and(
                builder.greaterThan(root.get("created"),Utilities.getDateGreaterThan(start))),
                        builder.equal(root.get("branch"),branch))
                .orderBy(builder.desc(root.get("id")));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }

    public static Vector<Reserve> getActives(Branch branch){
        criteria=builder.createQuery(Reserve.class);
        root=criteria.from(Reserve.class);
        criteria.select(root).where(builder.and(
                builder.equal(root.get("branch"),branch),
                builder.isTrue(root.get("active"))
        )).orderBy(builder.desc(root.get("id")));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }

}
