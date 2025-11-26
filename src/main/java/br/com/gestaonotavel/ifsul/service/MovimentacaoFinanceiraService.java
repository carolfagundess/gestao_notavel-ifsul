package br.com.gestaonotavel.ifsul.service;

import br.com.gestaonotavel.ifsul.dao.MovimentacaoFinanceiraDAO;
import br.com.gestaonotavel.ifsul.model.MovimentacaoFinanceira;
import br.com.gestaonotavel.ifsul.util.DataChangeManager;
import br.com.gestaonotavel.ifsul.util.RegraDeNegocioException;

import java.time.LocalDate;
import java.util.List;

public class MovimentacaoFinanceiraService {

    private final MovimentacaoFinanceiraDAO dao;

    public MovimentacaoFinanceiraService() {
        this.dao = new MovimentacaoFinanceiraDAO();
    }

    public MovimentacaoFinanceira salvar(MovimentacaoFinanceira movimentacao) {
        if (movimentacao.getValorMovimentacao() == null || movimentacao.getValorMovimentacao() <= 0) {
            throw new RegraDeNegocioException("O valor da movimentação deve ser maior que zero.");
        }
        if (movimentacao.getDataMovimentacao() == null) {
            throw new RegraDeNegocioException("A data da movimentação é obrigatória.");
        }
        if (movimentacao.getTipoMovimento() == null) {
            throw new RegraDeNegocioException("O tipo de movimento (Entrada/Saída) é obrigatório.");
        }
        if (movimentacao.getFormaPagamento() == null) {
            throw new RegraDeNegocioException("A forma de pagamento é obrigatória.");
        }
        if (movimentacao.getObservacao() == null || movimentacao.getObservacao().trim().isEmpty()) {
            throw new RegraDeNegocioException("A descrição/observação é obrigatória.");
        }

        try {
            MovimentacaoFinanceira salvo = dao.salvar(movimentacao);
            DataChangeManager.getInstance().notificarListeners("MovimentacaoFinanceira");
            return salvo;
        } catch (Exception e) {
            throw new RegraDeNegocioException("Erro ao salvar movimentação financeira: " + e.getMessage());
        }
    }

    public List<MovimentacaoFinanceira> listarTodas() {
        return dao.listarTodas();
    }

    public List<MovimentacaoFinanceira> listarPorPeriodo(LocalDate inicio, LocalDate fim) {
        return dao.listarPorPeriodo(inicio, fim);
    }

    public Double calcularSaldoAtual() {
        List<MovimentacaoFinanceira> todas = dao.listarTodas();
        double saldo = 0.0;

        for (MovimentacaoFinanceira m : todas) {
            if (m.getTipoMovimento() != null) {
                switch (m.getTipoMovimento()) {
                    case ENTRADA:
                        saldo += m.getValorMovimentacao();
                        break;
                    case SAIDA:
                        saldo -= m.getValorMovimentacao();
                        break;
                }
            }
        }
        return saldo;
    }
}