package br.com.gestaonotavel.ifsul.dao;

import br.com.gestaonotavel.ifsul.model.Paciente;
import br.com.gestaonotavel.ifsul.model.Responsavel;
import br.com.gestaonotavel.ifsul.util.JpaUtil;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import java.util.List;

public class PacienteDAO {

    public Paciente salvarPaciente(Paciente paciente) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            Paciente pacienteSalvo = em.merge(paciente);
            tx.commit();
            return pacienteSalvo;
        } catch (PersistenceException ex) {
            tx.rollback();
            throw ex;
        } finally {
            em.close();
        }
    }

    public List<Paciente> listarTodos() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("SELECT DISTINCT p FROM Paciente p LEFT JOIN FETCH p.responsaveisLista", Paciente.class).getResultList();
        } finally {
            em.close();
        }
    }

    public void excluir(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            Paciente paciente = em.find(Paciente.class, id);
            if (paciente != null) em.remove(paciente);
            tx.commit();
        } catch (PersistenceException ex) {
            if (tx.isActive()) tx.rollback();
            throw ex;
        } finally {
            em.close();
        }
    }

    public Paciente buscarPorCpf(String cpfBuscado) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("SELECT p FROM Paciente p WHERE p.cpf = :cpf", Paciente.class)
                    .setParameter("cpf", cpfBuscado).getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    // Salva paciente e responsavel juntos em uma transação para garantir sessão ativa
    public void salvarPacienteEAssociarResponsavel(Paciente paciente, Responsavel responsavel) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            Paciente pacienteGerenciado = em.merge(paciente);
            Responsavel responsavelGerenciado = em.merge(responsavel);
            pacienteGerenciado.adicionarResponsavel(responsavelGerenciado);
            em.merge(pacienteGerenciado);
            em.merge(responsavelGerenciado);
            tx.commit();
        } catch (PersistenceException ex) {
            tx.rollback();
            throw ex;
        } finally {
            em.close();
        }
    }
}