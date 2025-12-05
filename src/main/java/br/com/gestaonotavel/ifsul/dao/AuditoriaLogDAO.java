package br.com.gestaonotavel.ifsul.dao;

import br.com.gestaonotavel.ifsul.model.AuditoriaLog;
import br.com.gestaonotavel.ifsul.util.JpaUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import java.util.List;

public class AuditoriaLogDAO {

    public void salvar(AuditoriaLog log) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            em.persist(log);
            tx.commit();
        } catch (PersistenceException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            System.err.println("Erro ao salvar log de auditoria: " + e.getMessage());
        } finally {
            em.close();
        }
    }


    public List<AuditoriaLog> listarTodos() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("SELECT a FROM AuditoriaLog a ORDER BY a.timestamp DESC", AuditoriaLog.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}