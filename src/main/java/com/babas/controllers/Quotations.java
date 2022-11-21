package com.babas.controllers;

import com.babas.models.Branch;
import com.babas.models.Quotation;
import com.babas.models.Sale;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import jakarta.persistence.LockModeType;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.Date;
import java.util.Vector;

public class Quotations extends Babas {
    private static Root<Quotation> root;
    private static CriteriaQuery<Quotation> criteria;

    public static Quotation get(Integer id) {
        return session.find(Quotation.class, id, LockModeType.NONE);
    }

    public static Quotation getByNumber(Long numberSale){
        criteria = builder.createQuery(Quotation.class);
        root=criteria.from(Quotation.class);
        criteria.select(root).where(
                builder.equal(root.get("numberQuotation"),numberSale));
        return session.createQuery(criteria).getSingleResultOrNull();
    }

    public static Vector<Quotation> getTodos(){
        criteria = builder.createQuery(Quotation.class);
        criteria.select(criteria.from(Quotation.class));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }

    public static Vector<Quotation> getByRangeOfDate(Date start, Date end){
        criteria = builder.createQuery(Quotation.class);
        root=criteria.from(Quotation.class);
        criteria.select(root).where(
                        builder.between(root.get("created"),Utilities.getDateStart(start),Utilities.getDateEnd(end)))
                .orderBy(builder.desc(root.get("id")));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }

    public static Vector<Quotation> getBefore(Date end){
        criteria = builder.createQuery(Quotation.class);
        root=criteria.from(Quotation.class);
        criteria.select(root).where(
                builder.lessThan(root.get("created"),Utilities.getDateLessThan(end)))
                .orderBy(builder.desc(root.get("id")));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }

    public static Vector<Quotation> getAfter(Date start){
        criteria = builder.createQuery(Quotation.class);
        root=criteria.from(Quotation.class);
        criteria.select(root).where(
                builder.greaterThan(root.get("created"),Utilities.getDateGreaterThan(start)))
                .orderBy(builder.desc(root.get("id")));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }
}
