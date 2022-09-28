package com.babas.controllers;

import com.babas.models.Box;
import com.babas.models.BoxSession;
import com.babas.models.Size;
import com.babas.models.User;
import com.babas.utilities.Babas;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

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

    public static BoxSession getByBox(Box box){
        criteria = builder.createQuery(BoxSession.class);
        root=criteria.from(BoxSession.class);
        criteria.select(root).where(builder.and(builder.equal(root.get("box"), box),builder.isNull(root.get("updated"))));
        return session.createQuery(criteria).uniqueResult();
    }
}
