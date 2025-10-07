package br.com.gestaonotavel.ifsul.dao;

import br.com.gestaonotavel.ifsul.model.Responsavel;
import br.com.gestaonotavel.ifsul.util.JpaUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import java.util.List;

public class ResponsavelDAO {

    public Responsavel salvarResponsavel(Responsavel responsavelSalvando) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            Responsavel responsavelSalvo = em.merge(responsavelSalvando);
            tx.commit();
            return responsavelSalvo;
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public Responsavel buscarPorId(Long idResponsavel) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.find(Responsavel.class, idResponsavel);

        } catch (Exception e) {
            throw e;
        } finally {
            em.close();
        }
    }

    public List<Responsavel> buscarTodos() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
             return em.createQuery("SELECT r FROM Responsavel r", Responsavel.class).getResultList();
        }catch (Exception e) {
            throw e;
        }finally {
            em.close();
        }
    }

    public void removerResponsavel(Long idResponsavel) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            Responsavel responsavel = em.find(Responsavel.class, idResponsavel);
            if (responsavel != null) {
                em.remove(responsavel);
                tx.commit();
            }
        }catch (Exception e) {
            tx.rollback();
            throw e;
        }finally {
            em.close();
        }
    }

    public Responsavel buscarPorCpf(String cpfResponsavel) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("SELECT r FROM Responsavel r WHERE r.cpf = :cpf", Responsavel.class)
                    .setParameter("cpf", cpfResponsavel)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
}
