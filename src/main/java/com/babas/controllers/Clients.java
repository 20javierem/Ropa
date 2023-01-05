package com.babas.controllers;

import com.babas.models.Client;
import com.babas.models.Color;
import com.babas.models.User;
import com.babas.modelsFacture.ApiClient;
import com.babas.utilities.Babas;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.Vector;

public class Clients extends Babas {
    private static Root<Client> root;
    private static CriteriaQuery<Client> criteria;

    public static Client get(Integer id) {
        return session.find(Client.class, id);
    }

    public static Vector<Client> getTodos(){
        criteria = builder.createQuery(Client.class);
        criteria.select(criteria.from(Client.class));
        return new Vector<>(session.createQuery(criteria).getResultList());
    }

    public static Client getByDNI(String dni,boolean api){
        criteria = builder.createQuery(Client.class);
        root=criteria.from(Client.class);
        criteria.select(root).where(builder.equal(root.get("dni"), dni));
        Client client=session.createQuery(criteria).uniqueResult();
        if(client!=null){
            return client;
        }else if(api){
            return ApiClient.getClient(dni);
        }else{
            return null;
        }
    }
}
