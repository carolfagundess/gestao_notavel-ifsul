package br.com.gestaonotavel.ifsul.dao;

import br.com.gestaonotavel.ifsul.model.TipoAtividade;
import br.com.gestaonotavel.ifsul.util.JpaUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;

public class TipoAtividadeDAO {

    public TipoAtividade salvar(TipoAtividade tipoAtividade) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            TipoAtividade tipoAtividadeSalvo = em.merge(tipoAtividade);
            tx.commit();
            return tipoAtividadeSalvo;
        } catch (PersistenceException e) {
            tx.rollback();
            throw e;
        } finally {
            em.close();
        }

    }
}
