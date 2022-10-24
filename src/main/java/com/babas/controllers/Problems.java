package com.babas.controllers;

import com.babas.models.Color;
import com.babas.models.Problem;
import com.babas.utilities.Babas;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;
import java.util.Vector;

public class Problems extends Babas {
    private static Root<Problem> root;
    private static CriteriaQuery<Problem> criteria;

    public static Problem get(Integer id) {
        return session.find(Problem.class, id);
    }

    public static Vector<Problem> getTodos(){
        criteria = builder.createQuery(Problem.class);
        criteria.select(criteria.from(Problem.class));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }

    public static Vector<Problem> getActives(){
        criteria = builder.createQuery(Problem.class);
        root=criteria.from(Problem.class);
        criteria.select(root).where(builder.isTrue(root.get("active")));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }

    public static void getSons(Problem problem){
        criteria=builder.createQuery(Problem.class);
        root=criteria.from(Problem.class);
        criteria.select(root).where(builder.equal(root.get("problem"),problem));
        problem.getSons().addAll(session.createQuery(criteria).getResultList());
        problem.getSons().forEach(problem1 -> {
            if(problem1.getHasDependency()){
                getSons(problem1);
            }
        });
    }

}
