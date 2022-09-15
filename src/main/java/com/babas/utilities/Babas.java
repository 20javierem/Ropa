package com.babas.utilities;

import jakarta.persistence.LockModeType;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.hibernate.*;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;


public class Babas {
    public static Session session;
    protected static CriteriaBuilder builder;
    public static boolean state;
    private static SessionFactory sessionFactory;
    public static void initialize() {
        buildSessionFactory();
        state=true;
    }
    private static void buildSessionFactory() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        session = sessionFactory.openSession();
        builder = session.getCriteriaBuilder();
    }

    public void refresh(){
        session.beginTransaction();
        session.refresh(this);
        session.getTransaction().commit();
    }

    public void save(){
        session.beginTransaction();
        session.persist(this);
        session.getTransaction().commit();
    }
    public void delete(){
        session.beginTransaction();
        session.remove(this);
        session.getTransaction().commit();
    }
    public static void close(){
        session.close();
        state=false;
    }

}