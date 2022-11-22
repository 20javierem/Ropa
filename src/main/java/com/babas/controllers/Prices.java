package com.babas.controllers;

import com.babas.models.Presentation;
import com.babas.models.Price;
import com.babas.utilities.Babas;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class Prices extends Babas {
    private static Root<Price> root;
    private static CriteriaQuery<Price> criteria;

    public static Price get(Integer id) {
        return session.find(Price.class, id);
    }

    public static Price getByUniqueCode(String uniqueCode,double price){
        criteria = builder.createQuery(Price.class);
        root=criteria.from(Price.class);
        criteria.select(root).where(builder.and(
                builder.equal(root.get("presentation").get("uniqueCode"), uniqueCode),
                builder.equal(root.get("price"),price)
        ));
        return session.createQuery(criteria).uniqueResult();
    }
}
