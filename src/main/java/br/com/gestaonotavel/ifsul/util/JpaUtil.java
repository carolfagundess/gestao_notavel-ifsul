package br.com.gestaonotavel.ifsul.util;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

//Criado apenas uma vez dentro da apliacação de forma centralizada
public class JpaUtil {
    // A fábrica será estática e inicializada uma única vez
    private static final EntityManagerFactory FACTORY = Persistence.createEntityManagerFactory("gestao-notavel-pu");

    // Método para obter uma nova instância de EntityManager
    public static EntityManager getEntityManager() {
        return FACTORY.createEntityManager();
    }

    // Método para fechar a fábrica no final da aplicação
    public static void closeFactory() {
        if (FACTORY.isOpen()) {
            FACTORY.close();
        }
    }
}
