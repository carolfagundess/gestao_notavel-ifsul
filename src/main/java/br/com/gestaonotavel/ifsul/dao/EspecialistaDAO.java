package br.com.gestaonotavel.ifsul.dao;

import br.com.gestaonotavel.ifsul.model.Especialista;
import br.com.gestaonotavel.ifsul.util.JpaUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
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
        } catch (Exception ex) {
            tx.rollback();
            throw ex;
        } finally {
            em.close();
        }
    }

    public Especialista buscarPorId(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.find(Especialista.class, id);
        } finally {
            em.close();
        }
    }

    public List<Especialista> listarTodos() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("SELECT e FROM Especialista e", Especialista.class).getResultList();
        } catch (Exception ex) {
            throw ex;
        } finally {
            em.close();
        }
    }

    public void excluir(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            Especialista especialista = em.find(Especialista.class, id);
            if (especialista != null) {
                em.remove(especialista);
            }
            tx.commit();
        } catch (Exception ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw ex;
        } finally {
            em.close();
        }
    }

    public Especialista buscarPorRegistroProfissional(String registroBuscado) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("SELECT e FROM Especialista e WHERE e.registroProfissional = :registroBuscado", Especialista.class)
                    .setParameter("registroBuscado", registroBuscado)
                    .getSingleResult();
        }catch (NoResultException ex){
            return null;
        }finally {
            em.close();
        }

    }
}
