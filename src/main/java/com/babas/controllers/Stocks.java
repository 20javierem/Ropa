package com.babas.controllers;

import com.babas.models.*;
import com.babas.utilities.Babas;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.Vector;

public class Stocks extends Babas {
    private static Root<Stock> root;
    private static CriteriaQuery<Stock> criteria;

    public static Stock get(Integer id) {
        return session.find(Stock.class, id);
    }

    public static Vector<Stock> getActives(){
        criteria = builder.createQuery(Stock.class);
        root=criteria.from(Stock.class);
        criteria.select(root).where(builder.isTrue(root.get("active")));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }

    public static Stock getStock(Branch branch, Product product){
        criteria = builder.createQuery(Stock.class);
        root=criteria.from(Stock.class);
        criteria.select(root).where(builder.and(
                builder.equal(root.get("branch"), branch)),
                builder.equal(root.get("product"),product));
        return session.createQuery(criteria).getSingleResultOrNull();
    }
}
