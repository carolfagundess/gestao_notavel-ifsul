package br.com.gestaonotavel.ifsul.dao;

import br.com.gestaonotavel.ifsul.model.Atividade;
import br.com.gestaonotavel.ifsul.util.JpaUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;

public class AtividadeDAO {
    public Atividade salvar(Atividade atividade) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            Atividade atividadeSalvo = em.merge(atividade);
            tx.commit();
            return atividadeSalvo;
        } catch (PersistenceException e) {
            tx.rollback();
            throw e;
        } finally {
            em.close();
        }

    }
}
