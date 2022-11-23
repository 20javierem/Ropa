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

    public static Vector<Rental> getTodos(){
        criteria = builder.createQuery(Rental.class);
        criteria.select(criteria.from(Rental.class));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }

    public static Vector<Rental> getByRangeOfDate(Branch branch, Date start, Date end){
        criteria = builder.createQuery(Rental.class);
        root=criteria.from(Rental.class);
        criteria.select(root).where(builder.and(
                        builder.between(root.get("created"),Utilities.getDateStart(start),Utilities.getDateEnd(end)),
                        builder.equal(root.get("branch"),branch)))
                .orderBy(builder.desc(root.get("id")));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }

    public static Vector<Rental> getBefore(Branch branch,Date end){
        criteria = builder.createQuery(Rental.class);
        root=criteria.from(Rental.class);
        criteria.select(root).where(builder.and(
                builder.lessThan(root.get("created"),Utilities.getDateLessThan(end))),
                        builder.equal(root.get("branch"),branch))
                .orderBy(builder.desc(root.get("id")));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }

    public static Vector<Rental> getAfter(Branch branch,Date start){
        criteria = builder.createQuery(Rental.class);
        root=criteria.from(Rental.class);
        criteria.select(root).where(builder.and(
                builder.greaterThan(root.get("created"),Utilities.getDateGreaterThan(start))),
                        builder.equal(root.get("branch"),branch))
                .orderBy(builder.desc(root.get("id")));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }

    public static Vector<Rental> getActives(Branch branch){
        criteria=builder.createQuery(Rental.class);
        root=criteria.from(Rental.class);
        criteria.select(root).where(builder.and(
                builder.equal(root.get("branch"),branch),
                builder.equal(root.get("active"),0)
        )).orderBy(builder.desc(root.get("id")));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }

}
