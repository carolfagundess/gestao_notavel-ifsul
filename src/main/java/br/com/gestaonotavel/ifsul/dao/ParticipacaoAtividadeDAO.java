package br.com.gestaonotavel.ifsul.dao;

import br.com.gestaonotavel.ifsul.model.Atividade;
import br.com.gestaonotavel.ifsul.model.ParticipacaoAtividade;
import br.com.gestaonotavel.ifsul.model.Responsavel;
import br.com.gestaonotavel.ifsul.util.JpaUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import java.util.List;

public class ParticipacaoAtividadeDAO {

    public ParticipacaoAtividade salvar(ParticipacaoAtividade participacao) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            ParticipacaoAtividade participacaoSalva = em.merge(participacao);
            tx.commit();
            return participacaoSalva;
        } catch (PersistenceException e) {
            tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public List<ParticipacaoAtividade> listarPorResponsavel(Responsavel responsavel) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("SELECT p FROM ParticipacaoAtividade p JOIN FETCH p.atividade WHERE p.responsavel = :responsavel ORDER BY p.dataRegistro DESC", ParticipacaoAtividade.class)
                    .setParameter("responsavel", responsavel)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<ParticipacaoAtividade> listarPorAtividade(Atividade atividade) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("SELECT p FROM ParticipacaoAtividade p JOIN FETCH p.responsavel WHERE p.atividade = :atividade ORDER BY p.dataRegistro DESC", ParticipacaoAtividade.class)
                    .setParameter("atividade", atividade)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public ParticipacaoAtividade buscarPorResponsavelEAtividade(Responsavel responsavel, Atividade atividade) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("SELECT p FROM ParticipacaoAtividade p WHERE p.responsavel = :responsavel AND p.atividade = :atividade", ParticipacaoAtividade.class)
                    .setParameter("responsavel", responsavel)
                    .setParameter("atividade", atividade)
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
}