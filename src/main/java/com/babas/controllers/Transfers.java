package com.babas.controllers;

import com.babas.models.Transfer;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import jakarta.persistence.LockModeType;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.Date;
import java.util.Vector;

public class Transfers extends Babas {
    private static Root<Transfer> root;
    private static CriteriaQuery<Transfer> criteria;

    public static Transfer get(Integer id) {
        return session.find(Transfer.class, id, LockModeType.NONE);
    }

    public static Vector<Transfer> getTodos(){
        criteria = builder.createQuery(Transfer.class);
        criteria.select(criteria.from(Transfer.class));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }

    public static Vector<Transfer> getByRangeOfDate(Date start,Date end){
        criteria = builder.createQuery(Transfer.class);
        root=criteria.from(Transfer.class);
        criteria.select(root).where(builder.between(root.get("created"),Utilities.getDateStart(start),Utilities.getDateEnd(end)));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }

    public static Vector<Transfer> getBefore(Date end){
        criteria = builder.createQuery(Transfer.class);
        root=criteria.from(Transfer.class);
        criteria.select(root).where(builder.lessThan(root.get("created"),Utilities.getDateLessThan(end)));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }

    public static Vector<Transfer> getAfter(Date start){
        criteria = builder.createQuery(Transfer.class);
        root=criteria.from(Transfer.class);
        criteria.select(root).where(builder.greaterThan(root.get("created"),Utilities.getDateGreaterThan(start)));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }
}
