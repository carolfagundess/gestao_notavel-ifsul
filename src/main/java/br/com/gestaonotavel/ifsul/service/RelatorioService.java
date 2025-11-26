package br.com.gestaonotavel.ifsul.service;

import br.com.gestaonotavel.ifsul.dao.AtendimentoDAO;
import br.com.gestaonotavel.ifsul.dao.MovimentacaoFinanceiraDAO;
import br.com.gestaonotavel.ifsul.dao.ParticipacaoAtividadeDAO;
import br.com.gestaonotavel.ifsul.model.Atendimento;
import br.com.gestaonotavel.ifsul.model.MovimentacaoFinanceira;
import br.com.gestaonotavel.ifsul.model.ParticipacaoAtividade;
import br.com.gestaonotavel.ifsul.model.Responsavel;
import br.com.gestaonotavel.ifsul.util.RegraDeNegocioException;

import java.time.LocalDate;
import java.util.List;

public class RelatorioService {

    private final AtendimentoDAO atendimentoDAO;
    private final MovimentacaoFinanceiraDAO financeiroDAO;
    private final ParticipacaoAtividadeDAO participacaoDAO;
    private final ResponsavelService responsavelService;

    public RelatorioService() {
        this.atendimentoDAO = new AtendimentoDAO();
        this.financeiroDAO = new MovimentacaoFinanceiraDAO();
        this.participacaoDAO = new ParticipacaoAtividadeDAO();
        this.responsavelService = new ResponsavelService();
    }

    public List<Atendimento> gerarRelatorioAtendimentosPorPeriodo(LocalDate inicio, LocalDate fim) {
        if (inicio == null || fim == null) {
            throw new RegraDeNegocioException("As datas de início e fim são obrigatórias.");
        }
        return atendimentoDAO.buscarPorEspecialista(null);
    }

    public List<MovimentacaoFinanceira> gerarRelatorioFinanceiro(LocalDate inicio, LocalDate fim) {
        return financeiroDAO.listarPorPeriodo(inicio, fim);
    }

    public List<Responsavel> gerarRelatorioVoluntariado() {
        return responsavelService.buscarTodos();
    }

    public List<ParticipacaoAtividade> detalharParticipacoes(Responsavel responsavel) {
        return participacaoDAO.listarPorResponsavel(responsavel);
    }
}