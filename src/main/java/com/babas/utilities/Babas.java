package com.babas.utilities;

import com.babas.models.BoxSession;
import com.babas.models.Company;
import com.babas.models.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.hibernate.*;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.service.spi.ServiceException;


public class Babas {
    public static Session session;
    protected static CriteriaBuilder builder;
    public static boolean state=false;
    private static SessionFactory sessionFactory;
    public static User user;
    public static BoxSession boxSession =new BoxSession();
    public static Company company=new Company();
    
    public static void initialize() {
        buildSessionFactory();
    }

    private static void buildSessionFactory() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            session = sessionFactory.openSession();
            builder = session.getCriteriaBuilder();
            state=true;
        }catch (ServiceException e){
            System.out.println("error de conexion");
        }

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
    public static void close(){
        session.close();
        state=false;
    }

}