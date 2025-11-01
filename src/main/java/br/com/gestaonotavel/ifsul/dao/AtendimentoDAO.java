package br.com.gestaonotavel.ifsul.dao;

import br.com.gestaonotavel.ifsul.model.Atendimento;
import br.com.gestaonotavel.ifsul.util.JpaUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import java.time.LocalDateTime;
import java.util.List;

public class AtendimentoDAO {

    public Atendimento salvar(Atendimento atendimento) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            Atendimento atendimentoSalvo = em.merge(atendimento);
            tx.commit();
            return atendimentoSalvo;
        }catch (PersistenceException e){
            tx.rollback();
            throw e;
        }finally {
            em.close();
        }
    }

    public List<Atendimento> buscarPorEspecialista(Integer idEspecialista) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("SELECT a FROM Atendimento a WHERE a.especialista.id = :idEspecialista", Atendimento.class)
                    .setParameter("idEspecialista", idEspecialista)
                    .getResultList();
        }catch (NoResultException e){
            return null;
        }finally {
            em.close();
        }
    }

    public List<Atendimento> buscarPorEspecialistaEDataHora(Integer idEspecialista, LocalDateTime dataHora){
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("SELECT a FROM Atendimento a WHERE a.especialista.id = :idEspecialista AND a.dataHora = :dataHora", Atendimento.class)
                    .setParameter("idEspecialista", idEspecialista)
                    .setParameter("dataHora", dataHora)
                    .getResultList();
        }catch (NoResultException e){
            return null;
        }finally {
            em.close();
        }
    }
}
