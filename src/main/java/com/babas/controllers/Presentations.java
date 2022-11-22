package com.babas.controllers;

import com.babas.models.Presentation;
import com.babas.models.Product;
import com.babas.utilities.Babas;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class Presentations extends Babas {
    private static Root<Presentation> root;
    private static CriteriaQuery<Presentation> criteria;

    public static Presentation get(Integer id) {
        return session.find(Presentation.class, id);
    }
    public static Presentation getByUniqueCode(String uniqueCode){
        criteria = builder.createQuery(Presentation.class);
        root=criteria.from(Presentation.class);
        criteria.select(root).where(builder.equal(root.get("uniqueCode"), uniqueCode));
        return session.createQuery(criteria).uniqueResult();
    }
}
