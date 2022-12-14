package com.babas.controllers;

import com.babas.models.Branch;
import com.babas.models.Rental;
import com.babas.models.Sale;
import com.babas.models.Transfer;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import jakarta.persistence.LockModeType;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.Date;
import java.util.List;
import java.util.Vector;

public class Rentals extends Babas {
    private static Root<Rental> root;
    private static CriteriaQuery<Rental> criteria;

    public static Rental get(Integer id) {
        return session.find(Rental.class, id);
    }

    public static Rental getByCorrelativoAndType(Long correlativo,String type){
        criteria = builder.createQuery(Rental.class);
        root=criteria.from(Rental.class);
        criteria.select(root).where(builder.and(
                builder.equal(root.get("correlativo"),correlativo),
                builder.equal(root.get("typeVoucher"),type)));
        return session.createQuery(criteria).getSingleResultOrNull();
    }

    public static List<Rental> getOnWait(){
        criteria = builder.createQuery(Rental.class);
        root=criteria.from(Rental.class);
        criteria.select(root).where(builder.and(
                        builder.isFalse(root.get("statusSunat")),
                        builder.equal(root.get("active"),1)))
                .orderBy(builder.asc(root.get("id")));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }
    public static Rental getFirstOnWait(String type){
        criteria = builder.createQuery(Rental.class);
        root=criteria.from(Rental.class);
        criteria.select(root).where(builder.and(
                        builder.isFalse(root.get("statusSunat")),
                        builder.equal(root.get("typeVoucher"),type)))
                .orderBy(builder.asc(root.get("id")));
        List<Rental> sales=session.createQuery(criteria).getResultList();
        return sales.isEmpty()?null:sales.get(0);
    }
    public static Vector<Rental> getTodos(){
        criteria = builder.createQuery(Rental.class);
        criteria.select(criteria.from(Rental.class));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }

    public static Vector<Rental> getByRangeOfDate(Date start, Date end){
        criteria = builder.createQuery(Rental.class);
        root=criteria.from(Rental.class);
        criteria.select(root).where(builder.and(
                        builder.between(root.get("created"),Utilities.getDateStart(start),Utilities.getDateEnd(end))),
                        builder.notEqual(root.get("active"),0))
                .orderBy(builder.desc(root.get("id")));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }

    public static Vector<Rental> getBefore(Date end){
        criteria = builder.createQuery(Rental.class);
        root=criteria.from(Rental.class);
        criteria.select(root).where(builder.and(
                builder.lessThan(root.get("created"),Utilities.getDateLessThan(end))),
                        builder.notEqual(root.get("active"),0))
                .orderBy(builder.desc(root.get("id")));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }

    public static Vector<Rental> getAfter(Date start){
        criteria = builder.createQuery(Rental.class);
        root=criteria.from(Rental.class);
        criteria.select(root).where(builder.and(
                builder.greaterThan(root.get("created"),Utilities.getDateGreaterThan(start))),
                        builder.notEqual(root.get("active"),0))
                .orderBy(builder.desc(root.get("id")));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }

    public static Vector<Rental> getActives(){
        criteria=builder.createQuery(Rental.class);
        root=criteria.from(Rental.class);
        criteria.select(root).where(
                builder.equal(root.get("active"),0)
        ).orderBy(builder.desc(root.get("id")));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }
    public static Vector<Rental> getLast30(){
        criteria = builder.createQuery(Rental.class);
        root=criteria.from(Rental.class);
        criteria.select(root).where(
                builder.notEqual(root.get("active"),0)
        ).orderBy(builder.desc(root.get("id")));
        return new Vector<>(session.createQuery(criteria).setMaxResults(30).getResultList());
    }
}
