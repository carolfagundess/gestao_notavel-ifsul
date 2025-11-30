package br.com.gestaonotavel.ifsul.dao;

import br.com.gestaonotavel.ifsul.model.Especialista;
import br.com.gestaonotavel.ifsul.util.JpaUtil;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import java.util.List;

public class EspecialistaDAO {

    public Especialista salvar(Especialista especialista) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            Especialista especialistaSalvo = em.merge(especialista);
            tx.commit();
            return especialistaSalvo;
        } catch (PersistenceException ex) {
            tx.rollback();
            throw ex;
        } finally {
            em.close();
        }
    }

    public List<Especialista> listarTodos() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("SELECT e FROM Especialista e", Especialista.class).getResultList();
        } finally {
            em.close();
        }
    }

    public Especialista buscarPorRegistroProfissional(String registroBuscado) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("SELECT e FROM Especialista e WHERE e.registroProfissional = :registroBuscado", Especialista.class)
                    .setParameter("registroBuscado", registroBuscado).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } finally {
            em.close();
        }
    }
}