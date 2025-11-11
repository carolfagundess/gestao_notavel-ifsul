package br.com.gestaonotavel.ifsul.dao;

import br.com.gestaonotavel.ifsul.model.TipoAtividade;
import br.com.gestaonotavel.ifsul.util.JpaUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import java.util.List;

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

    public List<TipoAtividade> listarTodos() {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            return em.createQuery("SELECT t FROM TipoAtividade t", TipoAtividade.class).getResultList();
        }catch(PersistenceException e){
            throw e;
        }finally {
            em.close();
        }
    }
}
