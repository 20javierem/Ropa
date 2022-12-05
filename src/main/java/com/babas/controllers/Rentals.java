package com.babas.controllers;

import com.babas.models.Branch;
import com.babas.models.Rental;
import com.babas.models.Sale;
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

    public static Rental getByNumber(Long numberSale){
        criteria = builder.createQuery(Rental.class);
        root=criteria.from(Rental.class);
        criteria.select(root).where(
                builder.equal(root.get("numberRental"),numberSale));
        return session.createQuery(criteria).getSingleResultOrNull();
    }
    public static List<Rental> getOnWait(){
        criteria = builder.createQuery(Rental.class);
        root=criteria.from(Rental.class);
        criteria.select(root).where(
                        builder.isFalse(root.get("statusSunat")))
                .orderBy(builder.asc(root.get("id")));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }
    public static Vector<Rental> getTodos(){
        criteria = builder.createQuery(Rental.class);
        criteria.select(criteria.from(Rental.class));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }

    public static Vector<Rental> getByRangeOfDate(Date start, Date end){
        criteria = builder.createQuery(Rental.class);
        root=criteria.from(Rental.class);
        criteria.select(root).where(
                        builder.between(root.get("created"),Utilities.getDateStart(start),Utilities.getDateEnd(end)))
                .orderBy(builder.desc(root.get("id")));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }

    public static Vector<Rental> getBefore(Date end){
        criteria = builder.createQuery(Rental.class);
        root=criteria.from(Rental.class);
        criteria.select(root).where(
                builder.lessThan(root.get("created"),Utilities.getDateLessThan(end)))
                .orderBy(builder.desc(root.get("id")));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }

    public static Vector<Rental> getAfter(Date start){
        criteria = builder.createQuery(Rental.class);
        root=criteria.from(Rental.class);
        criteria.select(root).where(
                builder.greaterThan(root.get("created"),Utilities.getDateGreaterThan(start)))
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

}
