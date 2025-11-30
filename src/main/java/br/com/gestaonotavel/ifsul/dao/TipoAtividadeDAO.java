package br.com.gestaonotavel.ifsul.dao;

import br.com.gestaonotavel.ifsul.model.TipoAtividade;
import br.com.gestaonotavel.ifsul.util.JpaUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
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
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public List<TipoAtividade> listarTodos() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("SELECT t FROM TipoAtividade t ORDER BY t.nome", TipoAtividade.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public TipoAtividade buscarPorNome(String nome) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("SELECT t FROM TipoAtividade t WHERE t.nome = :nome", TipoAtividade.class)
                    .setParameter("nome", nome)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    public void excluir(Integer id) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            TipoAtividade tipo = em.find(TipoAtividade.class, id);
            if (tipo != null) {
                em.remove(tipo);
            }
            tx.commit();
        } catch (PersistenceException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}