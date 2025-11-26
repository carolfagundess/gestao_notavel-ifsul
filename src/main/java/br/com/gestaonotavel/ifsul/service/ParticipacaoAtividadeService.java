package br.com.gestaonotavel.ifsul.service;

import br.com.gestaonotavel.ifsul.dao.ParticipacaoAtividadeDAO;
import br.com.gestaonotavel.ifsul.model.Atividade;
import br.com.gestaonotavel.ifsul.model.ParticipacaoAtividade;
import br.com.gestaonotavel.ifsul.model.Responsavel;
import br.com.gestaonotavel.ifsul.util.RegraDeNegocioException;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import br.com.gestaonotavel.ifsul.util.JpaUtil;

public class ParticipacaoAtividadeService {

    private final ParticipacaoAtividadeDAO participacaoDAO;

    public ParticipacaoAtividadeService() {
        this.participacaoDAO = new ParticipacaoAtividadeDAO();
    }

    public void registrarParticipacao(Responsavel responsavel, Atividade atividade, Double horas, String funcao) {
        if (responsavel == null) throw new RegraDeNegocioException("Selecione um responsável.");
        if (atividade == null) throw new RegraDeNegocioException("Selecione uma atividade.");
        if (horas == null || horas <= 0) throw new RegraDeNegocioException("As horas trabalhadas devem ser maiores que zero.");
        if (funcao == null || funcao.trim().isEmpty()) throw new RegraDeNegocioException("Descreva a função desempenhada.");

        if (participacaoDAO.buscarPorResponsavelEAtividade(responsavel, atividade) != null) {
            throw new RegraDeNegocioException("Este responsável já teve participação registrada nesta atividade.");
        }

        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            ParticipacaoAtividade novaParticipacao = new ParticipacaoAtividade(responsavel, atividade, horas, funcao.trim());
            em.persist(novaParticipacao);

            Responsavel responsavelGerenciado = em.find(Responsavel.class, responsavel.getId());
            if (responsavelGerenciado == null) {
                throw new RegraDeNegocioException("Responsável não encontrado no banco de dados.");
            }

            responsavelGerenciado.setHorasVoluntariado(
                    responsavelGerenciado.getHorasVoluntariado() + novaParticipacao.getHorasTrabalhadas()
            );
            responsavelGerenciado.setCreditos(
                    responsavelGerenciado.getCreditos() + novaParticipacao.getCreditosGerados()
            );

            tx.commit();

        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RegraDeNegocioException("Erro ao registrar participação: " + e.getMessage());
        } finally {
            em.close();
        }
    }
}