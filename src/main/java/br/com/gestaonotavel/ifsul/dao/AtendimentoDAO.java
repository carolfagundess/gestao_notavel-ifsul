package br.com.gestaonotavel.ifsul.dao;

import br.com.gestaonotavel.ifsul.model.Atendimento;
import br.com.gestaonotavel.ifsul.util.JpaUtil;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import java.time.LocalDateTime;
import java.util.Collections;
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

    public Atendimento buscarPorId(Integer id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.find(Atendimento.class, id);
        } finally {
            em.close();
        }
    }

    public List<Atendimento> buscarPorEspecialista(Integer idEspecialista) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            if (idEspecialista == null) {
                return em.createQuery("SELECT a FROM Atendimento a", Atendimento.class).getResultList();
            }
            return em.createQuery("SELECT a FROM Atendimento a WHERE a.especialista.idEspecialista = :idEspecialista", Atendimento.class)
                    .setParameter("idEspecialista", idEspecialista)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Atendimento> buscarPorEspecialistaEDataHora(Integer idEspecialista, LocalDateTime dataHora){
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("SELECT a FROM Atendimento a WHERE a.especialista.idEspecialista = :idEspecialista AND a.dataHora = :dataHora", Atendimento.class)
                    .setParameter("idEspecialista", idEspecialista)
                    .setParameter("dataHora", dataHora)
                    .getResultList();
        }catch (NoResultException e){
            return null;
        }finally {
            em.close();
        }
    }

    /**
     * Busca atendimentos de um especialista que se sobrepõem a um determinado intervalo de tempo.
     * Um atendimento existente (E) se sobrepõe a um novo intervalo (N) se:
     * (E.start < N.end) AND (E.end > N.start)
     *
     * @param idEspecialista ID do especialista
     * @param inicioNovoAtendimento Início do novo agendamento
     * @param fimNovoAtendimento Fim do novo agendamento
     * @return Lista de atendimentos conflitantes
     */
    public List<Atendimento> buscarConflitosDeHorario(Integer idEspecialista, LocalDateTime inicioNovoAtendimento, LocalDateTime fimNovoAtendimento) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            // A duração é armazenada em minutos no especialista. Precisamos derivar o fim do atendimento existente.
            // Usamos a função TIMESTAMPADD do SQL, que pode variar entre dialetos (ex: DATE_ADD no MySQL).
            // JPQL não tem uma função padrão para adicionar minutos, então usamos uma query nativa ou uma abordagem mais complexa.
            // Para simplificar e manter a portabilidade, vamos buscar os atendimentos do dia e filtrar no serviço.
            // Esta abordagem é menos performática, mas mais segura em termos de JPQL.
            LocalDateTime inicioDoDia = inicioNovoAtendimento.toLocalDate().atStartOfDay();
            LocalDateTime fimDoDia = inicioNovoAtendimento.toLocalDate().atTime(23, 59, 59);

            return em.createQuery(
                    "SELECT a FROM Atendimento a " +
                    "WHERE a.especialista.idEspecialista = :idEspecialista " +
                    "AND a.dataHora BETWEEN :inicioDoDia AND :fimDoDia " +
                    "AND a.statusAtendimento != :statusCancelado", Atendimento.class)
                .setParameter("idEspecialista", idEspecialista)
                .setParameter("inicioDoDia", inicioDoDia)
                .setParameter("fimDoDia", fimDoDia)
                .setParameter("statusCancelado", br.com.gestaonotavel.ifsul.model.StatusAtendimento.CANCELADO)
                .getResultList();
        } catch (NoResultException e) {
            return Collections.emptyList();
        } finally {
            em.close();
        }
    }
}
