package br.com.gestaonotavel.ifsul.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JpaUtil {

    private static final EntityManagerFactory FACTORY = Persistence.createEntityManagerFactory("gestao-notavel-pu");

    public static EntityManager getEntityManager() {
        return FACTORY.createEntityManager();
    }

    public static void closeFactory() {
        if (FACTORY.isOpen()) {
            FACTORY.close();
        }
    }
}