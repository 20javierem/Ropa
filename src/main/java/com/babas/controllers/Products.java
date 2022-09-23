package com.babas.controllers;

import com.babas.models.*;
import com.babas.utilities.Babas;
import jakarta.persistence.LockModeType;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Products extends Babas {
    private static Root<Product> root;
    private static CriteriaQuery<Product> criteria;

    public static Product get(Integer id) {
        return session.find(Product.class, id);
    }

    public static Vector<Product> getTodos(){
        criteria = builder.createQuery(Product.class);
        criteria.select(criteria.from(Product.class));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }

    public static Vector<Product> getActives(){
        criteria = builder.createQuery(Product.class);
        root=criteria.from(Product.class);
        criteria.select(root).where(builder.isTrue(root.get("active")));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }

    private List<Product> getByAtributes(Style style, Color color, Size size, Sex sex,Brand brand,Stade stade,Dimention dimention){
        criteria = builder.createQuery(Product.class);
        root=criteria.from(Product.class);
        criteria.select(root)
                .where(builder.and((Predicate) getPredicates(style,color,size,sex,brand,stade,dimention).spliterator()));
        return new Vector<>(session.createQuery(criteria).getResultList());
    };

    private List<Predicate> getPredicates(Style style, Color color, Size size, Sex sex, Brand brand, Stade stade, Dimention dimention){
        List<Predicate> predicates=new ArrayList<>();
        predicates.add(builder.equal(root.get("style"),style));
        if(color!=null){
            predicates.add(builder.equal(root.get("color"),color));
        }
        if(color!=null){
            predicates.add(builder.equal(root.get("color"),color));
        }
        if(color!=null){
            predicates.add(builder.equal(root.get("color"),color));
        }
        if(color!=null){
            predicates.add(builder.equal(root.get("color"),color));
        }
        return predicates;
    }
}
