package com.babas.controllers;

import com.babas.models.Branch;
import com.babas.models.Rental;
import com.babas.models.Sale;
import com.babas.models.Transfer;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import jakarta.persistence.LockModeType;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Root;
import org.hibernate.jpa.QueryHints;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

public class Sales extends Babas {
    private static Root<Sale> root;
    private static CriteriaQuery<Sale> criteria;

    public static Sale get(Integer id) {
        return session.find(Sale.class, id);
    }

    public static List<Sale> getOnWait(){
        criteria = builder.createQuery(Sale.class);
        root=criteria.from(Sale.class);
        criteria.select(root).where(builder.and(
                        builder.isFalse(root.get("statusSunat")),
                        builder.isTrue(root.get("active"))))
                .orderBy(builder.asc(root.get("id")));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }

    public static Sale getFirstOnWait(String type){
        criteria = builder.createQuery(Sale.class);
        root=criteria.from(Sale.class);
        criteria.select(root).where(builder.and(
                builder.isFalse(root.get("statusSunat")),
                        builder.equal(root.get("typeVoucher"),type)))
                .orderBy(builder.asc(root.get("id")));
        List<Sale> sales=session.createQuery(criteria).getResultList();
        return sales.isEmpty()?null:sales.get(0);
    }

    public static Sale getByCorrelativoAndType(Long correlativo,String type){
        criteria = builder.createQuery(Sale.class);
        root=criteria.from(Sale.class);
        criteria.select(root).where(builder.and(
                builder.equal(root.get("correlativo"),correlativo),
                builder.equal(root.get("typeVoucher"),type))
        );
        return session.createQuery(criteria).getSingleResultOrNull();
    }

    public static Vector<Sale> getTodos(){
        criteria = builder.createQuery(Sale.class);
        criteria.select(criteria.from(Sale.class));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }

    public static Vector<Sale> getByRangeOfDate( Date start, Date end){
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
        criteria.select(root).where(builder.and(
                builder.lessThan(root.get("created"),Utilities.getDateLessThan(end))))
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

    public static Vector<Sale> getLast30(){
        criteria = builder.createQuery(Sale.class);
        root=criteria.from(Sale.class);
        criteria.select(root).orderBy(builder.desc(root.get("id")));
        return new Vector<>(session.createQuery(criteria).setMaxResults(30).getResultList());
    }
}
