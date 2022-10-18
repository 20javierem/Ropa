package com.babas.controllers;

import com.babas.models.*;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.Date;
import java.util.Vector;

public class BoxSessions extends Babas {
    private static Root<BoxSession> root;
    private static CriteriaQuery<BoxSession> criteria;

    public static BoxSession get(Integer id) {
        return session.find(BoxSession.class, id);
    }

    public static Vector<BoxSession> getTodos(){
        criteria = builder.createQuery(BoxSession.class);
        criteria.select(criteria.from(BoxSession.class));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }

    public static BoxSession getByBox(Box box) {
        criteria = builder.createQuery(BoxSession.class);
        root = criteria.from(BoxSession.class);
        criteria.select(root).where(builder.and(builder.equal(root.get("box"), box), builder.isNull(root.get("updated"))));
        return session.createQuery(criteria).uniqueResult();
    }

    public static Vector<BoxSession> getCloseds(){
        criteria = builder.createQuery(BoxSession.class);
        root=criteria.from(BoxSession.class);
        criteria.select(root).where(builder.isNotNull(root.get("updated")));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }

    public static Vector<BoxSession> getByRangeOfDate(Branch branch, Date start, Date end){
        criteria = builder.createQuery(BoxSession.class);
        root=criteria.from(BoxSession.class);
        criteria.select(root).where(builder.and(
                builder.between(root.get("created"), Utilities.getDateStart(start),Utilities.getDateEnd(end)),
                        builder.isNotNull(root.get("updated")),
                        builder.equal(root.get("box").get("branch"),branch)))
                .orderBy(builder.desc(root.get("id")));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }

    public static Vector<BoxSession> getBefore(Branch branch,Date end){
        criteria = builder.createQuery(BoxSession.class);
        root=criteria.from(BoxSession.class);
        criteria.select(root).where(builder.and(
                builder.lessThan(root.get("created"),Utilities.getDateLessThan(end))),
                        builder.isNotNull(root.get("updated")),
                        builder.equal(root.get("box").get("branch"),branch))
                .orderBy(builder.desc(root.get("id")));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }

    public static Vector<BoxSession> getAfter(Branch branch,Date start){
        criteria = builder.createQuery(BoxSession.class);
        root=criteria.from(BoxSession.class);
        criteria.select(root).where(builder.and(
                builder.greaterThan(root.get("created"),Utilities.getDateGreaterThan(start))),
                        builder.isNotNull(root.get("updated")),
                        builder.equal(root.get("box").get("branch"),branch))
                .orderBy(builder.desc(root.get("id")));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }
}
