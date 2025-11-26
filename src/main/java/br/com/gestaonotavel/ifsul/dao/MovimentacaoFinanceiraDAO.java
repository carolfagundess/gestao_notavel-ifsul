package br.com.gestaonotavel.ifsul.dao;

import br.com.gestaonotavel.ifsul.model.MovimentacaoFinanceira;
import br.com.gestaonotavel.ifsul.util.JpaUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import java.time.LocalDate;
import java.util.List;

public class MovimentacaoFinanceiraDAO {

    public MovimentacaoFinanceira salvar(MovimentacaoFinanceira movimentacao) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            MovimentacaoFinanceira salvo = em.merge(movimentacao);
            tx.commit();
            return salvo;
        } catch (PersistenceException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public List<MovimentacaoFinanceira> listarTodas() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("SELECT m FROM MovimentacaoFinanceira m ORDER BY m.dataMovimentacao DESC", MovimentacaoFinanceira.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<MovimentacaoFinanceira> listarPorPeriodo(LocalDate inicio, LocalDate fim) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("SELECT m FROM MovimentacaoFinanceira m WHERE m.dataMovimentacao BETWEEN :inicio AND :fim ORDER BY m.dataMovimentacao DESC", MovimentacaoFinanceira.class)
                    .setParameter("inicio", inicio.atStartOfDay())
                    .setParameter("fim", fim.atTime(23, 59, 59))
                    .getResultList();
        } finally {
            em.close();
        }
    }
}